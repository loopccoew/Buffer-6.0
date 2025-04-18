# import os
# import cv2
# import numpy as np
# from skimage.feature import local_binary_pattern
# from sklearn.neighbors import KDTree
# import streamlit as st
# from PIL import Image
# import json
# import sys

# from core.feature_extraction.extractor import extract_features
# from core.feature_extraction.loading import load_kdtree, load_labels, load_label_map
# from core.severity_scoring.severity_minheap import SeverityScorer, SEVERITY_WEIGHTS
# from core.treatment_suggester.treatment_map import TreatmentMap
# from core.symptom_checker.symptom_trie import SymptomTrie

# # Adjust system path if needed
# sys.path.append('C:/Users/aryas/OneDrive/Desktop/skin_disease_detector')

# # Dataset and constants
# DATASET_PATH = '/kaggle/input/skin-diseases-image-dataset/IMG_CLASSES'
# IMG_SIZE = (128, 128)

# # Load necessary resources
# tree = load_kdtree()
# labels = load_labels()
# label_map = load_label_map()
# treatment_map = TreatmentMap(r'C:\Users\aryas\OneDrive\Desktop\skin_disease_detector\data\disease_info.csv')
# scorer = SeverityScorer()

# # Streamlit UI
# st.set_page_config(page_title="Skin Disease Detector", layout="wide")
# st.title("🩺 AI-Powered Skin Disease Analyzer")

# tab1, tab2 = st.tabs(["📷 Disease Detection", "📝 Symptom Checker"])

# # ------------------ TAB 1 ------------------
# with tab1:
#     uploaded_file = st.file_uploader("Upload a skin image", type=['jpg', 'jpeg', 'png'])

#     if uploaded_file:
#         image = Image.open(uploaded_file)
#         st.image(image, caption="Uploaded Image", use_container_width=True)

#         img_cv = np.array(image.convert('RGB'))[:, :, ::-1]
#         feat = extract_features(img_cv).reshape(1, -1)

#         dist, idx = tree.query(feat, k=5)

#         st.subheader("🔍 Top Predictions:")
#         diseases = []
#         for i in range(len(idx[0])):
#             label_id = labels[idx[0][i]]
#             # If label_map uses integer keys:
#             disease = label_map.get(label_id, f"Disease_{label_id}")
#             # If label_map uses string keys:
#             disease = label_map.get(str(label_id), f"Disease_{label_id}")

#             diseases.append(disease)
#             score = round(1 / (dist[0][i] + 1e-6), 2)
#             scorer.add_raw_score(disease, score)
#             st.markdown(f"**{disease}** (Score: {score})")

#         _, top_disease = scorer.get_most_severe()
#         if not isinstance(top_disease, str):
#             top_disease = str(top_disease)

#         st.success(f"🧪 Most Likely: **{top_disease}**")

#         st.subheader("📊 Severity Scoring")
#         with st.form("severity_form"):
#             pain_level = st.slider("Pain Level", 0, 10, 5)
#             itching_level = st.slider("Itching Level", 0, 10, 5)
#             duration_days = st.number_input("Duration of Symptoms (days)", min_value=0, value=5)
#             max_duration = st.number_input("Max Expected Duration (days)", min_value=1, value=10)
#             area_factor = st.slider("Affected Area Percentage", 0.0, 1.0, 0.2)
#             fever_present = st.checkbox("Fever Present?")
#             bleeding = st.checkbox("Bleeding Observed?")
#             spread_rate_factor = st.slider("Spread Rate (0.0 - stable to 1.0 - spreading fast)", 0.0, 1.0, 0.3)
#             submitted = st.form_submit_button("Compute Severity")

#         if submitted:
#             def compute_weighted_severity(disease):
#                 weights = SEVERITY_WEIGHTS.get(disease, {})
#                 score = (
#                     weights.get("pain_level", 0) * (pain_level / 10) +
#                     weights.get("itching_level", 0) * (itching_level / 10) +
#                     weights.get("duration_days", 0) * (duration_days / max_duration) +
#                     weights.get("area_factor", 0) * area_factor +
#                     weights.get("fever_present", 0) * (1 if fever_present else 0) +
#                     weights.get("bleeding", 0) * (1 if bleeding else 0) +
#                     weights.get("spread_rate_factor", 0) * spread_rate_factor
#                 )
#                 return max(round(score, 3), 0.01)

#             severity_score = compute_weighted_severity(top_disease)
#             st.metric(label=f"🔥 Severity Score for {top_disease}", value=severity_score)

#         st.subheader("💊 Recommended Treatment:")
#         try:
#             treatment_info = treatment_map.get_treatment(top_disease)
#         except Exception as e:
#             treatment_info = f"Error fetching treatment info: {str(e)}"
#         st.info(treatment_info)

