package lt.praktika.hotelapp.admin;

import lt.praktika.hotelapp.reservation.ReservationController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.HotelItem;
import lt.praktika.hotelapp.model.RoomTypeItem;

import java.sql.*;
import java.util.*;

public abstract class AdminHomeController extends ReservationController {

    protected abstract ObservableList<HotelItem> loadHotelItems();
    protected abstract ObservableList<RoomTypeItem> loadRoomTypeItems();
    protected abstract void addAvailableRoom(int hotelId, int roomTypeId, String roomNumber, int floor) throws SQLException;

    protected abstract void showRoomTypeManagement();
    protected abstract void showFacilityManagement();
    protected abstract void showCountryManagement();
    protected abstract void showHolidayManagement();
    protected abstract void showSeasonManagement();
    protected abstract void showCompetitorPriceManagement();
    protected abstract void showRoomPriceManagement();
    protected abstract void showAvailabilityManagement();
    protected abstract void showBookingManagement();
    protected abstract void showPaymentManagement();
    protected abstract void showPredictionManagement();
    protected abstract void showBalanceManagement();

    protected void showFullCrudManagement() {
        List<String> tables = Arrays.asList(
                "role", "app_user", "customer", "country", "hotel", "room_type", "room",
                "facility", "hotel_facility", "holiday", "season", "room_price",
                "availability", "competitor_price", "booking", "payment", "price_prediction"
        );

        Label title = new Label("Pilnas CRUD valdymas");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ComboBox<String> tableBox = new ComboBox<>();
        tableBox.getItems().addAll(tables);
        tableBox.setPromptText("Pasirinkite lentelę");
        tableBox.setMaxWidth(Double.MAX_VALUE);

        Button open = adminUiFactory.primaryButton("Atidaryti CRUD");
        Button back = adminUiFactory.neutralButton("Grįžti");
        Label info = new Label("Pasirinkus lentelę galima pridėti, peržiūrėti, koreguoti ir trinti įrašus.");
        info.setWrapText(true);

        open.setOnAction(e -> {
            if (tableBox.getValue() == null) {
                info.setText("Pasirinkite lentelę.");
                return;
            }
            showGenericCrudScreen(tableBox.getValue(), "CRUD: " + tableBox.getValue(), this::showFullCrudManagement);
        });
        back.setOnAction(e -> showAdminPanel());

        VBox box = new VBox(12, title, tableBox, open, back, info);
        box.setPadding(new Insets(20));
        box.setMaxWidth(520);
        stage.setScene(new Scene(centered(box), 800, 560));
    }

    /**
     * Universalus CRUD ekranas. Mygtukai (Pridėti / Atnaujinti / Ištrinti / Išvalyti / Grįžti)
     * dabar yra ryškūs spalvoti ir patalpinti vertikalioje šoninėje juostoje dešinėje pusėje,
     * kad visada būtų matomi nepriklausomai nuo lentelės pločio.
     */
    protected void showGenericCrudScreen(String tableName, String titleText, Runnable backAction) {
        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label info = new Label();
        info.setWrapText(true);
        info.setStyle("-fx-text-fill: #2563eb;");

        TableView<Map<String, String>> table = new TableView<>();
        table.setPlaceholder(new Label("Įrašų nėra. Užpildykite formą ir spauskite „Pridėti“."));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(12));

        Map<String, TextField> fields = new LinkedHashMap<>();
        List<String> columns;
        Set<String> primaryKeys;
        Set<String> autoIncrementColumns;
        try {
            columns = getTableColumns(tableName);
            primaryKeys = getPrimaryKeys(tableName);
            autoIncrementColumns = getAutoIncrementColumns(tableName);
        } catch (SQLException ex) {
            showError("CRUD klaida", ex.getMessage());
            return;
        }

