package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonParserUtil {

    /**
     * Reads JSON data from a given URL.
     *
     * @param urlString The URL to fetch data from.
     * @return The JSON response as a string.
     * @throws Exception If an error occurs during the request.
     */
    public static String readUrl(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

    public static double parsePrice(String json, String crypto, String currency) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.getAsJsonObject(crypto).get(currency).getAsDouble();
    }
}
