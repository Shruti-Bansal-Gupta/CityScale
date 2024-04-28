package org.example;
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONObject;
public class CostOfLivingComparator {
    static double calculateCost(JSONObject cityData) {
        double totalCost = 0.0;
        totalCost += Double.parseDouble(cityData.get("Cost of Living Month Total").toString());
        String[] categoryNames = {
                "Restaurants prices",
                "Markets prices",
                "Transportation prices",
                "Utilities Per Month prices",
        };
        for (String categoryName : categoryNames) {
            totalCost += getCostFromCategory(cityData, categoryName);
        }
        if (cityData.has("Rent Per Month prices")) {
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
                String valueString = costItem.getString("Value");

                try {
                    categoryCost += Double.parseDouble(valueString.replace(",", "")); // Remove comma temporarily
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing cost: " + valueString);
                }
            }
        }
        return categoryCost;
    }
}


