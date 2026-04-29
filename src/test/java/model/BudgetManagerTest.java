package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class BudgetManagerTest {
    private BudgetManager budgetManager;

    @BeforeEach
    public void setUp() {
        budgetManager = new BudgetManager(new BigDecimal("1000.00"));
    }

    @Test
    public void testGetOverallMonthlyLimit() {
        assertEquals(new BigDecimal("1000.00"), budgetManager.getOverallMonthlyLimit());
    }

    @Test
    public void testSetOverallMonthlyLimit() {
        budgetManager.setOverallMonthlyLimit(new BigDecimal("1200.00"));
        assertEquals(new BigDecimal("1200.00"), budgetManager.getOverallMonthlyLimit());
    }

    @Test
    public void testAddAndGetSpendingCategory() {
        budgetManager.addSpendingCategory("Food", new BigDecimal("300.00"));
        SpendingCategory category = budgetManager.getSpendingCategory("Food");
        assertNotNull(category);
        assertEquals("Food", category.getName());
        assertEquals(new BigDecimal("300.00"), category.getMonthlySpendingLimit());
    }

    @Test
    public void testRemoveSpendingCategory() {
        budgetManager.addSpendingCategory("Food", new BigDecimal("300.00"));
        budgetManager.removeSpendingCategory("Food");
        assertNull(budgetManager.getSpendingCategory("Food"));
    }

    @Test
    public void testIsOverSpendingCategoryMonthlyLimit() {
        budgetManager.addSpendingCategory("Food", new BigDecimal("100.00"));
        SpendingCategory category = budgetManager.getSpendingCategory("Food");

        // Add a spending transaction of $50
        category.addTransaction(new Transaction(new TransactionSpec(new BigDecimal("-50.00"), "Lunch")));

        // If we add $60 more, it will be 110 which is over 100 limit
        assertTrue(budgetManager.isOverSpendingCategoryMonthlyLimit("Food", new BigDecimal("60.00")));

        // If we add $40 more, it will be 90 which is not over 100 limit
        assertFalse(budgetManager.isOverSpendingCategoryMonthlyLimit("Food", new BigDecimal("40.00")));
    }
}