        for (String col : columns) {
            TableColumn<Map<String, String>, String> tc = new TableColumn<>(col);
            tc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getOrDefault(col, "")));
            tc.setPrefWidth(140);
            table.getColumns().add(tc);
        }

        int row = 0;
        for (String col : columns) {
            TextField field = new TextField();
            field.setPromptText(autoIncrementColumns.contains(col) ? col + " (auto)" : col);
            if (autoIncrementColumns.contains(col)) field.setDisable(true);
            fields.put(col, field);
            Label colLabel = new Label(col + (primaryKeys.contains(col) ? " *" : ""));
            colLabel.setStyle("-fx-font-weight: bold;");
            form.add(colLabel, 0, row);
            form.add(field, 1, row);
            GridPane.setHgrow(field, Priority.ALWAYS);
            row++;
        }
        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setMinWidth(140);
        ColumnConstraints inputCol = new ColumnConstraints();
        inputCol.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(labelCol, inputCol);

        // Paieška pagal bet kurį stulpelį.
        TextField searchField = new TextField();
        searchField.setPromptText("Paieška visuose stulpeliuose...");

        ObservableList<Map<String, String>> source = FXCollections.observableArrayList();
        FilteredList<Map<String, String>> filtered = new FilteredList<>(source, item -> true);
        table.setItems(filtered);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String q = newVal == null ? "" : newVal.trim().toLowerCase();
            if (q.isEmpty()) {
                filtered.setPredicate(item -> true);
            } else {
                filtered.setPredicate(item -> {
                    if (item == null) return false;
                    for (String value : item.values()) {
                        if (value != null && value.toLowerCase().contains(q)) return true;
                    }
                    return false;
                });
            }
        });

        Runnable refresh = () -> {
            try {
                source.setAll(loadCrudRows(tableName, columns));
            } catch (SQLException ex) {
                showError("Duomenų įkėlimo klaida", ex.getMessage());
            }
        };

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                for (String col : columns) fields.get(col).setText(selected.getOrDefault(col, ""));
            }
        });

        // Spalvoti, ryškūs šoniniai mygtukai.
        Button add = adminUiFactory.successButton("➕  Pridėti");
        Button update = adminUiFactory.warningButton("✎  Atnaujinti");
        Button delete = adminUiFactory.dangerButton("🗑  Ištrinti");
        Button clear = adminUiFactory.neutralButton("✖  Išvalyti");
        Button reload = adminUiFactory.primaryButton("⟳  Atnaujinti sąrašą");
        Button back = adminUiFactory.neutralButton("←  Grįžti");

        add.setOnAction(e -> {
            try {
                insertCrudRow(tableName, columns, autoIncrementColumns, fields);
                info.setText("Įrašas pridėtas.");
                refresh.run();
            } catch (Exception ex) { showError("Pridėjimo klaida", ex.getMessage()); }
        });

        update.setOnAction(e -> {
            Map<String, String> selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { info.setText("Pasirinkite įrašą atnaujinimui."); return; }
            try {
                updateCrudRow(tableName, columns, primaryKeys, fields, selected);
                info.setText("Įrašas atnaujintas.");
                refresh.run();
            } catch (Exception ex) { showError("Atnaujinimo klaida", ex.getMessage()); }
        });

        delete.setOnAction(e -> {
            Map<String, String> selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { info.setText("Pasirinkite įrašą trynimui."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Patvirtinimas");
            confirm.setHeaderText("Ar tikrai norite ištrinti pasirinktą įrašą?");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
            try {
                deleteCrudRow(tableName, primaryKeys, selected);
                info.setText("Įrašas ištrintas.");
                refresh.run();
                fields.values().forEach(TextInputControl::clear);
            } catch (Exception ex) { showError("Trynimo klaida", ex.getMessage()); }
        });

        clear.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            fields.values().forEach(TextInputControl::clear);
            info.setText("");
        });
        reload.setOnAction(e -> { refresh.run(); info.setText("Sąrašas atnaujintas."); });
        back.setOnAction(e -> backAction.run());

        // Veiksmų sidebar dešinėje pusėje. Mygtukai išsidėstę vertikaliai ir visi matomi.
        Label actionsTitle = new Label("Veiksmai");
        actionsTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        VBox actionBar = new VBox(8, actionsTitle, add, update, delete, clear, reload, new Separator(), back);
        actionBar.setPadding(new Insets(12));
        actionBar.setMinWidth(200);
        actionBar.setMaxWidth(220);
        actionBar.setStyle(
                "-fx-background-color: #f3f4f6;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #d1d5db;"
                        + "-fx-border-radius: 10;"
        );

        ScrollPane formScroll = new ScrollPane(form);
        formScroll.setFitToWidth(true);
        formScroll.setPrefHeight(300);
        formScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Label formTitle = new Label("Įrašo forma");
        formTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        VBox formBox = new VBox(8, formTitle, formScroll, info);
        formBox.setPadding(new Insets(12));
        formBox.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #d1d5db;"
                        + "-fx-border-radius: 10;"
        );
        formBox.setMinWidth(360);
        formBox.setPrefWidth(420);

        // Vidurinis stulpelis: lentelė + paieška virš jos.
        HBox searchRow = new HBox(8, new Label("🔍"), searchField);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        VBox tableBox = new VBox(8, searchRow, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        HBox.setHgrow(tableBox, Priority.ALWAYS);

        HBox body = new HBox(12, tableBox, formBox, actionBar);
        body.setPadding(new Insets(0));

        VBox root = new VBox(14, title, body);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f6fa;");
        VBox.setVgrow(body, Priority.ALWAYS);

        refresh.run();
        stage.setScene(new Scene(root, 1280, 760));
    }

    private List<String> getTableColumns(String tableName) throws SQLException {
        List<String> cols = new ArrayList<>();
        try (Connection c = database.connect(); ResultSet rs = c.getMetaData().getColumns(c.getCatalog(), null, tableName, null)) {
            while (rs.next()) cols.add(rs.getString("COLUMN_NAME"));
        }
        if (cols.isEmpty()) throw new SQLException("Lentelė nerasta arba neturi stulpelių: " + tableName);
        return cols;
    }

    private Set<String> getPrimaryKeys(String tableName) throws SQLException {
        Set<String> keys = new LinkedHashSet<>();
        try (Connection c = database.connect(); ResultSet rs = c.getMetaData().getPrimaryKeys(c.getCatalog(), null, tableName)) {
            while (rs.next()) keys.add(rs.getString("COLUMN_NAME"));
        }
        return keys;
    }

    private Set<String> getAutoIncrementColumns(String tableName) throws SQLException {
        Set<String> auto = new HashSet<>();
        try (Connection c = database.connect(); ResultSet rs = c.getMetaData().getColumns(c.getCatalog(), null, tableName, null)) {
            while (rs.next()) if ("YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"))) auto.add(rs.getString("COLUMN_NAME"));
        }
        return auto;
    }

    private ObservableList<Map<String, String>> loadCrudRows(String tableName, List<String> columns) throws SQLException {
        ObservableList<Map<String, String>> rows = javafx.collections.FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + tableName + " LIMIT 1000";
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (String col : columns) {
                    Object value = rs.getObject(col);
                    row.put(col, value == null ? "" : String.valueOf(value));
                }
                rows.add(row);
            }
        }
        return rows;
    }

    private void setParam(PreparedStatement ps, int idx, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) ps.setNull(idx, Types.NULL);
        else ps.setString(idx, value.trim());
    }

    private void insertCrudRow(String tableName, List<String> columns, Set<String> autoColumns, Map<String, TextField> fields) throws SQLException {
        List<String> insertCols = new ArrayList<>();
        for (String col : columns) if (!autoColumns.contains(col)) insertCols.add(col);
        String placeholders = String.join(",", Collections.nCopies(insertCols.size(), "?"));
        String sql = "INSERT INTO " + tableName + " (" + String.join(",", insertCols) + ") VALUES (" + placeholders + ")";
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            for (String col : insertCols) setParam(ps, i++, fields.get(col).getText());
            ps.executeUpdate();
        }
    }

    private void updateCrudRow(String tableName, List<String> columns, Set<String> primaryKeys, Map<String, TextField> fields, Map<String, String> selected) throws SQLException {
        if (primaryKeys.isEmpty()) throw new SQLException("Negalima atnaujinti: lentelė neturi pirminio rakto.");
        List<String> nonKeys = new ArrayList<>();
        for (String col : columns) if (!primaryKeys.contains(col)) nonKeys.add(col);
        if (nonKeys.isEmpty()) throw new SQLException("Šioje lentelėje visi stulpeliai yra pirminis raktas, todėl koregavimui nėra atskirų laukų. Naudokite pridėjimą arba trynimą.");
        StringJoiner set = new StringJoiner(", ");
        nonKeys.forEach(col -> set.add(col + "=?"));
        StringJoiner where = new StringJoiner(" AND ");
        primaryKeys.forEach(col -> where.add(col + "=?"));
        String sql = "UPDATE " + tableName + " SET " + set + " WHERE " + where;
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            for (String col : nonKeys) setParam(ps, i++, fields.get(col).getText());
            for (String col : primaryKeys) setParam(ps, i++, selected.get(col));
            ps.executeUpdate();
        }
    }

    private void deleteCrudRow(String tableName, Set<String> primaryKeys, Map<String, String> selected) throws SQLException {
        if (primaryKeys.isEmpty()) throw new SQLException("Negalima trinti: lentelė neturi pirminio rakto.");
        StringJoiner where = new StringJoiner(" AND ");
        primaryKeys.forEach(col -> where.add(col + "=?"));
        String sql = "DELETE FROM " + tableName + " WHERE " + where;
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            for (String col : primaryKeys) setParam(ps, i++, selected.get(col));
            ps.executeUpdate();
        }
    }

    @Override
    protected void showAccountantPanel() {
        showAdminPanel();
    }

    private boolean isAccountant() {
        return currentUser != null
                && ("accountant".equalsIgnoreCase(currentUser.roleName())
                || "buhalteris".equalsIgnoreCase(currentUser.roleName()));
    }

    private boolean isReceptionist() {
        return currentUser != null
                && ("receptionist".equalsIgnoreCase(currentUser.roleName())
                || "registratorius".equalsIgnoreCase(currentUser.roleName()));
    }


    protected void showAdminPanel() {
        if (isReceptionist()) {
            showReceptionistPanel();
            return;
        }
        Button logout = new Button("Atsijungti");
        logout.setOnAction(e -> {
            currentUser = null;
            showAuthScreen();
        });

        ComboBox<HotelItem> hotelBox = new ComboBox<>(loadHotelItems());
        hotelBox.setPromptText("Pasirinkite viešbutį");
        hotelBox.setMaxWidth(Double.MAX_VALUE);

        ComboBox<RoomTypeItem> roomTypeBox = new ComboBox<>(loadRoomTypeItems());
        roomTypeBox.setPromptText("Pasirinkite kambario tipą");
        roomTypeBox.setMaxWidth(Double.MAX_VALUE);

        TextField roomNumber = new TextField();
        roomNumber.setPromptText("Kambario numeris");

        TextField floor = new TextField();
        floor.setPromptText("Aukštas");

        Button saveRoom = new Button("Pridėti laisvą kambarį");
        saveRoom.setMaxWidth(Double.MAX_VALUE);
        Label info = new Label();
        info.setWrapText(true);

        saveRoom.setOnAction(e -> {
            if (hotelBox.getValue() == null || roomTypeBox.getValue() == null
                    || roomNumber.getText().trim().isEmpty()
                    || floor.getText().trim().isEmpty()) {
                info.setText("Užpildykite visus kambario laukus.");
                return;
            }

            try {
                addAvailableRoom(
                        hotelBox.getValue().id(),
                        roomTypeBox.getValue().id(),
                        roomNumber.getText(),
                        Integer.parseInt(floor.getText())
                );
                info.setText("Kambarys sėkmingai pridėtas.");
                roomNumber.clear();
                floor.clear();
            } catch (Exception ex) {
                showError("Admin klaida", ex.getMessage());
            }
        });

        GridPane roomForm = new GridPane();
        roomForm.setHgap(12);
        roomForm.setVgap(10);
        roomForm.setPadding(new Insets(14));
        roomForm.add(new Label("Viešbutis:"), 0, 0); roomForm.add(hotelBox, 1, 0);
        roomForm.add(new Label("Kambario tipas:"), 0, 1); roomForm.add(roomTypeBox, 1, 1);
        roomForm.add(new Label("Kambario numeris:"), 0, 2); roomForm.add(roomNumber, 1, 2);
        roomForm.add(new Label("Aukštas:"), 0, 3); roomForm.add(floor, 1, 3);
        roomForm.add(saveRoom, 1, 4);
        roomForm.add(info, 1, 5);
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setMinWidth(135);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setHgrow(Priority.ALWAYS);
        roomForm.getColumnConstraints().addAll(labelColumn, inputColumn);

        TitledPane quickRoomPane = new TitledPane("Greitas laisvo kambario pridėjimas", roomForm);
        quickRoomPane.setCollapsible(true);
        quickRoomPane.setExpanded(true);

        Button roomTypes = adminButton("Atidaryti", this::showRoomTypeManagement);
        Button facilities = adminButton("Atidaryti", this::showFacilityManagement);
        Button countries = adminButton("Atidaryti", this::showCountryManagement);
        Button holidays = adminButton("Atidaryti", this::showHolidayManagement);
        Button seasons = adminButton("Atidaryti", this::showSeasonManagement);
        Button competitorPrices = adminButton("Atidaryti", this::showCompetitorPriceManagement);
        Button roomPrices = adminButton("Atidaryti", this::showRoomPriceManagement);
        Button availability = adminButton("Atidaryti", this::showAvailabilityManagement);
        Button bookings = adminButton("Atidaryti", this::showBookingManagement);
        Button payments = adminButton("Atidaryti", this::showPaymentManagement);
        Button predictions = adminButton("Atidaryti", this::showPredictionManagement);
        Button balances = adminButton("Atidaryti", this::showBalanceManagement);
        Button fullCrud = adminButton("Atidaryti", this::showFullCrudManagement);

        FlowPane grid;
        if (isAccountant()) {
            grid = adminUiFactory.actionGrid(
                    adminUiFactory.card("Mokėjimai", "Pajamos ir mokėjimų sąrašas.", payments),
                    adminUiFactory.card("Balansai", "Klientų balansų papildymas.", balances)
            );
        } else {
            grid = adminUiFactory.actionGrid(
                    adminUiFactory.card("Rezervacijos", "Peržiūra ir statusų keitimas.", bookings),
                    adminUiFactory.card("Mokėjimai", "Pajamos ir mokėjimų sąrašas.", payments),
                    adminUiFactory.card("Balansai", "Klientų balansų papildymas.", balances),
                    adminUiFactory.card("Kainos", "Kambarių tipų kainų valdymas.", roomPrices),
                    adminUiFactory.card("Laisvumas", "Dienos laisvų kambarių kiekiai.", availability),
                    adminUiFactory.card("Prognozės", "Rekomenduojamų kainų generavimas.", predictions),
                    adminUiFactory.card("Kambarių tipai", "Tipai, aprašymai ir talpa.", roomTypes),
                    adminUiFactory.card("Patogumai", "Patogumų kūrimas ir priskyrimas.", facilities),
                    adminUiFactory.card("Šalys", "Šalių, valiutų ir laiko zonų duomenys.", countries),
                    adminUiFactory.card("Šventinės dienos", "Švenčių kainų koeficientai.", holidays),
                    adminUiFactory.card("Sezonai", "Sezoniniai kainų koeficientai.", seasons),
                    adminUiFactory.card("Konkurentų kainos", "Konkurentų kainų įvedimas.", competitorPrices),
                    adminUiFactory.card("Pilnas CRUD", "Visų pagrindinių lentelių įvedimas, peržiūra, koregavimas ir trynimas.", fullCrud)
            );
        }

        Label sectionTitle = new Label("Valdymo moduliai");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        String panelTitle = isAccountant() ? "Buhalterio panelė" : "Admin panelė";
        VBox content = isAccountant()
                ? new VBox(16, adminUiFactory.topBar(panelTitle, logout), sectionTitle, grid)
                : new VBox(16, adminUiFactory.topBar(panelTitle, logout), quickRoomPane, sectionTitle, grid);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f4f6fa;");
        VBox.setVgrow(grid, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        stage.setScene(new Scene(scrollPane, 1050, 720));
    }

    /**
     * Patogesnė registratoriaus panelė. Viršuje rodoma pasveikinimas ir dažniausiai
     * naudojamų rezervacijų statistika (laukia patvirtinimo, šios dienos atvykimai),
     * po to greitos rezervacijos paieškos juosta, o žemiau – aiškios kortelės su
     * pagrindiniais registratoriaus moduliais.
     */
    @Override
    protected void showReceptionistPanel() {
        Button logout = new Button("Atsijungti");
        logout.setOnAction(e -> { currentUser = null; showAuthScreen(); });

        // Pasveikinimas su vartotojo vardu.
        String userName = currentUser != null ? currentUser.username() : "registratoriau";
        Label welcome = new Label("Sveiki, " + userName + "!");
        welcome.setStyle("-fx-font-size: 16px; -fx-text-fill: #374151;");

        // Statistikos kortelės.
        ReceptionistStats stats = loadReceptionistStats();
        VBox pendingCard = adminUiFactory.statCard(
                "Laukia patvirtinimo",
                String.valueOf(stats.pendingBookings),
                "#f59e0b"
        );
        VBox todayArrivalsCard = adminUiFactory.statCard(
                "Šiandienos atvykimai",
                String.valueOf(stats.todayArrivals),
                "#2563eb"
        );
        VBox todayDeparturesCard = adminUiFactory.statCard(
                "Šiandienos išvykimai",
                String.valueOf(stats.todayDepartures),
                "#16a34a"
        );
        VBox totalActiveCard = adminUiFactory.statCard(
                "Aktyvių rezervacijų",
                String.valueOf(stats.activeBookings),
                "#7c3aed"
        );

        HBox statsRow = new HBox(14, pendingCard, todayArrivalsCard, todayDeparturesCard, totalActiveCard);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        // Greita rezervacijos paieška pagal ID arba kliento el. paštą.
        TextField quickSearch = new TextField();
        quickSearch.setPromptText("Įveskite rezervacijos ID arba kliento el. paštą");
        Button quickSearchBtn = adminUiFactory.primaryButton("Ieškoti");
        quickSearchBtn.setMinWidth(120);
        quickSearchBtn.setMaxWidth(120);
        Label quickSearchInfo = new Label();
        quickSearchInfo.setStyle("-fx-text-fill: #2563eb;");

        Runnable doQuickSearch = () -> {
            String q = quickSearch.getText() == null ? "" : quickSearch.getText().trim();
            if (q.isEmpty()) {
                quickSearchInfo.setText("Įveskite paieškos reikšmę.");
                return;
            }
            try {
                String result = quickFindBookingSummary(q);
                if (result == null) {
                    quickSearchInfo.setText("Rezervacija nerasta.");
                } else {
                    showInfo("Rezervacija", result);
                    quickSearchInfo.setText("");
                }
            } catch (SQLException ex) {
                showError("Paieškos klaida", ex.getMessage());
            }
        };
        quickSearchBtn.setOnAction(e -> doQuickSearch.run());
        quickSearch.setOnAction(e -> doQuickSearch.run());

        HBox searchRow = new HBox(8, quickSearch, quickSearchBtn);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(quickSearch, Priority.ALWAYS);

        Label searchPanelTitle = new Label("Greita rezervacijos peržiūra");
        searchPanelTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        VBox searchPanel = new VBox(8, searchPanelTitle, searchRow, quickSearchInfo);
        searchPanel.setPadding(new Insets(14));
        searchPanel.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #d9dee8;"
                        + "-fx-border-radius: 10;"
        );

        // Pagrindinės kortelės su veiksmais.
        Button bookings = adminButton("Atidaryti", this::showBookingManagement);
        Button availability = adminButton("Atidaryti", this::showAvailabilityManagement);
        Button bookingCrud = adminButton("Atidaryti",
                () -> showGenericCrudScreen("booking", "Registratorius: rezervacijų CRUD", this::showReceptionistPanel));
        Button customerCrud = adminButton("Atidaryti",
                () -> showGenericCrudScreen("customer", "Registratorius: klientų CRUD", this::showReceptionistPanel));
        Button roomCrud = adminButton("Atidaryti",
                () -> showGenericCrudScreen("room", "Registratorius: kambarių CRUD", this::showReceptionistPanel));
        Button paymentCrud = adminButton("Atidaryti",
                () -> showGenericCrudScreen("payment", "Registratorius: mokėjimų CRUD", this::showReceptionistPanel));
        Button refresh = adminButton("Atnaujinti", this::showReceptionistPanel);

        FlowPane grid = adminUiFactory.actionGrid(
                adminUiFactory.card("Rezervacijų statusai", "Patvirtinimas ir rezervacijų peržiūra.", bookings),
                adminUiFactory.card("Laisvumas", "Kambarių laisvumo įvedimas ir koregavimas.", availability),
                adminUiFactory.card("Rezervacijų CRUD", "Kurti, peržiūrėti, redaguoti ir trinti rezervacijas.", bookingCrud),
                adminUiFactory.card("Klientų CRUD", "Klientų duomenų įvedimas, koregavimas ir trynimas.", customerCrud),
                adminUiFactory.card("Kambarių CRUD", "Kambarių būsenų ir duomenų valdymas.", roomCrud),
                adminUiFactory.card("Mokėjimų CRUD", "Mokėjimų duomenų registravimas ir koregavimas.", paymentCrud),
                adminUiFactory.card("Atnaujinti rodiklius", "Iš naujo įkrauti viršuje rodomą statistiką.", refresh)
        );

        Label sectionTitle = new Label("Registratoriaus moduliai");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label statsTitle = new Label("Šios dienos apžvalga");
        statsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox content = new VBox(
                16,
                adminUiFactory.topBar("Registratoriaus panelė", logout),
                welcome,
                statsTitle,
                statsRow,
                searchPanel,
                sectionTitle,
                grid
        );
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f4f6fa;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        stage.setScene(new Scene(scrollPane, 1100, 760));
    }

    /** Statistinė informacija registratoriaus pradinei panelei. */
    protected static class ReceptionistStats {
        int pendingBookings;
        int todayArrivals;
        int todayDepartures;
        int activeBookings;
    }

    protected ReceptionistStats loadReceptionistStats() {
        ReceptionistStats s = new ReceptionistStats();
        try (Connection c = database.connect()) {
            countInto(c, "SELECT COUNT(*) FROM booking WHERE LOWER(status) IN ('pending','laukia','laukiama')",
                    v -> s.pendingBookings = v);
            countInto(c, "SELECT COUNT(*) FROM booking WHERE check_in = CURDATE()",
                    v -> s.todayArrivals = v);
            countInto(c, "SELECT COUNT(*) FROM booking WHERE check_out = CURDATE()",
                    v -> s.todayDepartures = v);
            countInto(c, "SELECT COUNT(*) FROM booking WHERE LOWER(status) IN ('confirmed','patvirtinta','active','aktyvi')",
                    v -> s.activeBookings = v);
        } catch (SQLException ignored) {
            // Jei DB struktūra šiek tiek skiriasi, paliekame 0 reikšmes.
        }
        return s;
    }

    private void countInto(Connection c, String sql, java.util.function.IntConsumer setter) {
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) setter.accept(rs.getInt(1));
        } catch (SQLException ignored) {
            // Tyliai praleidžiame, jei stulpelis kitoks.
        }
    }

    /** Greitos rezervacijos paieškos rezultatas tekstine forma. */
    protected String quickFindBookingSummary(String query) throws SQLException {
        String trimmed = query.trim();
        boolean numeric = trimmed.matches("\\d+");
        String sql;
        if (numeric) {
            sql = "SELECT b.booking_id, c.email, c.first_name, c.last_name, b.status, "
                    + "b.check_in, b.check_out "
                    + "FROM booking b JOIN customer c ON c.customer_id = b.customer_id "
                    + "WHERE b.booking_id = ? LIMIT 1";
        } else {
            sql = "SELECT b.booking_id, c.email, c.first_name, c.last_name, b.status, "
                    + "b.check_in, b.check_out "
                    + "FROM booking b JOIN customer c ON c.customer_id = b.customer_id "
                    + "WHERE c.email = ? ORDER BY b.booking_id DESC LIMIT 1";
        }
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (numeric) ps.setInt(1, Integer.parseInt(trimmed));
            else ps.setString(1, trimmed);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return "Rezervacijos ID: " + rs.getInt("booking_id")
                        + "\nKlientas: " + safe(rs.getString("first_name")) + " " + safe(rs.getString("last_name"))
                        + "\nEl. paštas: " + safe(rs.getString("email"))
                        + "\nStatusas: " + safe(rs.getString("status"))
                        + "\nAtvykimas: " + safe(String.valueOf(rs.getDate("check_in")))
                        + "\nIšvykimas: " + safe(String.valueOf(rs.getDate("check_out")));
            }
        }
    }

    private String safe(String value) {
        return (value == null || "null".equals(value)) ? "-" : value;
    }

    protected Button adminButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }
}