# # ------------------ TAB 2 ------------------
# with tab2:
#     st.subheader("🧠 Enter Symptoms to Predict Possible Diseases")
#     symptoms_input = st.text_input("Enter symptoms separated by commas (e.g. itchy skin, redness, dry patches)")

#     if symptoms_input:
#         symptoms = [s.strip() for s in symptoms_input.split(',') if s.strip()]
#         trie = SymptomTrie()

#         # Sample mappings
#         trie.insert("itchy skin", "Eczema")
#         trie.insert("redness", "Rosacea")
#         trie.insert("dry patches", "Psoriasis")
#         trie.insert("oozing blisters", "Impetigo")
#         trie.insert("scaly rash", "Ringworm")

#         all_diseases = set()
#         for symptom in symptoms:
#             matched = trie.search(symptom)
#             all_diseases.update(matched)

#         if all_diseases:
#             st.success("🩻 Possible Diseases Based on Symptoms:")
#             for dis in sorted(all_diseases):
#                 st.markdown(f"- **{dis}**")
#                 st.markdown(f"  💊 *{treatment_map.get_treatment(dis)}*")
#         else:
#             st.warning("No matches found. Try different or simpler symptom terms.")

import os
import glob
import cv2
import numpy as np
from skimage.feature import local_binary_pattern
from sklearn.neighbors import KDTree
import streamlit as st
from PIL import Image
from core.feature_extraction.extractor import extract_features
from core.feature_extraction.loading import load_kdtree, load_labels, load_label_map
from core.severity_scoring.severity_minheap import SeverityScorer
from core.treatment_suggester.treatment_map import TreatmentMap
from core.symptom_checker.symptom_trie import SymptomTrie
from core.severity_scoring.severity_minheap import SEVERITY_WEIGHTS
import sys
import json
with open("data/disease_labels.json") as f:
    label_map = json.load(f)
sys.path.append('C:/Users/aryas/OneDrive/Desktop/skin_disease_detector')

# 👇 Add disease_labels here
disease_labels = {
    0: "Eczema",
    1: "Melanoma",
    2: "Psoriasis",
    3: "Atopic Dermatitis",
    4: "Basal Cell Carcinoma (BCC)",
    5: "Actinic Keratosis"
}

# Dataset path and image size constants
DATASET_PATH = '/kaggle/input/skin-diseases-image-dataset/IMG_CLASSES'
IMG_SIZE = (128, 128)  # Resize images

# 1. Helper Function to Extract Features
def extract_features(image):
    # Resize
    image = cv2.resize(image, IMG_SIZE)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    # Color Histogram
    hist = cv2.calcHist([image], [0, 1, 2], None, [8, 8, 8],
                        [0, 256, 0, 256, 0, 256])
    hist = cv2.normalize(hist, hist).flatten()

    # LBP Texture Features
    lbp = local_binary_pattern(gray, P=8, R=1, method='uniform')
    (lbp_hist, _) = np.histogram(lbp.ravel(),
                                 bins=np.arange(0, 10),
                                 range=(0, 9))
    lbp_hist = lbp_hist.astype("float")
    lbp_hist /= (lbp_hist.sum() + 1e-7)

    # Final Feature Vector
    features = np.hstack([hist, lbp_hist])
    return features

# ==================== Streamlit Interface ====================
# Load pre-trained model
tree = load_kdtree()  # Pre-trained KDTree for image feature matching
labels = load_labels()  # Labels corresponding to each feature vector
label_map = load_label_map()  # Mapping of label IDs to disease names
treatment_map = TreatmentMap(r'C:\Users\aryas\OneDrive\Desktop\skin_disease_detector\data\disease_info.csv')
# Initialize Severity Scorer
scorer = SeverityScorer()

st.set_page_config(page_title="Skin Disease Detector", layout="wide")
st.title("🩺 AI-Powered Skin Disease Analyzer")

tab1, tab2 = st.tabs(["📷 Disease Detection", "📝 Symptom Checker"])

