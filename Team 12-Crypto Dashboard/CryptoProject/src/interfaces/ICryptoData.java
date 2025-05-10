package interfaces;

public interface ICryptoData {
    String getName();

    double getPrice();

    double getHigh();

    double getLow();

    double getPercentChange();

    double getVolume();

    String getIconUrl();

    void setIconUrl(String url); // Added missing method

    double getMarketCap();

    void setVolume(double volume); // Added missing method
}
