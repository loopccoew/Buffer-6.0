package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CryptoDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

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
