module CryptoProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires com.google.gson;
    requires java.net.http;
    requires org.java_websocket;

    exports ui;
    exports models to javafx.base, com.google.gson;
    exports logic to javafx.base;
    exports api;
    exports arbitrage;
    exports services;
    exports interfaces;
    exports utils;

    opens ui;
    opens models to com.google.gson;
    opens logic to javafx.base;
}
