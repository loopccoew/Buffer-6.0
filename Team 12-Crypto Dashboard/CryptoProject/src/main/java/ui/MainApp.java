package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/CryptoDashboard.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);

        // Load the stylesheet
        try {
            scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: Could not load stylesheet. Ensure 'style.css' exists in the resources folder.");
        }

        primaryStage.setTitle("Crypto Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/*
 * java --module-path
 * "c:\Users\Asus\Desktop\CryptoProject\javafx-sdk-21.0.6\lib" --add-module
 * javafx.controls,javafx.fxml -cp "bin;lib\*" ui.MainApp
 */