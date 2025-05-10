package api;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;
import javafx.scene.chart.*;
import javafx.beans.property.*;
import logic.CryptoData;

public class ApiIntegration {
    public static final String BINANCE_API_URL = "https://api.binance.com/api/v3";
    public static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3";
    public static final String COINAPI_KEY = "your-api-key";
    private static final String COINGECKO_MARKETS_URL = COINGECKO_API_URL + "/coins/markets";
    private static final String PARAMS = "?vs_currency=usd&order=market_cap_desc&per_page=100&sparkline=true";

    public List<CryptoData> fetchDetailedCryptoData() throws IOException {
        List<CryptoData> cryptoList = new ArrayList<>();

        try {
            URL url = new URL(COINGECKO_MARKETS_URL + PARAMS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    JsonArray coins = JsonParser.parseReader(reader).getAsJsonArray();

                    for (JsonElement element : coins) {
                        JsonObject coin = element.getAsJsonObject();
                        CryptoData crypto = new CryptoData(
                                coin.get("symbol").getAsString().toUpperCase(),
                                coin.get("name").getAsString());

                        crypto.setPrice(coin.get("current_price").getAsDouble());
                        crypto.setPriceChangePercent(coin.get("price_change_percentage_24h").getAsDouble());
                        crypto.setVolume(coin.get("total_volume").getAsDouble());
                        crypto.setIconUrl(coin.get("image").getAsString());

                        // Add simulated exchange prices
                        addExchangePrices(crypto);

                        cryptoList.add(crypto);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching crypto data: " + e.getMessage());
            throw e;
        }

        return cryptoList;
    }

    private void addExchangePrices(CryptoData crypto) {
        double basePrice = crypto.getPrice();
        double variation = 0.02; // 2% variation
        Random random = new Random();

        Map<String, Double> prices = new HashMap<>();
        prices.put("Binance", basePrice * (1 + random.nextDouble() * variation));
        prices.put("KuCoin", basePrice * (1 + random.nextDouble() * variation));
        prices.put("Coinbase", basePrice * (1 + random.nextDouble() * variation));
        crypto.setExchangePrices(prices);
    }
}
