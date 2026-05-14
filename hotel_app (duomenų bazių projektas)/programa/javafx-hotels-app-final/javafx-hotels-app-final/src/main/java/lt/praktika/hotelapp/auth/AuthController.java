package lt.praktika.hotelapp.auth;

import lt.praktika.hotelapp.app.AppContext;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lt.praktika.hotelapp.model.AppUser;
import lt.praktika.hotelapp.model.CountryItem;

import java.sql.*;

public abstract class AuthController extends AppContext {
    /**
     * Implemented by the concrete application/navigation layer.
     */
    protected abstract void showAdminPanel();
    /**
     * Implemented by the concrete application/navigation layer.
     */
    protected abstract void showAccountantPanel();
    /** Registratoriaus panelė kasdienėms rezervacijų operacijoms. */
    protected abstract void showReceptionistPanel();

    /**
     * Implemented by the concrete application/navigation layer.
     */
    protected abstract void showUserHomeScreen();


    protected void showAuthScreen() {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(new Tab("Prisijungimas", createLoginPane()));
        tabs.getTabs().add(new Tab("Registracija", createRegisterPane()));
        tabs.getTabs().forEach(tab -> tab.setClosable(false));
        BorderPane root = new BorderPane(tabs);
        root.setPadding(new Insets(20));
        stage.setScene(new Scene(root, 720, 520));
    }

    protected Pane createLoginPane() {
        TextField usernameOrEmail = new TextField();
        usernameOrEmail.setPromptText("Naudotojo vardas arba el. paštas");
        PasswordField password = new PasswordField();
        password.setPromptText("Slaptažodis");
        Button login = new Button("Prisijungti");
        login.setDefaultButton(true);
        Label info = new Label();

        login.setOnAction(e -> {
            try {
                AppUser user = login(usernameOrEmail.getText(), password.getText());
                if (user == null) {
                    info.setText("Neteisingi prisijungimo duomenys.");
                    return;
                }
                currentUser = user;

                if ("admin".equalsIgnoreCase(user.roleName())) {
                    showAdminPanel();
                    return;
                }

                if ("accountant".equalsIgnoreCase(user.roleName())
                        || "buhalteris".equalsIgnoreCase(user.roleName())) {
                    showAccountantPanel();
                    return;
                }

                if ("receptionist".equalsIgnoreCase(user.roleName())
                        || "registratorius".equalsIgnoreCase(user.roleName())) {
                    showReceptionistPanel();
                    return;
                }

                // Saugiklis: paprasto vartotojo ekranas rodomas tik rolei „user“.
                // Taip registratorius / buhalteris / admin niekada nepatenka į kliento viešbučių paiešką,
                // net jei DB rolės pavadinimas parašytas netiksliai arba buvo migracijos klaida.
                if ("user".equalsIgnoreCase(user.roleName())) {
                    if (!customerExists(user.email())) {
                        showCustomerDetailsScreen(user);
                    } else {
                        showUserHomeScreen();
                    }
                    return;
                }

                showWarning("Neatpažinta rolė: " + user.roleName());
                currentUser = null;
                showAuthScreen();
            } catch (SQLException ex) {
                showError("Prisijungimo klaida", ex.getMessage());
            }
        });

        VBox box = formBox(new Label("Prisijungimas"), usernameOrEmail, password, login, info);
        return centered(box);
    }

    protected Pane createRegisterPane() {
        TextField username = new TextField();
        username.setPromptText("Naudotojo vardas");
        TextField email = new TextField();
        email.setPromptText("El. paštas");
        PasswordField password = new PasswordField();
        password.setPromptText("Slaptažodis");
        PasswordField repeat = new PasswordField();
        repeat.setPromptText("Pakartoti slaptažodį");
        Button register = new Button("Registruotis");
        Label info = new Label();

        register.setOnAction(e -> {
            if (username.getText().trim().isEmpty() || email.getText().trim().isEmpty()
                    || password.getText().isEmpty() || repeat.getText().isEmpty()) {
                info.setText("Užpildykite visus registracijos laukus.");
                return;
            }
            if (!password.getText().equals(repeat.getText())) {
                info.setText("Slaptažodžiai nesutampa.");
                return;
            }
            try {
                AppUser user = registerUser(username.getText(), email.getText(), password.getText());
                currentUser = user;
                showCustomerDetailsScreen(user);
            } catch (SQLIntegrityConstraintViolationException ex) {
                info.setText("Toks naudotojas arba el. paštas jau yra.");
            } catch (SQLException ex) {
                showError("Registracijos klaida", ex.getMessage());
            }
        });

        VBox box = formBox(new Label("Registracija"), username, email, password, repeat, register, info);
        return centered(box);
    }

