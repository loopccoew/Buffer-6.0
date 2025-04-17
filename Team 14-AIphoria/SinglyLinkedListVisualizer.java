import java.util.Scanner;
class Node {
    int data;
    Node next;

    public Node(int data) {
        this.data = data;
        this.next = null;
    }
}


public class SinglyLinkedListVisualizer {
    Node head = null;
    int size = 0;
    Scanner sc = new Scanner(System.in);

    public void singlyLinkedListMenu() {
        while (true) {
            System.out.println("\n📘 Singly Linked List Menu:");
            System.out.println("1. Insert at Start");
            System.out.println("2. Insert at End");
            System.out.println("3. Insert at Position");
            System.out.println("4. Delete from Start");
            System.out.println("5. Delete from End");
            System.out.println("6. Delete from Position");
            System.out.println("7. Search");
            System.out.println("8. Reverse");
            System.out.println("9. 🎓 Learning Mode");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> insertAtStart();
                case 2 -> insertAtEnd();
                case 3 -> insertAtPosition();
                case 4 -> deleteFromStart();
                case 5 -> deleteFromEnd();
                case 6 -> deleteFromPosition();
                case 7 -> search();
                case 8 -> reverse();
                case 9 -> learningMenu();
                case 0 -> { return; }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    // Basic Operations
    public void insertAtStart() {
        System.out.print("Enter value to insert at start: ");
        int val = sc.nextInt();
        Node newNode = new Node(val);
        newNode.next = head;
        head = newNode;
        size++;
        System.out.println("✅ Inserted at start.");
        displayList();
    }

    public void insertAtEnd() {
        System.out.print("Enter value to insert at end: ");
        int val = sc.nextInt();
        Node newNode = new Node(val);
        if (head == null) head = newNode;
        else {
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
        }
        size++;
        System.out.println("✅ Inserted at end.");
        displayList();
    }

    public void insertAtPosition() {
        System.out.print("Enter position: ");
        int pos = sc.nextInt();
        System.out.print("Enter value: ");
        int val = sc.nextInt();
        if (pos < 1 || pos > size + 1) {
            System.out.println("❌ Invalid position.");
            return;
        }
        Node newNode = new Node(val);
        if (pos == 1) {
            newNode.next = head;
            head = newNode;
        } else {
            Node temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;
            newNode.next = temp.next;
            temp.next = newNode;
        }
        size++;
        System.out.println("✅ Inserted at position " + pos);
        displayList();
    }

    public void deleteFromStart() {
        if (head == null) {
            System.out.println("❌ List is empty.");
            return;
        }
        head = head.next;
        size--;
        System.out.println("✅ Deleted from start.");
        displayList();
    }

    public void deleteFromEnd() {
        if (head == null) {
            System.out.println("❌ List is empty.");
            return;
        }
        if (head.next == null) {
            head = null;
        } else {
            Node temp = head;
            while (temp.next.next != null) temp = temp.next;
            temp.next = null;
        }
        size--;
        System.out.println("✅ Deleted from end.");
        displayList();
    }

    public void deleteFromPosition() {
        System.out.print("Enter position to delete: ");
        int pos = sc.nextInt();
        if (pos < 1 || pos > size) {
            System.out.println("❌ Invalid position.");
            return;
        }
        if (pos == 1) {
            head = head.next;
        } else {
            Node temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;
            temp.next = temp.next.next;
        }
        size--;
        System.out.println("✅ Deleted from position " + pos);
        displayList();
    }

    public void search() {
        System.out.print("Enter value to search: ");
        int val = sc.nextInt();
        Node temp = head;
        int pos = 1;
        while (temp != null) {
            if (temp.data == val) {
                System.out.println("✅ Found " + val + " at position " + pos);
                return;
            }
            temp = temp.next;
            pos++;
        }
        System.out.println("❌ Value not found.");
    }

    public void reverse() {
        Node prev = null, current = head, next;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        head = prev;
        System.out.println("✅ List Reversed.");
        displayList();
    }

    // Display method
    public void displayList() {
        System.out.print("HEAD ➜ ");
        Node temp = head;
        while (temp != null) {
            System.out.print("[" + temp.data + "] ➜ ");
            temp = temp.next;
        }
        System.out.println("NULL");
        System.out.println("📏 Size: " + size);
    }

    // Learning Mode Menu
    public void learningMenu() {
        System.out.println("\n🎓 Learning Mode - Choose an Operation:");
        System.out.println("1. Insert at Start");
        System.out.println("2. Insert at End");
        System.out.println("3. Insert at Position");
        System.out.println("4. Delete from Start");
        System.out.println("5. Delete from End");
        System.out.println("6. Delete from Position");
        System.out.println("7. Search");
        System.out.println("8. Reverse");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        int ch = sc.nextInt();

        switch (ch) {
            case 1 -> learningInsertAtStart();
            case 2 -> learningInsertAtEnd();
            case 3 -> learningInsertAtPosition();
            case 4 -> learningDeleteFromStart();
            case 5 -> learningDeleteFromEnd();
            case 6 -> learningDeleteFromPosition();
            case 7 -> learningSearch();
            case 8 -> learningReverse();
            case 0 -> {}
            default -> System.out.println("❌ Invalid choice!");
        }
    }

    // Learning Implementations
    private void learningInsertAtStart() {
        System.out.print("Enter value to insert at start: ");
        int value = sc.nextInt();

        System.out.println("🔍 Step-by-step:");
        displayList();

        Node newNode = new Node(value);
        System.out.println("🛠️ Step 1: Create new node with value " + value);
        pause();
        System.out.println("--> After Step 1:");
        displayTemporaryList(newNode, head);

        System.out.println("🛠️ Step 2: Point new node to head");
        newNode.next = head;
        pause();
        System.out.println("--> After Step 2:");
        displayTemporaryList(newNode, head);

        System.out.println("🛠️ Step 3: Move head to new node");
        head = newNode;
        size++;
        pause();
        System.out.println("--> After Step 3:");
        displayList();

        System.out.println("✅ Final List:");
        displayList();
        quiz("Time complexity of inserting at start?", "O(1)");
    }


    private void learningInsertAtEnd() {
        System.out.print("Enter value to insert at end: ");
        int value = sc.nextInt();

        System.out.println("🔍 Step-by-step:");
        displayList();

        Node newNode = new Node(value);
        System.out.println("🛠️ Step 1: Create new node with value " + value);
        pause();
        System.out.println("--> After Step 1:");
        displayTemporaryList(newNode, head);

        if (head == null) {
            System.out.println("🛠️ Step 2: List empty, set head to new node");
            head = newNode;
        } else {
            System.out.println("🛠️ Step 2: Traverse to end of list");
            Node temp = head;
            while (temp.next != null) temp = temp.next;
            pause();
            System.out.println("--> After Step 2:");
            displayTemporaryList(newNode, head);

            System.out.println("🛠️ Step 3: Link last node to new node");
            temp.next = newNode;
        }
        size++;
        pause();
        System.out.println("--> After Step 3:");
        displayList();

        System.out.println("✅ Final List:");
        displayList();
        quiz("Time complexity of inserting at end?", "O(n)");
    }


    private void learningInsertAtPosition() {
        System.out.print("Enter position: ");
        int pos = sc.nextInt();
        System.out.print("Enter value: ");
        int value = sc.nextInt();

        if (pos < 1 || pos > size + 1) {
            System.out.println("❌ Invalid position.");
            return;
        }

        Node newNode = new Node(value);
        System.out.println("🔍 Step-by-step:");
        displayList();

        System.out.println("🛠️ Step 1: Create new node with value " + value);
        pause();
        System.out.println("--> After Step 1:");
        displayTemporaryList(newNode, head);

        if (pos == 1) {
            System.out.println("🛠️ Step 2: Insert at start");
            newNode.next = head;
            head = newNode;
        } else {
            Node temp = head;
            System.out.println("🛠️ Step 2: Traverse to position " + (pos - 1));
            for (int i = 1; i < pos - 1; i++) temp = temp.next;
            pause();
            System.out.println("--> After Step 2:");
            displayTemporaryList(newNode, head);

            System.out.println("🛠️ Step 3: Insert node at position");
            newNode.next = temp.next;
            temp.next = newNode;
        }
        size++;
        pause();
        System.out.println("--> After Step 3:");
        displayList();

        System.out.println("✅ Final List:");
        displayList();
        quiz("Time complexity of insert at position?", "O(n)");
    }


    private void learningDeleteFromStart() {
        if (head == null) {
            System.out.println("❌ List empty.");
            return;
        }

        System.out.println("🔍 Step-by-step:");
        displayList();

        System.out.println("🛠️ Step 1: Move head to next node");
        pause();
        head = head.next;
        size--;
        System.out.println("--> After Step 1:");
        displayList();

        System.out.println("✅ Final List:");
        displayList();
        quiz("Time complexity of deleting from start?", "O(1)");
    }


    private void learningDeleteFromEnd() {
        if (head == null) {
            System.out.println("❌ List empty.");
            return;
        }

        System.out.println("🔍 Step-by-step:");
        displayList();

        if (head.next == null) {
            System.out.println("🛠️ Only one node. Delete head.");
            head = null;
        } else {
            System.out.println("🛠️ Traverse to second-last node");
            Node temp = head;
            while (temp.next.next != null) temp = temp.next;
            pause();
            System.out.println("--> After Step 1:");
            displayTemporaryList(temp, head);

            System.out.println("🛠️ Remove reference to last node");
            temp.next = null;
        }
        size--;
        pause();
        System.out.println("--> After Step 2:");
        displayList();

        System.out.println("✅ Final List:");
        displayList();
        quiz("Time complexity of deleting from end?", "O(n)");
    }


    private void learningDeleteFromPosition() {
        System.out.print("Enter position to delete: ");
        int pos = sc.nextInt();

        if (pos < 1 || pos > size) {
            System.out.println("❌ Invalid position.");
            return;
        }

        System.out.println("🔍 Step-by-step:");
        displayList();

        if (pos == 1) {
            System.out.println("🛠️ Step 1: Move head to next node");
            head = head.next;
        } else {
            Node temp = head;
            System.out.println("🛠️ Step 1: Traverse to position " + (pos - 1));
            for (int i = 1; i < pos - 1; i++) temp = temp.next;
            pause();
            System.out.println("--> After Step 1:");
            displayTemporaryList(temp, head);

            System.out.println("🛠️ Step 2: Remove node at position");
            temp.next = temp.next.next;
        }
        size--;
        pause();
        System.out.println("--> After Step 2:");
        displayList();

        System.out.println("✅ Final List:");
        displayList();
        quiz("Time complexity of deleting from position?", "O(n)");
    }


    private void learningSearch() {
        System.out.print("Enter value to search: ");
        int val = sc.nextInt();
        Node temp = head;
        int pos = 1;

        System.out.println("🔍 Step-by-step:");
        displayList();

        while (temp != null) {
            if (temp.data == val) {
                System.out.println("✅ Found at position " + pos);
                displayList();
                quiz("Time complexity of searching?", "O(n)");
                return;
            }
            temp = temp.next;
            pos++;
        }

        System.out.println("❌ Not found.");
        displayList();
    }


    private void learningReverse() {
        System.out.println("🔍 Step-by-step:");
        displayList();

        Node prev = null, current = head, next;

        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
            pause();
            System.out.println("--> After Step:");
            displayTemporaryList(prev, current);
        }

        head = prev;
        System.out.println("✅ Final List:");
        displayList();
        quiz("Time complexity of reversing a list?", "O(n)");
    }


    // Helpers
    private void pause() {
        try { Thread.sleep(1500); } catch (Exception e) {}
    }

    private void quiz(String question, String answer) {
        System.out.println("\n🎓 Quiz: " + question);
        System.out.print("Your answer: ");
        String userAnswer = sc.next();
        if (userAnswer.equalsIgnoreCase(answer)) {
            System.out.println("🎉 Correct!");
        } else {
            System.out.println("❌ Oops! Correct answer is: " + answer);
        }
    }
    private void displayTemporaryList(Node newHead, Node rest) {
        System.out.print("HEAD ➜ ");
        Node temp = newHead;
        while (temp != null) {
            System.out.print("[" + temp.data + "] ➜ ");
            temp = temp.next;
        }
        if (rest != null && newHead.next != rest) {
            // Make sure rest nodes are connected if not already
            temp = rest;
            while (temp != null) {
                System.out.print("[" + temp.data + "] ➜ ");
                temp = temp.next;
            }
        }
        System.out.println("NULL");
    }

}
