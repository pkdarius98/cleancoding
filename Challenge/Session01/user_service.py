import db

def create_user(name, email):
    db.execute_update("INSERT INTO users (name, email) VALUES (?, ?)", [name, email])

def get_users():
    users = db.get_list("SELECT * FROM users", [])
    user_list = []
    for user in users:
        user_list.append({
        'id': user[0],
        'name': user[1],
        'email': user[2]
        })
    return user_list

def get_user(user_id):
    user = db.get_one("SELECT * FROM users WHERE id = ?", [user_id])
    return user

def update_user(user_id, name, email):
    db.execute_update("UPDATE users SET name = ?, email = ? WHERE id = ?", [name, email, user_id])

def delete_user(user_id):
    db.execute_update("DELETE FROM users WHERE id = ?", [user_id])