package loader;
	import model.StockEntry;

	import java.io.IOException;
	import java.nio.file.*;
	import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

	public class CSVLoader {

	
			public static List<StockEntry> loadStockData(String filePath) {
			    List<StockEntry> stockData = new ArrayList<>();
			    try {
			        List<String> lines = Files.readAllLines(Paths.get(filePath));
			        for (int i = 1; i < lines.size(); i++) { 
			            String[] tokens = lines.get(i).split(",");

			            for (int j = 0; j < tokens.length; j++) {
			                tokens[j] = tokens[j].replace("\"", "").trim();
			            }
			            
			            
			            DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			            DateTimeFormatter altFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			            LocalDate date;
			            try {
			                date = LocalDate.parse(tokens[0], isoFormatter);
			            } catch (Exception e) {
			                date = LocalDate.parse(tokens[0], altFormatter);
			            }
			            
			            double open = Double.parseDouble(tokens[1]);
			            double high = Double.parseDouble(tokens[2]);
			            double low = Double.parseDouble(tokens[3]);
			            double close = Double.parseDouble(tokens[4]);
			            long  volume = Long.parseLong(tokens[5]); 

			            StockEntry entry = new StockEntry(date, open, high, low, close, volume);
			            stockData.add(entry);
			        }
			    } catch (IOException e) {
			        System.out.println("Error reading CSV: " + e.getMessage());
			    }
			    return stockData;
			}
			
			


	    public static Map<String, List<StockEntry>> loadAllStocks(String folderPath) {
	        Map<String, List<StockEntry>> stockMap = new HashMap<>();

	        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath), "*.csv")) {
	            for (Path file : stream) {
	                String fileName = file.getFileName().toString();
	                String stockSymbol = fileName.replace(".csv", "");

	                List<StockEntry> data = loadStockData(file.toString());
	                stockMap.put(stockSymbol, data);
	            }
	        } catch (IOException e) {
	            System.out.println("Error reading directory: " + e.getMessage());
	        }

	        return stockMap;
	    }
	}