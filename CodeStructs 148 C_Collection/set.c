#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "set.h"

// Default hash function: for strings, simple hash based on the sum of ASCII values
int default_hash(void* key) {
    char* str = (char*)key;
    int hash = 0;
    while (*str) {
        hash = (hash * 31) + *str++;
    }
    return hash % 100; // Assume table size of 100 for simplicity
}

// Default equality function: checks if two keys (strings) are equal
int default_equals(void* key1, void* key2) {
    return strcmp((char*)key1, (char*)key2) == 0;
}

// Insert value into set (no duplicates allowed)
void set_insert(Set* set, void* value) {
    int index = set->hash(value);
    SetEntry* entry = set->table[index];
    while (entry) {
        if (set->equals(entry->value, value)) {
            return; // Value already exists in set, do not insert
        }
        entry = entry->next;
    }

    // Insert the new value at the beginning of the list (chain)
    entry = (SetEntry*)malloc(sizeof(SetEntry));
    entry->value = value;
    entry->next = set->table[index];
    set->table[index] = entry;
}

// Check if the set contains the value
int set_contains(Set* set, void* value) {
    int index = set->hash(value);
    SetEntry* entry = set->table[index];
    while (entry) {
        if (set->equals(entry->value, value)) {
            return 1; // Value found
        }
        entry = entry->next;
    }
    return 0; // Value not found
}

// Remove a value from the set
void set_remove(Set* set, void* value) {
    int index = set->hash(value);
    SetEntry* entry = set->table[index];
    SetEntry* prev = NULL;

    while (entry) {
        if (set->equals(entry->value, value)) {
            if (prev) {
                prev->next = entry->next;
            } else {
                set->table[index] = entry->next;
            }
            free(entry);
            return;
        }
        prev = entry;
        entry = entry->next;
    }
}

// Free all memory allocated for the set
void set_free(Set* set) {
    for (int i = 0; i < set->capacity; i++) {
        SetEntry* entry = set->table[i];
        while (entry) {
            SetEntry* next = entry->next;
            free(entry);
            entry = next;
        }
    }
    free(set->table);
    free(set);
}

// Create a new set
Set* create_set() {
    Set* set = (Set*)malloc(sizeof(Set));
    set->capacity = 100;
    set->table = (SetEntry**)calloc(set->capacity, sizeof(SetEntry*));
    set->hash = default_hash;
    set->equals = default_equals;
    set->insert = set_insert;
    set->contains = set_contains;
    set->remove = set_remove;
    set->free = set_free;
    return set;
}
