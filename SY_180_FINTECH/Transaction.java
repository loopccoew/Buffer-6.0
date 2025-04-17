public class Transaction {
    int senderId;
    int receiverId;
    int amount;
    String timestamp;

    public Transaction(int senderId, int receiverId, int amount, String timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "From: " + senderId + ", To: " + receiverId + ", ₹" + amount + ", Time: " + timestamp;
    }
}
