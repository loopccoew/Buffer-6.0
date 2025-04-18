package services;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import logic.CryptoData;
import javafx.collections.ObservableList;

public class WebSocketService {
    private static final String COINGECKO_WS_URL = "wss://ws.coincap.io/prices?assets=bitcoin,ethereum,binancecoin,solana,ripple,cardano,dogecoin,polkadot,matic-network,avalanche-2";
    private WebSocketClient webSocketClient;
    private final Map<String, CryptoData> cryptoMap;
    private final ObservableList<CryptoData> cryptoList;

    public WebSocketService(ObservableList<CryptoData> cryptoList) {
        this.cryptoList = cryptoList;
        this.cryptoMap = new ConcurrentHashMap<>();
        setupWebSocket();
    }

    private void setupWebSocket() {
        try {
            webSocketClient = new WebSocketClient(new URI(COINGECKO_WS_URL)) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("WebSocket Connected");
                }

                @Override
                public void onMessage(String message) {
                    handlePriceUpdate(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("WebSocket Closed: " + reason);
                    // Attempt to reconnect after 5 seconds
                    new Thread(() -> {
                        try {
                            Thread.sleep(5000);
                            setupWebSocket();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("WebSocket Error: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            System.err.println("Error setting up WebSocket: " + e.getMessage());
        }
    }

    private void handlePriceUpdate(String message) {
        try {
            JsonObject priceData = JsonParser.parseString(message).getAsJsonObject();

            Platform.runLater(() -> {
                priceData.entrySet().forEach(entry -> {
                    String symbol = entry.getKey();
                    double newPrice = entry.getValue().getAsDouble();

                    // Update price for matching crypto
                    cryptoList.stream()
                            .filter(crypto -> crypto.getName().equalsIgnoreCase(symbol))
                            .findFirst()
                            .ifPresent(crypto -> {
                                double oldPrice = crypto.priceProperty().get();
                                crypto.setPrice(newPrice);

                                // Calculate price change
                                double priceChange = ((newPrice - oldPrice) / oldPrice) * 100;
                                crypto.updatePriceChange(priceChange);

                                // Update mini chart
                                crypto.addPriceToHistory(newPrice);
                            });
                });
            });
        } catch (Exception e) {
            System.err.println("Error handling price update: " + e.getMessage());
        }
    }

    public void stop() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
