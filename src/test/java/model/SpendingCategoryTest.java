package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SpendingCategoryTest {
    private SpendingCategory category;

    @BeforeEach
    public void setUp() {
        category = new SpendingCategory.Builder()
                .name("Entertainment")
                .description("Movies and Games")
                .monthlySpendingLimit(new BigDecimal("200.00"))
                .build();
    }

    @Test
    public void testBuilder() {
        assertEquals("Entertainment", category.getName());
        assertEquals("Movies and Games", category.getDescription());
        assertEquals(new BigDecimal("200.00"), category.getMonthlySpendingLimit());
    }

    @Test
    public void testAddAndGetTransactions() {
        Transaction t1 = new Transaction(new TransactionSpec(new BigDecimal("-20.00"), "Movie Ticket"));
        assertTrue(category.addTransaction(t1));

        List<Transaction> transactions = category.getTransactions();
        assertEquals(1, transactions.size());
        assertEquals(t1, transactions.get(0));
    }

    @Test
    public void testRemoveTransaction() {
        Transaction t1 = new Transaction(new TransactionSpec(new BigDecimal("-20.00"), "Movie Ticket"));
        category.addTransaction(t1);
        int id = t1.getID();

        Transaction removed = category.removeTransaction(id);
        assertNotNull(removed);
        assertEquals(id, removed.getID());
        assertEquals(0, category.getTransactions().size());
    }

    @Test
    public void testFilterByAmount() {
        category.addTransaction(new Transaction(new TransactionSpec(new BigDecimal("-10.00"), "Snacks")));
        category.addTransaction(new Transaction(new TransactionSpec(new BigDecimal("-50.00"), "Game")));
        category.addTransaction(new Transaction(new TransactionSpec(new BigDecimal("-100.00"), "Concert")));

        List<Transaction> filtered = category.filterByAmount(new BigDecimal("-60.00"), new BigDecimal("-20.00"));
        assertEquals(1, filtered.size());
        assertEquals("Game", filtered.get(0).getSpec().getDescription());
    }
}
