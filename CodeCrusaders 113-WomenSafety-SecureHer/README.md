🔐 SecureHer – Women Safety App

SecureHer is a desktop-based safety application built with *Python (Tkinter)* and *MySQL*. It empowers women by enabling secure offline and online harassment reporting, viewing high-risk zones, storing encrypted evidence, and providing safe route navigation—all within an intuitive interface.

📌 Features

 👥 User & Admin Authentication
- *Signup/Login* for users with phone number & password
- *Admin login* for monitoring all user reports

 📝 Report Harassment
- *Offline Report*: Physical incident reports linked to a real-world location.
- *Online Report*: Harassment through social platforms with URL, screenshot, and category.

📍 Safe Route Navigation
- Computes the *safest path* using *Dijkstra's Algorithm*, penalizing dangerous zones using live danger scores.
- Opens *Google Maps* for route navigation.

 📊 High-Risk Location Viewer
- Lists areas with *highest danger scores*, updated based on reports.

 🧾 Report History
- Users can view all *online and offline reports* submitted by them.

 🧠 Admin Dashboard
- Admin can view all reports (categorized), with names, contact info, and timestamps.

 🔐 Encrypted Evidence Storage
- Upload, encrypt, and view sensitive media evidence (e.g., videos, images).

 📞 Emergency Helplines (Optional Add-on)
- View important helpline numbers at a click (ensure `helpline_ui.py` is available).

 🛠️ Installation & Setup

 🔧 Requirements
- Python 3.x
- MySQL Server
- `mysql-connector-python`
- OS: Windows/Linux/macOS

 📦 Python Dependencies
Install dependencies via pip:
```bash
pip install mysql-connector-python
```

 🗄️ Database Setup
1. Create a database named `SecureHer`.
2. Import or create the following tables:
   - `users` (user_id, name, contact, password)
   - `admin` (adname, password)
   - `locations` (id, name, latitude, longitude)
   - `offline_reports`, `online_reports`
   - `location_danger` (tracks danger score)
3. Ensure your MySQL credentials match those in `db.py`.

🚀 Getting Started
1. Clone the repository:
```bash
git clone https://github.com/your-username/SecureHer.git
cd SecureHer
```
2. Run the app:
```bash
python main.py
```
 🧭 Project Structure

| File/Folder              | Description |
|--------------------------|-------------|
| `main.py`                | Launch screen with animated title and options for login/signup |
| `auth.py`                | Handles user authentication and account creation |
| `main_menu.py`           | User dashboard with navigation to all functionalities |
| `admin.py`               | Admin login and report viewing functionality |
| `report_selection.py`    | Chooses between offline and online report types |
| `offline_report.py`      | Submit physical harassment reports with location dropdown |
| `online_report_.py`      | Submit cyber harassment with URL, platform, and screenshot |
| `view_reports.py`        | View all submitted reports for the logged-in user |
| `safe_route.py`          | Route computation using Dijkstra algorithm and Google Maps |
| `location_danger_history.py` | Shows location-wise danger scores and last reports |
| `evidence.py`            | File upload, encryption (reverse byte logic), and viewing |
| `db.py`                  | MySQL connection utility |
| `session.py`         | Stores current user session data |
| `helpline_ui.py`  | Displays emergency helpline numbers |

 🧠 How the Danger Score System Works

- Each offline report submitted increases the *danger score* of that location.
- Safe route pathfinding uses *danger scores to reduce edge weights* of the graph—lower weights mean more danger.
- Locations with higher danger scores are penalized more, and safer paths are preferred.

 🔐 Evidence Storage

- Files are *encrypted* using simple byte-reversal and stored securely.
- Users can decrypt and view them during the session, but decrypted files are stored temporarily.

 ✅ Future Enhancements
- Voice assistant for report submission
- Map-based UI with live risk updates
- Machine Learning-based risk prediction
- Cloud-based storage for encrypted evidence




video Link -https://drive.google.com/file/d/1GYRV8GDtId--nCnQVbuNgshaRb7zD0Jl/view?usp=drivesdk
