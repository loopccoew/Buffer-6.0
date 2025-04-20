package BlockPackage.SupplyChain;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import BlockPackage.DataStructure.*;

public class LogisticsTracker {
    private final Blockchain<Product> blockchain;
    private final List<Product> uncommittedData;

    public LogisticsTracker(int difficulty) {
        blockchain = new Blockchain<>(difficulty);
        uncommittedData = new ArrayList<>();
    }

    public void addShipment(String productId, String location, String status) {
        Product p = new Product(productId, location, status);
        uncommittedData.add(p);
        System.out.println(" Shipment added for " + productId);
    }

    public void deleteLastUncommitted() {
        if (!uncommittedData.isEmpty()) {
            Product removed = uncommittedData.remove(uncommittedData.size() - 1);
            System.out.println("Removed: " + removed);
        } else {
            System.out.println(" No uncommitted entries to remove.");
        }
    }

    public void editUncommitted(int index, String location, String status) {
        if (index >= 0 && index < uncommittedData.size()) {
            Product old = uncommittedData.get(index);
            Product updated = new Product(old.getProductId(), location, status);
            uncommittedData.set(index, updated);
            System.out.println(" Updated entry at index " + index);
        } else {
            System.out.println(" Invalid index");
        }
    }

    public void commitBlock() {
        if (!uncommittedData.isEmpty()) {
            blockchain.addBlock(new ArrayList<>(uncommittedData));
            uncommittedData.clear();
            System.out.println(" Block committed.");
        } else {
            System.out.println(" No data to commit.");
        }
    }

    public void viewFullChain() {
        blockchain.printChain();
    }

    public void trackPackage(String id) {
        System.out.println("Tracking: " + id);
        for (Block<Product> block : blockchain.getChain()) {
            for (Product p : block.data) {
                if (p.getProductId().equals(id)) {
                    System.out.println("  → " + p);
                }
            }
        }
    }

    public void listAllIds() {
        Set<String> ids = new HashSet<>();
        for (Block<Product> block : blockchain.getChain()) {
            for (Product p : block.data) {
                ids.add(p.getProductId());
            }
        }
        System.out.println("Tracked IDs:");
        ids.forEach(System.out::println);
    }

    public void liveStatus() {
        Map<String, Product> latest = new HashMap<>();
        for (Block<Product> block : blockchain.getChain()) {
            for (Product p : block.data) {
                latest.put(p.getProductId(), p);
            }
        }
        System.out.println(" Latest Status:");
        latest.values().forEach(System.out::println);
    }

    public void searchByKeyword(String keyword) {
        System.out.println(" Searching for keyword: " + keyword);
        for (Block<Product> block : blockchain.getChain()) {
            for (Product p : block.data) {
                if (p.toString().toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println("  • " + p);
                }
            }
        }
    }

    public void validateChain() {
        System.out.println(
            blockchain.isChainValid()
            ? " Blockchain is valid."
            : " Blockchain tampered!"
        );
    }

    public void exportToFile(String filename) {
        try (FileWriter fw = new FileWriter(filename)) {
            for (Block<Product> block : blockchain.getChain()) {
                fw.write("Block #" + block.index + "\n");
                for (Product p : block.data) {
                    fw.write("  • " + p + "\n");
                }
                fw.write("Hash: " + block.hash + "\n");
                fw.write("Prev: " + block.previousHash + "\n\n");
            }
            System.out.println("Exported to " + filename);
        } catch (IOException e) {
            System.out.println(" Error exporting file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogisticsTracker tracker = new LogisticsTracker(3);

        while (true) {
            System.out.println("\n=== LOGISTICS TRACKER ===");
            System.out.println("1. Add Shipment");
            System.out.println("2. Edit Last Entry");
            System.out.println("3. Delete Last Entry");
            System.out.println("4. Commit Block");
            System.out.println("5. Track Package");
            System.out.println("6. List Product IDs");
            System.out.println("7. Live Status Dashboard");
            System.out.println("8. Search by Keyword");
            System.out.println("9. Validate Blockchain");
            System.out.println("10. Export to File");
            System.out.println("11. View Full Chain");
            System.out.println("12. Exit");
            System.out.print("Choose: ");

            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input!");
                continue;
            }

            switch (ch) {
                case 1:
                    System.out.print("ID: ");
                    String id = sc.nextLine();
                    System.out.print("Loc: ");
                    String loc = sc.nextLine();
                    System.out.print("Status: ");
                    String s = sc.nextLine();
                    tracker.addShipment(id, loc, s);
                    break;

                case 2:
                    System.out.print("Index to edit: ");
                    int i = Integer.parseInt(sc.nextLine());
                    System.out.print("New Loc: ");
                    String newLoc = sc.nextLine();
                    System.out.print("New Status: ");
                    String newStatus = sc.nextLine();
                    tracker.editUncommitted(i, newLoc, newStatus);
                    break;

                case 3:
                    tracker.deleteLastUncommitted();
                    break;

                case 4:
                    tracker.commitBlock();
                    break;

                case 5:
                    System.out.print("Track ID: ");
                    tracker.trackPackage(sc.nextLine());
                    break;

                case 6:
                    tracker.listAllIds();
                    break;

                case 7:
                    tracker.liveStatus();
                    break;

                case 8:
                    System.out.print("Keyword: ");
                    tracker.searchByKeyword(sc.nextLine());
                    break;

                case 9:
                    tracker.validateChain();
                    break;

                case 10:
                    System.out.print("File name: ");
                    tracker.exportToFile(sc.nextLine());
                    break;

                case 11:
                    tracker.viewFullChain();
                    break;

                case 12:
                    System.out.println("Bye!");
                    sc.close();
                    return;

                default:
                    System.out.println(" Invalid option.");
                    break;
            }
        }
    }
}
