class FenwickTree {
  constructor(size) {
    this.tree = Array(size + 1).fill(0);
    this.size = size;
  }

  update(index, value) {
    while (index <= this.size) {
      this.tree[index] += value;
      index += index & -index;
    }
  }

  query(index) {
    let sum = 0;
    while (index > 0) {
      sum += this.tree[index];
      index -= index & -index;
    }
    return sum;
  }
}

module.exports = FenwickTree;
