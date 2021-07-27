import pymysql

from config.config import mysql_config


def get_mysql_connection():
    return pymysql.connect(
        host=mysql_config["host"],
        port=mysql_config["port"],
        user=mysql_config["user"],
        passwd=mysql_config["password"],
        db=mysql_config["database"]
    )


def close_my_connection(conn):
    assert type(conn) == pymysql.Connection
    conn.close()


if __name__ == '__main__':
    conn = get_mysql_connection()
    cursor = conn.cursor()
    cursor.execute("select version()")
    version = cursor.fetchone()
    conn.close()
    print("MySQL version:", version)
