package gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

import java.time.YearMonth;
import java.util.function.Consumer;

// This class represents the top navigation bar of the application
// It includes month navigation and page switching buttons
public class TopBar extends ToolBar {

    private YearMonth selectedMonth;
    private Label monthLabel;

    public TopBar(YearMonth currentMonth,
            Consumer<YearMonth> onMonthChanged,
            DashboardPage dashboardPage) {

        this.selectedMonth = currentMonth;

        // Exit button to close the application
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> Platform.exit());

        // Search Feature
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> dashboardPage.showSearchPage());

        // Button to open the about page
        Button aboutBtn = new Button("About");
        aboutBtn.setOnAction(e -> dashboardPage.showAboutPage());

        // Buttons to navigate between months
        Button previousMonthBtn = new Button("<");
        Button nextMonthBtn = new Button(">");

        // Displays the currently selected month
        monthLabel = new Label(selectedMonth.toString());
        monthLabel.setStyle("-fx-font-weight: bold;");

        // Go to previous month
        previousMonthBtn.setOnAction(e -> {
            selectedMonth = selectedMonth.minusMonths(1);
            monthLabel.setText(selectedMonth.toString());

            if (onMonthChanged != null) {
                onMonthChanged.accept(selectedMonth);
            }
        });

        nextMonthBtn.setOnAction(e -> {
            selectedMonth = selectedMonth.plusMonths(1);
            monthLabel.setText(selectedMonth.toString());

            if (onMonthChanged != null) {
                onMonthChanged.accept(selectedMonth);
            }
        });

        // Navigation Buttons
        Button dashboardBtn = new Button("Dashboard");
        Button analyticsBtn = new Button("Analytics");
        Button budgetBtn = new Button("Budget");

        dashboardBtn.setOnAction(e -> dashboardPage.showDashboardPage());
        analyticsBtn.setOnAction(e -> dashboardPage.showAnalyticsPage(selectedMonth));
        budgetBtn.setOnAction(e -> dashboardPage.showBudgetPage());

        // Add all UI elements to the toolbar
        this.getItems().addAll(
                new Label("Select Month:"),
                previousMonthBtn,
                monthLabel,
                nextMonthBtn,
                new Separator(),

                dashboardBtn,
                analyticsBtn,
                budgetBtn,
                searchBtn,

                new Separator(),
                aboutBtn,
                exitBtn);
    }

    public YearMonth getSelectedMonth() {
        return selectedMonth;
    }
}
