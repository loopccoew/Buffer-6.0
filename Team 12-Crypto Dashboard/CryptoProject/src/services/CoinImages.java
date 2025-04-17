package services;

import java.util.*;
import java.net.URL;
import java.nio.file.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

public class CoinImages {
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
            // Add more coins as needed
        }
    };

    private static final String CACHE_DIR = "src/resources/images/coins/";
    private static final Map<String, Image> imageCache = new HashMap<>();

    public static void initializeImages() {
        try {
            // Create cache directory
            Files.createDirectories(Paths.get(CACHE_DIR));

            // Download all images in parallel
            COIN_IMAGES.entrySet().parallelStream().forEach(entry -> {
                try {
                    String symbol = entry.getKey();
                    String url = entry.getValue();
                    String filename = CACHE_DIR + symbol.toLowerCase() + ".png";

                    // Download if not exists
                    if (!Files.exists(Paths.get(filename))) {
                        downloadImage(url, filename);
                    }

                    // Cache the image
                    Image img = new Image(Paths.get(filename).toUri().toString());
                    imageCache.put(symbol, img);

                } catch (Exception e) {
                    System.err.println("Error initializing image for " + entry.getKey() + ": " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error creating cache directory: " + e.getMessage());
        }
    }

    public static ImageView getSymbolImage(String symbol) {
        try {
            Image image = imageCache.get(symbol.toUpperCase());
            if (image == null) {
                image = new Image(Paths.get(CACHE_DIR + "default-crypto.png").toUri().toString());
            }

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(32);
            imageView.setFitWidth(32);
            imageView.setPreserveRatio(true);

            // Add hover effect
            imageView.setOnMouseEntered(e -> {
                imageView.setScaleX(1.2);
                imageView.setScaleY(1.2);
            });

            imageView.setOnMouseExited(e -> {
                imageView.setScaleX(1.0);
                imageView.setScaleY(1.0);
            });

            return imageView;
        } catch (Exception e) {
            System.err.println("Error getting image for " + symbol + ": " + e.getMessage());
            return new ImageView(new Image(Paths.get(CACHE_DIR + "default-crypto.png").toUri().toString()));
        }
    }

    private static void downloadImage(String imageUrl, String destinationFile) throws Exception {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
