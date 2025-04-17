import java.util.Scanner;

public class BinaryTreeVisualizer {
    private static class Node {
        int value;
        Node left, right;

        public Node(int item) {
            value = item;
            left = right = null;
        }
    }

    private Node root;
    private Scanner sc;

    public BinaryTreeVisualizer() {
        root = null;
        sc = new Scanner(System.in);
    }

    public void treeMenu() {
        System.out.println("\n🎓 Do you want to enter Learning Mode first?");
        System.out.println("1. Yes, teach me about Binary Trees 🌳");
        System.out.println("2. No, take me to operations 🚀");
        System.out.print("Enter your choice: ");
        int learnChoice = sc.nextInt();
        if (learnChoice == 1) {
            treeLearningMode();
        }

        while (true) {
            System.out.println("\n===== 🌳 Binary Tree Operations Menu =====");
            System.out.println("1. Insert a node");
            System.out.println("2. Display Tree Structure");
            System.out.println("3. In-order Traversal");
            System.out.println("4. Pre-order Traversal");
            System.out.println("5. Post-order Traversal");
            System.out.println("6. Fun Fact about Trees 🎉");
            System.out.println("7. Tree Quiz 🤔");
            System.out.println("0. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> insertNode();
                case 2 -> displayTreeStructure();
                case 3 -> inOrderTraversal(root);
                case 4 -> preOrderTraversal(root);
                case 5 -> postOrderTraversal(root);
                case 6 -> funFact();
                case 7 -> treeQuiz();
                case 0 -> {
                    System.out.println("🔙 Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }

    private void treeLearningMode() {
        System.out.println("\n📚 Welcome to Binary Tree Learning Mode!");
        showRealLifeVisualization();
        System.out.println("\n🌳 What is a Binary Tree?");
        System.out.println("- A binary tree is a tree in which each node has at most two children.");
        System.out.println("- Binary trees are used in many places like databases, expression parsing, and file systems!");

        System.out.println("\n✨ Important Rules:");
        System.out.println("- Each node has at most two children: left and right.");
        System.out.println("- The root is the starting node, and all other nodes are connected by edges.");

        System.out.println("\n🧠 Quick Quiz:");
        System.out.println("Q. How many children can a node have in a Binary Tree?");
        System.out.println("1. 1");
        System.out.println("2. 2");
        System.out.println("3. More than 2");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 2) {
            System.out.println("✅ Correct! A node can have at most two children.");
        } else {
            System.out.println("❌ Incorrect! In a Binary Tree, each node can have at most two children.");
        }

        System.out.println("\n🎉 You've completed Binary Tree Learning Mode! Let's start operations!");
    }

    private void insertNode() {
        System.out.print("Enter value to insert: ");
        int value = sc.nextInt();
        root = insertRec(root, value);
        System.out.println("✅ Node with value " + value + " inserted successfully!");
    }

    private Node insertRec(Node root, int value) {
        if (root == null) {
            root = new Node(value);
            return root;
        }
        System.out.println("Where would you like to insert the value " + value + "?");
        System.out.println("1. Left Child");
        System.out.println("2. Right Child");
        System.out.print("Enter your choice (1/2): ");
        int choice = sc.nextInt();

        if (choice == 1) {
            root.left = insertRec(root.left, value);
        } else if (choice == 2) {
            root.right = insertRec(root.right, value);
        } else {
            System.out.println("❌ Invalid choice. Please try again.");
        }
        return root;
    }

    private void inOrderTraversal(Node root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.print(root.value + " ");
            inOrderTraversal(root.right);
        }
    }

    private void preOrderTraversal(Node root) {
        if (root != null) {
            System.out.print(root.value + " ");
            preOrderTraversal(root.left);
            preOrderTraversal(root.right);
        }
    }

    private void postOrderTraversal(Node root) {
        if (root != null) {
            postOrderTraversal(root.left);
            postOrderTraversal(root.right);
            System.out.print(root.value + " ");
        }
    }

    private void displayTreeStructure() {
        System.out.println("\n📜 Displaying the tree structure:");
        displayTree(root, "", true);
    }

    private void displayTree(Node root, String indent, boolean isLeft) {
        if (root != null) {
            System.out.println(indent + (isLeft ? "L---- " : "R---- ") + root.value);
            displayTree(root.left, indent + (isLeft ? "|   " : "    "), true);
            displayTree(root.right, indent + (isLeft ? "|   " : "    "), false);
        }
    }

    private void showRealLifeVisualization() {
        System.out.println("\n🌳 Imagine a family tree where each person can have two children:");
        System.out.println("          Grandparent");
        System.out.println("         /           \\");
        System.out.println("     Parent1       Parent2");
        System.out.println("     /   \\            /  \\");
        System.out.println("  Child1 Child2   Child3 Child4");
        System.out.println("\nThis is similar to a Binary Tree!");
    }

    private void funFact() {
        System.out.println("\n🎉 FUN FACT:");
        System.out.println("Binary Trees are the backbone of many algorithms like sorting, searching, and even network routing!");
    }

    private void treeQuiz() {
        System.out.println("\n🤔 TREE QUIZ:");
        System.out.println("Q. What is the maximum number of children each node can have in a Binary Tree?");
        System.out.println("1. 1");
        System.out.println("2. 2");
        System.out.println("3. 3");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 2) {
            System.out.println("✅ Correct! A Binary Tree node can have a maximum of two children.");
        } else {
            System.out.println("❌ Incorrect. The correct answer is 2.");
        }
    }

    public static void main(String[] args) {
        BinaryTreeVisualizer visualizer = new BinaryTreeVisualizer();
        visualizer.treeMenu();
    }
}
