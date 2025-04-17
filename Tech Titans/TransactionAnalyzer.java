Transaction Analyzer Code(Uses real-time behaviour simulation, real-time timestamp, sleep for waiting, abnormal activity detection, all the features like view history,min,max,delete,etc...):-







package system;

import java.util.*;

//Transaction class
class Transaction {
 double amount;
 Date timestamp;

 public Transaction(double amount, Date timestamp) {
     this.amount = amount;
     this.timestamp = timestamp;
 }

 @Override
 public String toString() {
     return "Transaction: Amount = Rs." + amount + ", Time = " + timestamp;
 }
}

//BST Node class
class TransactionNode {
 Transaction data;
 TransactionNode left, right;

 public TransactionNode(Transaction data) {
     this.data = data;
     this.left = this.right = null;
 }
}

//Main TransactionAnalyzer class
class TransactionAnalyzer {
 TransactionNode root;
 double totalAmount = 0;
 int count = 0;
 List<Transaction> recentTransactions = new ArrayList<>();

 public void addTransaction(double amount) throws InterruptedException {
     Date now = new Date();
     Transaction newTxn = new Transaction(amount, now);
     root = insert(root, newTxn);
     totalAmount += amount;
     count++;
     recentTransactions.add(newTxn);
     System.out.println("Transaction added: " + newTxn);
     Thread.sleep(300);

     detectRealTimeAbnormality(newTxn);
 }

 private TransactionNode insert(TransactionNode root, Transaction txn) {
     if (root == null) return new TransactionNode(txn);
     if (txn.amount < root.data.amount) root.left = insert(root.left, txn);
     else root.right = insert(root.right, txn);
     return root;
 }

 public void printTransactionsInOrder() throws InterruptedException {
     if (root == null) {
         System.out.println("No transactions yet.");
     } else {
         System.out.println("--- Transaction History (In-Order) ---");
         inOrder(root);
     }
 }

 private void inOrder(TransactionNode node) throws InterruptedException {
     if (node != null) {
         inOrder(node.left);
         System.out.println(node.data);
         Thread.sleep(200);
         inOrder(node.right);
     }
 }

 public double getAverageSpend() {
     if (count == 0) return 0;
     return totalAmount / count;
 }

 public void searchTransaction(double amount) throws InterruptedException {
     TransactionNode found = search(root, amount);
     if (found != null) {
         System.out.println("Transaction Found: " + found.data);
     } else {
         System.out.println("No transaction found for amount Rs." + amount);
     }
     Thread.sleep(300);
 }

 private TransactionNode search(TransactionNode node, double amount) {
     if (node == null || node.data.amount == amount) return node;
     if (amount < node.data.amount) return search(node.left, amount);
     else return search(node.right, amount);
 }

 public void getMinTransaction() throws InterruptedException {
     if (root == null) {
         System.out.println("No transactions available.");
         return;
     }
     TransactionNode current = root;
     while (current.left != null) current = current.left;
     System.out.println("Minimum Transaction: " + current.data);
     Thread.sleep(300);
 }

 public void getMaxTransaction() throws InterruptedException {
     if (root == null) {
         System.out.println("No transactions available.");
         return;
     }
     TransactionNode current = root;
     while (current.right != null) current = current.right;
     System.out.println("Maximum Transaction: " + current.data);
     Thread.sleep(300);
 }

 public void detectAbnormalTransactions() throws InterruptedException {
     if (count < 3) {
         System.out.println("Not enough data to detect abnormal transactions.");
         return;
     }

     double avg = getAverageSpend();
     System.out.printf("Average Spend: Rs.%.2f. Flagging amounts > Rs.%.2f\n", avg, 2 * avg);
     System.out.println("--- Abnormal Transactions ---");
     boolean found = detectAbnormalHelper(root, avg);
     if (!found) System.out.println("No abnormal transactions found.");
 }

 private boolean detectAbnormalHelper(TransactionNode node, double avg) throws InterruptedException {
     if (node == null) return false;

     boolean left = detectAbnormalHelper(node.left, avg);
     boolean abnormal = false;
     if (node.data.amount > 2 * avg) {
         System.out.println("[ABNORMAL] " + node.data);
         Thread.sleep(300);
         abnormal = true;
     }
     boolean right = detectAbnormalHelper(node.right, avg);
     return left || abnormal || right;
 }

 public void detectRealTimeAbnormality(Transaction txn) {
     double avg = getAverageSpend();

     // Rule 1: If current txn is more than 2x average
     if (count > 2 && txn.amount > 2 * avg) {
         System.out.println("ALERT: Sudden spike detected! Amount exceeds 2x average.");
     }

     // Rule 2: Multiple high-value txns in 1 minute
     long now = txn.timestamp.getTime();
     int highCount = 0;
     for (Transaction t : recentTransactions) {
         if (now - t.timestamp.getTime() <= 60000 && t.amount > avg) {
             highCount++;
         }
     }
     if (highCount >= 3) {
         System.out.println("ALERT: Multiple high-value transactions in short time span!");
     }
 }

 public void deleteTransaction(double amount) {
     if (root == null) {
         System.out.println("No transactions to delete.");
         return;
     }

     int oldCount = count;
     root = deleteNode(root, amount);
     if (count < oldCount) {
         System.out.println("Transaction of Rs." + amount + " deleted.");
     } else {
         System.out.println("No transaction found for amount Rs." + amount);
     }
 }

 private TransactionNode deleteNode(TransactionNode node, double amount) {
     if (node == null) return null;

     if (amount < node.data.amount) {
         node.left = deleteNode(node.left, amount);
     } else if (amount > node.data.amount) {
         node.right = deleteNode(node.right, amount);
     } else {
         // Found the node
         totalAmount -= node.data.amount;
         count--;
         recentTransactions.removeIf(t -> t.amount == node.data.amount && t.timestamp.equals(node.data.timestamp));

         // Case 1: no child
         if (node.left == null && node.right == null) return null;

         // Case 2: one child
         if (node.left == null) return node.right;
         else if (node.right == null) return node.left;

         // Case 3: two children
         TransactionNode successor = findMin(node.right);
         node.data = successor.data;
         node.right = deleteNode(node.right, successor.data.amount);
     }
     return node;
 }

 private TransactionNode findMin(TransactionNode node) {
     while (node.left != null) node = node.left;
     return node;
 }
}