package ui;

import connector.P2PLendingSystemConnector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainUI extends JFrame {

    private final P2PLendingSystemConnector connector = new P2PLendingSystemConnector();

    public MainUI() {
        setTitle("Smart P2P Lending System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Register Borrower", wrapPanel(createRegisterPanel()));
        tabbedPane.addTab("Request Loan", wrapPanel(createLoanPanel()));
        tabbedPane.addTab("Make Repayment", wrapPanel(createRepaymentPanel()));
        tabbedPane.addTab("Top Borrower", wrapPanel(createTopBorrowerPanel()));
        tabbedPane.addTab("Blacklist User", wrapPanel(createBlacklistPanel()));
        tabbedPane.addTab("Register Lender", wrapPanel(createLenderRegisterPanel()));
        tabbedPane.addTab("Match Lender to Borrower", wrapPanel(createMatchLenderPanel()));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel wrapPanel(JPanel inner) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        wrapper.add(inner, BorderLayout.CENTER);
        return wrapper;
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField();
        JButton registerButton = new JButton("Register Borrower");

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String creditText = creditField.getText().trim();

            if (name.isEmpty() || creditText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            try {
                int score = Integer.parseInt(creditText);
                connector.registerBorrower(name, score);
                JOptionPane.showMessageDialog(this, "Borrower Registered Successfully!");
                clearFields(nameField, creditField);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Credit score must be a number.");
            }
        });

        panel.add(new JLabel("Borrower Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Credit Score:"));
        panel.add(creditField);
        panel.add(new JLabel());
        panel.add(registerButton);

        return panel;
    }

    private JPanel createLoanPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField rateField = new JTextField();
        JButton loanButton = new JButton("Request Loan");

        loanButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String amountText = amountField.getText().trim();
            String rateText = rateField.getText().trim();

            if (name.isEmpty() || amountText.isEmpty() || rateText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            try {
                int amount = Integer.parseInt(amountText);
                int rate = Integer.parseInt(rateText);

                boolean success = connector.requestLoan(name, amount, rate);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Loan Requested Successfully!");
                    clearFields(nameField, amountField, rateField);
                } else {
                    JOptionPane.showMessageDialog(this, "Loan request denied. Borrower may be blacklisted or not registered.");
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Loan amount and interest rate must be numbers.");
            }
        });

        panel.add(new JLabel("Borrower Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Loan Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Interest Rate:"));
        panel.add(rateField);
        panel.add(new JLabel());
        panel.add(loanButton);

        return panel;
    }

    private JPanel createRepaymentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField amountField = new JTextField();
        JButton repayButton = new JButton("Make Repayment");

        repayButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String amountText = amountField.getText().trim();

            if (name.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            try {
                int amount = Integer.parseInt(amountText);
                connector.makeRepayment(name, amount);
                JOptionPane.showMessageDialog(this, "Repayment Successful!");
                clearFields(nameField, amountField);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Amount must be a number.");
            }
        });

        panel.add(new JLabel("Borrower Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel());
        panel.add(repayButton);

        return panel;
    }

    private JPanel createTopBorrowerPanel() {
        JPanel panel = new JPanel();
        JButton topButton = new JButton("Show Top Borrowers");

        topButton.addActionListener(e -> {
            connector.showTopCreditBorrower();
        });

        panel.add(topButton);
        return panel;
    }

    private JPanel createBlacklistPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField nameField = new JTextField();
        JButton blacklistButton = new JButton("Blacklist User");

        blacklistButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.");
                return;
            }
            connector.blacklistUser(name);
            JOptionPane.showMessageDialog(this, "User '" + name + "' has been blacklisted.");
            clearFields(nameField);
        });

        panel.add(new JLabel("Name to Blacklist:"));
        panel.add(nameField);
        panel.add(new JLabel());
        panel.add(blacklistButton);

        return panel;
    }

    private JPanel createLenderRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField rateField = new JTextField();
        JTextField amountField = new JTextField();
        JButton registerButton = new JButton("Register Lender");

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String rateText = rateField.getText().trim();
            String amountText = amountField.getText().trim();

            if (name.isEmpty() || rateText.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            try {
                int preferredRate = Integer.parseInt(rateText);
                int maxAmount = Integer.parseInt(amountText);
                connector.registerLender(name, preferredRate, maxAmount);
                JOptionPane.showMessageDialog(this, "Lender Registered Successfully!");
                clearFields(nameField, rateField, amountField);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Rate and amount must be numbers.");
            }
        });

        panel.add(new JLabel("Lender Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Preferred Interest Rate:"));
        panel.add(rateField);
        panel.add(new JLabel("Maximum Loan Amount:"));
        panel.add(amountField);
        panel.add(new JLabel());
        panel.add(registerButton);

        return panel;
    }

    private JPanel createMatchLenderPanel() {
        JPanel panel = new JPanel();
        JButton matchButton = new JButton("Match Lender to Borrower");

        matchButton.addActionListener(e -> {
            String result = connector.matchLenderToBorrower();
            JOptionPane.showMessageDialog(this, result, "Matching Result", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(matchButton);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }
}
