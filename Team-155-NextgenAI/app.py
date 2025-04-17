from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sumy.parsers.plaintext import PlaintextParser
from sumy.summarizers.lsa import LsaSummarizer
from sumy.summarizers.luhn import LuhnSummarizer
from sumy.summarizers.text_rank import TextRankSummarizer


app = Flask(__name__)

# ✅ 1. Set up database configs
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///papers.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

# ✅ 2. Define DB Model
class Paper(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100))
    authors = db.Column(db.String(100))
    publication = db.Column(db.String(100))
    year = db.Column(db.Integer)
    summary = db.Column(db.Text)

# Citation model to store related papers and similarity score
class Citation(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    paper_id = db.Column(db.Integer, db.ForeignKey('paper.id'), nullable=False)
    related_paper_id = db.Column(db.Integer, db.ForeignKey('paper.id'), nullable=False)
    similarity_score = db.Column(db.Float, nullable=False)

    paper = db.relationship('Paper', foreign_keys=[paper_id], backref='citations_from')
    related_paper = db.relationship('Paper', foreign_keys=[related_paper_id], backref='citations_to')



# ✅ 3. Create DB Tables
with app.app_context():
    db.create_all()




    # Temporary script (add sample papers to the DB)
    #sample_papers = [
    #    Paper(title="ML for Diagnosis", authors="Harshit", publication="AIConf", year=2023, summary="This study investigates ML models for medical diagnosis."),
    #    Paper(title="AI in Radiology", authors="Biresh", publication="HealthTech", year=2021, summary="Using CNNs in radiology for image analysis."),
    #]

     #Add sample papers to the database
    #db.session.add_all(sample_papers)
    #db.session.commit()

# ✅ 4. Home route
@app.route('/')
def home():
    return render_template('index.html') 

# ✅ 5. Paper form route
@app.route('/paper', methods=['GET', 'POST'])
def paper_submit():
    if request.method == 'POST':
        try:
            new_paper = Paper(
                title=request.form['title'],
                authors=request.form['authors'],
                publication=request.form['publication'],
                year=request.form['year'],
                summary=request.form['summary']
            )
            db.session.add(new_paper)
            db.session.commit()
            return 'Paper submitted successfully!'
        except Exception as e:
            db.session.rollback()
            return f'Error: {str(e)}'
    return render_template('form.html')

# ✅ 6. Vectorizing summaries (run after some papers exist)
@app.route('/vectorize')
def vectorize_papers():
    papers = Paper.query.all()
    all_summaries = [p.summary for p in papers]

    if not all_summaries:
        return "No papers in DB to vectorize."

    vectorizer = TfidfVectorizer()
    vectors = vectorizer.fit_transform(all_summaries)

    return "Papers vectorized successfully!"


@app.route('/similarities')
def get_similarities():
    papers = Paper.query.all()
    all_summaries = [p.summary for p in papers]

    if len(all_summaries) < 2:
        return "Need at least two papers to compute similarity."

    vectorizer = TfidfVectorizer()
    vectors = vectorizer.fit_transform(all_summaries)

    similarities = cosine_similarity(vectors)

    # ✅ Clear existing citations before re-calculating
    Citation.query.delete()

    for i in range(len(papers)):
        for j in range(i + 1, len(papers)):
            score = similarities[i][j]
            if score > 0.5:  # Threshold
                c1 = Citation(paper_id=papers[i].id, related_paper_id=papers[j].id, similarity_score=score)
                c2 = Citation(paper_id=papers[j].id, related_paper_id=papers[i].id, similarity_score=score)
                db.session.add_all([c1, c2])
    
    db.session.commit()

    return "Similarity computation and citation creation done!"




@app.route('/citations/<int:paper_id>')
def get_citations(paper_id):
    paper = Paper.query.get_or_404(paper_id)
    citations = Citation.query.filter_by(paper_id=paper_id).all()

    related_papers = []
    for citation in citations:
        related = Paper.query.get(citation.related_paper_id)
        if related:
            related_papers.append((related, citation.similarity_score))  # Pass similarity score too

    return render_template('citations.html', paper=paper, related_papers=related_papers)





# ✅ 7. Run server
if __name__ == '__main__':
    app.run(debug=True)
