#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "linkedlist.h"  // Assuming you have a linked list implementation
#include "hybridcollection.h"

// Helper function to create a new node
static HybridNode* create_node(const char* key, const char* value) {
    HybridNode* node = (HybridNode*)malloc(sizeof(HybridNode));
    node->key = strdup(key);
    node->value = value ? strdup(value) : NULL;
    node->next = NULL;
    return node;
}

// Insert a new key-value pair into the hybrid collection
static void insert(HybridCollection* hc, const char* key, const char* value) {
    HybridNode* node = create_node(key, value);
    node->next = hc->head;
    hc->head = node;
}

// Check if the key exists in the hybrid collection
static int contains(HybridCollection* hc, const char* key) {
    HybridNode* current = hc->head;
    while (current) {
        if (strcmp(current->key, key) == 0) {
            return 1; // Key found
        }
        current = current->next;
    }
    return 0; // Key not found
}

// Get the value associated with the key in the hybrid collection
static char* get(HybridCollection* hc, const char* key) {
    HybridNode* current = hc->head;
    while (current) {
        if (strcmp(current->key, key) == 0) {
            return current->value; // Return the value
        }
        current = current->next;
    }
    return NULL; // Key not found
}

// Remove a key-value pair from the hybrid collection
static void remove_key(HybridCollection* hc, const char* key) {
    HybridNode* current = hc->head;
    HybridNode* prev = NULL;

    while (current) {
        if (strcmp(current->key, key) == 0) {
            if (prev) {
                prev->next = current->next;
            } else {
                hc->head = current->next;
            }
            free(current->key);
            if (current->value) {
                free(current->value);
            }
            free(current);
            return;
        }
        prev = current;
        current = current->next;
    }
}

// Free all memory used by the hybrid collection
static void free_collection(HybridCollection* hc) {
    HybridNode* current = hc->head;
    while (current) {
        free(current->key); // Free the key memory
        if (current->value) { // Free the value memory if it's not NULL
            free(current->value);
        }
        HybridNode* temp = current;
        current = current->next;
        free(temp); // Free the node itself
    }
    free(hc); // Finally, free the collection itself
}

// Print all the key-value pairs in the hybrid collection
static void print_collection(HybridCollection* hc) {
    HybridNode* current = hc->head;
    while (current) {
        printf("Key: %s, Value: %s\n", current->key, current->value ? current->value : "NULL");
        current = current->next;
    }
}

// Create a hybrid collection based on the specified type
HybridCollection* create_hybrid_collection(CollectionType type) {
    HybridCollection* hc = (HybridCollection*)malloc(sizeof(HybridCollection));
    hc->type = type;
    hc->head = NULL;
    hc->insert = insert;
    hc->contains = contains;
    hc->get = get;
    hc->remove = remove_key;
    hc->free = free_collection;
    hc->print = print_collection;
    return hc;
}
