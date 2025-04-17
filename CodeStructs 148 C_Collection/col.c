#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "arraylist.h"
#include "linkedlist.h"
#include "stack.h"
#include "queue.h"
#include "hashmap.h"
#include "set.h"
#include "hybridcollection.h"
#include "utility.h"
#define MAX_INPUT_LENGTH 100

void menu(HybridCollection* hc) {
    int choice;
    char key[100], value[100];

    do {
        printf("\n--- Hybrid Collection Menu ---\n");
        printf("1. Insert\n");
        printf("2. Search\n");
        if (hc->type == MAP) {
            printf("3. Get Value by Key\n");
            printf("4. Remove\n");
            printf("5. Print Collection\n");
            printf("6. Exit\n");
        } else if (hc->type == SET) {
            printf("3. Remove\n");
            printf("4. Print Collection\n");
            printf("5. Exit\n");
        } else { // LIST
            printf("3. Print Collection\n");
            printf("4. Exit\n");
        }

        printf("Enter your choice: ");
        scanf("%d", &choice);
        getchar(); // Clear newline buffer

        switch (choice) {
            case 1:
                printf("Enter key: ");
                fgets(key, 100, stdin); key[strcspn(key, "\n")] = '\0';
                if (hc->type == MAP) {
                    printf("Enter value: ");
                    fgets(value, 100, stdin); value[strcspn(value, "\n")] = '\0';
                    hc->insert(hc, key, value);
                } else {
                    hc->insert(hc, key, NULL);
                }
                break;

            case 2:
                printf("Enter key to search: ");
                fgets(key, 100, stdin); key[strcspn(key, "\n")] = '\0';
                if (hc->contains(hc, key)) {
                    printf("Key '%s' exists.\n", key);
                } else {
                    printf("Key '%s' does not exist.\n", key);
                }
                break;

            case 3:
                if (hc->type == MAP) {
                    printf("Enter key to get value: ");
                    fgets(key, 100, stdin); key[strcspn(key, "\n")] = '\0';
                    char* result = hc->get(hc, key);
                    if (result) {
                        printf("Value: %s\n", result);
                    } else {
                        printf("Key not found.\n");
                    }
                } else if (hc->type == SET) {
                    printf("Enter key to remove: ");
                    fgets(key, 100, stdin); key[strcspn(key, "\n")] = '\0';
                    hc->remove(hc, key);
                    printf("Removed if present.\n");
                } else if (hc->type == LIST) {
                    hc->print(hc);
                }
                break;

            case 4:
                if (hc->type == MAP) {
                    printf("Enter key to remove: ");
                    fgets(key, 100, stdin); key[strcspn(key, "\n")] = '\0';
                    hc->remove(hc, key);
                    printf("Removed if present.\n");
                } else if (hc->type == SET) {
                    hc->print(hc);
                }
                break;

            case 5:
                if (hc->type == MAP || hc->type == SET) {
                    hc->print(hc);
                }
                break;

            case 6:
                if (hc->type == MAP) {
                    printf("Exiting...\n");
                    break;
                }

            default:
                if ((hc->type == MAP && choice == 6) || (hc->type == SET && choice == 5) || (hc->type == LIST && choice == 4)) break;
                printf("Invalid choice!\n");
        }

    } while (!((hc->type == MAP && choice == 6) || (hc->type == SET && choice == 5) || (hc->type == LIST && choice == 4)));

    hc->free(hc);
}


