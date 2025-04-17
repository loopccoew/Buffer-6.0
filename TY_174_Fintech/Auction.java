package auction;

import java.util.PriorityQueue;

public class Auction {
    String itemName;
    double startPrice;
    PriorityQueue<Bid> bids;
    long endTime;
    boolean antiSniping;
    boolean suddenDeath;
    boolean reverseAuction;
    boolean closed = false;

    public Auction(String itemName, double startPrice, int durationSec, boolean antiSniping, boolean suddenDeath, boolean reverseAuction) {
        this.itemName = itemName;
        this.startPrice = startPrice;
        this.endTime = System.currentTimeMillis() + durationSec * 1000L;
        this.bids = new PriorityQueue<>();
        this.antiSniping = antiSniping;
        this.suddenDeath = suddenDeath;
        this.reverseAuction = reverseAuction;
    }

    public void placeBid(String username, double amountUSD, String originalCurrency, double originalAmount) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > endTime || closed) {
            System.out.println("Auction has ended or closed.");
            return;
        }

        if (antiSniping && (endTime - currentTime) < 5000) {
            endTime += 5000;
            System.out.println("⏳ Anti-sniping activated! Time extended.");
        }

        if (suddenDeath && !bids.isEmpty()) {
            System.out.println("Sudden Death Mode: Only one bid allowed!");
            return;
        }

        bids.add(new Bid(username, amountUSD, originalCurrency, originalAmount));
        System.out.println("✅ " + username + " placed a bid of " + originalAmount + " " + originalCurrency);
    }

    public void runReverseAuction() {
        new Thread(() -> {
            double currentPrice = startPrice;
            while (!closed) {
                System.out.println("Current Reverse Auction Price: $" + currentPrice);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
                currentPrice -= 5;
                if (currentPrice <= 0) break;
            }
        }).start();
    }

    public Bid closeAuction() {
        closed = true;
        if (bids.isEmpty()) {
            System.out.println("❌ No bids. Auction closed.");
            return null;
        }

        Bid winner = bids.poll();
        double finalAmountInUserCurrency = CurrencyConverter.convertFromUSD(winner.amount, winner.originalCurrency);
        System.out.println("🏁 Auction closed! Winner: " + winner.bidder + " | Amount: " +
                String.format("%.2f", finalAmountInUserCurrency) + " " + winner.originalCurrency);
        return winner;
    }
}
