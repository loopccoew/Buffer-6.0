import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FraudDetectionGUI {
    private static Set<String> transactionIds = new HashSet<>();
    private static Map<String, String> userLastLocation = new HashMap<>();
    private static Map<String, Integer> userAlertCount = new HashMap<>();
    private static final double HIGH_AMOUNT_THRESHOLD = 10000.0;
    private static final int SUSPICIOUS_THRESHOLD = 2;

    private static JTextArea transactionArea;
    private static JTextArea alertArea;
    private static JTextArea suspiciousArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fraud Detection System");
        frame.setSize(1000, 600); // increased width for 3 panels
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField txnIdField = new JTextField();
        JTextField userIdField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField locationField = new JTextField();

        inputPanel.add(new JLabel("Transaction ID (TXN...):"));
        inputPanel.add(txnIdField);
        inputPanel.add(new JLabel("User ID (USER...):"));
        inputPanel.add(userIdField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);

        JButton addButton = new JButton("Add Transaction");
        inputPanel.add(addButton);

        // Display Panel (now 3 columns)
        JPanel displayPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        transactionArea.setBorder(BorderFactory.createTitledBorder("All Transactions"));

        alertArea = new JTextArea();
        alertArea.setEditable(false);
        alertArea.setForeground(Color.RED);
        alertArea.setBorder(BorderFactory.createTitledBorder("High Alerts"));

        suspiciousArea = new JTextArea();
        suspiciousArea.setEditable(false);
        suspiciousArea.setForeground(Color.MAGENTA);
        suspiciousArea.setBorder(BorderFactory.createTitledBorder("Suspicious Users"));

        displayPanel.add(new JScrollPane(transactionArea));
        displayPanel.add(new JScrollPane(alertArea));
        displayPanel.add(new JScrollPane(suspiciousArea));

        // Add to frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(displayPanel, BorderLayout.CENTER);

        // Action Listener
        addButton.addActionListener(e -> {
            String txnId = txnIdField.getText().trim();
            String userId = userIdField.getText().trim();
            String amountStr = amountField.getText().trim();
            String location = locationField.getText().trim();

            if (!txnId.startsWith("TXN")) {
                showAlert("Transaction ID must start with 'TXN'");
                return;
            }
            if (!userId.startsWith("USER")) {
                showAlert("User ID must start with 'USER'");
                return;
            }
            if (transactionIds.contains(txnId)) {
                String message = "Duplicate Transaction ID: " + txnId;
                showPopupAlert("Duplicate Transaction Alert", message);
                alertArea.append("ALERT: " + message + "\n");
                addUserAlert(userId);
                updateSuspiciousUsers();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException ex) {
                showAlert("Amount must be a valid number");
                return;
            }

            // Log transaction
            String transactionLog = String.format("TxnID=%s, User=%s, ₹%.2f, Location=%s",
                    txnId, userId, amount, location);
            transactionArea.append(transactionLog + "\n");
            transactionIds.add(txnId);

            // Check for high amount
            if (amount > HIGH_AMOUNT_THRESHOLD) {
                String message = "High amount transaction detected!\nUser: " + userId + "\nAmount: ₹" + amount;
                showPopupAlert("High Amount Alert", message);
                alertArea.append("ALERT: " + message.replace("\n", " ") + "\n");
                addUserAlert(userId);
            }

            // Check for location change
            if (userLastLocation.containsKey(userId) &&
                    !userLastLocation.get(userId).equalsIgnoreCase(location)) {
                String message = "Location change detected!\nUser: " + userId +
                        "\nFrom: " + userLastLocation.get(userId) + " -> To: " + location;
                showPopupAlert("Location Change Alert", message);
                alertArea.append("ALERT: " + message.replace("\n", " ") + "\n");
                addUserAlert(userId);
            }

            userLastLocation.put(userId, location);
            updateSuspiciousUsers();

            // Clear input fields
            txnIdField.setText("");
            userIdField.setText("");
            amountField.setText("");
            locationField.setText("");
        });

        frame.setVisible(true);
    }

    private static void addUserAlert(String userId) {
        userAlertCount.put(userId, userAlertCount.getOrDefault(userId, 0) + 1);
    }

    private static void updateSuspiciousUsers() {
        suspiciousArea.setText("");
        for (Map.Entry<String, Integer> entry : userAlertCount.entrySet()) {
            if (entry.getValue() >= SUSPICIOUS_THRESHOLD) {
                suspiciousArea.append("User: " + entry.getKey() + " | Alerts: " + entry.getValue() + "\n");
            }
        }
    }

    private static void showAlert(String message) {
        JOptionPane.showMessageDialog(null, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private static void showPopupAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
}

