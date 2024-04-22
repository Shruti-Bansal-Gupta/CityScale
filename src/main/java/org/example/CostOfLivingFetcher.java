package org.example;

import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CostOfLivingFetcher {

    private static final String API_KEY = "1ec619e953mshea7c61ed7f6c153p19b1b7jsneab72f07ca59";
    static final String API_HOST = "cities-cost-of-living-and-average-prices-api.p.rapidapi.com";
    static final String API_ENDPOINT = "/cost_of_living";

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        String country = "india";
        String city = "pune";

        String url = buildUrl(country, city);
        String responseBody = fetchData(url);

        System.out.println(responseBody);
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

    static String fetchData(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
