import streamlit as st
import pandas as pd
import pickle
import os
from datetime import datetime
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report
from Haffmun_Encoding import huffman_compress_decompress

st.sidebar.image(r'C:/Users/prite/Downloads/Women Harassment/Women Harassment/Logo.png')

s = st.sidebar.selectbox("Know About Women Harassment and Legal Help", ("Women Harassment", "Helpline & Laws"))

if s == "Women Harassment":
    st.sidebar.write('''
    Women harassment refers to a range of abusive behaviors directed toward women, including physical, verbal, emotional, and online abuse.
    This can happen at home, workplace, public spaces, or on the internet. 
    Harassment impacts a woman's mental and physical well-being and is a serious violation of human rights.

    Types of harassment include:
    - Sexual Harassment(workplace, public transport, etc.)
    - Verbal Abuse
    - Domestic Violence
    - Online Abuse (cyberstalking, obscene messages)
    - Stalking
    - Threats and Intimidation
    ''')

elif s == "Helpline & Laws":
    st.sidebar.write('''
    Important Helpline Numbers (India):
    - Women Helpline (All India): 1091
    - National Commission for Women (NCW): 7827170170 (WhatsApp)
    - Police Emergency Services: 112
    - Cyber Crime Helpline:** 1930 or [cybercrime.gov.in](https://www.cybercrime.gov.in)

    Key Laws Protecting Women in India:
    - Section 354 (IPC): Assault or criminal force to woman with intent to outrage her modesty.
    - Section 509 (IPC): Word, gesture or act intended to insult the modesty of a woman.
    - The Sexual Harassment of Women at Workplace (Prevention, Prohibition and Redressal) Act, 2013
    - Protection of Women from Domestic Violence Act, 2005
    - Information Technology Act, 2000– Covers cyber harassment, cyberstalking, etc.

    You are not alone. Speak up and reach out. Help is available.
    ''')


st.image(r"C:/Users/prite/Downloads/Women Harassment/Women Harassment/PB_Banner 1.png")

# Streamlit App Interface
st.title("Harassment Detection")

# Display the dataset
#st.write("Dataset")
#st.write(df.head())

# Text input
input_text = st.text_area("Enter Your Received Message or comment from Unknown Persons:")



# Initialize session flags
if "label" not in st.session_state:
    st.session_state.label = None
if "encoded_ready" not in st.session_state:
    st.session_state.encoded_ready = False
if "report_sent" not in st.session_state:
    st.session_state.report_sent = False

# Load model once
if "model" not in st.session_state:
        with open('C:/Users/prite/Downloads/Women Harassment/harassment_detection.pkl', 'rb') as model_file:
            model = pickle.load(model_file)
# Run prediction
if st.button("DETECT"):
    if input_text:
        input_data = pd.DataFrame({'tweet': [input_text]})
        st.session_state.label = model.predict(input_data)[0]
        st.success("Message analyzed successfully.")

# Display based on prediction
if st.session_state.label == 'not_harassing':
    st.image(r"C:/Users/prite/Downloads/Women Harassment/Women Harassment/normal.png", caption='Normal', width=200)

