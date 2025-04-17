package blockchain;

import java.util.ArrayList;

public class Blockchain {

    private ArrayList<Block> chain;  // The chain of blocks
    private int difficulty;          // The difficulty level for mining (number of leading zeros)

    // Constructor to initialize the blockchain
    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;

        // Add the genesis block (the first block in the chain)
        chain.add(createGenesisBlock());
    }

    // Create the genesis block
    private Block createGenesisBlock() {
        return new Block("Genesis Block", "0"); // Data and previous hash of "0"
    }

    // Retrieve the latest block in the chain
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1); // Return the last block in the chain
    }

    // Add a new block to the blockchain
    public void addBlock(String data) {
        Block latestBlock = getLatestBlock();
        Block newBlock = new Block(data, latestBlock.getHash()); // Link to the latest block's hash
        newBlock.mineBlock(difficulty); // Mine the block with proof-of-work
        chain.add(newBlock); // Add the mined block to the chain
    }

    // Validate the entire blockchain
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Check if the current block's hash is correct
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            // Check if the current block links correctly to the previous block
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true; // Return true if all blocks are valid
    }

    // Retrieve the chain of blocks
    public ArrayList<Block> getChain() {
        return chain;
    }
}
