package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class GoogleTranslateUtil {

    private static final String API_URL = "https://translation.googleapis.com/language/translate/v2";
    private static final String API_KEY = "YOUR_GOOGLE_TRANSLATE_API_KEY"; // Replace with your API key

    /**
     * Translates the given text to the specified target language.
     *
     * @param text           The text to translate.
     * @param targetLanguage The target language code (e.g., "es" for Spanish, "fr"
     *                       for French).
     * @return The translated text.
     * @throws IOException If an error occurs during the API request.
     */
    public static String translate(String text, String targetLanguage) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Build the request body
        RequestBody body = new FormBody.Builder()
                .add("q", text)
                .add("target", targetLanguage)
                .add("key", API_KEY)
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse the response JSON
            String responseBody = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            return jsonObject.getAsJsonObject("data")
                    .getAsJsonArray("translations")
                    .get(0)
                    .getAsJsonObject()
                    .get("translatedText")
                    .getAsString();
        }
    }
}
