package utils;

import javafx.scene.image.Image;
import java.util.concurrent.ConcurrentHashMap;

public class ImageCache {
    private static final ConcurrentHashMap<String, Image> cache = new ConcurrentHashMap<>();
    private static final String DEFAULT_ICON = "/resources/images/default-crypto.png";
    private static final String COIN_ICONS_PATH = "/resources/images/coins/";

    // Sample coin icons map
    private static final ConcurrentHashMap<String, String> SAMPLE_ICONS = new ConcurrentHashMap<>() {
        {
            put("BTC", "bitcoin.png");
            put("ETH", "ethereum.png");
            put("SOL", "solana.png");
            put("BNB", "binance.png");
            put("XRP", "ripple.png");
            put("ADA", "cardano.png");
            put("DOGE", "dogecoin.png");
            put("MATIC", "polygon.png");
            put("DOT", "polkadot.png");
            put("AVAX", "avalanche.png");
        }
    };

    public static Image getCryptoIcon(String symbol, String apiUrl) {
        // First try to load from local sample icons
        String localPath = COIN_ICONS_PATH + SAMPLE_ICONS.getOrDefault(symbol.toUpperCase(), "");

        try {
            // Try loading from cache first
            return cache.computeIfAbsent(symbol, key -> {
                try {
                    // Try local sample icon first
                    Image localImage = new Image(ImageCache.class.getResourceAsStream(localPath));
                    if (!localImage.isError()) {
                        return localImage;
                    }

                    // If local fails, try API URL
                    Image apiImage = new Image(apiUrl, 32, 32, true, true, true);
                    if (!apiImage.isError()) {
                        return apiImage;
                    }
                } catch (Exception e) {
                    System.err.println("Error loading icon for " + symbol);
                }
                return getDefaultIcon();
            });
        } catch (Exception e) {
            return getDefaultIcon();
        }
    }

    private static Image getDefaultIcon() {
        return cache.computeIfAbsent("default", key -> new Image(ImageCache.class.getResourceAsStream(DEFAULT_ICON)));
    }

    public static void clearCache() {
        cache.clear();
    }
}
