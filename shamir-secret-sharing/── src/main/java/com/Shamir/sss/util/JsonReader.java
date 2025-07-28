package com.shamir.sss.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shamir.sss.model.Share;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class to read n, k, and share data from a JSON file
 * with a specific format where x-coordinates are object keys
 * and y-values are base-encoded.
 */
public class JsonReader {

    // A simple container to hold the parsed data from the JSON file
    public static class ParsedInput {
        public int n;
        public int k;
        public List<Share> shares;
    }

    /**
     * Reads and parses the JSON file according to the new format.
     *
     * @param filePath The path to the JSON file.
     * @return A ParsedInput object containing n, k, and the decoded shares.
     * @throws IOException if an I/O error occurs.
     * @throws NumberFormatException if any id or decoded value is not a valid BigInteger.
     */
    public static ParsedInput readInput(String filePath) throws IOException {
        ParsedInput parsedInput = new ParsedInput();
        List<Share> shareList = new ArrayList<>();

        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // Get n and k from the "keys" object
            JsonObject keys = jsonObject.getAsJsonObject("keys");
            parsedInput.n = keys.get("n").getAsInt();
            parsedInput.k = keys.get("k").getAsInt();

            // Iterate through the rest of the object keys to find share data
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                // Skip the "keys" object itself
                if ("keys".equals(key)) {
                    continue;
                }

                // x is the key of the object (e.g., "1", "2")
                BigInteger x = new BigInteger(key);

                // y value is in a nested object with "base" and "value"
                JsonObject shareData = entry.getValue().getAsJsonObject();
                int base = Integer.parseInt(shareData.get("base").getAsString());
                String encodedValue = shareData.get("value").getAsString();

                // Decode the y value using BigInteger with the specified base
                BigInteger y = new BigInteger(encodedValue, base);

                shareList.add(new Share(x, y));
            }
            parsedInput.shares = shareList;

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Error decoding share value or ID: " + e.getMessage());
        } catch (NullPointerException e) {
            throw new IOException("Malformed JSON: Missing 'n', 'k', 'base', or 'value' field. " + e.getMessage());
        }
        return parsedInput;
    }
}