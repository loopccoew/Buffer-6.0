import java.util.Scanner;
import java.util.Stack;

public class StackVisualizer {
    private Stack<Integer> stack;
    private Scanner sc;

    public StackVisualizer() {
        stack = new Stack<>();
        sc = new Scanner(System.in);
    }

    public void stackMenu() {
        System.out.println("\n🎓 Do you want to enter Learning Mode first?");
        System.out.println("1. Yes, teach me about Stack 📚");
        System.out.println("2. No, take me to operations 🚀");
        System.out.print("Enter your choice: ");
        int learnChoice = sc.nextInt();
        if (learnChoice == 1) {
            stackLearningMode();
        }

        while (true) {
            System.out.println("\n===== 📚 Stack Operations Menu =====");
            System.out.println("1. Push an element");
            System.out.println("2. Pop an element");
            System.out.println("3. Peek (view top element)");
            System.out.println("4. Display Stack");
            System.out.println("5. Fun Fact about Stack 🎉");
            System.out.println("6. Stack Quiz 🤔");
            System.out.println("0. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> push();
                case 2 -> pop();
                case 3 -> peek();
                case 4 -> display();
                case 5 -> funFact();
                case 6 -> stackQuiz();
                case 0 -> {
                    System.out.println("🔙 Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }

    private void stackLearningMode() {
        System.out.println("\n📚 Welcome to Stack Learning Mode!");
        showRealLifeVisualization();
        System.out.println("\n🛠️ What is a Stack?");
        System.out.println("- A Stack is a data structure where elements are added and removed from the same end.");
        System.out.println("- Think of a stack of plates 🍽️ or a pile of books 📚!");

        System.out.println("\n✨ Important Rule:");
        System.out.println("- LIFO: Last In, First Out!");

        System.out.println("\n🎯 Real-life Example:");
        System.out.println("- Plates in a cafeteria 🥗. You remove the top plate first!");

        System.out.println("\n🧠 Quick Quiz:");
        System.out.println("Q. If you push 5, 10, and 15 into a stack, which will be popped first?");
        System.out.println("1. 5");
        System.out.println("2. 10");
        System.out.println("3. 15");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 3) {
            System.out.println("✅ Correct! 15 will be popped first.");
        } else {
            System.out.println("❌ Incorrect! Remember: Last In, First Out!");
        }

        System.out.println("\n🎉 You've completed Stack Learning Mode! Let's start operations!");
    }

    private void push() {
        System.out.print("Enter value to push: ");
        int value = sc.nextInt();
        System.out.println("\n🛠️ Step-by-Step:");
        System.out.println("Real-life Example: 📚 Stacking books on top of each other!");
        System.out.println("1. Take the value " + value + ".");
        System.out.println("2. Place it on top of the current stack.");
        System.out.println("3. Now " + value + " becomes the new top element.");
        stack.push(value);
        System.out.println("✅ " + value + " pushed onto the stack!");
        display();
    }

    private void pop() {
        System.out.println("\n🛠️ Step-by-Step:");
        if (stack.isEmpty()) {
            System.out.println("Real-life Example: 📚 Trying to remove a book from an empty table!");
            System.out.println("⚠️ Stack is empty! Cannot pop.");
        } else {
            System.out.println("Real-life Example: 📖 Removing the top book from a pile!");
            int value = stack.pop();
            System.out.println("✅ " + value + " popped from the stack!");
            display();
        }
    }

    private void peek() {
        System.out.println("\n🛠️ Step-by-Step:");
        if (stack.isEmpty()) {
            System.out.println("Real-life Example: 📚 Checking an empty table for books!");
            System.out.println("⚠️ Stack is empty! Nothing to peek.");
        } else {
            System.out.println("Real-life Example: 👀 Checking the top book without picking it up!");
            System.out.println("👀 Top element: " + stack.peek());
        }
    }

    private void display() {
        System.out.println("\n🛠️ Step-by-Step:");
        if (stack.isEmpty()) {
            System.out.println("Real-life Example: 📚 No books stacked currently!");
            System.out.println("⚠️ Stack is empty! Nothing to display.");
        } else {
            System.out.println("Real-life Example: 📚 Listing books from top to bottom!");
            System.out.println("\n📚 Current Stack (Top ➡ Bottom):");
            for (int i = stack.size() - 1; i >= 0; i--) {
                System.out.println(" ┌───────┐ ");
                System.out.printf(" │  %3d  │ \n", stack.get(i));
                System.out.println(" └───────┘ ");
            }
        }
    }

    private void showRealLifeVisualization() {
        System.out.println("\nImagine a stack of plates 🍽️:");
        System.out.println("Top Plate (Last inserted)");
        System.out.println(" ┌───────┐ ");
        System.out.println(" │ Plate │ ");
        System.out.println(" └───────┘ ");
        System.out.println(" ┌───────┐ ");
        System.out.println(" │ Plate │ ");
        System.out.println(" └───────┘ ");
        System.out.println(" ┌───────┐ ");
        System.out.println(" │ Plate │ ");
        System.out.println(" └───────┘ ");
        System.out.println("Bottom Plate (First inserted)");
        System.out.println("\n➡️  Last In, First Out (LIFO) principle!");
    }

    private void funFact() {
        System.out.println("\n🎉 FUN FACT:");
        System.out.println("The 'Undo' feature in apps like Word or Photoshop uses a Stack!");
        System.out.println("Each action you do is pushed onto the stack. Undo pops it!");
    }

    private void stackQuiz() {
        System.out.println("\n🤔 STACK QUIZ:");
        System.out.println("Q. What happens when you try to pop from an empty stack?");
        System.out.println("1. It gives the top element");
        System.out.println("2. It throws an error or shows empty warning");
        System.out.println("3. It adds a new element");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 2) {
            System.out.println("✅ Correct! Popping from an empty stack gives an error or warning.");
        } else {
            System.out.println("❌ Incorrect. Correct answer is 2: Empty Stack Error!");
        }
    }
}
