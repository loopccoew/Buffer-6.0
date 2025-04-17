import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SinglyLinkedListVisualizer sll = new SinglyLinkedListVisualizer();
        DoublyLinkedListVisualizer dll = new DoublyLinkedListVisualizer();
        StackVisualizer stack = new StackVisualizer();   // Added Stack Visualizer
        QueueVisualizer queue = new QueueVisualizer();   // Added Queue Visualizer
        BinaryTreeVisualizer binaryTree = new BinaryTreeVisualizer();  // Added Binary Tree Visualizer
        ExpressionTreeVisualizer expressionTree = new ExpressionTreeVisualizer();  // Added Expression Tree Visualizer
        GraphVisualizer graph = new GraphVisualizer();  // Added Graph Visualizer

        while (true) {
            System.out.println("\n🌟 Welcome to Data Structure Visualizer 🌟");
            System.out.println("Choose a Data Structure:");
            System.out.println("1. Singly Linked List");
            System.out.println("2. Doubly Linked List");
            System.out.println("3. Stack");
            System.out.println("4. Queue");
            System.out.println("5. Binary Tree");
            System.out.println("6. Expression Tree");
            System.out.println("7. Graph");   // Added Graph option to menu
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> sll.singlyLinkedListMenu();
                case 2 -> dll.doublyLinkedListMenu();
                case 3 -> stack.stackMenu();   // Call Stack Menu
                case 4 -> queue.queueMenu();   // Call Queue Menu
                case 5 -> binaryTree.treeMenu();   // Call Binary Tree Menu
                case 6 -> expressionTree.expressionTreeMenu();   // Call Expression Tree Menu
                case 7 -> graph.graphMenu();   // Call Graph Menu (added here)
                case 0 -> {
                    System.out.println("👋 Thank you for using Data Structure Visualizer! Goodbye!");
                    sc.close();
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid choice. Please try again!");
            }
        }
    }
}
