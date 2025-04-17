package utils;

import java.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.HashMap;

public class CoinImages {
    private static final Map<String, String> COIN_IMAGES = new HashMap<>() {
        {
            put("BTC", "https://assets.coingecko.com/coins/images/1/large/bitcoin.png");
            put("ETH", "https://assets.coingecko.com/coins/images/279/large/ethereum.png");
            put("BNB", "https://assets.coingecko.com/coins/images/825/large/bnb-icon2_2x.png");
            put("SOL", "https://assets.coingecko.com/coins/images/4128/large/solana.png");
            put("XRP", "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png");
            put("ADA", "https://assets.coingecko.com/coins/images/975/large/cardano.png");
            put("DOGE", "https://assets.coingecko.com/coins/images/5/large/dogecoin.png");
            put("MATIC", "https://assets.coingecko.com/coins/images/4713/large/matic-token-icon.png");
            put("DOT", "https://assets.coingecko.com/coins/images/12171/large/polkadot.png");
            put("AVAX", "https://assets.coingecko.com/coins/images/12559/large/avax.png");
        }
    };

    public static String getImageUrl(String symbol) {
        return COIN_IMAGES.getOrDefault(symbol.toUpperCase(),
                "https://ui-avatars.com/api/?name=" + symbol + "&background=random&size=128");
    }

    public static void downloadIcons() {
        File dir = new File("src/resources/images/coins");
        dir.mkdirs();

        COIN_IMAGES.forEach((symbol, url) -> {
            try {
                java.net.URL imageUrl = new java.net.URL(url);
                String filePath = "src/resources/images/coins/" + symbol.toLowerCase() + ".png";

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    byte[] imageBytes = imageUrl.openStream().readAllBytes();
                    fos.write(imageBytes);
                    System.out.println("Downloaded icon for " + symbol);
                }
            } catch (Exception e) {
                System.err.println("Failed to download icon for " + symbol + ": " + e.getMessage());
            }
        });
    }
}
