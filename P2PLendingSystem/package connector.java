package connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Statement;

import dao.*;
import dsa_modules.*;
import db.DatabaseUtil;

import javax.swing.JOptionPane;

public class P2PLendingSystemConnector {

    private final BorrowerDAO borrowerDAO;
    private final LenderDAO lenderDAO;
    private final LoanDAO loanDAO;
    private final RepaymentDAO repaymentDAO;
    private final FraudDAO fraudDAO;
    private final MatchDAO matchDAO;

    private final CreditScoreBST creditScoreBST = new CreditScoreBST();
    private final InterestRateManager interestHeap = new InterestRateManager();
    private final FraudDetector fraudTrie = new FraudDetector();
    private final SegmentTree repaymentTree;
    private final Graph borrowerLenderGraph = new Graph();

    private final Map<String, Borrower> borrowerMap = new HashMap<>();
    private final Map<String, Lender> lenderMap = new HashMap<>();
    private int borrowerIdCounter = 1;
    private int lenderIdCounter = 1;

    private final Connection conn;

    public P2PLendingSystemConnector() {
        Connection connectionTemp = null;
        try {
            connectionTemp = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
        this.conn = connectionTemp;
        this.borrowerDAO = new BorrowerDAO(conn);
        this.lenderDAO = new LenderDAO(conn);
        this.loanDAO = new LoanDAO(conn);
        this.repaymentDAO = new RepaymentDAO(conn);
        this.fraudDAO = new FraudDAO(conn);
        this.matchDAO = new MatchDAO(conn);
        this.repaymentTree = new SegmentTree(new int[100]); // Placeholder

        Map<String, Borrower> loadedBorrowers = borrowerDAO.loadAllBorrowersIntoMap();
        borrowerMap.putAll(loadedBorrowers);
        loadedBorrowers.values().forEach(creditScoreBST::insert);
    }

    public void registerBorrower(String name, int creditScore) {
        try {
            if (fraudDAO.isFraud(name)) {
                System.out.println("User is flagged as fraudulent. Cannot register.");
                return;
            }

            Borrower borrower = new Borrower(borrowerIdCounter++, name, creditScore);
            creditScoreBST.insert(borrower);
            borrowerMap.put(name, borrower);
            borrowerLenderGraph.addBorrower(borrower.getId());
            borrowerDAO.saveBorrower(borrower);

            System.out.println("Borrower registered: " + name + " (ID: " + borrower.getId() + ")");
        } catch (SQLException e) {
            System.out.println("Borrower registration error: " + e.getMessage());
        }
    }

    public void registerLender(String name, int preferredRate, int maxAmount) {
        try {
            if (fraudDAO.isFraud(name)) {
                System.out.println("User is flagged as fraudulent. Cannot register.");
                return;
            }

            Lender lender = new Lender(lenderIdCounter++, name, preferredRate, maxAmount);
            lenderMap.put(name, lender);
            borrowerLenderGraph.addLender(lender.getId());
            lenderDAO.saveLender(lender);

            System.out.println("Lender registered: " + name + " (ID: " + lender.getId() + ")");
        } catch (SQLException e) {
            System.out.println("Lender registration error: " + e.getMessage());
        }
    }

    public void blacklistUser(String name) {
        String sql = "UPDATE borrowers SET status = 'blacklisted' WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User " + name + " has been blacklisted.");
            } else {
                System.out.println("No user found with name " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error blacklisting user: " + e.getMessage());
        }
    }

    public boolean requestLoan(String borrowerName, int amount, int interestRate) {
        try {
            Borrower borrower = borrowerMap.get(borrowerName);
            if (borrower == null) {
                System.out.println("❌ Borrower not found in map: " + borrowerName);
                return false;
            }

            if (fraudDAO.isFraud(borrowerName)) {
                System.out.println("🚫 Loan request denied. Borrower is flagged as fraudulent.");
                return false;
            }

            String status = borrowerDAO.getBorrowerStatusById(borrower.getId());
            System.out.println("Borrower status: " + status);
            if ("blacklisted".equalsIgnoreCase(status)) {
                System.out.println("🚫 Loan request denied. Borrower is blacklisted.");
                return false;
            }

            loanDAO.addLoan(borrower.getId(), amount, interestRate);
            borrowerDAO.updateRequestedDetails(borrower.getId(), amount, interestRate);
            borrower.setRequestedAmount(amount);
            borrower.setRequestedRate(interestRate);
            interestHeap.addBorrowerRate(interestRate);

            int lenderId = findBestLenderForBorrower(interestRate);
            if (lenderId != -1) {
                borrowerLenderGraph.addMatch(lenderId, new Match(borrower.getId(), lenderId, interestRate));
            }

            System.out.println("✅ Loan request placed: " + borrowerName + " - Rs." + amount + " @ " + interestRate + "%");
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Loan request error: " + e.getMessage());
            return false;
        }
    }

    private int findBestLenderForBorrower(int borrowerRate) {
        for (Lender lender : lenderMap.values()) {
            if (lender.getPreferredRate() <= borrowerRate) {
                return lender.getId();
            }
        }
        return -1;
    }

