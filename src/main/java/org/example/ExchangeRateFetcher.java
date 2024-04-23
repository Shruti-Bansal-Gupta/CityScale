package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ExchangeRateFetcher {

    public static double getExchangeRate() {
        try {
            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://exchange-rate-api1.p.rapidapi.com/latest?base=USD"))
                .header("X-RapidAPI-Key", "1ec619e953mshea7c61ed7f6c153p19b1b7jsneab72f07ca59")
                .header("X-RapidAPI-Host", "exchange-rate-api1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

            // Send HTTP request and retrieve response
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.body());
            double exchangeRate = jsonResponse.getJSONObject("rates").getDouble("INR");

            return exchangeRate;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return -1 if there's an error
        }
    }
    public static double convertUSDToINR(double amountUSD) {
        // Fetch current exchange rate
        double exchangeRate = getExchangeRate();

        if (exchangeRate != -1) {
            // Convert USD to INR using the exchange rate
            double amountINR = amountUSD * exchangeRate;
            return amountINR;
        } else {
            // Failed to fetch exchange rate
            System.out.println("Failed to fetch exchange rate.");
            return -1;
        }
    }

    public static void main(String[] args) {
        // Example usage:
        double rate = getExchangeRate();
        if (rate != -1) {
            System.out.println("Current exchange rate from USD to INR: " + rate);
        } else {
            System.out.println("Failed to fetch exchange rate.");
        }
    }
}
