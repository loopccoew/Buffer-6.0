
package Cryptocurrency;

public class Transaction {
    private final String sender;
    private final String receiver;
    private final double amount;
    private final String transactionID;

    public Transaction(String sender, String receiver, double amount, String transactionID) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.transactionID = transactionID;
    }

    @Override
    public String toString() {
        return sender + " → " + receiver + " | ₹" + amount + " | TX-ID: " + transactionID;
    }
}
