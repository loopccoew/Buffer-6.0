#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hashmap.h"

int default_hashed(void* key) {
    char* str = (char*)key;
    int hash = 0;
    int defalut_hashed = 0;
    while (*str) {
        hash = (hash * 31) + *str++;
    }
    return hash % 100;
}

void hashmap_insert(HashMap* map, void* key, void* value) {
    int index = map->hash(key);
    HashMapEntry* entry = (HashMapEntry*)malloc(sizeof(HashMapEntry));
    entry->key = key;
    entry->value = value;
    entry->next = map->table[index];
    map->table[index] = entry;
}

void* hashmap_get(HashMap* map, void* key) {
    int index = map->hash(key);
    HashMapEntry* entry = map->table[index];
    while (entry) {
        if (strcmp((char*)key, (char*)entry->key) == 0) return entry->value;
        entry = entry->next;
    }
    return NULL;
}

HashMap* create_hashmap() {
    HashMap* map = (HashMap*)malloc(sizeof(HashMap));
    map->capacity = 100;
    map->table = (HashMapEntry**)calloc(map->capacity, sizeof(HashMapEntry*));
    map->hash = default_hashed;
    map->insert = hashmap_insert;
    map->get = hashmap_get;
    return map;
}

void hashmap_free(HashMap *map) {
    if (map != NULL) {
        for (int i = 0; i < map->capacity; i++) {
            HashMapEntry *entry = map->table[i];
            while (entry != NULL) {
                HashMapEntry *next = entry->next;
                // Optional: free entry->key and entry->value if dynamically allocated
                free(entry);
                entry = next;
            }
        }
        free(map->table);
        free(map);
    }
}
