package config;

public class ApiConfig {
    public static final String COINAPI_KEY = "your-coinapi-key";

    // Add other API configurations here
    public static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3";
    public static final String BINANCE_API_URL = "https://api.binance.com/api/v3";
    public static final String COINAPI_BASE_URL = "https://rest.coinapi.io/v1";

    // Rate limiting configs
    public static final int API_RETRY_ATTEMPTS = 3;
    public static final int API_TIMEOUT_MS = 15000;
}
