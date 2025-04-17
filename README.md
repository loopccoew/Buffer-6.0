---
### Video Link : https://drive.google.com/drive/folders/18RsVmOT71FL1C_V2yZXf_W-ADPadFt_H?usp=drive_link
### Drive Link : https://drive.google.com/drive/folders/1XGKgUPozFdUUSngjnxsbVj9ospnDXLx6?usp=sharing
# 📄 Report Buffer

**Flask-Based Research Summarization & Citation Web App**

---

## 🎯 Objective

The goal of this project is to build a **Flask-based web application** that enhances interaction with research papers. Users can:

- Upload papers
- Receive summaries tailored to their academic level
- View citations to related research

This is achieved through advanced **Natural Language Processing (NLP)** techniques like **TF-IDF**, **TextRank**, and **Transformer-based models (T5)**. The system also uses **cosine similarity** to recommend related work.

A sleek and modern frontend is developed using **HTML, CSS, and JavaScript**.

---

## 🧠 Data Structures Used

| Purpose | Technique / Structure |
|--------|------------------------|
| Extractive summarization | TF-IDF Matrix (Sparse Matrix) |
| Graph-based summarization | Adjacency Matrix (for TextRank) |
| Citation similarity | Cosine Similarity Matrix (2D Array) |
| Persistent storage | Relational Database (SQLAlchemy ORM) |

**Tables:**
- **Paper** – stores uploaded papers and their summaries
- **Citation** – stores similarity-based links between papers

---

## 🚀 Features

- 📤 **Upload Research Paper** (plain text)
- 📚 **Summarization by Academic Level**
  - **School & PhD** → TF-IDF + Cosine Similarity (Extractive)
  - **Undergraduate** → TextRank (Graph-based)
  - **General** → T5 (Abstractive)
- 🔍 **Citation Recommendation**
  - Uses cosine similarity on summary vectors
- 🗄️ **Database Management**
  - PostgreSQL via SQLAlchemy
- 💻 **Modern Frontend**
  - HTML, CSS (dark theme), and JavaScript for interactivity

---

## ⚙️ Implementation Details

### 🔍 Summarization Module

- `tfidf_cosine_summarizer()`
- `text_rank_summarizer()`
- `t5_abstractive_summarizer()`

### 🔗 Citation Engine

- Computes similarity using `cosine_similarity()` on TF-IDF vectors
- Stores strong matches (score > 0.5) in `Citation` table

### 🌐 Flask Routes

| Route | Description |
|-------|-------------|
| `/` | Homepage |
| `/upload` | Upload page |
| `/summarize` | Summarizes uploaded paper |
| `/generate_citations` | Computes and stores related papers |
| `/citations/<id>` | Displays recommended papers for a given paper |

---

## 🧭 Menu Operations / User Flow

1. Upload a research paper
2. Select academic level for summarization
3. View generated summary
4. Explore related research via citation recommendations
5. *(Future Scope)* Admin operations to manage/delete entries

---

## ✅ Conclusion

This web application fuses **NLP** and **web development** to simplify academic research access. From **tailored summarization** to **smart citation discovery**, the app delivers powerful features in a user-friendly format.

The choice of **sparse matrices**, **graph algorithms**, and **transformer models** makes the system robust and adaptable.

🚧 **Coming Soon**:
- PDF upload support
- Cloud deployment (e.g., Render, Heroku, or AWS)

---

## 👩‍💻 Built With

- Python (Flask, Transformers, Scikit-learn)
- HTML + CSS + JavaScript
- SQLAlchemy + PostgreSQL
