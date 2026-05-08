package gui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends VBox{
    private BudgetManager budgetManager;

    private TextField nameField;
    private TextField categoryField;
    private TextField amountField;
    private ComboBox<String> filterType;

    private TableView<Transaction> table;

    public SearchPage(BudgetManager budgetManager)
    {
        this.budgetManager = budgetManager;

        setSpacing(10);
        setPadding(new Insets(10));

        // Inputs
        nameField = new TextField();
        nameField.setPromptText("Search by description: ");

        categoryField = new TextField();
        categoryField.setPromptText("Search by category: ");

        amountField = new TextField();
        amountField.setPromptText("Search by amount: ");

        filterType = new ComboBox<>();
        filterType.getItems().addAll("Greater Than", "Less Than");

        Button button = new Button("Search");

        // TABLE
        table = new TableView<>();

        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getSpec().getAmount().toString()
            )
        );

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getSpec().getAmount().toString()
                )
        );

        table.getColumns().addAll(descCol, amountCol);
        button.setOnAction(e -> performSearch());

        getChildren().addAll(
                new Label("Transaction Search"),
                nameField,
                categoryField,
                amountField,
                filterType,
                button,
                table
        );
    }

    private void performSearch()
    {
        String name = nameField.getText().toLowerCase();
        String category = categoryField.getText().toLowerCase();
        String amountText = amountField.getText();
        String type = filterType.getValue();

        BigDecimal amount = null;

        try {
            if(!amountText.isEmpty())
            {
                amount = new BigDecimal(amountText);
            }
        } catch (Exception e) {
            return;
        }

        List<Transaction> all = budgetManager.collectAllTransactions();
        List<Transaction> results = new ArrayList<>();

        for(Transaction t: all)
        {
            TransactionSpec spec = t.getSpec();
            boolean matches = true;

            // name filter
            if(!name.isEmpty())
            {
                if(!spec.getDescription().toLowerCase().contains(name))
                {
                    matches = false;
                }
            }
            // category filter
            if(!category.isEmpty())
            {
                SpendingCategory aCategory = budgetManager.getSpendingCategory(category);

                if(aCategory == null || !aCategory.getTransactions().contains(t))
                {
                    matches = false;
                }
            }
            // amount filter
            if(amount != null && type != null)
            {
                int cmp = spec.getAmount().compareTo(amount);

                if(type.equals("Greater Than") && cmp <= 0) matches = false;
                if(type.equals("Less Than") && cmp >= 0) matches = false;
            }
            if(matches)
            {
                results.add(t);
            }
        }
        table.setItems(FXCollections.observableArrayList(results));
    }
}
