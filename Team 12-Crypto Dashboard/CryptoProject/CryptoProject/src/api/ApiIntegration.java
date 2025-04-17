package api;

import com.google.gson.*;
import javafx.scene.chart.*;
import logic.CryptoData;
import arbitrage.ArbitrageOpportunity;
import java.io.*;
import java.net.*;
import java.util.*;

public class ApiIntegration {
    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/price";
    private static final Random random = new Random();
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets";
    private static final String COINGECKO_SYMBOLS = String.join(",", Arrays.asList(
            "bitcoin", "ethereum", "tether", "binancecoin", "ripple", "usdc", "staked-ether",
            "cardano", "dogecoin", "solana", "tron", "polkadot", "matic-network", "litecoin",
            "chainlink", "bitcoin-cash", "stellar", "monero", "ethereum-classic", "cosmos",
            "hedera", "filecoin", "near", "vechain", "eos", "uniswap", "avalanche-2",
            "chainlink", "cosmos", "stellar", "monero", "algorand", "bitcoin-cash"));

    private static final String QUERY_PARAMS = "?vs_currency=usd&ids=" + COINGECKO_SYMBOLS +
            "&order=market_cap_desc&per_page=250&sparkline=false&locale=en";
    private static final String COINAPI_KEY = "0ff8204f-6d2e-4355-b7ee-9c562285e84d";
    private static final String COINAPI_URL = "https://rest.coinapi.io/v1";

    /**
     * Fetch live cryptocurrency prices from Binance API.
     *
     * @return A map where the key is the cryptocurrency symbol (e.g., BTCUSDT) and
     *         the value is the price.
     */
    public static Map<String, Double> fetchLivePrices() {
        Map<String, Double> prices = new HashMap<>();

        try {
            // Create URL connection
            URL url = new URL(BINANCE_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // Parse JSON response
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String symbol = obj.get("symbol").getAsString();
                double price = obj.get("price").getAsDouble();
                prices.put(symbol, price);
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error fetching live prices from Binance API: " + e.getMessage());
        }

        return prices;
    }

    /**
     * Get the live price for a currency pair.
     */
    public static double getLivePrice(String symbol, String exchange) {
        // Mock implementation
        return 1000 + random.nextDouble() * 1000;
    }

    public List<CryptoData> fetchCryptoData() throws IOException {
        List<CryptoData> cryptoDataList = new ArrayList<>();
        URL url = new URL(BINANCE_API_URL);

        // Create URL connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String symbol = obj.get("symbol").getAsString();

                if (!symbol.endsWith("USDT"))
                    continue;

                // Create CryptoData with just symbol and name
                CryptoData cryptoData = new CryptoData(symbol, symbol);
                cryptoData.getExchangePrices().put("Binance", obj.get("price").getAsDouble());

                // Set the price graph
                cryptoData.setPriceGraph(createMiniGraph(obj.get("price").getAsDouble()));

                cryptoDataList.add(cryptoData);
            }
        }

        return cryptoDataList;
    }

    public List<CryptoData> fetchDetailedCryptoData() throws IOException {
        List<CryptoData> cryptoDataList = new ArrayList<>();
        URL url = new URL(COINGECKO_API_URL + QUERY_PARAMS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                CryptoData cryptoData = parseCryptoData(obj);
                if (cryptoData != null) {
                    cryptoDataList.add(cryptoData);
                }
            }
        }
        return cryptoDataList;
    }

    private CryptoData parseCryptoData(JsonObject obj) {
        try {
            // Type checking before accessing fields
            if (!obj.has("symbol") || !obj.has("current_price") ||
                    obj.get("symbol").isJsonNull() || obj.get("current_price").isJsonNull()) {
                return null;
            }

            String symbol = obj.get("symbol").getAsString().toUpperCase();
            String name = obj.has("name") && !obj.get("name").isJsonNull() ? obj.get("name").getAsString() : symbol;

            // Parse numeric values
            double currentPrice = getDoubleOrDefault(obj, "current_price", 0.0);
            double priceChange = getDoubleOrDefault(obj, "price_change_percentage_24h", 0.0);
            double high24h = getDoubleOrDefault(obj, "high_24h", 0.0);
            double low24h = getDoubleOrDefault(obj, "low_24h", 0.0);
            double marketCap = getDoubleOrDefault(obj, "market_cap", 0.0);
            double volume = getDoubleOrDefault(obj, "total_volume", 0.0);

            // Handle image URLs
            String imageUrl = "";
            if (obj.has("image")) {
                JsonElement imageElement = obj.get("image");
                if (imageElement.isJsonObject()) {
                    JsonObject imageObj = imageElement.getAsJsonObject();
                    imageUrl = imageObj.has("small") ? imageObj.get("small").getAsString()
                            : imageObj.has("thumb") ? imageObj.get("thumb").getAsString() : "";
                } else if (imageElement.isJsonPrimitive()) {
                    imageUrl = imageElement.getAsString();
                }
            }

            CryptoData cryptoData = new CryptoData(symbol, name);
            cryptoData.updateDetails(currentPrice, priceChange, high24h, low24h, marketCap, imageUrl);
            cryptoData.setVolume(volume);
            cryptoData.setIconUrl(imageUrl);

            // Add mock exchange prices
            Map<String, Double> prices = new HashMap<>();
            prices.put("Binance", currentPrice * (1 + Math.random() * 0.02 - 0.01));
            prices.put("Coinbase", currentPrice * (1 + Math.random() * 0.02 - 0.01));
            prices.put("Kraken", currentPrice * (1 + Math.random() * 0.02 - 0.01));
            cryptoData.getExchangePrices().putAll(prices);

            return cryptoData;
        } catch (Exception e) {
            System.err.println("Error parsing crypto data: " + e.getMessage());
            return null;
        }
    }

    private double getDoubleOrDefault(JsonObject obj, String key, double defaultValue) {
        try {
            return obj.has(key) ? obj.get(key).getAsDouble() : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static LineChart<Number, Number> createMiniGraph(double baseValue) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setVisible(false);
        yAxis.setVisible(false);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setPrefSize(120, 60);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < 10; i++) {
            series.getData().add(new XYChart.Data<>(i, baseValue + Math.random() * 100 - 50));
        }

        lineChart.getData().add(series);
        return lineChart;
    }

    /**
     * Fetch arbitrage opportunities by comparing prices across exchanges.
     *
     * @param priceMap A map where the key is the coin symbol and the value is a map
     *                 of exchange prices.
     * @return A list of arbitrage opportunities.
     */
    public static List<ArbitrageOpportunity> fetchArbitrageOpportunities(Map<String, Map<String, Double>> priceMap) {
        List<ArbitrageOpportunity> opportunities = new ArrayList<>();
        double MIN_PROFIT_THRESHOLD = 0.5; // Minimum profit threshold (0.5%)

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
            double profitPercentage = (lowestPrice > 0) ? (profit / lowestPrice) * 100 : 0.0;

            // Add opportunities above the threshold
            if (profitPercentage >= MIN_PROFIT_THRESHOLD) {
                opportunities.add(new ArbitrageOpportunity(
                        coin, bestBuyExchange, bestSellExchange, lowestPrice, highestPrice, profitPercentage));
            }
        }

        return opportunities;
    }

    public static List<String> fetchCryptoList() {
        List<String> cryptoList = new ArrayList<>();
        try {
            String apiUrl = "https://api.coingecko.com/api/v3/coins/list";
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                cryptoList.add(jsonObject.get("id").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cryptoList;
    }
}
