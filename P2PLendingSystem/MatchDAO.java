package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import dsa_modules.Match;

public class MatchDAO {
    private final Connection conn;

    public MatchDAO(Connection conn) {
        this.conn = conn;
    }

    // Save a match to the database
    public void saveMatch(Match match) throws SQLException {
        String sql = "INSERT INTO matches (borrower_id, lender_id, interest_rate) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, match.getBorrowerId());
            stmt.setInt(2, match.getLenderId());
            stmt.setInt(3, (int)match.getInterestRate()); // interest rate is now an int

            // Log the match being saved
            System.out.println("Saving match: Borrower ID = " + match.getBorrowerId() +
                               ", Lender ID = " + match.getLenderId() +
                               ", Interest Rate = " + match.getInterestRate());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Match saved successfully.");
            } else {
                System.out.println("Failed to save match.");
            }
        } catch (SQLException e) {
            System.err.println("Error saving match: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Retrieve all matches from the database
    public List<Match> getAllMatches() throws SQLException {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT borrower_id, lender_id, interest_rate FROM matches";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int borrowerId = rs.getInt("borrower_id");
                int lenderId = rs.getInt("lender_id");
                int rate = rs.getInt("interest_rate"); // interest rate as int
                list.add(new Match(borrowerId, lenderId, rate)); // assuming Match constructor uses int now
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving matches: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return list;
    }
}

