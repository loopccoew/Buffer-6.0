package auction;

import java.util.HashMap;

public class CurrencyConverter {
    private static final HashMap<String, Double> rates = new HashMap<>();

    static {
        rates.put("USD", 1.0);
        rates.put("INR", 0.012);  // 1 INR = 0.012 USD
        rates.put("EUR", 1.1);    // 1 EUR = 1.1 USD
    }

    public static double convertToUSD(double amount, String currency) {
        return amount * rates.getOrDefault(currency, 1.0);
    }

    public static double convertFromUSD(double amountUSD, String toCurrency) {
        double rate = rates.getOrDefault(toCurrency, 1.0);
        return amountUSD / rate;
    }
}
