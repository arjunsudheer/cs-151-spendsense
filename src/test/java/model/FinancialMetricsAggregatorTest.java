package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FinancialMetricsAggregatorTest {
    private List<Transaction> transactions;

    @BeforeEach
    public void setUp() {
        transactions = new ArrayList<>();
        transactions.add(new Transaction(new TransactionSpec(new BigDecimal("1000.00"), "Salary")));
        transactions.add(new Transaction(new TransactionSpec(new BigDecimal("200.00"), "Bonus")));
        transactions.add(new Transaction(new TransactionSpec(new BigDecimal("-150.00"), "Groceries")));
        transactions.add(new Transaction(new TransactionSpec(new BigDecimal("-50.00"), "Gas")));
    }

    @Test
    public void testCalculateDeposits() {
        BigDecimal deposits = FinancialMetricsAggregator.calculateDeposits(transactions);
        assertEquals(new BigDecimal("1200.00"), deposits);
    }

    @Test
    public void testCalculateSpending() {
        BigDecimal spending = FinancialMetricsAggregator.calculateSpending(transactions);
        assertEquals(new BigDecimal("200.00"), spending);
    }

    @Test
    public void testCalculateNetValue() {
        BigDecimal netValue = FinancialMetricsAggregator.calculateNetValue(transactions);
        assertEquals(new BigDecimal("1000.00"), netValue);
    }
}
