# train_model.py
import matplotlib.pyplot as plt
import pandas as pd
import pickle
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report

# Step 1: Load dataset
df = pd.read_csv('C:/Users/prite/Downloads/Women Harassment/Women Harassment/Messages.csv')

# Step 2: Prepare data
X = df[['tweet']]
y = df['label']

# Step 3: Train-test split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Step 4: Create pipeline
preprocessor = ColumnTransformer(
    transformers=[('text', TfidfVectorizer(max_features=5000), 'tweet')]
)
pipeline = Pipeline([
    ('preprocessor', preprocessor),
    ('classifier', LogisticRegression())
])

# Step 5: Train the model
pipeline.fit(X_train, y_train)

# Step 6: Evaluate
y_pred = pipeline.predict(X_test)
print("Accuracy:", accuracy_score(y_test, y_pred))
print("Classification Report:\n", classification_report(y_test, y_pred))
accuracy = accuracy_score(y_test, y_pred)
correct = accuracy * 100
incorrect = 100 - correct

# Data and style
sizes = [correct, incorrect]
labels = ['Correct', 'Incorrect']
colors = ['#4CAF50', '#F44336']

fig, ax = plt.subplots()
wedges, texts, autotexts = ax.pie(
    sizes, labels=labels, autopct='%1.1f%%', startangle=90, colors=colors,
    wedgeprops=dict(width=0.4), textprops=dict(color="black")
)
ax.set_title("Model Accuracy", fontsize=14)
plt.show()

# Step 7: Save model
with open('harassment_detection.pkl', 'wb') as f:
    pickle.dump(pipeline, f)
