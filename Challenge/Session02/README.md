
#### **Scenario:**

You are tasked with improving the design of a basic Users CRUD (Create, Read, Update, Delete) application. The existing implementation has several **code smells**, such as tightly coupled code, repeated logic, and poor scalability. To enhance maintainability, flexibility, and scalability, you will refactor the application using three key design patterns: **Singleton**, **Factory**, and **Observer**.

#### **Challenge Requirements:**

1.  **Code Sample:** Below is the existing Users CRUD application code. The goal is to identify code smells and apply **Singleton**, **Factory**, and **Observer** patterns to refactor and fix the issues.

    python

    Sao chép mã

    `from flask import Flask, request, jsonify
    import sqlite3

    app = Flask(__name__)

    # Database connection (smell: repeated connections and lack of control over the connection instance)
    def connect_db():
    return sqlite3.connect('users.db')

    # Create a new user (smell: business logic and DB logic tightly coupled)
    @app.route('/users', methods=['POST'])
    def create_user():
    conn = connect_db()
    cursor = conn.cursor()
    name = request.json.get('name')
    email = request.json.get('email')
    cursor.execute("INSERT INTO users (name, email) VALUES (?, ?)", (name, email))
    conn.commit()
    conn.close()
    return jsonify({"message": "User created"}), 201

    # Get a list of users (smell: duplicate code for DB operations and business logic)
    @app.route('/users', methods=['GET'])
    def get_users():
    conn = connect_db()
    cursor = conn.cursor()
    users = cursor.execute("SELECT * FROM users").fetchall()
    conn.close()
    user_list = [{"id": user[0], "name": user[1], "email": user[2]} for user in users]
    return jsonify(user_list), 200

    # Get user by ID (smell: lack of observer pattern for notifying other parts of the system)
    @app.route('/users/<int:user_id>', methods=['GET'])
    def get_user(user_id):
    conn = connect_db()
    cursor = conn.cursor()
    user = cursor.execute("SELECT * FROM users WHERE id = ?", (user_id,)).fetchone()
    conn.close()
    if not user:
    return jsonify({"message": "User not found"}), 404
    return jsonify({"id": user[0], "name": user[1], "email": user[2]}), 200

    # Update user by ID
    @app.route('/users/<int:user_id>', methods=['PUT'])
    def update_user(user_id):
    conn = connect_db()
    cursor = conn.cursor()
    name = request.json.get('name')
    email = request.json.get('email')
    cursor.execute("UPDATE users SET name = ?, email = ? WHERE id = ?", (name, email, user_id))
    conn.commit()
    conn.close()
    return jsonify({"message": "User updated"}), 200

    # Delete user by ID
    @app.route('/users/<int:user_id>', methods=['DELETE'])
    def delete_user(user_id):
    conn = connect_db()
    cursor = conn.cursor()
    cursor.execute("DELETE FROM users WHERE id = ?", (user_id,))
    conn.commit()
    conn.close()
    return jsonify({"message": "User deleted"}), 200`

2.  **Objective:**

    -   **Identify code smells** in the provided code.
    -   Refactor the code using:
        -   **Singleton Pattern** for managing the database connection.
        -   **Factory Pattern** to create instances of different user-related objects.
        -   **Observer Pattern** to notify other parts of the system when CRUD actions are performed (e.g., logging, analytics).

#### **Steps to Complete:**

1.  **Analyze the code smells:**

    -   **Singleton Pattern**: The current implementation creates a new database connection for each request, which is inefficient and prone to connection issues. Implement a Singleton for database connection management.
    -   **Factory Pattern**: User creation and retrieval logic is hardcoded within the route functions. Refactor this by using a Factory pattern to create User objects.
    -   **Observer Pattern**: When user data is created, updated, or deleted, other parts of the system (like logging or external services) may need to be notified. Implement the Observer pattern to manage event notifications.
2.  **Refactor the code**:

    -   Implement a **Singleton** class for managing database connections.
    -   Use the **Factory Pattern** to create and return user objects from a user repository or service layer.
    -   Apply the **Observer Pattern** to notify observers (e.g., logging, notifications) when CRUD actions occur.
3.  **Provide detailed explanations** of how the design patterns improve the overall architecture of the application.