# ===================== TAB 1: IMAGE DIAGNOSIS ====================
with tab1:
    uploaded_file = st.file_uploader("Upload a skin image", type=['jpg', 'jpeg', 'png'])

    if uploaded_file is None:
        st.warning("Please upload a valid image file.")

    if uploaded_file:
        # Display uploaded image
        image = Image.open(uploaded_file)
        st.image(image, caption="Uploaded Image", use_container_width=True)

        img_cv = np.array(image.convert('RGB'))[:, :, ::-1]  # Convert to OpenCV format
        feat = extract_features(img_cv)  # Extract features

        # Ensure feat is a 1D array (reshape if necessary)
        feat = feat.reshape(1, -1)

        # Perform the KD-tree query to find the closest matches
        dist, idx = tree.query(feat, k=5)

        # Display top disease predictions and severity scoring
        st.subheader("🔍 Top Predictions:")
        diseases = []
        for i in range(len(idx[0])):
            label_id = labels[idx[0][i]]

            # Fetch disease name from label_map
            disease = label_map.get(str(label_id), f"Disease_{label_id}")
            diseases.append(disease)
            score = round(1 / (dist[0][i] + 1e-6), 2)  # Avoid division by zero
            
            scorer.add_raw_score(disease, score)
            st.markdown(f"**{disease}** (Score: {score})")

        # Get most severe prediction
        _, top_disease = scorer.get_most_severe()

        # Ensure top_disease is a string
        if not isinstance(top_disease, str):
            top_disease = str(top_disease)

        st.success(f"🧪 Most Likely: **{top_disease}**")

        st.subheader("📊 Severity Scoring")

        # Severity scoring form
        with st.form("severity_form"):
            pain_level = st.slider("Pain Level", 0, 10, 5)
            itching_level = st.slider("Itching Level", 0, 10, 5)
            duration_days = st.number_input("Duration of Symptoms (days)", min_value=0, value=5)
            max_duration = st.number_input("Max Expected Duration (days)", min_value=1, value=10)
            area_factor = st.slider("Affected Area Percentage", 0.0, 1.0, 0.2)
            fever_present = st.checkbox("Fever Present?")
            bleeding = st.checkbox("Bleeding Observed?")
            spread_rate_factor = st.slider("Spread Rate (0.0 - stable to 1.0 - spreading fast)", 0.0, 1.0, 0.3)
            submitted = st.form_submit_button("Compute Severity")

        if submitted:
            def compute_weighted_severity(disease):
                weights = SEVERITY_WEIGHTS.get(disease, {})
                score = (
                    weights.get("pain_level", 0) * (pain_level / 10) +
                    weights.get("itching_level", 0) * (itching_level / 10) +
                    weights.get("duration_days", 0) * (duration_days / max_duration) +
                    weights.get("area_factor", 0) * area_factor +
                    weights.get("fever_present", 0) * (1 if fever_present else 0) +
                    weights.get("bleeding", 0) * (1 if bleeding else 0) +
                    weights.get("spread_rate_factor", 0) * spread_rate_factor
                )
                return max(round(score, 3), 0.01)  # Ensure score isn't zero

            severity_score = compute_weighted_severity(top_disease)
            st.metric(label=f"🔥 Severity Score for {top_disease}", value=severity_score)

        st.subheader("💊 Recommended Treatment:")

        

        # Assuming treatment_map is already loaded in the code
        try:
            treatment_info = treatment_map.get_treatment(top_disease)
        except Exception as e:
            treatment_info = f"Error fetching treatment info: {str(e)}"

        st.info(treatment_info)

# ===================== TAB 2: SYMPTOM CHECKER ====================
with tab2:
    st.subheader("🧠 Enter Symptoms to Predict Possible Diseases")
    symptoms_input = st.text_input("Enter symptoms separated by commas (e.g. itchy skin, redness, dry patches)")

    if symptoms_input:
        symptoms = [s.strip() for s in symptoms_input.split(',') if s.strip()]
        trie = SymptomTrie()

        # Sample symptom-disease mappings (can be expanded or loaded from CSV)
        trie.insert("itchy skin", "Eczema")
        trie.insert("redness", "Rosacea")
        trie.insert("dry patches", "Psoriasis")
        trie.insert("oozing blisters", "Impetigo")
        trie.insert("scaly rash", "Ringworm")

        all_diseases = set()
        for symptom in symptoms:
            matched = trie.search(symptom)
            all_diseases.update(matched)

        if all_diseases:
            st.success("🩻 Possible Diseases Based on Symptoms:")
            for dis in sorted(all_diseases):
                st.markdown(f"- **{dis}**")
                st.markdown(f"  💊 *{treatment_map.get_treatment(dis)}*")
        else:
            st.warning("No matches found. Try different or simpler symptom terms.")


# # # # import streamlit as st
# # # # import cv2
# # # # import numpy as np
# # # # from PIL import Image

# # # # from core.feature_extraction.extractor import extract_features
# # # # from core.feature_extraction.loading import load_kdtree, load_labels, load_label_map
# # # # from core.severity_scoring.severity_minheap import SeverityScorer
# # # # from core.symptom_checker.symptom_trie import SymptomTrie
# # # # from core.treatment_suggester.treatment_map import TreatmentMap
# # # # from core.severity_scoring.severity_minheap import load_severity_weights, SEVERITY_WEIGHTS

# # # # # Load weights at the beginning
# # # # load_severity_weights("data/severity_weights.csv")

