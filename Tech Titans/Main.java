package system;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static TransactionAnalyzer analyzer = new TransactionAnalyzer();
    static FraudReportManager fraudManager = new FraudReportManager();

    static Map<String, String> credentials = new HashMap<>();
    static Map<String, Set<String>> userSources = new HashMap<>();
    static Map<String, Queue<Long>> failedAttempts = new HashMap<>();

    static final int MAX_ATTEMPTS = 5;
    static final long TIME_WINDOW = 60 * 1000;

    public static void main(String[] args) throws InterruptedException {
        credentials.put("unnati", "1234");
        credentials.put("shruti", "abcd");
        credentials.put("ishwari", "pass");

        while (true) {
            System.out.println("\n===== Identity Theft Detection System =====");
            System.out.println("1. Login Tracking");
            System.out.println("2. Transaction Analyzer");
            System.out.println("3. Fraud Report Menu");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: loginTracking(); break;
                case 2: transactionMenu(); break;
                case 3: fraudManager.menu(); break;
                case 4:
                    System.out.println("👋 Exiting system.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    static void loginTracking() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.print("Enter location: ");
        String location = sc.nextLine();
        System.out.print("Enter device: ");
        String device = sc.nextLine();
        boolean isSuccess = validateLogin(username, password);
        login(username, location, device, isSuccess);
    }

    static boolean validateLogin(String username, String password) {
        if (!credentials.containsKey(username)) {
            System.out.println("❌ Username not found.");
            return false;
        }
        if (!credentials.get(username).equals(password)) {
            System.out.println("❌ Incorrect password.");
            return false;
        }
        return true;
    }

    static void login(String username, String location, String device, boolean isSuccess) {
        String source = location + "-" + device;
        if (isSuccess) {
            boolean isNew = !userSources.getOrDefault(username, new HashSet<>()).contains(source);
            if (isNew) {
                System.out.println("⚠️ Suspicious login from: " + source);
                fraudManager.addReport(new FraudReport(username, timestamp(), "Suspicious login", "Alerted", "Login Issue"));
            } else {
                System.out.println("✅ Login successful from known source.");
            }
            userSources.putIfAbsent(username, new HashSet<>());
            userSources.get(username).add(source);
            failedAttempts.remove(username);
        } else {
            System.out.println("❌ Failed login from: " + source);
            detectBruteForce(username);
        }
    }

    static void detectBruteForce(String username) {
        long now = System.currentTimeMillis();
        failedAttempts.putIfAbsent(username, new LinkedList<>());
        Queue<Long> q = failedAttempts.get(username);
        while (!q.isEmpty() && (now - q.peek() > TIME_WINDOW)) q.poll();
        q.add(now);
        if (q.size() >= MAX_ATTEMPTS) {
            System.out.println("🚨 Brute-force attack suspected on: " + username);
            fraudManager.addReport(new FraudReport(username, timestamp(), "Brute-force detected", "Blocked temporarily", "Login Issue"));
        }
    }

    static void transactionMenu() throws InterruptedException {
        int choice;
        do {
            System.out.println("\n--- Transaction Menu ---");
            System.out.println("1. Add Transaction");
            System.out.println("2. View All Transactions");
            System.out.println("3. Average Spend");
            System.out.println("4. Search Transaction");
            System.out.println("5. Min Transaction");
            System.out.println("6. Max Transaction");
            System.out.println("7. Transaction Count");
            System.out.println("8. Detect Abnormal Transactions");
            System.out.println("9. Delete Transaction");
            System.out.println("10. Back");
            System.out.print("Choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    analyzer.addTransaction(sc.nextDouble());
                    break;
                case 2: analyzer.printTransactionsInOrder(); break;
                case 3: System.out.printf("Avg Spend: Rs.%.2f\n", analyzer.getAverageSpend()); break;
                case 4:
                    System.out.print("Enter amount to search: ");
                    analyzer.searchTransaction(sc.nextDouble()); break;
                case 5: analyzer.getMinTransaction(); break;
                case 6: analyzer.getMaxTransaction(); break;
                case 7: System.out.println("Total Transactions: " + analyzer.count); break;
                case 8: analyzer.detectAbnormalTransactions(); break;
                case 9:
                    System.out.print("Enter amount to delete: ");
                    analyzer.deleteTransaction(sc.nextDouble()); break;
                case 10: break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 10);
    }

    static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}