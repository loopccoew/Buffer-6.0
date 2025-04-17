package dsminiproject;
import javax.swing.SwingUtilities;

import java.util.*;

public class Portfolio {

	TransactionHistory trackTransaction= new TransactionHistory();
	private HashMap<Stock, Integer> holdings = new HashMap<>();
	private double totalPortfolioValue=0;
	private double MyMoney=0;
	UserAccount user = new UserAccount();
	Portfolio()
	{
		MyMoney=user.getInitialMoney();
	}
	


	


	// private double MyMoney=9000;
	Market market = new Market();

	public void portfolio_menu() {
		int ch;
		Scanner sc = new Scanner(System.in);
		// TODO Auto-generated method stub
		do {
			

			 System.out.println("=====================================\n" + 
                     "               MENU                 \n" +
                     "=====================================\n" + 
                     " 1) Add Stock to Portfolio (Buy Stock)\n" +
                     " 2) Remove Stock from Portfolio (Sell Stock)\n" + 
                     " 3) Search Stock in Portfolio\n" +
                     " 4) Display All Stocks in Portfolio\n" + 
                     " 5) Filter Stocks by Cap Size\n" +
                     " 6) Filter Stocks by Sector\n" + 
                     " 7) See Portfolio Analysis Chart Pop-Up Menu\n" +
                     " 8) Portfolio Risk Analyzer \n " +
                     " 9) Go Back to Simulator Menu\n" + 
                     " 10) Exit\n" +
                     "=====================================\n" + 
                     "Enter your choice: ");
			ch = sc.nextInt();

			sc.nextLine();

			switch (ch) {

			case 1:

				System.out.println("Enter name of stock to be added: ");
				String name = sc.nextLine();
				System.out.println("Enter the quantity of stock to be added:");
				int quantity = sc.nextInt();
				addStock(name, quantity);
				break;

			case 2:
				System.out.println("Enter name of stock to be removed: ");
				String nameremoved = sc.nextLine();
				System.out.println("Enter the quantity of stock to be removed:");
				int quantityremoved = sc.nextInt();
				removeStock(nameremoved,quantityremoved);
				break;

			case 3:

				System.out.println("Enter name of stock to be search: ");
				String nameofStock = sc.nextLine();
				searchStockByName(nameofStock);
				break;

			case 4:

				viewPortfolio();
				break;
			case 5:

				System.out.println("Enter capsize (Small Cap, Mid Cap, Large Cap) of stock to be search: ");
				String capsize = sc.nextLine();
				List<Stock>capsizestocks=filterStocksByCapSize(capsize);
				displayFilteredStocks(capsizestocks);
				//System.out.println("Case 4: Filter Stocks by Cap Size");
//	             System.out.print("Enter capsize (Small Cap, Mid Cap, Large Cap) of stock to be search: ");
//	             String capSize = sc.nextLine().trim(); // User enters cap size
//
//	             // Filter stocks by the entered cap size
//	             List<Stock> filteredStocks = market.filterStocksByCapSize(capSize);

	             // Check if any stocks were found and display them
	             if (capsizestocks.isEmpty()) {
	                 System.out.println("No stocks found with capsize: " + capsize);
	             } else {
	                 //System.out.println("Stocks with capsize: " + capSize);
	                // market.displayFilteredStocks(filteredStocks);
	             }
				break;

			case 6:
				System.out.println("Enter sector of stock to be search: ");
				String sector = sc.nextLine();
				//market.filterStocksBySector(sector);
				List<Stock>sectorstocks=filterStocksBySector(sector);
				//displayFilteredStocks(sectorstocks);

				break;
				
			case 7:
//				Analysis analysis= new Analysis(holdings);
				SwingUtilities.invokeLater(() -> {
			        new Analysis(holdings);  // your HashMap<Stock, Integer>
			    });
//				analysis.display_menu(sc);
	            break;
			case 8:
				PortfolioRiskAnalyzer analyze = new PortfolioRiskAnalyzer();
				List<Stock> stockUniverse = new ArrayList<>(market.allStocks);

				analyze.riskAndDiversificationAnalysis(new ArrayList<>(holdings.keySet()), stockUniverse);
				break;
			case 9:
				System.out.println("Thank you. Going Back to Simulator Menu");
				return;

			case 10:
				// case to exit the program
				System.out.println("Exiting the program");
				break;

			default:
				// default statement
				System.out.println("Please enter a correct input.");
				break;
			}
		} while (ch != 10);
	}

