package auction;

public class Bid implements Comparable<Bid> {
    String bidder;
    double amount; // In USD
    String originalCurrency;
    double originalAmount;

    public Bid(String bidder, double amountUSD, String currency, double originalAmount) {
        this.bidder = bidder;
        this.amount = amountUSD;
        this.originalCurrency = currency;
        this.originalAmount = originalAmount;
    }

    @Override
    public int compareTo(Bid other) {
        return Double.compare(other.amount, this.amount); // Higher USD bid wins
    }
}
