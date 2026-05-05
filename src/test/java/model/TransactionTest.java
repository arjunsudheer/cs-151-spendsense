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

public class TransactionTest {
    private TransactionSpec spec;

    @BeforeEach
    void setUp()
    {
        spec = new TransactionSpec(new BigDecimal("50.00"), "Grocery Store");
    }

    @Test
    void testConstructorSetsSpec()
    {
        Transaction t = new Transaction(spec);
        assertEquals(spec.getAmount(), t.getSpec().getAmount());
        assertEquals(spec.getDescription(),t.getSpec().getDescription());
    }

    @Test
    void testIDsAreUnique()
    {
        Transaction t1 = new Transaction(spec);
        Transaction t2 = new Transaction(spec);
        assertNotEquals(t1.getID(), t2.getID());
    }

    @Test
    void testGetSpecReturnsDefensiveCopy()
    {
        Transaction t = new Transaction(spec);
        assertNotSame(spec, t.getSpec());
    }

}
