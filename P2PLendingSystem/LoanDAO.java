package dao;

import java.sql.*;

public class LoanDAO {
    private Connection connection;

    public LoanDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert a new loan into the database
    public void addLoan(int borrowerId, int amount, int interestRate) throws SQLException {
        // Check if the borrower already has a pending loan
        String checkQuery = "SELECT COUNT(*) FROM loans WHERE borrower_id = ? AND loan_status = 'pending'";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, borrowerId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("❌ Borrower already has a pending loan request.");
                return; // Loan already exists
            }
        }

        // Insert the loan into the loans table
        String query = "INSERT INTO loans (borrower_id, amount, interest_rate, loan_status) VALUES (?, ?, ?, 'pending')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowerId);
            statement.setInt(2, amount);
            statement.setInt(3, interestRate);
            statement.executeUpdate();
        }
    }

    // Get loan details by borrower ID
    public ResultSet getLoanByBorrowerId(int borrowerId) throws SQLException {
        String query = "SELECT * FROM loans WHERE borrower_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, borrowerId);
        return statement.executeQuery();
    }

    // Get total loan amount by borrower ID
    public int getTotalLoanAmountByBorrowerId(int borrowerId) throws SQLException {
        String query = "SELECT SUM(amount) FROM loans WHERE borrower_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, borrowerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1); // Return the sum of amounts
                }
            }
        }
        return 0; // Return 0 if no loans are found
    }
    
    public void updateLoanStatus(int borrowerId, int lenderId, String status) throws SQLException {
        String sql = "UPDATE loans SET loan_status = ? WHERE borrower_id = ? AND lender_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, borrowerId);
            stmt.setInt(3, lenderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Loan status updated for Borrower ID " + borrowerId + " and Lender ID " + lenderId);
            } else {
                System.out.println("⚠️ No matching loan found for Borrower ID " + borrowerId + " and Lender ID " + lenderId);
            }
        }
    }
}
