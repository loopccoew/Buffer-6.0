package utils;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadCoinIcons {
    private static final String[][] COIN_ICONS = {
            { "BTC", "https://assets.coingecko.com/coins/images/1/small/bitcoin.png" },
            { "ETH", "https://assets.coingecko.com/coins/images/279/small/ethereum.png" },
            { "SOL", "https://assets.coingecko.com/coins/images/4128/small/solana.png" },
            { "BNB", "https://assets.coingecko.com/coins/images/825/small/bnb-icon2_2x.png" },
            { "XRP", "https://assets.coingecko.com/coins/images/44/small/xrp-symbol-white-128.png" },
            { "ADA", "https://assets.coingecko.com/coins/images/975/small/cardano.png" },
            { "DOGE", "https://assets.coingecko.com/coins/images/5/small/dogecoin.png" },
            { "MATIC", "https://assets.coingecko.com/coins/images/4713/small/matic-token-icon.png" },
            { "DOT", "https://assets.coingecko.com/coins/images/12171/small/polkadot.png" },
            { "AVAX", "https://assets.coingecko.com/coins/images/12559/small/coin-round-red.png" }
    };

    public static void main(String[] args) {
        String baseDir = "c:\\Users\\Asus\\Desktop\\CryptoProject\\src\\resources\\images\\coins";
        new File(baseDir).mkdirs();

        for (String[] coin : COIN_ICONS) {
            try {
                String fileName = coin[0].toLowerCase() + ".png";
                String filePath = baseDir + "\\" + fileName;
                URL url = new URL(coin[1]);

                try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                        FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    System.out.println("Downloaded: " + fileName);
                }
            } catch (Exception e) {
                System.err.println("Error downloading " + coin[0] + ": " + e.getMessage());
            }
        }
    }
}
