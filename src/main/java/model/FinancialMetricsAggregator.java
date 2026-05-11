package model;

import java.util.Collection;
import java.math.BigDecimal;

public class FinancialMetricsAggregator {
    public static BigDecimal calculateDeposits(Collection<Transaction> transactions) {
        // Sum up all positive transactions
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (!t.getSpec().isSpending()) {
                sum = sum.add(t.getSpec().getAmount());
            }
        }
        return sum;
    }

    public static BigDecimal calculateSpending(Collection<Transaction> transactions) {
        // Sum up absolute values of negative transactions
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getSpec().isSpending()) {
                sum = sum.add(t.getSpec().getAmount().abs());
            }
        }
        return sum;
    }

    public static BigDecimal calculateNetValue(Collection<Transaction> transactions) {
        // Calculate difference between deposits and spending
        return calculateDeposits(transactions).subtract(calculateSpending(transactions));
    }
}