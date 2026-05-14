package lt.praktika.hotelapp.admin.operations;

import lt.praktika.hotelapp.admin.catalog.AdminCatalogManagementController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public abstract class AdminOperationsManagementController extends AdminCatalogManagementController {

    protected abstract ObservableList<BookingRow> loadBookings();
    protected abstract ObservableList<PaymentRow> loadPayments();
    protected abstract ObservableList<PredictionRow> loadPredictions();
    protected abstract void generatePredictions() throws SQLException;
    protected abstract String getTotalIncome();
    protected abstract void updateBookingStatus(int bookingId, String status) throws SQLException;


    protected void addCountry(
            String countryName,
            String countryCode,
            String currency,
            String timezone
    ) throws SQLException {

        String sql = """
            INSERT INTO country (country_name, country_code, currency, timezone)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, countryName.trim());
            ps.setString(2, countryCode.trim().toUpperCase());
            ps.setString(3, currency.trim().toUpperCase());
            ps.setString(4, timezone.trim());

            ps.executeUpdate();
        }
    }

    protected void addHoliday(
            int countryId,
            String holidayName,
            LocalDate holidayDate,
            BigDecimal impactFactor
    ) throws SQLException {

        String sql = """
            INSERT INTO holiday (
                country_id,
                holiday_name,
                holiday_date,
                is_public_holiday,
                price_multiplier
            )
            VALUES (?, ?, ?, 1, ?)
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, countryId);
            ps.setString(2, holidayName.trim());
            ps.setDate(3, Date.valueOf(holidayDate));
            ps.setBigDecimal(4, impactFactor);

            ps.executeUpdate();
        }
    }

    protected void addSeason(String seasonName, LocalDate startDate, LocalDate endDate, BigDecimal multiplier) throws SQLException {
        String sql = """
            INSERT INTO season (name, start_date, end_date, price_multiplier)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection c = database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, seasonName.trim());
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            ps.setBigDecimal(4, multiplier);

            ps.executeUpdate();
        }
    }

    protected void showBookingManagement() {
        Label title = new Label("Rezervacijų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TableView<BookingRow> table = new TableView<>();

        TableColumn<BookingRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<BookingRow, String> customerCol = new TableColumn<>("Klientas");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        customerCol.setPrefWidth(220);

        TableColumn<BookingRow, String> hotelCol = new TableColumn<>("Viešbutis");
        hotelCol.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        hotelCol.setPrefWidth(180);

        TableColumn<BookingRow, String> statusCol = new TableColumn<>("Statusas");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<BookingRow, Void> actionCol = new TableColumn<>("Veiksmas");
        actionCol.setPrefWidth(160);

        actionCol.setCellFactory(col -> new TableCell<>() {
            protected final Button confirmButton = new Button("Patvirtinti");

            {
                confirmButton.setOnAction(e -> {
                    BookingRow row = getTableView().getItems().get(getIndex());

                    try {
                        updateBookingStatus(row.getBookingId(), "confirmed");
                        table.setItems(loadBookings());

                    } catch (Exception ex) {
                        showError("Rezervacijos klaida", ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : confirmButton);
            }
        });

        table.getColumns().addAll(idCol, customerCol, hotelCol, statusCol, actionCol);
        table.setItems(loadBookings());

        Button back = new Button("Grįžti");
        back.setOnAction(e -> showAdminPanel());

        VBox root = new VBox(15, title, table, back);
        root.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setScene(new Scene(root, 950, 650));
    }

    protected void showPaymentManagement() {
        Label title = new Label("Mokėjimų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label totalIncomeLabel = new Label("Bendros pajamos: " + getTotalIncome() + " €");
        totalIncomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<PaymentRow> table = new TableView<>();

        TableColumn<PaymentRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));

        TableColumn<PaymentRow, Integer> bookingCol = new TableColumn<>("Booking ID");
        bookingCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<PaymentRow, String> amountCol = new TableColumn<>("Suma");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<PaymentRow, String> statusCol = new TableColumn<>("Statusas");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        table.getColumns().addAll(idCol, bookingCol, amountCol, statusCol);
        table.setItems(loadPayments());

        Button back = new Button("Grįžti");
        back.setOnAction(e -> showAdminPanel());

        VBox root = new VBox(15, title, totalIncomeLabel, table, back);
        root.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setScene(new Scene(root, 950, 650));
    }

    protected void showPredictionManagement() {
        Label title = new Label("Kainų prognozės");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TableView<PredictionRow> table = new TableView<>();

        TableColumn<PredictionRow, Integer> hotelIdCol = new TableColumn<>("Hotel ID");
        hotelIdCol.setCellValueFactory(new PropertyValueFactory<>("hotelId"));

        TableColumn<PredictionRow, String> hotelNameCol = new TableColumn<>("Viešbutis");
        hotelNameCol.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        hotelNameCol.setPrefWidth(180);

        TableColumn<PredictionRow, String> basePriceCol = new TableColumn<>("Bazinė kaina");
        basePriceCol.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        TableColumn<PredictionRow, String> competitorCol = new TableColumn<>("Konkurento kaina");
        competitorCol.setCellValueFactory(new PropertyValueFactory<>("competitorPrice"));

        TableColumn<PredictionRow, String> recommendedCol = new TableColumn<>("Rekomenduojama");
        recommendedCol.setCellValueFactory(new PropertyValueFactory<>("recommendedPrice"));

        table.getColumns().addAll(
                hotelIdCol,
                hotelNameCol,
                basePriceCol,
                competitorCol,
                recommendedCol
        );

        table.setItems(loadPredictions());

        Button generate = new Button("Generuoti prognozes");
        Button back = new Button("Grįžti");

        generate.setOnAction(e -> {
            try {
                generatePredictions();
                table.setItems(loadPredictions());
                showInfo("Prognozės", "Prognozės sėkmingai sugeneruotos.");

            } catch (Exception ex) {
                showError("Prognozių klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox root = new VBox(15, title, table, generate, back);
        root.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setScene(new Scene(root, 1050, 700));
    }

    protected void showCountryManagement() {
        Label title = new Label("Šalių valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField countryNameField = new TextField();
        countryNameField.setPromptText("Šalies pavadinimas");

        TextField countryCodeField = new TextField();
        countryCodeField.setPromptText("Country code (pvz. LT)");

        TextField currencyField = new TextField();
        currencyField.setPromptText("Valiuta (pvz. EUR)");

        TextField timezoneField = new TextField();
        timezoneField.setPromptText("Timezone (pvz. Europe/Vilnius)");

        Button save = new Button("Pridėti šalį");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (countryNameField.getText().trim().isEmpty()
                    || countryCodeField.getText().trim().isEmpty()
                    || currencyField.getText().trim().isEmpty()
                    || timezoneField.getText().trim().isEmpty()) {

                info.setText("Užpildykite visus laukus.");
                return;
            }

            try {
                addCountry(
                        countryNameField.getText(),
                        countryCodeField.getText(),
                        currencyField.getText(),
                        timezoneField.getText()
                );

                info.setText("Šalis sėkmingai pridėta.");

                countryNameField.clear();
                countryCodeField.clear();
                currencyField.clear();
                timezoneField.clear();

            } catch (Exception ex) {
                showError("Šalies klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                countryNameField,
                countryCodeField,
                currencyField,
                timezoneField,
                save,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 750, 600));
    }

    protected void showHolidayManagement() {
        Label title = new Label("Šventinių dienų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ComboBox<CountryItem> countryBox = new ComboBox<>(loadCountries());
        countryBox.setPromptText("Pasirinkite šalį");

        TextField holidayNameField = new TextField();
        holidayNameField.setPromptText("Šventės pavadinimas");

        DatePicker holidayDate = new DatePicker();
        holidayDate.setPromptText("Šventės data");

        TextField impactField = new TextField();
        impactField.setPromptText("Kainos koeficientas (pvz. 1.20)");

        Button save = new Button("Pridėti šventinę dieną");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (countryBox.getValue() == null
                    || holidayNameField.getText().trim().isEmpty()
                    || holidayDate.getValue() == null
                    || impactField.getText().trim().isEmpty()) {
                info.setText("Užpildykite visus laukus.");
                return;
            }

            try {
                addHoliday(
                        countryBox.getValue().id(),
                        holidayNameField.getText(),
                        holidayDate.getValue(),
                        new BigDecimal(impactField.getText().trim())
                );

                info.setText("Šventinė diena sėkmingai pridėta.");

                holidayNameField.clear();
                holidayDate.setValue(null);
                impactField.clear();

            } catch (Exception ex) {
                showError("Šventės klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                countryBox,
                holidayNameField,
                holidayDate,
                impactField,
                save,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 750, 600));
    }

    protected void showSeasonManagement() {
        Label title = new Label("Sezonų valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField seasonNameField = new TextField();
        seasonNameField.setPromptText("Sezono pavadinimas");

        DatePicker startDate = new DatePicker();
        startDate.setPromptText("Pradžios data");

        DatePicker endDate = new DatePicker();
        endDate.setPromptText("Pabaigos data");

        TextField multiplierField = new TextField();
        multiplierField.setPromptText("Kainos koeficientas (pvz. 1.30)");

        Button save = new Button("Pridėti sezoną");
        Button back = new Button("Grįžti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (seasonNameField.getText().trim().isEmpty()
                    || startDate.getValue() == null
                    || endDate.getValue() == null
                    || multiplierField.getText().trim().isEmpty()) {
                info.setText("Užpildykite visus laukus.");
                return;
            }

            try {
                addSeason(
                        seasonNameField.getText(),
                        startDate.getValue(),
                        endDate.getValue(),
                        new BigDecimal(multiplierField.getText().trim())
                );

                info.setText("Sezonas sėkmingai pridėtas.");

                seasonNameField.clear();
                startDate.setValue(null);
                endDate.setValue(null);
                multiplierField.clear();

            } catch (Exception ex) {
                showError("Sezono klaida", ex.getMessage());
            }
        });

        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(
                12,
                title,
                seasonNameField,
                startDate,
                endDate,
                multiplierField,
                save,
                back,
                info
        );

        box.setPadding(new Insets(20));
        box.setMaxWidth(500);

        stage.setScene(new Scene(centered(box), 750, 600));
    }
}
