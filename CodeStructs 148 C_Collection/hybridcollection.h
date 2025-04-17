#ifndef HYBRIDCOLLECTION_H
#define HYBRIDCOLLECTION_H

#include "linkedlist.h"

typedef enum {
    LIST,
    SET,
    MAP
} CollectionType;

// Use a different name than "Node" to avoid conflicts
typedef struct HybridNode {
    char* key;
    char* value;
    struct HybridNode* next;
} HybridNode;

typedef struct HybridCollection {
    CollectionType type;
    HybridNode* head;

    void (*insert)(struct HybridCollection*, const char*, const char*);
    int (*contains)(struct HybridCollection*, const char*);
    char* (*get)(struct HybridCollection*, const char*);
    void (*remove)(struct HybridCollection*, const char*);
    void (*free)(struct HybridCollection*);
    void (*print)(struct HybridCollection*);
} HybridCollection;

HybridCollection* create_hybrid_collection(CollectionType type);

#endif