	// Method to add stock to portfolio
	public void addStock(String stockname, int quantity) {
		// Search for the stock by name
		double totalCost = 0;
		Stock stockToAdd = market.searchStockByName(stockname);

		// Check if the stock exists and if user has enough money to buy the stock
		if (stockToAdd != null) {
			if (MyMoney >= stockToAdd.getPrice() * quantity) {

				// Check if the stock already exists in holdings
				boolean stockExists = false;
				for (Stock holding : holdings.keySet()) {
					if (holding.getName().equalsIgnoreCase(stockname)) {
						// If the stock already exists, update the quantity
						int currentQuantity = holdings.get(holding); // Get current quantity
						holdings.put(holding, currentQuantity + quantity); // Update quantity
						stockExists = true;
						totalPortfolioValue+=holding.getPrice()*holdings.get(holding);
						
						Transaction transaction=new Transaction("Buy", stockToAdd, quantity, stockToAdd.getPrice(), totalPortfolioValue);
						trackTransaction.addTransaction(transaction);
						break;
					}
				}

				// If the stock doesn't exist in holdings, add a new entry
				if (!stockExists) {
					holdings.put(stockToAdd, quantity); // Add new stock with quantity
					totalPortfolioValue+=stockToAdd.getPrice()*holdings.get(stockToAdd);
					Transaction transaction=new Transaction("Buy", stockToAdd, quantity, stockToAdd.getPrice(), totalPortfolioValue);
							trackTransaction.addTransaction(transaction);
				}

				// Deduct the total cost of the stock bought (price * quantity) from MyMoney
				totalCost = stockToAdd.getPrice() * quantity;
				MyMoney -= totalCost;

				System.out.println("Stock added: " + stockToAdd.getName() + " | Quantity: " + quantity);
				System.out.println("Total cost: " + totalCost);
				System.out.println("Remaining Money: " + MyMoney);

				// Update the total value of the portfolio (holdings)
//				double totalPortfolioValue=updatePortfolioValue();
			} else {

				System.out.println("Total cost of stock: " + totalCost);
				System.out.println("Money in your Account: " + MyMoney);
				System.out.println(
						"Sorry! You don't have enough money to buy this stock. Try to reduce quantity or choose another stock.");
			}
		} else {
			System.out.println("Stock not found: " + stockname);
		}
	}

	// Method to update the total value of the portfolio
//	public double updatePortfolioValue() {
//		double totalValue=0;
//		for (Stock stockobj : holdings.keySet()) {
//			totalValue += stockobj.getPrice() * holdings.get(stockobj);
//		}
//		///System.out.println("Total portfolio value: " + totalValue);
//		
//		return totalValue;
//	}

	// Method to remove stock from portfolio
	public void removeStock(String stockname, int quantity) {

		// Search for the stock by name
				double totalCost = 0;
				Stock stockToRemove = market.searchStockByName(stockname);

				// Check if the stock exists and if user has enough money to buy the stock
				if (stockToRemove != null) {
					

						// Check if the stock already exists in holdings
						boolean stockExists = false;
						for (Stock holding : holdings.keySet()) {
							if (holding.getName().equalsIgnoreCase(stockname)) {
								stockExists = true;
								// If the stock already exists, update the quantity
								int currentQuantity = holdings.get(holding); // Get current quantity
								if(currentQuantity - quantity>0) {
								holdings.put(holding, currentQuantity - quantity); // Update quantity
								// ADD the total cost of the stock bought (price * quantity) to MyMoney
								totalCost = stockToRemove.getPrice() * quantity;
								MyMoney += totalCost;
								System.out.println("Stock removed: " + stockToRemove.getName() + " | Quantity Removed: " + quantity);
								System.out.println("Total cost: " + totalCost);
								System.out.println("Remaining Money: " + MyMoney);
								totalPortfolioValue-=stockToRemove.getPrice()*holdings.get(holding);
								Transaction transaction=new Transaction("Sell", stockToRemove, quantity, stockToRemove.getPrice(), totalPortfolioValue);
								trackTransaction.addTransaction(transaction);
								
								}
								else if(currentQuantity - quantity==0)
								{
									holdings.remove(stockToRemove);
									// ADD the total cost of the stock bought (price * quantity) to MyMoney
									totalCost = stockToRemove.getPrice() * quantity;
									MyMoney += totalCost;
									System.out.println("Stock removed: " + stockToRemove.getName() + " | Quantity Removed: " + quantity);
									System.out.println("Total cost: " + totalCost);
									System.out.println("Remaining Money: " + MyMoney);
									Transaction transaction=new Transaction("Sell", stockToRemove, quantity, stockToRemove.getPrice(), totalPortfolioValue);
									trackTransaction.addTransaction(transaction);

								}
								else
								{
									System.out.println("Check your Stock Quantity");
								}
								
								break;
							}
						}

						// If the stock doesn't exist in holdings, add a new entry
						if (!stockExists) {
							System.out.println("This Stock does not exist in your portfolio. Please try again.");
						}

						
						

						// Update the total value of the portfolio (holdings)
						//updatePortfolioValue();
					} 
				 else {
					System.out.println("Stock not found: " + stockname);
				}
			
	}

