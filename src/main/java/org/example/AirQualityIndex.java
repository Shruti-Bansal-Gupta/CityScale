package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class AirQualityIndex {
    public static int getAqiOfCity(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        InputStream inputStream = url.openStream();
        Scanner scanner = new Scanner(inputStream);
        StringBuilder jsonContent = new StringBuilder();
        while (scanner.hasNextLine()) {
            jsonContent.append(scanner.nextLine());
        }
        scanner.close();
        inputStream.close();

        JsonElement jsonElement = JsonParser.parseString(jsonContent.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        int aqi = data.get("aqi").getAsInt();
        return aqi;
    }



}
