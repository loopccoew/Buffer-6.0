package blockchain;

import java.util.*;

public class Block<T> {
    public int index;
    public String previousHash;
    public String hash;
    public long timestamp;
    public int nonce;
    public List<T> data;

    public Block(int index, String previousHash, List<T> data) {
        if (previousHash == null || previousHash.isEmpty()) {
            throw new IllegalArgumentException("Previous hash cannot be null or empty");
        }

        // Only check data for null, not empty — allow empty data
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        this.index = index;
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return Utils.applySHA256(index + previousHash + timestamp + data.toString() + nonce);
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined: " + hash);
    }
}
