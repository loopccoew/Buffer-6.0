package blockchain;

import java.util.*;

@SuppressWarnings("unchecked")
public class Blockchain<T> {
    private final List<Block<T>> chain;
    private final int difficulty;

    // Default constructor (sets default difficulty)
    public Blockchain() {
        this.difficulty = 2;  // ✅ default difficulty
        chain = new ArrayList<>();
        List<T> genesisData = new ArrayList<>();
        genesisData.add((T) "Genesis Block");
        chain.add(new Block<>(0, "0", genesisData));
    }

    // Constructor with custom difficulty
    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        chain = new ArrayList<>();
        List<T> genesisData = new ArrayList<>();
        genesisData.add((T) "Genesis Block");
        chain.add(new Block<>(0, "0", genesisData));
    }

    public void addBlock(List<T> data) {
        Block<T> previousBlock = getLatestBlock();
        Block<T> newBlock = new Block<>(chain.size(), previousBlock.hash, data);
        chain.add(newBlock);
        System.out.println(" Block added at index " + newBlock.index);
    }

    public Block<T> getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public Block<T> getBlock(int index) {
        if (index >= 0 && index < chain.size()) {
            return chain.get(index);
        }
        System.out.println(" Invalid block index.");
        return null;
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block<T> current = chain.get(i);
            Block<T> previous = chain.get(i - 1);

            if (!current.hash.equals(current.calculateHash()))
                return false;
            if (!current.previousHash.equals(previous.hash))
                return false;
        }
        return true;
    }

    public int getSize() {
        return chain.size();
    }

    public void printChain() {
        for (Block<T> block : chain) {
            System.out.println("\n------------------------------");
            System.out.println("Index        : " + block.index);
            System.out.println("Timestamp    : " + new Date(block.timestamp));
            System.out.println("Previous Hash: " + block.previousHash);
            System.out.println("Hash         : " + block.hash);
            System.out.println("Nonce        : " + block.nonce);
            System.out.println("Data         : " + block.data);
        }
        System.out.println("------------------------------");
    }

    public void searchByItem(String item) {
        boolean found = false;
        for (Block<T> block : chain) {
            for (T dataItem : block.data) {
                if (dataItem.toString().contains(item)) {
                    found = true;
                    System.out.println("🔎 Found in Block " + block.index + ": " + block.data);
                    break;
                }
            }
        }
        if (!found)
            System.out.println(" Item not found.");
    }

    public void searchByLocation(String location) {
        boolean found = false;
        for (Block<T> block : chain) {
            for (T dataItem : block.data) {
                if (dataItem.toString().toLowerCase().contains(location.toLowerCase())) {
                    found = true;
                    System.out.println(" Found in Block " + block.index + ": " + block.data);
                    break;
                }
            }
        }
        if (!found)
            System.out.println(" No blocks found with location: " + location);
    }

    public int countOccurrences(String item) {
        int count = 0;
        for (Block<T> block : chain) {
            for (T dataItem : block.data) {
                if (dataItem.toString().contains(item)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void clearChain() {
        chain.clear();
        List<T> genesisData = new ArrayList<>();
        genesisData.add((T) "Genesis Block");
        chain.add(new Block<>(0, "0", genesisData));
        System.out.println(" Blockchain has been reset.");
    }
    public List<Block<T>> getChain() {
        return chain;
    }
}
