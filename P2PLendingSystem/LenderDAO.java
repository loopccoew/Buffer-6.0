package dao;

import dsa_modules.Lender;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LenderDAO {
    private Connection conn;

    public LenderDAO(Connection conn) {
        this.conn = conn;
    }

    // Save a lender to the database
    public void saveLender(Lender lender) throws SQLException {
        String sql = "INSERT INTO lenders (name, preferred_rate, max_amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, lender.getName());
            stmt.setInt(2, lender.getPreferredRate());
            stmt.setInt(3, lender.getMaxAmount());

            // Execute the insertion
            int rowsAffected = stmt.executeUpdate();

            // If insertion is successful, retrieve the generated ID
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    lender.setId(generatedKeys.getInt(1)); // Set the auto-generated ID
                }
            }
        }
    }

    // Get a lender by ID
    public Lender getLenderById(int id) throws SQLException {
        String sql = "SELECT * FROM lenders WHERE lender_id = ?"; // Changed id to lender_id
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Lender(
                    rs.getInt("lender_id"),  // Changed id to lender_id
                    rs.getString("name"),
                    rs.getInt("preferred_rate"),
                    rs.getInt("max_amount")
                );
            }
        }
        return null;
    }

    // Get all lenders from the database
    public List<Lender> getAllLenders() throws SQLException {
        List<Lender> lenders = new ArrayList<>();
        String sql = "SELECT * FROM lenders";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lenders.add(new Lender(
                    rs.getInt("lender_id"),  // Changed id to lender_id
                    rs.getString("name"),
                    rs.getInt("preferred_rate"),
                    rs.getInt("max_amount")
                ));
            }
        }
        return lenders;
    }
}

