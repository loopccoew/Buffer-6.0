package dsminiproject;

import java.time.LocalDateTime;

public class Transaction {

	private String transactionType; // "Buy" or "Sell"
    private Stock stock;
    private int quantity;
    private double pricePerShare;
    private double totalAmount;
    private LocalDateTime transactionDate;
    private double portfolioValueAfterTransaction=0;

    // Constructor
    public Transaction(String transactionType, Stock stock, int quantity, double pricePerShare, double portfolioValue) {
        this.transactionType = transactionType;
        this.stock = stock;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.transactionDate = LocalDateTime.now();
        this.totalAmount = calculateTotalAmount();
        this.portfolioValueAfterTransaction = portfolioValue;
    }

    Transaction()
    {
    	
    }
    // Method to calculate the total amount of the transaction
    private double calculateTotalAmount() {
        return this.quantity * this.pricePerShare;
    }

    // Method to get a structured representation of transaction details
    public String getTransactionDetails() {
        return "Transaction Type: " + transactionType + ", Stock: " + stock.getName() +
               ", Quantity: " + quantity + ", Price Per Share: " + pricePerShare +
               ", Total Amount: " + totalAmount + ", Date: " + transactionDate +
               ", Portfolio Value After Transaction: " + portfolioValueAfterTransaction;
    }

    // toString method for printing transaction details
    @Override
    public String toString() {
        return getTransactionDetails();
    }

    // Getters for individual attributes as needed
    public String getTransactionType() { return transactionType; }
    public Stock getStock() { return stock; }
    public int getQuantity() { return quantity; }
    public double getPricePerShare() { return pricePerShare; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public double getPortfolioValueAfterTransaction() { return portfolioValueAfterTransaction; }
    
    
    
}
