package lt.praktika.hotelapp.app;

import lt.praktika.hotelapp.persistence.Database;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lt.praktika.hotelapp.security.PasswordService;
import lt.praktika.hotelapp.ui.AdminUiFactory;
import lt.praktika.hotelapp.ui.DialogService;
import lt.praktika.hotelapp.model.AppUser;
import lt.praktika.hotelapp.model.CountryItem;

import java.sql.*;

public abstract class AppContext extends Application {
    protected AppConfig config;
    protected Database database;
    protected Stage stage;
    protected Label statusLabel;
    protected AppUser currentUser;
    protected final PasswordService passwordService = new PasswordService();
    protected final DialogService dialogs = new DialogService();
    protected final AdminUiFactory adminUiFactory = new AdminUiFactory();

    protected boolean hasValue(String value) {
        return value != null && !value.trim().isEmpty() && !value.trim().equals("-");
    }

    protected ObservableList<CountryItem> loadCountries() {
        ObservableList<CountryItem> countries = FXCollections.observableArrayList();
        try (Connection c = database.connect(); PreparedStatement ps = c.prepareStatement("SELECT country_id, country_name FROM country ORDER BY country_name"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) countries.add(new CountryItem(rs.getInt(1), rs.getString(2)));
        } catch (SQLException ignored) {}
        return countries;
    }

    protected VBox formBox(javafx.scene.Node... nodes) {
        VBox box = new VBox(10, nodes);
        box.setPadding(new Insets(20));
        box.setMaxWidth(430);
        return box;
    }

    protected StackPane centered(javafx.scene.Node node) {
        StackPane pane = new StackPane(node);
        pane.setPadding(new Insets(25));
        return pane;
    }

    protected void showWarning(String message) {
        dialogs.warning(message);
    }

    protected void showInfo(String title, String message) {
        dialogs.info(title, message);
    }

    protected void showError(String title, String message) {
        dialogs.error(title, message);
    }
}
