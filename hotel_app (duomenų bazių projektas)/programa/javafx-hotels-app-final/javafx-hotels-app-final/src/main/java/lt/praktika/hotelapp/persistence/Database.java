package lt.praktika.hotelapp.persistence;

import lt.praktika.hotelapp.app.AppConfig;

import lt.praktika.hotelapp.db.ConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database implements ConnectionProvider {
    private final AppConfig config;

    public Database(AppConfig config) {
        this.config = config;
    }

    
    public Connection connect() throws SQLException {
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName()
                + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";

        return DriverManager.getConnection(url, config.getUser(), config.getPassword());
    }

    public boolean canConnect() {
        try (Connection ignored = connect()) {
            return true;
        } catch (SQLException exception) {
            return false;
        }
    }
}
