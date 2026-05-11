package model;

public interface BudgetObserver {
    // Called when a budget or category property changes
    void onBudgetChanged();
}
