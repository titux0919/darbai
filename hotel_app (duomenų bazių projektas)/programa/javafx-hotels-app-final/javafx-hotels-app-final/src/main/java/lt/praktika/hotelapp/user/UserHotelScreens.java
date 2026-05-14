package lt.praktika.hotelapp.user;

import lt.praktika.hotelapp.auth.AuthController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.*;

import java.sql.*;
import java.time.LocalDate;

public abstract class UserHotelScreens extends AuthController {

    protected abstract void reserveHotel(HotelRow hotel);

    protected abstract String getCustomerBalanceText();


    protected void showUserHomeScreen() {
        Label welcome = new Label("Sveiki " + currentUser.username());
        welcome.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label searchTitle = new Label("Viešbučių paieška");
        searchTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label balanceLabel = new Label("Balansas: " + getCustomerBalanceText() + " €");
        balanceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField name = dashField("Pavadinimas");
        ComboBox<String> stars = new ComboBox<>(FXCollections.observableArrayList("-", "1", "2", "3", "4", "5"));
        stars.getSelectionModel().select("-");
        TextField checkIn = dashField("Check-in, pvz. 2026-05-01");
        TextField checkOut = dashField("Check-out, pvz. 2026-05-05");
        Button search = new Button("Ieškoti");

        Button logout = new Button("Atsijungti");

        TableView<HotelRow> table = createHotelTable();
        search.setOnAction(e -> table.setItems(loadHotels(name.getText(), stars.getValue(), checkIn.getText(), checkOut.getText())));
        logout.setOnAction(e -> {
            currentUser = null;
            showAuthScreen();
        });
        table.setItems(loadHotels("-", "-", "-", "-"));

        GridPane filters = new GridPane();
        filters.setHgap(10); filters.setVgap(8);
        filters.add(new Label("Pavadinimas:"), 0, 0); filters.add(name, 1, 0);
        filters.add(new Label("Žvaigždutės:"), 2, 0); filters.add(stars, 3, 0);
        filters.add(new Label("Check-in:"), 0, 1); filters.add(checkIn, 1, 1);
        filters.add(new Label("Check-out:"), 2, 1); filters.add(checkOut, 3, 1);

        filters.add(search, 4, 1);
        filters.add(logout, 5, 1);

        VBox root = new VBox(14, welcome, balanceLabel, searchTitle, filters, table);
        root.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);
        stage.setScene(new Scene(root, 980, 650));
    }

    protected TextField dashField(String prompt) {
        TextField field = new TextField("-");
        field.setPromptText(prompt);
        field.focusedProperty().addListener((obs, oldValue, focused) -> {
            if (focused && field.getText().trim().equals("-")) field.clear();
            if (!focused && field.getText().trim().isEmpty()) field.setText("-");
        });
        return field;
    }

    protected TableView<HotelRow> createHotelTable() {
        TableView<HotelRow> table = new TableView<>();
        TableColumn<HotelRow, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        TableColumn<HotelRow, String> hotelName = new TableColumn<>("Pavadinimas");
        hotelName.setCellValueFactory(new PropertyValueFactory<>("name")); hotelName.setPrefWidth(190);
        TableColumn<HotelRow, String> city = new TableColumn<>("Miestas");
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        TableColumn<HotelRow, String> address = new TableColumn<>("Adresas");
        address.setCellValueFactory(new PropertyValueFactory<>("address")); address.setPrefWidth(220);
        TableColumn<HotelRow, Integer> stars = new TableColumn<>("Žvaigždutės");
        stars.setCellValueFactory(new PropertyValueFactory<>("starRating"));
        TableColumn<HotelRow, String> facilities = new TableColumn<>("Patogumai");
        facilities.setCellValueFactory(new PropertyValueFactory<>("facilities")); facilities.setPrefWidth(180);
        TableColumn<HotelRow, Void> action = new TableColumn<>("Veiksmas");
        action.setPrefWidth(110);
        action.setCellFactory(col -> new TableCell<>() {
            private final Button button = new Button("Užsakyti");
            {
                button.setOnAction(e -> reserveHotel(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });
        table.getColumns().addAll(id, hotelName, city, address, stars, facilities, action);
        return table;
    }

    protected ObservableList<HotelRow> loadHotels(String name, String stars, String checkIn, String checkOut) {
        ObservableList<HotelRow> hotels = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("""
                SELECT h.hotel_id, h.name, h.city, h.address, h.star_rating,
                       COALESCE(GROUP_CONCAT(DISTINCT f.name SEPARATOR ', '), '-') AS facilities
                FROM hotel h
                LEFT JOIN hotel_facility hf ON hf.hotel_id = h.hotel_id
                LEFT JOIN facility f ON f.facility_id = hf.facility_id
                WHERE 1=1
                """);
        boolean useName = hasValue(name);
        boolean useStars = hasValue(stars);
        boolean useDates = hasValue(checkIn) && hasValue(checkOut);
        if (useName) sql.append(" AND h.name LIKE ? ");
        if (useStars) sql.append(" AND h.star_rating = ? ");
        if (useDates) sql.append(" AND EXISTS (SELECT 1 FROM availability a WHERE a.hotel_id = h.hotel_id AND a.date >= ? AND a.date < ? AND a.available_rooms > 0) ");
        sql.append(" GROUP BY h.hotel_id, h.name, h.city, h.address, h.star_rating ORDER BY h.name");

        try (Connection connection = database.connect(); PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int i = 1;
            if (useName) ps.setString(i++, "%" + name.trim() + "%");
            if (useStars) ps.setInt(i++, Integer.parseInt(stars.trim()));
            if (useDates) {
                ps.setDate(i++, Date.valueOf(LocalDate.parse(checkIn.trim())));
                ps.setDate(i++, Date.valueOf(LocalDate.parse(checkOut.trim())));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hotels.add(new HotelRow(rs.getInt("hotel_id"), rs.getString("name"), rs.getString("city"),
                            rs.getString("address"), rs.getInt("star_rating"), rs.getString("facilities")));
                }
            }
        } catch (Exception ex) {
            showError("Viešbučių paieškos klaida", ex.getMessage());
        }
        return hotels;
    }

    protected ObservableList<FacilityItem> loadFacilityItems() {
        ObservableList<FacilityItem> facilities = FXCollections.observableArrayList();

        String sql = """
            SELECT facility_id, name
            FROM facility
            ORDER BY name
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                facilities.add(new FacilityItem(
                        rs.getInt("facility_id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException ex) {
            showError("Patogumų įkėlimo klaida", ex.getMessage());
        }

        return facilities;
    }

    protected ObservableList<CustomerItem> loadCustomerItems() {
        ObservableList<CustomerItem> customers = FXCollections.observableArrayList();

        String sql = """
            SELECT customer_id,
                   email,
                   CONCAT(first_name, ' ', last_name) AS full_name,
                   COALESCE(balance, 0) AS balance
            FROM customer
            ORDER BY first_name, last_name
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(new CustomerItem(
                        rs.getInt("customer_id"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getBigDecimal("balance")
                ));
            }

        } catch (SQLException ex) {
            showError("Vartotojų įkėlimo klaida", ex.getMessage());
        }

        return customers;
    }
}
