package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
//
//
//public class Main {
//    public static int aqi;
//    public static void main(String[] args) {
//        new Main().getAQI();
//    }
//
//    public void getAQI(){
//        String apiUrl = "https://api.waqi.info/feed/newyork/?token=69bb917284373b34c2099442b1ea2fa004f3a2cb";
//        try {
//            aqi = AirQualityIndex.getAqiOfCity(apiUrl);
//            System.out.println("AQI of : " + aqi);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    };
//
//}



import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Main extends JFrame {
    public static int aqi;
    private final JLabel resultLabel;
    private final JTextField city1Field, city2Field;

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

        JButton compareButton = new JButton("Compare");
        compareButton.setBounds(120, 110, 100, 30);
        compareButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compareCities();
            }
        });
        add(compareButton);

        resultLabel = new JLabel();
        resultLabel.setBounds(50, 150, 300, 30);
        add(resultLabel);

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
    public static void main(String[] args) {
        new Main();
    }
}


