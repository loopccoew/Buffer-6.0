import java.util.LinkedList;
import java.util.Scanner;

public class FraudReportManager {
    LinkedList<FraudReport> reportList = new LinkedList<>();

    // Add a new report
    public void addReport(FraudReport report) {
        reportList.add(report);
        System.out.println("✅ Fraud report added successfully.\n");
    }

    // View all reports
    public void viewReports() {
        if (reportList.isEmpty()) {
            System.out.println("No fraud reports available.");
            return;
        }

        for (FraudReport report : reportList) {
            report.printReport();
        }
    }

    // Clear all reports
    public void clearReports() {
        reportList.clear();
        System.out.println("🧹 All fraud reports cleared.");
    }

    // Filter by type
    public void filterByType(String type) {
        boolean found = false;
        for (FraudReport report : reportList) {
            if (report.getType().equalsIgnoreCase(type)) {
                report.printReport();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reports found for type: " + type);
        }
    }

    
    public void menu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n📋 Fraud Report Menu:");
            System.out.println("1. Add Report");
            System.out.println("2. View Reports");
            System.out.println("3. Clear Reports");
            System.out.println("4. Filter by Type");
            System.out.println("5. Exit");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Username: ");
                    String username = sc.nextLine();
                    System.out.print("Timestamp: ");
                    String timestamp = sc.nextLine();
                    System.out.print("Reason: ");
                    String reason = sc.nextLine();
                    System.out.print("Action Taken: ");
                    String action = sc.nextLine();
                    System.out.print("Type (Login Issue / Transaction Spike): ");
                    String type = sc.nextLine();

                    FraudReport report = new FraudReport(username, timestamp, reason, action, type);
                    addReport(report);
                    break;

                case 2:
                    viewReports();
                    break;

                case 3:
                    clearReports();
                    break;

                case 4:
                    System.out.print("Enter type to filter by: ");
                    String filterType = sc.nextLine();
                    filterByType(filterType);
                    break;

                case 5:
                    System.out.println("Exiting Fraud Report Menu.");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