# # # # # Load model/data only once
# # # # tree = load_kdtree()
# # # # labels = load_labels()
# # # # label_map = load_label_map()
# # # # treatment_map = TreatmentMap('data/disease_info.csv')

# # # # st.set_page_config(page_title="Skin Disease Detector", layout="wide")
# # # # st.title("🩺 AI-Powered Skin Disease Analyzer")

# # # # tab1, tab2 = st.tabs(["📷 Disease Detection", "📝 Symptom Checker"])

# # # # # ===================== TAB 1: IMAGE DIAGNOSIS ====================
# # # # with tab1:
# # # #     uploaded_file = st.file_uploader("Upload a skin image", type=['jpg', 'jpeg', 'png'])

# # # #     if uploaded_file is None:
# # # #         st.warning("Please upload a valid image file.")

# # # #     if uploaded_file:
# # # #         image = Image.open(uploaded_file)
# # # #         st.image(image, caption="Uploaded Image", use_container_width=True)

# # # #         img_cv = np.array(image.convert('RGB'))[:, :, ::-1]
# # # #         feat = extract_features(img_cv)
# # # #         dist, idx = tree.query([feat], k=5)

# # # #         st.subheader("🔍 Top Predictions:")
# # # #         diseases = []
# # # #         scorer = SeverityScorer()

# # # #         for i in range(len(idx[0])):
# # # #             label_id = labels[idx[0][i]]

# # # #             # Ensure label_id is str and mapped correctly
# # # #             disease = label_map.get(str(label_id), f"Disease_{label_id}")

# # # #             diseases.append(disease)
# # # #             score = round(1 / (dist[0][i] + 1e-6), 2)  # Avoid division by zero
# # # #             scorer.add_raw_score(disease, score)
# # # #             # Display disease names, not scores
# # # #             st.markdown(f"**{disease}** (Score: {score})")

# # # #         # Get most severe prediction (disease name)
# # # #         _, top_disease = scorer.get_most_severe()

# # # #         # Ensure it's a string
# # # #         if not isinstance(top_disease, str):
# # # #             top_disease = str(top_disease)

# # # #         # Display the most likely disease
# # # #         st.success(f"🧪 Most Likely: **{top_disease}**")

# # # #         st.subheader("📊 Severity Scoring")

# # # #         with st.form("severity_form"):
# # # #             pain_level = st.slider("Pain Level", 0, 10, 5)
# # # #             itching_level = st.slider("Itching Level", 0, 10, 5)
# # # #             duration_days = st.number_input("Duration of Symptoms (days)", min_value=0, value=5)
# # # #             max_duration = st.number_input("Max Expected Duration (days)", min_value=1, value=10)
# # # #             area_factor = st.slider("Affected Area Percentage", 0.0, 1.0, 0.2)
# # # #             fever_present = st.checkbox("Fever Present?")
# # # #             bleeding = st.checkbox("Bleeding Observed?")
# # # #             spread_rate_factor = st.slider("Spread Rate (0.0 - stable to 1.0 - spreading fast)", 0.0, 1.0, 0.3)
# # # #             submitted = st.form_submit_button("Compute Severity")

# # # #         if submitted:
# # # #             def compute_weighted_severity(disease):
# # # #                 weights = SEVERITY_WEIGHTS.get(disease, {})
# # # #                 score = (
# # # #                     weights.get("pain_level", 0) * (pain_level / 10) +
# # # #                     weights.get("itching_level", 0) * (itching_level / 10) +
# # # #                     weights.get("duration_days", 0) * (duration_days / max_duration) +
# # # #                     weights.get("area_factor", 0) * area_factor +
# # # #                     weights.get("fever_present", 0) * (1 if fever_present else 0) +
# # # #                     weights.get("bleeding", 0) * (1 if bleeding else 0) +
# # # #                     weights.get("spread_rate_factor", 0) * spread_rate_factor
# # # #                 )
# # # #                 return max(round(score, 3), 0.01)  # Ensure score isn't zero

# # # #             severity_score = compute_weighted_severity(top_disease)
# # # #             st.metric(label=f"🔥 Severity Score for {top_disease}", value=severity_score)

# # # #         st.subheader("💊 Recommended Treatment:")
# # # #         try:
# # # #             treatment_info = treatment_map.get_treatment(top_disease)
# # # #         except Exception as e:
# # # #             treatment_info = f"Error fetching treatment info: {str(e)}"

# # # #         st.info(treatment_info)


# # # # # ===================== TAB 2: SYMPTOM CHECKER ====================
# # # # with tab2:
# # # #     st.subheader("🧠 Enter Symptoms to Predict Possible Diseases")
# # # #     symptoms_input = st.text_input("Enter symptoms separated by commas (e.g. itchy skin, redness, dry patches)")

