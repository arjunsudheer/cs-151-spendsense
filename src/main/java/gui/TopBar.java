package gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

import notification.info.InfoNotifier;
import notification.info.PopupInfoNotifier;

import java.time.YearMonth;
import java.util.function.Consumer;

public class TopBar extends ToolBar {

    private YearMonth selectedMonth;
    private Label monthLabel;
    private InfoNotifier infoNotifier;

    public TopBar(YearMonth currentMonth,
                  Consumer<YearMonth> onMonthChanged,
                  CenterPanel centerPanel) {

        this.infoNotifier = PopupInfoNotifier.getInstance();
        this.selectedMonth = currentMonth;

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> Platform.exit());

        Button aboutBtn = new Button("About");
        aboutBtn.setOnAction(e ->
                infoNotifier.pushNotification("SpendSense Budget Planner\nCS 151 SJSU")
        );

        Button previousMonthBtn = new Button("<");
        Button nextMonthBtn = new Button(">");

        monthLabel = new Label(selectedMonth.toString());
        monthLabel.setStyle("-fx-font-weight: bold;");

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

        // 🔥 NEW NAVIGATION BUTTONS
        Button dashboardBtn = new Button("Dashboard");
        Button analyticsBtn = new Button("Analytics");
        Button budgetBtn = new Button("Budget");

        dashboardBtn.setOnAction(e -> centerPanel.showDashboardPage());
        analyticsBtn.setOnAction(e -> centerPanel.showAnalyticsPage(selectedMonth));
        budgetBtn.setOnAction(e -> centerPanel.showBudgetPage());

        this.getItems().addAll(
                new Label("Select Month:"),
                previousMonthBtn,
                monthLabel,
                nextMonthBtn,
                new Separator(),

                dashboardBtn,
                analyticsBtn,
                budgetBtn,

                new Separator(),
                aboutBtn,
                exitBtn
        );
    }

    public YearMonth getSelectedMonth() {
        return selectedMonth;
    }
}
