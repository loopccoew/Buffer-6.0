package api;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class CoinGeckoAPI {
    private static final String BASE_URL = "https://api.coingecko.com/api/v3";
    private static final Map<String, String> symbolToId = new HashMap<>();

    static {
        // Initialize common symbol to ID mappings
        symbolToId.put("BTC", "bitcoin");
        symbolToId.put("ETH", "ethereum");
        symbolToId.put("SOL", "solana");
        symbolToId.put("BNB", "binancecoin");
        symbolToId.put("XRP", "ripple");
        symbolToId.put("ADA", "cardano");
        symbolToId.put("DOGE", "dogecoin");
        symbolToId.put("MATIC", "matic-network");
        symbolToId.put("DOT", "polkadot");
        symbolToId.put("AVAX", "avalanche-2");
    }

    public static JsonArray fetchTopCoins(int limit) throws IOException {
        String endpoint = String
                .format("/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=%d&sparkline=true", limit);
        return makeRequest(endpoint).getAsJsonArray();
    }

    public static JsonObject fetchCoinData(String coinId) throws IOException {
        String endpoint = "/coins/" + coinId;
        return makeRequest(endpoint).getAsJsonObject();
    }

    private static JsonObject makeRequest(String endpoint) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 429) {
            System.out.println("Rate limit hit, waiting...");
            try {
                Thread.sleep(30000);
                return makeRequest(endpoint);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    public static String getIdForSymbol(String symbol) {
        return symbolToId.getOrDefault(symbol.toUpperCase(), symbol.toLowerCase());
    }
}
