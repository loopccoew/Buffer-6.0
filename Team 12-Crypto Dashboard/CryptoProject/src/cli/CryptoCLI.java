package cli;

import arbitrage.ArbitrageFinder;
import arbitrage.ArbitrageOpportunity;
import logic.CryptoData;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CryptoCLI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Crypto CLI!");
        System.out.println("Fetching the latest cryptocurrency data...");

        try {
            // Fetch the latest crypto data
            List<CryptoData> cryptoDataList = CryptoData.fetchLatestCryptoData();
            Map<String, Map<String, Double>> priceMap = CryptoData.toPriceMap(cryptoDataList);

            System.out.println("Available cryptocurrencies:");
            for (CryptoData data : cryptoDataList) {
                System.out.printf("- %s (Price: $%.2f)%n", data.getSymbol(), data.getPrice());
            }

            System.out.println("\nOptions:");
            System.out.println("1. Find Arbitrage Opportunities");
            System.out.println("2. Find Best Conversion Path");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.println("\nFinding arbitrage opportunities...");
                List<ArbitrageOpportunity> opportunities = ArbitrageFinder.findArbitrageOpportunities(priceMap);

                if (opportunities.isEmpty()) {
                    System.out.println("No arbitrage opportunities found.");
                } else {
                    for (ArbitrageOpportunity opportunity : opportunities) {
                        System.out.println(opportunity);
                    }
                }
            } else if (choice == 2) {
                System.out.print("\nEnter the starting cryptocurrency (e.g., BTC): ");
                String startCoin = scanner.next().toUpperCase();
                System.out.print("Enter the target cryptocurrency (e.g., ETH): ");
                String targetCoin = scanner.next().toUpperCase();

                System.out.println("\nFinding the best conversion path...");
                List<String> bestPath = ArbitrageFinder.findBestConversionPath(priceMap, startCoin, targetCoin);

                if (bestPath.isEmpty()) {
                    System.out.println("No conversion path found.");
                } else {
                    System.out.println("Best Conversion Path: " + String.join(" -> ", bestPath));
                }
            } else {
                System.out.println("Invalid choice. Exiting.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
