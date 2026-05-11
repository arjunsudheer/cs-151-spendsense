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
    private DashboardPage dashboardPage;

    @Override
    public void start(Stage stage) {
        // Initialize manager and current view state
        dataManager = DataManager.getInstance();
        currentMonth = YearMonth.now();

        dashboardPage = new DashboardPage();
        topBar = new TopBar(currentMonth, this::onMonthChanged, dashboardPage);
        categoryPanel = new CategoryPanel(this::onCategorySelected);

        loadMonthData(currentMonth);

        // Assemble main layout
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(categoryPanel);
        root.setCenter(dashboardPage);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("SpendSense Budget Planner");
        stage.setScene(scene);
        stage.show();
    }

    private void loadMonthData(YearMonth month) {
        // Attach observer to the newly loaded month's budget
        currentBudgetManager = dataManager.getBudgetForMonth(month);
        currentBudgetManager.addObserver(this);

        categoryPanel.setBudgetManager(currentBudgetManager);
        dashboardPage.setBudgetManager(currentBudgetManager);

        dashboardPage.setViewMode(categoryPanel.isTotalSelected(), null);

        updateUI();
    }

    private void onMonthChanged(YearMonth newMonth) {
        currentMonth = newMonth;
        loadMonthData(currentMonth);
        dashboardPage.refreshCurrentPage(currentMonth);
    }

    private void onCategorySelected(SpendingCategory category) {
        dashboardPage.setViewMode(categoryPanel.isTotalSelected(), category);
        dashboardPage.updateData();
    }

    @Override
    public void onBudgetChanged() {
        // Ensure UI updates occur on the main JavaFX application thread
        Platform.runLater(this::updateUI);
    }

    private void updateUI() {
        categoryPanel.updateList();
        dashboardPage.updateData();
    }
}
