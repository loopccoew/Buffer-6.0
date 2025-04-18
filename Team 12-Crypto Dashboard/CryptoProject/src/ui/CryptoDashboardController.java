package ui;

// Add these imports at the top
import logic.CryptoData;
import api.ApiIntegration;
import arbitrage.ArbitrageOpportunity;
import arbitrage.ArbitrageFinder;
import dsa.BellmanFordAlgorithm;
import dsa.Graph;
import javafx.beans.property.SimpleObjectProperty;

import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.OutputStream;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.Interpolator;
import javafx.scene.control.Labeled;
import javafx.beans.value.WritableValue;
import java.util.concurrent.CompletionException;

public class CryptoDashboardController {
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<CryptoData> cryptoTable;
    @FXML
    private TableColumn<CryptoData, String> nameCol;
    @FXML
    private TableColumn<CryptoData, String> symbolCol;
    @FXML
    private TableColumn<CryptoData, Double> priceCol;
    @FXML
    private TableColumn<CryptoData, Double> changeCol;
    @FXML
    private TableColumn<CryptoData, Double> highCol;
    @FXML
    private TableColumn<CryptoData, Double> lowCol;
    @FXML
    private TableColumn<CryptoData, Double> marketCapCol;
    @FXML
    private TableColumn<CryptoData, ImageView> iconCol;
    @FXML
    private Label statusBar;
    @FXML
    private Pane graphPane;
    @FXML
    private LineChart<Number, Number> priceChart;

    @FXML
    private TableView<ArbitrageOpportunity> arbitrageTable;
    @FXML
    private TableColumn<ArbitrageOpportunity, String> marketCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, String> pairCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, Double> buyPriceCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, Double> sellPriceCol;
    @FXML
    private TableColumn<ArbitrageOpportunity, Double> profitCol;

    @FXML
    private TableColumn<CryptoData, Double> change1hCol;
    @FXML
    private TableColumn<CryptoData, Double> change24hCol;
    @FXML
    private TableColumn<CryptoData, Double> change7dCol;
    @FXML
    private TableColumn<CryptoData, Double> change30dCol;

    @FXML
    private TableView<CryptoAlert> alertsTable;
    @FXML
    private TableView<Portfolio.Position> portfolioTable;

    private Portfolio userPortfolio;
    private final ObservableList<CryptoAlert> alerts = FXCollections.observableArrayList();

    private ObservableList<ArbitrageOpportunity> arbitrageList = FXCollections.observableArrayList();

    private ObservableList<CryptoData> cryptoDataList = FXCollections.observableArrayList();
    private Timeline autoRefreshTimeline;
    private Parent root; // Add this field

    @FXML
    private Slider minProfitSlider;
    @FXML
    private Label minProfitLabel;
    @FXML
    private Button autoRefreshButton;
    private Timeline arbitrageRefreshTimeline;
    private boolean autoRefreshEnabled = false;

    private WebSocketService webSocketService;

    // Add these private fields
    private DoubleProperty price = new SimpleDoubleProperty();
    private DoubleProperty high = new SimpleDoubleProperty();
    private DoubleProperty low = new SimpleDoubleProperty();
    private DoubleProperty priceChangePercent = new SimpleDoubleProperty();
    private DoubleProperty marketCap = new SimpleDoubleProperty();
    private DoubleProperty volume = new SimpleDoubleProperty();

    // Add getters/setters
    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        price.set(value);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public double getHigh() {
        return high.get();
    }

    public void setHigh(double value) {
        high.set(value);
    }

    public DoubleProperty highProperty() {
        return high;
    }

    public double getLow() {
        return low.get();
    }

    public void setLow(double value) {
        low.set(value);
    }

    public DoubleProperty lowProperty() {
        return low;
    }

    public double getPriceChangePercent() {
        return priceChangePercent.get();
    }

    public void setPriceChangePercent(double value) {
        priceChangePercent.set(value);
    }

    public DoubleProperty priceChangePercentProperty() {
        return priceChangePercent;
    }

