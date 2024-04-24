package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CityAutocompleteExample {
    public static void fetchAndPopulateCities(JComboBox<String> comboBox) {
        List<String> cityNames = fetchCityNamesFromAPI();
        for (String cityName : cityNames) {
            comboBox.addItem(cityName);
        }
    }
    public static List<String> fetchCityNamesFromAPI() {
        List<String> cityNames = new ArrayList<>();
        try {
            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://cities-cost-of-living-and-average-prices-api.p.rapidapi.com/cities?country=india"))
                    .header("X-RapidAPI-Key", "6d36d23bc7msh66f3a8d38ffbbbfp1ee3b4jsnfc943197d821")
                    .header("X-RapidAPI-Host", "cities-cost-of-living-and-average-prices-api.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            // Send HTTP request and retrieve response
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Extract city names from the response
            String responseBody = response.body();
            JSONArray jsonArray = new JSONArray(responseBody);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cityObject = jsonArray.getJSONObject(i);
                String cityName = cityObject.getString("name");
                cityNames.add(cityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityNames;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CityAutocompleteExample::new);
    }
}

