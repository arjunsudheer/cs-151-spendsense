package main;

import java.math.BigDecimal;

public class BudgetManager {
    private BigDecimal overallMonthlyBudget;
    private Map<String, SpendingCategory> spendingCategories;

    public BudgetManager(BigDecimal overallMonthlyBudget) {
        this.overallMonthlyBudget = overallMonthlyBudget;
        this.spendingCategories = new HashMap<>();
    }

    public BigDecimal getOverallMonthlyLimit() {
        return overallMonthlyBudget;
    }

    public void setOverallMonthlyLimit(BigDecimal overallMonthlyBudget) {
        this.overallMonthlyBudget = overallMonthlyBudget;
    }

    public SpendingCategory getSpendingCategory(String categoryName) {
        return spendingCategories.get(categoryName);
    }

    public void addSpendingCategory(String categoryName, BigDecimal monthlyLimit) {
        spendingCategories.put(categoryName, new SpendingCategory(categoryName, monthlyLimit));
    }

    public void removeSpendingCategory(String categoryName) {
        spendingCategories.remove(categoryName);
    }

    public boolean isOverSpendingCategoryMonthlyLimit(String categoryName, BigDecimal amount) {
        // TODO: Implement logic to check if adding the amount would exceed the
        // category's monthly limit
        return false; // Placeholder return value
    }

    private boolean spendingCategoryExists(String categoryName) {
        return spendingCategories.containsKey(categoryName);
    }

    private List<Transaction> collectAllTransactions() {
        List<Transaction> allTransactions = new ArrayList<>();
        for (SpendingCategory category : spendingCategories.values()) {
            allTransactions.addAll(category.getTransactions());
        }
        return allTransactions;
    }
}