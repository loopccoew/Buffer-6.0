package logic;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.ApiIntegration;

public class CryptoData {

    private String name;
    private String readableName;
    private Map<String, Double> exchangePrices;
    private final List<Double> priceHistory = new ArrayList<>();

    // JavaFX property for priceGraph
    private final ObjectProperty<Node> priceGraph = new SimpleObjectProperty<>();

    public CryptoData(String name, String readableName) {
        this.name = name;
        this.readableName = readableName;
        this.exchangePrices = new HashMap<>();
        setPriceGraph(null); // Initialize priceGraph with a default value
    }

    public String getName() {
        return name;
    }

    public String getReadableName() {
        return readableName;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }

    public Map<String, Double> getExchangePrices() {
        return exchangePrices;
    }

    public double getPrice(String exchange) {
        return exchangePrices.getOrDefault(exchange, -1.0);
    }

    public void updatePrices() {
        String symbol = getSymbolFromName(name);

        double binancePrice = ApiIntegration.getLivePrice(symbol, "binance");
        double kucoinPrice = ApiIntegration.getLivePrice(symbol, "kucoin");
        double coingeckoPrice = ApiIntegration.getLivePrice(symbol, "coingecko");

        if (binancePrice > 0) {
            exchangePrices.put("Binance", binancePrice);
        }
        if (kucoinPrice > 0) {
            exchangePrices.put("KuCoin", kucoinPrice);
        }
        if (coingeckoPrice > 0) {
            exchangePrices.put("CoinGecko", coingeckoPrice);
        }

        if (binancePrice > 0) {
            if (priceHistory.size() > 20)
                priceHistory.remove(0);
            priceHistory.add(binancePrice);
        }

        // Update priceGraph with a new graph node
        setPriceGraph(GraphUtil.generateGraphNode(priceHistory)); // Ensure GraphUtil is implemented
    }

    private static String getSymbolFromName(String coinName) {
        switch (coinName.toLowerCase()) {
            case "bitcoin":
                return "btc";
            case "ethereum":
                return "eth";
            case "solana":
                return "sol";
            default:
                return coinName.toLowerCase();
        }
    }

    public double getPriceChangePercent() {
        if (priceHistory.size() < 2)
            return 0;
        double latest = priceHistory.get(priceHistory.size() - 1);
        double previous = priceHistory.get(priceHistory.size() - 2);
        return ((latest - previous) / previous) * 100;
    }

    public static Map<String, Map<String, Double>> fetchLatestCryptoData() {
        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd";
        int maxRetries = 3;
        int timeout = 15000;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("Attempt " + attempt + ": Connecting to API: " + apiUrl);
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);

                int status = conn.getResponseCode();
                System.out.println("Attempt " + attempt + ": Response code = " + status);

                if (status == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, Map<String, Double>>>() {
                        }.getType();
                        return gson.fromJson(reader, type);
                    }
                } else {
                    System.err.println("HTTP Error: " + status + ". Retrying...");
                }

            } catch (SocketTimeoutException e) {
                System.err.println("Attempt " + attempt + ": Socket Timeout - " + e.getMessage());
            } catch (MalformedURLException e) {
                System.err.println("Malformed URL: " + apiUrl);
                break;
            } catch (IOException e) {
                System.err.println("Attempt " + attempt + ": IO Error - " + e.getMessage());
            } catch (JsonSyntaxException e) {
                System.err.println("JSON Parsing Error: " + e.getMessage());
            }

            try {
                Thread.sleep(2000); // Wait before retrying
            } catch (InterruptedException ignored) {
            }
        }

        System.err.println("Failed to fetch data after " + maxRetries + " attempts.");
        return new HashMap<>();
    }

    public static Map<String, Map<String, Double>> toPriceMap(List<CryptoData> dataList) {
        Map<String, Map<String, Double>> priceMap = new HashMap<>();
        for (CryptoData data : dataList) {
            Map<String, Double> exchangePrices = data.getExchangePrices();
            if (exchangePrices != null && !exchangePrices.isEmpty()) {
                priceMap.put(data.getName(), exchangePrices);
            }
        }
        return priceMap;
    }

    public List<Double> getPriceHistory() {
        return priceHistory;
    }

    public Node getPriceGraph() {
        return priceGraph.get();
    }

    public void setPriceGraph(Node graph) {
        this.priceGraph.set(graph);
    }

    public ObjectProperty<Node> priceGraphProperty() {
        return priceGraph;
    }
}
