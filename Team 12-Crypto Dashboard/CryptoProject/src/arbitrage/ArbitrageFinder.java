package arbitrage;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import dsa.BellmanFordAlgorithm.Graph;

public class ArbitrageFinder {
    private static final double MIN_PROFIT_THRESHOLD = 0.5; // 0.5% minimum profit
    private static final double MIN_VOLUME_THRESHOLD = 10000; // $10,000 minimum volume

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
        List<ArbitrageOpportunity> opportunities = new ArrayList<>();

        for (Map.Entry<String, Map<String, Double>> entry : priceMap.entrySet()) {
            String coin = entry.getKey();
            Map<String, Double> exchangePrices = entry.getValue();

            // Find arbitrage opportunities between exchanges
            for (String buyExchange : exchangePrices.keySet()) {
                for (String sellExchange : exchangePrices.keySet()) {
                    if (buyExchange.equals(sellExchange))
                        continue;

                    double buyPrice = exchangePrices.get(buyExchange);
                    double sellPrice = exchangePrices.get(sellExchange);
                    double profitPercent = ((sellPrice - buyPrice) / buyPrice) * 100;

                    if (profitPercent > MIN_PROFIT_THRESHOLD) {
                        opportunities.add(new ArbitrageOpportunity(
                                coin, buyExchange, sellExchange, buyPrice, sellPrice, profitPercent));
                    }
                }
            }
        }

        // Sort by profit percentage descending
        opportunities.sort((a, b) -> Double.compare(b.getProfitPercentage(), a.getProfitPercentage()));
        return opportunities;
    }

    private static Map<String, Double> getExchangeVolumes(String coin) {
        // Implement real volume fetching here
        Map<String, Double> volumes = new HashMap<>();
        // For now, return mock volumes
        volumes.put("Binance", 1000000.0);
        volumes.put("Coinbase", 800000.0);
        volumes.put("Kraken", 600000.0);
        volumes.put("KuCoin", 400000.0);
        return volumes;
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
    public static List<String> findBestConversionPath(Map<String, Map<String, Double>> priceMap,
            String startCoin, String targetCoin) {
        Graph graph = new Graph();

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