elif st.session_state.label == 'harassing':
    st.image(r"C:/Users/prite/Downloads/Women Harassment/Women Harassment/hatespeech.png", caption='Hate Speech', width=200)
    st.markdown("### ⚠️ Harassment Detected - Please Provide Sender's Info")

    sender_name = st.text_input("Enter Sender's Name (Optional)")
    phone_number = st.text_input("Enter Sender's Phone Number")
    message_date = st.date_input("On Which Date Message Was Received")
    message_time = st.time_input("At What Time Was the Message Received")

    # Encode report button
    if st.button("🚨 Process & Encode Report") or st.session_state.encoded_ready:
        full_report = f"""Sender Name: {sender_name}
Phone Number: {phone_number}
Date: {message_date}
Time: {message_time}

Message:
{input_text}"""

        encoded, decoded = huffman_compress_decompress(full_report)
        st.session_state.encoded_ready = True  # Flag to stay on this part
        st.session_state.decoded = decoded     # Save for later use

        encoded_filename = f"reports_encoded/encoded_report_{datetime.now().strftime('%Y%m%d_%H%M%S')}.txt"
        os.makedirs("reports_encoded", exist_ok=True)
        with open(encoded_filename, "w") as f:
            f.write(encoded)

        with open(encoded_filename, "rb") as f:
            st.download_button("⬇️ Download Encoded Report", f, file_name=os.path.basename(encoded_filename), mime="text/plain")

        st.success("✅ Report processed and encoded successfully.")
        st.warning("⚠️ This will send the report to authorities. Ensure information is correct.")
        # Location preview section
        if st.button("📍Tap to view possible origin of this number (estimated from SIM registration data)"):
            from phonenumbers import geocoder as geo
            from phonenumbers import carrier
            import phonenumbers
            from opencage.geocoder import OpenCageGeocode

            try:
                parsed_number = phonenumbers.parse(phone_number)
                location = geo.description_for_number(parsed_number, "en")
                provider = carrier.name_for_number(parsed_number, "en")

                open_cage_key = "6d6f969fd9024ac8afde957f0c86a5ba"
                opencage_geocoder = OpenCageGeocode(open_cage_key)
                geo_results = opencage_geocoder.geocode(location)

                if geo_results:
                    phone_lat = geo_results[0]['geometry']['lat']
                    phone_lng = geo_results[0]['geometry']['lng']
                    map_data = pd.DataFrame({'lat': [phone_lat], 'lon': [phone_lng]})
                    st.success(f"Approximate Location: {location} (Carrier: {provider})")
                    st.map(map_data)
                else:
                    st.warning("⚠️ Unable to determine precise coordinates. Try again or check the phone number.")

            except Exception as e:
                st.error(f"❌ Error while fetching location: {str(e)}")

        # Report to authority button
        if st.button("📤 Report to Authority") and not st.session_state.report_sent:
            class DetailNode:
                def __init__(self, label, value):
                    self.label = label
                    self.value = value
                    self.next = None

            class DetailLinkedList:
                def __init__(self):
                    self.head = None

                def add_detail(self, label, value):
                    node = DetailNode(label, value)
                    if not self.head:
                        self.head = node
                    else:
                        current = self.head
                        while current.next:
                            current = current.next
                        current.next = node

                def get_string(self):
                    details = ""
                    current = self.head
                    while current:
                        details += f"{current.label}: {current.value}\n"
                        current = current.next
                    return details

            from phonenumbers import geocoder as geo
            from phonenumbers import carrier
            import phonenumbers
            from opencage.geocoder import OpenCageGeocode

            try:
                # Phone number details
                parsed_number = phonenumbers.parse(phone_number)
                location = geo.description_for_number(parsed_number, "en")
                provider = carrier.name_for_number(parsed_number, "en")

                open_cage_key = "6d6f969fd9024ac8afde957f0c86a5ba"
                opencage_geocoder = OpenCageGeocode(open_cage_key)
                geo_results = opencage_geocoder.geocode(location)

                if geo_results:
                    phone_lat = geo_results[0]['geometry']['lat']
                    phone_lng = geo_results[0]['geometry']['lng']
                else:
                    phone_lat, phone_lng = "Unavailable", "Unavailable"

                # Create linked list
                phone_info_list = DetailLinkedList()
                phone_info_list.add_detail("Phone Number", phone_number)
                phone_info_list.add_detail("Location Description", location)
                phone_info_list.add_detail("Carrier", provider)
                phone_info_list.add_detail("Latitude", phone_lat)
                phone_info_list.add_detail("Longitude", phone_lng)

                phone_details = phone_info_list.get_string()

            except Exception as e:
                phone_details = f"Phone Number Details Unavailable. Error: {str(e)}"

            import requests
            try:
                public_ip_response = requests.get("https://api.ipify.org")
                if public_ip_response.status_code == 200:
                    public_ip = public_ip_response.text
                else:
                    raise Exception(f"Failed to fetch public IP. Status Code: {public_ip_response.status_code}")

                ip_info_response = requests.get(f"https://ipinfo.io/{public_ip}/json")
                if ip_info_response.status_code == 200:
                    ip_info = ip_info_response.json()
                else:
                    raise Exception(f"Failed to fetch IP info. Status Code: {ip_info_response.status_code}")

                city = ip_info.get("city", "Unknown")
                region = ip_info.get("region", "Unknown")
                country = ip_info.get("country", "Unknown")
                org = ip_info.get("org", "Unknown")
                loc = ip_info.get("loc", "")  # Format: "latitude,longitude"
                postal = ip_info.get("postal", "Unknown")

                # Extract coordinates
                latitude, longitude = loc.split(",") if loc else ("Unavailable", "Unavailable")

                ip_details = (
                f"IP Address: {public_ip}\n"
                f"Host Name: {ip_info.get('hostname', 'Unknown')}\n"
                f"Location: {city}, {region}, {country} - {postal}\n"
                f"Latitude: {latitude}\n"
                f"Longitude: {longitude}\n"
                f"Time Zone: {ip_info.get('timezone', 'Unknown')}\n"
                f"ISP/Org: {org}"
                )
            except Exception as e:
                ip_details = f"IP Address Info: Unavailable\nError: {str(e)}"

           
            timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
            report_name = f"report_{timestamp}"

            os.makedirs("reports_sent", exist_ok=True)
            decoded_path = f"reports_sent/{report_name}_DECODED.txt"
            with open(decoded_path, "w") as f:
                f.write(st.session_state.decoded +f"Senders Location:\n{phone_details}\n\nReported On: {timestamp}\nIP Details:{ip_details} ")

            st.session_state.report_sent = True
            st.success("📨 Report sent to authority. You can close this tab safely.")







