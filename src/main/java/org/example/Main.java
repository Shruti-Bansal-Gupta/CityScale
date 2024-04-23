package org.example;
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.net.*;
import javax.swing.border.LineBorder;

import static org.example.CostOfLivingComparator.calculateCost;
import static org.example.CostOfLivingFetcher.*;

public class Main extends JFrame {
    public static int aqi;
    private final JTextArea resultLabel, resultLabel2;
    public JLabel label1, label2;
    public JTextField city1Field, country1Field;
    public JTextField city2Field, country2Field;

    public Main() {
        setTitle("CityScale");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 240, 255));

        Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
        Border border1 = BorderFactory.createLineBorder(Color.BLACK,3);

        JLabel label1 = new JLabel("Compare cities based on Air Quality and Cost of Living");
        label1.setForeground(Color.BLUE);
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setFont(new Font("Arial", Font.BOLD, 23));
        label1.setBounds(35, 30, 700, 30);
        add(label1);

        JLabel label2 = new JLabel("Choose on what basis you want to compare the above selected cities");
        label2.setForeground(Color.BLUE);
//        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 15));
        label2.setBounds(50, 230, 700, 30);


        JLabel city1Label = new JLabel("City 1:");
        city1Label.setFont(new Font("Arial", Font.BOLD, 17));
        city1Label.setBounds(50, 90, 70, 30);
        add(city1Label);

        city1Field = new JTextField();
        city1Field.setBounds(160, 90, 150, 30);
        add(city1Field);

        JLabel city2Label = new JLabel("City 2:");
        city2Label.setBounds(390, 90, 70, 30);
        city2Label.setFont(new Font("Arial", Font.BOLD, 17));
        add(city2Label);

        city2Field = new JTextField();
        city2Field.setBounds(500, 90, 150, 30);
        add(city2Field);

        JLabel country1Label = new JLabel("Country 1:");
        country1Label.setBounds(50, 170, 90, 30);
        country1Label.setFont(new Font("Arial", Font.BOLD, 17));
        add(country1Label);

        country1Field = new JTextField();
        country1Field.setBounds(160, 170, 150, 30);
        add(country1Field);

        JLabel country2Label = new JLabel("Country 2:");
        country2Label.setBounds(390, 170, 90, 30);
        country2Label.setFont(new Font("Arial", Font.BOLD, 17));
        add(country2Label);

        country2Field = new JTextField();
        country2Field.setBounds(500, 170, 150, 30);
        add(country2Field);

        // Radio buttons for comparison choice
        ButtonGroup comparisonChoice = new ButtonGroup();

        JRadioButton aqiCompareButton = new JRadioButton("Air Quality");
        aqiCompareButton.setBounds(50, 270, 160, 30);
        aqiCompareButton.setFont(new Font("Arial", Font.BOLD, 17));
        aqiCompareButton.setSelected(true); // Set AQI comparison as default
        comparisonChoice.add(aqiCompareButton);


        JRadioButton costCompareButton = new JRadioButton("Cost of Living");
        costCompareButton.setBounds(420, 270, 160, 30);
        costCompareButton.setFont(new Font("Arial", Font.BOLD, 17));
        comparisonChoice.add(costCompareButton);

        JButton compareButton = new JButton("Compare");
        compareButton.setBounds(320, 320, 150, 50);
        compareButton.setFont(new Font("Arial", Font.BOLD, 16));
        compareButton.setBorder(border1);

        compareButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (aqiCompareButton.isSelected()) {
                    compareCities();
                } else if (costCompareButton.isSelected()) {
                    try {
                        compareCost();
                    } catch (URISyntaxException | InterruptedException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    System.out.println("Please select a comparison criteria.");
                }
            }
        });
        add(aqiCompareButton);
        add(costCompareButton);
        add(label2);

//        add(compareButton);
        add(compareButton);

        resultLabel = new JTextArea();
        resultLabel.setBounds(50, 400, 320, 300);
        resultLabel.setBorder(border);
        add(resultLabel);

        resultLabel2 = new JTextArea();
        resultLabel2.setBounds(420, 400, 320, 300);
        resultLabel2.setBorder(border);
        add(resultLabel2);

        setVisible(true);
        setLocationRelativeTo(null);
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


