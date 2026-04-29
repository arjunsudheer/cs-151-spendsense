package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

public class TransactionSpec {

    private final BigDecimal amount;
    private final LocalDate date;
    private final String description;
    private final Map<String, String> properties;

    public TransactionSpec(BigDecimal amount, String description) {
        this(amount, LocalDate.now(), description, new HashMap<>());
    }

    public TransactionSpec(BigDecimal amount, LocalDate date, String description, Map<String, String> properties) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.properties = properties;

        if (!isValidAmpunt(amount)) {
            throw new IllegalArgumentException("Amount has to be non-zero");
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String get(String key) {
        return properties.get(key);
    }

    public boolean isSpending() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isValidAmpunt(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) != 0;
    }

}