    protected void showCustomerDetailsScreen(AppUser user) {
        Label title = new Label("Jei norite toliau naudotis sistema, prašome užpildyti registracijos duomenis.");
        title.setWrapText(true);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField firstName = new TextField(); firstName.setPromptText("Vardas");
        TextField lastName = new TextField(); lastName.setPromptText("Pavardė");
        TextField email = new TextField(user.email()); email.setPromptText("El. paštas");
        TextField phone = new TextField(); phone.setPromptText("Telefonas");
        ComboBox<CountryItem> country = new ComboBox<>(loadCountries());
        country.setPromptText("Šalis");
        if (!country.getItems().isEmpty()) country.getSelectionModel().selectFirst();
        Button save = new Button("Išsaugoti");
        Label info = new Label();

        save.setOnAction(e -> {
            if (firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
                info.setText("Vardas, pavardė ir el. paštas yra privalomi.");
                return;
            }
            try {
                saveCustomer(country.getValue(), firstName.getText(), lastName.getText(), email.getText(), phone.getText());
                showUserHomeScreen();
            } catch (SQLException ex) {
                showError("Duomenų išsaugojimo klaida", ex.getMessage());
            }
        });

        VBox box = formBox(title, firstName, lastName, email, phone, country, save, info);
        stage.setScene(new Scene(centered(box), 720, 520));
    }

    protected AppUser login(String login, String password) throws SQLException {
        String sql = """
    SELECT u.user_id, u.username, u.email, u.password_hash, r.name AS role_name
    FROM app_user u
    JOIN role r ON r.role_id = u.role_id
    WHERE u.username = ? OR u.email = ?
    LIMIT 1
    """;
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login.trim()); ps.setString(2, login.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String hash = rs.getString("password_hash");
                boolean ok = passwordService.matches(password, hash);
                if (!ok) return null;
                if (passwordService.needsUpgrade(hash)) upgradePassword(c, rs.getInt("user_id"), password);
                return new AppUser(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role_name")
                );
            }
        }
    }

    protected void upgradePassword(Connection c, int userId, String password) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE app_user SET password_hash=? WHERE user_id=?")) {
            ps.setString(1, passwordService.hash(password));
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    protected AppUser registerUser(String username, String email, String password) throws SQLException {
        int roleId = getUserRoleId();
        String sql = "INSERT INTO app_user(role_id, username, email, password_hash) VALUES(?,?,?,?)";
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, roleId);
            ps.setString(2, username.trim());
            ps.setString(3, email.trim());
            ps.setString(4, passwordService.hash(password));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new AppUser(
                        keys.getInt(1),
                        username.trim(),
                        email.trim(),
                        "user"
                );
            }
        }
    }

    protected int getUserRoleId() throws SQLException {
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement("SELECT role_id FROM role WHERE name='user' LIMIT 1"); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 3;
        }
    }

    protected boolean customerExists(String email) throws SQLException {
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement("SELECT customer_id FROM customer WHERE email=? LIMIT 1")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    protected void saveCustomer(CountryItem country, String firstName, String lastName, String email, String phone) throws SQLException {
        String sql = "INSERT INTO customer(country_id, first_name, last_name, email, phone) VALUES(?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE country_id=VALUES(country_id), first_name=VALUES(first_name), last_name=VALUES(last_name), phone=VALUES(phone)";
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (country == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, country.id());
            ps.setString(2, firstName.trim()); ps.setString(3, lastName.trim()); ps.setString(4, email.trim()); ps.setString(5, phone.trim());
            ps.executeUpdate();
        }
    }
}
