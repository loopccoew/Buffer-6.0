package db;

import java.sql.*;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/p2p_lending";
    private static final String USER = "root";
    private static final String PASSWORD = "Manjiri.27";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}