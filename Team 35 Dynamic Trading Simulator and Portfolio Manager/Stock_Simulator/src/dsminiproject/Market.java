package dsminiproject;
import java.io.*;
import java.util.*;
import java.util.Scanner;

public class Market {
	HashMap<Stock,String> RealPriceStock = new HashMap<>();

	RealTimeStockPrice objtocallmethod = new RealTimeStockPrice();
	private PriorityQueue<Stock> topPerformingStocks; // Max Heap for top performing stocks by volume or cap
	private BinarySearchTree<Stock> stockPriceTree; // BST for sorted stock prices
	public  PriorityQueue<Stock> allStocks;
    private BinarySearchTree<Stock> stockTree;

	
	String capsize=null;

	Scanner sc = new Scanner(System.in);

	public void loadStocks() {
	     File file = new File("stocks.dat");
	     if (!file.exists()) {
	         try {
	             // If the file doesn't exist, create a new one
	             file.createNewFile();
	             System.out.println("Created new file: stocks.dat");
	         } catch (IOException e) {
	             System.out.println("Error creating file: " + e.getMessage());
	         }
	     } else {
	         try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	             // Code to load the stocks from the file
	         } catch (IOException e) {
	             System.out.println("Error loading stocks from file: " + e.getMessage());
	         } 
	     }
	 }

//	public void saveStocksToFile() {
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("stocks.dat"))) {
//            out.writeObject(allStocks);
//        } catch (IOException e) {
//            System.out.println("Error saving stocks: " + e.getMessage());
//        }
//    }

	public void loadStocksFromFile() {
	     try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("stocks.dat"))) {
	         allStocks = (PriorityQueue<Stock>) in.readObject();
	     } catch (IOException | ClassNotFoundException e) {
	         System.out.println("Error loading stocks: " + e.getMessage());
	         allStocks = new PriorityQueue<>(); // Initialize if file not found
	     }
	 }

	public void updateStockSymbols(String stockName, String defaultSymbol) {
	    // Read the current stocks from the file
	    File file = new File("stocks.dat");
	    if (file.exists()) {
	        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
	            allStocks = (PriorityQueue<Stock>) in.readObject();
	            System.out.println("Stocks loaded from file for symbol update.");

	            boolean stockFound = false;
	            // Iterate over the existing stocks and update the symbol for the specific stockName
	            for (Stock stock : allStocks) {
	                if (stock.getName().equalsIgnoreCase(stockName)) {  // Match by stock name
	                    if (stock.getSymbol() == null || stock.getSymbol().isEmpty()) {
	                        stock.setSymbol(defaultSymbol);  // Update the symbol if it's null or empty
	                        saveStocksToFile();
	                        System.out.println("Symbol for " + stockName + " updated to " + defaultSymbol);
	                        stockFound = true;
	                        break;  // Exit the loop once the stock is found and updated
	                    } else {
	                        System.out.println("Stock " + stockName + " already has a symbol: " + stock.getSymbol());
	                        stockFound = true;
	                        break;
	                    }
	                }
	            }

	            if (!stockFound) {
	                System.out.println("Stock with name " + stockName + " not found.");
	            } else {
	                // Save the updated stocks back to the file
	                saveStocksToFile();
	                System.out.println("Stock symbols updated.");
	            }
	        } catch (IOException | ClassNotFoundException e) {
	            System.out.println("Error updating stock symbols: " + e.getMessage());
	        }
	    } else {
	        System.out.println("The file does not exist or is empty.");
	    }
	}

	private void saveStocksToFile() {
	    // Implement this method to save the updated PriorityQueue<Stock> back to "stocks.dat"
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("stocks.dat"))) {
	        out.writeObject(allStocks);
	        System.out.println("Stocks saved to file.");
	    } catch (IOException e) {
	        System.out.println("Error saving stocks to file: " + e.getMessage());
	    }
	}

    // Call this method at startup to load stocks from the file
    public Market() {
    	loadStocks();
        loadStocksFromFile();
        stockTree = new BinarySearchTree<>();

    }

	
	public void create_all_stocks() {
		
	
		
		System.out.println("Enter Name:");
         String name=sc.nextLine();
        		 
		System.out.println("Enter sector:");
		String sector=sc.nextLine();
         
		System.out.println("Enter volume:");
		double volume=sc.nextDouble(); 
	    sc.nextLine();  // Consume the newline character after nextDouble()

			
		System.out.println("Enter price  ");
		double price=sc.nextDouble();
	    sc.nextLine();  // Consume the newline character after nextDouble()

		
		System.out.println("Enter volatality:");
		double volatility=sc.nextDouble();
	    sc.nextLine();  // Consume the newline character after nextDouble()

		
		
		
		double marketcap=calculate_MarketCap(price,volume);
		 capsize=calculate_capsize(marketcap);
		
		Stock Stock = new Stock(name,sector,volume,price,volatility,marketcap,capsize);
		allStocks.add(Stock);
	    saveStocksToFile(); // Save after adding
		 
		
	}
	
	public double calculate_MarketCap(double price, double volume)
	{
	
		double marketcap;
		marketcap=price*volume; //in crores
		return marketcap;
   }
	public String calculate_capsize(double marketcap)
	{
		
		if(marketcap>=128000000)
		{
			return "Large Cap";
		}
		else if(marketcap<128000000 && marketcap>5000000)
		{
			return "Mid Cap";
		}
		else {
			return "Small Cap";
		}
		
	}

