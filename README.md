# 📚Web Crawler – Buffer 6.0
## team 4S_QUAD

This Java-based research assistant crawls Google Scholar and arXiv in real-time, offering dynamic keyword search and auto-suggestions to enhance research efficiency. It provides customizable filters, smart ranking, and personalized alerts for the most relevant papers. Users can stay updated with the latest research, ensuring quick access to quality academic content. This tool enables users to explore citation paths, identify isolated works, and interactively filter results by author or year of publication.

---

## 🎯 Why This Project is Helpful

🔬 **For Researchers**:  
- Quickly visualize how research papers are interconnected.
- Identify influential papers through citation paths.
- Spot isolated or under-cited research for potential exploration.

🎓 **For Students**:  
- Gain clarity on how academic papers build upon each other.
- Discover related work efficiently without manual digging.
- Learn core data structures (like graphs and tries) through a real-world application.

This tool serves as both a **research aid** and an **educational demo**, bridging theoretical computer science concepts with practical academic workflows.

---

## 🚀 Features

- 📈 **Citation Graph Construction**: Builds a directed graph where nodes represent papers and edges represent citations.
- 🔍 **Related Paper Suggestions**: Suggests papers by tracing citation paths.
- 🚫 **Isolated Paper Detection**: Identifies papers that are not cited by or do not cite others.
- 🎯 **Interactive Filtering**: Filter nodes by author name and publication year for focused analysis.
- 🧠 **Keyword Auto-Suggestions**: Built-in search assistance using trie-based suggestions.

---

## 🧩 Core DSA Implementations

- **Trie**  
  Efficient keyword auto-completion based on user input.

- **Graph**  
  Used to model citation relationships between academic papers.

- **Queue & Set**  
  Internally used for crawling and graph traversal logic (like BFS).

- **Java Stream API**  
  Powers efficient filtering and supports functional programming paradigms.

---
<details> <summary>📊 <strong>Data Structures & Algorithms Used in <code>Main.java</code></strong></summary>
markdown
Copy code
| Class Name   | DS Used           | Purpose of DS                                                  | Algorithm Used              | Purpose of Algorithm                                             |
|--------------|------------------|----------------------------------------------------------------|-----------------------------|------------------------------------------------------------------|
| Main         | Trie              | To store and suggest keywords based on prefix                  | Prefix Matching              | Suggest relevant keywords using prefix-based search             |
| Main         | List<String>      | To store and display keyword suggestions                       | Linear Iteration             | Iterate through suggestions for display and selection           |
| Main         | List<Paper>       | To collect and store search results                            | Merge/AddAll                 | Combine results from Arxiv and Google Scholar crawlers          |
| Main         | Scanner           | To read user input for prefix and filtering options            | N/A                          | Get inputs like keywords, filters from the user                 |
| Main         | BufferedReader    | To read keywords from CSV file                                 | File Reading                 | Load keywords into Trie from a CSV file                         |
| Main         | CitationGraph     | Graph of paper citations                                       | Graph Construction           | Build graph by adding papers and citations                      |
| Main         | CitationGraph     | Graph of paper citations                                       | Related Paper Suggestion     | Traverse graph to find papers connected to a base paper         |
| Main         | CitationGraph     | Graph of paper citations                                       | Isolated Node Detection      | Detect nodes with no incoming or outgoing edges                 |
| Main         | List<Paper>       | Used for filterable paper results                              | Stream Filtering              | Filter by author name and publication year                      |
| Main         | Regex Matcher     | Extract year from date strings                                 | Regex Pattern Matching        | Match publication year from publication date string             |
</details>
---

## 🖼️ Visualization

- Integrated with **Graphviz** to generate dynamic graph visualizations of the citation network.
- Clear, intuitive visuals with citation direction, isolated nodes, and highlighting based on filters.

> ⚙️ *Ensure Graphviz is installed and added to your system's PATH.*

---

## 📦 Tech Stack

- **Java 8+**
- **Eclipse IDE**
- **Graphviz** (for visualization)
- **Git** (version control)

---

## 🛠️ Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/Shrutit051/Buffer-6.0.git
   cd Buffer-6.0

Buffer-6.0/
│
├── src/
│   ├── buffer.core/
│   │   └── Main.java                   # Entry point
│   │
│   ├── buffer.Crawler/
│   │   ├── ArxivCrawler.java          # Handles Arxiv scraping
│   │   ├── GoogleScholarCrawler.java  # Handles Google Scholar scraping
│   │   └── Crawler.java               # Base class for crawlers
│   │
│   ├── buffer.ds/
│   │   ├── CitationGraph.java         # Graph structure and logic
│   │   ├── Trie.java                  # Keyword suggestion via Trie
│   │   └── TrieNode.java              # Node structure for Trie
│   │
│   └── buffer.model/
│       └── Paper.java                 # Paper model containing metadata
│
├── Referenced Libraries/
│   └── jsoup-1.19.1.jar               # Library used for HTML parsing
│
└── README.md

