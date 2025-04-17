public class SegmentTree {
    private int[] tree;
    private int[] lazy;
    private int size;

    public SegmentTree(int size) {
        this.size = size;
        int height = (int) Math.ceil(Math.log(size) / Math.log(2));
        int maxSize = 2 * (int) Math.pow(2, height) - 1;
        tree = new int[maxSize];
        lazy = new int[maxSize];
        build(0, 0, size - 1);
    }

    private void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = 1; // Initially all slots are available
            return;
        }

        int mid = (start + end) / 2;
        build(2 * node + 1, start, mid);
        build(2 * node + 2, mid + 1, end);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    private void pushDown(int node, int start, int end) {
        if (lazy[node] != 0) {
            tree[node] += lazy[node] * (end - start + 1);
            if (start != end) {
                lazy[2 * node + 1] += lazy[node];
                lazy[2 * node + 2] += lazy[node];
            }
            lazy[node] = 0;
        }
    }

    public void updateRange(int start, int end, int value) {
        updateRange(0, 0, size - 1, start, end, value);
    }

    private void updateRange(int node, int start, int end, int l, int r, int value) {
        pushDown(node, start, end);

        if (start > end || start > r || end < l) {
            return;
        }

        if (start >= l && end <= r) {
            tree[node] += value * (end - start + 1);
            if (start != end) {
                lazy[2 * node + 1] += value;
                lazy[2 * node + 2] += value;
            }
            return;
        }

        int mid = (start + end) / 2;
        updateRange(2 * node + 1, start, mid, l, r, value);
        updateRange(2 * node + 2, mid + 1, end, l, r, value);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    public int queryRange(int start, int end) {
        return queryRange(0, 0, size - 1, start, end);
    }

    private int queryRange(int node, int start, int end, int l, int r) {
        pushDown(node, start, end);

        if (start > end || start > r || end < l) {
            return 0;
        }

        if (start >= l && end <= r) {
            return tree[node];
        }

        int mid = (start + end) / 2;
        int left = queryRange(2 * node + 1, start, mid, l, r);
        int right = queryRange(2 * node + 2, mid + 1, end, l, r);
        return left + right;
    }

    public int getAvailableSlots() {
        return queryRange(0, size - 1);
    }

    public boolean isSlotAvailable(int slot) {
        return queryRange(slot, slot) == 1;
    }

    public void bookSlot(int slot) {
        if (isSlotAvailable(slot)) {
            updateRange(slot, slot, -1);
        }
    }

    public void releaseSlot(int slot) {
        if (!isSlotAvailable(slot)) {
            updateRange(slot, slot, 1);
        }
    }
} 