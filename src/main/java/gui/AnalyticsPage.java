package gui;

import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import model.*;

import java.math.BigDecimal;
import java.time.YearMonth;

// This class represents the Analytics page in the GUI
// It extends VBox so we can stack charts vertically
public class AnalyticsPage extends VBox {
    
    // Constructor that builds the analytics view using current budget data and selected month
    public AnalyticsPage(BudgetManager budgetManager, YearMonth month) {
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        
         // Create axes for the line chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create a line chart to show spending trend
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Spending Trend");

        // Create a data series for the line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Spending");

        // Calculate total spending for the selected month using BudgetManager
        BigDecimal totalSpending = FinancialMetricsAggregator.calculateSpending(
                budgetManager.collectAllTransactions());

        // Add data point to the chart (month -> total spending)
        series.getData().add(new XYChart.Data<>(month.toString(), totalSpending.doubleValue()));
        lineChart.getData().add(series);

        // Create a pie chart to show spending breakdown by category
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Spending Breakdown for " + month);

        // Loop through each spending category
        for (SpendingCategory category : budgetManager.getCategories()) {
            BigDecimal categorySpending = FinancialMetricsAggregator.calculateSpending(
                    category.getTransactions());

            // Only include categories that have spending greater than 0
            if (categorySpending.compareTo(BigDecimal.ZERO) > 0) {
                pieChart.getData().add(
                        new PieChart.Data(category.getName(), categorySpending.doubleValue()));
            }
        }
        // Add both charts to the page layout
        this.getChildren().addAll(lineChart, pieChart);
    }
}
