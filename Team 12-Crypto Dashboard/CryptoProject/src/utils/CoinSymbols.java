package utils;

import javafx.scene.image.Image;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class CoinSymbols {
    private static final Map<String, String> SYMBOL_URLS = new HashMap<>() {
        {
            put("BTC", "https://s2.coinmarketcap.com/static/img/coins/64x64/1.png");
            put("ETH", "https://s2.coinmarketcap.com/static/img/coins/64x64/1027.png");
            put("SOL", "https://s2.coinmarketcap.com/static/img/coins/64x64/5426.png");
            put("BNB", "https://s2.coinmarketcap.com/static/img/coins/64x64/1839.png");
            put("XRP", "https://s2.coinmarketcap.com/static/img/coins/64x64/52.png");
            put("ADA", "https://s2.coinmarketcap.com/static/img/coins/64x64/2010.png");
            // Add more symbols as needed
        }
    };

    private static final ConcurrentHashMap<String, Image> imageCache = new ConcurrentHashMap<>();

    public static void downloadAllSymbols() {
        File dir = new File("src/resources/images/coins");
        dir.mkdirs();

        SYMBOL_URLS.forEach((symbol, url) -> {
            try {
                String filename = String.format("src/resources/images/coins/%s.png", symbol.toLowerCase());
                URL website = new URL(url);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(filename);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                System.out.println("Downloaded " + symbol);
            } catch (Exception e) {
                System.err.println("Error downloading " + symbol + ": " + e.getMessage());
            }
        });
    }

    public static Image getSymbol(String symbol) {
        return imageCache.computeIfAbsent(symbol.toUpperCase(), key -> {
            try {
                String localPath = String.format("/resources/images/coins/%s.png", key.toLowerCase());
                Image img = new Image(CoinSymbols.class.getResourceAsStream(localPath));
                if (img.isError()) {
                    String url = SYMBOL_URLS.getOrDefault(key,
                            "https://ui-avatars.com/api/?name=" + key + "&size=64&background=random");
                    return new Image(url, true);
                }
                return img;
            } catch (Exception e) {
                return new Image(CoinSymbols.class.getResourceAsStream("/resources/images/default-crypto.png"));
            }
        });
    }
}
