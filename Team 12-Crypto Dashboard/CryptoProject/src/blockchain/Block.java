package blockchain;

import java.util.Date;
import utils.StringUtil;

public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private long timestamp;
    private int nonce;

    // Constructor
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Calculate the hash for the block
    public String calculateHash() {
        String input = previousHash + Long.toString(timestamp) + Integer.toString(nonce) + data;
        return StringUtil.applySHA256(input);
    }

    // Mine the block
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    // Getters
    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}
