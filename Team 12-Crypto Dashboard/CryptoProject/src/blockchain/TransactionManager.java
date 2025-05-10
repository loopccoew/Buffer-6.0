package blockchain;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private final List<String> pendingTransactions;

    public TransactionManager() {
        this.pendingTransactions = new ArrayList<>();
    }

    public void addTransaction(String transaction) {
        pendingTransactions.add(transaction);
        System.out.println("Transaction added: " + transaction);
    }

    public List<String> getPendingTransactions() {
        return new ArrayList<>(pendingTransactions);
    }

    public void clearTransactions() {
        pendingTransactions.clear();
    }
}
