package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TranslationService {
    private static final ConcurrentHashMap<String, String> translationCache = new ConcurrentHashMap<>();
    private static final String LIBRE_TRANSLATE_URL = "https://translate.terraprint.co/translate"; // Updated URL
    private static final String GOOGLE_TRANSLATE_URL = "https://translate.googleapis.com/translate_a/single";
    private static final String LINGVA_URL = "https://lingva.pussthecat.org/api/v1";

    public static CompletableFuture<String> translate(String text, String targetLang) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (text == null || text.trim().isEmpty()) {
                    return text;
                }

                // Check cache first
                String cacheKey = text.trim() + "_" + targetLang;
                return translationCache.computeIfAbsent(cacheKey, k -> {
                    String translated = tryAllTranslationServices(text.trim(), targetLang);
                    return translated != null ? translated : text;
                });

            } catch (Exception e) {
                System.err.println("Translation error: " + e.getMessage());
                return text;
            }
        });
    }

    private static String tryAllTranslationServices(String text, String targetLang) {
        // Try each service in order until one succeeds
        String result = null;

        // Try LibreTranslate
        result = tryLibreTranslate(text, targetLang);
        if (isValidTranslation(result))
            return result;

        // Try Google Translate (free API)
        result = tryGoogleTranslate(text, targetLang);
        if (isValidTranslation(result))
            return result;

        // Try Lingva Translate
        result = tryLingvaTranslate(text, targetLang);
        if (isValidTranslation(result))
            return result;

        return text;
    }

    private static String tryLibreTranslate(String text, String targetLang) {
        try {
            URL url = new URL(LIBRE_TRANSLATE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonBody = String.format(
                    "{\"q\": \"%s\", \"source\": \"auto\", \"target\": \"%s\"}",
                    text.replace("\"", "\\\""), targetLang);

            try (var os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == 200) {
                try (var reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                    return response.get("translatedText").getAsString();
                }
            }
        } catch (Exception e) {
            System.err.println("LibreTranslate error: " + e.getMessage());
        }
        return null;
    }

    private static String tryGoogleTranslate(String text, String targetLang) {
        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = String.format("%s?client=gtx&sl=auto&tl=%s&dt=t&q=%s",
                    GOOGLE_TRANSLATE_URL, targetLang, encodedText);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (var reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JsonArray response = JsonParser.parseReader(reader).getAsJsonArray();
                JsonArray translations = response.get(0).getAsJsonArray();

                StringBuilder result = new StringBuilder();
                for (var translation : translations) {
                    if (!translation.isJsonNull() && translation.isJsonArray() &&
                            !translation.getAsJsonArray().get(0).isJsonNull()) {
                        result.append(translation.getAsJsonArray().get(0).getAsString());
                    }
                }
                return result.toString();
            }
        } catch (Exception e) {
            System.err.println("Google Translate error: " + e.getMessage());
        }
        return null;
    }

    private static String tryLingvaTranslate(String text, String targetLang) {
        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = String.format("%s/auto/%s/%s", LINGVA_URL, targetLang, encodedText);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (var reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                return response.get("translation").getAsString();
            }
        } catch (Exception e) {
            System.err.println("Lingva error: " + e.getMessage());
        }
        return null;
    }

    private static boolean isValidTranslation(String translation) {
        return translation != null && !translation.trim().isEmpty();
    }
}