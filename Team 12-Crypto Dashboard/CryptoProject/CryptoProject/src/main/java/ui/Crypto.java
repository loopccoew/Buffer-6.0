package ui;

import javafx.beans.property.*;

public class Crypto {
    private final StringProperty name;
    private final DoubleProperty price;

    public Crypto(String name, Double price) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty priceProperty() {
        return price;
    }
}
