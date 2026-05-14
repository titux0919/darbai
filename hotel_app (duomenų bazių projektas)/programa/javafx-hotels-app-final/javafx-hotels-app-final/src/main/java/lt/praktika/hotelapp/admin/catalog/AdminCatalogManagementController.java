package lt.praktika.hotelapp.admin.catalog;

import lt.praktika.hotelapp.admin.AdminHomeController;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.*;

import java.math.BigDecimal;
import java.sql.*;

public abstract class AdminCatalogManagementController extends AdminHomeController {

    protected abstract void assignFacilityToHotel(int hotelId, int facilityId) throws SQLException;


    protected void addBalanceToCustomer(String email, BigDecimal amount) throws SQLException {
        String sql = """
            UPDATE customer
            SET balance = balance + ?
            WHERE email = ?
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBigDecimal(1, amount);
            ps.setString(2, email.trim());

            int updated = ps.executeUpdate();

            if (updated == 0) {
                throw new SQLException("Vartotojas su tokiu el. paštu nerastas.");
            }
        }
    }

    protected void addRoomType(String name, String description, int baseCapacity) throws SQLException {
        String sql = """
            INSERT INTO room_type (name, description, base_capacity)
            VALUES (?, ?, ?)
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, description.trim());
            ps.setInt(3, baseCapacity);

            ps.executeUpdate();
        }
    }

    protected void showBalanceManagement() {
        Label title = new Label("Vartotojų balansų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ComboBox<CustomerItem> customerBox = new ComboBox<>(loadCustomerItems());
        customerBox.setPromptText("Pasirinkite vartotoją");

        Label currentBalanceLabel = new Label("Dabartinis balansas: -");

        TextField amountField = new TextField();
        amountField.setPromptText("Papildymo suma");

        Button addBalance = new Button("Papildyti balansą");
        Button back = new Button("Grįžti");
        Label info = new Label();

        customerBox.setOnAction(e -> {
            CustomerItem selected = customerBox.getValue();
            if (selected != null) {
                currentBalanceLabel.setText("Dabartinis balansas: " + selected.balance() + " €");
            }
        });

        addBalance.setOnAction(e -> {
            if (customerBox.getValue() == null || amountField.getText().trim().isEmpty()) {
                info.setText("Pasirinkite vartotoją ir įveskite sumą.");
                return;
            }

            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());

                addBalanceToCustomerById(customerBox.getValue().id(), amount);

                info.setText("Balansas sėkmingai papildytas.");
                amountField.clear();

                customerBox.setItems(loadCustomerItems());
                customerBox.getSelectionModel().clearSelection();
                currentBalanceLabel.setText("Dabartinis balansas: -");

            } catch (Exception ex) {
                showError("Balanso klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                customerBox,
                currentBalanceLabel,
                amountField,
                addBalance,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(550);

        stage.setScene(new Scene(centered(box), 750, 550));
    }

    protected void showFacilityManagement() {
        Label title = new Label("Patogumų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField facilityNameField = new TextField();
        facilityNameField.setPromptText("Patogumo pavadinimas");

        ComboBox<HotelItem> hotelBox = new ComboBox<>(loadHotelItems());
        hotelBox.setPromptText("Pasirinkite viešbutį");

        ComboBox<FacilityItem> facilityBox = new ComboBox<>(loadFacilityItems());
        facilityBox.setPromptText("Pasirinkite patogumą");

        Button assign = new Button("Priskirti patogumą viešbučiui");

        Button save = new Button("Pridėti patogumą");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (facilityNameField.getText().trim().isEmpty()) {
                info.setText("Įveskite patogumo pavadinimą.");
                return;
            }

            try {
                addFacility(facilityNameField.getText());

                info.setText("Patogumas sėkmingai pridėtas.");
                facilityNameField.clear();

            } catch (Exception ex) {
                showError("Patogumo klaida", ex.getMessage());
            }
        });

        assign.setOnAction(e -> {
            if (hotelBox.getValue() == null || facilityBox.getValue() == null) {
                info.setText("Pasirinkite viešbutį ir patogumą.");
                return;
            }

            try {
                assignFacilityToHotel(
                        hotelBox.getValue().id(),
                        facilityBox.getValue().id()
                );

                info.setText("Patogumas priskirtas viešbučiui.");

            } catch (Exception ex) {
                showError("Priskyrimo klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                facilityNameField,
                save,
                hotelBox,
                facilityBox,
                assign,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 700, 450));
    }

    protected void addFacility(String facilityName) throws SQLException {
        String sql = """
            INSERT INTO facility (name)
            VALUES (?)
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, facilityName.trim());
            ps.executeUpdate();
        }
    }

    protected void showRoomTypeManagement() {
        Label title = new Label("Kambarių tipų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Kambario tipo pavadinimas");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Aprašymas");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Vietų skaičius");

        Button save = new Button("Pridėti kambario tipą");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (nameField.getText().trim().isEmpty()
                    || descriptionField.getText().trim().isEmpty()
                    || capacityField.getText().trim().isEmpty()) {
                info.setText("Užpildykite visus laukus.");
                return;
            }

            try {
                addRoomType(
                        nameField.getText(),
                        descriptionField.getText(),
                        Integer.parseInt(capacityField.getText())
                );

                info.setText("Kambario tipas sėkmingai pridėtas.");

                nameField.clear();
                descriptionField.clear();
                capacityField.clear();

            } catch (Exception ex) {
                showError("Kambario tipo klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                nameField,
                descriptionField,
                capacityField,
                save,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 750, 550));
    }

    protected void addBalanceToCustomerById(int customerId, BigDecimal amount) throws SQLException {
        String sql = """
            UPDATE customer
            SET balance = COALESCE(balance, 0) + ?
            WHERE customer_id = ?
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBigDecimal(1, amount);
            ps.setInt(2, customerId);

            int updated = ps.executeUpdate();

            if (updated == 0) {
                throw new SQLException("Vartotojas nerastas.");
            }
        }
    }
}