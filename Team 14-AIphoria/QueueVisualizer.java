import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class QueueVisualizer {
    private Queue<Integer> queue;
    private Scanner sc;

    public QueueVisualizer() {
        queue = new LinkedList<>();
        sc = new Scanner(System.in);
    }

    public void queueMenu() {
        System.out.println("\n🎓 Do you want to enter Learning Mode first?");
        System.out.println("1. Yes, teach me about Queue 🚌");
        System.out.println("2. No, take me to operations 🚀");
        System.out.print("Enter your choice: ");
        int learnChoice = sc.nextInt();
        if (learnChoice == 1) {
            queueLearningMode();
        }

        while (true) {
            System.out.println("\n===== 🚌 Queue Operations Menu =====");
            System.out.println("1. Enqueue an element");
            System.out.println("2. Dequeue an element");
            System.out.println("3. Peek (view front element)");
            System.out.println("4. Display Queue");
            System.out.println("5. Fun Fact about Queue 🎉");
            System.out.println("6. Queue Quiz 🤔");
            System.out.println("0. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> enqueue();
                case 2 -> dequeue();
                case 3 -> peek();
                case 4 -> display();
                case 5 -> funFact();
                case 6 -> queueQuiz();
                case 0 -> {
                    System.out.println("🔙 Returning to Main Menu...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }

    private void queueLearningMode() {
        System.out.println("\n📚 Welcome to Queue Learning Mode!");
        showRealLifeVisualization();
        System.out.println("\n🛠️ What is a Queue?");
        System.out.println("- A Queue is a linear data structure where elements are added at the rear and removed from the front.");
        System.out.println("- Just like people standing in line at a ticket counter or bus stop 🚌!");

        System.out.println("\n✨ Important Rule:");
        System.out.println("- FIFO: First In, First Out!");

        System.out.println("\n🧠 Quick Quiz:");
        System.out.println("Q. If you enqueue 5, 10, and 15, which will be dequeued first?");
        System.out.println("1. 5");
        System.out.println("2. 10");
        System.out.println("3. 15");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 1) {
            System.out.println("✅ Correct! 5 entered first and will be removed first.");
        } else {
            System.out.println("❌ Incorrect! Remember: First In, First Out!");
        }

        System.out.println("\n🎉 You've completed Queue Learning Mode! Let's start operations!");
    }

    private void enqueue() {
        System.out.print("Enter value to enqueue: ");
        int value = sc.nextInt();
        System.out.println("\n🛠️ Step-by-Step:");
        System.out.println("Real-life Example: 🚶 Joining the end of a line!");
        System.out.println("1. Person (value " + value + ") joins at the rear.");
        System.out.println("2. Queue rearranges with " + value + " at the end.");
        queue.add(value);
        System.out.println("✅ " + value + " enqueued to the queue!");
        display();
    }

    private void dequeue() {
        System.out.println("\n🛠️ Step-by-Step:");
        if (queue.isEmpty()) {
            System.out.println("Real-life Example: 🪑 Empty waiting line!");
            System.out.println("⚠️ Queue is empty! Cannot dequeue.");
        } else {
            int value = queue.remove();
            System.out.println("🚪 Front person (value " + value + ") is now removed.");
            System.out.println("✅ " + value + " dequeued from the queue!");
            display();
        }
    }

    private void peek() {
        System.out.println("\n🛠️ Step-by-Step:");
        if (queue.isEmpty()) {
            System.out.println("Real-life Example: 🧐 Looking for someone in an empty queue!");
            System.out.println("⚠️ Queue is empty! Nothing to peek.");
        } else {
            System.out.println("👀 Front element: " + queue.peek());
        }
    }

    private void display() {
        System.out.println("\n🛠️ Step-by-Step:");
        if (queue.isEmpty()) {
            System.out.println("Real-life Example: 🙅 No one in the queue!");
            System.out.println("⚠️ Queue is empty!");
        } else {
            System.out.println("Real-life Example: 🚶🚶🚶 People standing in line.");
            System.out.println("\n🚌 Queue (Front ➡ Rear):");
            for (int val : queue) {
                System.out.print(" ┌───────┐ ");
            }
            System.out.println();
            for (int val : queue) {
                System.out.printf(" │  %3d  │ ", val);
            }
            System.out.println();
            for (int val : queue) {
                System.out.print(" └───────┘ ");
            }
            System.out.println("\n   Front          Rear");
        }
    }

    private void showRealLifeVisualization() {
        System.out.println("\nImagine people waiting at a bus stop 🚌:");
        System.out.println("Front → [👩] [🧑] [👨] ← Rear");
        System.out.println("➡️ First person in will be the first to board the bus!");
        System.out.println("\n➡️  First In, First Out (FIFO) principle!");
    }

    private void funFact() {
        System.out.println("\n🎉 FUN FACT:");
        System.out.println("Printer queues work using the queue data structure!");
        System.out.println("The first document sent to print is printed first!");
    }

    private void queueQuiz() {
        System.out.println("\n🤔 QUEUE QUIZ:");
        System.out.println("Q. What is the key difference between Stack and Queue?");
        System.out.println("1. Stack is FIFO and Queue is LIFO");
        System.out.println("2. Stack is LIFO and Queue is FIFO");
        System.out.println("3. Both are LIFO");
        System.out.print("Your answer (1/2/3): ");
        int answer = sc.nextInt();
        if (answer == 2) {
            System.out.println("✅ Correct! Stack is LIFO and Queue is FIFO.");
        } else {
            System.out.println("❌ Incorrect. Correct answer is 2.");
        }
    }
}
