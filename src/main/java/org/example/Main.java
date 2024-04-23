package org.example;
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONObject;
import java.io.IOException;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

import static org.example.CostOfLivingComparator.calculateCost;
import static org.example.CostOfLivingFetcher.*;

public class Main extends JFrame {
    public static int aqi;
    private final JLabel resultLabel,resultLabel2;
    public JTextField city1Field, country1Field;
    public JTextField city2Field, country2Field;

    public Main() {
        setTitle("CityScale");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel city1Label = new JLabel("Enter City 1:");
        city1Label.setBounds(50, 30, 100, 30);
        add(city1Label);

        city1Field = new JTextField();
        city1Field.setBounds(150, 30, 150, 30);
        add(city1Field);

        JLabel city2Label = new JLabel("Enter City 2:");
        city2Label.setBounds(50, 70, 100, 30);
        add(city2Label);

        city2Field = new JTextField();
        city2Field.setBounds(150, 70, 150, 30);
        add(city2Field);

        JLabel country1Label = new JLabel("Enter country 1:");
        country1Label.setBounds(50, 110, 100, 30);
        add(country1Label);

        country1Field = new JTextField();
        country1Field.setBounds(150, 110, 150, 30);
        add(country1Field);

        JLabel country2Label = new JLabel("Enter country 2:");
        country2Label.setBounds(50, 150, 100, 30);
        add(country2Label);

        country2Field = new JTextField();
        country2Field.setBounds(150, 150, 150, 30);
        add(country2Field);

        JButton compareButton = new JButton("Compare");
        compareButton.setBounds(120, 190, 100, 30);
        compareButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compareCities();
                System.out.println();
                try {
                    compareCost();
                } catch (URISyntaxException | InterruptedException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(compareButton);

        resultLabel = new JLabel();
        resultLabel.setBounds(50, 210, 300, 100);
        add(resultLabel);
        resultLabel2 = new JLabel();
        resultLabel2.setBounds(50, 250, 300, 100);
        add(resultLabel2);

        setVisible(true);
    }
    private void compareCities() {
        String city1 = city1Field.getText();
        String city2 = city2Field.getText();

        int airQualityCity1 = getAirQualityIndex(city1);
        int airQualityCity2 = getAirQualityIndex(city2);

        if (airQualityCity1 < 0 || airQualityCity2 < 0) {
            resultLabel.setText("Invalid city name!");
        } else if (airQualityCity1 < airQualityCity2) {
            resultLabel.setText(city1 + " is better to live in.");
        } else if (airQualityCity1 > airQualityCity2) {
            resultLabel.setText(city2 + " is better to live in.");
        } else {
            resultLabel.setText("Both cities have the same air quality.");
        }
    }

    private int getAirQualityIndex(String city) {
        String apiUrl = "https://api.waqi.info/feed/"+city+"/?token=69bb917284373b34c2099442b1ea2fa004f3a2cb";
        try {
             aqi = AirQualityIndex.getAqiOfCity(apiUrl);
//            System.out.println("AQI of : " + aqi);
            return aqi;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void compareCost() throws URISyntaxException, IOException, InterruptedException {
        String city1 = city1Field.getText();
        String city2 = city2Field.getText();
        String country1 = country1Field.getText();
        String country2 = country2Field.getText();

        String url1 = buildUrl(country1,city1);
        String url2 = buildUrl(country2,city2);


        JSONObject city1Data = new JSONObject(CostOfLivingFetcher.fetchData(url1));
        JSONObject city2Data = new JSONObject(CostOfLivingFetcher.fetchData(url2));

        double city1Cost = calculateCost(city1Data);
        double city2Cost = calculateCost(city2Data);

        String pocketFriendlyCity = city1Cost < city2Cost ? city1Data.getString("City Name") : city2Data.getString("City Name");

        System.out.println("Estimated cost of living for " + city1Data.getString("City Name") + ": $" + city1Cost);
        System.out.println("Estimated cost of living for " + city2Data.getString("City Name") + ": $" + city2Cost);
        System.out.println("The more pocket-friendly city to live in is: " + pocketFriendlyCity);
        resultLabel2.setText("The more pocket-friendly city to live in is: " + pocketFriendlyCity);
    }
    public static void main(String[] args) {
        new Main();
    }
}


