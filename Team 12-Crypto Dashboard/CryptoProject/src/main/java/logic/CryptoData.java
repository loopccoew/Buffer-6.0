package logic;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.scene.Node;
import utils.GraphUtil;
import interfaces.ICryptoData;

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
import java.util.Random;

import api.ApiIntegration;

// Add these imports at the top
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
// ...existing imports...

public class CryptoData implements ICryptoData {

    private final StringProperty name;
    private final StringProperty readableName;
    private final DoubleProperty price;
    private final DoubleProperty priceChangePercent;
    private final DoubleProperty high;
    private final DoubleProperty low;
    private final DoubleProperty marketCap;
    private DoubleProperty volume; // Removed final modifier
    private StringProperty iconUrl; // Removed final modifier
    private Map<String, Double> exchangePrices;
    private final List<Double> priceHistory = new ArrayList<>();

    // JavaFX property for priceGraph
    private final ObjectProperty<Node> priceGraph = new SimpleObjectProperty<>();

    // Initialize Timeline at declaration
    private final Timeline autoUpdateTimeline = new Timeline(
            new KeyFrame(Duration.seconds(30), e -> updatePriceData()));

    // Add these properties
    private final DoubleProperty priceChange1h;
    private final DoubleProperty priceChange24h;
    private final DoubleProperty priceChange7d;
    private final DoubleProperty priceChange30d;

    public CryptoData(String name, String readableName, double price, double priceChange) {
        this.name = new SimpleStringProperty(name);
        this.readableName = new SimpleStringProperty(readableName);
        this.price = new SimpleDoubleProperty(price);
        this.priceChangePercent = new SimpleDoubleProperty(priceChange);
        this.high = new SimpleDoubleProperty(0.0);
        this.low = new SimpleDoubleProperty(0.0);
        this.marketCap = new SimpleDoubleProperty(0.0);
        this.volume = new SimpleDoubleProperty(0.0);
        this.iconUrl = new SimpleStringProperty("");
        this.exchangePrices = new HashMap<>();
        this.priceGraph.set(GraphUtil.createMiniGraph(price));
        this.priceChange1h = new SimpleDoubleProperty(0.0);
        this.priceChange24h = new SimpleDoubleProperty(0.0);
        this.priceChange7d = new SimpleDoubleProperty(0.0);
        this.priceChange30d = new SimpleDoubleProperty(0.0);
    }

    // Add overloaded constructor
    public CryptoData(String name, String readableName) {
        this.name = new SimpleStringProperty(name);
        this.readableName = new SimpleStringProperty(readableName);
        this.price = new SimpleDoubleProperty(0.0);
        this.priceChangePercent = new SimpleDoubleProperty(0.0);
        this.high = new SimpleDoubleProperty(0.0);
        this.low = new SimpleDoubleProperty(0.0);
        this.marketCap = new SimpleDoubleProperty(0.0);
        this.volume = new SimpleDoubleProperty(0.0); // Initialize in constructor
        this.iconUrl = new SimpleStringProperty(""); // Initialize in constructor
        this.exchangePrices = new HashMap<>();
        this.priceGraph.set(GraphUtil.createMiniGraph(0));
        this.priceChange1h = new SimpleDoubleProperty(0.0);
        this.priceChange24h = new SimpleDoubleProperty(0.0);
        this.priceChange7d = new SimpleDoubleProperty(0.0);
        this.priceChange30d = new SimpleDoubleProperty(0.0);

        // Configure and start Timeline
        autoUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        autoUpdateTimeline.play();
    }

    private void updatePriceData() {
        Random random = new Random();
        double currentPrice = price.get();

        // Update price with small random changes
        double priceChange = currentPrice * (random.nextDouble() * 0.02 - 0.01); // ±1%
        price.set(currentPrice + priceChange);

        // Update exchange prices
        for (Map.Entry<String, Double> entry : exchangePrices.entrySet()) {
            double exchangeVariation = currentPrice * (random.nextDouble() * 0.02 - 0.01);
            exchangePrices.put(entry.getKey(), entry.getValue() + exchangeVariation);
        }

        // Update price history
        if (priceHistory.size() > 100)
            priceHistory.remove(0);
        priceHistory.add(currentPrice);

        // Update percentage changes
        priceChangePercent.set(((currentPrice - priceHistory.get(0)) / priceHistory.get(0)) * 100);
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public double getPrice() {
        return price.get();
    }

    @Override
    public double getHigh() {
        return high.get();
    }

    @Override
    public double getLow() {
        return low.get();
    }

    @Override
    public double getPercentChange() {
        return priceChangePercent.get();
    }

    @Override
    public double getVolume() {
        return volume.get();
    }

    @Override
    public String getIconUrl() {
        return iconUrl.get();
    }

    @Override
    public double getMarketCap() {
        return marketCap.get();
    }

    public String getReadableName() {
        return readableName.get();
    }

    public void setReadableName(String readableName) {
        this.readableName.set(readableName);
    }

    public Map<String, Double> getExchangePrices() {
        return exchangePrices;
    }

    public double getPrice(String exchange) {
        return exchangePrices.getOrDefault(exchange, -1.0);
    }

    public void updatePrices() {
        String symbol = getSymbolFromName(name.get());
        double currentPrice = price.get();

        // Add simulated price variations for different exchanges
        Map<String, Double> newPrices = new HashMap<>();
        Random random = new Random();
        double variation = 0.02; // 2% variation

        newPrices.put("Binance", currentPrice * (1 + (random.nextDouble() * variation * 2 - variation)));
        newPrices.put("KuCoin", currentPrice * (1 + (random.nextDouble() * variation * 2 - variation)));
        newPrices.put("CoinGecko", currentPrice * (1 + (random.nextDouble() * variation * 2 - variation)));

        exchangePrices.putAll(newPrices);

        if (priceHistory.size() > 20) {
            priceHistory.remove(0);
        }
        priceHistory.add(currentPrice);
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

    @Override
    public void setIconUrl(String url) {
        this.iconUrl.set(url); // Use set() method instead of direct assignment
        setupIcon(url);
    }

    @Override
    public void setVolume(double volume) {
        this.volume.set(volume); // Use set() method instead of direct assignment
    }

    private void setupIcon(String url) {
        // Implementation for setting up the icon
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setPriceChangePercent(double percent) {
        this.priceChangePercent.set(percent);
    }

    public void setExchangePrices(Map<String, Double> prices) {
        this.exchangePrices.clear();
        this.exchangePrices.putAll(prices);
    }

    // Add additional setters for time-based changes
    public void setPriceChange1h(double change) {
        this.priceChange1h.set(change);
    }

    public void setPriceChange24h(double change) {
        this.priceChange24h.set(change);
    }

    public void setPriceChange7d(double change) {
        this.priceChange7d.set(change);
    }

    public void setPriceChange30d(double change) {
        this.priceChange30d.set(change);
    }

    // Add these property getters
    public DoubleProperty priceChange1hProperty() {
        return priceChange1h;
    }

    public DoubleProperty priceChange24hProperty() {
        return priceChange24h;
    }

    public DoubleProperty priceChange7dProperty() {
        return priceChange7d;
    }

    public DoubleProperty priceChange30dProperty() {
        return priceChange30d;
    }
}
