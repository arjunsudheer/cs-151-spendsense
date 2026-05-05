package gui;

import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import model.*;

import java.math.BigDecimal;
import java.time.YearMonth;

public class AnalyticsPage extends VBox {

    public AnalyticsPage(BudgetManager budgetManager, YearMonth month) {
        this.setPadding(new Insets(20));
        this.setSpacing(20);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Spending Trend");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Spending");

        BigDecimal totalSpending = FinancialMetricsAggregator.calculateSpending(
                budgetManager.collectAllTransactions()
        );

        series.getData().add(new XYChart.Data<>(month.toString(), totalSpending.doubleValue()));
        lineChart.getData().add(series);

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Spending Breakdown for " + month);

        for (SpendingCategory category : budgetManager.getCategories()) {
            BigDecimal categorySpending = FinancialMetricsAggregator.calculateSpending(
                    category.getTransactions()
            );

            if (categorySpending.compareTo(BigDecimal.ZERO) > 0) {
                pieChart.getData().add(
                        new PieChart.Data(category.getName(), categorySpending.doubleValue())
                );
            }
        }

        this.getChildren().addAll(lineChart, pieChart);
    }
}
