# fraud_detector.py
# DSA-backed fraud checker using dictionary

messages = {
    0: {
        "text": "Dear Customer, your recent transaction was successful. If you did not authorize this, please call us at 1800-XXXX-XXXX.",
        "is_phishing": False
    },
    1: {
        "text": "Your account is about to be locked! To prevent this, we need your account number and password immediately. Click this link to update now!",
        "is_phishing": True
    },
    2: {
        "text": "Hello! You’ve successfully registered for mobile banking. If this wasn’t you, call us right away at 1800-XXXX-XXXX.",
        "is_phishing": False
    },
    3: {
        "text": "URGENT: You’ve won a prize of ₹50,000! To claim your prize, please send us your bank details and password immediately.",
        "is_phishing": True
    }
}

def check_answer(msg_id, user_answer):
    correct = messages[msg_id]["is_phishing"]
    return (user_answer == "red" and correct) or (user_answer == "green" and not correct)