# # # #     if symptoms_input:
# # # #         symptoms = [s.strip() for s in symptoms_input.split(',') if s.strip()]
# # # #         trie = SymptomTrie()

# # # #         # Sample symptom-disease mappings (can be expanded or loaded from CSV)
# # # #         trie.insert("itchy skin", "Eczema")
# # # #         trie.insert("redness", "Rosacea")
# # # #         trie.insert("dry patches", "Psoriasis")
# # # #         trie.insert("oozing blisters", "Impetigo")
# # # #         trie.insert("scaly rash", "Ringworm")

# # # #         all_diseases = set()
# # # #         for symptom in symptoms:
# # # #             matched = trie.search(symptom)
# # # #             all_diseases.update(matched)

# # # #         if all_diseases:
# # # #             st.success("🩻 Possible Diseases Based on Symptoms:")
# # # #             for dis in sorted(all_diseases):
# # # #                 st.markdown(f"- **{dis}**")
# # # #                 st.markdown(f"  💊 *{treatment_map.get_treatment(dis)}*")
# # # #         else:
# # # #             st.warning("No matches found. Try different or simpler symptom terms.")



# # # import cv2
# # # import numpy as np
# # # from PIL import Image
# # # import streamlit as st
# # # from core.feature_extraction.extractor import extract_features
# # # from core.feature_extraction.loading import load_kdtree, load_labels, load_label_map
# # # from core.severity_scoring.severity_minheap import SeverityScorer
# # # from core.symptom_checker.symptom_trie import SymptomTrie
# # # from core.treatment_suggester.treatment_map import TreatmentMap
# # # from core.severity_scoring.severity_minheap import load_severity_weights, SEVERITY_WEIGHTS

# # # # Load weights at the beginning
# # # load_severity_weights("data/severity_weights.csv")

# # # # Load model/data only once
# # # tree = load_kdtree()
# # # labels = load_labels()
# # # label_map = load_label_map()  # label_map should map label_id (int) → disease_name (str)
# # # treatment_map = TreatmentMap('data/disease_info.csv')

# # # st.set_page_config(page_title="Skin Disease Detector", layout="wide")
# # # st.title("🩺 Skin Disease Analyzer")

# # # tab1, tab2 = st.tabs(["📷 Disease Detection", "📝 Symptom Checker"])

# # # # ===================== TAB 1: IMAGE DIAGNOSIS ====================
# # # with tab1:
# # #     uploaded_file = st.file_uploader("Upload a skin image", type=['jpg', 'jpeg', 'png'])

# # #     if uploaded_file is None:
# # #         st.warning("Please upload a valid image file.")

# # #     if uploaded_file:
# # #         image = Image.open(uploaded_file)
# # #         st.image(image, caption="Uploaded Image", use_container_width=True)

# # #         img_cv = np.array(image.convert('RGB'))[:, :, ::-1]
# # #         feat = extract_features(img_cv)
# # #         dist, idx = tree.query([feat], k=5)

# # #         st.subheader("🔍 Top Predictions:")
# # #         diseases = []
# # #         scorer = SeverityScorer()

# # #         for i in range(len(idx[0])):
# # #             label_id = labels[idx[0][i]]  # Ensure label_id is int
# # #             disease = label_map.get(label_id, f"Disease_{label_id}")  # Use int directly

# # #             diseases.append(disease)
# # #             score = round(1 / (dist[0][i] + 1e-6), 2)  # Avoid division by zero
# # #             scorer.add_raw_score(disease, score)
# # #             st.markdown(f"{disease}** (Score: {score})")

# # #         # Correct unpacking: (disease, score)
# # #         top_disease, top_score = scorer.get_most_severe()

# # #         # Display the most likely disease
# # #         st.success(f"🧪 Most Likely: *{top_disease}*")

# # #         st.subheader("📊 Severity Scoring")

# # #         with st.form("severity_form"):
# # #             pain_level = st.slider("Pain Level", 0, 10, 5)
# # #             itching_level = st.slider("Itching Level", 0, 10, 5)
# # #             duration_days = st.number_input("Duration of Symptoms (days)", min_value=0, value=5)
# # #             max_duration = st.number_input("Max Expected Duration (days)", min_value=1, value=10)
# # #             area_factor = st.slider("Affected Area Percentage", 0.0, 1.0, 0.2)
# # #             fever_present = st.checkbox("Fever Present?")
# # #             bleeding = st.checkbox("Bleeding Observed?")
# # #             spread_rate_factor = st.slider("Spread Rate (0.0 - stable to 1.0 - spreading fast)", 0.0, 1.0, 0.3)
# # #             submitted = st.form_submit_button("Compute Severity")