int main() {
    ArrayList* arrList = create_arraylist();
    char input[MAX_INPUT_LENGTH];
    int n;

    // Ask user how many strings they want to insert
    printf("How many strings do you want to insert? ");
    scanf("%d", &n);
    getchar();  // to consume the newline character after reading the integer

    // Take user input for arraylist
    for (int i = 0; i < n; i++) {
        printf("Enter string %d: ", i + 1);
        fgets(input, MAX_INPUT_LENGTH, stdin);

        // Remove the newline character if present
        input[strcspn(input, "\n")] = '\0';

        // Insert into ArrayList
        arrList->insert(arrList, strdup(input));
    }

    // Print the list
    printf("List: ");
    arrList->print(arrList);

    // Example: Removing an item (optional)
    printf("Enter the index of the item you want to remove: ");
    int remove_index;
    scanf("%d", &remove_index);
    if (remove_index >= 0 && remove_index < arrList->size) {
        arrList->remove(arrList, arrList->items[remove_index]);
        printf("After removing item at index %d: ", remove_index);
        arrList->print(arrList);
    }

    // Get an element (optional)
    printf("Enter the index to retrieve an item: ");
    int get_index;
    scanf("%d", &get_index);
    if (get_index >= 0 && get_index < arrList->size) {
        char* item = (char*) arrList->get(arrList, get_index);
        printf("Item at index %d: %s\n", get_index, item);
    }

    // Cleanup
    for (int i = 0; i < arrList->size; i++) {
        free(arrList->items[i]);
    }
    free(arrList->items);
    free(arrList);

    LinkedList* list = create_linkedlist();

    // Fill the linked list with user input
    fill_linkedlist(list);

    // Print the linked list
    printf("\nContents of the LinkedList:\n");
    list->print(list);

    // Cleanup allocated memory (remember to free each item in the linked list)
    LinkedListNode* current = list->head;
    while (current != NULL) {
        LinkedListNode* next = current->next;
        free(current->data);  // Free the string stored in the node
        free(current);
        current = next;
    }
    free(list);

    Stack stack;
    initStack(&stack);
    int  val; 
    printf("\nEnter number of elements for Stack: ");
    scanf("%d", &n);
    printf("Enter %d elements:\n", n);
    for (int i = 0; i < n; i++) {
        scanf("%d", &val);
        push(&stack, val);
    }
    printf("\nStack (Top to Bottom):\n");
    printStack(&stack);

    Queue q;
    initQueue(&q);

    printf("\nEnter number of elements to enqueue in Queue: ");
    scanf("%d", &n);

    printf("Enter %d integers to enqueue:\n", n);
    for (int i = 0; i < n; i++) {
        scanf("%d", &val);
        enqueue(&q, val);
    }

    printf("\nQueue (Front to Rear):\n");
    printQueue(&q);

    int d;
    printf("\nHow many elements do you want to dequeue? ");
    scanf("%d", &d);

    for (int i = 0; i < d; i++) {
        int removed = dequeue(&q);
        if (removed != -1) {
            printf("Dequeued: %d\n", removed);
        }
    }

    printf("\nQueue after dequeue operations:\n");
    printQueue(&q);

    HashMap* map = create_hashmap();
    Set* mySet = create_set();

    
    printf("Enter number of key-value pairs to insert into the HashMap: ");
    scanf("%d", &n);
    getchar();  // clear newline from buffer

    for (int i = 0; i < n; i++) {
        char key[100], value[100];
        printf("Enter key %d: ", i + 1);
        fgets(key, sizeof(key), stdin);
        key[strcspn(key, "\n")] = '\0'; // remove newline

        printf("Enter value for '%s': ", key);
        fgets(value, sizeof(value), stdin);
        value[strcspn(value, "\n")] = '\0';

        map->insert(map, strdup(key), strdup(value));
    }

    printf("\nRetrieving values from HashMap:\n");
    for (int i = 0; i < n; i++) {
        char key[100];
        printf("Enter key to retrieve: ");
        fgets(key, sizeof(key), stdin);
        key[strcspn(key, "\n")] = '\0';

        char* result = (char*)map->get(map, key);
        if (result)
            printf("Value: %s\n", result);
        else
            printf("Key '%s' not found.\n", key);
    }

    hashmap_free(map);

    printf("\n--- Set Operations ---\n");
    int m;
    printf("Enter number of elements to insert into the Set: ");
    scanf("%d", &m);
    getchar();

    for (int i = 0; i < m; i++) {
        char value[100];
        printf("Enter value %d: ", i + 1);
        fgets(value, sizeof(value), stdin);
        value[strcspn(value, "\n")] = '\0';

        mySet->insert(mySet, strdup(value));
    }

    // Check for values
    char query[100];
    printf("Enter value to check in the set: ");
    fgets(query, sizeof(query), stdin);
    query[strcspn(query, "\n")] = '\0';

    if (mySet->contains(mySet, query)) {
        printf("Set contains %s\n", query);
    } else {
        printf("Set does not contain %s\n", query);
    }

    // Remove value
    printf("Enter value to remove from set: ");
    fgets(query, sizeof(query), stdin);
    query[strcspn(query, "\n")] = '\0';

    mySet->remove(mySet, query);

    if (mySet->contains(mySet, query)) {
        printf("Set still contains %s\n", query);
    } else {
        printf("Set does not contain %s anymore\n", query);
    }

    mySet->free(mySet);
    
    
    int userChoice;
    printf("Choose Hybrid Collection Type:\n");
    printf("1. LIST\n");
    printf("2. SET\n");
    printf("3. MAP\n");
    printf("Enter choice: ");
    scanf("%d", &userChoice);

    HybridCollection* hc = NULL;
    if (userChoice == 1) {
        hc = create_hybrid_collection(LIST);
    } else if (userChoice == 2) {
        hc = create_hybrid_collection(SET);
    } else if (userChoice == 3) {
        hc = create_hybrid_collection(MAP);
    } else {
        printf("Invalid choice. Exiting.\n");
        return 0;
    }

    menu(hc);
    // Get array from user
  

    

    int x, y;
    printf("Enter two integers:\n");
    printf("x: ");
    scanf("%d", &x);
    printf("y: ");
    scanf("%d", &y);

    printf("Minimum: %d\n", find_min(x, y));
    printf("Maximum: %d\n", find_max(x, y));

    int result = compare_ints(x, y);
    if (result == -1) {
        printf("%d is less than %d\n", x, y);
    } else if (result == 1) {
        printf("%d is greater than %d\n", x, y);
    } else {
        printf("%d is equal to %d\n", x, y);
    }

    return 0;
}