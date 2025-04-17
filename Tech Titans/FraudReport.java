public class FraudReport {
    String username;
    String timestamp;
    String reason;
    String actionTaken;
    String type; // e.g., "Login Issue", "Transaction Spike"

    public FraudReport(String username, String timestamp, String reason, String actionTaken, String type) {
        this.username = username;
        this.timestamp = timestamp;
        this.reason = reason;
        this.actionTaken = actionTaken;
        this.type = type;
    }

    public void printReport() {
        System.out.println("----- Fraud Report -----");
        System.out.println("Username     : " + username);
        System.out.println("Timestamp    : " + timestamp);
        System.out.println("Reason       : " + reason);
        System.out.println("Action Taken : " + actionTaken);
        System.out.println("Type         : " + type);
        System.out.println("------------------------");
    }

    public String getType() {
        return type;
    }
}

