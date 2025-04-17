package arbitrage;

import javafx.beans.property.*;

public class ArbitrageOpportunity {
    private final StringProperty coin;
    private final StringProperty buyExchange;
    private final StringProperty sellExchange;
    private final DoubleProperty buyPrice;
    private final DoubleProperty sellPrice;
    private final DoubleProperty profitPercentage;
    private final StringProperty market;
    private final StringProperty pair;

    public ArbitrageOpportunity(String coin, String buyExchange, String sellExchange,
            double buyPrice, double sellPrice, double profitPercentage) {
        this.coin = new SimpleStringProperty(coin);
        this.buyExchange = new SimpleStringProperty(buyExchange);
        this.sellExchange = new SimpleStringProperty(sellExchange);
        this.buyPrice = new SimpleDoubleProperty(buyPrice);
        this.sellPrice = new SimpleDoubleProperty(sellPrice);
        this.profitPercentage = new SimpleDoubleProperty(profitPercentage);
        this.market = new SimpleStringProperty(buyExchange + " -> " + sellExchange);
        this.pair = new SimpleStringProperty(coin);
    }

    // Getters
    public String getCoin() {
        return coin.get();
    }

    public String getBuyExchange() {
        return buyExchange.get();
    }

    public String getSellExchange() {
        return sellExchange.get();
    }

    public double getBuyPrice() {
        return buyPrice.get();
    }

    public double getSellPrice() {
        return sellPrice.get();
    }

    public double getProfitPercentage() {
        return profitPercentage.get();
    }

    // Property getters for JavaFX
    public StringProperty marketProperty() {
        return market;
    }

    public StringProperty pairProperty() {
        return pair;
    }

    public DoubleProperty buyPriceProperty() {
        return buyPrice;
    }

    public DoubleProperty sellPriceProperty() {
        return sellPrice;
    }

    public DoubleProperty profitProperty() {
        return profitPercentage;
    }
}
