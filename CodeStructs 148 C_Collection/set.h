#ifndef SET_H
#define SET_H

typedef struct SetEntry {
    void* value;
    struct SetEntry* next;
} SetEntry;

typedef struct Set {
    SetEntry** table;
    int capacity;
    int (*hash)(void*);
    int (*equals)(void*, void*);
    void (*insert)(struct Set*, void*);
    int (*contains)(struct Set*, void*);
    void (*remove)(struct Set*, void*);
    void (*free)(struct Set*);
} Set;

int default_hash(void* key);
int default_equals(void* key1, void* key2);
Set* create_set();
void set_insert(Set* set, void* value);
int set_contains(Set* set, void* value);
void set_remove(Set* set, void* value);
void set_free(Set* set);

#endif
