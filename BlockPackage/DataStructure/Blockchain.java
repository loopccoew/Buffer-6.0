package BlockPackage.DataStructure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import BlockPackage.voting.Vote;

enum ConsensusMode {
    PROOF_OF_WORK,
    PROOF_OF_STAKE
}

public class Blockchain<T> {
    private List<Block<T>> chain; // List to store the blocks
    private int difficulty;

    private Map<String, Integer> roleStake;
    private String currentRole;
    private ConsensusMode consensusMode;
    private Map<String, String> rolePasswords;


    // Default constructor: assumes PoW
    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        this.consensusMode = ConsensusMode.PROOF_OF_WORK;
        this.chain = new ArrayList<>();
        this.chain.add(createGenesisBlock());

    }

    // PoS constructor: role-based
    public Blockchain(int difficulty, ConsensusMode mode, String role) {
        this.difficulty = difficulty;
        this.consensusMode = mode;
        this.currentRole = role.toLowerCase();
        this.chain = new ArrayList<>();
        this.roleStake = new HashMap<>();
        setupStake();
        this.chain.add(createGenesisBlock());
    }

    // PoW constructor with explicit mode
    public Blockchain(int difficulty, ConsensusMode mode) {
        this.difficulty = difficulty;
        this.consensusMode = mode;
        this.chain = new ArrayList<>();
        this.chain.add(createGenesisBlock());
    }

    //Proof of Stake
    private void setupStake() {
        roleStake.put("admin", 70);
        roleStake.put("auditor", 30);
        roleStake.put("warehouse", 50);
        roleStake.put("transporter", 60);
    
        rolePasswords = new HashMap<>();
        rolePasswords.put("admin", "admin123");
        rolePasswords.put("auditor", "audit@2025");
        rolePasswords.put("warehouse", "wh@secure");
        rolePasswords.put("transporter", "trans@456");
    }
    
    // Proof of Stake: Simple validator
    private void validateByStake(Block<T> block) {
        Scanner scanner = new Scanner(System.in); // For password input
        int stake = roleStake.getOrDefault(currentRole, 0);
        int threshold = 40;
        System.out.println("Validating block by PoS... Role: " + currentRole + " | Stake: " + stake);
        
        System.out.print("Enter password for role '" + currentRole + "': ");
        String inputPassword = scanner.nextLine();

        String actualPassword = rolePasswords.get(currentRole);
        if (actualPassword == null || !actualPassword.equals(inputPassword)) {
            throw new RuntimeException(" Authentication failed for role: " + currentRole);
        }

        if (stake >= threshold) {
            System.out.println(" PoS Validation Success for Role: " + currentRole);
        } else {
            throw new RuntimeException(" PoS Validation Failed. Insufficient stake for role: " + currentRole);
        }
        
    }

    // Method to create the Genesis Block
    public Block<T> createGenesisBlock() {
        List<T> emptyData = new ArrayList<>(); // No data in the genesis block
        String currentTimestamp = String.valueOf(System.currentTimeMillis()); // Dynamic time
        return new Block<>(0, currentTimestamp, emptyData, "0"); // Index 0, no previous hash
    }    
    

    // Method to add a new block to the blockchain
    public void addBlock(List<T> blockData) {
        Block<T> newBlock = new Block<>(chain.size(), String.valueOf(System.currentTimeMillis()), blockData, chain.get(chain.size() - 1).getHash());
        this.chain.add(newBlock); // Add the new block to the blockchain
    }

    // Method to print the entire blockchain
    public void printChain() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
        System.out.println("\n========================== BLOCKCHAIN ==========================\n");
    
        for (int i = 0; i < chain.size(); i++) {
            Block<T> block = chain.get(i);
            String readableTime;
            try {
                long ts = Long.parseLong(block.getTimestamp());
                readableTime = sdf.format(new Date(ts));
            } catch (Exception e) {
                readableTime = block.getTimestamp(); // fallback
            }
            System.out.println("╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.printf ("║ %-116s ║\n", String.format("Block #%d", block.getIndex()));
            System.out.println("╠═════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf ("║ Timestamp     : %-98s ║\n", readableTime);
            System.out.printf ("║ Data          : %-98s ║\n", block.getData().toString());
            System.out.printf ("║ Prev. Hash    : %-98s ║\n", block.getPreviousHash());
            System.out.printf ("║ Hash          : %-98s ║\n", block.getHash());
            System.out.printf ("║ Merkle Root   : %-98s ║\n", block.getMerkleRoot());
            System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
    
            if (i < chain.size() - 1) {
                System.out.println("                                         │");
                System.out.println("                                         ▼\n");
            }
      
        }
    
        System.out.println("====================== END OF BLOCKCHAIN ======================\n");

    }
    
    // Getter method for the blockchain
    public List<Block<T>> getChain() {
        return chain;
    }
    public void mineBlock(Block<T> block) {
        System.out.println("Processing block: " + block.getIndex());
    
        if (consensusMode == ConsensusMode.PROOF_OF_WORK) {
            System.out.println("Mining using Proof of Work...");
            String target = new String(new char[difficulty]).replace('\0', '0');
            int nonce = 0;
            while (true) {
                block.setNonce(nonce);
                if (block.getHash().startsWith(target)) {
                    break;
                }
                nonce++;
            }
            System.out.println("Block mined! Hash: " + block.getHash());
    
        } else if (consensusMode == ConsensusMode.PROOF_OF_STAKE) {
            System.out.println("Validating using Proof of Stake...");
            validateByStake(block);  
            block.setNonce(0); // Optional: simulate a default value
            block.calculateHash(); // Optional: ensure hash is consistent
            System.out.println("Block validated and added using PoS.");
        }
        
    }
    
    
    public boolean isValid() {
        for (int i = 1; i < this.chain.size(); i++) {
            Block<T> currentBlock = this.chain.get(i);
            Block<T> previousBlock = this.chain.get(i - 1);

            // Validate hash
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            // Validate previous block hash
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }
    
    public int getSize() {
        return chain.size();
    }
    public List<Block<T>> getBlocksAfter(long timestamp) {
        List<Block<T>> result = new ArrayList<>();
        for (Block<T> block : chain) {
            if (Long.parseLong(block.getTimestamp()) > timestamp) {
                result.add(block);
            }
        }
        return result;
    }
    
   
    public Block<T> getLatestBlock() {
        return chain.get(chain.size() - 1);
    }
    public void mineBlock(Block<Vote> block, int difficulty) {
    System.out.println("⛏️ Mining block...");

    String target = new String(new char[difficulty]).replace('\0', '0');
    int nonce = 0;

    while (true) {
        block.setNonce(nonce);
        if (block.getHash().startsWith(target)) {
            break;
        }
        nonce++;
    }

    System.out.println("✅ Block mined! Hash: " + block.getHash());
}

public void addBlock(Block<T> block) {
    chain.add(block);
}
    
}
