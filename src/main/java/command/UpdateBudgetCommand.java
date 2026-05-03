package command;

import model.BudgetManager;
import java.math.BigDecimal;

public class UpdateBudgetCommand implements Command {

    private final BudgetManager budgetManager;
    private final BigDecimal newBudget;

    public UpdateBudgetCommand(BudgetManager budgetManager, BigDecimal newBudget) {
        this.budgetManager = budgetManager;
        this.newBudget = newBudget;
    }

    @Override
    public void execute() {
        budgetManager.setOverallMonthlyLimit(newBudget);
    }
}