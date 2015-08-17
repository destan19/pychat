import socket
import time
import string
import thread
import sys
import json
from packet import send_packet
from packet import rcv_packet
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
	
	def friend_list_request(self):
		obj={}
		obj['uid']=self.uid
		data=json.dumps(obj)
		send_packet(self.con,self.uid,3,data,len(data));
		command,content,content_len=rcv_packet(self.con)
	
		if content_len == -1:
			print 'connect server error.'
			return 0
		if len(content)==0:
			return 0
		else:
			content_json=json.loads(content)
			return content_json

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
		s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
		s.connect((host,port))
		self.con = s
		return s
		
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
	client.connect_to_server("192.168.17.134",80)
	client.login()	
	return 0
	
	rcv_thread=thread.start_new_thread(thread_rcv_msg,(s,uid))
	send_thread=thread.start_new_thread(thread_send_msg,(s,uid))
	while(1) :
		time.sleep(10)
	s.close()

if __name__=="__main__":
	main();
