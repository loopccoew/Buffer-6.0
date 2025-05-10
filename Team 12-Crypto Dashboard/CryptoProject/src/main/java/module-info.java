module com.example.CryptoProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;

    opens ui to javafx.fxml;

    exports ui;
    exports logic;
    exports api;
    exports interfaces;
}
