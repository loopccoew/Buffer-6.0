package dao;

import java.sql.*;

public class RepaymentDAO {
    private Connection connection;

    public RepaymentDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert repayment data
    public void addRepayment(int loanId, double amount) throws SQLException {
        String query = "INSERT INTO repayments (loan_id, amount) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, loanId);
            statement.setDouble(2, amount);
            statement.executeUpdate();
        }
    }

    // Update repayment data
    public void updateRepayment(int loanId, double amount) throws SQLException {
        String query = "UPDATE repayments SET amount = ? WHERE loan_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setInt(2, loanId);
            statement.executeUpdate();
        }
    }

    // Get total repayments for a loan
    public double getTotalRepayments(int loanId) throws SQLException {
        String query = "SELECT SUM(amount) FROM repayments WHERE loan_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, loanId);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getDouble(1);
        }
        return 0.0;
    }
}
