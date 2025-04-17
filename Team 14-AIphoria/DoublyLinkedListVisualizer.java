import java.util.Scanner;

class DNode {
    int data;
    DNode next, prev;

    public DNode(int data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

public class DoublyLinkedListVisualizer {
    DNode head = null;
    int size = 0;
    Scanner sc = new Scanner(System.in);

    public void doublyLinkedListMenu() {
        while (true) {
            System.out.println("\n📘 Doubly Linked List Menu:");
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
        DNode newNode = new DNode(val);
        if (head != null) head.prev = newNode;
        newNode.next = head;
        head = newNode;
        size++;
        System.out.println("✅ Inserted at start.");
        displayList();
    }

    public void insertAtEnd() {
        System.out.print("Enter value to insert at end: ");
        int val = sc.nextInt();
        DNode newNode = new DNode(val);
        if (head == null) {
            head = newNode;
        } else {
            DNode temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newNode;
            newNode.prev = temp;
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
        DNode newNode = new DNode(val);
        if (pos == 1) {
            if (head != null) head.prev = newNode;
            newNode.next = head;
            head = newNode;
        } else {
            DNode temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;
            newNode.next = temp.next;
            if (temp.next != null) temp.next.prev = newNode;
            temp.next = newNode;
            newNode.prev = temp;
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
        if (head != null) head.prev = null;
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
            DNode temp = head;
            while (temp.next != null) temp = temp.next;
            temp.prev.next = null;
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
            if (head != null) head.prev = null;
        } else {
            DNode temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;
            temp.next = temp.next.next;
            if (temp.next != null) temp.next.prev = temp;
        }
        size--;
        System.out.println("✅ Deleted from position " + pos);
        displayList();
    }

    public void search() {
        System.out.print("Enter value to search: ");
        int val = sc.nextInt();
        DNode temp = head;
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
        if (head == null) return;
        DNode temp = null;
        DNode current = head;
        while (current != null) {
            temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            current = current.prev;
        }
        if (temp != null) head = temp.prev;
        System.out.println("✅ List Reversed.");
        displayList();
    }

    // Display method
    public void displayList() {
        System.out.print("HEAD ➜ ");
        DNode temp = head;
        while (temp != null) {
            System.out.print("[" + temp.data + "] <-> ");
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

    // Learning Implementations for Doubly Linked List
    private void learningInsertAtStart() {
        System.out.print("Enter value to insert at start: ");
        int value = sc.nextInt();

        System.out.println("🔍 Step-by-step:");
        displayList(); // Display the current list state before starting the operation

        // Step 1: Create new node
        DNode newNode = new DNode(value);
        System.out.println("🛠️ Step 1: Create new node with value " + value);
        pause(); // Pause to let user review
        System.out.println("--> After Step 1:");
        displayTemporaryList(newNode, head); // Temporary list with the new node for visual feedback

        // Step 2: Insert new node at the start
        System.out.println("🛠️ Step 2: Insert the new node at the start.");
        newNode.next = head;
        if (head != null) head.prev = newNode;
        head = newNode;
        size++;

        pause(); // Pause for a moment to show the final changes
        System.out.println("--> After Step 2:");
        displayList(); // Show the list after the operation

        System.out.println("✅ Final List after insertion:");
        displayList(); // Final display of the list
        quiz("Time complexity of inserting at start?", "O(1)"); // Optional quiz for the user
    }

    private void learningInsertAtEnd() {
        System.out.print("Enter value to insert at end: ");
        int value = sc.nextInt();

        System.out.println("🔍 Step-by-step:");
        displayList(); // Display the current list state before starting the operation

        // Step 1: Create new node
        DNode newNode = new DNode(value);
        System.out.println("🛠️ Step 1: Create new node with value " + value);
        pause(); // Pause to let user review
        System.out.println("--> After Step 1:");
        displayTemporaryList(newNode, head); // Temporary list with the new node for visual feedback

        // Step 2: Check if the list is empty
        if (head == null) {
            System.out.println("🛠️ Step 2: List is empty, set head to new node.");
            head = newNode;
        } else {
            System.out.println("🛠️ Step 2: Traverse to the end of the list to find the last node.");
            DNode temp = head;
            while (temp.next != null) temp = temp.next;  // Traverse till the last node
            pause();  // Pause to let the user follow the traversal
            System.out.println("--> After Step 2:");
            displayTemporaryList(newNode, head);  // Show the updated state of the list as we reach the last node

            // Step 3: Link the last node's next pointer to the new node
            System.out.println("🛠️ Step 3: Link last node's next pointer to new node.");
            temp.next = newNode;
            newNode.prev = temp;  // Link the new node's prev pointer to the last node
        }

        size++;  // Increment the list size
        pause();  // Pause for a moment to show the final changes
        System.out.println("--> After Step 3:");
        displayList();  // Display the updated list after inserting at the end

        System.out.println("✅ Final List after insertion:");
        displayList();  // Final display of the list after completing the operation
        quiz("Time complexity of inserting at end?", "O(n)");  // Optional quiz for the user
    }

    // Learning implementation for Insert at Position
    private void learningInsertAtPosition() {
        System.out.print("Enter position to insert at: ");
        int pos = sc.nextInt();
        System.out.print("Enter value to insert: ");
        int value = sc.nextInt();

        System.out.println("🔍 Step-by-step:");
        displayList();  // Show the current state of the list

        // Step 1: Create new node
        DNode newNode = new DNode(value);
        System.out.println("🛠️ Step 1: Create a new node with value " + value);
        pause();
        System.out.println("--> After Step 1:");
        displayTemporaryList(newNode, head);  // Show the state after creating the new node

        // Step 2: Check if the position is valid
        if (pos < 1 || pos > size + 1) {
            System.out.println("❌ Invalid position.");
            return;
        }
        System.out.println("🛠️ Step 2: Check if position is valid.");
        pause();

        // Step 3: Traverse to the position (if needed)
        if (pos == 1) {
            System.out.println("🛠️ Step 3: Position is at the start, insert at the start.");
            newNode.next = head;
            if (head != null) head.prev = newNode;
            head = newNode;
        } else {
            DNode temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;
            System.out.println("🛠️ Step 3: Traverse to position " + (pos - 1));
            pause();
            System.out.println("--> After Step 3:");
            displayTemporaryList(newNode, head);  // Display the list while traversing to position

            // Step 4: Insert the new node at the specified position
            newNode.next = temp.next;
            if (temp.next != null) temp.next.prev = newNode;
            temp.next = newNode;
            newNode.prev = temp;
        }

        size++;
        System.out.println("✅ Insertion at position " + pos + " completed.");
        displayList();
        quiz("Time complexity of inserting at a specific position?", "O(n)");
    }

    // Learning implementation for Delete from Start
    private void learningDeleteFromStart() {
        System.out.println("🔍 Step-by-step:");
        displayList();  // Show the current state of the list

        // Step 1: Check if the list is empty
        if (head == null) {
            System.out.println("❌ List is empty.");
            return;
        }
        System.out.println("🛠️ Step 1: Check if the list is empty.");
        pause();

        // Step 2: Remove the first node
        System.out.println("🛠️ Step 2: Remove the first node.");
        head = head.next;
        if (head != null) head.prev = null;

        size--;
        System.out.println("✅ Deleted from start.");
        displayList();
        quiz("Time complexity of deleting from the start?", "O(1)");
    }

    // Learning implementation for Delete from End
    private void learningDeleteFromEnd() {
        System.out.println("🔍 Step-by-step:");
        displayList();  // Show the current state of the list

        // Step 1: Check if the list is empty
        if (head == null) {
            System.out.println("❌ List is empty.");
            return;
        }
        System.out.println("🛠️ Step 1: Check if the list is empty.");
        pause();

        // Step 2: Traverse to the last node
        DNode temp = head;
        while (temp.next != null) temp = temp.next;
        System.out.println("🛠️ Step 2: Traverse to the last node.");
        pause();

        // Step 3: Remove the last node
        System.out.println("🛠️ Step 3: Remove the last node.");
        temp.prev.next = null;

        size--;
        System.out.println("✅ Deleted from end.");
        displayList();
        quiz("Time complexity of deleting from the end?", "O(n)");
    }

    // Learning implementation for Delete from Position
    private void learningDeleteFromPosition() {
        System.out.print("Enter position to delete from: ");
        int pos = sc.nextInt();

        System.out.println("🔍 Step-by-step:");
        displayList();  // Show the current state of the list

        // Step 1: Check if the position is valid
        if (pos < 1 || pos > size) {
            System.out.println("❌ Invalid position.");
            return;
        }
        System.out.println("🛠️ Step 1: Check if position is valid.");
        pause();

        // Step 2: Traverse to the position
        DNode temp = head;
        for (int i = 1; i < pos; i++) temp = temp.next;
        System.out.println("🛠️ Step 2: Traverse to position " + pos);
        pause();

        // Step 3: Delete the node at the specified position
        System.out.println("🛠️ Step 3: Delete the node at position " + pos);
        if (temp.prev != null) temp.prev.next = temp.next;
        if (temp.next != null) temp.next.prev = temp.prev;

        size--;
        System.out.println("✅ Deleted from position " + pos);
        displayList();
        quiz("Time complexity of deleting at a specific position?", "O(n)");
    }

    // Learning implementation for Search
    private void learningSearch() {
        System.out.print("Enter value to search: ");
        int value = sc.nextInt();

        System.out.println("🔍 Step-by-step:");
        displayList();  // Show the current state of the list

        // Step 1: Traverse the list to search for the value
        DNode temp = head;
        int pos = 1;
        while (temp != null) {
            if (temp.data == value) {
                System.out.println("✅ Found " + value + " at position " + pos);
                return;
            }
            temp = temp.next;
            pos++;
        }

        System.out.println("❌ Value not found.");
        quiz("Time complexity of searching in a list?", "O(n)");
    }

    // Learning implementation for Reverse
    private void learningReverse() {
        System.out.println("🔍 Step-by-step:");
        displayList();  // Show the current state of the list

        // Step 1: Check if the list is empty
        if (head == null) {
            System.out.println("❌ List is empty.");
            return;
        }
        System.out.println("🛠️ Step 1: Check if the list is empty.");
        pause();

        // Step 2: Traverse and reverse the pointers
        System.out.println("🛠️ Step 2: Reverse the pointers of each node.");
        DNode temp = null;
        DNode current = head;
        while (current != null) {
            temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            current = current.prev;
        }
        if (temp != null) head = temp.prev;

        System.out.println("✅ List reversed.");
        displayList();
        quiz("Time complexity of reversing the list?", "O(n)");
    }


    // Helper method to pause the execution (just a delay for user to absorb the information)
    private void pause() {
        try {
            Thread.sleep(2000);  // Pause for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Helper method to display the temporary list during each step (show the changes clearly)
    private void displayTemporaryList(DNode newNode, DNode head) {
        DNode temp = head;
        System.out.print("HEAD ➜ ");
        while (temp != null) {
            System.out.print("[" + temp.data + "] <-> ");
            temp = temp.next;
        }
        if (newNode != null) {
            System.out.print("[" + newNode.data + "] <-> ");
        }
        System.out.println("NULL");
    }

    // Method to quiz the user on time complexity
    private void quiz(String question, String answer) {
        System.out.println("❓ Quiz Time: " + question);
        System.out.print("Answer: ");
        String userAnswer = sc.next();
        if (userAnswer.equalsIgnoreCase(answer)) {
            System.out.println("✅ Correct! Well done.");
        } else {
            System.out.println("❌ Incorrect. The correct answer is: " + answer);
        }
    }


}
