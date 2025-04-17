package auction;

import java.util.*;

public class AuctionHouse {
    static HashMap<String, User> users = new HashMap<>();
    static List<Auction> auctions = new ArrayList<>();
    static FraudDetection fraudDetection = new FraudDetection();
    static PaymentProcessor paymentProcessor = new PaymentProcessor();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n🌟 BidSure Auction Platform 🌟");
            System.out.println("1. Register User");
            System.out.println("2. Create Auction");
            System.out.println("3. Bid on Auction");
            System.out.println("4. Close Auction");
            System.out.println("5. View History");
            System.out.println("6. Exit");
            System.out.print("Pick: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> createAuction();
                case 3 -> bidOnAuction();
                case 4 -> closeAuction();
                case 5 -> viewHistory();
                case 6 -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void registerUser() {
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Balance: ");
        double balance = scanner.nextDouble();
        System.out.print("Currency (USD/EUR/INR): ");
        String currency = scanner.next();

        users.put(username, new User(username, balance, currency));
        System.out.println("✅ Registered " + username);
    }

    static void createAuction() {
        System.out.print("Item name: ");
        String name = scanner.next();
        System.out.print("Start price (USD): ");
        double price = scanner.nextDouble();
        System.out.print("Duration (sec): ");
        int dur = scanner.nextInt();
        System.out.print("Anti-sniping (true/false): ");
        boolean as = scanner.nextBoolean();
        System.out.print("Sudden Death Mode? ");
        boolean sd = scanner.nextBoolean();
        System.out.print("Reverse Auction Mode? ");
        boolean ra = scanner.nextBoolean();

        Auction auction = new Auction(name, price, dur, as, sd, ra);
        if (ra) auction.runReverseAuction();

        auctions.add(auction);
        System.out.println("🛍️ Auction created!");
    }

    static void bidOnAuction() {
        System.out.print("Your username: ");
        String user = scanner.next();
        if (!users.containsKey(user)) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("Available Auctions:");
        for (int i = 0; i < auctions.size(); i++) {
            System.out.println((i + 1) + ". " + auctions.get(i).itemName);
        }

        System.out.print("Choose auction number: ");
        int idx = scanner.nextInt() - 1;

        System.out.print("Enter bid amount in your currency: ");
        double userAmount = scanner.nextDouble();
        User currentUser = users.get(user);
        double inUSD = CurrencyConverter.convertToUSD(userAmount, currentUser.currency);

        auctions.get(idx).placeBid(user, inUSD, currentUser.currency, userAmount);
        fraudDetection.trackBid(user);
    }

    static void closeAuction() {
        for (int i = 0; i < auctions.size(); i++) {
            System.out.println((i + 1) + ". " + auctions.get(i).itemName);
        }

        System.out.print("Auction to close: ");
        int index = scanner.nextInt() - 1;
        Bid winner = auctions.get(index).closeAuction();

        if (winner != null && users.containsKey(winner.bidder)) {
            paymentProcessor.processPayment(winner, users.get(winner.bidder));
        }
    }

    static void viewHistory() {
        System.out.print("Username: ");
        String user = scanner.next();
        if (users.containsKey(user)) {
            users.get(user).showHistory();
        } else {
            System.out.println("User not found.");
        }
    }
}
