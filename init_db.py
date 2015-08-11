import sqlite3
import sys

def init_user_table():
	try:
		conn=sqlite3.connect("user.db")
	except sqlite3.Error,e:
		print "connect db error.",e.args[0]
		return
	cursor=conn.cursor()
	sql1='''create table user(
		uid INTEGER PRIMARY KEY autoincrement,
		loginid INTEGER,
		password VARCHAR(32),
		nickname VARCHAR(32),
		address VARCHAR(128),
		phone_num VARCHAR(32),
		sex VARCHAR(8),
		signature VARCHAR(128),
		mail VARCHAR(32),
		online INTEGER,
		age INTEGER);'''

	sql2=''' create table user_relation(
		ur_id INTEGER PRIMARY KEY autoincrement,
		friend_id INTEGER,
		self_id INTEGER ,
		FOREIGN KEY(self_id) REFERENCES user(loginid),
		FOREIGN KEY(friend_id) REFERENCES user(loginid));
		'''
	print sql1,sql2
	try:
		cursor.execute(sql1)
		cursor.execute(sql2)
	except sqlite3.Error,e:
		print 'create table error.',e.args[0]
		return

init_user_table()
