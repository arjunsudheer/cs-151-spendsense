package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import model.*;
import notification.confirm.ConfirmNotifier;
import notification.confirm.PopupConfirmNotifier;
import notification.info.PopupInfoNotifier;
import notification.info.SliderInfoNotifier;
import notification.input.InputNotifier;
import notification.input.PopupInputNotifier;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import command.Command;
import command.RemoveTransactionCommand;

public class CenterPanel extends VBox {
    private Label budgetLabel;
    private Label spendingLabel;
    private TableView<Transaction> transactionTable;
    private TableView<SpendingCategory> breakdownTable;

    private BudgetManager budgetManager;
    private SpendingCategory currentCategory;
    private boolean isTotalView = false;
    private boolean wasOverBudget = false;

    private InputNotifier inputNotifier;
    private ConfirmNotifier confirmNotifier;

    private HBox txBtnBox;
    private Label tableLabel;
    private VBox mainContent;

    public CenterPanel() {
        this.inputNotifier = new PopupInputNotifier();
        this.confirmNotifier = new PopupConfirmNotifier();

        this.setPadding(new Insets(10));
        this.setSpacing(10);

        HBox summaryBox = new HBox(20);
        budgetLabel = new Label("Overall Budget: $0.00");
        spendingLabel = new Label("Current Spending: $0.00");

        budgetLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        spendingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        summaryBox.getChildren().addAll(budgetLabel, spendingLabel);

        tableLabel = new Label("Transactions:");

        transactionTable = createTransactionTable();
        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(transactionTable, Priority.ALWAYS);

        breakdownTable = createBreakdownTable();
        breakdownTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(breakdownTable, Priority.ALWAYS);
        breakdownTable.setVisible(false);
        breakdownTable.setManaged(false);

        txBtnBox = new HBox(5);
        txBtnBox.setMaxWidth(Double.MAX_VALUE);

        Button addTxBtn = new Button("Add Transaction");
        addTxBtn.setOnAction(e -> addTransaction());

        Button rmTxBtn = new Button("Remove Transaction");
        rmTxBtn.setOnAction(e -> removeTransaction());

        txBtnBox.getChildren().addAll(addTxBtn, rmTxBtn);

        mainContent = new VBox();
        mainContent.setSpacing(10);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        mainContent.getChildren().addAll(tableLabel, transactionTable, breakdownTable, txBtnBox);

        this.getChildren().addAll(summaryBox, mainContent);
    }