	// Method to calculate total portfolio value
	

	// Method to view portfolio holdings
	public void viewPortfolio() {

		if (holdings.keySet().isEmpty()) {
			System.out.println("No stocks available in the portfolio.");
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

		System.out.println("Stocks in the Portfolio:");
		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------");
		System.out.printf(
				nameFormat + sectorFormat + marketCapFormat + capSizeFormat + priceFormat + volumeFormat
						+ volatilityFormat + "\t Quantity " + "%n",
				"Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------");

		// Adjusted formatting to display each stock's data with two decimal places
		// where applicable
		for (Stock stock : holdings.keySet()) {
			System.out.printf(
					nameFormat + sectorFormat + "%-15.2f" + capSizeFormat + "%-10.2f" + volumeFormat + volatilityFormat
							+ "\t "+ holdings.get(stock) + "%n",
					stock.getName(), stock.getSector(), stock.getMarketCap(), stock.getCapsize(), stock.getPrice(),
					stock.getVolume(), stock.getVolatility());

			System.out.println(
					"--------------------------------------------------------------------------------------------------------------------------");
		}
		
		
	}
	public Stock searchStockByName(String name) {
	    // Check if allStocks is initialized or empty
	    if (holdings == null || holdings.isEmpty()) {
	        System.out.println("No stocks available in the portfolio.");
	        return null;
	    }

	    // Trim any extra spaces and make search case-insensitive
	    name = name.trim();

	    // Search for the stock with the specified name
	    for (Stock stock : holdings.keySet()) {
	        if (stock.getName().equalsIgnoreCase(name)) {
	            System.out.println("Stock found: " + stock.getName());

	            // Display formatted stock details in a table format
	            System.out.println("---------------------------------------------------------------------------------");
	            System.out.printf("%-15s %-15s %-15s %-12s %-10s %-10s %-10s" +"\t Quantity" + "%n", 
	                              "Name", "Sector", "Market Cap", "Cap Size", "Price", "Volume", "Volatility");
	            System.out.println("---------------------------------------------------------------------------------");
	            System.out.printf("%-15s %-15s %-15.2f %-12s %-10.2f %-10.2f %-10.2f"+"\t "+holdings.get(stock)+"%n", 
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
	    System.out.println("Stock with name '" + name + "' not found in the Portfolio.");
	    return null;
	}
	public List<Stock> filterStocksByCapSize(String capsize) {
        List<Stock> filteredStocks = new ArrayList<>();

        // Check if allStocks is initialized
        if (holdings.keySet() == null || holdings.keySet().isEmpty()) {
            System.out.println("No stocks available in the Portfolio.");
            return filteredStocks; // Return empty list if no stocks
        }

        // Iterate through all the stocks
        for (Stock stock : holdings.keySet()) {
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
	    if (holdings.keySet() == null || holdings.keySet().isEmpty()) {
	        System.out.println("No stocks available in the Portfolio.");
	        return filteredStocks; // Return empty list if no stocks
	    }

	    // Iterate through the PriorityQueue to find stocks with the matching sector
	    for (Stock stock : holdings.keySet()) {
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


}
