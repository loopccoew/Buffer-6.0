package utils;

import org.json.JSONObject;

public class ApiIntegration {

    /**
     * Fetches the live price of a cryptocurrency from Binance API.
     *
     * @param coinSymbol The symbol of the cryptocurrency (e.g., "BTC").
     * @param exchange   The exchange name (currently unused, defaults to Binance).
     * @return The live price of the cryptocurrency, or -1 if an error occurs.
     */
    public static double getLivePrice(String coinSymbol, String exchange) {
        try {
            // Construct the Binance API URL
            String apiUrl = "https://api.binance.com/api/v3/ticker/price?symbol=" + coinSymbol + "USDT";

            // Fetch the JSON response
            String json = JsonParserUtil.readUrl(apiUrl);

            // Parse the JSON response
            JSONObject obj = new JSONObject(json);
            return obj.getDouble("price");
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 on error
        }
    }
}
