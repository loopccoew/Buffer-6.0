const express = require('express');
const cors = require('cors');
const InvestmentSuggestions = require('./dsa/investmentSuggestions');
const FenwickTree = require('./dsa/fenwickTree');

const app = express();
const PORT = 5000;

app.use(cors());
app.use(express.json());

const investment = new InvestmentSuggestions(31);

app.post('/add', (req, res) => {
  const { day, amount } = req.body;
  investment.addTransaction(day, amount);
  res.json({ message: 'Transaction added!' });
});

app.get('/suggestions', (req, res) => {
  const savings = investment.getSavings();
  const suggestion = investment.generateSuggestion(savings);

  res.json({
    savings,
    suggestion
  });
});

app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
