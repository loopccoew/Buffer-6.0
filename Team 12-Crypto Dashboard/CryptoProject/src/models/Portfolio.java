package models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Portfolio {
    public static class Position {
        private final StringProperty symbol;
        private final DoubleProperty amount;
        private final DoubleProperty currentValue;
        private final DoubleProperty profitLoss;

        public Position(String symbol, double amount) {
            this.symbol = new SimpleStringProperty(symbol);
            this.amount = new SimpleDoubleProperty(amount);
            this.currentValue = new SimpleDoubleProperty(0);
            this.profitLoss = new SimpleDoubleProperty(0);
        }

        // Add getters and property methods
        public StringProperty symbolProperty() {
            return symbol;
        }

        public DoubleProperty amountProperty() {
            return amount;
        }

        public DoubleProperty currentValueProperty() {
            return currentValue;
        }

        public DoubleProperty profitLossProperty() {
            return profitLoss;
        }
    }

    private final ObservableList<Position> positions = FXCollections.observableArrayList();

    public ObservableList<Position> getPositions() {
        return positions;
    }
}
