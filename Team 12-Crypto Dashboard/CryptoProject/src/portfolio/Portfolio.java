package portfolio;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class Portfolio {
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty totalValue = new SimpleDoubleProperty();
    private final ObservableList<Position> positions = FXCollections.observableArrayList();

    public static class Position {
        private final StringProperty symbol = new SimpleStringProperty();
        private final DoubleProperty amount = new SimpleDoubleProperty();
        private final DoubleProperty avgBuyPrice = new SimpleDoubleProperty();
        private final DoubleProperty currentValue = new SimpleDoubleProperty();
        private final DoubleProperty profitLoss = new SimpleDoubleProperty();

        public Position(String symbol, double amount, double buyPrice) {
            this.symbol.set(symbol);
            this.amount.set(amount);
            this.avgBuyPrice.set(buyPrice);
            updateValue(buyPrice);
        }

        public void updateValue(double currentPrice) {
            currentValue.set(amount.get() * currentPrice);
            profitLoss.set(currentValue.get() - (amount.get() * avgBuyPrice.get()));
        }

        // Add property getters
        public StringProperty symbolProperty() {
            return symbol;
        }

        public DoubleProperty amountProperty() {
            return amount;
        }

        public DoubleProperty avgBuyPriceProperty() {
            return avgBuyPrice;
        }

        public DoubleProperty currentValueProperty() {
            return currentValue;
        }

        public DoubleProperty profitLossProperty() {
            return profitLoss;
        }
    }

    public void addPosition(String symbol, double amount, double price) {
        positions.add(new Position(symbol, amount, price));
        updateTotalValue();
    }

    public void updatePositions(Map<String, Double> currentPrices) {
        positions.forEach(pos -> pos.updateValue(currentPrices.getOrDefault(pos.symbol.get(), 0.0)));
        updateTotalValue();
    }

    private void updateTotalValue() {
        double total = positions.stream()
                .mapToDouble(pos -> pos.currentValue.get())
                .sum();
        totalValue.set(total);
    }

    // Add property getters
    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty totalValueProperty() {
        return totalValue;
    }

    public ObservableList<Position> getPositions() {
        return positions;
    }
}