//	public void addStock() {
//		System.out.println("Enter Name:");
//        String name=sc.nextLine();
//       		 
//		System.out.println("Enter sector:");
//		String sector=sc.nextLine();
//        
//		System.out.println("Enter volume:");
//		double volume=sc.nextDouble(); 
//			
//		System.out.println("Enter price  ");
//		double price=sc.nextDouble();
//		
//		System.out.println("Enter volatality:");
//		double volatility=sc.nextDouble();
//		
//		
//		double marketcap=calculate_MarketCap(price,volume);
//		 capsize=calculate_capsize(marketcap);
//		
//		Stock Stock = new Stock(name,sector,volume,price,volatility,marketcap,capsize);
//
//		
//	}

	public Stock searchStockByName(String name) {
	    // Check if allStocks is initialized or empty
	    if (allStocks == null || allStocks.isEmpty()) {
	        System.out.println("No stocks available in the market.");
	        return null;
	    }

	    // Trim any extra spaces and make search case-insensitive
	    name = name.trim();

	    // Search for the stock with the specified name
	    for (Stock stock : allStocks) {
	        if (stock.getName().equalsIgnoreCase(name)) {
	            System.out.println("Stock found: " + stock.getName());

	            // Display formatted stock details in a table format
	            System.out.println("---------------------------------------------------------------------------------");
	            System.out.printf("%-15s %-15s %-15s %-12s %-10s %-10s %-10s%n", 
	                              "Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
	            System.out.println("---------------------------------------------------------------------------------");
	            System.out.printf("%-15s %-15s %-15.2f %-12s %-10.2f %-10.2f %-10.2f%n", 
	                              stock.getName(),
	                              stock.getSector(),
	                              stock.getMarketCap(),
	                              stock.getCapsize(),
	                              stock.getPrice(),
	                              stock.getVolume(),
	                              stock.getVolatility());
	            return stock;
	        }
	    }

	    // If the stock with the specified name is not found
	    System.out.println("Stock with name '" + name + "' not found in the market.");
	    return null;
	}



	public void display_all_stocks() {
	    if (allStocks.isEmpty()) {
	        System.out.println("No stocks available in the market.");
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

	    System.out.println("Stocks in the Market:");
	    System.out.println("--------------------------------------------------------------------------------------------------------------------------");
	    System.out.printf(nameFormat + sectorFormat + marketCapFormat + capSizeFormat + priceFormat + volumeFormat + volatilityFormat + "%n",
	                      "Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
	    System.out.println("--------------------------------------------------------------------------------------------------------------------------");

	    // Adjusted formatting to display each stock's data with two decimal places where applicable
	    for (Stock stock : allStocks) {
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


	// Method to filter stocks by market cap size
	public List<Stock> filterStocksByCapSize(String capsize) {
        List<Stock> filteredStocks = new ArrayList<>();

        // Check if allStocks is initialized
        if (allStocks == null || allStocks.isEmpty()) {
            System.out.println("No stocks available in the market.");
            return filteredStocks; // Return empty list if no stocks
        }

        // Iterate through all the stocks
        for (Stock stock : allStocks) {
            if (stock.getCapsize().equalsIgnoreCase(capsize)) {
                filteredStocks.add(stock);
            }
        }
        return filteredStocks;
    }

    // Method to display the filtered stocks
    public void displayFilteredStocks(List<Stock> filteredStocks) {
        if (filteredStocks.isEmpty()) {
            System.out.println("No stocks found.");
            return;
        }

        // Displaying the filtered stocks with proper formatting
        String nameFormat = "%-20s";
        String sectorFormat = "%-15s";
        String marketCapFormat = "%-15s";
        String capSizeFormat = "%-12s";
        String priceFormat = "%-10s";
        String volumeFormat = "%-10s";
        String volatilityFormat = "%-10s";

        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        System.out.printf(nameFormat + sectorFormat + marketCapFormat + capSizeFormat + priceFormat + volumeFormat + volatilityFormat + "%n",
                          "Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");

        // Display each stock's data in the filtered list
        for (Stock stock : filteredStocks) {
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
	

	public List<Stock> filterStocksBySector(String sector) {
	    // Create a list to store the filtered stocks
	    List<Stock> filteredStocks = new ArrayList<>();

	    // Check if allStocks is initialized
	    if (allStocks == null || allStocks.isEmpty()) {
	        System.out.println("No stocks available in the market.");
	        return filteredStocks; // Return empty list if no stocks
	    }

	    // Iterate through the PriorityQueue to find stocks with the matching sector
	    for (Stock stock : allStocks) {
	        if (stock.getSector().equalsIgnoreCase(sector)) { // assuming case-insensitive comparison
	            filteredStocks.add(stock);
	        }
	    }

	    // If no stocks match, notify the user
	    if (filteredStocks.isEmpty()) {
	        System.out.println("No stocks found in the sector: " + sector);
	    } else {
	        System.out.println("Found " + filteredStocks.size() + " stock(s) in the sector: " + sector);

	        // Displaying the filtered stocks
	        String nameFormat = "%-20s";
	        String sectorFormat = "%-15s";
	        String marketCapFormat = "%-15s";
	        String capSizeFormat = "%-12s";
	        String priceFormat = "%-10s";
	        String volumeFormat = "%-10s";
	        String volatilityFormat = "%-10s";

	        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
	        System.out.printf(nameFormat + sectorFormat + marketCapFormat + capSizeFormat + priceFormat + volumeFormat + volatilityFormat + "%n",
	                          "Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
	        System.out.println("--------------------------------------------------------------------------------------------------------------------------");

	        // Display each stock's data
	        for (Stock stock : filteredStocks) {
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

	    return filteredStocks;
	}


	
	// Method to get top n stocks by volume or cap size
		public List<Stock> getTopPerformingStocks(int n) {
		    // Create a list to store the top n stocks
		    List<Stock> topStocksList = new ArrayList<>();

		    topPerformingStocks=allStocks;
		    // Check if topStocks is initialized
		    if (topPerformingStocks== null || topPerformingStocks.isEmpty()) {
		        System.out.println("No stocks available in the top stocks list.");
		        return topStocksList; // Return empty list if no stocks
		    }

		    // Create a temporary priority queue to avoid modifying the original topStocks queue
		    PriorityQueue<Stock> tempQueue = new PriorityQueue<>(topPerformingStocks);

		    // Fetch the top n stocks from the priority queue
		    for (int i = 0; i < n && !tempQueue.isEmpty(); i++) {
		        topStocksList.add(tempQueue.poll());
		    }

		 // Column headers with consistent widths for readability
		    String nameFormat = "%-20s";
		    String sectorFormat = "%-15s";
		    String marketCapFormat = "%-15s";
		    String capSizeFormat = "%-12s";
		    String priceFormat = "%-10s";
		    String volumeFormat = "%-10s";
		    String volatilityFormat = "%-10s";

		    System.out.println("Stocks in the top of the Market:");
		    System.out.println("--------------------------------------------------------------------------------------------------------------------------");
		    System.out.printf(nameFormat + sectorFormat + marketCapFormat + capSizeFormat + priceFormat + volumeFormat + volatilityFormat + "%n",
		                      "Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
		    System.out.println("--------------------------------------------------------------------------------------------------------------------------");

		    // Adjusted formatting to display each stock's data with two decimal places where applicable
		    for (Stock stock : topStocksList) {
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
		    // If fewer stocks than requested were found, notify the user
		    if (topStocksList.size() < n) {
		        System.out.println("Only " + topStocksList.size() + " top stocks are available.");
		    } else {
		        System.out.println("Retrieved top " + n + " stocks.");
		    }

		    return topStocksList;
		}

		public void SetRealPricesToAllStocks()
		{
			
			for (Stock stock : allStocks) {
				try {
				RealPriceStock.put(stock,objtocallmethod.getStockPrice(stock.getSymbol()));
			//		RealPriceStock.put(allStocks.peek(),objtocallmethod.getStockPrice(allStocks.peek().getSymbol()));
				}
				catch(IOException e)
				{
					e.getMessage();
				}
				
		        }
		    }
		public void DisplayRealPriceStock() {
		    if (RealPriceStock.isEmpty()) {
		        System.out.println("No real-time prices available for stocks.");
		        return;
		    }

		    System.out.println("Real-time Stock Prices:");
		    System.out.println("-----------------------------------------------------------------------------------------");
		    String nameFormat = "%-20s";
		    String priceFormat = "%-15s";
		    System.out.printf(nameFormat + priceFormat + "%n", "Stock Name", "Real Price");
		    System.out.println("-----------------------------------------------------------------------------------------");

		    // Iterate over the hashmap and display stock names with their real prices
		    for (Map.Entry<Stock, String> entry : RealPriceStock.entrySet()) {
		        Stock stock = entry.getKey();
		        String realPrice = entry.getValue();
		        System.out.printf(nameFormat + priceFormat + "%n", stock.getName(), realPrice);
		    }

		    System.out.println("-----------------------------------------------------------------------------------------");
		}
		
		
		
		
		 public List<Stock> filterAndSearch(String capSize) {
		        List<Stock> filteredStocks = new ArrayList<>();
		        List<Stock> allStocks = stockTree.getAllNodes();

		        for (Stock stock : allStocks) {
		            if (capSize.equals("Large Cap") && stock.getMarketCap() >= 10) {
		                filteredStocks.add(stock);
		            } else if (capSize.equals("Mid Cap") && stock.getMarketCap() >= 2 && stock.getMarketCap() < 10) {
		                filteredStocks.add(stock);
		            } else if (capSize.equals("Small Cap") && stock.getMarketCap() < 2) {
		                filteredStocks.add(stock);
		            }
		        }
		        return filteredStocks;
		    }

		    public void display_FilteredStocks(List<Stock> filteredStocks) {
		        for (Stock stock : filteredStocks) {
		            System.out.printf("Stock: %-15s Cap Size: %-15s Market Cap: %.2f%n", 
		                               stock.getName(), stock.getCapsize(), stock.getMarketCap());
		        }
		    }

}



