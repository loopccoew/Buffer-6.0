# 📚Web Crawler – Buffer 6.0
## Team 4S_QUAD - Custom Data Structure

This Java-based research assistant crawls Google Scholar and arXiv in real-time, offering dynamic keyword search and auto-suggestions to enhance research efficiency. It provides customizable filters, smart ranking, and personalized alerts for the most relevant papers. Users can stay updated with the latest research, ensuring quick access to quality academic content. This tool enables users to explore citation paths, identify isolated works, and interactively filter results by author or year of publication.

---

[![Watch the video](https://drive.google.com/file/d/1f-s0y7YYcN3L7SFid2SN9v4CV7c5wIxz/view?usp=drive_link )](https://drive.google.com/file/d/1f-s0y7YYcN3L7SFid2SN9v4CV7c5wIxz/view?usp=drive_link )


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
  ![image](https://github.com/user-attachments/assets/30df98ae-3f26-4051-bd23-1aea531b0567)

- **Graph**  
  Used to model citation relationships between academic papers.

- **Queue & Set**  
  Internally used for crawling and graph traversal logic (like BFS).

- **Java Stream API**  
  Powers efficient filtering and supports functional programming paradigms.

---
<details> <summary>📊 <strong>Data Structures & Algorithms Used in <code>Main.java</code></strong></summary>
</details>


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

<details> <summary>🌐 <strong>Data Structures & Algorithms Used in <code>ArvixCrawler.java</code></strong></summary></details>

| Class Name     | DS Used           | Purpose of DS                                               | Algorithm Used          | Purpose of Algorithm                                              |
|----------------|------------------|-------------------------------------------------------------|-------------------------|-------------------------------------------------------------------|
| ArvixCrawler   | List<Paper>       | To store the parsed paper objects from search results       | Iteration & Parsing     | Loop over HTML elements to extract paper data                    |
| ArvixCrawler   | List<String>      | To store list of authors for each paper                     | DOM Traversal           | Extract author names from HTML anchor tags                       |
| ArvixCrawler   | Jsoup Document    | Represents the full HTML content of arXiv search results    | HTML Parsing            | Parse and navigate the HTML tree to extract relevant nodes       |
| ArvixCrawler   | Elements          | Stores multiple HTML elements selected from DOM             | Tag Selection           | Collect matching tags like title, authors, abstract, etc.        |
| ArvixCrawler   | Element           | Individual HTML tag representing a paper section            | Attribute Access        | Retrieve specific fields like `href`, `text`, etc.               |
| ArvixCrawler   | String Builder*   | Used during string operations (e.g., cleaning text)         | Regex Matching          | Clean and format abstract and date strings                       |


<details> <summary>🔎 <strong>Data Structures & Algorithms Used in <code>GoogleScholarCrawler.java</code></strong></summary></details>

| Class Name             | DS Used           | Purpose of DS                                                    | Algorithm Used           | Purpose of Algorithm                                             |
|------------------------|------------------|------------------------------------------------------------------|--------------------------|------------------------------------------------------------------|
| GoogleScholarCrawler   | List<Paper>       | To collect and return paper search results                       | Iteration & Parsing      | Loop through HTML content and convert to Paper objects           |
| GoogleScholarCrawler   | List<String>      | To store individual author names (split from author string)      | String Splitting         | Separate authors from a single string using `.split(",")`        |
| GoogleScholarCrawler   | Jsoup Document    | To represent the HTML content of the search result page          | HTML Parsing             | Parse Google Scholar's response into a DOM structure             |
| GoogleScholarCrawler   | Elements          | To store multiple DOM nodes (e.g., paper entries)                | DOM Selection            | Select all result containers using class selectors (`.gs_r`)     |
| GoogleScholarCrawler   | Element           | To represent a single HTML tag (e.g., title, abstract, link)     | Attribute/Text Access     | Extract title, abstract, and links from relevant HTML tags       |


<details> <summary>🧠 <strong>Data Structures & Algorithms Used in <code>CitationGraph.java</code></strong></summary></details>

| Class Name      | DS Used                     | Purpose of DS                                                                 | Algorithm Used         | Purpose of Algorithm                                                  |
|------------------|------------------------------|--------------------------------------------------------------------------------|--------------------------|------------------------------------------------------------------------|
| CitationGraph    | Map<String, Set<String>>     | To represent a directed graph: paper ➝ set of cited papers                     | DFS (Depth-First Search) | Traverse graph to find related papers recursively                     |
| CitationGraph    | Set<String>                  | To store unique citations and track visited nodes                              | Graph Traversal (DFS)    | Prevent revisiting nodes while traversing citations                   |
| CitationGraph    | List<String>                 | To collect suggestions and isolated papers                                     | Result Collection        | Store results of traversal or filtering                               |
| CitationGraph    | HashMap, HashSet             | Fast insert and lookup for paper nodes and citations                           | Map-based Adjacency List | Efficient graph representation                                        |
| CitationGraph    | Map.Entry iteration          | Iterate through key-value pairs of citation graph                              | Reverse Lookup           | Find all nodes citing a given paper                                   |


<details> <summary>🔤 <strong>Data Structures & Algorithms Used in <code>Trie.java</code></strong></summary></details>

| Class Name | DS Used                     | Purpose of DS                                                                  | Algorithm Used            | Purpose of Algorithm                                                   |
|------------|-----------------------------|---------------------------------------------------------------------------------|---------------------------|-------------------------------------------------------------------------|
| Trie       | TrieNode                     | Represents nodes in the Trie, storing character links and word-ending markers    | Depth-First Search (DFS)  | Traverse the Trie to suggest words with the given prefix               |
| Trie       | Map<Character, TrieNode>     | Stores child nodes for each character in a TrieNode                             | Trie Insertion/Search     | Insert/search a word or prefix by traversing through the characters     |
| Trie       | List<String>                 | To collect word suggestions based on a prefix                                  | String Construction       | Build and return the list of complete words from the Trie               |
| Trie       | StringBuilder                | Efficiently manage word construction during DFS traversal                      | Path Building             | Build suggested words by appending and removing characters during DFS   |
| TrieNode   | boolean                      | Marks the end of a word in the Trie                                            | Word Termination          | Flag whether the current node marks the end of a word                  |


<details> <summary>🔤 <strong>Data Structures Used in <code>TrieNode.java</code></strong></summary></details>

| Class Name   | DS Used                     | Purpose of DS                                                |
|--------------|-----------------------------|--------------------------------------------------------------|
| TrieNode     | Map<Character, TrieNode>     | To store child nodes for each character                       |
| TrieNode     | boolean                      | To mark whether the current node is the end of a word        |


<details> <summary>📄 <strong>Data Structures Used in <code>Paper.java</code></strong></summary></details>

| Class Name | DS Used             | Purpose of DS                                                 |
|------------|---------------------|---------------------------------------------------------------|
| Paper      | String              | To store the title of the paper                                |
| Paper      | List<String>        | To store authors of the paper                                  |
| Paper      | String              | To store the abstract text of the paper                        |
| Paper      | String              | To store the publication date of the paper                     |
| Paper      | String              | To store the link to the paper                                 |
| Paper      | String              | To store the source of the paper (e.g., IEEE, ArXiv)           |


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

## 📁 File Structure

```
buffer-research-crawler/  
├── 📁 src/  
│   └── 📁 buffer/  
│       ├── 📁 core/  
│       │   └── Main.java                   # Entry point  
│       ├── 📁 Crawler/  
│       │   ├── Crawler.java               # Base interface for crawlers  
│       │   ├── ArvixCrawler.java          # Handles Arxiv scraping  
│       │   └── GoogleScholarCrawler.java  # Handles Google Scholar scraping  
│       ├── 📁 ds/  
│       │   ├── CitationGraph.java         # Graph structure and logic  
│       │   ├── Trie.java                  # Keyword suggestion via Trie  
│       │   └── TrieNode.java              # Node structure for Trie  
│       └── 📁 model/  
│           └── Paper.java                 # Paper model containing metadata  
├── keywords.csv  
├── jsoup-1.19.1.jar                       # Library used for HTML parsing  
├── README.md  
└── .gitignore  
```


