package testTransaction;
import main.Transaction;
import main.TransactionSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

public class TransactionSpecTest {
    @Test
    void testTwoArgConstructorDefaultDateToToday()
    {
        TransactionSpec spec = new TransactionSpec(new BigDecimal("20.00"), "Coffee");
        assertEquals(LocalDate.now(), spec.getDate());
    }
    @Test
    void testTwoArgConstructorDefaultPropertiesToEmpty()
    {
        TransactionSpec spec = new TransactionSpec(new BigDecimal("20.00"), "Coffee");
        assertTrue(spec.getProperties().isEmpty());
    }
    @Test
    void testGetAmount()
    {
        TransactionSpec spec = new TransactionSpec(new BigDecimal("99,99"), "Shoes");
        assertEquals(new BigDecimal("99.99"), spec.getAmount());
    }
    @Test
    void testGetDescription()
    {
        TransactionSpec spec = new TransactionSpec(new BigDecimal("10.00"), "Bus Fare");
        assertEquals("Bus Fare", spec.getDescription());
    }
    @Test
    void testGetDate()
    {
        LocalDate date = LocalDate.of(2026, 1, 15);
        TransactionSpec spec = new TransactionSpec(new BigDecimal("10.00"), date, "Lunch", new HashMap<>());
        assertEquals(date, spec.getDate());
    }
    @Test
    void testGetProperties()
    {
        Map<String, String> props = new HashMap<>();
        props.put("category", "food");
        TransactionSpec spec = new TransactionSpec(new BigDecimal("10.00"), LocalDate.now(), "Lunch", props);
        assertEquals("food", spec.get("category"));
    }
    @Test
    void testPropertiesAreImmutable()
    {
        Map<String, String> props = new HashMap<>();
        props.put("category", "food");
        TransactionSpec spec = new TransactionSpec(new BigDecimal("10.00"), LocalDate.now(), "Lunch", props);
        assertThrows(UnsupportedOperationException.class, () -> spec.getProperties().put("new", "value"));
    }
    @Test
    void testIsSpendingReturnsTrueForNegative()
    {
        TransactionSpec spec = new TransactionSpec(new BigDecimal("-50.00"), "Rent");
        assertFalse(spec.isSpending());
    }

    @Test
    void testIsSpendingRulesFalsePositive()
    {
        TransactionSpec spec = new TransactionSpec(new BigDecimal("50.00"), "Paycheck");
        assertTrue(spec.isSpending());
    }
    @Test
    void testZeroAmountThrowsException()
    {
        assertThrows(IllegalArgumentException.class, () -> new TransactionSpec(new BigDecimal("0.00"), "Nothing"));
    }

    @Test
    void testNullAmountThrowsException()
    {
        assertThrows(IllegalArgumentException.class, () -> new TransactionSpec(null, "Nothing"));
    }


}
