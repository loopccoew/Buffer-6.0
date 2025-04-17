package dsminiproject;
import java.util.*;
public class Wishlist {
	
	Market market = new Market();
	private List<Stock> wishlist;
	public Wishlist() {
	 wishlist = new LinkedList<>();
	}
	
	public void addStockToWishlist(Stock stock) {
	 if (!wishlist.contains(stock)) { 
		 wishlist.add(stock);
		 System.out.println(stock.getName() + " added to wishlist.");
	 }
	else { 
		System.out.println(stock.getName() + " is already in wishlist.");
	}
	}
	
	
	public void removeStockFromWishlist(Stock stock) {
	if (wishlist.contains(stock)) {
		wishlist.remove(stock);
		System.out.println(stock.getName() + " removed from wishlist.");
	}
	 
	else {
	System.out.println(stock.getName() + " not found in wishlist.");
	}
	}
	
	public List<Stock> getWishlist() {
		
	
	return wishlist;
	}
	public void displayWishlist() {
	    List<Stock> wishlist = getWishlist();

	    if (wishlist.isEmpty()) {
	        System.out.println("Your wishlist is currently empty.");
	        return;
	    }

	    // Column headers with consistent widths for readability
	    String nameFormat = "%-20s";
	    String sectorFormat = "%-15s";
	    String marketCapFormat = "%-15s";
	    String capSizeFormat = "%-12s";
	    String priceFormat = "%-10s";
	    String volumeFormat = "%-10s";
	    String volatilityFormat = "%-10s";

	    System.out.println("Wishlist:");
	    System.out.println("--------------------------------------------------------------------------------------------------------------------------");
	    System.out.printf(nameFormat + sectorFormat + marketCapFormat + capSizeFormat + priceFormat + volumeFormat + volatilityFormat + "%n",
	                      "Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
	    System.out.println("--------------------------------------------------------------------------------------------------------------------------");

	    // Loop through each stock in the wishlist and print in formatted columns
	    for (Stock stock : wishlist) {
	        System.out.printf(nameFormat + sectorFormat + "%-15.2f" + capSizeFormat + "%-10.2f" + volumeFormat + volatilityFormat + "%n",
	                          stock.getName(),
	                          stock.getSector(),
	                          stock.getMarketCap(),
	                          stock.getCapsize(),
	                          stock.getPrice(),
	                          stock.getVolume(),
	                          stock.getVolatility());

	        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
	    }
	}

	
	public boolean checkStockInWishlist(Stock stock)
	{
	return wishlist.contains(stock);
	}
	public void notifyWhenProfitIncreases(Stock stock, double targetProfit)
	{
	// Implement logic to monitor stock profit and notify user
	System.out.println("Notification set for " + stock.getName() + " when profit increases by " + targetProfit + "%.");
	}
	// Menu method for wishlist
    public void wishlistMenu(Scanner sc) {
        
        int choice;

        do {
            System.out.println("\n=====================================");
            System.out.println("            WISHLIST MENU            ");
            System.out.println("=====================================");
            System.out.println("1) Display Wishlist");
            System.out.println("2) Add Stock to Wishlist");
            System.out.println("3) Remove Stock from Wishlist");
            System.out.println("4) Check if Stock is in Wishlist");
            System.out.println("5) Set Profit Increase Notification");
            System.out.println("6) Go Back to Simulator Menu");
            System.out.println("=====================================");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    displayWishlist();
                    break;
                case 2:
                    System.out.print("Enter stock name to add: ");
                    String stockNameToAdd = sc.nextLine();
                    Stock stockToAdd = market.searchStockByName(stockNameToAdd);
                    addStockToWishlist(stockToAdd);
                    break;
                case 3:
                    System.out.print("Enter stock name to remove: ");
                    String stockNameToRemove = sc.nextLine();
                    Stock stockToRemove = market.searchStockByName(stockNameToRemove);
                    removeStockFromWishlist(stockToRemove);
                    break;
                case 4:
                    System.out.print("Enter stock name to check: ");
                    String stockNameToCheck = sc.nextLine();
                    Stock stockToCheck = market.searchStockByName(stockNameToCheck);
                    if (checkStockInWishlist(stockToCheck)) {
                        System.out.println(stockNameToCheck + " is in your wishlist.");
                    } else {
                        System.out.println(stockNameToCheck + " is not in your wishlist.");
                    }
                    break;
                case 5:
                    System.out.print("Enter stock name for notification: ");
                    String stockNameForNotification = sc.nextLine();
                    System.out.print("Enter target profit percentage: ");
                    double targetProfit = sc.nextDouble();
                    Stock stockForNotification = new Stock(stockNameForNotification, "", 0, 0, 0, 0, ""); // Adjusted for demonstration
                    notifyWhenProfitIncreases(stockForNotification, targetProfit);
                    break;
                case 6:
                    System.out.println("Exiting Wishlist Menu...");
                    return;
                    
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        } while (choice != 6);

        
    }
}
