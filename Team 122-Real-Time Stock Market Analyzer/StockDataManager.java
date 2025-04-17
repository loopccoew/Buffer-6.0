package buffer_files;
import java.util.*;
public class StockDataManager {
    private List<String> stockSymbols;
    private List<String> timeStamps;
    private Map<String, List<Double>> stockPrices;

    public void setStockSymbols(List<String> symbols) { this.stockSymbols = symbols; }
    public void setTimeStamps(List<String> times) { this.timeStamps = times; }
    public void setStockPrices(Map<String, List<Double>> prices) { this.stockPrices = prices; }

    public List<String> getStockSymbols() { return stockSymbols; }
    public List<String> getTimeStamps() { return timeStamps; }
    public Map<String, List<Double>> getStockPrices() { return stockPrices; }

    public void displayMenu(MovingAverageCalculator maCalc, MinMaxTracker tracker, ProfitCalculator profitCalc) {
        Scanner sc = new Scanner(System.in);
        int choice = -1;
        while (choice != 4) {
            System.out.println("\nChoose an option:");
            System.out.println("[1] View Price Table");
            System.out.println("[2] View Analysis (MA + Min/Max + Action)");
            System.out.println("[3] Run Max Profit Calculation");
            System.out.println("[4] Exit");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> printPriceTable();
                case 2 -> displayAnalysis(maCalc, tracker);
                case 3 -> runProfitCalc(profitCalc);
            }
        }
    }

    private void printPriceTable() {
        System.out.printf("%-8s", "Time");
        for (String s : stockSymbols) System.out.printf("%-8s", s);
        System.out.println();
        for (int i = 0; i < timeStamps.size(); i++) {
            System.out.printf("%-8s", timeStamps.get(i));
            for (String s : stockSymbols)
            	System.out.printf("%-8.2f", stockPrices.get(s).get(i));

            System.out.println();
        }
    }


    private void displayAnalysis(MovingAverageCalculator maCalc, MinMaxTracker tracker) {
        for (String stock : stockSymbols) {
            List<Double> prices = stockPrices.get(stock);
            System.out.println("\n📈 Analysis for Stock: " + stock);
            System.out.printf("%-8s %-8s %-8s %-8s %-8s\n", "Time", "Price", "MA", "Min", "Max");

            for (int i = 0; i < prices.size(); i++) {
                double price = prices.get(i);
                if (i < 2) {
                    // Not enough data for moving average
                    System.out.printf("%-8s %-8.2f %-8s %-8s %-8s (%s)\n", 
                        timeStamps.get(i), price, "N/A", "N/A", "N/A", "WAIT");
                } else {
                    double ma = maCalc.getMovingAverage(prices, 3, i);
                    double min = tracker.getMin(prices, i - 2, i);
                    double max = tracker.getMax(prices, i - 2, i);
                    String action = tracker.getAction(prices.get(i), ma, min, max);  // UPDATED

                    System.out.printf("%-8s %-8.2f %-8.2f %-8.2f %-8.2f (%s)\n",
                        timeStamps.get(i), price, ma, min, max, action);
                }
            }
        }
    }


    

    

//    private void runProfitCalc(ProfitCalculator pc) {
//        for (String stock : stockSymbols) {
//            List<Double> prices = stockPrices.get(stock);
//            double maxProfit = pc.getMaxProfit(prices);
//            System.out.println("💰 Max Profit for " + stock + ": " + maxProfit);
//        }
//    }
    private void runProfitCalc(ProfitCalculator pc) {
        System.out.printf("%-10s %-12s %-10s %-10s %-20s\n", "Stock", "Max Profit", "Buy At", "Sell At", "Suggestion");
        System.out.println("-----------------------------------------------------------------------");

        for (String stock : stockSymbols) {
            List<Double> prices = stockPrices.get(stock);

            double minPrice = prices.get(0);
            int minIndex = 0;
            double maxProfit = 0;
            int buyAt = 0, sellAt = 0;

            for (int i = 0; i < prices.size(); i++) {
                if (prices.get(i) < minPrice) {
                    minPrice = prices.get(i);
                    minIndex = i;
                }
                double profit = prices.get(i) - minPrice;
                if (profit > maxProfit) {
                    maxProfit = profit;
                    buyAt = minIndex;
                    sellAt = i;
                }
            }

            String suggestion;
            if (maxProfit > 8) {
                suggestion = "📈 Best Growth ✅";
            } else if (maxProfit >= 5) {
                suggestion = "⚠ Medium Return";
            } else {
                suggestion = "💤Poor Growth";
            }

            System.out.printf("%-10s ₹%-11.2f %-10.2f %-10.2f %-20s\n",
                    stock,
                    maxProfit,
                    prices.get(buyAt),
                    prices.get(sellAt),
                    suggestion);
        }
    }

}

