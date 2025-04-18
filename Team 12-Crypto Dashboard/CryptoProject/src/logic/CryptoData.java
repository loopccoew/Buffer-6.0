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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Tooltip;

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
import java.util.Random;

public class CryptoData implements ICryptoData {

    private final StringProperty name;
    private final StringProperty readableName;
    private final DoubleProperty price;
    private final DoubleProperty priceChangePercent;
    private final DoubleProperty high;
    private final DoubleProperty low;
    private final DoubleProperty marketCap;
    private final DoubleProperty volume; // Added missing field
    private final StringProperty iconUrl; // Added missing field
    private final ObjectProperty<ImageView> iconView = new SimpleObjectProperty<>();
    private Map<String, Double> exchangePrices;
    private final List<Double> priceHistory = new ArrayList<>();
    private final ObjectProperty<Node> priceGraph = new SimpleObjectProperty<>();
    private long lastUpdated;
    private final Timeline autoUpdateTimeline;
    private final DoubleProperty priceChange1h = new SimpleDoubleProperty();
    private final DoubleProperty priceChange24h = new SimpleDoubleProperty();
    private final DoubleProperty priceChange7d = new SimpleDoubleProperty();
    private final DoubleProperty priceChange30d = new SimpleDoubleProperty();
    private final List<Double> realtimePrices = new ArrayList<>();
    private static final int MAX_REALTIME_PRICES = 100;

    public CryptoData(String name, String readableName) {
        this.name = new SimpleStringProperty(name);
        this.readableName = new SimpleStringProperty(readableName);
        this.price = new SimpleDoubleProperty(0.0);
        this.priceChangePercent = new SimpleDoubleProperty(0.0);
        this.high = new SimpleDoubleProperty(0.0);
        this.low = new SimpleDoubleProperty(0.0);
        this.marketCap = new SimpleDoubleProperty(0.0);
        this.volume = new SimpleDoubleProperty(0.0);
        this.iconUrl = new SimpleStringProperty("");
        this.exchangePrices = new HashMap<>();
        this.priceGraph.set(GraphUtil.createMiniGraph(0));
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
        double binancePrice = getPriceFromExchange(symbol, "binance");
        double kucoinPrice = getPriceFromExchange(symbol, "kucoin");
        double coingeckoPrice = getPriceFromExchange(symbol, "coingecko");

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

    private double getPriceFromExchange(String symbol, String exchange) {
        // Simulate price with random variation around base price
        double basePrice = price.get();
        double variation = 0.02; // 2% variation
        return basePrice * (1 + (Math.random() * variation * 2 - variation));
    }

    private static double fetchPriceFromExchange(String symbol, String exchange) {
        return 1000 + Math.random() * 1000; // Replace with actual implementation
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

    // Remove duplicate methods and keep only these versions:
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

    @Override
    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    @Override
    public double getVolume() {
        return volume.get();
    }

    public void updateDetails(double price, double priceChange1h, double priceChange24h,
            double priceChange7d, double priceChange30d, double high, double low,
            double marketCap, String imageUrl) {
        this.price.set(price);
        this.priceChange1h.set(priceChange1h);
        this.priceChange24h.set(priceChange24h);
        this.priceChange7d.set(priceChange7d);
        this.priceChange30d.set(priceChange30d);
        this.high.set(high);
        this.low.set(low);
        this.marketCap.set(marketCap);
        this.imageUrl.set(imageUrl);
    }

    private void setupIcon(String symbol) {
        Platform.runLater(() -> {
            try {
                Image img = CoinImageService.getIcon(symbol);
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(32);
                imgView.setFitWidth(32);
                imgView.setPreserveRatio(true);

                // Add hover effects
                imgView.setOnMouseEntered(e -> {
                    imgView.setEffect(new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.GOLD));
                    imgView.setScaleX(1.2);
                    imgView.setScaleY(1.2);
                });

                imgView.setOnMouseExited(e -> {
                    imgView.setEffect(null);
                    imgView.setScaleX(1.0);
                    imgView.setScaleY(1.0);
                });

                // Add detailed tooltip
                Tooltip tooltip = new Tooltip(String.format(
                        "%s (%s)\nPrice: $%.2f\n24h Change: %.2f%%\nVolume: $%,.2f\nMarket Cap: $%,.2f",
                        readableName.get(),
                        name.get(),
                        price.get(),
                        priceChangePercent.get(),
                        volume.get(),
                        marketCap.get()));
                tooltip.getStyleClass().add("custom-tooltip");
                Tooltip.install(imgView, tooltip);

                iconView.set(imgView);
            } catch (Exception e) {
                System.err.println("Error loading icon for " + name.get() + ": " + e.getMessage());
                setFallbackIcon();
            }
        });
    }

    private void setFallbackIcon() {
        Image fallback = new Image(getClass().getResourceAsStream("/resources/images/default-crypto.png"));
        ImageView fallbackView = new ImageView(fallback);
        fallbackView.setFitHeight(32);
        fallbackView.setFitWidth(32);
        iconView.set(fallbackView);
    }

    @Override
    public void setIconUrl(String url) {
        this.iconUrl.set(url);
        setupIcon(url);
    }

    @Override
    public String getIconUrl() {
        return iconUrl.get();
    }

    public StringProperty iconUrlProperty() {
        return iconUrl;
    }

    public ObjectProperty<ImageView> iconViewProperty() {
        return iconView;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setPriceGraph(LineChart<Number, Number> chart) {
        this.priceGraph.set(chart);
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

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // Clean up resources when no longer needed
    public void cleanup() {
        autoUpdateTimeline.stop();
    }

    // Add property getters
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

    public void addPriceToHistory(double price) {
        if (realtimePrices.size() >= MAX_REALTIME_PRICES) {
            realtimePrices.remove(0);
        }
        realtimePrices.add(price);

        // Update price change calculations
        if (realtimePrices.size() > 1) {
            double oldPrice = realtimePrices.get(0);
            double change = ((price - oldPrice) / oldPrice) * 100;
            priceChangePercent.set(change);
        }

        // Update price property
        this.price.set(price);

        // Update mini chart
        Platform.runLater(() -> {
            createRealtimeChart();
        });
    }

    public void updatePriceChange(double change) {
        this.priceChangePercent.set(change);
    }

    private void createRealtimeChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setVisible(false);
        yAxis.setVisible(false);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        chart.setPrefSize(180, 40);
        chart.setMaxHeight(40);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < realtimePrices.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, realtimePrices.get(i)));
        }

        chart.getData().add(series);
        setPriceGraph(chart);
    }

    @Override
    public double getPrice() {
        return price.get();
    }

    public void setExchangePrices(Map<String, Double> prices) {
        this.exchangePrices = new HashMap<>(prices);
    }

    public void setPriceHistory(List<Double> prices) {
        this.priceHistory.clear();
        this.priceHistory.addAll(prices);
        updatePriceGraph();
    }

    private void updatePriceGraph() {
        Platform.runLater(() -> {
            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setVisible(false);
            yAxis.setVisible(false);

            LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
            chart.setCreateSymbols(false);
            chart.setAnimated(false);
            chart.setLegendVisible(false);
            chart.setPrefSize(180, 40);
            chart.setMaxHeight(40);

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i < priceHistory.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, priceHistory.get(i)));
            }

            chart.getData().add(series);
            setPriceGraph(chart);
        });
    }
}