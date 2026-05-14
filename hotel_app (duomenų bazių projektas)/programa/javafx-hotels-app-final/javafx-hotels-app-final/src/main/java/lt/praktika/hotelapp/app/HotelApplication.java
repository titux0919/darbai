package lt.praktika.hotelapp.app;

import lt.praktika.hotelapp.persistence.Database;

import lt.praktika.hotelapp.admin.availability.AdminAvailabilityManagementController;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class HotelApplication extends AdminAvailabilityManagementController {

    public void start(Stage stage) {
        this.stage = stage;
        config = new AppConfig();
        database = new Database(config);
        showHelloScreen();
        Platform.runLater(this::checkDatabaseConnection);
    }

    protected void showHelloScreen() {
        Label helloLabel = new Label("Tikrinamas ryšys su duomenų baze");
        helloLabel.setStyle("-fx-font-size: 34px; -fx-font-weight: bold;");
        statusLabel = new Label("Tikrinamas ryšys su duomenų baze. Prašome palaukti!");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setCenter(helloLabel);
        root.setBottom(statusLabel);
        BorderPane.setAlignment(helloLabel, Pos.CENTER);
        BorderPane.setAlignment(statusLabel, Pos.CENTER);

        stage.setTitle("Viešbučių sistema");
        stage.setScene(new Scene(root, 720, 480));
        stage.show();
    }

    protected void checkDatabaseConnection() {
        try (Connection ignored = database.connect()) {
            ensureDefaultRoles();
            statusLabel.setText("Prisijungta prie DB: " + config.getDatabaseName());
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> showAuthScreen());
            pause.play();
        } catch (SQLException exception) {
            askForDatabaseNameUntilConnected();
        }
    }

    /**
     * Užtikrina, kad role lentelėje būtų visos sistemos rolės su konkrečiais role_id:
     * 1 = admin, 2 = user, 3 = buhalteris, 4 = registratorius.
     *
     * Jeigu role_id = 4 dar nėra — sukuriamas registratoriaus įrašas su tuo ID.
     * Jeigu jau egzistuoja, bet pavadinimas kitoks — pavadinimas atnaujinamas į „registratorius“.
     */
    protected void ensureDefaultRoles() {
        try (Connection c = database.connect()) {
            ensureRoleAtId(c, 1, "admin");
            ensureRoleAtId(c, 2, "user");
            ensureRoleAtId(c, 3, "buhalteris");
            ensureRoleAtId(c, 4, "registratorius");
            ensureDefaultReceptionistUser(c);
        } catch (SQLException ex) {
            // Jei role/app_user lentelių struktūra kitokia, programa veikia toliau su esamais vaidmenimis.
        }
    }

    /**
     * Užtikrina, kad lentelėje role egzistuotų konkretus įrašas: role_id = expectedId, name = roleName.
     * - Jei rolės su tokiu ID nėra, bet rolė tokiu pavadinimu yra (kitokiu ID) — paliekame ją ramybėje
     *   (negalime keisti PK, jei yra FK iš app_user).
     * - Jei rolės su tokiu ID nėra ir pavadinimu nėra — įterpiame su nurodytu ID.
     * - Jei rolė su tokiu ID yra, bet pavadinimas kitoks — atnaujiname pavadinimą.
     */
    protected void ensureRoleAtId(Connection c, int expectedId, String roleName) throws SQLException {
        try (PreparedStatement byId = c.prepareStatement("SELECT name FROM role WHERE role_id=?")) {
            byId.setInt(1, expectedId);
            try (ResultSet rs = byId.executeQuery()) {
                if (rs.next()) {
                    String existing = rs.getString(1);
                    if (existing != null && !existing.equalsIgnoreCase(roleName)) {
                        try (PreparedStatement rename = c.prepareStatement("UPDATE role SET name=? WHERE role_id=?")) {
                            rename.setString(1, roleName);
                            rename.setInt(2, expectedId);
                            rename.executeUpdate();
                        }
                    }
                    return;
                }
            }
        }

        try (PreparedStatement byName = c.prepareStatement("SELECT role_id FROM role WHERE name=? LIMIT 1")) {
            byName.setString(1, roleName);
            try (ResultSet rs = byName.executeQuery()) {
                if (rs.next()) return;
            }
        }

        try (PreparedStatement insert = c.prepareStatement("INSERT INTO role (role_id, name) VALUES (?, ?)")) {
            insert.setInt(1, expectedId);
            insert.setString(2, roleName);
            insert.executeUpdate();
        }
    }

    protected void ensureDefaultReceptionistUser(Connection c) throws SQLException {
        int roleId = 0;
        try (PreparedStatement role = c.prepareStatement("SELECT role_id FROM role WHERE name=? LIMIT 1")) {
            role.setString(1, "registratorius");
            try (ResultSet rs = role.executeQuery()) {
                if (rs.next()) roleId = rs.getInt(1);
            }
        }
        if (roleId == 0) return;

        // Jeigu vartotojas jau buvo sukurtas anksčiau kaip paprastas „user“,
        // privalome jam atnaujinti role_id. Kitaip prisijungus su registratoriumi
        // būtų atidaromas paprasto vartotojo viešbučių paieškos ekranas.
        try (PreparedStatement check = c.prepareStatement("SELECT user_id FROM app_user WHERE username=? OR email=? LIMIT 1")) {
            check.setString(1, "registratorius");
            check.setString(2, "registratorius@hotel.local");
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    try (PreparedStatement update = c.prepareStatement("UPDATE app_user SET role_id=?, username=?, email=? WHERE user_id=?")) {
                        update.setInt(1, roleId);
                        update.setString(2, "registratorius");
                        update.setString(3, "registratorius@hotel.local");
                        update.setInt(4, userId);
                        update.executeUpdate();
                    }
                    return;
                }
            }
        }
        try (PreparedStatement insert = c.prepareStatement("INSERT INTO app_user(role_id, username, email, password_hash) VALUES(?,?,?,?)")) {
            insert.setInt(1, roleId);
            insert.setString(2, "registratorius");
            insert.setString(3, "registratorius@hotel.local");
            insert.setString(4, passwordService.hash("Registratorius123"));
            insert.executeUpdate();
        }
    }

    protected void askForDatabaseNameUntilConnected() {
        while (true) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Ryšio klaida");
            errorAlert.setHeaderText("Patikrinkite ryšį");
            errorAlert.setContentText("Nepavyko prisijungti prie duomenų bazės. Įveskite duomenų bazės pavadinimą.");
            errorAlert.showAndWait();

            TextInputDialog dialog = new TextInputDialog(config.getDatabaseName());
            dialog.setTitle("Duomenų bazė");
            dialog.setHeaderText("Įveskite duomenų bazės pavadinimą");
            dialog.setContentText("DB pavadinimas:");

            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) {
                statusLabel.setText("Ryšys su DB nenustatytas.");
                return;
            }

            String dbName = result.get().trim();
            if (dbName.isEmpty()) {
                showWarning("DB pavadinimas negali būti tuščias.");
                continue;
            }

            config.setDatabaseName(dbName);
            database = new Database(config);
            try (Connection ignored = database.connect()) {
                ensureDefaultRoles();
                config.save();
                statusLabel.setText("Prisijungta prie DB: " + config.getDatabaseName());
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> showAuthScreen());
                pause.play();
                return;
            } catch (SQLException exception) {
                statusLabel.setText("Nepavyko prisijungti prie DB: " + dbName);
            } catch (IOException exception) {
                showWarning("Prisijungta, bet nepavyko išsaugoti pasirinkimo: " + exception.getMessage());
                showAuthScreen();
                return;
            }
        }
    }
}
