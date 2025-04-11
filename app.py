from flask import Flask, render_template,jsonify,request
from flask_sqlalchemy import SQLAlchemy
import mysql.connector 
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///dbms.db"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db=SQLAlchemy(app)

con=mysql.connector.connect(
  host='localhost',
  user='root',
  password='root123',
  database='userinfo',  
)
id=0
@app.route('/submit',methods=['GET','POST'])
def get_tables():
    global id
    password = request.form['password']
    email = request.form['email']
    id+=1
    cursor=con.cursor()
    cursor.execute("insert into login(ID,email,pass) values(%s,%s,%s)",(4,email,password))
    con.commit()
    cursor.execute("select * from login where email=niharika@gmai.com;")
    rows=cursor.fetchall()
    cursor.close
    con.close
    return jsonify({"login":rows}),200 
    
    
    
    
    
    
    
    
@app.route('/')
def index():
  return render_template('index.html')


@app.route('/product')
def products():
  return '<h1>Helllo World! Product page</h1>'

if __name__ == '__main__':
  app.run(debug=True, port=8000)