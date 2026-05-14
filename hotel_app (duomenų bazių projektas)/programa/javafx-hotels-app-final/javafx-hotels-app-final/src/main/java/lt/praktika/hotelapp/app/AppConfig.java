package lt.praktika.hotelapp.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AppConfig {
    private static final String APP_DIR = ".javafx-hotel-start";
    private static final String CONFIG_FILE = "config.properties";

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DATABASE = "db_praktika_2026";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private final Properties properties = new Properties();
    private final Path configPath;

    public AppConfig() {
        Path appDirectory = Path.of(System.getProperty("user.home"), APP_DIR);
        this.configPath = appDirectory.resolve(CONFIG_FILE);
        load();
    }

    private void load() {
        setDefaults();

        if (Files.exists(configPath)) {
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                properties.load(inputStream);
            } catch (IOException ignored) {
                setDefaults();
            }
        }
    }

    private void setDefaults() {
        properties.setProperty("db.host", DEFAULT_HOST);
        properties.setProperty("db.port", DEFAULT_PORT);
        properties.setProperty("db.name", DEFAULT_DATABASE);
        properties.setProperty("db.user", DEFAULT_USER);
        properties.setProperty("db.password", DEFAULT_PASSWORD);
    }

    public String getHost() {
        return properties.getProperty("db.host", DEFAULT_HOST);
    }

    public String getPort() {
        return properties.getProperty("db.port", DEFAULT_PORT);
    }

    public String getDatabaseName() {
        return properties.getProperty("db.name", DEFAULT_DATABASE);
    }

    public String getUser() {
        return properties.getProperty("db.user", DEFAULT_USER);
    }

    public String getPassword() {
        return properties.getProperty("db.password", DEFAULT_PASSWORD);
    }

    public void setDatabaseName(String databaseName) {
        properties.setProperty("db.name", databaseName == null ? "" : databaseName.trim());
    }

    public void save() throws IOException {
        Files.createDirectories(configPath.getParent());
        try (OutputStream outputStream = Files.newOutputStream(configPath)) {
            properties.store(outputStream, "JavaFX Hotel project settings");
        }
    }

    public String getConfigLocation() {
        return configPath.toString();
    }
}
