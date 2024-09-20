from flask import Flask, request, jsonify

import user_service

app = Flask(__name__)

# ERROR CODE
BAD_REQUEST = 400
CREATED = 201
SUCCESS = 200
NOT_FOUND = 404

# Create a new user
@app.route('/users', methods=['POST'])
def create_user():
    name = request.json.get('name')
    email = request.json.get('email')
    if not name or not email:
        return "Name and email are required", BAD_REQUEST
    user_service.create_user(name, email)
    return jsonify({"message": "User created"}), CREATED

# Get a list of users
@app.route('/users', methods=['GET'])
def get_users():
    user_list = user_service.get_users()
    return jsonify(user_list), SUCCESS

# Get user by ID
@app.route('/users/<int:user_id>', methods=['GET'])
def get_user(user_id):
    user = user_service.get_user(user_id)
    if not user:
        return "User not found", NOT_FOUND
    return jsonify({
    'id': user[0],
    'name': user[1],
    'email': user[2]
    }), SUCCESS

# Update user by ID
@app.route('/users/<int:user_id>', methods=['PUT'])
def update_user(user_id):
    name = request.json.get('name')
    email = request.json.get('email')
    if not name or not email:
        return "Name and email are required", BAD_REQUEST
    user_service.update_user(user_id, name, email)
    return jsonify({"message": "User updated"}), SUCCESS

# Delete user by ID
@app.route('/users/<int:user_id>', methods=['DELETE'])
def delete_user(user_id):
    user_service.delete_user(user_id)
    return jsonify({"message": "User deleted"}), SUCCESS

if __name__ == "__main__":
    app.run()