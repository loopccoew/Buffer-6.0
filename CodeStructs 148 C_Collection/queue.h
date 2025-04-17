#ifndef QUEUE_H
#define QUEUE_H

#define MAX 100

typedef struct {
    int data[MAX];
    int front;
    int rear;
} Queue;

void initQueue(Queue* q);
int  isQueueEmpty(Queue* q);
int isQueueFull(Queue* q);
void enqueue(Queue* q, int value);
int dequeue(Queue* q);
void printQueue(Queue* q);

#endif
