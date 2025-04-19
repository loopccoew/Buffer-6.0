package blockchain;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Blockchain<String> blockchain = new Blockchain<>();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = getChoice();
            scanner.nextLine(); // Flush newline

            switch (choice) {
    case 1:
        addBlock();
        break;
    case 2:
        mineLatestBlock();
        break;
    case 3:
        viewBlock();
        break;
    case 4:
        viewLatestBlock();
        break;
    case 5:
        printChain();
        break;
    case 6:
        validateChain();
        break;
    case 7:
        searchByItem();
        break;
    case 8:
        searchByLocation();
        break;
    case 9:
        countItemOccurrences();
        break;
    case 10:
        showChainSize();
        break;
    case 11:
        clearChain();
        break;
    case 0:
        System.out.println("Exiting...");
        return;
    default:
        System.out.println("Invalid option.");
        break;
}
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Blockchain Menu ---");
        System.out.println("1. Add Block");
        System.out.println("2. Mine Last Block");
        System.out.println("3. View Block by Index");
        System.out.println("4. View Latest Block");
        System.out.println("5. Print Entire Blockchain");
        System.out.println("6. Validate Blockchain");
        System.out.println("7. Search by Item");
        System.out.println("8. Search by Location");
        System.out.println("9. Count Item Occurrences");
        System.out.println("10. Get Blockchain Size");
        System.out.println("11. Clear Blockchain");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private static int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void addBlock() {
        System.out.print("Enter comma-separated data: ");
        String input = scanner.nextLine();
        List<String> data = Arrays.asList(input.split("\\s*,\\s*"));
        blockchain.addBlock(data);
    }

    private static void mineLatestBlock() {
        Block<String> block = blockchain.getLatestBlock();
        System.out.print("Enter mining difficulty: ");
        int difficulty = getChoice();
        block.mineBlock(difficulty);
    }

    private static void viewBlock() {
        System.out.print("Enter block index: ");
        int index = getChoice();
        Block<String> block = blockchain.getBlock(index);
        if (block != null) {
            System.out.println(" Block #" + block.index + ": " + block.data);
        }
    }

    private static void viewLatestBlock() {
        Block<String> block = blockchain.getLatestBlock();
        System.out.println(" Latest Block: " + block.data);
    }

    private static void printChain() {
        blockchain.printChain();
    }

    private static void validateChain() {
        if (blockchain.isChainValid()) {
            System.out.println(" Blockchain is valid.");
        } else {
            System.out.println(" Blockchain is NOT valid.");
        }
    }

    private static void searchByItem() {
        System.out.print("Enter item to search: ");
        String item = scanner.nextLine();
        blockchain.searchByItem(item);
    }

    private static void searchByLocation() {
        System.out.print("Enter location to search: ");
        String location = scanner.nextLine();
        blockchain.searchByLocation(location);
    }

    private static void countItemOccurrences() {
        System.out.print("Enter item to count: ");
        String item = scanner.nextLine();
        int count = blockchain.countOccurrences(item);
        System.out.println(" Item found " + count + " times.");
    }

    private static void showChainSize() {
        System.out.println(" Blockchain size: " + blockchain.getSize());
    }

    private static void clearChain() {
        blockchain.clearChain();
    }
}
