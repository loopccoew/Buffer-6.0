#ifndef LINKEDLIST_H
#define LINKEDLIST_H

typedef struct LinkedListNode {
    void* data;
    struct LinkedListNode* next;
} LinkedListNode;

typedef struct LinkedList {
    LinkedListNode* head;
    int size;
    void (*insert)(struct LinkedList*, void*);
    void (*remove)(struct LinkedList*, void*);
    void (*print)(struct LinkedList*);
} LinkedList;

// Function declarations
LinkedList* create_linkedlist();
void linkedlist_insert(LinkedList* list, void* val);
void linkedlist_remove(LinkedList* list, void* val);
void linkedlist_print(LinkedList* list);
void fill_linkedlist(LinkedList* list);
void linkedlist_free(LinkedList* list);

#endif
