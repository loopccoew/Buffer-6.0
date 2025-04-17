from flask import Flask, render_template

from fraud_detection_game.fraud_detector import messages, check_answer  # your game logic

app = Flask(__name__)

app = Flask(__name__)
app.secret_key = 'a1b2c3d4e5f6g7h8i9j0secretkey123'  # required for session

@app.route('/')
def home():
    return render_template('homepage.html')

@app.route('/game1')
def game1():
    return render_template('game1.html')

@app.route('/game2')
def game2():
    return render_template('game2.html')

@app.route('/game3')
def game3():
    return render_template('game3.html')

@app.route('/game4')
def game4():
    return render_template('game4.html')

@app.route('/aboutUs')
def aboutUs():
    return render_template('AboutUs.html')

@app.route('/login')
def login():
    return render_template('login.html')











# ----------------------
# FRAUD GAME - LEVEL 1
# ----------------------
@app.route('/fraud-level1', methods=['GET', 'POST'])
def fraud_level1():
    if 'current' not in session:
        session['current'] = 0
        session['score'] = 0
        session['attempts'] = 0

    current = session['current']
    msg = messages.get(current)

    if request.method == 'POST':
        user_choice = request.form['choice']
        correct = check_answer(current, user_choice)

        if correct:
            if session['attempts'] == 0:
                session['score'] += 1
            session['attempts'] = 0
            session['current'] += 1
            if session['current'] >= len(messages):
                return redirect(url_for('fraud_result'))
            return render_template('fraud_level1.html', msg=messages[session['current']], feedback="correct", user_choice=user_choice)
        else:
            session['attempts'] += 1
            if session['attempts'] == 1:
                return render_template('fraud_level1.html', msg=msg, feedback="wrong", retry=True)

    return render_template('fraud_level1.html', msg=msg)

# ----------------------
# FRAUD GAME - LEVEL 2 (GRAPH)
# ----------------------

graph_level2 = {
    'A': ['B', 'C'],
    'B': [],
    'C': ['D', 'E'],
    'D': ['F'],
    'E': [],
    'F': []
}

descriptions_level2 = {
    'A': "A clean-looking ATM. Card slot looks fine. Nothing unusual at first glance. Want to proceed from here?",
    'B': "Hmm… The card slot on this ATM feels a little different. Could be just old... or something else?",
    'C': "Good!! ATM is functioning normally.",
    'D': "This one looks just like the ones you’ve used before. Seems okay, right?",
    'E': "There's a small piece of tape sticking out of the cash dispenser. Suspicious!",
    'F': "🏁 Everything seems clean and simple. Looks like you’ve reached the final ATM."
}

fraud_nodes_level2 = ['B', 'E']

@app.route('/fraud-level2', methods=['GET', 'POST'])
def fraud_level2():
    if 'path' not in session:
        session['path'] = ['A']  # Start from node A

    current_node = session['path'][-1]
    message = ""

    if request.method == 'POST':
        next_node = request.form['next_node']

        if next_node in fraud_nodes_level2:
            message = "❌ Oops! This ATM has a skimming device. Try another route!"
        else:
            session['path'].append(next_node)
            current_node = next_node
            if current_node == 'F' and session['path'] == ['A', 'C', 'D', 'F']:
                message = "✅ You’ve successfully reached a secure ATM! Great job detecting and avoiding fraud traps."
            else:
                message = "✅ Moved to next ATM."

    next_options = graph_level2.get(current_node, [])
    description = descriptions_level2.get(current_node, "")

    return render_template('fraud_level2.html',
                           current_node=current_node,
                           description=description,
                           next_options=next_options,
                           message=message,
                           path=session['path'],
                           descriptions_level2=descriptions_level2)

# ----------------------
# FRAUD GAME - RESULT
# ----------------------
@app.route('/fraud-result')
def fraud_result():
    score = session.get('score', 0)
    total = len(messages)
    session.clear()  # reset for replay
    return render_template('fraud_result.html', score=score, total=total)















if __name__ == '__main__':
    app.run(debug=True)
