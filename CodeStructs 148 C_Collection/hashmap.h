#ifndef HASHMAP_H
#define HASHMAP_H

typedef struct HashMapEntry {
    void* key;
    void* value;
    struct HashMapEntry* next;
} HashMapEntry;

typedef struct HashMap {
    HashMapEntry** table;
    int capacity;
    int (*hash)(void*);
    void (*insert)(struct HashMap*, void*, void*);
    void* (*get)(struct HashMap*, void*);
} HashMap;

int default_hash(void* key);
HashMap* create_hashmap();
void hashmap_insert(HashMap* map, void* key, void* value);
void* hashmap_get(HashMap* map, void* key);
void hashmap_free(HashMap* map);

#endif