# # #         if submitted and top_disease:
# # #             def compute_weighted_severity(disease):
# # #                 weights = SEVERITY_WEIGHTS.get(disease, {})
# # #                 score = (
# # #                     weights.get("pain_level", 0) * (pain_level / 10) +
# # #                     weights.get("itching_level", 0) * (itching_level / 10) +
# # #                     weights.get("duration_days", 0) * (duration_days / max_duration) +
# # #                     weights.get("area_factor", 0) * area_factor +
# # #                     weights.get("fever_present", 0) * (1 if fever_present else 0) +
# # #                     weights.get("bleeding", 0) * (1 if bleeding else 0) +
# # #                     weights.get("spread_rate_factor", 0) * spread_rate_factor
# # #                 )
# # #                 return max(round(score, 3), 0.01)  # Ensure score isn't zero

# # #             severity_score = compute_weighted_severity(top_disease)
# # #             st.metric(label=f"🔥 Severity Score for {top_disease}", value=severity_score)

# # #         st.subheader("💊 Recommended Treatment:")
# # #         try:
# # #             treatment_info = treatment_map.get_treatment(top_disease)
# # #         except Exception as e:
# # #             treatment_info = f"Error fetching treatment info: {str(e)}"

# # #         st.info(treatment_info)

# # # # ===================== TAB 2: SYMPTOM CHECKER ====================
# # # with tab2:
# # #     st.subheader("🧠 Enter Symptoms to Predict Possible Diseases")
# # #     symptoms_input = st.text_input("Enter symptoms separated by commas (e.g. itchy skin, redness, dry patches)")

# # #     if symptoms_input:
# # #         symptoms = [s.strip() for s in symptoms_input.split(',') if s.strip()]
# # #         trie = SymptomTrie()

# # #         # Sample symptom-disease mappings (can be expanded or loaded from CSV)
# # #         trie.insert("itchy skin", "Eczema")
# # #         trie.insert("redness", "Rosacea")
# # #         trie.insert("dry patches", "Psoriasis")
# # #         trie.insert("oozing blisters", "Impetigo")
# # #         trie.insert("scaly rash", "Ringworm")

# # #         all_diseases = set()
# # #         for symptom in symptoms:
# # #             matched = trie.search(symptom)
# # #             all_diseases.update(matched)

# # #         if all_diseases:
# # #             st.success("🩻 Possible Diseases Based on Symptoms:")
# # #             for dis in sorted(all_diseases):
# # #                 st.markdown(f"- *{dis}*")
# # #                 st.markdown(f"  💊 {treatment_map.get_treatment(dis)}")
# # #         else:
# # #             st.warning("No matches found. Try different or simpler symptom terms.")


# # # # Load weights at the beginning
# # # load_severity_weights("data/severity_weights.csv")

# # # # Load model/data only once
# # # tree = load_kdtree()
# # # labels = load_labels()
# # # label_map = load_label_map()  # label_map should map label_id (int) → disease_name (str)
# # # treatment_map = TreatmentMap('data/disease_info.csv')

# # # st.set_page_config(page_title="Skin Disease Detector", layout="wide")
# # # st.title("🩺 AI-Powered Skin Disease Analyzer")

# # # tab1, tab2 = st.tabs(["📷 Disease Detection", "📝 Symptom Checker"])

# # # # ===================== TAB 1: IMAGE DIAGNOSIS ====================
# # # with tab1:
# # #     uploaded_file = st.file_uploader("Upload a skin image", type=['jpg', 'jpeg', 'png'])

# # #     if uploaded_file is None:
# # #         st.warning("Please upload a valid image file.")

# # #     if uploaded_file:
# # #         image = Image.open(uploaded_file)
# # #         st.image(image, caption="Uploaded Image", use_container_width=True)

# # #         img_cv = np.array(image.convert('RGB'))[:, :, ::-1]
# # #         feat = extract_features(img_cv)
# # #         dist, idx = tree.query([feat], k=5)

# # #         st.subheader("🔍 Top Predictions:")
# # #         diseases = []
# # #         scorer = SeverityScorer()

# # #         for i in range(len(idx[0])):
# # #             label_id = labels[idx[0][i]]  # Ensure label_id is int
# # #             disease = label_map.get(label_id, f"Disease_{label_id}")  # Use int directly

# # #             diseases.append(disease)
# # #             score = round(1 / (dist[0][i] + 1e-6), 2)  # Avoid division by zero
# # #             scorer.add_raw_score(disease, score)
# # #             st.markdown(f"{disease}** (Score: {score})")

# # #         # Correct unpacking: (disease, score)
# # #         top_disease, top_score = scorer.get_most_severe()

# # #         # Display the most likely disease
# # #         st.success(f"🧪 Most Likely: *{top_disease}*")

# # #         st.subheader("📊 Severity Scoring")

