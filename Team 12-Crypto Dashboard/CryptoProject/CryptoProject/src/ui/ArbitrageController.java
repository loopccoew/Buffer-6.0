package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import arbitrage.ArbitrageOpportunity;
import logic.CryptoData;
import api.ApiIntegration;
import arbitrage.ArbitrageFinder;
import java.util.List;
import java.util.Map;

public class ArbitrageController {
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
    private Label statusLabel;
    @FXML
    private Label profitLabel;

    private ObservableList<ArbitrageOpportunity> opportunities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        marketCol.setCellValueFactory(cellData -> cellData.getValue().marketProperty());
        pairCol.setCellValueFactory(cellData -> cellData.getValue().pairProperty());
        buyPriceCol.setCellValueFactory(cellData -> cellData.getValue().buyPriceProperty().asObject());
        sellPriceCol.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty().asObject());
        profitCol.setCellValueFactory(cellData -> cellData.getValue().profitProperty().asObject());

        // Format profit column
        profitCol.setCellFactory(column -> new TableCell<ArbitrageOpportunity, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f%%", value));
                    setStyle(value >= 0 ? "-fx-text-fill: #00ff00;" : "-fx-text-fill: #ff0000;");
                }
            }
        });

        arbitrageTable.setItems(opportunities);
        refreshArbitrage();
    }

    @FXML
    private void refreshArbitrage() {
        try {
            statusLabel.setText("Fetching arbitrage opportunities...");
            ApiIntegration api = new ApiIntegration();
            List<CryptoData> cryptoData = api.fetchDetailedCryptoData();
            Map<String, Map<String, Double>> priceMap = CryptoData.toPriceMap(cryptoData);

            List<ArbitrageOpportunity> newOpportunities = ArbitrageFinder.findArbitrageOpportunities(priceMap);
            opportunities.setAll(newOpportunities);

            if (!newOpportunities.isEmpty()) {
                ArbitrageOpportunity best = newOpportunities.get(0);
                profitLabel.setText(String.format("Best Profit: %.2f%%", best.getProfitPercentage()));
                statusLabel.setText("Found " + newOpportunities.size() + " opportunities");
            } else {
                profitLabel.setText("No profitable opportunities found");
                statusLabel.setText("Ready");
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
