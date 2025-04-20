package BlockPackage.DataStructure;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Blockchain<String> blockchain = null;

        System.out.println("=== Blockchain Test Console ===");
        System.out.println("Choose Consensus Mode: ");
        System.out.println("1. Proof of Work");
        System.out.println("2. Proof of Stake");
        int modeChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (modeChoice == 1) {
            blockchain = new Blockchain<>(3); // PoW with difficulty 3
        } else {
            System.out.print("Enter your role (admin, auditor, warehouse, transporter): ");
            String role = scanner.nextLine();
            blockchain = new Blockchain<>(3, ConsensusMode.PROOF_OF_STAKE, role);
        }

        while (true) {
            System.out.println("\n===== Blockchain Menu =====");
            System.out.println("0. Create New Genesis Block");
            System.out.println("1. Add & Mine Block");
            System.out.println("2. Print Blockchain");
            System.out.println("3. Validate Blockchain");
            System.out.println("4. Show Merkle Roots");
            System.out.println("5. Tamper With a Block");
            System.out.println("6. Get Blocks After Timestamp");
            System.out.println("7. Get Blockchain Size");
            System.out.println("8. Exit");
            System.out.print("Enter option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0:
                    if (modeChoice == 1)
                        blockchain = new Blockchain<>(3); // Reset to PoW
                    else {
                        System.out.print("Enter role: ");
                        String role = scanner.nextLine();
                        blockchain = new Blockchain<>(3, ConsensusMode.PROOF_OF_STAKE, role);
                    }
                    System.out.println(" New genesis block created.");
                    break;

                case 1:
                    System.out.println("Enter transactions (comma-separated):");
                    String input = scanner.nextLine();
                    List<String> blockData = Arrays.asList(input.split("\\s*,\\s*"));
                    blockchain.addBlock(blockData);
                    Block<String> newBlock = blockchain.getChain().get(blockchain.getSize() - 1);

                    if (modeChoice == 1) {
                        blockchain.mineBlock(newBlock); // Proof of Work
                    } else {
                        try {
                            blockchain.mineBlock(newBlock); // Mine as usual
                            // Then validate via PoS
                            System.out.println("Simulating Proof of Stake validation...");
                            blockchain.isValid(); // optional
                        } catch (Exception e) {
                            System.out.println(" PoS Validation Failed: " + e.getMessage());
                        }
                    }
                    break;

                case 2:
                    blockchain.printChain();
                    break;

                case 3:
                    boolean valid = blockchain.isValid();
                    System.out.println("Blockchain is " + (valid ? "VALID " : "INVALID "));
                    break;

                case 4:
                    System.out.println("Merkle Roots:");
                    for (Block<String> block : blockchain.getChain()) {
                        List<String> hashedData = new ArrayList<>();
                        for (String d : block.getData()) {
                            hashedData.add(Utils.applySHA256(d));
                        }
                        System.out.println("Block " + block.getIndex() + ": " + block.merkleRoot);
                    }
                    break;

                case 5:
                    System.out.print("Enter block index to tamper: ");
                    int idx = scanner.nextInt();
                    scanner.nextLine();
                    if (idx > 0 && idx < blockchain.getSize()) {
                        System.out.print("New transaction to replace first entry: ");
                        String fake = scanner.nextLine();
                        blockchain.getChain().get(idx).getData().set(0, fake);
                        System.out.println("Block " + idx + " tampered.");
                    } else {
                        System.out.println(" Invalid index. Cannot tamper with genesis block.");
                    }
                    break;

                case 6:
                    System.out.print("Enter timestamp (in milliseconds): ");
                    long ts = scanner.nextLong();
                    scanner.nextLine();
                    List<Block<String>> filtered = blockchain.getBlocksAfter(ts);
                    System.out.println("Blocks after timestamp " + ts + ":");
                    for (Block<String> b : filtered) {
                        System.out.println("Block #" + b.getIndex() + " | Timestamp: " + b.getTimestamp());
                    }
                    break;

                case 7:
                
                    System.out.println(" Blockchain size: " + blockchain.getSize() + " blocks");
                    break;

            

                case 8:
                    System.out.println("Exiting... ");
                    return;

                default:
                    System.out.println(" Invalid choice");
            }
        }
    }
}


