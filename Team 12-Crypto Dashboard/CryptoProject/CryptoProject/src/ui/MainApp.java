package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            TabPane tabPane = new TabPane();

            // Market Overview Tab
            Tab marketTab = new Tab("Market Overview");
            FXMLLoader marketLoader = new FXMLLoader(getClass().getResource("/resources/MarketView.fxml"));
            Parent marketRoot = marketLoader.load();
            marketTab.setContent(marketRoot);
            marketTab.setClosable(false);

            // Arbitrage Tab
            Tab arbitrageTab = new Tab("Arbitrage");
            FXMLLoader arbitrageLoader = new FXMLLoader(getClass().getResource("/resources/ArbitrageView.fxml"));
            Parent arbitrageRoot = arbitrageLoader.load();
            arbitrageTab.setContent(arbitrageRoot);
            arbitrageTab.setClosable(false);

            tabPane.getTabs().addAll(marketTab, arbitrageTab);

            Scene scene = new Scene(tabPane, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());

            primaryStage.setTitle("Crypto Market Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start application: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
