package lt.praktika.hotelapp.admin.pricing;

import lt.praktika.hotelapp.admin.operations.AdminOperationsManagementController;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public abstract class AdminPricingManagementController extends AdminOperationsManagementController {

    protected void addCompetitorPrice(
            int hotelId,
            int roomTypeId,
            String competitorName,
            LocalDate priceDate,
            BigDecimal price,
            String currency
    ) throws SQLException {

        String sql = """
            INSERT INTO competitor_price (
                hotel_id,
                room_type_id,
                date,
                competitor_name,
                price,
                currency
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            ps.setInt(2, roomTypeId);
            ps.setDate(3, Date.valueOf(priceDate));
            ps.setString(4, competitorName.trim());
            ps.setBigDecimal(5, price);
            ps.setString(6, currency.trim().toUpperCase());

            ps.executeUpdate();
        }
    }

    protected void saveRoomPrice(int hotelId, int roomTypeId, BigDecimal price) throws SQLException {
        String checkSql = """
            SELECT price_id
            FROM room_price
            WHERE hotel_id = ? AND room_type_id = ?
            LIMIT 1
            """;

        String insertSql = """
            INSERT INTO room_price (hotel_id, room_type_id, price)
            VALUES (?, ?, ?)
            """;

        String updateSql = """
            UPDATE room_price
            SET price = ?
            WHERE hotel_id = ? AND room_type_id = ?
            """;

        try (Connection c = database.connect()) {

            boolean exists = false;

            try (PreparedStatement check = c.prepareStatement(checkSql)) {
                check.setInt(1, hotelId);
                check.setInt(2, roomTypeId);

                try (ResultSet rs = check.executeQuery()) {
                    exists = rs.next();
                }
            }

            if (exists) {
                try (PreparedStatement update = c.prepareStatement(updateSql)) {
                    update.setBigDecimal(1, price);
                    update.setInt(2, hotelId);
                    update.setInt(3, roomTypeId);
                    update.executeUpdate();
                }
            } else {
                try (PreparedStatement insert = c.prepareStatement(insertSql)) {
                    insert.setInt(1, hotelId);
                    insert.setInt(2, roomTypeId);
                    insert.setBigDecimal(3, price);
                    insert.executeUpdate();
                }
            }
        }
    }

    protected void showCompetitorPriceManagement() {
        Label title = new Label("Konkurentų kainų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ComboBox<HotelItem> hotelBox = new ComboBox<>(loadHotelItems());
        hotelBox.setPromptText("Pasirinkite viešbutį");

        ComboBox<RoomTypeItem> roomTypeBox = new ComboBox<>(loadRoomTypeItems());
        roomTypeBox.setPromptText("Pasirinkite kambario tipą");

        TextField competitorNameField = new TextField();
        competitorNameField.setPromptText("Konkurento pavadinimas");

        DatePicker priceDate = new DatePicker();
        priceDate.setPromptText("Datos pasirinkimas");

        TextField priceField = new TextField();
        priceField.setPromptText("Kaina");

        TextField currencyField = new TextField("EUR");
        currencyField.setPromptText("Valiuta");

        Button save = new Button("Pridėti konkurento kainą");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (hotelBox.getValue() == null
                    || roomTypeBox.getValue() == null
                    || competitorNameField.getText().trim().isEmpty()
                    || priceDate.getValue() == null
                    || priceField.getText().trim().isEmpty()
                    || currencyField.getText().trim().isEmpty()) {
                info.setText("Užpildykite visus laukus.");
                return;
            }

            try {
                addCompetitorPrice(
                        hotelBox.getValue().id(),
                        roomTypeBox.getValue().id(),
                        competitorNameField.getText(),
                        priceDate.getValue(),
                        new BigDecimal(priceField.getText().trim()),
                        currencyField.getText()
                );

                info.setText("Konkurento kaina sėkmingai pridėta.");

                competitorNameField.clear();
                priceDate.setValue(null);
                priceField.clear();

                currencyField.clear();
                currencyField.setText("EUR");

            } catch (Exception ex) {
                showError("Konkurento kainos klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                hotelBox,
                roomTypeBox,
                currencyField,
                competitorNameField,
                priceDate,
                priceField,
                save,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 750, 600));
    }

    protected void showRoomPriceManagement() {
        Label title = new Label("Kainų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ComboBox<HotelItem> hotelBox = new ComboBox<>(loadHotelItems());
        hotelBox.setPromptText("Pasirinkite viešbutį");

        ComboBox<RoomTypeItem> roomTypeBox = new ComboBox<>(loadRoomTypeItems());
        roomTypeBox.setPromptText("Pasirinkite kambario tipą");

        TextField priceField = new TextField();
        priceField.setPromptText("Bazinė kambario kaina");

        Button save = new Button("Išsaugoti kainą");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (hotelBox.getValue() == null
                    || roomTypeBox.getValue() == null
                    || priceField.getText().trim().isEmpty()) {
                info.setText("Užpildykite visus laukus.");
                return;
            }

            try {
                saveRoomPrice(
                        hotelBox.getValue().id(),
                        roomTypeBox.getValue().id(),
                        new BigDecimal(priceField.getText().trim())
                );

                info.setText("Kaina sėkmingai išsaugota.");
                priceField.clear();

            } catch (Exception ex) {
                showError("Kainos klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                hotelBox,
                roomTypeBox,
                priceField,
                save,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 750, 550));
    }

    protected void assignFacilityToHotel(int hotelId, int facilityId) throws SQLException {
        String checkSql = """
            SELECT hotel_id
            FROM hotel_facility
            WHERE hotel_id = ? AND facility_id = ?
            LIMIT 1
            """;

        String insertSql = """
            INSERT INTO hotel_facility (hotel_id, facility_id)
            VALUES (?, ?)
            """;

        try (Connection c = database.connect()) {
            boolean exists;

            try (PreparedStatement check = c.prepareStatement(checkSql)) {
                check.setInt(1, hotelId);
                check.setInt(2, facilityId);

                try (ResultSet rs = check.executeQuery()) {
                    exists = rs.next();
                }
            }

            if (exists) {
                throw new SQLException("Šis patogumas jau priskirtas šiam viešbučiui.");
            }

            try (PreparedStatement insert = c.prepareStatement(insertSql)) {
                insert.setInt(1, hotelId);
                insert.setInt(2, facilityId);
                insert.executeUpdate();
            }
        }
    }
}
