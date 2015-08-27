import socket
import time
import string
import thread
import sys
import json
from packet import send_packet
from packet import rcv_packet
from friend import Friend
HOST='192.168.17.134'
PORT=80
class Client:
	def __init__(self,uid,password):
		print 'init client.......'
		self.con=None
		self.uid=uid
		self.password=password
	def login(self):
		obj={}
		obj['username']=self.uid
		obj['password']=self.password
		data=json.dumps(obj)
		send_packet(self.con,self.uid,1,data,len(data));
		command,content,content_len=rcv_packet(self.con)
		print content
		if content_len == -1:
			print 'connect server error.'
			return 0
		if  len(content)==0:
			return 0
		else:
			content_json=json.loads(content)
			
			if content_json['login_status'] == 'success':
				print 'login success........'
				return 1
			else:
				print 'login fail.............'
				return 0
	
	def get_friend_by_uid(self):
		for i,value in enumerate(self.client.get_friend_list()['list']):
			if friend_uid == value['loginid']:
				friend=value
				break
	
	def update_friend_list(self):
		obj={}
		obj['uid']=self.uid
		self.friend_list=[]
		data=json.dumps(obj)
		send_packet(self.con,self.uid,3,data,len(data));
		command,content,content_len=rcv_packet(self.con)
		
		if content_len == -1:
			print 'connect server error.'
			return -1
		else:
			content_json=json.loads(content)
			self.friend_num=content_json['friend_num']
			for f in content_json['list']:
				friend=Friend(f['loginid'],f['nickname'],f['address'],f['phone_num'],
				f['sex'],f['signature'],f['mail'],f['online'],f['age'])
				self.friend_list.append(friend);
			return content_json
			
	def get_user_info(self):
		obj={}
		obj['uid']=self.uid
		data=json.dumps(obj)
		send_packet(self.con,self.uid,9,data,len(data));
		command,content,content_len=rcv_packet(self.con)
		
		if content_len == -1:
			print 'connect server error.'
			return -1
		else:
			content_json=json.loads(content)
			return content_json		
			
	def get_friend_list(self):
		return self.friend_list
		
	def send_msg_to_friend(self,con,uid,fid,msg):
		obj={}
		obj['uid']=uid
		obj['fid']=fid
		obj['msg']=msg
		data=json.dumps(obj)
		send_packet(con,uid,20,data,len(data));
		command,content,content_len=rcv_packet(con)
		print content
		if content_len == -1:
			print 'connect server error.'
			return 0
		else:
			content_json=json.loads(content)
			print content_json


	def connect_to_server(self,host,port):
		try:
			s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
			try:
				s.connect((host,port))
			except socket.error as e:
				print e
				s.close()
				return 0
		except socket.error as e:
			print e
			s=None
			return 0
		self.con = s
		return 1
		
def thread_rcv_msg(con,uid):
	while(1):
		command,content,content_len=rcv_packet(con)
		print 'client rcv',content
		
def thread_send_msg(con,uid):
	while(1):
		print 'fid:',
		fid_str=raw_input()
		fid=string.atoi(fid_str)
		print 'msg:',
		msg=raw_input()
		if len(msg)==0:
			print 'msg:',
			msg=raw_input()
		send_msg_to_friend(con,uid,fid,msg)
def main():
	print sys.argv
	if len(sys.argv) is not 2:
		print 'please set uid '
		return 0
	uid=string.atoi(sys.argv[1])
	password=123456
	client=Client(uid,password)
	client.connect_to_server("192.168.66.134",1088)
	client.login()	
	
	rcv_thread=thread.start_new_thread(thread_rcv_msg,(s,uid))
	send_thread=thread.start_new_thread(thread_send_msg,(s,uid))
	while(1) :
		time.sleep(10)
	s.close()

if __name__=="__main__":
	main();
