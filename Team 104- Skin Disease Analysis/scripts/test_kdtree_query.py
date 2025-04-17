import cv2
import numpy as np
import pickle
import pandas as pd
from skimage.feature import local_binary_pattern

# Constants
IMG_SIZE = (128, 128)

# Feature Extraction
def extract_features(image):
    image = cv2.resize(image, IMG_SIZE)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    
    # Color Histogram
    hist = cv2.calcHist([image], [0, 1, 2], None, [8, 8, 8], [0, 256, 0, 256, 0, 256])
    hist = cv2.normalize(hist, hist).flatten()
    
    # LBP Features
    lbp = local_binary_pattern(gray, P=8, R=1, method='uniform')
    lbp_hist, _ = np.histogram(lbp.ravel(), bins=np.arange(0, 10), range=(0, 9))
    lbp_hist = lbp_hist.astype("float")
    lbp_hist /= (lbp_hist.sum() + 1e-7)
    
    return np.hstack([hist, lbp_hist])

# Load Resources
with open('kdtree.pkl', 'rb') as f:
    tree = pickle.load(f)
features_array = np.load('features.npy')
labels_array = np.load('labels.npy')
with open('label_map.pkl', 'rb') as f:
    label_map = pickle.load(f)

# Load severity weights
severity_df = pd.read_csv('severity_weights.csv')
severity_weights = {
    row['disease'].lower().replace(' ', '_'): row.drop('disease').to_dict()
    for _, row in severity_df.iterrows()
}

# Test Image Processing
img = cv2.imread(r'C:\Users\aryas\OneDrive\Desktop\skin_disease_detector\assets\test_images\BCC-test.jpg')
feat = extract_features(img).reshape(1, -1).astype(np.float32)

# Dimension Check
if feat.shape[1] != features_array.shape[1]:
    raise ValueError(f"Feature dimension mismatch: test={feat.shape[1]}, expected={features_array.shape[1]}")

# Query KDTree
dist, idx = tree.query(feat, k=5)

print("Top 5 Predictions:")
total_scores = {}

for i in idx[0]:
    label = labels_array[i]
    disease = label_map.get(str(label), f"Disease_{label}")  # Ensure string key lookup
    weights = severity_weights.get(disease.lower().replace(' ', '_'), {})
    severity_score = sum(float(val) for val in weights.values())
    total_scores[disease] = severity_score
    print(f"- {disease} | Severity Score: {severity_score:.2f}")

most_likely = min(total_scores, key=total_scores.get)
print(f"\nMost Likely Disease: {most_likely.upper()} (Severity: {total_scores[most_likely]:.2f})")
