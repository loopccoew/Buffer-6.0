package ui;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import logic.CryptoData;
import arbitrage.ArbitrageFinder;
import arbitrage.ArbitrageOpportunity;

import java.util.*;
import java.util.concurrent.*;

public class CryptoDashboardController {

    @FXML
    private TableView<CryptoData> cryptoTable;
    @FXML
    private TableColumn<CryptoData, String> symbolCol;
    @FXML
    private TableColumn<CryptoData, String> nameCol;
    @FXML
    private TableColumn<CryptoData, Double> priceCol;
    @FXML
    private TableColumn<CryptoData, Double> changeCol;
    @FXML
    private TableColumn<CryptoData, Double> highCol;
    @FXML
    private TableColumn<CryptoData, Double> lowCol;
    @FXML
    private TableColumn<CryptoData, String> readableNameCol;
    @FXML
    private TableColumn<CryptoData, Double> percentChangeCol;
    @FXML
    private TableColumn<CryptoData, String> currencyNameCol; // New column for cryptocurrency name
    @FXML
    private TableColumn<CryptoData, Node> graphCol; // Fixed priceGraph column

    @FXML
    private CheckBox autoRefreshToggle;
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
    private Label conversionResult;
    @FXML
    private Label profitPercentageLabel;
    @FXML
    private TextField searchBar;
    @FXML
    private LineChart<Number, Number> priceTrendChart;
    @FXML
    private Label arbitrageAlert;
    @FXML
    private TextField currencyInput;
    @FXML
    private ChoiceBox<String> currencyFrom;
    @FXML
    private ChoiceBox<String> currencyTo;
    @FXML
    private Label statusBar;
    @FXML
    private TableView<String> namesTable;
    @FXML
    private TableColumn<String, String> nameColumn;

    @FXML
    private TableView<CryptoData> trendingTable;
    @FXML
    private TableColumn<CryptoData, String> trendingSymbolCol;
    @FXML
    private TableColumn<CryptoData, String> trendingNameCol;
    @FXML
    private TableColumn<CryptoData, Double> trendingPriceCol;
    @FXML
    private TableColumn<CryptoData, Double> trendingChangeCol;

    @FXML
    private Label detailedSymbol;
    @FXML
    private Label detailedName;
    @FXML
    private Label detailedPrice;
    @FXML
    private Label detailedChange;
    @FXML
    private Label detailedHigh;
    @FXML
    private Label detailedLow;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private ScheduledExecutorService priceUpdateScheduler;
    private ScheduledExecutorService autoRefreshScheduler;
    private final Map<String, Double> previousPrices = new HashMap<>();

    @FXML
    public void initialize() {
        // Debug logs to verify initialization
        System.out.println("cryptoTable: " + cryptoTable);
        System.out.println("arbitrageTable: " + arbitrageTable);

        // Ensure columns are properly bound
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Add a listener to handle row selection safely
        cryptoTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    System.out.println("Selected: " + newSelection.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setupCryptoTable();
        setupArbitrageTable();
        populateCurrencyOptions();
        setupCurrencyConversionListeners();

        if (autoRefreshToggle.isSelected()) {
            startAutoRefresh();
        }

        fetchCryptoData();
        startPriceUpdates();

        nameColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));
        namesTable.getItems().addAll("Alice", "Bob", "Charlie", "Diana");

        fetchArbitrageOpportunities();

        // Ensure all columns are correctly bound to existing properties
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        changeCol.setCellValueFactory(new PropertyValueFactory<>("change"));
        highCol.setCellValueFactory(new PropertyValueFactory<>("high"));
        lowCol.setCellValueFactory(new PropertyValueFactory<>("low"));
        percentChangeCol.setCellValueFactory(new PropertyValueFactory<>("percentChange"));
        readableNameCol.setCellValueFactory(new PropertyValueFactory<>("readableName"));
        currencyNameCol.setCellValueFactory(new PropertyValueFactory<>("readableName"));

