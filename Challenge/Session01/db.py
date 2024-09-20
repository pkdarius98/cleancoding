import sqlite3

# Connect to database
def connect_db():
    return sqlite3.connect('users.db')

def execute_update(sql, params):
    conn = connect_db()
    cursor = conn.cursor()
    cursor.execute(sql, params)
    conn.commit()
    conn.close()

def get_list(sql, params):
    conn = connect_db()
    cursor = conn.cursor()
    list = cursor.execute(sql, params).fetchall()
    conn.close()
    return list

def get_one(sql, params):
    conn = connect_db()
    cursor = conn.cursor()
    result = cursor.execute(sql, params).fetchone()
    conn.close()
    return result