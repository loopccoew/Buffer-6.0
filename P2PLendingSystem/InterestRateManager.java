package dsa_modules;

import java.util.Collections;
import java.util.PriorityQueue;

//2. Heap for Interest Rate Adjustment
public class InterestRateManager {
PriorityQueue<Double> borrowerHeap = new PriorityQueue<>(Collections.reverseOrder()); // Max-Heap
PriorityQueue<Double> lenderHeap = new PriorityQueue<>(); // Min-Heap

public void addBorrowerRate(double rate) {
   borrowerHeap.offer(rate);
}

void addLenderRate(double rate) {
   lenderHeap.offer(rate);
}

double getMaxBorrowerRate() {
   return borrowerHeap.peek();
}

double getMinLenderRate() {
   return lenderHeap.peek();
}
}