        // Fix graphCol binding
        graphCol.setCellValueFactory(new PropertyValueFactory<>("priceGraph"));
        graphCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || item == null ? null : item);
            }
        });

        // Bind the new currency name column
        currencyNameCol.setCellValueFactory(new PropertyValueFactory<>("readableName"));

        // Add the new column next to the symbol column
        cryptoTable.getColumns().add(1, currencyNameCol);

        // Bind trending table columns
        trendingSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        trendingNameCol.setCellValueFactory(new PropertyValueFactory<>("readableName"));
        trendingPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        trendingChangeCol.setCellValueFactory(new PropertyValueFactory<>("percentChange"));

        // Add listener for detailed view
        cryptoTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                detailedSymbol.setText(newSelection.getName());
                detailedName.setText(newSelection.getReadableName());
                detailedPrice.setText(String.format("$%,.2f", newSelection.getPrice()));
                detailedChange.setText(String.format("%.2f%%", newSelection.getPriceChangePercent()));
                detailedHigh.setText(String.format("$%,.2f", newSelection.getHigh()));
                detailedLow.setText(String.format("$%,.2f", newSelection.getLow()));
            }
        });

        // Bind arbitrage table columns
        marketCol.setCellValueFactory(new PropertyValueFactory<>("buyExchange"));
        pairCol.setCellValueFactory(new PropertyValueFactory<>("coin"));
        buyPriceCol.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        profitCol.setCellValueFactory(new PropertyValueFactory<>("profitPercentage"));

        // Format buy and sell prices as currency
        buyPriceCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%,.2f", price));
            }
        });

        sellPriceCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%,.2f", price));
            }
        });

        // Format profit percentage with two decimal places
        profitCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double profit, boolean empty) {
                super.updateItem(profit, empty);
                setText(empty || profit == null ? null : String.format("%.2f%%", profit));
            }
        });

        // Start automatic data refresh
        startAutoRefresh();

        // Monitor for price spikes and dips
        monitorPriceChanges();
    }

    private void setupCryptoTable() {
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        readableNameCol.setCellValueFactory(new PropertyValueFactory<>("readableName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        changeCol.setCellValueFactory(new PropertyValueFactory<>("change"));
        highCol.setCellValueFactory(new PropertyValueFactory<>("high"));
        lowCol.setCellValueFactory(new PropertyValueFactory<>("low"));
        percentChangeCol.setCellValueFactory(new PropertyValueFactory<>("percentChange"));
        currencyNameCol.setCellValueFactory(new PropertyValueFactory<>("readableName"));

        graphCol.setCellValueFactory(new PropertyValueFactory<>("priceGraph")); // 👈

        graphCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || item == null ? null : item);
            }
        });

        priceCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%,.2f", price));
            }
        });

        changeCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double change, boolean empty) {
                super.updateItem(change, empty);
                if (empty || change == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f%%", change));
                    setTextFill(change >= 0 ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);
                }
            }
        });

        cryptoTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(CryptoData item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getPercentChange() > 0) {
                        setStyle("-fx-background-color: #d4edda;");
                    } else if (item.getPercentChange() < 0) {
                        setStyle("-fx-background-color: #f8d7da;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void setupArbitrageTable() {
        marketCol.setCellValueFactory(new PropertyValueFactory<>("buyExchange"));
        pairCol.setCellValueFactory(new PropertyValueFactory<>("coin"));
        buyPriceCol.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        profitCol.setCellValueFactory(new PropertyValueFactory<>("profitPercentage"));

        arbitrageTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                profitPercentageLabel.setText(String.format("Profit: %.2f%%", newSel.getProfitPercentage()));
            } else {
                profitPercentageLabel.setText("Profit: N/A");
            }
        });
    }

    @FXML
    private void populateCurrencyOptions() {
        // Define available cryptocurrencies
        Map<String, String> currencies = Map.of(
                "BTC", "Bitcoin",
                "ETH", "Ethereum",
                "USDT", "Tether",
                "BNB", "Binance Coin",
                "ADA", "Cardano",
                "XRP", "Ripple",
                "SOL", "Solana",
                "DOT", "Polkadot",
                "DOGE", "Dogecoin",
                "MATIC", "Polygon");

        // Populate the dropdowns
        currencies.forEach((symbol, name) -> {
            currencyFrom.getItems().add(symbol + " (" + name + ")");
            currencyTo.getItems().add(symbol + " (" + name + ")");
        });

        // Set default values
        currencyFrom.setValue("BTC (Bitcoin)");
        currencyTo.setValue("ETH (Ethereum)");
    }

    private void setupCurrencyConversionListeners() {
        currencyFrom.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> updateConversion());
        currencyTo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateConversion());
        currencyInput.textProperty().addListener((obs, oldVal, newVal) -> updateConversion());

        autoRefreshToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected)
                startAutoRefresh();
            else
                stopAutoRefresh();
        });
    }

    private void updateConversion() {
        try {
            String from = currencyFrom.getValue();
            String to = currencyTo.getValue();
            String input = currencyInput.getText();

            if (from == null || to == null || input.isEmpty()) {
                conversionResult.setText("Conversion Result: ");
                return;
            }

            if (from.equals(to)) {
                conversionResult.setText("Conversion Result: Source and target currencies cannot be the same.");
                return;
            }

            double amount = Double.parseDouble(input);
            double conversionRate = 1.1; // Placeholder
            double result = amount * conversionRate;
            conversionResult.setText(String.format("Converted: %.2f %s", result, to));
        } catch (NumberFormatException e) {
            conversionResult.setText("Conversion Result: Invalid amount.");
        }
    }

    private void fetchCryptoData() {
        statusBar.setText("Fetching data...");
        executorService.submit(() -> {
            try {
                Map<String, Map<String, Double>> fetched = CryptoData.fetchLatestCryptoData();
                List<CryptoData> cryptoList = new ArrayList<>();

                for (Map.Entry<String, Map<String, Double>> entry : fetched.entrySet()) {
                    CryptoData data = new CryptoData(entry.getKey(), entry.getKey());
                    data.getExchangePrices().putAll(entry.getValue());
                    data.updatePrices(); // ensure price history is filled
                    data.setPriceGraph(GraphUtil.generateGraphNode(data.getPriceHistory())); // 👈 Optional graph
                                                                                             // generator

                    // Check for price spikes or dips
                    checkForPriceSpikesAndDips(data);

                    cryptoList.add(data);
                }

                Platform.runLater(() -> {
                    cryptoTable.getItems().setAll(cryptoList);
                    statusBar.setText("Data fetched successfully.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> statusBar.setText("Error: " + e.getMessage()));
                e.printStackTrace();
            }
        });
    }

    private void checkForPriceSpikesAndDips(CryptoData data) {
        double currentPrice = data.getPrice("Binance"); // Example: using Binance price
        if (currentPrice <= 0)
            return;

        String symbol = data.getName();
        if (previousPrices.containsKey(symbol)) {
            double previousPrice = previousPrices.get(symbol);
            double changePercent = ((currentPrice - previousPrice) / previousPrice) * 100;

            if (changePercent >= 5) {
                showNotification("Price Spike", symbol + " price spiked by " + String.format("%.2f%%", changePercent));
            } else if (changePercent <= -5) {
                showNotification("Price Dip", symbol + " price dipped by " + String.format("%.2f%%", changePercent));
            }
        }
        previousPrices.put(symbol, currentPrice);
    }

    private void showNotification(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

    private void monitorPriceChanges() {
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10000); // Check every 10 seconds
                    Platform.runLater(this::fetchCryptoData);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private void fetchArbitrageOpportunities() {
        executorService.submit(() -> {
            try {
                Map<String, Map<String, Double>> priceMap = CryptoData.fetchLatestCryptoData();
                List<ArbitrageOpportunity> opportunities = ArbitrageFinder.findArbitrageOpportunities(priceMap);

                Platform.runLater(() -> {
                    arbitrageTable.getItems().setAll(opportunities);
                    statusBar.setText("Arbitrage opportunities updated.");
                });
            } catch (Exception e) {
                Platform.runLater(() -> statusBar.setText("Error fetching arbitrage opportunities: " + e.getMessage()));
                e.printStackTrace();
            }
        });
    }

    private void startPriceUpdates() {
        priceUpdateScheduler = Executors.newSingleThreadScheduledExecutor();
        priceUpdateScheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                for (CryptoData crypto : cryptoTable.getItems()) {
                    // Update your graph here, or animate values
                }
            });
        }, 0, 3, TimeUnit.SECONDS);
    }

    private void stopPriceUpdates() {
        if (priceUpdateScheduler != null && !priceUpdateScheduler.isShutdown()) {
            priceUpdateScheduler.shutdown();
        }
    }

    private void startAutoRefresh() {
        autoRefreshScheduler = Executors.newSingleThreadScheduledExecutor();
        autoRefreshScheduler.scheduleAtFixedRate(() -> Platform.runLater(this::fetchCryptoData), 0, 10,
                TimeUnit.SECONDS);
    }

    private void stopAutoRefresh() {
        if (autoRefreshScheduler != null && !autoRefreshScheduler.isShutdown()) {
            autoRefreshScheduler.shutdown();
        }
    }

    @FXML
    private void search() {
        System.out.println("Search clicked");
        // Add your logic to filter/search coins here
    }

    @FXML
    private void refreshData() {
        System.out.println("Refreshing data...");
        // Logic to refresh crypto data
    }

    @FXML
    private void refreshArbitrage() {
        System.out.println("Refreshing arbitrage opportunities...");
        // Logic to refresh arbitrage data
    }

    @FXML
    private void convertCurrency() {
        System.out.println("Converting currency...");
        // Logic for currency conversion
    }

    @FXML
    private void findBestConversion() {
        System.out.println("Finding best conversion path...");
        // Logic for best conversion path
    }

    @FXML
    private void displayArbitrageDetails() {
        System.out.println("Displaying arbitrage details...");
        // Add logic to display arbitrage details
    }

    @FXML
    private void showTrending() {
        executorService.submit(() -> {
            List<CryptoData> trendingCryptos = fetchTrendingCryptos(); // Replace with actual API call if available
            Platform.runLater(() -> {
                trendingTable.getItems().setAll(trendingCryptos);
                statusBar.setText("Trending cryptocurrencies updated.");
            });
        });
    }

    @FXML
    private void convertCurrency() {
        try {
            String from = currencyFrom.getValue();
            String to = currencyTo.getValue();
            String input = currencyInput.getText();

            if (from == null || to == null || input.isEmpty()) {
                conversionResult.setText("Conversion Result: Invalid input.");
                return;
            }

            if (from.equals(to)) {
                conversionResult.setText("Conversion Result: Source and target currencies cannot be the same.");
                return;
            }

            double amount = Double.parseDouble(input);

            // Placeholder conversion rate logic
            double conversionRate = fetchConversionRate(from.split(" ")[0], to.split(" ")[0]);
            double result = amount * conversionRate;

            conversionResult.setText(String.format("Converted: %.6f %s", result, to));
        } catch (NumberFormatException e) {
            conversionResult.setText("Conversion Result: Invalid amount.");
        } catch (Exception e) {
            conversionResult.setText("Conversion Result: Error during conversion.");
            e.printStackTrace();
        }
    }

    private double fetchConversionRate(String from, String to) {
        // Placeholder logic for fetching conversion rate
        // Replace this with actual API integration or logic
        Map<String, Double> mockRates = Map.of(
                "BTC-ETH", 14.5,
                "ETH-BTC", 0.069,
                "BTC-USDT", 50000.0,
                "USDT-BTC", 0.00002);

        String key = from + "-" + to;
        return mockRates.getOrDefault(key, 1.0); // Default to 1.0 if no rate is found
    }

    @FXML
    private void findBestConversion() {
        String from = currencyFrom.getValue();
        String to = currencyTo.getValue();

        if (from == null || to == null || from.equals(to)) {
            conversionResult.setText("Invalid conversion selection.");
            return;
        }

        executorService.submit(() -> {
            try {
                Map<String, Map<String, Double>> priceMap = CryptoData.fetchLatestCryptoData();
                List<String> bestPath = ArbitrageFinder.findBestConversionPath(priceMap, from.split(" ")[0],
                        to.split(" ")[0]);

                Platform.runLater(() -> {
                    if (bestPath.isEmpty()) {
                        conversionResult.setText("No valid conversion path found.");
                    } else {
                        conversionResult.setText("Best Path: " + String.join(" -> ", bestPath));
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> conversionResult.setText("Error finding conversion path."));
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void findMaxProfit() {
        executorService.submit(() -> {
            try {
                Map<String, Map<String, Double>> priceMap = CryptoData.fetchLatestCryptoData();
                List<ArbitrageOpportunity> opportunities = ArbitrageFinder.findArbitrageOpportunities(priceMap);

                if (!opportunities.isEmpty()) {
                    ArbitrageOpportunity bestOpportunity = opportunities.stream()
                            .max(Comparator.comparingDouble(ArbitrageOpportunity::getProfitPercentage))
                            .orElse(null);

                    if (bestOpportunity != null) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Max Profit Opportunity");
                            alert.setHeaderText("Best Arbitrage Opportunity Found");
                            alert.setContentText(bestOpportunity.toString());
                            alert.show();
                        });
                    }
                }
            } catch (Exception e) {
                Platform.runLater(() -> statusBar.setText("Error finding max profit: " + e.getMessage()));
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void shutdown() {
        stopAutoRefresh();
        executorService.shutdown();
    }

    @FXML
    private void showTrending() {
        // Fetch and display trending cryptocurrencies
        executorService.submit(() -> {
            List<CryptoData> trendingCryptos = fetchTrendingCryptos(); // Implement this method
            Platform.runLater(() -> trendingTable.getItems().setAll(trendingCryptos));
        });
    }

    private List<CryptoData> fetchTrendingCryptos() {
        // Mock implementation, replace with API call
        return List.of(
                new CryptoData("BTC", "Bitcoin", 50000.0, 2.5),
                new CryptoData("ETH", "Ethereum", 4000.0, 3.1),
                new CryptoData("DOGE", "Dogecoin", 0.25, 5.0));
    }

    @FXML
    private void findBestConversion() {
        String from = currencyFrom.getValue();
        String to = currencyTo.getValue();

        if (from == null || to == null || from.equals(to)) {
            conversionResult.setText("Invalid conversion selection.");
            return;
        }

        executorService.submit(() -> {
            try {
                Map<String, Map<String, Double>> priceMap = CryptoData.fetchLatestCryptoData();
                List<String> bestPath = ArbitrageFinder.findBestConversionPath(priceMap, from.split(" ")[0],
                        to.split(" ")[0]);

                Platform.runLater(() -> {
                    if (bestPath.isEmpty()) {
                        conversionResult.setText("No valid conversion path found.");
                    } else {
                        conversionResult.setText("Best Path: " + String.join(" -> ", bestPath));
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> conversionResult.setText("Error finding conversion path."));
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void findMaxProfit() {
        executorService.submit(() -> {
            try {
                Map<String, Map<String, Double>> priceMap = CryptoData.fetchLatestCryptoData();
                List<ArbitrageOpportunity> opportunities = ArbitrageFinder.findArbitrageOpportunities(priceMap);

                if (!opportunities.isEmpty()) {
                    ArbitrageOpportunity bestOpportunity = opportunities.stream()
                            .max(Comparator.comparingDouble(ArbitrageOpportunity::getProfitPercentage))
                            .orElse(null);

                    if (bestOpportunity != null) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Max Profit Opportunity");
                            alert.setHeaderText("Best Arbitrage Opportunity Found");
                            alert.setContentText(bestOpportunity.toString());
                            alert.show();
                        });
                    }
                }
            } catch (Exception e) {
                Platform.runLater(() -> statusBar.setText("Error finding max profit: " + e.getMessage()));
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void shutdown() {
        stopAutoRefresh();
        executorService.shutdown();
    }
}
