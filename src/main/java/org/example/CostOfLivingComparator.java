package org.example;

// public class Comparator {

// }
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONObject;

import java.net.URISyntaxException;


import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.example.CostOfLivingFetcher.API_ENDPOINT;
import static org.example.CostOfLivingFetcher.API_HOST;



public class CostOfLivingComparator {

    public static void main(String[] args) throws Exception {
        // Replace these with actual JSON data from your API call

        String url1 = null;
        String url2 = null;
        JSONObject city1Data = new JSONObject(CostOfLivingFetcher.fetchData(url1));
        JSONObject city2Data = new JSONObject(CostOfLivingFetcher.fetchData(url2));

        double city1Cost = calculateCost(city1Data);
        double city2Cost = calculateCost(city2Data);

        String pocketFriendlyCity = city1Cost < city2Cost ? city1Data.getString("City Name") : city2Data.getString("City Name");

        System.out.println("Estimated cost of living for " + city1Data.getString("City Name") + ": $" + city1Cost);
        System.out.println("Estimated cost of living for " + city2Data.getString("City Name") + ": $" + city2Cost);
        System.out.println("The more pocket-friendly city to live in is: " + pocketFriendlyCity);
    }

    private static String buildUrl(String country, String city) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
                .setHost(API_HOST)
                .setPath(API_ENDPOINT)
                .addParameter("country", country)
                .addParameter("city", city);

        return builder.build().toString();
    }
    private static double calculateCost(JSONObject cityData) {
        double totalCost = 0.0;

        // Base cost of living (excluding rent)
        totalCost += Double.parseDouble(cityData.getString("Cost of Living Month Total"));

        // Include user-defined categories (modify as needed)
        String[] categoryNames = {
                "Restaurants prices",
                "Markets prices",
                "Transportation prices",
                "Utilities Per Month prices",
                // Add other categories here
        };

        for (String categoryName : categoryNames) {
            totalCost += getCostFromCategory(cityData, categoryName);
        }

        // Optionally include rent based on user needs
        if (cityData.has("Rent Per Month prices")) {
            // Choose specific rent cost based on user preference (city center, etc.)
            // Modify this logic to pick the desired rent cost
            JSONObject rentItem = cityData.getJSONArray("Rent Per Month prices").getJSONObject(0);
            totalCost += Double.parseDouble(rentItem.getString("Value"));
        }

        return totalCost;
    }


    private static double getCostFromCategory(JSONObject cityData, String categoryName) {
        double categoryCost = 0.0;
        if (cityData.has(categoryName)) {
            for (Object item : cityData.getJSONArray(categoryName)) {
                JSONObject costItem = (JSONObject) item;
                categoryCost += Double.parseDouble(costItem.getString("Value"));
            }
        }
        return categoryCost;
    }
}

//
//    private static String buildUrl(String country, String city) throws URISyntaxException {
//        URIBuilder builder = new URIBuilder();
//        builder.setScheme("https")
//                .setHost(API_HOST)
//                .setPath(API_ENDPOINT)
//                .addParameter("country", country)
//                .addParameter("city", city);
//
//        return builder.build().toString();
//    }
//    private static double calculateCost(JSONObject cityData) {
//        double totalCost = 0.0;
//
//        // Base cost of living (excluding rent)
//        totalCost += Double.parseDouble(cityData.get("Cost of Living Month Total").toString());
//
//        // Include user-defined categories (modify as needed)
//        String[] categoryNames = {
//                "Restaurants prices",
//                "Markets prices",
//                "Transportation prices",
//                "Utilities Per Month prices",
//                // Add other categories here
//        };
//
//        for (String categoryName : categoryNames) {
//            totalCost += getCostFromCategory(cityData, categoryName);
//        }
//
//        // Optionally include rent based on user needs
//        if (cityData.has("Rent Per Month prices")) {
//            // Choose specific rent cost based on user preference (city center, etc.)
//            // Modify this logic to pick the desired rent cost
//            JSONObject rentItem = cityData.getJSONArray("Rent Per Month prices").getJSONObject(0);
//            totalCost += Double.parseDouble(rentItem.getString("Value"));
//        }
//
//        return totalCost;
//    }
//
//
//    private static double getCostFromCategory(JSONObject cityData, String categoryName) {
//        double categoryCost = 0.0;
//        if (cityData.has(categoryName)) {
//            for (Object item : cityData.getJSONArray(categoryName)) {
//                JSONObject costItem = (JSONObject) item;
//                String valueString = costItem.getString("Value");
//
//                // Locale-aware parsing
//                try {
//                    categoryCost += Double.parseDouble(valueString.replace(",", "")); // Remove comma temporarily
//                } catch (NumberFormatException e) {
//                    // Handle potential parsing errors (optional)
//                    System.err.println("Error parsing cost: " + valueString);
//                }
//            }
//        }
//        return categoryCost;
//    }

