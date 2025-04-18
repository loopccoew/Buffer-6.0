module CryptoProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.base;

    opens ui to javafx.fxml;
}
