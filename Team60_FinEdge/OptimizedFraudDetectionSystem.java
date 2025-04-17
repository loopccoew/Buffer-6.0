
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

// ------------ Transaction Model ------------
class Transaction {
    private final String transactionId;
    private final String userId;
    private final double amount;
    private final String location;
    private final long timestamp;

    public Transaction(String transactionId, String userId, double amount, String location, long timestamp) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getLocation() { return location; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return "[TxnID=" + transactionId + ", User=" + userId + ", Amount=₹" + amount +
                ", Location=" + location + ", Time=" + sdf.format(new Date(timestamp)) + "]";
    }
}

// ------------ Fraud Detection Strategy Interface ------------
interface FraudCheck {
    void apply(Transaction txn);
}

// ------------ Duplicate Check ------------
class DuplicateCheck implements FraudCheck {
    private final Set<String> seenTxnIds = new HashSet<>();

    @Override
    public void apply(Transaction txn) {
        if (seenTxnIds.contains(txn.getTransactionId())) {
            System.out.println("\u001B[31mALERT: Duplicate Transaction ID - " + txn.getTransactionId() + "\u001B[0m");
        } else {
            seenTxnIds.add(txn.getTransactionId());
        }
    }
}

// ------------ Location Check ------------
class LocationCheck implements FraudCheck {
    private final Map<String, String> userLocations = new HashMap<>();

    @Override
    public void apply(Transaction txn) {
        String userId = txn.getUserId();
        String currentLocation = txn.getLocation();
        if (userLocations.containsKey(userId) && !userLocations.get(userId).equalsIgnoreCase(currentLocation)) {
            System.out.println("\u001B[33mALERT: Location change for user " + userId +
                    " from " + userLocations.get(userId) + " to " + currentLocation + "\u001B[0m");
        }
        userLocations.put(userId, currentLocation);
    }
}

// ------------ High Amount Check ------------
class HighAmountCheck implements FraudCheck {
    private final PriorityQueue<Transaction> highRiskQueue = new PriorityQueue<>((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
    private final double threshold;

    public HighAmountCheck(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void apply(Transaction txn) {
        highRiskQueue.offer(txn);
        if (txn.getAmount() > threshold) {
            System.out.println("\u001B[35mALERT: High-risk transaction (₹" + txn.getAmount() + ") by " + txn.getUserId() + "\u001B[0m");
        }
    }

    public List<Transaction> getTopHighRisk(int n) {
        List<Transaction> list = new ArrayList<>(highRiskQueue);
        list.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
        return list.subList(0, Math.min(n, list.size()));
    }
}

// ------------ Time Anomaly Check ------------
class TimeAnomalyCheck implements FraudCheck {
    private final Map<String, Long> lastTxnTime = new HashMap<>();
    private final long minInterval = 10000; // 10 seconds

    @Override
    public void apply(Transaction txn) {
        String userId = txn.getUserId();
        long currentTime = txn.getTimestamp();
        if (lastTxnTime.containsKey(userId) && currentTime - lastTxnTime.get(userId) < minInterval) {
            System.out.println("\u001B[36mALERT: Rapid transaction detected for user " + userId + "\u001B[0m");
        }
        lastTxnTime.put(userId, currentTime);
    }
}

// ------------ Suspicious User Tracker ------------
class SuspiciousUserTracker implements FraudCheck {
    final Map<String, Integer> userAlerts = new HashMap<>();

    @Override
    public void apply(Transaction txn) {
        userAlerts.put(txn.getUserId(), userAlerts.getOrDefault(txn.getUserId(), 0) + 1);
    }

    public void displaySuspiciousUsers() {
        System.out.println("\nSuspicious Users:");
        for (Map.Entry<String, Integer> entry : userAlerts.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println("User: " + entry.getKey() + " | Alerts: " + entry.getValue());
            }
        }
    }
}

// ------------ Transaction Processor ------------
class TransactionProcessor {
    private final List<FraudCheck> checks = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public void addCheck(FraudCheck check) {
        checks.add(check);
    }

    public void process(Transaction txn) {
        System.out.println("\nProcessing: " + txn);
        for (FraudCheck check : checks) {
            check.apply(txn);
        }
        transactions.add(txn);
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public void exportToCSV() {
        try (FileWriter writer = new FileWriter("transactions.csv")) {
            writer.write("TransactionID,UserID,Amount,Location,Timestamp\n");
            for (Transaction txn : transactions) {
                writer.write(txn.getTransactionId() + "," + txn.getUserId() + "," + txn.getAmount() + "," +
                        txn.getLocation() + "," + txn.getTimestamp() + "\n");
            }
            System.out.println("Transactions exported to transactions.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// ------------ Main Menu ------------
public class OptimizedFraudDetectionSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransactionProcessor processor = new TransactionProcessor();
        HighAmountCheck highAmountCheck = new HighAmountCheck(10000);
        SuspiciousUserTracker suspiciousUserTracker = new SuspiciousUserTracker();

        // Add all checks
        processor.addCheck(new DuplicateCheck());
        processor.addCheck(new LocationCheck());
        processor.addCheck(highAmountCheck);
        processor.addCheck(new TimeAnomalyCheck());
        processor.addCheck(suspiciousUserTracker);

        while (true) {
            System.out.println("\n==== FRAUD DETECTION SYSTEM MENU ====");
            System.out.println("1. Add Transaction");
            System.out.println("2. View All Transactions");
            System.out.println("3. View Top High-Risk Transactions");
            System.out.println("4. Export Transactions to CSV");
            System.out.println("5. View Suspicious Users");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Transaction ID: ");
                    String txnId = scanner.nextLine();
                    System.out.print("User ID: ");
                    String userId = scanner.nextLine();
                    System.out.print("Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Location: ");
                    String location = scanner.nextLine();
                    long timestamp = System.currentTimeMillis();

                    Transaction txn = new Transaction(txnId, userId, amount, location, timestamp);
                    processor.process(txn);
                }
                case 2 -> {
                    System.out.println("\nAll Transactions:");
                    for (Transaction txn : processor.getAllTransactions()) {
                        System.out.println(txn);
                    }
                }
                case 3 -> {
                    System.out.println("\nTop High-Risk Transactions:");
                    List<Transaction> topTxns = highAmountCheck.getTopHighRisk(5);
                    for (Transaction txn : topTxns) {
                        System.out.println(txn);
                    }
                }
                case 4 -> processor.exportToCSV();
                case 5 -> suspiciousUserTracker.displaySuspiciousUsers();
                case 6 -> {
                    System.out.println("Exiting... Stay safe!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}