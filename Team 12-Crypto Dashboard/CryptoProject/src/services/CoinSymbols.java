package services;

import java.util.HashMap;
import java.util.Map;

public class CoinSymbols {
    private static final Map<String, String> symbols = new HashMap<>();

    public static void downloadAllSymbols() {
        // Implement symbol downloading logic
        symbols.put("BTC", "bitcoin");
        symbols.put("ETH", "ethereum");
        // Add more symbols as needed
    }

    public static String getSymbol(String name) {
        return symbols.getOrDefault(name.toUpperCase(), name.toLowerCase());
    }
}
