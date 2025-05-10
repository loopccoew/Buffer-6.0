package api;

import com.google.gson.*;
import javafx.scene.chart.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets; // Add this import
import logic.CryptoData; // Change to use logic.CryptoData
import javafx.scene.chart.CategoryAxis; // Add this import
import config.ApiConfig; // Add this import

// Add these class definitions at the top of the file
class ArbitrageOpportunity {
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
}

public class ApiIntegration {
    public ApiIntegration() {
        // Download all crypto symbols on initialization
        CoinSymbols.downloadAllSymbols();
    }

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/price";
    private static final Random random = new Random();
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins";
    private static final String QUERY_PARAMS = "?vs_currency=usd" +
            "&order=market_cap_desc&per_page=250&sparkline=true&locale=en";
    private static final String COINAPI_KEY = "YOUR-API-KEY";
    private static final String COINAPI_URL = "https://rest.coinapi.io/v1";
    private static final String BACKUP_API_URL = ApiConfig.COINGECKO_API_URL
            + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&sparkline=true";
    private static final String COINAPI_BASE_URL = "https://rest.coinapi.io/v1";
    private static final String LIBRETRANSLATE_API_URL = "https://translate.argosopentech.com/translate";

    /**
     * Fetch live cryptocurrency prices from Binance API.
     *
     * @return A map where the key is the cryptocurrency symbol (e.g., BTCUSDT) and
     *         the value is the price.
     */
    public static Map<String, Double> fetchLivePrices() {
        Map<String, Double> prices = new HashMap<>();

        try {
            // Create URL connection
            URL url = URI.create(BINANCE_API_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // Parse JSON response
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String symbol = obj.get("symbol").getAsString();
                double price = obj.get("price").getAsDouble();
                prices.put(symbol, price);
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error fetching live prices from Binance API: " + e.getMessage());
        }

        return prices;
    }

    /**
     * Get the live price for a currency pair.
     */
    public static double getLivePrice(String symbol, String exchange) {
        // Mock implementation
        return 1000 + random.nextDouble() * 1000;
    }

    public List<CryptoData> fetchCryptoData() throws IOException {
        List<CryptoData> cryptoDataList = new ArrayList<>();
        URL url = URI.create(BINANCE_API_URL).toURL();

        // Create URL connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String symbol = obj.get("symbol").getAsString();

                if (!symbol.endsWith("USDT"))
                    continue;

                // Create CryptoData with just symbol and name
                CryptoData cryptoData = new CryptoData(symbol, symbol);
                cryptoData.getExchangePrices().put("Binance", obj.get("price").getAsDouble());

                // Set the price graph
                cryptoData.setPriceGraph(createMiniGraph(obj.get("price").getAsDouble()));

                cryptoDataList.add(cryptoData);
            }
        }

