package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FraudDAO {
    private Connection connection;

    public FraudDAO(Connection connection) {
        this.connection = connection;
    }

    // Original method
    public void addToFraudList(String borrowerName) throws SQLException {
        String query = "INSERT INTO fraud_list (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, borrowerName);
            statement.executeUpdate();
        }
    }

    // ✅ Wrapper method that you can call from other parts of your code
    public void addFraudulentName(String borrowerName) {
        try {
            addToFraudList(borrowerName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check if a user is in the fraud list
    public boolean isFraud(String borrowerName) throws SQLException {
        String query = "SELECT COUNT(*) FROM fraud_list WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, borrowerName);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    // Optional: Retrieve all fraud names (used in FraudDetector.loadFromDatabase)
    public List<String> getAllFraudulentNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT name FROM fraud_list";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
}