    private TableView<Transaction> createTransactionTable() {
        TableView<Transaction> table = new TableView<>();

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSpec().getDate().toString()));

        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSpec().getDescription()));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(
                data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getSpec().getAmount())));

        table.getColumns().addAll(java.util.Arrays.asList(dateCol, descCol, amountCol));

        table.setRowFactory(tv -> new TableRow<Transaction>() {
            @Override
            protected void updateItem(Transaction item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if (item.getSpec().isSpending()) {
                    setStyle("-fx-control-inner-background: #ffcccc;");
                } else {
                    setStyle("-fx-control-inner-background: #ccffcc;");
                }
            }
        });

        return table;
    }

    private TableView<SpendingCategory> createBreakdownTable() {
        TableView<SpendingCategory> table = new TableView<>();

        TableColumn<SpendingCategory, String> nameCol = new TableColumn<>("Category");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<SpendingCategory, String> spentCol = new TableColumn<>("Spent");
        spentCol.setCellValueFactory(data -> {
            BigDecimal spent = FinancialMetricsAggregator.calculateSpending(data.getValue().getTransactions());
            return new SimpleStringProperty(String.format("$%.2f", spent));
        });

        TableColumn<SpendingCategory, String> limitCol = new TableColumn<>("Limit");
        limitCol.setCellValueFactory(
                data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getMonthlySpendingLimit())));

        table.getColumns().addAll(java.util.Arrays.asList(nameCol, spentCol, limitCol));

        table.setRowFactory(tv -> new TableRow<SpendingCategory>() {
            @Override
            protected void updateItem(SpendingCategory item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {
                    BigDecimal spent = FinancialMetricsAggregator.calculateSpending(item.getTransactions());

                    if (spent.compareTo(item.getMonthlySpendingLimit()) > 0) {
                        setStyle("-fx-control-inner-background: #ffcccc;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        return table;
    }

    public void setBudgetManager(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    public void showDashboardPage() {
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(tableLabel, transactionTable, breakdownTable, txBtnBox);
        setViewMode(isTotalView, currentCategory);
        updateData();
    }

    public void showBudgetPage() {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(new BudgetPage(budgetManager));
    }

    public void showAnalyticsPage(YearMonth month) {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(new AnalyticsPage(budgetManager, month));
    }

    public void setViewMode(boolean isTotal, SpendingCategory category) {
        this.isTotalView = isTotal;
        this.currentCategory = category;

        if (isTotal) {
            tableLabel.setText("Category Breakdown Dashboard:");
            transactionTable.setVisible(false);
            transactionTable.setManaged(false);
            breakdownTable.setVisible(true);
            breakdownTable.setManaged(true);
            txBtnBox.setVisible(false);
            txBtnBox.setManaged(false);
        } else {
            tableLabel.setText(category != null ? "Transactions for " + category.getName() + ":" : "Transactions:");
            transactionTable.setVisible(true);
            transactionTable.setManaged(true);
            breakdownTable.setVisible(false);
            breakdownTable.setManaged(false);
            txBtnBox.setVisible(true);
            txBtnBox.setManaged(true);
        }
    }

    public void updateData() {
        if (budgetManager == null) {
            return;
        }

        budgetLabel.setText(String.format("Overall Budget: $%.2f", budgetManager.getOverallMonthlyLimit()));

        BigDecimal totalSpending = FinancialMetricsAggregator.calculateSpending(
                budgetManager.collectAllTransactions());

        spendingLabel.setText(String.format("Current Spending: $%.2f", totalSpending));

        boolean isOverBudget = totalSpending.compareTo(budgetManager.getOverallMonthlyLimit()) > 0;

        if (isOverBudget) {
            spendingLabel.setTextFill(Color.RED);

            if (!wasOverBudget) {
                SliderInfoNotifier.getInstance()
                        .pushNotification("Warning: You are over your overall monthly budget!");
            }
        } else {
            spendingLabel.setTextFill(Color.BLACK);
        }

        wasOverBudget = isOverBudget;

        if (isTotalView) {
            breakdownTable.setItems(FXCollections.observableArrayList(budgetManager.getCategories()));
        } else if (currentCategory != null) {
            transactionTable.setItems(FXCollections.observableArrayList(currentCategory.getTransactions()));
        } else {
            transactionTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void addTransaction() {
        if (currentCategory == null) {
            PopupInfoNotifier.getInstance().pushNotification("Please select a category first.");
            return;
        }

        Optional<Pair<String, String>> result = inputNotifier.pushInputPrompt(
                "Add Transaction", "Enter Transaction Details", "Amount (e.g., -50 or 100)", "Description");

        result.ifPresent(pair -> {
            try {
                BigDecimal amount = new BigDecimal(pair.getKey().trim());
                String desc = pair.getValue().trim();

                if (amount.compareTo(BigDecimal.ZERO) < 0 &&
                        budgetManager.isOverSpendingCategoryMonthlyLimit(currentCategory.getName(), amount.abs())) {
                    if (!confirmNotifier.pushPrompt("Warning: This transaction exceeds your category limit! Add anyway?")) {
                        return;
                    }
                }

                Transaction t = new Transaction(new TransactionSpec(amount, desc));
                currentCategory.addTransaction(t);
                SliderInfoNotifier.getInstance().pushNotification("Transaction added: " + desc);

            } catch (Exception e) {
                PopupInfoNotifier.getInstance().pushNotification("Error adding transaction. Check your input.");
            }
        });
    }

    private void removeTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();

        if (selected != null && currentCategory != null) {
            if (confirmNotifier.pushPrompt("Remove selected transaction?")) {
                Command command = new RemoveTransactionCommand(currentCategory, selected.getID());
                command.execute();

                SliderInfoNotifier.getInstance().pushNotification("Transaction removed.");
            }
        }
    }
}
