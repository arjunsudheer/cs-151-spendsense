package model;

public class Transaction {
    private static int counter = 0;
    private final int id;
    private final TransactionSpec spec;

    public Transaction(TransactionSpec spec) {
        this.id = generateValidID();
        this.spec = spec;
    }

    public int getID() {
        return id;
    }

    public TransactionSpec getSpec() {
        // Return a defensive copy of the specification
        return new TransactionSpec(
                spec.getAmount(),
                spec.getDate(),
                spec.getDescription(),
                spec.getProperties());
    }

    private int generateValidID() {
        // Generate sequentially unique IDs for each transaction
        return counter++;
    }

}
