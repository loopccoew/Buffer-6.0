package dao;

import db.DatabaseUtil;
import dsa_modules.Borrower;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BorrowerDAO {

    private Connection conn;

    // Constructor
    public BorrowerDAO(Connection connection) {
        this.conn = connection;
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Failed to set auto-commit: " + e.getMessage());
        }
    }

    // Save borrower (default status = 'active')
    public void saveBorrower(Borrower b) {
        String sql = "INSERT INTO borrowers (name, credit_score, requested_amount, requested_rate, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, b.getName());
            stmt.setInt(2, b.getCreditScore());
            stmt.setInt(3, b.getRequestedAmount());
            stmt.setDouble(4, b.getRequestedRate());
            stmt.setString(5, "active"); // default status

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    b.setId(generatedId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving borrower: " + e.getMessage());
        }
    }

    // Retrieve borrower by ID
    public Borrower getBorrowerById(int id) {
        String sql = "SELECT * FROM borrowers WHERE borrower_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Borrower(
                        rs.getInt("borrower_id"),
                        rs.getString("name"),
                        rs.getInt("credit_score"),
                        rs.getInt("requested_amount"),
                        rs.getDouble("requested_rate")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving borrower: " + e.getMessage());
        }
        return null;
    }

    // ✅ Retrieve borrower by name
    public Borrower getBorrowerByName(String name) {
        String sql = "SELECT * FROM borrowers WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Borrower(
                        rs.getInt("borrower_id"),
                        rs.getString("name"),
                        rs.getInt("credit_score"),
                        rs.getInt("requested_amount"),
                        rs.getDouble("requested_rate")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving borrower by name: " + e.getMessage());
        }
        return null;
    }

    // Get borrower status by ID
    public String getBorrowerStatusById(int borrowerId) {
        String sql = "SELECT status FROM borrowers WHERE borrower_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    return status != null ? status.trim().toLowerCase() : "active";
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting borrower status: " + e.getMessage());
        }
        return "active";
    }

    // Update borrower status
    public void updateBorrowerStatus(int borrowerId, String status) {
        String sql = "UPDATE borrowers SET status = ? WHERE borrower_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, borrowerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating borrower status: " + e.getMessage());
        }
    }

    // Get all active borrowers sorted by credit score (descending)
    public List<Borrower> getAllBorrowersSortedByCreditScore() {
        List<Borrower> borrowers = new ArrayList<>();
        String sql = "SELECT * FROM borrowers WHERE status = 'active'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Borrower b = new Borrower(
                    rs.getInt("borrower_id"),
                    rs.getString("name"),
                    rs.getInt("credit_score"),
                    rs.getInt("requested_amount"),
                    rs.getDouble("requested_rate")
                );
                borrowers.add(b);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving borrowers: " + e.getMessage());
        }

        borrowers.sort(Comparator.comparingInt(Borrower::getCreditScore).reversed());
        return borrowers;
    }

    // Update requested amount and rate
    public void updateRequestedDetails(int borrowerId, int amount, double rate) throws SQLException {
        String sql = "UPDATE borrowers SET requested_amount = ?, requested_rate = ? WHERE borrower_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setDouble(2, rate);
            stmt.setInt(3, borrowerId);
            stmt.executeUpdate();
        }
    }
    
    // Load all active borrowers into a map (key = borrower name)
    public Map<String, Borrower> loadAllBorrowersIntoMap() {
        Map<String, Borrower> borrowerMap = new HashMap<>();
        String sql = "SELECT * FROM borrowers WHERE status = 'active'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Borrower b = new Borrower(
                    rs.getInt("borrower_id"),
                    rs.getString("name"),
                    rs.getInt("credit_score"),
                    rs.getInt("requested_amount"),
                    rs.getDouble("requested_rate")
                );
                borrowerMap.put(b.getName(), b);
            }

        } catch (SQLException e) {
            System.err.println("Error loading borrowers into map: " + e.getMessage());
        }

        return borrowerMap;
    }

}




