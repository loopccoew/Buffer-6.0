# Buffer-6.0

# Women-Harassment-Detection
# Protect. Detect. Empower.
# Overview
Tech Features That Safeguard Women, One Message at a Time.
A Streamlit-based web app that detects online harassment from user-submitted text using an ML model. If harassment is detected, it encodes a report with message, sender details, and location metadata to help authorities take action.

# Data Structures Used
 **1.pandas.DataFrame**
 
 📍 Used in:
- Reading and displaying CSV data
- Passing user message input to the model
  
 🔎 Why:
- Structured format suitable for ML
- Compatible with `scikit-learn` pipelines

 **2.list**
 
 📍 Used in:
- Collecting values (labels, sizes) for plotting
- Creating the heap in Huffman encoding
  
 🔎 Why:
- Dynamic and easy to manipulate
- Supports priority queues via `heapq`

  **3. dict**
  
 📍 Used in:
- Storing frequency counts
- Huffman encoding map
- Reverse lookup during decoding
- IP and location API responses
  
 🔎 Why:
- O(1) average time complexity for lookups
- Flexible for structured data mapping

**4. defaultdict**

 📍 Used in:
- Counting character frequency in `create_frequency_dict`
  
 🔎 Why:
- Prevents key errors while incrementing
- Cleaner and safer syntax than regular `dict`

 **5. heapq` (Min Heap using list)**
 
 📍 Used in:
- `build_huffman_tree()` for character priority
  
 🔎 Why:
- Huffman coding needs the two least frequent elements repeatedly
- Efficient for implementing priority queue

 **6. tuple**
 
 📍 Used in:
- Storing `(char, encoding)` pairs
- Returning multiple values like latitude, longitude
  
 🔎 Why:
- Lightweight, immutable group of values
- Ideal for returning compact data from functions
- 
  **7. `str` (String)**
  
 📍 Used in:
- User inputs
- Huffman text compression
- File naming
- Report formatting

 🔎 Why:
- Native support for encoding/decoding operations
- Fast and memory-efficient for text
- 
  **8. Custom `Linked List`**
  
 📍 Used in:
- Organizing and displaying phone number and IP address metadata for reports
  
 🔎 Why:
- Good for sequential insertion without resizing
- Educational purpose — reinforces understanding of pointers and nodes
- 
  **9.`Huffman Tree` (Nested List/Tree Structure)**
  
 📍 Used in:
- build_huffman_tree() 
  The tree is built like:
  python
  [total_weight, [char1, code1], [char2, code2], ...]
  
 🔎 Why:
- The **binary tree** represents prefix codes for each character
- Left child adds `'0'`, right child adds `'1'`
- Helps generate **minimum average bit-length** encoding
 🔺 Example:
If you encode `"aabbbc"`, the tree might look like:
python
[6,
 ['c', '00'],
 ['a', '01'],
 ['b', '1']
]
Each `[char, code]` pair shows the binary code assigned to that character.

# Summary Table:
![image](https://github.com/user-attachments/assets/fcc0015c-52eb-4ae9-b10b-79b0b77e8132)

# Flow Daigram
![WhatsApp Image 2025-04-17 at 21 59 42_3e6437b4](https://github.com/user-attachments/assets/88e2323f-69d7-43ce-ba46-00a71ff717e8)

![WhatsApp Image 2025-04-17 at 22 45 00_cda38c13](https://github.com/user-attachments/assets/d2241ca9-05e6-47be-b579-2345f10bde87)

# 1.Information Available
![image](https://github.com/user-attachments/assets/a13cfb10-88fb-45d8-ad42-bea1deca4706)

# 2.Non Harassing Speech Detected
![WhatsApp Image 2025-04-17 at 23 21 27_f5bc0ba4](https://github.com/user-attachments/assets/6cb5473f-8761-43cf-8217-9ddcf4e986ee)

# 3.Harassing Speech Detected
![WhatsApp Image 2025-04-17 at 23 21 28_9b8df684](https://github.com/user-attachments/assets/f4bc6771-3121-4051-83df-3bec68a39269)

# 4.Encoded Text in Report Using Huffman algorithm
![WhatsApp Image 2025-04-17 at 23 21 27_868c8a86](https://github.com/user-attachments/assets/4d6e68b4-beee-4725-8df4-07014996036d)

![WhatsApp Image 2025-04-17 at 23 21 29_44b8360d](https://github.com/user-attachments/assets/361cd052-e2e5-48b4-97c6-e96f4eb3e5bf)

# 5.Approx location of sender's sim origin as per telecom registration
![WhatsApp Image 2025-04-17 at 23 21 29_16444168](https://github.com/user-attachments/assets/20d26316-91bb-4d14-833e-c90537414d6d)

# Model Accuracy
![image](https://github.com/user-attachments/assets/ec3f0f44-de68-4200-877f-2b2a9f3e4f79)

Accuracy: 0.9729675206778293
**Classification Report:**
                precision    recall  f1-score   support

    harassing       0.99      0.98      0.98      3731
not_harassing       0.94      0.96      0.95      1226

     accuracy                           0.97      4957
    macro avg       0.96      0.97      0.96      4957
 weighted avg       0.97      0.97      0.97      4957

# Key Features and their role in Women Safety.
**🔍 1. Real-Time Harassment Detection (ML-powered)**
Feature: Uses machine learning to classify messages as harassing or non-harassing.
Advantage: Immediate alert for any suspicious or abusive text content.
Why Use It: Reduces response time and prevents escalation.
Women’s Safety Link: Helps identify verbal abuse and threats at an early stage, encouraging timely intervention.

**2. AI-Powered Sentiment Analysis**
Feature: Analyzes emotional tone using logistic regression with TF-IDF vectorization.
Advantage: Understands the intensity and intent behind messages.
Why Use It: More than keyword-based — understands nuance.
Women’s Safety Link: Detects manipulation, gaslighting, or persistent threats often missed in simple filters.

**🔐 3. Huffman Encoding for Privacy**
Feature: Compresses and encrypts reports before storage.
Advantage: Protects sensitive data and reduces file size.
Why Use It: Ensures user safety, privacy, and report integrity.
Women’s Safety Link: Empowers victims to report safely without fear of exposure.

**🌍 4. Geo-location APIs (Phone/IP-based)**
Feature: Uses OpenCage (phone number) and IPinfo (IP address) to trace approximate location.
Advantage: Tracks suspect’s location without revealing victim’s location.
Why Use It: Builds credible reports that authorities can use.
Women’s Safety Link: Offers location-aware evidence, useful in legal escalation or emergency action.

**🧾 5. Auto-Generated Reports (LinkedList Structured)**
Feature: Final reports are created automatically in a clean, structured format.
Advantage: Saves time, ensures consistency, and makes filing easy.
Why Use It: Reduces emotional burden on the victim to explain everything repeatedly.
Women’s Safety Link: Supports quicker filing of complaints or proof submission to trusted authorities.

 **📊 6. Minimal UI with Streamlit**
Feature: Simple, intuitive interface.
Advantage: Anyone can use it — no tech skills required.
Why Use It: Lower barrier for entry, even for first-time users.
Women’s Safety Link: Encourages self-empowerment through easy-to-access protection tools.

# 🚺 Why It Matters for Women’s Safety
📱 **Accessible**: Even through mobile.
🧩 **Smart Detection**: ML adapts with more data.
🛡 **Private Reporting**: Secure, encoded, and discreet.
🧭 **Location Insight**: Helps trace origin of threats.
💬 **Psychological Signal Detection**: Picks up hidden or veiled abuse.

# 🔮 Future Work & Improvements
 **1. Advanced ML Models**
Upgrade to deep learning models like LSTM, BERT, or DistilBERT for better language understanding.
Fine-tune on larger datasets to improve accuracy and detect subtler forms of harassment.

**🌍 2. Multilingual Support**
Extend detection to regional and international languages to support diverse users.
Use libraries like spaCy, transformers, or Google Translate API.

 **🔐 3. End-to-End Encryption & Cloud Integration**
Implement secure cloud storage (e.g., AWS, Firebase) for report management.
Add end-to-end encryption to protect user data during transfer.

 **🤖 4. Real-Time Monitoring**
Integrate with platforms (email, social media, chat apps) for live scanning of abusive content.
Optional alert systems for parents, authorities, or platform moderators.

 **📱 5. Mobile App Version**
Build an Android/iOS app for easier, on-the-go reporting.
Could integrate voice-to-text input for accessibility.

**🧾 6. Automated Report Filing**
Connect directly with local cyber cells or women’s helplines.
Send auto-generated reports with minimal manual effort.

**🧪 7. Community Feedback & Learning**
Allow users to flag false positives/negatives to improve model accuracy over time.
Introduce feedback loops into model training pipeline.

# Refrences
https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/

https://thesai.org/Downloads/Volume15No3/Paper_103-Detection_of_Harassment_Toward_Women_in_Twitter.pdf

https://www.sciencedirect.com/science/article/abs/pii/S0140366420319101?via%3Dihub
