package main;
import java.util.*;
import loader.CSVLoader;
import core.CRUD_op;
import model.StockEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
    	
    	
    	/*Loading data from csv file to stockmap for access*/
    	String folderPath = "data";
    	Map<String, List<StockEntry>> stockMap = CSVLoader.loadAllStocks(folderPath);
    	
    	CRUD_op store = new CRUD_op(stockMap);  //creating object of CRUD_op class to access it's methods
    	
    	int con;
    	Scanner sc=new Scanner(System.in);
    	System.out.println("WELCOME TO STOCK TREND ANALYZER!");
    	System.out.println("");
    	System.out.println("GET STARTED BY PRESSING 1");
    	int start=sc.nextInt();
    	if(start==1) {
    		System.out.println("Please select your login mode: \n1.User\n2.Admin");  //Login Mode
    		int mode=sc.nextInt();
    		/*User mode: 5 core functionalities have been provided*/
    		if(mode==1) {   
    			do {
    	    		System.out.println("Menu: \n1.Display all Stocks\n2.Search"
    	    				+ " for a Particular Stock \n3.Find Highest & Lowest Stock Prices Over"
    	    				+ " a Time Period\n4.Calculate Moving Averages for a recent time period\n5.Detect " 
    	    				+"Bullish or Bearish Trends for a Particular Stock\n6.Find out Top k Highest/Lowest"
    	    				+ " Prices Over a Time Period\n7.Get technical analysis for a particular stock");
    	    	
    	    	int choice=sc.nextInt();
    	    	switch(choice) {
    	    	
    	    	case 1:
    	    		//Displaying all the stocks in a tabular form
    	    		System.out.println("Data for all the Stocks:");
    	    		for (String sys : stockMap.keySet()) {
    	    		    System.out.println(sys + " Stock Data:");
    	    		    System.out.println();
    	    		    System.out.printf("%-12s | %-8s | %-8s | %-8s | %-8s | %-12s\n", 
    	    		                      "Date", "Open", "High", "Low", "Close", "Volume");
    	    		    System.out.println("---------------------------------------------------------------");
    	    		    
    	    		    List<StockEntry> entries = stockMap.get(sys);
    	    		    for (StockEntry ent : entries) {
    	    		        System.out.println(ent);
    	    		    }
    	    		    System.out.println();
    	    		}
    	    		
    	    		break;
    	    		
    	    	case 2:
    	    		//search
    	    		System.out.println("Enter stock symbol (e.g., AAPL) to search for: ");
    	    		String sys=sc.next();
    	              System.out.println(sys + " Stock Data:");
    	              List<StockEntry> entries = stockMap.get(sys);
    	              for (StockEntry ent : entries) {
    	                  System.out.println(ent);
    	              }    		
    	              System.out.println();
    	    		break;
    	    		
    	    	case 3:
    	    		//Finding Highest and Lowest stock prices for a particular stock
    	    		System.out.println("Choose an option:");
    	            System.out.println("1. Find maximum closing price");
    	            System.out.println("2. Find minimum closing price");
    	            int userChoice = sc.nextInt();

    	            if (userChoice == 1) {
    	                store.findClosingPriceInRange(true);  // true means find maximum. Calling function from CRUD_op class
    	            } else   {
    	                store.findClosingPriceInRange(false); // false means find minimum. Calling function from CRUD_op class
    	            }
    	    		System.out.println();
    	    		break;
    	    		
    	    	case 4:
    	    		//Finding rolling averages for latest k days
    	    		System.out.print("Enter stock symbol (e.g., AAPL): ");
    	    		String symbolForAvg = sc.next();
    	    		System.out.print("Enter window size for moving average (e.g., 7, 30): ");
    	    		int window = sc.nextInt();
    	    		double avg = store.calculateMovingAverage(symbolForAvg, window);
    	    		if (avg != -1) {
    	    			System.out.printf("%d-day Moving Average for %s = %.2f\n", window, symbolForAvg, avg);
    	    		}
    	    		System.out.println();
    	    		break;
    	    		
    	    	case 5:
    	    		//Finding trend, whether bullish or bearish for a particular stock
    	    		System.out.print("Enter stock symbol (e.g., AAPL): ");
    	    		String trendSymbol = sc.next();
    	    		System.out.print("Enter number of days to analyze for trend: ");
    	    		int trendDays = sc.nextInt();
    	    		String trendResult = store.detectTrend(trendSymbol, trendDays);
    	    		System.out.println(trendResult);
    	    		System.out.println();
    	    		break;
    	    		
    	    	case 6:
    	    		//Finding k highest or lowest stock entries from all stocks/ all data
    	    		System.out.print("Enter value of K: ");
    	            int k = sc.nextInt();
    	            sc.nextLine(); 

    	            System.out.print("Enter start date (yyyy-MM-dd): ");
    	            String startInput = sc.nextLine().trim();
    	            if (startInput.isEmpty()) {
    	                System.out.println("Start date cannot be empty.");
    	                return;
    	            }
    	            LocalDate star = LocalDate.parse(startInput);

    	            System.out.print("Enter end date (yyyy-MM-dd): ");
    	            String endInput = sc.nextLine().trim();
    	            if (endInput.isEmpty()) {
    	                System.out.println("End date cannot be empty.");
    	                return;
    	            }
    	            LocalDate end = LocalDate.parse(endInput);

    	            System.out.print("Find top K (1 for highest, 2 for lowest): ");
    	            int cho = sc.nextInt();
    	            boolean findMax = (cho == 1);

    	            store.findTopKClosingPrices(k, star, end, findMax);
    	            System.out.println();
    	    		break;
    	    		
    	    	case 7:
    	    		System.out.print("Enter Stock Symbol: ");
    	    	    String summarySymbol = sc.next().toUpperCase();
    	    	    System.out.println("\nTechnical Summary for " + summarySymbol);
    	    	    String summary = store.getTechnicalSummary(summarySymbol);
    	    	    System.out.println("Summary: " + summary);
    	    	    System.out.println("Analyst Sentiment: " + (summary.contains("Buy") ? "Buy" : "Sell"));
    	    	    System.out.println("Predicted Target Price: $" + String.format("%.2f", store.predictPriceTarget(summarySymbol)));
    	    	    break;
    	    		
    	    		default:
    	    			System.out.println("You have entered incorrect choice");
    	    			break;
    	    		
    	    	}
    	    	System.out.println("Do you wish to continue? 1.Yes 2.No");
    	    	con=sc.nextInt();
    	    	}
    	    	while(con==1);
    		}
    		/*Admin mode: CRUD operations have been implemented, which are accessible only by the admins*/
    		else if(mode==2){
    			System.out.println("Enter your secret passkey");
    			String pass=sc.next();
    			if(pass.equals("Pass@123")) {
    		do {
    		System.out.println("Menu: \n1.Create Stock Entry for a Particular Stock\n2.Update Stock Entry for a Particular Stock \n3.Delete "
    				+ "Stock Entry for a Particular Stock");
    	
    	int choice=sc.nextInt();
    	switch(choice) {
    		
    	case 1:
    		//create
    		store.add();
    		System.out.println();
    		break;
    		
    		
    	case 2:
    		//update
    		store.update();
    		System.out.println();
    		break;
    		
    	case 3:
    		//delete
    		store.delete();
    		System.out.println();
    		break;
    		
    		default:
    			System.out.println("You have entered incorrect choice");
    			break;
    		
    	}
    	System.out.println("Do you wish to continue? 1.Yes 2.No");
    	con=sc.nextInt();
    	}
    	while(con==1);
    		}
    			else {
    				System.out.println("Incorrect password. Pls try again :( ");
    			}
    		}
    		else {
    			System.out.println("You enterred incorrect choice");
    		}
    	
    	}
    	System.out.println("THANKYOU FOR VISITING");
	
    }
}