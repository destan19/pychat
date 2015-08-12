import os
import sys
import sqlite3
import random

class User:
	loginid=0
	password=123456
	nickname="me"
	address="Guangdong ShenZhen"
	phone_num="158xxxxxxxx"
	sex="M"
	signature="I love python!"
	mail="xxx@pychat.xyz"
	online=0
	age=0
	def __init__(self,loginid):
		self.loginid=loginid
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_query='''select  loginid,password,nickname,address,phone_num,sex,signature,mail,online,age from user where loginid=%s'''%loginid
		cursor.execute(sql_query)
		for item in cursor:
			self.password=item[1]
			self.nickname=item[2]
			self.address=item[3]
			self.phone_num=item[4]
			self.sex=item[5]
			self.signature=item[6]
			self.mail=item[7]
			self.online=item[8]
			self.age=item[9]
			username=item[1]
		conn.close()
		
	def save(self):
		print 'save to database'
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_insert='''insert into user(loginid,password,nickname,address,\
			phone_num,sex,signature,mail,online,age) \
			values(%d,'%s','%s','%s','%s','%s','%s','%s',%d,%d);'''	%(self.loginid,self.password,self.nickname,self.address,self.phone_num,self.sex,self.signature,self.mail,self.online,self.age)
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
	def get_friend_list(self):
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_query=''' select x.loginid,x.nickname,x.phone_num,x.sex,x.signature,x.mail,x.online,x.age
		from user x,user_relation y where y.self_id=%d and x.loginid=y.friend_id;'''%self.loginid
		print sql_query
		cursor.execute(sql_query)
		friend_list=[]
		for item in cursor:
			friend_list.append(item[0])
		conn.close()
		return friend_list
	
	@staticmethod
	def login(uid,password):
		try:
			conn=sqlite3.connect("user.db")
		except sqlite3.Error,e:
			print "connect db error.",e.args[0]
			return
		cursor=conn.cursor()
		sql_query="select * from user where loginid=%d and password=%d"%(uid,password)
		cursor.execute(sql_query)
		for item in cursor:
			username=item[1]
			password=item[2]
			if username > 0:
				return User(username,password)
			else:
				return None
		conn.close()
		
def main():
	print 'user main'
	for i in range(1,20):
		user=User(i+100);
		user.save()
		for j in range(1,5):
			friend=random.randint(101,120)
			user.add_friend(friend)
	User.show()

if __name__ == "__main__":
	main()

