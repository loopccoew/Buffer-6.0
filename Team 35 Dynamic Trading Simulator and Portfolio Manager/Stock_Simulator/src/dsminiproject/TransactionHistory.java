package dsminiproject;
import java.util.Scanner;
import java.util.Stack;
import java.time.LocalDateTime;

	
//	public string TransactionType { get; set; }  // "Buy" or "Sell"
//    public decimal Price { get; set; }
//    public int Quantity { get; set; }
//	
//    
//    public Transaction(String transactionType, decimal price, int quantity)
//    {
//        TransactionType = transactionType;
//        Price = price;
//        Quantity = quantity;


public class TransactionHistory {
	
	Scanner sc = new Scanner(System.in);

	public static Stack<Transaction> transactions = new Stack<>();
	
	public void addTransaction(Transaction transaction) {
		
		transactions.push(transaction);
	}
	
	public void printTransactionHistory(Scanner sc) {
	    // Print the table header
		if (transactions.isEmpty()) {
	        System.out.println("No Transactions done yet.");
	        return;
	    }

		else {
			
		
	    System.out.printf("%-15s %-15s %-10s %-15s %-15s %-25s %-20s%n", 
	                      "Transaction Type", "Stock", "Quantity", "Price/Share", 
	                      "Total Amount", "Transaction Date", "Portfolio Value");

	    System.out.println("-------------------------------------------------------------------------------------------");

	    // Print each transaction in a tabulated format
	    for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            System.out.printf("%-15s %-15s %-10d %-15.2f %-15.2f %-25s %-20.2f%n",
                              transaction.getTransactionType(),
                              transaction.getStock().getName(),
                              transaction.getQuantity(),
                              transaction.getPricePerShare(),
                              transaction.getTotalAmount(),
                              transaction.getTransactionDate(),
                              transaction.getPortfolioValueAfterTransaction());
        }
		}
	}

	
	public void searchtransaction()
	{
		
		    if (transactions.isEmpty()) {
		        System.out.println("No Transactions done yet.");
		        return;
		    }

		    System.out.print("Enter stock name to search transactions: ");
		    String stockName = sc.nextLine().trim();

		    boolean found = false;

		    System.out.printf("%-15s %-15s %-10s %-15s %-15s %-25s %-20s%n", 
		                      "Transaction Type", "Stock", "Quantity", "Price/Share", 
		                      "Total Amount", "Transaction Date", "Portfolio Value");

		    System.out.println("-------------------------------------------------------------------------------------------");

		    for (Transaction transaction : transactions) {
		        if (transaction.getStock().getName().equalsIgnoreCase(stockName)) {
		            System.out.printf("%-15s %-15s %-10d %-15.2f %-15.2f %-25s %-20.2f%n",
		                              transaction.getTransactionType(),
		                              transaction.getStock().getName(),
		                              transaction.getQuantity(),
		                              transaction.getPricePerShare(),
		                              transaction.getTotalAmount(),
		                              transaction.getTransactionDate(),
		                              transaction.getPortfolioValueAfterTransaction());
		            found = true;
		        }
		    }

		    if (!found) {
		        System.out.println("No transactions found for stock: " + stockName);
		    }
		}
	
	public void transactionMenu(Scanner sc) {
		int choice;
		do {
			System.out.println(" Transaction Menu ");
			
			System.out.println(
	                "=======================================\n" +
	                "           Select an option below:     \n" +
	                "---------------------------------------\n" +
	                " 1) View Transaction          \n" +
	                " 2) Search Transaction        \n" +
	                " 3) Go Back to Simulator Menu      \n" +
	                "=======================================\n" +
	                "Enter your choice: ");
			choice=sc.nextInt();
			
			switch(choice) {
			case 1: 
				printTransactionHistory(sc);
				break;
			case 2:
				searchtransaction();
				break;
			case 3:
				return;  
				
				default:
				System.out.println("Invalid choice. Please enter valid input");
			}
		}while(choice!=3);
	}
}

