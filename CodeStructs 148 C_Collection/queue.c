#include <stdio.h>
#include "queue.h"

void initQueue(Queue* q) {
    q->front = 0;
    q->rear = -1;
}

int isQueueEmpty(Queue* q) {
    return q->front > q->rear;
}

int isQueueFull(Queue* q) {
    return q->rear == MAX - 1;
}

void enqueue(Queue* q, int value) {
    if (isQueueFull(q)) {
        printf("Queue Overflow\n");
        return;
    }
    q->data[++(q->rear)] = value;
}

int dequeue(Queue* q) {
    if (isQueueEmpty(q)) {
        printf("Queue Underflow\n");
        return -1;
    }
    return q->data[(q->front)++];
}

void printQueue(Queue* q) {
    if (isQueueEmpty(q)) {
        printf("Queue is empty\n");
        return;
    }
    for (int i = q->front; i <= q->rear; i++) {
        printf("%d ", q->data[i]);
    }
    printf("\n");
}
