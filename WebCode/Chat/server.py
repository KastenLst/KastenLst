from flask import Flask, render_template, request, jsonify, session
from flask_socketio import SocketIO, send

app = Flask(__name__)
app.config['SECRET_KEY'] = 'your_secret_key'
socketio = SocketIO(app, cors_allowed_origins="*")

users = {}

@app.route('/')
def index():
    return render_template('chat.html')

@app.route('/profile')
def profile():
    return render_template('profile.html')

@socketio.on('message')
def handle_message(msg):
    username = session.get('username', 'Unbekannt')
    message_data = {'username': username, 'message': msg}
    send(message_data, broadcast=True)

@app.route('/set_username', methods=['POST'])
def set_username():
    username = request.json.get('username')
    session['username'] = username
    return jsonify({'status': 'ok'})

if __name__ == '__main__':
    socketio.run(app, debug=True)
