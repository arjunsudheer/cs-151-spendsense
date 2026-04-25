package main;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpendingCategory {
    private String name;
    private String description;
    private BigDecimal monthlySpendingLimit;
    private final List<Transaction> transactions;

    public SpendingCategory(String name, String description, BigDecimal monthlySpendingLimit) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Category name must be non-empty and alphanumeric.");
        }
        if (monthlySpendingLimit == null || monthlySpendingLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly spending limit must be non-negative.");
        }

        this.name = name;
        this.description = description == null ? "" : description;
        this.monthlySpendingLimit = monthlySpendingLimit;
        this.transactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Category name must be non-empty and alphanumeric.");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    public BigDecimal getMonthlySpendingLimit() {
        return monthlySpendingLimit;
    }

    public void setMonthlySpendingLimit(BigDecimal monthlySpendingLimit) {
        if (monthlySpendingLimit == null || monthlySpendingLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly spending limit must be non-negative.");
        }
        this.monthlySpendingLimit = monthlySpendingLimit;
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        return transactions.add(transaction);
    }

    public Transaction removeTransaction(int id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getID() == id) {
                return transactions.remove(i);
            }
        }
        return null;
    }

    public List<Transaction> search(TransactionSpec spec) {
        List<Transaction> matches = new ArrayList<>();

        if (spec == null) {
            return matches;
        }

        for (Transaction transaction : transactions) {
            if (matchesSpec(transaction, spec)) {
                matches.add(transaction);
            }
        }

        return matches;
    }

    public List<Transaction> filterByDate(LocalDate start, LocalDate end) {
        List<Transaction> matches = new ArrayList<>();

        if (start == null || end == null || start.isAfter(end)) {
            return matches;
        }

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getSpec().getDate();
            if ((transactionDate.isEqual(start) || transactionDate.isAfter(start)) &&
                (transactionDate.isEqual(end) || transactionDate.isBefore(end))) {
                matches.add(transaction);
            }
        }

        return matches;
    }

    public List<Transaction> filterByAmount(BigDecimal lowerAmount, BigDecimal upperAmount) {
        List<Transaction> matches = new ArrayList<>();

        if (lowerAmount == null || upperAmount == null || lowerAmount.compareTo(upperAmount) > 0) {
            return matches;
        }

        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getSpec().getAmount();
            if (amount.compareTo(lowerAmount) >= 0 && amount.compareTo(upperAmount) <= 0) {
                matches.add(transaction);
            }
        }

        return matches;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private boolean matchesSpec(Transaction transaction, TransactionSpec spec) {
        if (transaction == null || spec == null) {
            return false;
        }

        TransactionSpec transactionSpec = transaction.getSpec();

        boolean amountMatches = transactionSpec.getAmount().compareTo(spec.getAmount()) == 0;
        boolean dateMatches = transactionSpec.getDate().equals(spec.getDate());
        boolean descriptionMatches = transactionSpec.getDescription().equals(spec.getDescription());

        return amountMatches && dateMatches && descriptionMatches;
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z0-9 ]+");
    }
}
