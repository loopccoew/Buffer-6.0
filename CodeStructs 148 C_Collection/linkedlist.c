#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "linkedlist.h"

void linkedlist_insert(LinkedList* list, void* val) {
    LinkedListNode* new_node = (LinkedListNode*)malloc(sizeof(LinkedListNode));
    if (!new_node) {
        printf("Memory allocation failed!\n");
        return;
    }
    new_node->data = val;
    new_node->next = list->head;
    list->head = new_node;
    list->size++;
}

void linkedlist_remove(LinkedList* list, void* val) {
    LinkedListNode* current = list->head;
    LinkedListNode* prev = NULL;

    while (current != NULL) {
        if (current->data == val) {
            if (prev != NULL) {
                prev->next = current->next;
            } else {
                list->head = current->next;
            }
            free(current);
            list->size--;
            return;
        }
        prev = current;
        current = current->next;
    }
}

void linkedlist_print(LinkedList* list) {
    LinkedListNode* current = list->head;
    while (current != NULL) {
        printf("%s\n", (char*)current->data);
        current = current->next;
    }
}

LinkedList* create_linkedlist() {
    LinkedList* list = (LinkedList*)malloc(sizeof(LinkedList));
    if (!list) {
        printf("Memory allocation failed!\n");
        return NULL;
    }
    list->head = NULL;
    list->size = 0;
    list->insert = linkedlist_insert;
    list->remove = linkedlist_remove;
    list->print = linkedlist_print;
    return list;
}

void fill_linkedlist(LinkedList* list) {
    char buffer[100];
    printf("Enter elements for LinkedList (type 'end' to stop):\n");
    while (1) {
        scanf("%s", buffer);
        if (strcmp(buffer, "end") == 0) break;
        char* value = strdup(buffer);  // allocate memory and copy input
        if (value == NULL) {
            printf("Memory allocation for string failed!\n");
            return;
        }
        list->insert(list, value);
    }
}
void linkedlist_free(LinkedList* list) {
    LinkedListNode* current = list->head;
    while (current) {
        LinkedListNode* next = current->next;
        free(current->data); // Free the string stored in 'data'
        free(current); // Free the node itself
        current = next;
    }
    free(list); // Finally, free the list structure
}

