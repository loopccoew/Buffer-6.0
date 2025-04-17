package buffer_files;
import java.util.*;

public class InputHandler {
    public void captureInput(StockDataManager dataManager) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of stocks: ");
        int numStocks = Integer.parseInt(sc.nextLine());

        System.out.print("Enter stock symbols (comma-separated): ");
        String[] symbols = sc.nextLine().split(",");
        dataManager.setStockSymbols(Arrays.asList(symbols));

        System.out.print("Number of time entries: ");
        int numTimes = Integer.parseInt(sc.nextLine());

        List<String> times = new ArrayList<>();
        Map<String, List<Double>> priceMap = new HashMap<>();
        for (String s : symbols) priceMap.put(s, new ArrayList<>());

//        for (int i = 0; i < numTimes; i++) {
//            System.out.print(">> ");
//            String[] line = sc.nextLine().split(" ");
//            times.add(line[0]);
//
//            for (int j = 0; j < symbols.length; j++) {
//                priceMap.get(symbols[j]).add(Integer.parseInt(line[j + 1]));
//            }
//        }
        for (int i = 0; i < numTimes; i++) {
            String[] line;

            while (true) {
                System.out.print(">> ");
                String raw = sc.nextLine().trim();
                line = raw.split("\\s+"); // allow flexible spacing

                if (line.length != symbols.length + 1) {
                    System.out.println("Now enter the time followed by " + symbols.length + " prices.");
                    System.out.println("Format: HH:MM price1 price2 ...");
                } else {
                    break;
                }
            }

            times.add(line[0]);

            for (int j = 0; j < symbols.length; j++) {
                try {
                    priceMap.get(symbols[j]).add(Double.parseDouble(line[j + 1]));
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid price format at " + symbols[j] + ". Skipping this entry.");
                    priceMap.get(symbols[j]).add(0.0); // or you can choose to retry the input
                }
            }
        }

        dataManager.setTimeStamps(times);
        dataManager.setStockPrices(priceMap);
    }
}

