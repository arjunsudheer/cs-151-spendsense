package gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

import notification.info.InfoNotifier;
import notification.info.PopupInfoNotifier;
import java.time.YearMonth;
import java.util.function.Consumer;

public class TopBar extends ToolBar {
    private ComboBox<YearMonth> monthSelector;
    private InfoNotifier infoNotifier;

    public TopBar(YearMonth currentMonth, Consumer<YearMonth> onMonthChanged) {
        this.infoNotifier = PopupInfoNotifier.getInstance();

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> Platform.exit());

        Button aboutBtn = new Button("About");
        aboutBtn.setOnAction(e -> infoNotifier.pushNotification("SpendSense Budget Planner\nCS 151 SJSU"));

        this.getItems().add(new Label("Select Month:"));

        monthSelector = new ComboBox<>();
        for (int i = -5; i <= 5; i++) {
            monthSelector.getItems().add(YearMonth.now().plusMonths(i));
        }
        monthSelector.setValue(currentMonth);
        monthSelector.setOnAction(e -> {
            if (onMonthChanged != null) {
                onMonthChanged.accept(monthSelector.getValue());
            }
        });

        this.getItems().addAll(monthSelector, new javafx.scene.control.Separator(), aboutBtn, exitBtn);
    }

    public YearMonth getSelectedMonth() {
        return monthSelector.getValue();
    }
}
