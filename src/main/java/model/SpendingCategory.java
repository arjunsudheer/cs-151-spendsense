package model;

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
    private final List<BudgetObserver> observers = new ArrayList<>();

    // Private constructor for Builder
    private SpendingCategory(Builder builder) {
        if (!isValidName(builder.name)) {
            throw new IllegalArgumentException("Category name must be non-empty and alphanumeric.");
        }
        if (builder.monthlySpendingLimit == null || builder.monthlySpendingLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly spending limit must be non-negative.");
        }

        this.name = builder.name;
        this.description = builder.description == null ? "" : builder.description;
        this.monthlySpendingLimit = builder.monthlySpendingLimit;
        this.transactions = new ArrayList<>();
    }

    public static class Builder {
        private String name;
        private String description = "";
        private BigDecimal monthlySpendingLimit = BigDecimal.ZERO;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder monthlySpendingLimit(BigDecimal limit) {
            this.monthlySpendingLimit = limit;
            return this;
        }

        public SpendingCategory build() {
            return new SpendingCategory(this);
        }
    }

    public void addObserver(BudgetObserver observer) {
        this.observers.add(observer);
    }

    private void notifyObservers() {
        for (BudgetObserver observer : observers) {
            observer.onBudgetChanged();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Category name must be non-empty and alphanumeric.");
        }
        this.name = name;
        notifyObservers();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
        notifyObservers();
    }

    public BigDecimal getMonthlySpendingLimit() {
        return monthlySpendingLimit;
    }

    public void setMonthlySpendingLimit(BigDecimal monthlySpendingLimit) {
        if (monthlySpendingLimit == null || monthlySpendingLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly spending limit must be non-negative.");
        }
        this.monthlySpendingLimit = monthlySpendingLimit;
        notifyObservers();
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        boolean added = transactions.add(transaction);
        if (added) {
            notifyObservers();
        }
        return added;
    }

    public Transaction removeTransaction(int id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getID() == id) {
                Transaction removed = transactions.remove(i);
                notifyObservers();
                return removed;
            }
        }
        return null;
    }

    public List<Transaction> search(TransactionSpec spec) {
        List<Transaction> matches = new ArrayList<>();
        if (spec == null)
            return matches;
        for (Transaction transaction : transactions) {
            if (matchesSpec(transaction, spec)) {
                matches.add(transaction);
            }
        }
        return matches;
    }

    public List<Transaction> filterByDate(LocalDate start, LocalDate end) {
        List<Transaction> matches = new ArrayList<>();
        if (start == null || end == null || start.isAfter(end))
            return matches;
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
        if (lowerAmount == null || upperAmount == null || lowerAmount.compareTo(upperAmount) > 0)
            return matches;
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
        if (transaction == null || spec == null)
            return false;
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
