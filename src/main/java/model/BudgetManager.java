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
        this.observers.add(observer);
    }

    @Override
    public void onBudgetChanged() {
        for (BudgetObserver observer : observers) {
            observer.onBudgetChanged();
        }
    }

    public BigDecimal getOverallMonthlyLimit() {
        return overallMonthlyBudget;
    }

    public void setOverallMonthlyLimit(BigDecimal overallMonthlyBudget) {
        this.overallMonthlyBudget = overallMonthlyBudget;
        onBudgetChanged();
    }

    public SpendingCategory getSpendingCategory(String categoryName) {
        return spendingCategories.get(categoryName);
    }

    public void addSpendingCategory(SpendingCategory category) {
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
        spendingCategories.remove(categoryName);
        onBudgetChanged();
    }

    public boolean isOverSpendingCategoryMonthlyLimit(String categoryName, BigDecimal amount) {
        if (!spendingCategoryExists(categoryName)) {
            return false;
        }
        SpendingCategory category = spendingCategories.get(categoryName);
        BigDecimal currentSpending = FinancialMetricsAggregator.calculateSpending(category.getTransactions());
        return currentSpending.add(amount).compareTo(category.getMonthlySpendingLimit()) > 0;
    }

    private boolean spendingCategoryExists(String categoryName) {
        return spendingCategories.containsKey(categoryName);
    }

    public List<Transaction> collectAllTransactions() {
        List<Transaction> allTransactions = new ArrayList<>();
        for (SpendingCategory category : spendingCategories.values()) {
            allTransactions.addAll(category.getTransactions());
        }
        return allTransactions;
    }

    public Collection<SpendingCategory> getCategories() {
        return spendingCategories.values();
    }
}