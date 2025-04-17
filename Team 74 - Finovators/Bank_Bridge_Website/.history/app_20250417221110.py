from flask import Flask, render_template, request, redirect, url_for, session

from fraud_detection_game.fraud_detector import messages, check_answer  # your game logic
from collections import deque


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
        user_choice = request.form.get('choice')
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

    return render_template('fraud_level1.html', msg=msg, feedback=None, retry=False)

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
        next_node = request.form.get('next_node')


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








@app.route('/account-game/level1')
def account_level1():
    return render_template('account_level1.html')

@app.route('/account-game/level2')
def account_level2():
    return render_template('account_level2.html')

@app.route('/account-game/level3')
def account_level3():
    return render_template('account_level3.html')

@app.route('/account-game/level4')
def account_level4():
    return render_template('account_level4.html')








@app.route('/credit-game/level1')
def level1():
    return render_template('credit_level1.html')









# --------------------- Sample Data ---------------------

# Transactions for Level 1
TRANSACTIONS = deque([
    {"from": "Alice", "to": "Bob", "amount": 100},
    {"from": "Bob", "to": "Charlie", "amount": 200},
    {"from": "Charlie", "to": "Alice", "amount": -50},  # invalid
    {"from": "Unknown", "to": "Bob", "amount": 75},     # invalid
    {"from": "Bob", "to": "Alice", "amount": 50}
])

# Accounts and queued actions (can be used in later levels)
accounts = {"user1": 5000, "user2": 3000, "user3": 1000}
transaction_queue = deque([
    {"user": "user1", "amount": 1000, "type": "deposit"},
    {"user": "user2", "amount": 1500, "type": "withdraw"},
    {"user": "user3", "amount": 500, "type": "deposit"},
    {"user": "user1", "amount": 2000, "type": "withdraw"}
])



# --------------------- Level 1 ---------------------

@app.route('/transaction-game/level1', methods=['GET', 'POST'])
def transactionlevel1():
    if 'transactions' not in session:
        session['transactions'] = list(TRANSACTIONS)
        session['ledger'] = {"Alice": 1000, "Bob": 1000, "Charlie": 1000}
        session['score'] = 0

    queue = deque(session['transactions'])
    ledger = session['ledger']
    score = session['score']

    if request.method == 'POST':
        action = request.form['action']
        current = queue.popleft()

        is_valid = current["amount"] > 0 and current["from"] in ledger and current["to"] in ledger

        if (is_valid and action == 'apply') or (not is_valid and action == 'skip'):
            score += 1
            if is_valid:
                ledger[current["from"]] -= current["amount"]
                ledger[current["to"]] += current["amount"]
        else:
            score -= 1

        session['transactions'] = list(queue)
        session['ledger'] = ledger
        session['score'] = score

        if not queue:
            return redirect(url_for('result'))

    current_txn = queue[0] if queue else None
    return render_template("transaction_level1.html", txn=current_txn, ledger=ledger, score=score)

@app.route('/transaction-game/result')
def result():
    score = session.get('score', 0)
    ledger = session.get('ledger', {})
    session.clear()
    return render_template("transaction_result.html", score=score, ledger=ledger)









class Candy:
    def __init__(self, name, emoji, price):
        self.name = name
        self.emoji = emoji
        self.price = price

menu = {
    "lollipop": Candy("Lollipop", "🍭", 5),
    "chocolate": Candy("Chocolate", "🍫", 10),
    "gummy": Candy("Gummy Bears", "🐻", 7),
    "jelly": Candy("Jelly Beans", "🫘", 12)
}

@app.route('/transaction-game/level2')
def home1():
    return render_template('transaction_level2_home.html')

@app.route('/scenario/<int:num>')
def start_scenario(num):
    session.clear()
    if num == 1:
        session['coins'] = 20
        session['scenario'] = 1
    elif num == 2:
        session['coins'] = 18
        session['bag'] = ['jelly']
        session['stack'] = ['jelly']
        session['scenario'] = 2
    else:
        return redirect(url_for('home'))

    if 'stack' not in session:
        session['stack'] = []
    if 'bag' not in session:
        session['bag'] = []
    return redirect(url_for('index'))

@app.route('/game')
def index():
    if 'coins' not in session:
        return redirect(url_for('home'))
    return render_template('transaction_level2_index.html', menu=menu, coins=session['coins'], bag=session['bag'], scenario=session.get('scenario', 1))

@app.route('/buy/<candy_name>')
def buy_candy(candy_name):
    candy = menu.get(candy_name)
    if candy:
        if session['coins'] >= candy.price:
            session['coins'] -= candy.price
            session['stack'].append(candy_name)
            session['bag'].append(candy_name)
    return redirect(url_for('index'))

@app.route('/undo')
def undo():
    if session['stack']:
        last_candy_name = session['stack'].pop()
        candy = menu[last_candy_name]
        session['coins'] += candy.price
        session['bag'].remove(last_candy_name)
    return redirect(url_for('index'))

@app.route('/reset')
def reset():
    return redirect(url_for('home1'))













if __name__ == '__main__':
    app.run(debug=True)
