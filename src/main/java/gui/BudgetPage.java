package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.BudgetManager;

import java.math.BigDecimal;

public class BudgetPage extends VBox {

    public BudgetPage(BudgetManager budgetManager) {
        this.setPadding(new Insets(20));
        this.setSpacing(12);

        Label title = new Label("Edit Monthly Budget");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label instruction = new Label("Enter the overall monthly budget for the selected month:");

        TextField budgetField = new TextField();
        budgetField.setPromptText("Example: 1000.00");

        if (budgetManager != null) {
            budgetField.setText(budgetManager.getOverallMonthlyLimit().toString());
        }

        Button saveButton = new Button("Save Budget");
        Label messageLabel = new Label();

        saveButton.setOnAction(e -> {
            try {
                BigDecimal newBudget = new BigDecimal(budgetField.getText().trim());

                if (newBudget.compareTo(BigDecimal.ZERO) < 0) {
                    messageLabel.setText("Budget cannot be negative.");
                    return;
                }

                budgetManager.setOverallMonthlyLimit(newBudget);
                messageLabel.setText("Budget updated successfully.");

            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid number.");
            }
        });

        this.getChildren().addAll(title, instruction, budgetField, saveButton, messageLabel);
    }
}
