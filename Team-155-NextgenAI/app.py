from flask import Flask, render_template, request, redirect
from flask_sqlalchemy import SQLAlchemy
import re, random
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from summa import summarizer
from transformers import T5ForConditionalGeneration, T5Tokenizer

app = Flask(__name__)

#  ---------- DATABASE SETUP ---------
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///papers.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

#  ---------- DATABASE MODELS ---------
class Paper(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100))
    authors = db.Column(db.String(100))
    publication = db.Column(db.String(100))
    year = db.Column(db.Integer)
    summary = db.Column(db.Text)

with app.app_context():
    db.create_all()

#    sample_papers = [
#    Paper(title="AI in Healthcare", authors="Aastha", publication="MedAI", year=2022, summary="This paper explores AI in healthcare and its applications."),
#    Paper(title="ML for Diagnosis", authors="Harshit", publication="AIConf", year=2023, summary="This study investigates ML models for medical diagnosis."),
#    Paper(title="AI in Radiology", authors="Biresh", publication="HealthTech", year=2021, summary="Using CNNs in radiology for image analysis."),
#    Paper(title="Natural Language Processing for Clinical Text", authors="Chaitrali", publication="NLP4Health", year=2020, summary="This paper discusses NLP applications in analyzing clinical and medical records."),
#    Paper(title="Reinforcement Learning for Drug Discovery", authors="Mehul", publication="BioAI", year=2023, summary="Explores how reinforcement learning is used for drug molecule optimization."),
#    Paper(title="Generative Adversarial Networks for Medical Imaging", authors="Pranav", publication="MedVision", year=2021, summary="GANs are applied to synthesize high-quality medical images for training data augmentation."),
#    Paper(title="Explainable AI in Medical Diagnostics", authors="Ananya", publication="XAIConf", year=2022, summary="This paper emphasizes the importance of explainable models in medical diagnosis systems."),
#    Paper(title="AI-Powered Wearables for Health Monitoring", authors="Sanya", publication="SmartHealth", year=2023, summary="Explores the use of AI in analyzing data from wearable health devices."),
#    Paper(title="Predictive Models for Pandemic Response", authors="Ritvik", publication="EpiAI", year=2020, summary="Analyzes how AI was used to predict outbreaks and manage healthcare resources during COVID-19."),
#    Paper(title="AI for Mental Health Detection", authors="Ishita", publication="NeuroAI", year=2023, summary="Investigates sentiment analysis and emotion detection models for mental health prediction."),
#]

#    Add sample papers to the database
#    db.session.add_all(sample_papers)
#    db.session.commit()


class Citation(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    paper_id = db.Column(db.Integer, db.ForeignKey('paper.id'), nullable=False)
    related_paper_id = db.Column(db.Integer, db.ForeignKey('paper.id'), nullable=False)
    similarity_score = db.Column(db.Float, nullable=False)

    paper = db.relationship('Paper', foreign_keys=[paper_id], backref='citations_from')
    related_paper = db.relationship('Paper', foreign_keys=[related_paper_id], backref='citations_to')

with app.app_context():
    db.create_all()


# ---------- SUMMARIZER FUNCTIONS ----------
def split_into_sentences(text):
    sentences = re.split(r'(?<=[.!?]) +', text.strip())
    return [s for s in sentences if len(s.split()) > 2]

def tfidf_cosine_summarizer(text, top_n=None):
    sentences = split_into_sentences(text)
    if len(sentences) < 3:
        return "Not enough content to summarize."
    top_n = top_n or max(3, int(0.7 * len(sentences)))
    tfidf_vectorizer = TfidfVectorizer(stop_words='english')
    tfidf_matrix = tfidf_vectorizer.fit_transform(sentences)
    scores = cosine_similarity(tfidf_matrix).sum(axis=1)
    ranked = np.argsort(scores)[::-1][:top_n]
    selected = sorted(random.sample(ranked.tolist(), min(top_n, len(ranked))))
    return ' '.join([sentences[i] for i in selected])

def textrank_summarizer(text, top_n=None):
    summary = summarizer.summarize(text, ratio=0.5)
    return summary or "Summary not available due to content length or complexity."

def abstractive_summarizer(text, top_n=None):
    model = T5ForConditionalGeneration.from_pretrained("t5-small")
    tokenizer = T5Tokenizer.from_pretrained("t5-small")
    inputs = tokenizer.encode("summarize: " + text, return_tensors="pt", max_length=512, truncation=True)
    ids = model.generate(inputs, max_length=250, min_length=100, length_penalty=2.0, num_beams=6, early_stopping=True)
    return tokenizer.decode(ids[0], skip_special_tokens=True)

def hybrid_summarizer(text, top_n=None):
    return abstractive_summarizer(textrank_summarizer(text, top_n))


# ---------- ROUTES ----------
@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upload')
def home():
    return render_template('home2.html')

@app.route('/summarize', methods=['POST'])
def summarize():
    title = request.form['title']
    author = request.form['author']
    year = request.form['year']
    text = request.form['text']
    qualification = request.form['qualification'].lower()

    if "school" in qualification:
        summary = tfidf_cosine_summarizer(text, top_n=3)
    elif "phd" in qualification:
        summary = tfidf_cosine_summarizer(text, top_n=7)
    else:
        summary = textrank_summarizer(text, top_n=5)

    return render_template('summary.html', title=title, summary=summary)


@app.route('/generate_citations', methods=['POST'])
def generate_citations():
    title = request.form['title']
    summary_text = request.form['summary']

    # Save summarized paper only
    new_paper = Paper(
        title=title,
        authors="Summarizer",
        publication="Auto-generated",
        year=2025,
        summary=summary_text
    )
    db.session.add(new_paper)
    db.session.commit()

    # Vectorize all paper summaries
    papers = Paper.query.all()
    summaries = [p.summary for p in papers]
    vectors = TfidfVectorizer().fit_transform(summaries)
    sim_matrix = cosine_similarity(vectors)

    # Clear old citations
    Citation.query.delete()
    db.session.commit()

    # Add new citations (if similarity > 0.5)
    for i, p1 in enumerate(papers):
        for j, p2 in enumerate(papers):
            if i != j and sim_matrix[i][j] > 0.5:
                db.session.add(Citation(paper_id=p1.id, related_paper_id=p2.id, similarity_score=sim_matrix[i][j]))

    db.session.commit()
    return redirect(f"/citations/{new_paper.id}")


@app.route('/citations/<int:paper_id>')
def get_citations(paper_id):
    paper = Paper.query.get_or_404(paper_id)
    citations = Citation.query.filter_by(paper_id=paper_id).all()
    related = [(Paper.query.get(c.related_paper_id), c.similarity_score) for c in citations]
    return render_template('citations.html', paper=paper, related_papers=related)


# ---------- MAIN ----------
if __name__ == '__main__':
    app.run(debug=True)
