const express = require('express');
const cors = require('cors');
const InvestmentSuggestions = require('./dsa/investmentSuggestions');

const app = express();
const PORT = 5000;

app.use(cors());
app.use(express.json());

const investment = new InvestmentSuggestions(31); // 31 din ka max month

// ✅ Add Transaction
app.post('/add', (req, res) => {
  const { day, amount } = req.body;
  investment.addTransaction(day, amount);

  const savings = investment.getSavings();
  const suggestionObj = investment.generateSuggestion(savings);

  res.json({
    summary: `💰 Your current savings are ₹${savings}`,
    suggestion: suggestionObj.text,
    links: suggestionObj.links
  });
});

// ✅ Investment Suggestions (GET)
app.get('/suggestions', (req, res) => {
  const savings = investment.getSavings();
  const suggestionObj = investment.generateSuggestion(savings);

  res.json({
    savings,
    suggestion: suggestionObj
  });
});

// ✅ Start Server
app.listen(PORT, () => {
  console.log(`✅ Server running at http://localhost:${PORT}`);
});