# # #         with st.form("severity_form"):
# # #             pain_level = st.slider("Pain Level", 0, 10, 5)
# # #             itching_level = st.slider("Itching Level", 0, 10, 5)
# # #             duration_days = st.number_input("Duration of Symptoms (days)", min_value=0, value=5)
# # #             max_duration = st.number_input("Max Expected Duration (days)", min_value=1, value=10)
# # #             area_factor = st.slider("Affected Area Percentage", 0.0, 1.0, 0.2)
# # #             fever_present = st.checkbox("Fever Present?")
# # #             bleeding = st.checkbox("Bleeding Observed?")
# # #             spread_rate_factor = st.slider("Spread Rate (0.0 - stable to 1.0 - spreading fast)", 0.0, 1.0, 0.3)
# # #             submitted = st.form_submit_button("Compute Severity")

# # #         if submitted and top_disease:
# # #             def compute_weighted_severity(disease):
# # #                 weights = SEVERITY_WEIGHTS.get(disease, {})
# # #                 score = (
# # #                     weights.get("pain_level", 0) * (pain_level / 10) +
# # #                     weights.get("itching_level", 0) * (itching_level / 10) +
# # #                     weights.get("duration_days", 0) * (duration_days / max_duration) +
# # #                     weights.get("area_factor", 0) * area_factor +
# # #                     weights.get("fever_present", 0) * (1 if fever_present else 0) +
# # #                     weights.get("bleeding", 0) * (1 if bleeding else 0) +
# # #                     weights.get("spread_rate_factor", 0) * spread_rate_factor
# # #                 )
# # #                 return max(round(score, 3), 0.01)  # Ensure score isn't zero

# # #             severity_score = compute_weighted_severity(top_disease)
# # #             st.metric(label=f"🔥 Severity Score for {top_disease}", value=severity_score)

# # #         st.subheader("💊 Recommended Treatment:")
# # #         try:
# # #             treatment_info = treatment_map.get_treatment(top_disease)
# # #         except Exception as e:
# # #             treatment_info = f"Error fetching treatment info: {str(e)}"

# # #         st.info(treatment_info)

# # # # ===================== TAB 2: SYMPTOM CHECKER ====================
# # # with tab2:
# # #     st.subheader("🧠 Enter Symptoms to Predict Possible Diseases")
# # #     symptoms_input = st.text_input("Enter symptoms separated by commas (e.g. itchy skin, redness, dry patches)")

# # #     if symptoms_input:
# # #         symptoms = [s.strip() for s in symptoms_input.split(',') if s.strip()]
# # #         trie = SymptomTrie()

# # #         # Sample symptom-disease mappings (can be expanded or loaded from CSV)
# # #         trie.insert("itchy skin", "Eczema")
# # #         trie.insert("redness", "Rosacea")
# # #         trie.insert("dry patches", "Psoriasis")
# # #         trie.insert("oozing blisters", "Impetigo")
# # #         trie.insert("scaly rash", "Ringworm")

# # #         all_diseases = set()
# # #         for symptom in symptoms:
# # #             matched = trie.search(symptom)
# # #             all_diseases.update(matched)

# # #         if all_diseases:
# # #             st.success("🩻 Possible Diseases Based on Symptoms:")
# # #             for dis in sorted(all_diseases):
# # #                 st.markdown(f"- *{dis}*")
# # #                 st.markdown(f"  💊 {treatment_map.get_treatment(dis)}")
# # #         else:
# # #             st.warning("No matches found. Try different or simpler symptom terms.")

# import os
# import cv2
# import numpy as np
# from skimage.feature import local_binary_pattern
# from sklearn.neighbors import KDTree
# import streamlit as st
# from PIL import Image
# import json
# import sys

# # Adjust system path if needed
# sys.path.append('C:/Users/aryas/OneDrive/Desktop/skin_disease_detector')

# # Constants
# DATASET_PATH = '/kaggle/input/skin-diseases-image-dataset/IMG_CLASSES'
# IMG_SIZE = (128, 128)

# # Feature Extraction
# def extract_features(image):
#     image = cv2.resize(image, IMG_SIZE)
#     gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    
#     hist = cv2.calcHist([image], [0, 1, 2], None, [8, 8, 8], [0, 256, 0, 256, 0, 256])
#     hist = cv2.normalize(hist, hist).flatten()
    
#     lbp = local_binary_pattern(gray, P=8, R=1, method='uniform')
#     lbp_hist, _ = np.histogram(lbp.ravel(), bins=np.arange(0, 10), range=(0, 9))
#     lbp_hist = lbp_hist.astype("float")
#     lbp_hist /= (lbp_hist.sum() + 1e-7)
    
#     return np.hstack([hist, lbp_hist])

