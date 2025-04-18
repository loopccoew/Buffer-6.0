package arbitrage;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import dsa.BellmanFordAlgorithm;

public class ArbitrageFinder {

    /**
     * Finds arbitrage opportunities by comparing cryptocurrency prices across
     * multiple exchanges.
     *
     * @param priceMap Map where key is the coin symbol and value is a map of
     *                 exchange prices.
     *                 Example: { "BTC": { "ExchangeA": 50000, "ExchangeB": 50500,
     *                 "ExchangeC": 50200 } }
     * @return List of arbitrage opportunities found.
     */
    public static List<ArbitrageOpportunity> findArbitrageOpportunities(Map<String, Map<String, Double>> priceMap) {
        double MIN_PROFIT_THRESHOLD = 0.1; // Lowered threshold to 0.1%
        List<ArbitrageOpportunity> opportunities = new ArrayList<>();

        if (priceMap == null || priceMap.isEmpty()) {
            System.out.println("No cryptocurrency data available for arbitrage analysis.");
            return opportunities;
        }

        for (Map.Entry<String, Map<String, Double>> coinEntry : priceMap.entrySet()) {
            String coin = coinEntry.getKey();
            Map<String, Double> exchangePrices = coinEntry.getValue();

            String bestBuyExchange = null;
            String bestSellExchange = null;
            double lowestPrice = Double.MAX_VALUE;
            double highestPrice = Double.MIN_VALUE;

            // Find the best buy (lowest price) and sell (highest price) exchanges
            for (Map.Entry<String, Double> exchangeEntry : exchangePrices.entrySet()) {
                String exchange = exchangeEntry.getKey();
                double price = exchangeEntry.getValue();

                if (price < lowestPrice) {
                    lowestPrice = price;
                    bestBuyExchange = exchange;
                }

                if (price > highestPrice) {
                    highestPrice = price;
                    bestSellExchange = exchange;
                }
            }

            double profit = highestPrice - lowestPrice;
            double profitPercentage = (lowestPrice > 0) ? (profit / lowestPrice) * 100 : 0.0; // Avoid division by zero

            // Filter out opportunities below the threshold
            if (profitPercentage >= MIN_PROFIT_THRESHOLD) {
                ArbitrageOpportunity opportunity = new ArbitrageOpportunity(
                        coin,
                        bestBuyExchange,
                        bestSellExchange,
                        lowestPrice,
                        highestPrice,
                        profitPercentage);

                opportunities.add(opportunity);
            } else {
                System.out.println("Filtered Opportunity: Coin: " + coin + ", Profit Percentage: " + profitPercentage);
            }
        }
        return opportunities;
    }

    /**
     * Finds the best conversion path between two coins using the Bellman-Ford
     * algorithm.
     *
     * @param priceMap   Map where key is the coin symbol and value is a map of
     *                   exchange prices.
     * @param startCoin  The starting coin for the conversion.
     * @param targetCoin The target coin for the conversion.
     * @return List of coins representing the best conversion path.
     */
    public static List<String> findBestConversionPath(Map<String, Map<String, Double>> priceMap, String startCoin,
            String targetCoin) {
        BellmanFordAlgorithm.Graph graph = new BellmanFordAlgorithm.Graph();

        // Build the graph from the price map
        for (Map.Entry<String, Map<String, Double>> coinEntry : priceMap.entrySet()) {
            String coin = coinEntry.getKey();
            Map<String, Double> exchangePrices = coinEntry.getValue();

            if (exchangePrices == null) {
                System.out.println("No exchange prices available for coin: " + coin);
                continue;
            }

            for (Map.Entry<String, Double> exchangeEntry : exchangePrices.entrySet()) {
                String exchange = exchangeEntry.getKey();
                Double price = exchangeEntry.getValue();

                if (price == null) {
                    System.out.println("Missing price for: " + coin + " -> " + exchange);
                    continue;
                }

                // Use negative log for arbitrage (to maximize profit)
                graph.addEdge(coin, exchange, -Math.log(price));
            }
        }

        // Find the best path using Bellman-Ford
        Map<String, String> paths = graph.bellmanFordWithPaths(startCoin);

        // Reconstruct the path from startCoin to targetCoin
        List<String> path = new ArrayList<>();
        for (String at = targetCoin; at != null; at = paths.get(at)) {
            path.add(0, at);
        }

        // If the path does not start with the startCoin, it means no valid path exists
        if (!path.isEmpty() && !path.get(0).equals(startCoin)) {
            System.out.println("No valid conversion path found from " + startCoin + " to " + targetCoin);
            return new ArrayList<>();
        }

        return path;
    }
}
