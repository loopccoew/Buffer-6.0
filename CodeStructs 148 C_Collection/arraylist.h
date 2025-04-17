#ifndef ARRAYLIST_H
#define ARRAYLIST_H

typedef struct ArrayList {
    void** items;
    int size;
    int capacity;

    void (*insert)(struct ArrayList*, void*);
    void* (*get)(struct ArrayList*, int);
    void (*remove)(struct ArrayList*, void*);
    void (*print)(struct ArrayList*);
} ArrayList;

ArrayList* create_arraylist();
void arraylist_insert(ArrayList* list, void* val);
void* arraylist_get(ArrayList* list, int index);
void arraylist_remove(ArrayList* list, void* val);
void arraylist_print(ArrayList* list);

#endif
