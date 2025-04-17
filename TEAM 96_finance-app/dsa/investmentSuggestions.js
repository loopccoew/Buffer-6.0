const FenwickTree = require('./fenwickTree');

class InvestmentSuggestions {
  constructor(size) {
    this.tree = new FenwickTree(size);
    this.size = size;
  }

  addTransaction(day, amount) {
    this.tree.update(day, amount);
  }

  getSavings() {
    return this.tree.query(this.size);
  }

  generateSuggestion(savings) {
    if (savings > 5000) {
      return {
        text: "You’ve saved well! Consider investing in digital gold 💰.",
        links: [
          "https://www.youtube.com/watch?v=kX5B3Sz6BcM",
          "https://www.youtube.com/watch?v=tlh-wWPDC8A&t=52s"
        ]
      };
    } else {
      return {
        text: "Try low-risk mutual funds to grow your small savings 📈.",
        links: [
          "https://www.youtube.com/watch?v=rAqzpRZa78E",
          "https://www.youtube.com/watch?v=PbldLCsspgE"
        ]
      };
    }
  }
}

module.exports = InvestmentSuggestions;
