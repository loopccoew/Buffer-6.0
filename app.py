from flask import Flask, render_template, request, redirect, session, url_for, flash, jsonify
import mysql.connector

app = Flask(__name__)
app.secret_key = 'your_secret_key'

# MySQL connection function
def get_db_connection():
    return mysql.connector.connect(
        host='localhost',
        user='root',
        password='root123',
        database='userinfo'
    )

@app.route('/')
def home():
    if 'email' in session:
        return f"<h1>Welcome, {session['email']}!</h1><a href='/logout'>Logout</a>"
    return redirect(url_for('login'))

@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']
        con = get_db_connection()
        cursor = con.cursor()
        try:
            cursor.execute("INSERT INTO login (email, pass) VALUES (%s, %s)", (email, password))
            con.commit()
            flash("Registered successfully! You can now log in.")
        except mysql.connector.IntegrityError:
            flash("Email already registered.")
        cursor.close()
        con.close()
        return redirect(url_for('login'))
    return render_template('register.html')

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        email = request.form.get('email')
        password = request.form.get('password')
        con = get_db_connection()
        cursor = con.cursor(dictionary=True)
        cursor.execute("SELECT * FROM login WHERE email = %s AND pass = %s", (email, password))
        user = cursor.fetchone()
        cursor.close()
        con.close()
        if user:
            session['email'] = email
            return redirect(url_for('home'))
        else:
            flash('Invalid email or password.')
    return render_template('login.html')

@app.route('/logout')
def logout():
    session.pop('email', None)
    return redirect(url_for('login'))

if __name__ == '__main__':
    app.run(debug=True, port=8000)
