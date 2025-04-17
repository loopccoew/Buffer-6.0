Here’s a detailed README.md file for your Women Safety Android App project. This includes a project description, tech stack, and individual feature breakdown:


---

# Women Safety App

## Overview

*Women Safety App* is a powerful Android application designed to enhance women's safety by combining advanced algorithms, real-time location features, AI-powered chat assistance, and emergency communication tools. The app is built with a focus on offline accessibility, data structures for custom logic, and multiple storage systems (HashMap, SQLite, Firebase).

---

## Features

### 1. *User Authentication (Login/Register)*
- *HashMap*: Used for initial credential validation during runtime.
- *SQLite*: Stores user credentials locally.
- *Firebase*: Syncs user data securely to the cloud for backup and multi-device login.
- *Saved Emergency Contacts*: Users can store contacts during registration for emergency alerts.

---

### 2. *Help Centers Locator*
- *DSA Used*: Haversine Algorithm
- *Input*: A CSV file with latitude and longitude of verified help centers.
- *Logic*: Calculates the distance between user's live location and help centers.
- *Output*: Displays 7 nearest help centers as red pins on Google Maps (integrated using Maps API).

---

### 3. *Live Location Sharing*
- Sends the user's current GPS coordinates via SMS to the 7 nearest help centers.
- Uses Android LocationManager + SMSManager with fallback for Android 13+ permission handling.

---

### 4. *Fake Call Generator*
- *Array-based System*: Plays a fake call sound on a loop or starts a dummy incoming call screen.
- *Purpose*: To help users escape threatening situations discreetly.

---

### 5. *Bulk SOS Messaging*
- *Bulk Message Array*: Stores and sends SOS messages with current location to all emergency contacts saved during registration.
- *Includes*: Google Maps link for live location.

---

### 6. *Chatbot (Powered by Gemini API)*
- *Gemini API Integration*: Uses OkHttp or Gemini SDK to send safety-related queries and fetch responses.
- *ArrayList for Suggestions*: Suggested safety questions pop up for users to tap and interact.
- *Input Options*: TextBox for custom queries + preloaded questions.
- *Use Case*: Information on filing FIR, reporting harassment, cybercrime awareness, etc.

---

### 7. *"Do You Know?" Section*
- A curated list of:
  - *Emergency Convey Signals*: Universal hand gestures or body language cues for distress.
  - *General Safety Awareness*: Rights, helpline numbers, tips for safe commuting, cyber safety, etc.
- Presented using RecyclerView for clean UI.

---

## Tech Stack

- *Language*: Java
- *IDE*: Android Studio
- *Location & Map*: Google Maps API, LocationManager
- *SMS & Call*: Android Telephony APIs
- *DSA*: Haversine Algorithm
- *Storage*:
  - HashMap (in-memory)
  - SQLite (local persistent)
  - Firebase (cloud sync)
- *CSV Reader*: ACTION_OPEN_DOCUMENT for helpcenter file
- *Chatbot*: Gemini API with OkHttp / SDK
- *UI Components*: RecyclerView, CardView, Google Maps, EditText, Button, AlertDialog

---

## Folder Structure

app/ ├── activities/ │   ├── LoginActivity.java │   ├── RegisterActivity.java │   ├── MainActivity.java │   ├── MapActivity.java │   └── ChatbotActivity.java ├── utils/ │   ├── Haversine.java │   ├── FirebaseManager.java │   └── LocationHelper.java ├── data/ │   ├── HelpCenter.csv ├── res/ │   └── layout/ │       ├── activity_main.xml │       ├── activity_chatbot.xml │       └── ...

---

## Permissions Required

```xml
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CALL_PHONE" />





