package auction;

import java.util.ArrayList;

public class User {
    String username;
    double balance;
    String currency;
    ArrayList<String> bidHistory = new ArrayList<>();

    public User(String username, double balance, String currency) {
        this.username = username;
        this.balance = balance;
        this.currency = currency;
    }

    public void recordTransaction(String message) {
        bidHistory.add(message);
    }

    public void showHistory() {
        System.out.println("Transaction History for " + username + ":");
        for (String s : bidHistory) {
            System.out.println(" - " + s);
        }
    }
}
