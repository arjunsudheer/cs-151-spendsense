package command;

import model.SpendingCategory;

public class RemoveTransactionCommand implements Command {

    private final SpendingCategory category;
    private final int transactionId;

    public RemoveTransactionCommand(SpendingCategory category, int transactionId) {
        this.category = category;
        this.transactionId = transactionId;
    }

    @Override
    public void execute() {
        category.removeTransaction(transactionId);
    }
}