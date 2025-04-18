package models;

import javafx.beans.property.*;

public class CryptoAlert {
    private final StringProperty symbol;
    private final DoubleProperty targetPrice;
    private final BooleanProperty isAboveTarget;

    public CryptoAlert(String symbol, double targetPrice, boolean isAboveTarget) {
        this.symbol = new SimpleStringProperty(symbol);
        this.targetPrice = new SimpleDoubleProperty(targetPrice);
        this.isAboveTarget = new SimpleBooleanProperty(isAboveTarget);
    }

    // Add getters and property methods
}
