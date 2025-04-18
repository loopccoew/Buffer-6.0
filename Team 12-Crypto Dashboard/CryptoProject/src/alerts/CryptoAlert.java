package alerts;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class CryptoAlert {
    private StringProperty symbol = new SimpleStringProperty();
    private DoubleProperty targetPrice = new SimpleDoubleProperty();
    private StringProperty condition = new SimpleStringProperty(); // "ABOVE" or "BELOW"
    private BooleanProperty triggered = new SimpleBooleanProperty(false);
    private ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>(LocalDateTime.now());

    public CryptoAlert(String symbol, double targetPrice, String condition) {
        this.symbol.set(symbol);
        this.targetPrice.set(targetPrice);
        this.condition.set(condition);
    }

    public boolean checkCondition(double currentPrice) {
        if (!triggered.get()) {
            boolean isTriggered = condition.get().equals("ABOVE") ? currentPrice >= targetPrice.get()
                    : currentPrice <= targetPrice.get();

            if (isTriggered) {
                triggered.set(true);
                return true;
            }
        }
        return false;
    }

    // Add property getters
    public StringProperty symbolProperty() {
        return symbol;
    }

    public DoubleProperty targetPriceProperty() {
        return targetPrice;
    }

    public StringProperty conditionProperty() {
        return condition;
    }

    public BooleanProperty triggeredProperty() {
        return triggered;
    }
}
