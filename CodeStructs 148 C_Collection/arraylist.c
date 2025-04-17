#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "arraylist.h"

#define INITIAL_CAPACITY 10
#define MAX_INPUT_LENGTH 100

ArrayList* create_arraylist() {
    ArrayList* list = malloc(sizeof(ArrayList));
    list->items = malloc(sizeof(void*) * INITIAL_CAPACITY);
    list->size = 0;
    list->capacity = INITIAL_CAPACITY;

    // Assign function pointers
    list->insert = arraylist_insert;
    list->get = arraylist_get;
    list->remove = arraylist_remove;
    list->print = arraylist_print;

    return list;
}

void arraylist_insert(ArrayList* list, void* val) {
    if (list->size == list->capacity) {
        list->capacity *= 2;
        list->items = realloc(list->items, sizeof(void*) * list->capacity);
    }
    list->items[list->size++] = val;
}

void* arraylist_get(ArrayList* list, int index) {
    if (index >= 0 && index < list->size) {
        return list->items[index];
    }
    return NULL;
}

void arraylist_remove(ArrayList* list, void* val) {
    for (int i = 0; i < list->size; i++) {
        if (list->items[i] == val) {
            for (int j = i; j < list->size - 1; j++) {
                list->items[j] = list->items[j + 1];
            }
            list->size--;
            break;
        }
    }
}

void arraylist_print(ArrayList* list) {
    for (int i = 0; i < list->size; i++) {
        printf("%s ", (char*)list->items[i]); // assuming char* for demo
    }
    printf("\n");
}
