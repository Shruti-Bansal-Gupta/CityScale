package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class CityAQIComparison extends JFrame {
    public CityAQIComparison() {
        setTitle("City AQI Comparison");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a dataset with example data
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(50, "AQI", "City 1");
        dataset.addValue(70, "AQI", "City 2");

        // Create a bar chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "City AQI Comparison", // Title
                "Cities",              // X-axis label
                "AQI",                 // Y-axis label
                dataset);

        // Customize the chart (optional)
        // For example, you can customize the appearance of the chart here

        // Create a chart panel to display the chart
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(560, 320));

        // Add the chart panel to the frame
        add(chartPanel, BorderLayout.CENTER);

        // Display the frame
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(CityAQIComparison::new);
    }
}
