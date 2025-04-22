package BlockPackage.DataStructure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Block<T> {
    private int index;
    private String timestamp;
    public List<T> data;
    private String previousHash;
    private String hash;
    private int nonce; // Nonce for mining

    public String merkleRoot;

    public enum ConsensusMode {
        PROOF_OF_WORK,
        PROOF_OF_STAKE
    }
    

    // Setter & Getter
    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    
    // Constructor
    public Block(int index, String timestamp, List<T> data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
        computeAndSetMerkleRoot(); // set Merkle root at creation
    }

    // Method to calculate the hash of the block using SHA-256
    public String calculateHash() {
        String input = index + timestamp + data.toString() + previousHash + nonce;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public void computeAndSetMerkleRoot() {
        if (data == null || data.isEmpty()) {
            this.merkleRoot = ""; 
            return;
        }
        List<String> hashedData = new ArrayList<>();
        for (T item : data) {
            hashedData.add(Utils.applySHA256(item.toString()));
        }
        
    
        while (hashedData.size() > 1) {
            List<String> newHashes = new ArrayList<>();
            for (int i = 0; i < hashedData.size(); i += 2) {
                String left = hashedData.get(i);
                String right = (i + 1 < hashedData.size()) ? hashedData.get(i + 1) : left;
                String combined = left + right;
                newHashes.add(Utils.applySHA256(combined));
            }
            hashedData = newHashes;
        }
    
        this.merkleRoot = hashedData.get(0);
    }
    
    // Getter methods
    public int getIndex() {
        return index;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<T> getData() {
        return data;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }
    public int getNonce() {
        return nonce;
    }
    // Method to set the nonce after mining
    public void setNonce(int nonce) {
        this.nonce = nonce;
        this.hash = calculateHash(); // Recalculate the hash with the updated nonce
    }
    
    

    
}
