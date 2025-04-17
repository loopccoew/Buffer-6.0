package auction;

import java.util.LinkedList;
import java.util.Queue;

public class PaymentProcessor {
    Queue<Bid> paymentQueue = new LinkedList<>();

    public void processPayment(Bid bid, User user) {
        paymentQueue.add(bid);
        double amountInUserCurrency = CurrencyConverter.convertFromUSD(bid.amount, user.currency);
        user.recordTransaction("Paid " + String.format("%.2f", amountInUserCurrency) + " " + user.currency + " for winning bid.");
        System.out.println("💳 Processing payment for " + bid.bidder + ": " + amountInUserCurrency + " " + user.currency);
    }
}
