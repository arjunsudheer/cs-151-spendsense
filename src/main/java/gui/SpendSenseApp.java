package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import model.BudgetManager;
import model.BudgetObserver;
import model.DataManager;
import model.SpendingCategory;
import java.time.YearMonth;

public class SpendSenseApp extends Application implements BudgetObserver {
    private DataManager dataManager;
    private YearMonth currentMonth;
    private BudgetManager currentBudgetManager;

    private TopBar topBar;
    private CategoryPanel categoryPanel;
    private CenterPanel centerPanel;

    @Override
    public void start(Stage stage) {
        dataManager = DataManager.getInstance();
        currentMonth = YearMonth.now();

        topBar = new TopBar(currentMonth, this::onMonthChanged);
        categoryPanel = new CategoryPanel(this::onCategorySelected);
        centerPanel = new CenterPanel();

        loadMonthData(currentMonth);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(categoryPanel);
        root.setCenter(centerPanel);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("SpendSense Budget Planner");
        stage.setScene(scene);
        stage.show();
    }

    private void loadMonthData(YearMonth month) {
        currentBudgetManager = dataManager.getBudgetForMonth(month);
        currentBudgetManager.addObserver(this);

        categoryPanel.setBudgetManager(currentBudgetManager);
        centerPanel.setBudgetManager(currentBudgetManager);

        updateUI();
    }

    private void onMonthChanged(YearMonth newMonth) {
        currentMonth = newMonth;
        loadMonthData(currentMonth);
    }

    private void onCategorySelected(SpendingCategory category) {
        centerPanel.setViewMode(categoryPanel.isTotalSelected(), category);
        centerPanel.updateData();
    }

    @Override
    public void onBudgetChanged() {
        Platform.runLater(this::updateUI);
    }

    private void updateUI() {
        categoryPanel.updateList();
        centerPanel.updateData();
    }
}
