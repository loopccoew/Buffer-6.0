package dsminiproject;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Simulator {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		RealTimeStockPrice obj = new RealTimeStockPrice();
		System.out.println("Welcome to User Registration!");
		UserAccount user=new UserAccount();
		user.createAccount(sc);
        
        int ch=0;
		
	
        Market market=new Market();
		 Portfolio portfolio=new Portfolio();
		 Wishlist mywishlist = new Wishlist();
		 TransactionHistory trackTransaction= new TransactionHistory(); 
//		 Wishlist wishlist=new Wishlist();
		 GraphCorrelation relation=new GraphCorrelation();


		// TODO Auto-generated method stub
		do {
			System.out.println("=======================================\n" +
	                   "         WELCOME TO STOCK SIMULATOR   \n" +
	                   "=======================================\n" +
	                   "           Select an option below:     \n" +
	                   "---------------------------------------\n" +
	                   " 1) Add a Stock in Market          \n" +
	                   " 2) Search Stock from Market Top      \n" +
	                   " 3) Display Stocks in Market      \n" +
	                   " 4) Filter Stocks by Cap Size         \n" +
	                   " 5) Filter Stocks by Sector           \n" +
	                   " 6) Go to Portfolio Menu              \n" +
	                   " 7) Go to Wishlist                    \n" +
	                   " 8) Go to Transactions                 \n" +
	                   " 9) Get Real Time Prices of All Stocks     \n" +
	                   "10) Get Real time Price of specific Stock   \n"+
	                   "11) See Stock Relationships    \n "+
	                   "12) Risk Analyzer\n"+
	                   "13) Exit \n" +

	                   "=======================================\n" +
	                   "Enter your choice: ");
			
		 ch=sc.nextInt();

		 sc.nextLine();
		 switch (ch) {

         case 1:
            
          //  market.create_all_stocks();
        	 System.out.println("Enter Stock Name:");
        	 String Stocknameupdatestocksymbol = sc.nextLine();
        	 System.out.println("Enter Stock Symbol:");
        	 String inputSymbol=sc.nextLine();
        	 market.updateStockSymbols(Stocknameupdatestocksymbol,inputSymbol);
             break;

         case 2:
        	 System.out.println("Enter name of stock to be search: ");
        	 //sc.nextLine(); 
        	 String name=sc.nextLine();
             market.searchStockByName(name);    
             break;

         case 3:
        	 System.out.println("=====================================\n" +
                     "            DISPLAY OPTIONS           \n" +
                     "=====================================\n" +
                     " 1) Display All Stocks               \n" +
                     " 2) Display Specific Number of Top Stocks\n" +
                     "=====================================\n" +
                     "Enter your choice: ");
        	 int displaychoice=sc.nextInt();
        	 sc.nextLine();
        	 switch(displaychoice)
        	 {
        	 case 1:
        		 market.display_all_stocks();
        		 break;
        	 case 2:
        		 System.out.println("Enter Number of Stocks to display from Top");
        		 int n=sc.nextInt();
        		 market.getTopPerformingStocks(n);
        		 break;
        	 default:
                 // default statement
                 System.out.println("Please enter a correct input.");	 
        	 }
         
            
             break;
         case 4:
        	 System.out.println("Case 4: Filter Stocks by Cap Size");
             System.out.print("Enter capsize (Small Cap, Mid Cap, Large Cap): ");
             String capSize = sc.nextLine().trim(); // User enters cap size

             // Filter stocks by the entered cap size
             List<Stock> filteredStocks = market.filterStocksByCapSize(capSize);

             // Check if any stocks were found and display them
             if (filteredStocks.isEmpty()) {
                 System.out.println("No stocks found with capsize: " + capSize);
             } else {
                 System.out.println("Stocks with capsize: " + capSize);
                 market.displayFilteredStocks(filteredStocks);
             }
            
             break;
             
             
         case 5:
        	 System.out.println("Enter sector of stock to be search: ");
        	 String sector=sc.next();
        	 market.filterStocksBySector(sector);
             break;
             
             
             
         case 6:
        	 
             portfolio.portfolio_menu();

        	 break;
        	 
         case 7:
        	
        	 mywishlist.wishlistMenu(sc);
        	 break;
             
         case 8:
        	 trackTransaction.transactionMenu(sc);
        	 break;
        	 
         case 9:
        	 market.SetRealPricesToAllStocks();
        	 market.DisplayRealPriceStock();
        	 
        	 break;
         case 10:
        	 System.out.println("Enter Stock Symbol to get Real Time Price:");
        	 String stocksymbol=sc.nextLine();
        	 try {
        	String realprice= obj.getStockPrice(stocksymbol);
        	System.out.println("Real Time Price is : "+realprice);
        	 }
        	 catch(IOException e)
        	 {
        		 e.getMessage();
        	 };
        	 break;
         case 11:
        	 
        	 relation.correlation_menu();
        	 break;
         case 12:
        	 System.out.println("Enter stock name for risk analysis: ");
     	    String stockToAnalyze = sc.nextLine();
     	    RiskAnalyzer.calculateRiskScore(stockToAnalyze);
     	    break;
         case 13:
             // case to exit the program
             System.out.println("Exiting the program");
             break;
             
             

         default:
             // default statement
             System.out.println("Please enter a correct input.");
             break;
     }
		}
		while(ch!=12);

		
	}

}








        
    



