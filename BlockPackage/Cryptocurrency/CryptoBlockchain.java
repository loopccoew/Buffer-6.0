package BlockPackage.Cryptocurrency;
import BlockPackage.DataStructure.*;

import java.util.ArrayList;
import java.util.List;

public class CryptoBlockchain extends Blockchain<Transaction> {
    private final List<Transaction> pendingTransactions = new ArrayList<>();

    public CryptoBlockchain(int difficulty) {
        super(difficulty); // Calls generic Blockchain constructor (you can modify if needed)
    }

    public void addTransaction(Transaction tx) {
        pendingTransactions.add(tx);
    }

    public void addBlock() {
        if (pendingTransactions.isEmpty()) {
            System.out.println(" No transactions to add.");
            return;
        }

        Block<Transaction> newBlock = new Block<>(
            getSize(),
            String.valueOf(System.currentTimeMillis()),
            new ArrayList<>(pendingTransactions),
            getLatestBlock().getHash()
        );
        
        getChain().add(newBlock);
        pendingTransactions.clear();
        System.out.println(" Block added with transactions.");
    }
}

