package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import model.BudgetManager;
import model.SpendingCategory;
import notification.confirm.ConfirmNotifier;
import notification.confirm.PopupConfirmNotifier;
import notification.info.PopupInfoNotifier;
import notification.info.SliderInfoNotifier;
import notification.input.InputNotifier;
import notification.input.PopupInputNotifier;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

public class CategoryPanel extends VBox {
    private ListView<SpendingCategory> categoryListView;
    private BudgetManager budgetManager;
    private InputNotifier inputNotifier;
    private ConfirmNotifier confirmNotifier;

    private final SpendingCategory totalCategoryMock;

    public CategoryPanel(Consumer<SpendingCategory> onCategorySelected) {
        this.inputNotifier = new PopupInputNotifier();
        this.confirmNotifier = new PopupConfirmNotifier();

        // Mock category for Total
        totalCategoryMock = new SpendingCategory.Builder()
                .name("Total")
                .description("Dashboard")
                .build();

        this.setPadding(new Insets(10));
        this.setPrefWidth(250);
        this.setSpacing(10);

        Label catLabel = new Label("Spending Categories");
        catLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        categoryListView = new ListView<>();
        categoryListView.setCellFactory(param -> new ListCell<SpendingCategory>() {
            @Override
            protected void updateItem(SpendingCategory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item == totalCategoryMock) {
                        setText("⭐ Total Dashboard");
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setText(item.getName() + " (Limit: $" + item.getMonthlySpendingLimit() + ")");
                        setStyle("");
                    }
                }
            }
        });

        categoryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (onCategorySelected != null) {
                onCategorySelected.accept(newVal);
            }
        });

        HBox btnBox = new HBox(5);
        Button addCatBtn = new Button("Add");
        addCatBtn.setOnAction(e -> addCategory());
        Button rmCatBtn = new Button("Remove");
        rmCatBtn.setOnAction(e -> removeCategory());
        btnBox.getChildren().addAll(addCatBtn, rmCatBtn);

        this.getChildren().addAll(catLabel, categoryListView, btnBox);
    }

    public void setBudgetManager(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
        updateList();
    }

    public void updateList() {
        if (budgetManager == null)
            return;

        SpendingCategory selected = categoryListView.getSelectionModel().getSelectedItem();
        String selectedName = selected != null ? selected.getName() : null;

        ObservableList<SpendingCategory> categories = FXCollections.observableArrayList();
        categories.add(totalCategoryMock);
        categories.addAll(budgetManager.getCategories());

        categoryListView.setItems(categories);

        if (selectedName != null) {
            for (SpendingCategory cat : categories) {
                if (cat.getName().equals(selectedName)) {
                    categoryListView.getSelectionModel().select(cat);
                    break;
                }
            }
        }
    }

    private void addCategory() {
        Optional<Pair<String, String>> result = inputNotifier.pushInputPrompt(
                "Add Category", "Enter Category Details", "Name", "Limit");

        result.ifPresent(pair -> {
            try {
                String categoryName = pair.getKey().trim();
                BigDecimal categoryLimit = new BigDecimal(pair.getValue().trim());

                if (categoryLimit.compareTo(BigDecimal.ZERO) < 0) {
                    PopupInfoNotifier.getInstance().pushNotification("Category limit cannot be negative.");
                    return;
                }

                if (budgetManager.wouldExceedOverallBudgetWithCategoryLimit(categoryLimit)) {
                    boolean continueAnyway = confirmNotifier.pushPrompt(
                            "This category limit makes total category limits exceed your overall budget. Continue anyway?"
                    );

                    if (!continueAnyway) {
                        return;
                    }
                }

                budgetManager.addSpendingCategory(categoryName, categoryLimit);
                SliderInfoNotifier.getInstance().pushNotification("Added category: " + categoryName);

            } catch (NumberFormatException e) {
                PopupInfoNotifier.getInstance().pushNotification("Invalid category limit.");
            } catch (Exception e) {
                PopupInfoNotifier.getInstance().pushNotification("Error adding category. Check your input.");
            }
        });
    }

    private void removeCategory() {
        SpendingCategory selected = categoryListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected == totalCategoryMock) {
                PopupInfoNotifier.getInstance().pushNotification("Cannot remove Total Dashboard.");
                return;
            }
            if (confirmNotifier.pushPrompt("Are you sure you want to remove " + selected.getName() + "?")) {
                budgetManager.removeSpendingCategory(selected.getName());
                SliderInfoNotifier.getInstance().pushNotification("Removed category: " + selected.getName());
            }
        }
    }

    public boolean isTotalSelected() {
        return categoryListView.getSelectionModel().getSelectedItem() == totalCategoryMock;
    }
}
