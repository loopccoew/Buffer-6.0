package arbitrage;

public class ArbitrageOpportunity {
    private String coin;
    private String buyExchange;
    private String sellExchange;
    private double buyPrice;
    private double sellPrice;
    private double profitPercentage;

    public ArbitrageOpportunity(String coin, String buyExchange, String sellExchange,
            double buyPrice, double sellPrice, double profitPercentage) {
        this.coin = coin;
        this.buyExchange = buyExchange;
        this.sellExchange = sellExchange;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.profitPercentage = profitPercentage;
    }

    public String getCoin() {
        return coin;
    }

    public String getBuyExchange() {
        return buyExchange;
    }

    public String getSellExchange() {
        return sellExchange;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getProfitPercentage() {
        return profitPercentage;
    }

    @Override
    public String toString() {
        return String.format(
                "Arbitrage Opportunity: Coin: %s, Buy from: %s at %.2f, Sell to: %s at %.2f, Profit: %.2f%%",
                coin, buyExchange, buyPrice, sellExchange, sellPrice, profitPercentage);
    }
}
