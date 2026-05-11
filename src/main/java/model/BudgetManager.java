package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetManager implements BudgetObserver {
    private BigDecimal overallMonthlyBudget;
    private Map<String, SpendingCategory> spendingCategories;

    // Optional list of observers for the BudgetManager itself, to update GUI
    private List<BudgetObserver> observers = new ArrayList<>();

    public BudgetManager(BigDecimal overallMonthlyBudget) {
        this.overallMonthlyBudget = overallMonthlyBudget;
        this.spendingCategories = new HashMap<>();
    }

    public void addObserver(BudgetObserver observer) {
        // Register an observer to receive budget updates
        this.observers.add(observer);
    }

    @Override
    public void onBudgetChanged() {
        // Notify all registered observers of budget changes
        for (BudgetObserver observer : observers) {
            observer.onBudgetChanged();
        }
    }

    public BigDecimal getOverallMonthlyLimit() {
        return overallMonthlyBudget;
    }

    public void setOverallMonthlyLimit(BigDecimal overallMonthlyBudget) {
        // Update the monthly limit and trigger a UI refresh
        this.overallMonthlyBudget = overallMonthlyBudget;
        onBudgetChanged();
    }

    public SpendingCategory getSpendingCategory(String categoryName) {
        return spendingCategories.get(categoryName);
    }

    public void addSpendingCategory(SpendingCategory category) {
        // Add category, register it for observation, and notify changes
        spendingCategories.put(category.getName(), category);
        category.addObserver(this);
        onBudgetChanged();
    }

    public void addSpendingCategory(String categoryName, BigDecimal monthlyLimit) {
        SpendingCategory category = new SpendingCategory.Builder()
                .name(categoryName)
                .monthlySpendingLimit(monthlyLimit)
                .build();
        addSpendingCategory(category);
    }

    public void removeSpendingCategory(String categoryName) {
        // Remove category from tracking and update UI
        spendingCategories.remove(categoryName);
        onBudgetChanged();
    }

    public boolean isOverSpendingCategoryMonthlyLimit(String categoryName, BigDecimal amount) {
        if (!spendingCategoryExists(categoryName)) {
            return false;
        }
        SpendingCategory category = spendingCategories.get(categoryName);
        // Check if current spending plus new amount exceeds the category's monthly limit
        BigDecimal currentSpending = FinancialMetricsAggregator.calculateSpending(category.getTransactions());
        return currentSpending.add(amount).compareTo(category.getMonthlySpendingLimit()) > 0;
    }

    private boolean spendingCategoryExists(String categoryName) {

        return spendingCategories.containsKey(categoryName);
    }

    public List<Transaction> collectAllTransactions() {
        // Aggregate transactions from all spending categories
        List<Transaction> allTransactions = new ArrayList<>();
        for (SpendingCategory category : spendingCategories.values()) {
            allTransactions.addAll(category.getTransactions());
        }
        return allTransactions;
    }

    public BigDecimal getTotalCategoryLimits() {
        BigDecimal total = BigDecimal.ZERO;

        for (SpendingCategory category : spendingCategories.values()) {
            total = total.add(category.getMonthlySpendingLimit());
        }

        return total;
    }

    public boolean wouldExceedOverallBudgetWithCategoryLimit(BigDecimal newLimit) {
        // Ensure adding a new category limit doesn't push total limits over the overall budget
        return getTotalCategoryLimits()
                .add(newLimit)
                .compareTo(overallMonthlyBudget) > 0;
    }

    public Collection<SpendingCategory> getCategories() {

        return spendingCategories.values();
    }
}