    public void makeRepayment(String borrowerName, int amount) {
        try {
            Borrower borrower = borrowerMap.get(borrowerName);
            if (borrower == null) {
                System.out.println("Borrower not found.");
                return;
            }

            int loanId = borrower.getId();
            repaymentDAO.addRepayment(loanId, amount);
            repaymentTree.update(loanId, amount);

            System.out.println("Repayment recorded: " + borrowerName + " - Rs." + amount);
        } catch (SQLException e) {
            System.out.println("Repayment error: " + e.getMessage());
        }
    }

    public Borrower getBorrowerById(int borrowerId) {
        return borrowerDAO.getBorrowerById(borrowerId);
    }

    public Lender getLenderById(int id) {
        try {
            return lenderDAO.getLenderById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String matchLenderToBorrower() {
        StringBuilder result = new StringBuilder();
        try {
            // Step 1: Select all pending loans
            String sql = "SELECT * FROM loans WHERE loan_status = 'pending'";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

                List<Borrower> matchedBorrowers = new ArrayList<>();
                List<Lender> matchedLenders = new ArrayList<>();

                while (rs.next()) {
                    int borrowerId = rs.getInt("borrower_id");
                    double interestRate = rs.getDouble("interest_rate");

                    Borrower borrower = getBorrowerById(borrowerId);
                    if (borrower == null) {
                        result.append("❌ Borrower ID ").append(borrowerId).append(" not found.\n");
                        continue;
                    }

                    String status = borrowerDAO.getBorrowerStatusById(borrower.getId());
                    if ("blacklisted".equalsIgnoreCase(status)) {
                        result.append("❌ Skipped blacklisted borrower: ").append(borrower.getName()).append("\n");
                        continue;
                    }

                    // Step 2: Get eligible lenders
                    List<Lender> lenders = lenderDAO.getAllLenders(); // You can filter further if needed
                    boolean isMatched = false;

                    for (Lender lender : lenders) {
                        if (isMatch(lender, borrower)) {
                            // Step 3: Assign lender to loan
                            assignLenderToLoan(borrowerId, lender.getId(), interestRate);

                            // Step 4: Save match
                            Match match = new Match(borrowerId, lender.getId(), interestRate);
                            matchDAO.saveMatch(match);

                            // Step 5: Update loan status
                            updateLoanStatus(borrowerId, lender.getId(), "matched");

                            result.append("✅ Matched: ").append(borrower.getName())
                                  .append(" with ").append(lender.getName())
                                  .append(" @ ").append(interestRate).append("%\n");

                            matchedBorrowers.add(borrower);
                            matchedLenders.add(lender);
                            isMatched = true;
                            break; // Stop after first valid match
                        }
                    }

                    if (!isMatched) {
                        result.append("❌ No matching lender found for borrower: ").append(borrower.getName()).append("\n");
                    }
                }

                // Optional: update graph
                borrowerLenderGraph.removeBorrowers(matchedBorrowers);
                borrowerLenderGraph.removeLenders(matchedLenders);
            }

        } catch (SQLException e) {
            result.append("Database error: ").append(e.getMessage());
        } catch (Exception e) {
            result.append("An error occurred while matching: ").append(e.getMessage());
        }

        return result.toString();
    }
    
    public void updateLoanStatus(int borrowerId, int lenderId, String status) throws SQLException {
        String sql = "UPDATE loans SET loan_status = ? WHERE borrower_id = ? AND lender_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, borrowerId);
            stmt.setInt(3, lenderId);
            stmt.executeUpdate();
            System.out.println("✅ Loan status updated for Borrower ID " + borrowerId + " and Lender ID " + lenderId);
        }
    }

    
    public void assignLenderToLoan(int borrowerId, int lenderId, double interestRate) throws SQLException {
        String sql = "UPDATE loans SET lender_id = ?, interest_rate = ? WHERE borrower_id = ? AND loan_status = 'pending'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lenderId);
            stmt.setDouble(2, interestRate);
            stmt.setInt(3, borrowerId);
            stmt.executeUpdate();
            System.out.println("✅ Assigned Lender ID " + lenderId + " to Borrower ID " + borrowerId);
        }
    }
    
    private boolean isMatch(Lender lender, Borrower borrower) {
        System.out.println("Checking match: Lender " + lender.getId() + " and Borrower " + borrower.getId());
        System.out.println("Lender Max: " + lender.getMaxAmount() + ", Rate: " + lender.getPreferredRate());
        System.out.println("Borrower Request: " + borrower.getRequestedAmount() + ", Rate: " + borrower.getRequestedRate());

        return lender.getMaxAmount() >= borrower.getRequestedAmount() &&
               lender.getPreferredRate() <= borrower.getRequestedRate();
    }



    public void showTopCreditBorrower() {
        try {
            List<Borrower> topBorrowers = borrowerDAO.getAllBorrowersSortedByCreditScore();
            if (topBorrowers.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No borrowers found.");
                return;
            }

            StringBuilder sb = new StringBuilder("Top Borrowers by Credit Score:\n");
            for (Borrower b : topBorrowers) {
                sb.append("Name: ").append(b.getName())
                  .append(" | Credit Score: ").append(b.getCreditScore())
                  .append(" | Requested Amount: ").append(b.getRequestedAmount())
                  .append(" | Requested Rate: ").append(b.getRequestedRate()).append("%\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
