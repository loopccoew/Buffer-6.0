package dsa_modules;

//5. Segment Tree for Repayment Tracking
public class SegmentTree {
int[] tree;
int[] repayments;
int n;

public SegmentTree(int[] repayments) {
   this.n = repayments.length;
   this.repayments = repayments;
   this.tree = new int[4 * n];
   build(0, 0, n - 1);
}

void build(int node, int start, int end) {
   if (start == end) tree[node] = repayments[start];
   else {
       int mid = (start + end) / 2;
       build(2 * node + 1, start, mid);
       build(2 * node + 2, mid + 1, end);
       tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
   }
}

int query(int node, int start, int end, int l, int r) {
   if (r < start || end < l) return 0;
   if (l <= start && end <= r) return tree[node];
   int mid = (start + end) / 2;
   int leftSum = query(2 * node + 1, start, mid, l, r);
   int rightSum = query(2 * node + 2, mid + 1, end, l, r);
   return leftSum + rightSum;
}

void update(int node, int start, int end, int idx, int val) {
   if (start == end) tree[node] = val;
   else {
       int mid = (start + end) / 2;
       if (idx <= mid) update(2 * node + 1, start, mid, idx, val);
       else update(2 * node + 2, mid + 1, end, idx, val);
       tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
   }
}

//Wrapper method for easy usage
public void update(int idx, int val) {
 update(0, 0, n - 1, idx, val);
}

//Public wrapper for querying repayment sum
public int query(int l, int r) {
 return query(0, 0, n - 1, l, r);
}

}