        return cryptoDataList;
    }

    public List<CryptoData> fetchDetailedCryptoData() throws IOException {
        List<CryptoData> cryptoList = new ArrayList<>();
        String endpoint = COINAPI_BASE_URL + "/assets";

        HttpURLConnection conn = createCoinApiConnection(endpoint);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonArray assets = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : assets) {
                JsonObject asset = element.getAsJsonObject();

                // Skip if not a cryptocurrency
                if (!asset.has("type_is_crypto") || !asset.get("type_is_crypto").getAsBoolean()) {
                    continue;
                }

                CryptoData crypto = parseCryptoAsset(asset);
                if (crypto != null) {
                    fetchExtraDetails(crypto);
                    cryptoList.add(crypto);
                }
            }
        }

        return cryptoList;
    }

    private boolean isValidCryptoAsset(JsonObject asset) {
        // Check if it's a valid crypto asset
        if (!asset.has("type_is_crypto") || !asset.get("type_is_crypto").getAsBoolean()) {
            return false;
        }

        // Get the asset ID
        String assetId = asset.get("asset_id").getAsString();

        // List of major cryptocurrencies to track
        Set<String> majorCryptos = Set.of("BTC", "ETH", "USDT", "BNB", "XRP", "ADA",
                "SOL", "DOT", "DOGE", "USDC", "AVAX", "MATIC");

        return majorCryptos.contains(assetId);
    }

    private CryptoData fetchAssetDetails(JsonObject asset) {
        try {
            String symbol = asset.get("asset_id").getAsString();
            String name = asset.get("name").getAsString();

            // Fetch current price
            String priceUrl = String.format("%s/exchangerate/%s/USD", COINAPI_BASE_URL, symbol);
            HttpURLConnection priceConn = createCoinApiConnection(priceUrl);
            JsonObject priceData = JsonParser.parseReader(
                    new BufferedReader(new InputStreamReader(priceConn.getInputStream()))).getAsJsonObject();

            double price = priceData.get("rate").getAsDouble();

            // Fetch OHLCV data for price history
            String ohlcvUrl = String.format("%s/ohlcv/%s/USD/history?period_id=1DAY&limit=7",
                    COINAPI_BASE_URL, symbol);
            HttpURLConnection ohlcvConn = createCoinApiConnection(ohlcvUrl);
            JsonArray ohlcv = JsonParser.parseReader(
                    new BufferedReader(new InputStreamReader(ohlcvConn.getInputStream()))).getAsJsonArray();

            List<Double> prices = new ArrayList<>();
            double high24h = Double.MIN_VALUE;
            double low24h = Double.MAX_VALUE;

            for (JsonElement day : ohlcv) {
                JsonObject dayData = day.getAsJsonObject();
                double closePrice = dayData.get("price_close").getAsDouble();
                prices.add(closePrice);

                // Update 24h high/low
                high24h = Math.max(high24h, dayData.get("price_high").getAsDouble());
                low24h = Math.min(low24h, dayData.get("price_low").getAsDouble());
            }

            // Calculate price change
            double priceChange = prices.size() >= 2
                    ? ((prices.get(prices.size() - 1) - prices.get(0)) / prices.get(0)) * 100
                    : 0.0;

            // Create CryptoData object
            CryptoData cryptoData = new CryptoData(symbol, name);
            cryptoData.updateDetails(price, priceChange, high24h, low24h,
                    asset.get("volume_1day_usd").getAsDouble(),
                    asset.get("url_logo").getAsString());
            cryptoData.setSevenDayPrices(prices);

            // Add exchange prices for arbitrage
            addExchangePrices(cryptoData, symbol);

            return cryptoData;
        } catch (Exception e) {
            System.err.println("Error fetching details for " + asset.get("asset_id").getAsString() +
                    ": " + e.getMessage());
            return null;
        }
    }

    private void addExchangePrices(CryptoData cryptoData, String symbol) {
        try {
            String exchangesUrl = String.format("%s/quotes/%s/USD/latest?limit=10",
                    COINAPI_BASE_URL, symbol);
            HttpURLConnection conn = createCoinApiConnection(exchangesUrl);
            JsonArray quotes = JsonParser.parseReader(
                    new BufferedReader(new InputStreamReader(conn.getInputStream()))).getAsJsonArray();

            for (JsonElement quote : quotes) {
                JsonObject quoteObj = quote.getAsJsonObject();
                String exchange = quoteObj.get("exchange_id").getAsString();
                double quotePrice = quoteObj.get("ask_price").getAsDouble();
                cryptoData.getExchangePrices().put(exchange, quotePrice);
            }
        } catch (Exception e) {
            System.err.println("Error fetching exchange prices: " + e.getMessage());
        }
    }

    private HttpURLConnection createCoinApiConnection(String urlString) throws IOException {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-CoinAPI-Key", COINAPI_KEY);
        conn.setRequestProperty("Accept", "application/json");
        return conn;
    }

    private List<CryptoData> fetchFromCoinGecko() throws IOException {
        String url = COINGECKO_API_URL + "/markets" +
                "?vs_currency=usd" +
                "&order=market_cap_desc" +
                "&per_page=100" +
                "&sparkline=true" +
                "&price_change_percentage=1h,24h,7d,30d"; // Added time periods
        List<CryptoData> cryptoDataList = new ArrayList<>();

        try {
            JsonArray coins = CoinGeckoAPI.fetchTopCoins(100);
            System.out.println("Successfully fetched " + coins.size() + " coins from CoinGecko");

            for (JsonElement coin : coins) {
                JsonObject coinObj = coin.getAsJsonObject();

                // Get the best quality image URL
                String imageUrl = "";
                if (coinObj.has("image")) {
                    JsonElement imageElement = coinObj.get("image");
                    if (imageElement.isJsonObject()) {
                        JsonObject imageObj = imageElement.getAsJsonObject();
                        imageUrl = imageObj.has("large") ? imageObj.get("large").getAsString()
                                : imageObj.has("small") ? imageObj.get("small").getAsString()
                                        : imageObj.has("thumb") ? imageObj.get("thumb").getAsString() : "";
                    } else if (imageElement.isJsonPrimitive()) {
                        imageUrl = imageElement.getAsString();
                    }
                }

                CryptoData cryptoData = parseCoinGeckoData(coinObj);
                if (cryptoData != null) {
                    // Fetch detailed data including charts
                    String coinId = CoinGeckoAPI.getIdForSymbol(cryptoData.getName());
                    JsonObject details = CoinGeckoAPI.fetchCoinData(coinId);

                    // Set high-quality image and other details
                    cryptoData.setIconUrl(imageUrl);
                    updateCryptoDetails(cryptoData, details);
                    addSimulatedExchangePrices(cryptoData);
                    cryptoDataList.add(cryptoData);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching from CoinGecko: " + e.getMessage());
        }
        return cryptoDataList;
    }

    private void updateCryptoDetails(CryptoData crypto, JsonObject details) {
        try {
            if (details.has("market_data")) {
                JsonObject marketData = details.getAsJsonObject("market_data");

                // Get price history for charts
                if (marketData.has("sparkline_7d")) {
                    JsonObject sparkline = marketData.getAsJsonObject("sparkline_7d");
                    if (sparkline.has("price")) {
                        JsonArray priceArray = sparkline.getAsJsonArray("price");
                        List<Double> prices = new ArrayList<>();
                        priceArray.forEach(price -> {
                            if (!price.isJsonNull()) {
                                prices.add(price.getAsDouble());
                            }
                        });
                        crypto.setSevenDayPrices(prices);
                    }
                }

                // Update trading volume
                if (marketData.has("total_volume")) {
                    JsonObject volume = marketData.getAsJsonObject("total_volume");
                    if (volume.has("usd")) {
                        crypto.setVolume(volume.get("usd").getAsDouble());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating details for " + crypto.getName() + ": " + e.getMessage());
        }
    }

    private void addSimulatedExchangePrices(CryptoData cryptoData) {
        double basePrice = cryptoData.priceProperty().get();
        Map<String, Double> prices = new HashMap<>();
        Random random = new Random();

        // Add realistic price variations across exchanges
        prices.put("Binance", basePrice * (1 + (random.nextDouble() * 0.02 - 0.01)));
        prices.put("Coinbase", basePrice * (1 + (random.nextDouble() * 0.02 - 0.01)));
        prices.put("Kraken", basePrice * (1 + (random.nextDouble() * 0.02 - 0.01)));
        prices.put("Huobi", basePrice * (1 + (random.nextDouble() * 0.02 - 0.01)));
        prices.put("KuCoin", basePrice * (1 + (random.nextDouble() * 0.02 - 0.01)));

        cryptoData.getExchangePrices().putAll(prices);
    }

    private CryptoData parseCoinGeckoData(JsonObject coin) {
        try {
            String symbol = coin.get("symbol").getAsString().toUpperCase();
            String name = coin.get("name").getAsString();
            double price = coin.get("current_price").getAsDouble();
            double priceChange1h = getDoubleOrDefault(coin, "price_change_percentage_1h_in_currency", 0.0);
            double priceChange24h = getDoubleOrDefault(coin, "price_change_percentage_24h", 0.0);
            double priceChange7d = getDoubleOrDefault(coin, "price_change_percentage_7d_in_currency", 0.0);
            double priceChange30d = getDoubleOrDefault(coin, "price_change_percentage_30d_in_currency", 0.0);
            double high = coin.get("high_24h").getAsDouble();
            double low = coin.get("low_24h").getAsDouble();
            double marketCap = coin.get("market_cap").getAsDouble();

            // Get all available image URLs
            String imageUrl = "";
            if (coin.has("image")) {
                JsonElement imageElement = coin.get("image");
                if (imageElement.isJsonPrimitive()) {
                    imageUrl = imageElement.getAsString();
                } else if (imageElement.isJsonObject()) {
                    JsonObject imageObj = imageElement.getAsJsonObject();
                    // Try to get the best quality image available
                    imageUrl = imageObj.has("large") ? imageObj.get("large").getAsString()
                            : imageObj.has("small") ? imageObj.get("small").getAsString()
                                    : imageObj.has("thumb") ? imageObj.get("thumb").getAsString() : "";
                }
            } else {
                imageUrl = CoinImages.getImageUrl(symbol);
            }

            CryptoData cryptoData = new CryptoData(symbol, name);
            cryptoData.updateDetails(price, priceChange1h, priceChange24h, priceChange7d, priceChange30d, high, low,
                    marketCap, imageUrl);

            // Get 7-day price history from sparkline data
            if (coin.has("sparkline_in_7d")) {
                JsonObject sparkline = coin.getAsJsonObject("sparkline_in_7d");
                if (sparkline.has("price")) {
                    List<Double> prices = new ArrayList<>();
                    JsonArray priceArray = sparkline.getAsJsonArray("price");
                    for (JsonElement price7d : priceArray) {
                        if (!price7d.isJsonNull()) {
                            prices.add(price7d.getAsDouble());
                        }
                    }
                    cryptoData.setSevenDayPrices(prices);
                }
            }

            return cryptoData;
        } catch (Exception e) {
            System.err.println("Error parsing coin data: " + e.getMessage());
            return null;
        }
    }

    private CryptoData fetchCryptoWithPrice(JsonObject asset, String priceUrl) {
        try {
            String symbol = asset.get("asset_id").getAsString();
            String name = asset.get("name").getAsString();

            // Get current price
            HttpURLConnection priceConn = createCoinApiConnection(priceUrl);
            JsonObject priceData;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(priceConn.getInputStream()))) {
                priceData = JsonParser.parseReader(reader).getAsJsonObject();
            }
            double price = priceData.get("rate").getAsDouble();

            // Create CryptoData object
            CryptoData cryptoData = new CryptoData(symbol, name);
            String imageUrl = asset.has("url_logo") ? asset.get("url_logo").getAsString() : "";

            // Add exchange prices
            Map<String, Double> exchangePrices = new HashMap<>();
            exchangePrices.put("CoinAPI", price);
            // Add slightly different prices for other exchanges to simulate arbitrage
            exchangePrices.put("Binance", price * (1 + (Math.random() - 0.5) / 50));
            exchangePrices.put("Coinbase", price * (1 + (Math.random() - 0.5) / 50));
            cryptoData.getExchangePrices().putAll(exchangePrices);

            // Update other details
            double priceChange = 0.0; // Will be updated with historical data
            cryptoData.updateDetails(price, priceChange, price * 1.05, price * 0.95,
                    price * asset.get("volume_1day_usd").getAsDouble(), imageUrl);

            return cryptoData;
        } catch (Exception e) {
            System.err.println("Error fetching price for asset: " + e.getMessage());
            return null;
        }
    }

    private void fetchHistoricalData(CryptoData crypto) {
        try {
            String historyUrl = String.format("%s/ohlcv/%s/USD/history?period_id=1DAY&limit=7&apikey=%s",
                    COINAPI_URL, crypto.getName(), COINAPI_KEY);

            HttpURLConnection conn = createCoinApiConnection(historyUrl);
            JsonArray history = JsonParser.parseReader(
                    new BufferedReader(new InputStreamReader(conn.getInputStream()))).getAsJsonArray();

            List<Double> prices = new ArrayList<>();
            for (JsonElement day : history) {
                JsonObject dayData = day.getAsJsonObject();
                prices.add(dayData.get("price_close").getAsDouble());
            }

            crypto.setSevenDayPrices(prices);

            // Calculate price change
            if (prices.size() >= 2) {
                double oldPrice = prices.get(0);
                double newPrice = prices.get(prices.size() - 1);
                double priceChange = ((newPrice - oldPrice) / oldPrice) * 100;
                crypto.updateDetails(
                        crypto.getPrice("CoinAPI"),
                        priceChange,
                        crypto.getHigh(),
                        crypto.getLow(),
                        crypto.marketCapProperty().get(),
                        crypto.getIconUrl());
            }
        } catch (Exception e) {
            System.err.println("Error fetching historical data: " + e.getMessage());
        }
    }

    private boolean isValidCrypto(String symbol) {
        // List of major cryptocurrencies to track
        Set<String> majorCryptos = Set.of("BTC", "ETH", "USDT", "BNB", "XRP", "ADA", "SOL", "DOT", "DOGE", "MATIC");
        return majorCryptos.contains(symbol);
    }

    private void fetchExtraDetails(CryptoData crypto) {
        try {
            // Fetch OHLCV data for price history
            String ohlcvUrl = String.format("%s/ohlcv/%s/USD/history?period_id=1DAY&limit=7",
                    COINAPI_URL, crypto.getName());

            HttpURLConnection conn = createCoinApiConnection(ohlcvUrl);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                JsonArray ohlcv = JsonParser.parseReader(reader).getAsJsonArray();
                List<Double> prices = new ArrayList<>();

                for (JsonElement day : ohlcv) {
                    JsonObject dayData = day.getAsJsonObject();
                    prices.add(dayData.get("price_close").getAsDouble());
                }

                crypto.setSevenDayPrices(prices);
            }

            // Respect rate limits
            Thread.sleep(100);

        } catch (Exception e) {
            System.err.println("Error fetching extra details: " + e.getMessage());
        }
    }

    private CryptoData fetchCryptoDetails(String symbol) {
        try {
            String assetUrl = COINAPI_URL + "/assets/" + symbol + "?apikey=" + COINAPI_KEY;
            HttpURLConnection conn = createCoinApiConnection(assetUrl);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                JsonObject asset = JsonParser.parseReader(reader).getAsJsonObject();

                // Get current price and 24h data
                String ohlcvUrl = String.format("%s/ohlcv/%s/USD/latest?period_id=1DAY&limit=1&apikey=%s",
                        COINAPI_URL, symbol, COINAPI_KEY);
                JsonObject ohlcv = fetchJsonResponse(ohlcvUrl);

                // Get 7-day history
                String historyUrl = String.format("%s/ohlcv/%s/USD/history?period_id=1DAY&limit=7&apikey=%s",
                        COINAPI_URL, symbol, COINAPI_KEY);
                List<Double> history = fetchPriceHistory(historyUrl);

                return createCryptoData(asset, ohlcv, history);
            }
        } catch (Exception e) {
            // Fix the string concatenation syntax
            System.err.println("Error fetching details for " + symbol + ": " + e.getMessage());
            return null;
        }
    }

    private JsonObject fetchJsonResponse(String urlString) throws IOException {
        HttpURLConnection conn = createCoinApiConnection(urlString);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private CryptoData createCryptoData(JsonObject asset, JsonObject ohlcv, List<Double> history) {
        String symbol = asset.get("asset_id").getAsString();
        String name = asset.get("name").getAsString();

        CryptoData cryptoData = new CryptoData(symbol, name);

        double currentPrice = ohlcv.get("price_close").getAsDouble();
        double high = ohlcv.get("price_high").getAsDouble();
        double low = ohlcv.get("price_low").getAsDouble();
        double volume = ohlcv.get("volume_traded").getAsDouble();
        double marketCap = currentPrice * asset.get("volume_1day_usd").getAsDouble();
        String imageUrl = asset.has("url_logo") ? asset.get("url_logo").getAsString() : "";

        cryptoData.updateDetails(currentPrice, calculatePriceChange(history), high, low, marketCap, imageUrl);
        cryptoData.setVolume(volume);
        cryptoData.setSevenDayPrices(history);
        cryptoData.setIconUrl(imageUrl);

        return cryptoData;
    }

    private CryptoData parseCoinApiData(JsonObject obj) {
        try {
            String symbol = obj.get("asset_id").getAsString();
            String name = obj.get("name").getAsString();

            CryptoData cryptoData = new CryptoData(symbol, name);

            if (obj.has("price_usd")) {
                double price = obj.get("price_usd").getAsDouble();
                cryptoData.updateDetails(
                        price,
                        obj.has("volume_1day_usd") ? obj.get("volume_1day_usd").getAsDouble() : 0.0,
                        obj.has("price_1d_high") ? obj.get("price_1d_high").getAsDouble() : price,
                        obj.has("price_1d_low") ? obj.get("price_1d_low").getAsDouble() : price,
                        obj.has("market_cap_usd") ? obj.get("market_cap_usd").getAsDouble() : 0.0,
                        obj.has("icon_url") ? obj.get("icon_url").getAsString() : "");
            }

            return cryptoData;
        } catch (Exception e) {
            System.err.println("Error parsing crypto data: " + e.getMessage());
            return null;
        }
    }

    private double fetchCurrentPrice(String url) throws IOException {
        HttpURLConnection conn = createCoinApiConnection(url);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
            return response.get("rate").getAsDouble();
        }
    }

    private List<Double> fetchPriceHistory(String url) throws IOException {
        HttpURLConnection conn = createCoinApiConnection(url);
        List<Double> history = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonArray response = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : response) {
                JsonObject dataPoint = element.getAsJsonObject();
                history.add(dataPoint.get("rate_close").getAsDouble());
            }
        } catch (Exception e) {
            System.err.println("Error fetching price history: " + e.getMessage());
        }

        return history;
    }

    private double calculatePriceChange(List<Double> history) {
        if (history.size() < 2)
            return 0.0;
        double oldPrice = history.get(0);
        double newPrice = history.get(history.size() - 1);
        return ((newPrice - oldPrice) / oldPrice) * 100;
    }

    private CryptoData parseCryptoData(JsonObject obj) {
        try {
            // Type checking before accessing fields
            if (!obj.has("symbol") || !obj.has("current_price") ||
                    obj.get("symbol").isJsonNull() || obj.get("current_price").isJsonNull()) {
                return null;
            }

            String symbol = obj.get("symbol").getAsString().toUpperCase();
            String name = obj.has("name") && !obj.get("name").isJsonNull() ? obj.get("name").getAsString() : symbol;

            // Parse numeric values
            double currentPrice = getDoubleOrDefault(obj, "current_price", 0.0);
            double priceChange = getDoubleOrDefault(obj, "price_change_percentage_24h", 0.0);
            double high24h = getDoubleOrDefault(obj, "high_24h", 0.0);
            double low24h = getDoubleOrDefault(obj, "low_24h", 0.0);
            double marketCap = getDoubleOrDefault(obj, "market_cap", 0.0);
            double volume = getDoubleOrDefault(obj, "total_volume", 0.0);

            // Handle image URLs
            String imageUrl = "";
            if (obj.has("image")) {
                JsonElement imageElement = obj.get("image");
                if (imageElement.isJsonObject()) {
                    JsonObject imageObj = imageElement.getAsJsonObject();
                    imageUrl = imageObj.has("small") ? imageObj.get("small").getAsString()
                            : imageObj.has("thumb") ? imageObj.get("thumb").getAsString() : "";
                } else if (imageElement.isJsonPrimitive()) { // Fixed syntax
                    imageUrl = imageElement.getAsString();
                }
            } else {
                imageUrl = CoinImages.getImageUrl(symbol);
            }

            CryptoData cryptoData = new CryptoData(symbol, name);
            // Use the correct updateDetails method signature
            cryptoData.updateDetails(
                    currentPrice, // price
                    0.0, // priceChange1h
                    priceChange, // priceChange24h
                    0.0, // priceChange7d
                    0.0, // priceChange30d
                    high24h, // high
                    low24h, // low
                    marketCap, // marketCap
                    imageUrl // imageUrl
            );

            // Create a list for seven day prices before using it
            List<Double> sevenDayPrices = new ArrayList<>();

            // Parse 7d sparkline data if available
            if (obj.has("sparkline_in_7d") && !obj.get("sparkline_in_7d").isJsonNull()) {
                JsonObject sparkline = obj.getAsJsonObject("sparkline_in_7d");
                if (sparkline.has("price") && !sparkline.get("price").isJsonNull()) {
                    JsonArray priceArray = sparkline.getAsJsonArray("price");
                    for (JsonElement price : priceArray) {
                        if (!price.isJsonNull()) {
                            sevenDayPrices.add(price.getAsDouble());
                        }
                    }
                }
            }

            // Set the data in the crypto object
            cryptoData.setPrice(currentPrice);
            cryptoData.setPriceGraph(createMiniGraph(currentPrice));
            cryptoData.setSevenDayPrices(sevenDayPrices);
            cryptoData.setVolume(volume);

            // Add mock exchange prices
            Map<String, Double> prices = new HashMap<>();
            prices.put("Binance", currentPrice * (1 + Math.random() * 0.02 - 0.01));
            prices.put("Coinbase", currentPrice * (1 + Math.random() * 0.02 - 0.01));
            prices.put("Kraken", currentPrice * (1 + Math.random() * 0.02 - 0.01));
            cryptoData.getExchangePrices().putAll(prices);

            return cryptoData;
        } catch (Exception e) {
            System.err.println("Error parsing crypto data: " + e.getMessage());
            return null;
        }
    }

    private double getDoubleOrDefault(JsonObject obj, String key, double defaultValue) {
        try {
            return obj.has(key) ? obj.get(key).getAsDouble() : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static LineChart<Number, Number> createMiniGraph(double baseValue) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setVisible(false);
        yAxis.setVisible(false); // Fixed missing parenthesis

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setPrefSize(120, 60);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < 10; i++) {
            series.getData().add(new XYChart.Data<>(i, baseValue + Math.random() * 100 - 50));
        }

        lineChart.getData().add(series);
        return lineChart;
    }

    /**
     * Fetch arbitrage opportunities by comparing prices across exchanges.
     *
     * @param priceMap A map where the key is the coin symbol and the value is a map
     *                 of exchange prices.
     * @return A list of arbitrage opportunities.
     */
    public static List<ArbitrageOpportunity> fetchArbitrageOpportunities(Map<String, Map<String, Double>> priceMap) {
        List<ArbitrageOpportunity> opportunities = new ArrayList<>();
        double MIN_PROFIT_THRESHOLD = 0.5; // Minimum profit threshold (0.5%)

        for (Map.Entry<String, Map<String, Double>> coinEntry : priceMap.entrySet()) {
            String coin = coinEntry.getKey();
            Map<String, Double> exchangePrices = coinEntry.getValue();

            String bestBuyExchange = null;
            String bestSellExchange = null;
            double lowestPrice = Double.MAX_VALUE;
            double highestPrice = Double.MIN_VALUE;

            // Find the best buy (lowest price) and sell (highest price) exchanges
            for (Map.Entry<String, Double> exchangeEntry : exchangePrices.entrySet()) {
                String exchange = exchangeEntry.getKey();
                double price = exchangeEntry.getValue();

                if (price < lowestPrice) {
                    lowestPrice = price;
                    bestBuyExchange = exchange;
                }

                if (price > highestPrice) {
                    highestPrice = price;
                    bestSellExchange = exchange;
                }
            }

            double profit = highestPrice - lowestPrice;
            double profitPercentage = (lowestPrice > 0) ? (profit / lowestPrice) * 100 : 0.0;

            // Add opportunities above the threshold
            if (profitPercentage >= MIN_PROFIT_THRESHOLD) {
                opportunities.add(new ArbitrageOpportunity(
                        coin, bestBuyExchange, bestSellExchange, lowestPrice, highestPrice, profitPercentage));
            }
        }

        return opportunities;
    }

    public static List<String> fetchCryptoList() {
        List<String> cryptoList = new ArrayList<>();
        try {
            String apiUrl = "https://api.coingecko.com/api/v3/coins/list";
            HttpURLConnection connection = (HttpURLConnection) URI.create(apiUrl).toURL().openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                cryptoList.add(jsonObject.get("id").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cryptoList;
    }

    // Add Google Translate method
    public static String translateText(String text, String targetLang) {
        try {
            URL url = URI.create(LIBRETRANSLATE_API_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Prepare request body
            String jsonBody = String.format(
                    "{\"q\": \"%s\", \"source\": \"en\", \"target\": \"%s\"}",
                    text.replace("\"", "\\\""),
                    targetLang);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JsonObject response = JsonParser.parseReader(br).getAsJsonObject();
                return response.get("translatedText").getAsString();
            }
        } catch (Exception e) {
            System.err.println("Translation error: " + e.getMessage());
            return text; // Return original text if translation fails
        }
    }

    private CryptoData parseCryptoAsset(JsonObject asset) {
        try {
            String symbol = asset.get("asset_id").getAsString();
            String name = asset.get("name").getAsString();
            double price = asset.has("price_usd") ? asset.get("price_usd").getAsDouble() : 0.0;

            CryptoData crypto = new CryptoData(symbol, name);
            crypto.setPrice(price);

            if (asset.has("volume_1day_usd")) {
                crypto.setVolume(asset.get("volume_1day_usd").getAsDouble());
            }

            if (asset.has("url_logo")) {
                crypto.setIconUrl(asset.get("url_logo").getAsString());
            }

            return crypto;
        } catch (Exception e) {
            System.err.println("Error parsing asset: " + e.getMessage());
            return null;
        }
    }

    private String getImageUrl(String symbol) {
        // Default image URLs for common cryptocurrencies
        Map<String, String> imageUrls = new HashMap<>();
        imageUrls.put("BTC", "https://assets.coingecko.com/coins/images/1/large/bitcoin.png");
        imageUrls.put("ETH", "https://assets.coingecko.com/coins/images/279/large/ethereum.png");
        // Add more default images as needed

        return imageUrls.getOrDefault(symbol.toUpperCase(), "");
    }

    private void addExchangePrices(CryptoData cryptoData) {
        double basePrice = cryptoData.getPrice();
        double variation = 0.02; // 2% variation
        Random random = new Random();

        Map<String, Double> prices = new HashMap<>();
        prices.put("Binance", basePrice * (1 + random.nextDouble() * variation));
        prices.put("KuCoin", basePrice * (1 + random.nextDouble() * variation));
        prices.put("Coinbase", basePrice * (1 + random.nextDouble() * variation));
        cryptoData.setExchangePrices(prices);

        // Update price history
        List<Double> priceHistory = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            priceHistory.add(basePrice * (1 + (random.nextDouble() - 0.5) * 0.1));
        }
        cryptoData.setPriceHistory(priceHistory);
    }
}
