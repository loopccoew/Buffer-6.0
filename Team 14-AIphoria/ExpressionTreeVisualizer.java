import java.util.Scanner;
import java.util.Stack;

public class ExpressionTreeVisualizer {
    private static class Node {
        String value;
        Node left, right;

        public Node(String item) {
            value = item;
            left = right = null;
        }
    }

    private Node root;
    private Scanner sc;

    public ExpressionTreeVisualizer() {
        root = null;
        sc = new Scanner(System.in);
    }

    public void expressionTreeMenu() {
        System.out.println("\n🎓 Do you want to enter Learning Mode first?");
        System.out.println("1. Yes, teach me about Expression Trees 🧑‍🏫");
        System.out.println("2. No, take me to operations 🚀");
        System.out.print("Enter your choice: ");
        int learnChoice = sc.nextInt();
        if (learnChoice == 1) {
            expressionLearningMode();
        }

        while (true) {
            System.out.println("\n===== 🧑‍💻 Expression Tree Operations Menu =====");
            System.out.println("1. Build Expression Tree");
            System.out.println("2. In-order Traversal");
            System.out.println("3. Pre-order Traversal");
            System.out.println("4. Post-order Traversal");
            System.out.println("5. Evaluate Expression");
            System.out.println("6. Fun Fact about Expression Trees 🎉");
            System.out.println("7. Expression Tree Quiz 🤔");
            System.out.println("0. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> buildExpressionTree();
                case 2 -> inOrderTraversal(root);
                case 3 -> preOrderTraversal(root);
                case 4 -> postOrderTraversal(root);
                case 5 -> evaluateExpression();
                case 6 -> funFact();
                case 7 -> expressionTreeQuiz();
                case 0 -> {
                    System.out.println("🔙 Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }

    private void expressionLearningMode() {
        System.out.println("\n📚 Welcome to Expression Tree Learning Mode!");
        showRealLifeVisualization();
        System.out.println("\n🌳 What is an Expression Tree?");
        System.out.println("- An Expression Tree is a binary tree where each internal node is an operator and each leaf node is an operand.");
        System.out.println("- Used to represent expressions such as algebraic equations.");

        System.out.println("\n✨ Important Rules:");
        System.out.println("- Each non-leaf node (internal node) represents an operator (+, -, *, /).");
        System.out.println("- Leaf nodes represent operands (numbers or variables).");
        System.out.println("- The tree is evaluated in a bottom-up manner (post-order).");

        System.out.println("\n🧠 Quick Quiz:");
        System.out.println("Q. What type of node in an Expression Tree represents an operator?");
        System.out.println("1. Leaf Node");
        System.out.println("2. Internal Node");
        System.out.println("3. Root Node");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 2) {
            System.out.println("✅ Correct! Internal nodes represent operators.");
        } else {
            System.out.println("❌ Incorrect! Internal nodes represent operators in an Expression Tree.");
        }

        System.out.println("\n🎉 You've completed Expression Tree Learning Mode! Let's start operations!");
    }

    private void buildExpressionTree() {
        System.out.println("\n🔄 Enter an infix expression to build the tree (e.g., 3 + 5 * 2):");
        sc.nextLine();  // To consume the leftover newline character
        String expression = sc.nextLine();
        root = buildTreeFromExpression(expression);
        System.out.println("✅ Expression tree built successfully!");
    }

    private Node buildTreeFromExpression(String expression) {
        Stack<Node> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                String operand = String.valueOf(ch);
                operands.push(new Node(operand));
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek().charAt(0))) {
                    Node operatorNode = new Node(operators.pop());
                    operatorNode.right = operands.pop();
                    operatorNode.left = operands.pop();
                    operands.push(operatorNode);
                }
                operators.push(String.valueOf(ch));
            }
        }

        while (!operators.isEmpty()) {
            Node operatorNode = new Node(operators.pop());
            operatorNode.right = operands.pop();
            operatorNode.left = operands.pop();
            operands.push(operatorNode);
        }

        return operands.pop();
    }

    private int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0;
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

    private void evaluateExpression() {
        System.out.println("\n🔍 Evaluating the expression tree...");
        int result = evaluate(root);
        System.out.println("Result of the expression: " + result);
    }

    private int evaluate(Node node) {
        if (node == null) {
            return 0;
        }

        if (node.left == null && node.right == null) {
            return Integer.parseInt(node.value);
        }

        int leftVal = evaluate(node.left);
        int rightVal = evaluate(node.right);

        switch (node.value) {
            case "+" -> { return leftVal + rightVal; }
            case "-" -> { return leftVal - rightVal; }
            case "*" -> { return leftVal * rightVal; }
            case "/" -> { return leftVal / rightVal; }
        }
        return 0;
    }

    private void showRealLifeVisualization() {
        System.out.println("\n🌳 Imagine solving an expression like '3 + 5 * 2'.");
        System.out.println("In an Expression Tree:");
        System.out.println("        +");
        System.out.println("       / \\");
        System.out.println("      3   *");
        System.out.println("         / \\");
        System.out.println("        5   2");
        System.out.println("\nThis tree helps evaluate the expression from bottom to top!");
    }

    private void funFact() {
        System.out.println("\n🎉 FUN FACT:");
        System.out.println("Expression Trees are used in compilers to evaluate mathematical expressions!");
    }

    private void expressionTreeQuiz() {
        System.out.println("\n🤔 EXPRESSION TREE QUIZ:");
        System.out.println("Q. What is the root of an Expression Tree?");
        System.out.println("1. Operand");
        System.out.println("2. Operator");
        System.out.println("3. Both");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 2) {
            System.out.println("✅ Correct! The root node in an Expression Tree is usually an operator.");
        } else {
            System.out.println("❌ Incorrect. The root is typically an operator.");
        }
    }
}

