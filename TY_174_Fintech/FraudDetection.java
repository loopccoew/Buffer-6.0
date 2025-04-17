package auction;

import java.util.HashMap;

public class FraudDetection {
    HashMap<String, Integer> bidCounts = new HashMap<>();

    public void trackBid(String username) {
        bidCounts.put(username, bidCounts.getOrDefault(username, 0) + 1);
        if (bidCounts.get(username) > 5) {
            System.out.println("⚠ WARNING: Suspicious bidding detected for user: " + username);
        }
    }
}
