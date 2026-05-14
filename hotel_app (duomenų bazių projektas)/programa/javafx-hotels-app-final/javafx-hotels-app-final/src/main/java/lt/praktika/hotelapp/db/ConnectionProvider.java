package lt.praktika.hotelapp.db;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionProvider {
    Connection connect() throws SQLException;
}
