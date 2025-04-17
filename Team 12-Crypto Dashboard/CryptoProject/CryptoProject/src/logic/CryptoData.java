package logic;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.ApiIntegration;
import utils.GraphUtil;

public class CryptoData {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty readableName = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final DoubleProperty priceChangePercent = new SimpleDoubleProperty();
    private final DoubleProperty high = new SimpleDoubleProperty();
    private final DoubleProperty low = new SimpleDoubleProperty();
    private final DoubleProperty marketCap = new SimpleDoubleProperty();
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final DoubleProperty volume = new SimpleDoubleProperty();
    private final StringProperty iconUrl = new SimpleStringProperty();
    private final ObjectProperty<ImageView> iconView = new SimpleObjectProperty<>();
    private Map<String, Double> exchangePrices;
    private final List<Double> priceHistory = new ArrayList<>();
    private final ObjectProperty<Node> priceGraph = new SimpleObjectProperty<>();

    public CryptoData(String name, String readableName) {
        this.name.set(name);
        this.readableName.set(readableName);
        this.exchangePrices = new HashMap<>();
        setPriceGraph(null);
        this.iconView.set(new ImageView());
        this.iconView.get().setFitHeight(24);
        this.iconView.get().setFitWidth(24);
    }

    public String getName() {
        return name.get();
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
        ApiIntegration api = new ApiIntegration();
        double binancePrice = api.getLivePrice(symbol, "binance");
        double kucoinPrice = api.getLivePrice(symbol, "kucoin");
        double coingeckoPrice = api.getLivePrice(symbol, "coingecko");

        if (binancePrice > 0)
            exchangePrices.put("Binance", binancePrice);
        if (kucoinPrice > 0)
            exchangePrices.put("KuCoin", kucoinPrice);
        if (coingeckoPrice > 0)
            exchangePrices.put("CoinGecko", coingeckoPrice);

        if (priceHistory.size() > 20)
            priceHistory.remove(0);
        priceHistory.add(binancePrice);
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
            return 0.0;
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
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);

                if (conn.getResponseCode() == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, Map<String, Double>>>() {
                        }.getType();
                        return gson.fromJson(reader, type);
                    }
                }
            } catch (IOException | JsonSyntaxException e) {
                System.err.println("Error fetching data: " + e.getMessage());
            }
        }
        return new HashMap<>();
    }

    public static Map<String, Map<String, Double>> toPriceMap(List<CryptoData> dataList) {
        Map<String, Map<String, Double>> priceMap = new HashMap<>();
        for (CryptoData data : dataList) {
            priceMap.put(data.getName(), data.getExchangePrices());
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

    public double getHigh() {
        return priceHistory.stream().max(Double::compare).orElse(0.0);
    }

    public double getLow() {
        return priceHistory.stream().min(Double::compare).orElse(0.0);
    }

    public double getPercentChange() {
        if (priceHistory.size() < 2)
            return 0.0;
        double latest = priceHistory.get(priceHistory.size() - 1);
        double previous = priceHistory.get(priceHistory.size() - 2);
        return ((latest - previous) / previous) * 100;
    }

    // Add JavaFX property getters
    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty readableNameProperty() {
        return readableName;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public DoubleProperty priceChangePercentProperty() {
        return priceChangePercent;
    }

    public DoubleProperty highProperty() {
        return high;
    }

    public DoubleProperty lowProperty() {
        return low;
    }

    public DoubleProperty marketCapProperty() {
        return marketCap;
    }

    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public double getVolume() {
        return volume.get();
    }

    public void updateDetails(double price, double priceChange, double high, double low,
            double marketCap, String imageUrl) {
        this.price.set(price);
        this.priceChangePercent.set(priceChange);
        this.high.set(high);
        this.low.set(low);
        this.marketCap.set(marketCap);
        this.imageUrl.set(imageUrl);
    }

    private void setupIcon(String url) {
        if (url != null && !url.isEmpty()) {
            Platform.runLater(() -> {
                try {
                    Image img = new Image(url, true);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitHeight(24);
                    imgView.setFitWidth(24);
                    imgView.setPreserveRatio(true);
                    iconView.set(imgView);
                } catch (Exception e) {
                    System.err.println("Error loading icon: " + e.getMessage());
                }
            });
        }
    }

    public void setIconUrl(String url) {
        this.iconUrl.set(url);
        setupIcon(url);
    }

    public String getIconUrl() {
        return iconUrl.get();
    }

    public StringProperty iconUrlProperty() {
        return iconUrl;
    }

    public ObjectProperty<ImageView> iconViewProperty() {
        return iconView;
    }
}