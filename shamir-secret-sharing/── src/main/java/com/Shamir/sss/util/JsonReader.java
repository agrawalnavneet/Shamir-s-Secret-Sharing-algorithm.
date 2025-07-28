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


public class JsonReader {
    public static class ParsedInput {
        public int n;
        public int k;
        public List<Share> shares;
    }

  
    public static ParsedInput readInput(String filePath) throws IOException {
        ParsedInput parsedInput = new ParsedInput();
        List<Share> shareList = new ArrayList<>();

        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

  
            JsonObject keys = jsonObject.getAsJsonObject("keys");
            parsedInput.n = keys.get("n").getAsInt();
            parsedInput.k = keys.get("k").getAsInt();


            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
               
                if ("keys".equals(key)) {
                    continue;
                }

             
                BigInteger x = new BigInteger(key);

               
                JsonObject shareData = entry.getValue().getAsJsonObject();
                int base = Integer.parseInt(shareData.get("base").getAsString());
                String encodedValue = shareData.get("value").getAsString();

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
