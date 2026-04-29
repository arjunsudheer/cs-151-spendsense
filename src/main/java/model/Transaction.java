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
        return new TransactionSpec(
                spec.getAmount(),
                spec.getDate(),
                spec.getDescription(),
                spec.getProperties());
    }

    private int generateValidID() {
        return counter++;
    }

}
