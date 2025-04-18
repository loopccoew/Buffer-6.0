package services;

import java.util.*;
import java.net.URL;
import java.nio.file.*;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

public class CoinImageService {
    private static final Map<String, String> COIN_IMAGES = new HashMap<>() {
        {
            put("BTC", "https://assets.coingecko.com/coins/images/1/large/bitcoin.png");
            put("ETH", "https://assets.coingecko.com/coins/images/279/large/ethereum.png");
            put("USDT", "https://assets.coingecko.com/coins/images/325/large/Tether.png");
            put("BNB", "https://assets.coingecko.com/coins/images/825/large/bnb-icon2_2x.png");
            put("SOL", "https://assets.coingecko.com/coins/images/4128/large/solana.png");
            put("XRP", "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png");
            put("USDC", "https://assets.coingecko.com/coins/images/6319/large/USD_Coin_icon.png");
            put("ADA", "https://assets.coingecko.com/coins/images/975/large/cardano.png");
            put("AVAX", "https://assets.coingecko.com/coins/images/12559/large/Avalanche_Circle_RedWhite_Trans.png");
            put("DOGE", "https://assets.coingecko.com/coins/images/5/large/dogecoin.png");
        }
    };

    private static final String CACHE_DIR = "src/resources/images/coins/";
    private static final Map<String, Image> imageCache = new HashMap<>();

    public static void initializeImages() {
        try {
            Files.createDirectories(Paths.get(CACHE_DIR));

            CompletableFuture.runAsync(() -> {
                COIN_IMAGES.entrySet().parallelStream().forEach(entry -> {
                    try {
                        String symbol = entry.getKey();
                        String url = entry.getValue();
                        String filename = CACHE_DIR + symbol.toLowerCase() + ".png";

                        // Download if not exists
                        if (!Files.exists(Paths.get(filename))) {
                            System.out.println("Downloading icon for " + symbol);
                            downloadImage(url, filename);
                        }

                        // Cache the image
                        Platform.runLater(() -> {
                            try {
                                Image img = new Image(Paths.get(filename).toUri().toString());
                                imageCache.put(symbol, img);
                            } catch (Exception e) {
                                System.err.println("Error loading image for " + symbol + ": " + e.getMessage());
                            }
                        });

                    } catch (Exception e) {
                        System.err.println("Error initializing image for " + entry.getKey() + ": " + e.getMessage());
                    }
                });
            });
        } catch (Exception e) {
            System.err.println("Error creating cache directory: " + e.getMessage());
        }
    }

    private static void downloadImage(String imageUrl, String destinationFile) throws Exception {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static Image getIcon(String symbol) {
        return imageCache.getOrDefault(symbol.toUpperCase(),
                new Image(CoinImageService.class.getResourceAsStream("/resources/images/default-crypto.png")));
    }
}
