package gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

import notification.info.InfoNotifier;
import notification.info.PopupInfoNotifier;
import java.time.YearMonth;
import java.util.function.Consumer;

public class TopBar extends ToolBar {

    private YearMonth selectedMonth;

    private Label monthLabel;

    private InfoNotifier infoNotifier;

    public TopBar(YearMonth currentMonth, Consumer<YearMonth> onMonthChanged) {

        this.infoNotifier = PopupInfoNotifier.getInstance();

        this.selectedMonth = currentMonth;

        Button exitBtn = new Button("Exit");

        exitBtn.setOnAction(e -> Platform.exit());

        Button aboutBtn = new Button("About");

        aboutBtn.setOnAction(e -> infoNotifier.pushNotification("SpendSense Budget Planner\nCS 151 SJSU"));

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

        this.getItems().addAll(

                new Label("Select Month:"),

                previousMonthBtn,

                monthLabel,

                nextMonthBtn,

                new javafx.scene.control.Separator(),

                aboutBtn,

                exitBtn

        );

    }

    public YearMonth getSelectedMonth() {

        return selectedMonth;

    }

}