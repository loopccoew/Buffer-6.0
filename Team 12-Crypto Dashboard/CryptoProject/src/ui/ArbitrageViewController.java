package ui;

import arbitrage.ArbitrageOpportunity;
import arbitrage.ArbitrageFinder;
import logic.CryptoData;
import api.ApiIntegration;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.application.Platform;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ArbitrageViewController {
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
    private Label statusBar;
    @FXML
    private CheckBox autoRefresh;
    @FXML
    private ChoiceBox<String> languageChoice;
    @FXML
    private Slider minProfitSlider;
    @FXML
    private ObservableList<ArbitrageOpportunity> arbitrageList = FXCollections.observableArrayList();

    private ObservableList<ArbitrageOpportunity> opportunities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup columns
        marketCol.setCellValueFactory(data -> data.getValue().marketProperty());
        pairCol.setCellValueFactory(data -> data.getValue().pairProperty());
        buyPriceCol.setCellValueFactory(data -> data.getValue().buyPriceProperty().asObject());
        sellPriceCol.setCellValueFactory(data -> data.getValue().sellPriceProperty().asObject());
        profitCol.setCellValueFactory(data -> data.getValue().profitProperty().asObject());

        // Setup language selector
        languageChoice.setItems(FXCollections.observableArrayList(
                "English", "Spanish", "French", "German", "Chinese", "Japanese"));
        languageChoice.setValue("English");

        arbitrageTable.setItems(opportunities);
        findArbitrage();
    }

    @FXML
    public void findArbitrage() {
        final ApiIntegration api = new ApiIntegration();
        CompletableFuture.supplyAsync(() -> {
            try {
                final List<CryptoData> cryptoData = api.fetchDetailedCryptoData();
                final Map<String, Map<String, Double>> priceMap = CryptoData.toPriceMap(cryptoData);
                return ArbitrageFinder.findArbitrageOpportunities(priceMap);
            } catch (Exception e) {
                final String errorMessage = e.getMessage();
                Platform.runLater(() -> statusBar.setText("Error: " + errorMessage));
                return new ArrayList<>();
            }
        }).thenAccept(opportunities -> Platform.runLater(() -> {
            this.opportunities.setAll(opportunities);
            statusBar.setText("Found " + opportunities.size() + " opportunities");
        }));
    }

    private void processArbitrageResults(List<ArbitrageOpportunity> opportunities) {
        final double minProfit = minProfitSlider.getValue();
        List<ArbitrageOpportunity> filtered = opportunities.stream()
                .filter(opp -> opp.getProfitPercentage() >= minProfit)
                .collect(Collectors.toList());
        arbitrageList.setAll(filtered);
    }
}
