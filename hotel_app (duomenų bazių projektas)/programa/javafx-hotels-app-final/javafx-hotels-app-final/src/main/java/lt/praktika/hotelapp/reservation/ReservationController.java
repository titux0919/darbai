package lt.praktika.hotelapp.reservation;

import lt.praktika.hotelapp.user.UserHotelScreens;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public abstract class ReservationController extends UserHotelScreens {

    protected void reserveHotel(HotelRow hotel) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Viešbučio užsakymas");
        dialog.setHeaderText("Užsakymas: " + hotel.getName());
        DatePicker checkIn = new DatePicker();
        checkIn.setPromptText("Pasirinkite check-in datą:");

        DatePicker checkOut = new DatePicker();
        checkOut.setPromptText("Pasirinkite check-out datą:");
        TextField guests = new TextField("1");

        Label priceLabel = new Label("Kaina: -");
        Label balanceLabel = new Label("Jūsų balansas: " + getCustomerBalanceText() + " €");

        Button calculatePriceButton = new Button("Skaičiuoti kainą");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));
        grid.add(new Label("Check-in:"), 0, 0); grid.add(checkIn, 1, 0);
        grid.add(new Label("Check-out:"), 0, 1); grid.add(checkOut, 1, 1);
        grid.add(new Label("Svečiai:"), 0, 2); grid.add(guests, 1, 2);

        grid.add(calculatePriceButton, 1, 3);
        grid.add(priceLabel, 1, 4);
        grid.add(balanceLabel, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        calculatePriceButton.setOnAction(e -> {
            if (checkIn.getValue() == null || checkOut.getValue() == null) {
                showWarning("Pasirinkite check-in ir check-out datas.");
                return;
            }

            try {
                BigDecimal price = calculateBookingPriceForHotel(
                        hotel.getHotelId(),
                        checkIn.getValue(),
                        checkOut.getValue()
                );

                priceLabel.setText("Kaina: " + price + " €");

            } catch (Exception ex) {
                showError("Kainos skaičiavimo klaida", ex.getMessage());
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (checkIn.getValue() == null || checkOut.getValue() == null) {
                showWarning("Užsakymui būtina pasirinkti check-in ir check-out datas.");
                return;
            }
            try {
                createBooking(
                        hotel.getHotelId(),
                        checkIn.getValue(),
                        checkOut.getValue(),
                        Integer.parseInt(guests.getText().trim())
                );
                showInfo("Užsakymas sukurtas", "Užsakymas įrašytas į duomenų bazę.");
            } catch (SQLException ex) {
                showError("Užsakymo klaida", ex.getMessage());
            } catch (Exception ex) {
                showWarning("Patikrinkite užsakymo datas ir svečių skaičių.");
            }
        }
    }

    protected void createBooking(int hotelId, LocalDate checkIn, LocalDate checkOut, int guests) throws SQLException {
        if (!checkOut.isAfter(checkIn)) throw new SQLException("Check-out data turi būti vėlesnė už check-in.");

        try (Connection c = database.connect()) {
            c.setAutoCommit(false);
            try {
                int customerId = getCustomerIdByEmail(c, currentUser.email());
                int roomId = findAvailableRoom(c, hotelId, checkIn, checkOut);
                if (roomId == 0) throw new SQLException("Šiam viešbučiui nerasta laisvo kambario. Patikrinkite room / availability lenteles.");
                BigDecimal currentBalance = getCustomerBalance(c, customerId);
                BigDecimal totalPrice = calculateBookingPriceForHotel(hotelId, checkIn, checkOut);

                if (currentBalance.compareTo(totalPrice) < 0) {
                    throw new SQLException(
                            "Nepakanka balanso. Reikia: " + totalPrice + " €, turite: " + currentBalance + " €"
                    );
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO booking(customer_id, room_id, check_in, check_out, guests_count, total_price, status) VALUES(?,?,?,?,?,?, 'confirmed')",
                        Statement.RETURN_GENERATED_KEYS
                )) {
                    ps.setInt(1, customerId);
                    ps.setInt(2, roomId);
                    ps.setDate(3, Date.valueOf(checkIn));
                    ps.setDate(4, Date.valueOf(checkOut));
                    ps.setInt(5, guests);
                    ps.setBigDecimal(6, totalPrice);
                    ps.executeUpdate();
                    int bookingId;
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        bookingId = keys.getInt(1);
                    }

                    createPayment(c, bookingId, totalPrice);
                    updateCustomerBalance(c, customerId, totalPrice);
                }
                c.commit();
                showUserHomeScreen();
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    protected int findAvailableRoom(Connection c, int hotelId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = """
                SELECT r.room_id FROM room r
                WHERE r.hotel_id = ? AND r.status = 'available'
                  AND NOT EXISTS (
                    SELECT 1 FROM booking b
                    WHERE b.room_id = r.room_id AND b.status IN ('pending','confirmed')
                      AND b.check_in < ? AND b.check_out > ?
                  )
                LIMIT 1
                """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, hotelId);
            ps.setDate(2, Date.valueOf(checkOut));
            ps.setDate(3, Date.valueOf(checkIn));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    protected BigDecimal calculatePrice(Connection c, int hotelId, int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        String sql = "SELECT COALESCE(rp.price, 0) FROM room r LEFT JOIN room_price rp ON rp.hotel_id=r.hotel_id AND rp.room_type_id=r.room_type_id WHERE r.room_id=? AND r.hotel_id=? LIMIT 1";
        BigDecimal price = BigDecimal.ZERO;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setInt(2, hotelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) price = rs.getBigDecimal(1) == null ? BigDecimal.ZERO : rs.getBigDecimal(1);
            }
        }
        return price.multiply(BigDecimal.valueOf(java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut)));
    }


    protected BigDecimal calculateBookingPriceForHotel(
            int hotelId,
            LocalDate checkIn,
            LocalDate checkOut
    ) throws SQLException {

        int roomId = 0;
        int roomTypeId = 0;

        String roomSql = """
        SELECT r.room_id, r.room_type_id
        FROM room r
        JOIN room_price rp
            ON rp.hotel_id = r.hotel_id
           AND rp.room_type_id = r.room_type_id
        WHERE r.hotel_id = ?
          AND r.status = 'available'
        LIMIT 1
        """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(roomSql)) {

            ps.setInt(1, hotelId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                    roomTypeId = rs.getInt("room_type_id");
                }
            }
        }

        if (roomId == 0) {
            throw new SQLException("Nerastas laisvas kambarys.");
        }

        String priceSql = """
            SELECT COALESCE(price, 0)
            FROM room_price
            WHERE hotel_id = ?
              AND room_type_id = ?
            LIMIT 1
            """;

        BigDecimal basePrice = BigDecimal.ZERO;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(priceSql)) {

            ps.setInt(1, hotelId);
            ps.setInt(2, roomTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    basePrice = rs.getBigDecimal(1);
                }
            }
        }

        if (basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("Šiam viešbučiui nėra nustatyta kaina.");
        }

        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

        if (days <= 0) {
            throw new SQLException("Neteisingos datos.");
        }

        BigDecimal total = BigDecimal.ZERO;

        LocalDate currentDate = checkIn;

        try (Connection c = database.connect()) {
            while (currentDate.isBefore(checkOut)) {
                BigDecimal multiplier = getDateMultiplier(c, hotelId, currentDate);

                BigDecimal dayPrice = basePrice.multiply(multiplier);
                total = total.add(dayPrice);

                currentDate = currentDate.plusDays(1);
            }
        }

        return total.setScale(2, java.math.RoundingMode.HALF_UP);
    }


    protected void saveAvailability(
            int hotelId,
            int roomTypeId,
            LocalDate date,
            int availableRooms,
            int totalRooms
    ) throws SQLException {

        String checkSql = """
            SELECT availability_id
            FROM availability
            WHERE hotel_id = ? AND room_type_id = ? AND date = ?
            LIMIT 1
            """;

        String insertSql = """
            INSERT INTO availability (
                hotel_id,
                room_type_id,
                date,
                available_rooms,
                total_rooms
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        String updateSql = """
            UPDATE availability
            SET available_rooms = ?, total_rooms = ?
            WHERE hotel_id = ? AND room_type_id = ? AND date = ?
            """;

        try (Connection c = database.connect()) {
            boolean exists;

            try (PreparedStatement check = c.prepareStatement(checkSql)) {
                check.setInt(1, hotelId);
                check.setInt(2, roomTypeId);
                check.setDate(3, Date.valueOf(date));

                try (ResultSet rs = check.executeQuery()) {
                    exists = rs.next();
                }
            }

            if (exists) {
                try (PreparedStatement update = c.prepareStatement(updateSql)) {
                    update.setInt(1, availableRooms);
                    update.setInt(2, totalRooms);
                    update.setInt(3, hotelId);
                    update.setInt(4, roomTypeId);
                    update.setDate(5, Date.valueOf(date));
                    update.executeUpdate();
                }
            } else {
                try (PreparedStatement insert = c.prepareStatement(insertSql)) {
                    insert.setInt(1, hotelId);
                    insert.setInt(2, roomTypeId);
                    insert.setDate(3, Date.valueOf(date));
                    insert.setInt(4, availableRooms);
                    insert.setInt(5, totalRooms);
                    insert.executeUpdate();
                }
            }
        }
    }

    protected int getCustomerIdByEmail(Connection c, String email) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT customer_id FROM customer WHERE email=? LIMIT 1")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("Pirmiausia užpildykite registracijos duomenis.");
            }
        }
    }

    protected String getCustomerBalanceText() {
        String sql = "SELECT COALESCE(balance, 0) FROM customer WHERE email = ? LIMIT 1";

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, currentUser.email());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1).toPlainString();
                }
            }

        } catch (SQLException ex) {
            return "0.00";
        }

        return "0.00";
    }

    protected BigDecimal getCustomerBalance(Connection c, int customerId) throws SQLException {
        String sql = "SELECT COALESCE(balance, 0) FROM customer WHERE customer_id = ? LIMIT 1";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        }

        return BigDecimal.ZERO;
    }

    protected void updateCustomerBalance(Connection c, int customerId, BigDecimal amount) throws SQLException {
        String sql = """
            UPDATE customer
            SET balance = balance - ?
            WHERE customer_id = ?
            """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, amount);
            ps.setInt(2, customerId);
            ps.executeUpdate();
        }
    }

    protected BigDecimal getDateMultiplier(Connection c, int hotelId, LocalDate date) throws SQLException {
        BigDecimal seasonMultiplier = BigDecimal.ONE;
        BigDecimal holidayMultiplier = BigDecimal.ONE;

        String seasonSql = """
            SELECT COALESCE(MAX(price_multiplier), 1.00)
            FROM season
            WHERE (hotel_id IS NULL OR hotel_id = ?)
              AND ? BETWEEN start_date AND end_date
            """;

        try (PreparedStatement ps = c.prepareStatement(seasonSql)) {
            ps.setInt(1, hotelId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getBigDecimal(1) != null) {
                    seasonMultiplier = rs.getBigDecimal(1);
                }
            }
        }

        String holidaySql = """
            SELECT COALESCE(MAX(h.price_multiplier), 1.00)
            FROM holiday h
            JOIN hotel ht ON ht.country_id = h.country_id
            WHERE ht.hotel_id = ?
              AND h.holiday_date = ?
            """;

        try (PreparedStatement ps = c.prepareStatement(holidaySql)) {
            ps.setInt(1, hotelId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getBigDecimal(1) != null) {
                    holidayMultiplier = rs.getBigDecimal(1);
                }
            }
        }

        return seasonMultiplier.multiply(holidayMultiplier);
    }

    protected void createPayment(Connection c, int bookingId, BigDecimal amount) throws SQLException {
        String sql = """
        INSERT INTO payment (booking_id, amount, payment_method, status)
        VALUES (?, ?, 'card', 'paid')
        """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setBigDecimal(2, amount);
            ps.executeUpdate();
        }
    }
}
