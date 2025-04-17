package core;

import model.StockEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CRUD_op {

	private final Map<String, List<StockEntry>> stockMap;

    public CRUD_op(Map<String, List<StockEntry>> stockMap) { //constructor to initialize map
        this.stockMap = stockMap;
    }
    Scanner sc=new Scanner(System.in);

    
 // Calculate moving average for the given stock and number of days based on closing price
    public double calculateMovingAverage(String symbol, int days) {
        List<StockEntry> entries = stockMap.get(symbol);
        if (entries == null || entries.size() < days) {
            System.out.println("Not enough data to calculate " + days + "-day moving average for " + symbol);
            return -1;
        }

        // Sort by date to ensure correctness
        Collections.sort(entries, new Comparator<StockEntry>() {
            public int compare(StockEntry s1, StockEntry s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        int size = entries.size();
        double sum = 0;
        for (int i = size - days; i < size; i++) {
            sum += entries.get(i).getClose();
        }

        return sum / days;
    }
    
    
    
 // Detect bullish or bearish trend based on closing prices over a time period
    public String detectTrend(String symbol, int days) {
        List<StockEntry> entries = stockMap.get(symbol);
        if (entries == null || entries.size() < days) {
            return "Not enough data to analyze trend for " + symbol;
        }

        entries.sort(Comparator.comparing(StockEntry::getDate));
        int startIndex = entries.size() - days;
        int upCount = 0, downCount = 0;

        for (int i = startIndex; i < entries.size() - 1; i++) {
            double today = entries.get(i).getClose();
            double next = entries.get(i + 1).getClose();
            if (next > today) upCount++;
            else if (next < today) downCount++;
        }

        if (upCount > downCount) return "Bullish Trend Detected for " + symbol;
        else if (downCount > upCount) return "Bearish Trend Detected for " + symbol;
        else return "No Clear Trend for " + symbol;
    }

    private LocalDate parseDateFlexible(String rawDate) {
        List<DateTimeFormatter> formatters = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(rawDate, formatter);
            } catch (DateTimeParseException e) {
               
            }
        }

        throw new IllegalArgumentException("Unrecognized date format: " + rawDate);
    }

    //Highest and Lowest price
    public void findClosingPriceInRange(boolean findMax) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter stock symbol (e.g., AAPL): ");
        String symbol = scanner.nextLine().trim();

        System.out.print("Enter start date (yyyy-MM-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());

        System.out.print("Enter end date (yyyy-MM-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());

        List<StockEntry> entries = stockMap.get(symbol);

        if (entries == null || entries.isEmpty()) {
            System.out.println("No data found for symbol: " + symbol);
            return;
        }

        List<StockEntry> filtered = new ArrayList<>();
        for (StockEntry entry : entries) {
            if (!entry.getDate().isBefore(startDate) && !entry.getDate().isAfter(endDate)) {
                filtered.add(entry);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No data found in the given date range.");
            return;
        }

        PriorityQueue<StockEntry> heap;

        if (findMax) {
            heap = new PriorityQueue<>((a, b) -> Double.compare(b.getClose(), a.getClose()));
        } else {
            heap = new PriorityQueue<>((a, b) -> Double.compare(a.getClose(), b.getClose()));
        }

        heap.addAll(filtered);
        StockEntry result = heap.peek();

        if (findMax) {
            System.out.printf("Highest closing price of %s between %s and %s is: %.2f on %s%n",
                    symbol, startDate, endDate, result.getClose(), result.getDate());
        } else {
            System.out.printf("Lowest closing price of %s between %s and %s is: %.2f on %s%n",
                    symbol, startDate, endDate, result.getClose(), result.getDate());
        }
    }


    
    //Top k highest or lowest
    public void findTopKClosingPrices(int k, LocalDate startDate, LocalDate endDate, boolean findMax) {
    	Scanner sc=new Scanner(System.in);
        List<Map.Entry<String, StockEntry>> filteredEntries = new ArrayList<>();
        for (String symbol : stockMap.keySet()) {
            List<StockEntry> stockList = stockMap.get(symbol);
            for (StockEntry stock : stockList) {
                if (!stock.getDate().isBefore(startDate) && !stock.getDate().isAfter(endDate)) {
                    filteredEntries.add(new AbstractMap.SimpleEntry<>(symbol, stock));
                }
            }
        }
        if (filteredEntries.isEmpty()) {
            System.out.println("No stock data found in the given date range.");
            return;
        }
        filteredEntries.sort(new Comparator<Map.Entry<String, StockEntry>>() {
            public int compare(Map.Entry<String, StockEntry> a, Map.Entry<String, StockEntry> b) {
                double priceA = a.getValue().getClose();
                double priceB = b.getValue().getClose();
                if (findMax) {
                    return Double.compare(priceB, priceA);
                } else {
                    return Double.compare(priceA, priceB);
                }
            }
        });
        String type = findMax ? "Highest" : "Lowest";
        System.out.printf("Top %d %s Closing Prices between %s and %s:\n",
                k, type, startDate, endDate);
        int count = Math.min(k, filteredEntries.size());
        System.out.println("1.Detailed View \n2.Brief Overview");
        
        int view=sc.nextInt();
 
        for (int i = 0; i < count; i++) {
            Map.Entry<String, StockEntry> entry = filteredEntries.get(i);
            String symbol = entry.getKey();
            StockEntry stock = entry.getValue();
            
            if(view==1)
            {
            	 System.out.printf("Symbol: %s, Date: %s, Open: %.2f, High: %.2f, Low: %.2f, Close: %.2f, Volume: %d\n",
            		        symbol, stock.getDate(), stock.getOpen(), stock.getHigh(), stock.getLow(), stock.getClose(), stock.getVolume());
            	
            }
            else {
            System.out.printf("%d. Stock: %s, Date: %s, Close Price: %.2f\n",
                    i + 1, symbol, stock.getDate(), stock.getClose());
            }
        }
    }
    private void saveToCSV() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (Map.Entry<String, List<StockEntry>> entry : stockMap.entrySet()) {
            String symbol = entry.getKey();
            List<StockEntry> stocks = entry.getValue();
            String filePath = "data" + File.separator + symbol + ".csv";

            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.println("Date,Open,High,Low,Close,Volume");
                for (StockEntry stock : stocks) {
                    writer.printf("%s,%.2f,%.2f,%.2f,%.2f,%d%n",
                            stock.getDate(),
                            stock.getOpen(),
                            stock.getHigh(),
                            stock.getLow(),
                            stock.getClose(),
                            stock.getVolume());
                }
            } catch (IOException e) {
                System.out.println("Error writing to CSV for symbol " + symbol + ": " + e.getMessage());
            }
        }
    }
    


    public void add() {
        System.out.print("Enter stock symbol (e.g., AAPL): ");
        String symbol = sc.nextLine().trim();

        System.out.print("Enter date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(sc.nextLine().trim());

        System.out.print("Enter Open price: ");
        double open = sc.nextDouble();

        System.out.print("Enter High price: ");
        double high = sc.nextDouble();

        System.out.print("Enter Low price: ");
        double low = sc.nextDouble();

        System.out.print("Enter Close price: ");
        double close = sc.nextDouble();

        System.out.print("Enter Volume: ");
        long volume = sc.nextLong();
        sc.nextLine(); 

        stockMap.putIfAbsent(symbol, new ArrayList<>());
        stockMap.get(symbol).add(new StockEntry(date, open, high, low, close, volume));
        System.out.println("Entry added for " + symbol + " on " + date);

        saveToCSV();
    }

    public boolean update() {
        System.out.println("Enter the Stock name to update: ");
        String symbol = sc.nextLine().trim();

        System.out.println("Enter the date (yyyy-MM-dd) to update details: ");
        LocalDate inputDate = LocalDate.parse(sc.nextLine().trim());

        List<StockEntry> entries = stockMap.get(symbol);
        if (entries == null) {
            System.out.println("Stock data is not present for symbol: " + symbol);
            return false;
        }

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getDate().equals(inputDate)) {
                System.out.print("Enter Open price: ");
                double open = sc.nextDouble();

                System.out.print("Enter High price: ");
                double high = sc.nextDouble();

                System.out.print("Enter Low price: ");
                double low = sc.nextDouble();

                System.out.print("Enter Close price: ");
                double close = sc.nextDouble();

                System.out.print("Enter Volume: ");
                long volume = sc.nextLong();
                sc.nextLine(); // clear buffer

                entries.set(i, new StockEntry(inputDate, open, high, low, close, volume));
                System.out.println("Stock at symbol: " + symbol + " on date: " + inputDate + " is updated.");

                saveToCSV();
                return true;
            }
        }

        System.out.println("No entry found for symbol: " + symbol + " on date: " + inputDate);
        return false;
    }

    public void delete() {
        System.out.println("Enter the Symbol (e.g., AAPL):");
        String symbol = sc.nextLine().trim();

        System.out.println("Enter the date (yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(sc.nextLine().trim());

        List<StockEntry> entries = stockMap.get(symbol);
        if (entries == null) {
            System.out.println("Stock data is not present");
            return;
        }

        boolean removed = entries.removeIf(entry -> entry.getDate().equals(date));
        if (removed) {
            System.out.println("Stock data removed");
            saveToCSV();
        } else {
            System.out.println("No entry found on given date");
        }
    }
    
    //Trend analysis Summary
    public String getTechnicalSummary(String stockSymbol) {
        List<StockEntry> entries = stockMap.get(stockSymbol);
        if (entries == null || entries.isEmpty()) return "No Data";
        double ma5 = calculateMovingAverage(stockSymbol, 5);
        double ma10 = calculateMovingAverage(stockSymbol, 10);
        if (ma5 == -1 || ma10 == -1) return "Insufficient Data";
        if (ma5 > ma10 * 1.02) return "Strong Buy";
        else if (ma5 > ma10) return "Buy";
        else if (Math.abs(ma5 - ma10) < 0.01) return "Neutral";
        else if (ma5 < ma10) return "Sell";
        else return "Strong Sell";
    }

    public double predictPriceTarget(String stockSymbol) {
        List<StockEntry> entries = stockMap.get(stockSymbol);
        if (entries == null || entries.size() < 2) return 0;    
        entries.sort(Comparator.comparing(StockEntry::getDate));
        double lastPrice = entries.get(entries.size() - 1).getClose();
        double previousPrice = entries.get(entries.size() - 2).getClose();
        double growthRate = lastPrice - previousPrice;
        return lastPrice + growthRate * 5; 
    }


}