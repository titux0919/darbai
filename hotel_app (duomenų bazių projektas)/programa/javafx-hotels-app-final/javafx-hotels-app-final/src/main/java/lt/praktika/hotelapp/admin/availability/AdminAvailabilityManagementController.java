package lt.praktika.hotelapp.admin.availability;

import lt.praktika.hotelapp.admin.pricing.AdminPricingManagementController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.*;

import java.sql.*;

public abstract class AdminAvailabilityManagementController extends AdminPricingManagementController {

    protected void showAvailabilityManagement() {
        Label title = new Label("Laisvumo valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ComboBox<HotelItem> hotelBox = new ComboBox<>(loadHotelItems());
        hotelBox.setPromptText("Pasirinkite viešbutį");

        ComboBox<RoomTypeItem> roomTypeBox = new ComboBox<>(loadRoomTypeItems());
        roomTypeBox.setPromptText("Pasirinkite kambario tipą");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Data");

        TextField availableRoomsField = new TextField();
        availableRoomsField.setPromptText("Laisvų kambarių skaičius");

        TextField totalRoomsField = new TextField();
        totalRoomsField.setPromptText("Visų kambarių skaičius");

        Button save = new Button("Išsaugoti laisvumą");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (hotelBox.getValue() == null
                    || roomTypeBox.getValue() == null
                    || datePicker.getValue() == null
                    || availableRoomsField.getText().trim().isEmpty()
                    || totalRoomsField.getText().trim().isEmpty()) {
                info.setText("Užpildykite visus laukus.");
                return;
            }

            try {
                saveAvailability(
                        hotelBox.getValue().id(),
                        roomTypeBox.getValue().id(),
                        datePicker.getValue(),
                        Integer.parseInt(availableRoomsField.getText().trim()),
                        Integer.parseInt(totalRoomsField.getText().trim())
                );

                info.setText("Laisvumas sėkmingai išsaugotas.");
                availableRoomsField.clear();
                totalRoomsField.clear();

            } catch (Exception ex) {
                showError("Laisvumo klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                hotelBox,
                roomTypeBox,
                datePicker,
                availableRoomsField,
                totalRoomsField,
                save,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 750, 600));
    }

    protected ObservableList<BookingRow> loadBookings() {
        ObservableList<BookingRow> bookings = FXCollections.observableArrayList();

        String sql = """
            SELECT b.booking_id,
                   c.email AS customer_email,
                   h.name AS hotel_name,
                   b.status
            FROM booking b
            JOIN customer c ON c.customer_id = b.customer_id
            JOIN room r ON r.room_id = b.room_id
            JOIN hotel h ON h.hotel_id = r.hotel_id
            ORDER BY b.booking_id DESC
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                bookings.add(new BookingRow(
                        rs.getInt("booking_id"),
                        rs.getString("customer_email"),
                        rs.getString("hotel_name"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException ex) {
            showError("Rezervacijų įkėlimo klaida", ex.getMessage());
        }

        return bookings;
    }

    protected ObservableList<PaymentRow> loadPayments() {
        ObservableList<PaymentRow> payments = FXCollections.observableArrayList();

        String sql = """
                SELECT payment_id,
                      booking_id,
                      amount,
                      status
               FROM payment
               ORDER BY payment_id DESC
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(new PaymentRow(
                        rs.getInt("payment_id"),
                        rs.getInt("booking_id"),
                        rs.getBigDecimal("amount").toPlainString(),
                        rs.getString("status")
                ));
            }

        } catch (SQLException ex) {
            showError("Mokėjimų įkėlimo klaida", ex.getMessage());
        }

        return payments;
    }

    protected ObservableList<PredictionRow> loadPredictions() {
        ObservableList<PredictionRow> predictions = FXCollections.observableArrayList();

        String sql = """
            SELECT
                p.hotel_id,
                h.name AS hotel_name,
                COALESCE(p.base_price, 0) AS base_price,
               COALESCE(p.avg_competitor_price, 0) AS competitor_price,
               COALESCE(p.predicted_price, 0) AS recommended_price
            FROM price_prediction p
            JOIN hotel h ON h.hotel_id = p.hotel_id
            ORDER BY h.name
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                predictions.add(new PredictionRow(
                        rs.getInt("hotel_id"),
                        rs.getString("hotel_name"),
                        rs.getBigDecimal("base_price").toPlainString(),
                        rs.getBigDecimal("competitor_price").toPlainString(),
                        rs.getBigDecimal("recommended_price").toPlainString()
                ));
            }

        } catch (SQLException ex) {
            showError("Prognozių įkėlimo klaida", ex.getMessage());
        }

        return predictions;
    }

    protected void generatePredictions() throws SQLException {
        String clearSql = "DELETE FROM price_prediction";

        String insertSql = """
                INSERT INTO price_prediction (
                 hotel_id,
                 room_type_id,
                 target_date,
                 base_price,
                 avg_competitor_price,
                 predicted_price
             )
            SELECT
                   v.hotel_id,
                   v.room_type_id,
                   CURDATE(),
                   v.base_price,
                   v.avg_competitor_price,
                   ROUND(
                       (v.base_price + COALESCE(v.avg_competitor_price, v.base_price)) / 2,
                       2
                   )
            FROM v_price_prediction_inputs v
            """;

        try (Connection c = database.connect()) {
            c.setAutoCommit(false);

            try {
                try (PreparedStatement clear = c.prepareStatement(clearSql)) {
                    clear.executeUpdate();
                }

                try (PreparedStatement insert = c.prepareStatement(insertSql)) {
                    insert.executeUpdate();
                }

                c.commit();

            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    protected String getTotalIncome() {
        String sql = """
            SELECT COALESCE(SUM(amount), 0)
            FROM payment
            WHERE status = 'paid'
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal(1).toPlainString();
            }

        } catch (SQLException ex) {
            return "0.00";
        }

        return "0.00";
    }

    protected void updateBookingStatus(int bookingId, String status) throws SQLException {
        String sql = """
            UPDATE booking
            SET status = ?
            WHERE booking_id = ?
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    protected ObservableList<HotelItem> loadHotelItems() {
        ObservableList<HotelItem> hotels = FXCollections.observableArrayList();

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement("SELECT hotel_id, name FROM hotel ORDER BY name");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                hotels.add(new HotelItem(rs.getInt("hotel_id"), rs.getString("name")));
            }

        } catch (SQLException ex) {
            showError("Viešbučių įkėlimo klaida", ex.getMessage());
        }

        return hotels;
    }

    protected ObservableList<RoomTypeItem> loadRoomTypeItems() {
        ObservableList<RoomTypeItem> roomTypes = FXCollections.observableArrayList();

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement("SELECT room_type_id, name FROM room_type ORDER BY name");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roomTypes.add(new RoomTypeItem(rs.getInt("room_type_id"), rs.getString("name")));
            }

        } catch (SQLException ex) {
            showError("Kambarių tipų įkėlimo klaida", ex.getMessage());
        }

        return roomTypes;
    }

    protected void addAvailableRoom(int hotelId, int roomTypeId, String roomNumber, int floor) throws SQLException {
        String sql = """
            INSERT INTO room (hotel_id, room_type_id, room_number, floor, status)
            VALUES (?, ?, ?, ?, 'available')
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            ps.setInt(2, roomTypeId);
            ps.setString(3, roomNumber.trim());
            ps.setInt(4, floor);
            ps.executeUpdate();
        }
    }
}