# # Load Resources
# def load_kdtree():
#     with open('kdtree.pkl', 'rb') as f:
#         return pickle.load(f)

# def load_labels():
#     return np.load('labels.npy')

# def load_label_map():
#     with open('label_map.pkl', 'rb') as f:
#         return pickle.load(f)

# class SeverityScorer:
#     def __init__(self):
#         self.scores = []

#     def add_raw_score(self, disease, score):
#         self.scores.append((disease, score))

#     def get_most_severe(self):
#         return max(self.scores, key=lambda x: x[1]) if self.scores else (None, None)

# class TreatmentMap:
#     def __init__(self, csv_path):
#         self.treatments = {}
#         # Implement CSV loading here

#     def get_treatment(self, disease):
#         return self.treatments.get(disease, "Treatment information not available")

# class SymptomTrie:
#     def __init__(self):
#         self.root = {}

#     def insert(self, symptom, disease):
#         node = self.root
#         for char in symptom:
#             node = node.setdefault(char, {})
#         node['DISEASE'] = disease

#     def search(self, symptom):
#         node = self.root
#         for char in symptom:
#             if char not in node:
#                 return []
#             node = node[char]
#         return [node.get('DISEASE', '')]

# # Initialize components
# tree = load_kdtree()
# labels = load_labels()
# label_map = load_label_map()
# treatment_map = TreatmentMap(r'C:\Users\aryas\OneDrive\Desktop\skin_disease_detector\data\disease_info.csv')
# scorer = SeverityScorer()

# # Streamlit UI
# st.set_page_config(page_title="Skin Disease Detector", layout="wide")
# st.title("🩺 AI-Powered Skin Disease Analyzer")

# tab1, tab2 = st.tabs(["📷 Disease Detection", "📝 Symptom Checker"])

# # ------------------ TAB 1 ------------------
# with tab1:
#     uploaded_file = st.file_uploader("Upload a skin image", type=['jpg', 'jpeg', 'png'])

#     if uploaded_file:
#         image = Image.open(uploaded_file)
#         st.image(image, caption="Uploaded Image", use_container_width=True)

#         img_cv = np.array(image.convert('RGB'))[:, :, ::-1]
#         feat = extract_features(img_cv).reshape(1, -1).astype(np.float32)

#         dist, idx = tree.query(feat, k=5)

#         # Get top disease
#         top_label_id = labels[idx[0][0]]
#         top_disease = label_map.get(str(top_label_id), f"Disease_{top_label_id}")
        
#         st.subheader("🔍 Top Predictions:")
#         diseases = []
#         for i in range(len(idx[0])):
#             label_id = labels[idx[0][i]]
#             disease = label_map.get(str(label_id), f"Disease_{label_id}")
#             diseases.append(disease)
#             score = round(1 / (dist[0][i] + 1e-6), 2)
#             scorer.add_raw_score(disease, score)
#             st.markdown(f"**{disease}** (Score: {score})")

#         st.success(f"🧪 Most Likely: **{top_disease}**")

#         st.subheader("📊 Severity Scoring")
#         with st.form("severity_form"):
#             pain_level = st.slider("Pain Level", 0, 10, 5)
#             itching_level = st.slider("Itching Level", 0, 10, 5)
#             duration_days = st.number_input("Duration of Symptoms (days)", min_value=0, value=5)
#             max_duration = st.number_input("Max Expected Duration (days)", min_value=1, value=10)
#             area_factor = st.slider("Affected Area Percentage", 0.0, 1.0, 0.2)
#             fever_present = st.checkbox("Fever Present?")
#             bleeding = st.checkbox("Bleeding Observed?")
#             spread_rate_factor = st.slider("Spread Rate (0.0 - stable to 1.0 - spreading fast)", 0.0, 1.0, 0.3)
#             submitted = st.form_submit_button("Compute Severity")

#         if submitted:
#             def compute_weighted_severity(disease):
#                 weights = SEVERITY_WEIGHTS.get(disease.lower().replace(' ', '_'), {})
#                 score = (
#                     weights.get("pain_level", 0) * (pain_level / 10) +
#                     weights.get("itching_level", 0) * (itching_level / 10) +
#                     weights.get("duration_days", 0) * (duration_days / max_duration) +
#                     weights.get("area_factor", 0) * area_factor +
#                     weights.get("fever_present", 0) * (1 if fever_present else 0) +
#                     weights.get("bleeding", 0) * (1 if bleeding else 0) +
#                     weights.get("spread_rate_factor", 0) * spread_rate_factor
#                 )
#                 return max(round(score, 3), 0.01)

#             severity_score = compute_weighted_severity(top_disease)
#             st.metric(label=f"🔥 Severity Score for {top_disease}", value=severity_score)
