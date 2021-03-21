package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSingleton {
    private static DBSingleton instance;
    private Connection connection;

    private void connect() throws SQLException {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/mymori?user=dev&password=dev";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new SQLException("Failed connection!");
        }
    }

    Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed())
            connect();
        return connection;
    }

    private DBSingleton() {
    }

    public static DBSingleton getInstance() {
        if (instance == null) {
            instance = new DBSingleton();
        }
        return instance;
    }
}