    @FXML
    public void initialize() {
        try {
            // Initialize coin images
            CoinImages.initializeImages();

            // Setup table columns
            setupTableColumns();

            // Setup table items and data loading
            cryptoTable.setItems(cryptoDataList);
            setupAutoRefresh();
            loadInitialData();

            // Setup other components
            setupArbitrageTable();
            setupPortfolioTable();
            setupTechnicalIndicators();
            initializeWebSocket();

        } catch (Exception e) {
            e.printStackTrace();
            statusBar.setText("Error initializing: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        // Setup icon column
        iconCol.setCellValueFactory(data -> data.getValue().iconViewProperty());
        iconCol.setCellFactory(col -> new TableCell<CryptoData, ImageView>() {
            @Override
            protected void updateItem(ImageView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    item.setFitHeight(24);
                    item.setFitWidth(24);
                    item.setPreserveRatio(true);
                    setGraphic(item);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
        iconCol.setPrefWidth(40);

        // Setup other columns
        nameCol.setCellValueFactory(data -> data.getValue().readableNameProperty());
        symbolCol.setCellValueFactory(data -> data.getValue().nameProperty());
        priceCol.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        changeCol.setCellValueFactory(data -> data.getValue().priceChangePercentProperty().asObject());
        highCol.setCellValueFactory(data -> data.getValue().highProperty().asObject());
        lowCol.setCellValueFactory(data -> data.getValue().lowProperty().asObject());
        marketCapCol.setCellValueFactory(data -> data.getValue().marketCapProperty().asObject());
    }

    @FXML
    private void setupPriceColumnFormatting() {
        priceCol.setCellFactory(col -> new TableCell<CryptoData, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
                    setText(formatter.format(value));
                }
            }
        });
    }

    private void loadInitialData() {
        refreshData();
    }

    private void setupAutoRefresh() {
        autoRefreshTimeline = new Timeline(new KeyFrame(Duration.seconds(30), e -> refreshData()));
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void initializeWebSocket() {
        webSocketService = new WebSocketService(cryptoDataList);
    }

    private void updatePriceChart(CryptoData crypto) {
        if (crypto == null)
            return;

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(crypto.getName());

        List<Double> prices = crypto.getPriceHistory();
        for (int i = 0; i < prices.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, prices.get(i)));
        }

        priceChart.getData().clear();
        priceChart.getData().add(series);
    }

    @FXML
    public void search() {
        try {
            String query = searchBar.getText().toLowerCase().trim();
            ObservableList<CryptoData> filteredList = FXCollections.observableArrayList();

            for (CryptoData data : cryptoDataList) {
                if (data.getName().toLowerCase().contains(query)) {
                    filteredList.add(data);
                }
            }

            cryptoTable.setItems(filteredList);
        } catch (Exception e) {
            statusBar.setText("Search error: " + e.getMessage());
        }
    }

    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private Label errorLabel;

    @FXML
    public void refreshData() {
        loadingIndicator.setVisible(true);
        errorLabel.setVisible(false);
        statusBar.setText("Fetching latest cryptocurrency data...");

        CompletableFuture.supplyAsync(() -> {
            try {
                ApiIntegration api = new ApiIntegration();
                return api.fetchDetailedCryptoData();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(cryptoData -> {
            Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                if (cryptoData.isEmpty()) {
                    errorLabel.setText("No data available");
                    errorLabel.setVisible(true);
                } else {
                    cryptoDataList.setAll(cryptoData);
                    statusBar.setText("Successfully loaded " + cryptoData.size() + " cryptocurrencies");
                }
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                errorLabel.setText("Error: " + e.getCause().getMessage());
                errorLabel.setVisible(true);
            });
            return null;
        });
    }

    @FXML
    public void refreshArbitrage() {
        statusBar.setText("Finding arbitrage opportunities...");
        arbitrageTable.setPlaceholder(new Label("Searching..."));

        CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Map<String, Double>> priceMap = CryptoData.toPriceMap(cryptoDataList);
                return ArbitrageFinder.findArbitrageOpportunities(priceMap);
            } catch (Exception e) {
                return new ArrayList<ArbitrageOpportunity>();
            }
        }).thenAccept(opportunities -> {
            Platform.runLater(() -> {
                arbitrageList.clear();
                if (!opportunities.isEmpty()) {
                    arbitrageList.addAll(opportunities);
                    statusBar.setText(String.format("Found %d arbitrage opportunities", opportunities.size()));
                } else {
                    statusBar.setText("No profitable arbitrage opportunities found");
                    arbitrageTable.setPlaceholder(new Label("No opportunities found"));
                }
            });
        });
    }

    @FXML
    public void visualizeCurrentGraph() {
        // Implementation for graph visualization
        statusBar.setText("Graph visualization not implemented yet");
    }

    @FXML
    public void showShortestPath() {
        // Implementation for shortest path
        statusBar.setText("Shortest path visualization not implemented yet");
    }

    @FXML
    public void displayArbitrageDetails() {
        // Implementation for arbitrage details
        statusBar.setText("Arbitrage details not implemented yet");
    }

    @FXML
    public void convertCurrency() {
        // Implementation for currency conversion
        statusBar.setText("Currency conversion not implemented yet");
    }

    @FXML
    public void findBestConversion() {
        // Implementation for finding best conversion path
        statusBar.setText("Best conversion path not implemented yet");
    }

    private void updatePriceChart() {
        // Create price chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Price History");

        // Add data series for each selected cryptocurrency
        cryptoTable.getSelectionModel().getSelectedItems().forEach(crypto -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(crypto.getName());

            List<Double> prices = crypto.getPriceHistory();
            for (int i = 0; i < prices.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, prices.get(i)));
            }

            lineChart.getData().add(series);
        });

        // Style the chart
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.getStyleClass().add("crypto-chart");

        graphPane.getChildren().setAll(lineChart);
    }

    private List<Node> getAllNodes(Parent root) {
        List<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private void addAllDescendents(Parent parent, List<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
    }

    private Callback<TableColumn<CryptoData, Double>, TableCell<CryptoData, Double>> getPercentageCellFactory() {
        return column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f%%", value));
                    if (value > 0) {
                        setStyle("-fx-text-fill: #00b15d;"); // Green for positive
                    } else if (value < 0) {
                        setStyle("-fx-text-fill: #ff6666;"); // Red for negative
                    } else {
                        setStyle("-fx-text-fill: white;"); // Default color
                    }
                }
            }
        };
    }

    private void setupPortfolioTable() {
        TableColumn<Portfolio.Position, String> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(data -> data.getValue().symbolProperty());

        TableColumn<Portfolio.Position, Number> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> data.getValue().amountProperty());

        TableColumn<Portfolio.Position, Number> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(data -> data.getValue().currentValueProperty());

        TableColumn<Portfolio.Position, Number> plCol = new TableColumn<>("P/L");
        plCol.setCellValueFactory(data -> data.getValue().profitLossProperty());

        portfolioTable.getColumns().addAll(symbolCol, amountCol, valueCol, plCol);
        portfolioTable.setItems(userPortfolio.getPositions());
    }

    private void setupTechnicalIndicators() {
        CheckBox showRSI = new CheckBox("Show RSI");
        CheckBox showMA = new CheckBox("Show Moving Average");
        CheckBox showBB = new CheckBox("Show Bollinger Bands");

        showRSI.setOnAction(e -> updateChartIndicators());
        showMA.setOnAction(e -> updateChartIndicators());
        showBB.setOnAction(e -> updateChartIndicators());

        // Add checkboxes to the UI
        // ...
    }

    private void updateChartIndicators() {
        CryptoData selectedCrypto = cryptoTable.getSelectionModel().getSelectedItem();
        if (selectedCrypto == null)
            return;

        List<Double> prices = selectedCrypto.getPriceHistory();
        // Rest of method implementation...
    }

    @FXML
    private void addAlert() {
        CryptoData selected = cryptoTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        Dialog<CryptoAlert> dialog = new Dialog<>();
        dialog.setTitle("Add Price Alert");

        // Create dialog content
        // ...

        dialog.showAndWait().ifPresent(alert -> alerts.add(alert));
    }

    private void setupArbitrageTable() {
        // Format price columns
        buyPriceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        sellPriceCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        // Format profit column with colors
        profitCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double profit, boolean empty) {
                super.updateItem(profit, empty);
                if (empty || profit == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f%%", profit));
                    setStyle(profit >= 0 ? "-fx-text-fill: #00b15d; -fx-font-weight: bold;"
                            : "-fx-text-fill: #ff6666; -fx-font-weight: bold;");
                }
            }
        });

        // Setup min profit slider
        minProfitSlider.valueProperty().addListener((obs, old, newValue) -> {
            minProfitLabel.setText(String.format("%.2f%%", newValue.doubleValue()));
            refreshArbitrage();
        });

        // Setup auto-refresh
        arbitrageRefreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(30), e -> refreshArbitrage()));
        arbitrageRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void toggleAutoRefresh() {
        autoRefreshEnabled = !autoRefreshEnabled;
        if (autoRefreshEnabled) {
            arbitrageRefreshTimeline.play();
            autoRefreshButton.setText("Stop Auto-Refresh");
            autoRefreshButton.setStyle("-fx-background-color: #ff6666;");
        } else {
            arbitrageRefreshTimeline.stop();
            autoRefreshButton.setText("Start Auto-Refresh");
            autoRefreshButton.setStyle("");
        }
    }

    private void updateUI() {
        cryptoTable.refresh();
        CryptoData selectedCrypto = cryptoTable.getSelectionModel().getSelectedItem();
        if (selectedCrypto != null) {
            updatePriceChart(selectedCrypto);
        }
        refreshArbitrage();
    }

    private void showTooltip(Node node, String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.getStyleClass().add("chart-tooltip");
        Tooltip.install(node, tooltip);
    }
}