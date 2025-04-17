package ui;

import arbitrage.ArbitrageFinder;
import arbitrage.ArbitrageOpportunity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import logic.CryptoData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
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
    private TextField minProfitField;
    @FXML
    private CheckBox highlightToggle;
    @FXML
    private Button exportCsvButton;
    @FXML
    private BarChart<String, Number> profitBarChart;

    private List<ArbitrageOpportunity> allOpportunities = new ArrayList<>();

    @FXML
    public void initialize() {
        // Set up table columns
        marketCol.setCellValueFactory(new PropertyValueFactory<>("buyExchange"));
        pairCol.setCellValueFactory(new PropertyValueFactory<>("coin"));
        buyPriceCol.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        profitCol.setCellValueFactory(new PropertyValueFactory<>("profitPercentage"));

        // Custom cell factory to color the profit column
        profitCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double profit, boolean empty) {
                super.updateItem(profit, empty);
                if (empty || profit == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f%%", profit));
                    setStyle("-fx-text-fill: " + (profit >= 0 ? "green;" : "red;"));
                }
            }
        });

        // Highlight entire row if profit > 5% when toggle is selected
        arbitrageTable.setRowFactory(tableView -> new TableRow<>() {
            @Override
            protected void updateItem(ArbitrageOpportunity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    if (highlightToggle.isSelected() && item.getProfitPercentage() > 5) {
                        setStyle("-fx-background-color: #ffffcc;"); // light yellow highlight
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // Listener to refresh row highlighting when toggle changes
        highlightToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            arbitrageTable.refresh();
        });

        exportCsvButton.setOnAction(e -> exportToCSV());
        loadOpportunities();
    }

    private void loadOpportunities() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                // Fetch opportunities from API (via CryptoData and ArbitrageFinder)
                Map<String, Map<String, Double>> data = CryptoData.fetchLatestCryptoData();
                List<ArbitrageOpportunity> opportunities = ArbitrageFinder.findArbitrageOpportunities(data);
                Platform.runLater(() -> {
                    allOpportunities = opportunities;
                    applyProfitFilter();
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Error", "Failed to load arbitrage data."));
            }
        });
    }

    @FXML
    private void applyProfitFilter() {
        double minProfit = 0.0;
        try {
            String input = minProfitField.getText();
            if (input != null && !input.trim().isEmpty()) {
                minProfit = Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for min profit %.");
            return;
        }

        List<ArbitrageOpportunity> filtered = allOpportunities.stream()
                .filter(opp -> opp.getProfitPercentage() >= minProfit)
                .sorted(Comparator.comparingDouble(ArbitrageOpportunity::getProfitPercentage).reversed())
                .collect(Collectors.toList());

        arbitrageTable.getItems().setAll(filtered);
        updateBarChart(filtered);
    }

    private void updateBarChart(List<ArbitrageOpportunity> opportunities) {
        // Clear previous chart data
        profitBarChart.getData().clear();

        // Get top 5 opportunities by profit % (if available)
        List<ArbitrageOpportunity> topOpportunities = opportunities.stream()
                .sorted(Comparator.comparingDouble(ArbitrageOpportunity::getProfitPercentage).reversed())
                .limit(5)
                .collect(Collectors.toList());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < topOpportunities.size(); i++) {
            ArbitrageOpportunity opp = topOpportunities.get(i);
            // Use the coin/pair as the category label
            series.getData().add(new XYChart.Data<>(opp.getCoin(), opp.getProfitPercentage()));
        }
        profitBarChart.getData().add(series);
    }

    private void exportToCSV() {
        // Let user choose the location to save the CSV file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.setInitialFileName("arbitrage_opportunities.csv");
        File file = fileChooser.showSaveDialog(exportCsvButton.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write CSV header
                writer.write("Market,Pair,Buy Price,Sell Price,Profit (%)\n");

                // Export the currently filtered results
                for (ArbitrageOpportunity opp : arbitrageTable.getItems()) {
                    writer.write(String.format("%s,%s,%.2f,%.2f,%.2f\n",
                            opp.getBuyExchange(),
                            opp.getCoin(),
                            opp.getBuyPrice(),
                            opp.getSellPrice(),
                            opp.getProfitPercentage()));
                }
                showAlert("Export Successful", "CSV file exported successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Export Failed", "Error occurred while exporting CSV.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
