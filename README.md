🧠 **Skin Disease Analyzer & Severity Predictor using Custom Data Structures**
🩺 Overview
This project is a hybrid intelligence system for analyzing skin disease images, assessing severity, and recommending treatments. It is uniquely designed using custom-built data structures that integrate trees, graphs, tries, hash maps, and lists — tailored to optimize medical image analysis, data retrieval, and adaptive decision-making.

🔍 Key Features
Multi-scale lesion detection via a hybrid Segment Tree and Quadtree.

Adaptive image enhancement using Skip Lists.

Fast feature similarity search with a KD-Tree.

Disease correlation via a Symptom Graph.

Severity scoring based on weighted CSV rules.

Treatment recommendations via intelligent HashMaps.

Symptom-to-disease prediction using Trie-Graph hybrids (future scope).

📊 Custom Data Structures and Their Roles

Structure	Purpose
📐 Segment Tree + Quadtree	Region-based lesion segmentation and multi-scale localization
📋 Skip List	Adaptive image filtering (contrast, denoising)
🌲 KD-Tree	Feature-based nearest neighbor search (color, shape, texture)
🕸️ Feature Correlation Graph	Symptom co-occurrence mapping and analysis
🧮 HashMap (Disease/Treatment Info)	Quick lookup of treatment protocol, severity scores, and symptom map
🔤 Trie (Planned Feature)	Autocomplete and symptom-to-disease navigation
🔁 Process Flow
1. Image Acquisition & Preprocessing
The uploaded image is split using a hybrid Segment Tree + Quadtree.

Adaptive filters (denoising, sharpening, contrast normalization) applied using a Skip List.

2. Feature Extraction & Disease Matching
Features are extracted using custom functions (color histograms, edge maps).

Features are embedded into a KD-Tree.

Nearest matches are queried to find likely diseases.

A Feature Correlation Graph (Adjacency List + HashMap) identifies co-occurring symptoms.

3. Disease Severity Scoring
Disease-specific weights are loaded from severity_weights.csv.

A custom formula computes a numeric severity score.

4. Treatment Recommendation
Using the match from KD-Tree and severity index, treatment suggestions are retrieved from a HashMap.

5. Symptom to Disease Prediction (planned)
Input symptoms will be parsed via Trie and connected to diseases using a graph structure.

🧪 File Structure
bash
Copy
Edit
📁 skin-disease-analyzer/
│
├── main.py                           # Core process pipeline
├── extract_and_build_kdtree.py      # Feature extraction and KD-Tree building
├── test_kdtree_query.py             # KD-Tree querying
├── disease_info.csv                 # Disease metadata and symptom/treatment mapping
├── severity_weights.csv             # Severity weights per disease/symptom
├── requirements.txt                 # Python dependencies
⚙️ Installation
bash
Copy
Edit
# Clone the repo
git clone https://github.com/yourname/skin-disease-analyzer.git
cd skin-disease-analyzer

# Create virtual environment
python -m venv env
source env/bin/activate  # or env\Scripts\activate on Windows

# Install dependencies
pip install -r requirements.txt

▶️ Running the Project
bash
Copy
Edit
# Build the KD-Tree
python extract_and_build_kdtree.py

# Query with test features
python test_kdtree_query.py

# Run the main analyzer
python main.py
🚀 Use Cases
Dermatology diagnostics

Smart health monitoring kiosks

Mobile skin disease detection tools

Research on visual symptom correlation and severity classification

✅ Why Custom Data Structures?
✔️ Faster multi-scale segmentation
✔️ Scalable and real-time capable
✔️ Enables interpretable decision paths
✔️ Adaptive feature processing and matching
✔️ Promotes modularity and algorithmic experimentation

🧭 Future Enhancements
Web interface (Streamlit or Flask UI)

Integration with medical databases

Graph-based progression tracking over time

Real symptom-based chatbot navigation

Video Drive link:https://drive.google.com/drive/folders/1EXuzwrOQUltTfpvK1yEGrofSK7g7Elp0?usp=sharing

