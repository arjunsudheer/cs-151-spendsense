package main;

import java.util.Collection;
import java.math.BigDecimal;

public  class FinancialMetricsAggregator {
    public static BigDecimal calculateDeposits(Collection<Transaction> transactions) {
        // TODO: Implement logic to sum all deposit transactions
        return BigDecimal.ZERO; // Placeholder return value
    }

    public static BigDecimal calculateSpending(Collection<Transaction> transactions) {
        // TODO: Implement logic to sum all spending transactions
        return BigDecimal.ZERO; // Placeholder return value
    }

    public static BigDecimal calculateNetValue(Collection<Transaction> transactions) {
        return calculateDeposits(transactions).subtract(calculateSpending(transactions));
    }
}