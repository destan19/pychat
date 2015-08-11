import os
import sys
import sqlite3

class User:
	def __init__(self,username,password):
		self.loginid=username
		self.password=password
		print username,password
	def save(self):
		print 'save to database'
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_insert="insert into user(loginid,password) values(%d,'%s');"%(self.loginid,self.password)
		print sql_insert
		try:
			cursor.execute(sql_insert)
		except sqlite3.Error,e:
			print 'add data error!'
			return
		conn.commit()
		conn.close()
	
	def add_friend(self,friend_id):
		print 'add friend'
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_insert="insert into user_relation(self_id,friend_id) values(%d,%d);"%(self.loginid,friend_id)
		print sql_insert
		try:
			cursor.execute(sql_insert)
		except sqlite3.Error,e:
			print 'add friend error!'
			return
		conn.commit()
		conn.close()

	@staticmethod
	def show():
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_query="select * from user"
		print sql_query
		cursor.execute(sql_query)
		for item in cursor:
			username=item[1]
			print username
	@staticmethod
	def login(uid,password):
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_query="select * from user where loginid=%d and password=%d"%(uid,password)
		print sql_query
		cursor.execute(sql_query)
		for item in cursor:
			username=item[1]
			password=item[2]
			print username,password
			if username > 0:
				print 'found user %d'%username
				return User(username,password)
			else:
				return None
	
		
def main():
	print 'user main'
	for i in range(1,100):
		user=User(i+100,'123456');
		user.save()
		user.add_friend(i+10000)
	User.show()

if __name__ == "__main__":
	main()


