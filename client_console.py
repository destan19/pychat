import socket
import time
import string
import thread
import sys
import json
import base64
from packet import send_packet
from packet import rcv_packet
HOST='192.168.17.134'
PORT=80

def login(con,uid,password):
	obj={}
	obj['username']=uid
	obj['password']=password
	data=json.dumps(obj)
	send_packet(con,uid,1,data,len(data));
	command,content,content_len=rcv_packet(con)
	print content
	if content_len == -1:
		print 'connect server error.'
		return 0
	if command!=3 or len(content)==0:
		return 0
	else:
		content_json=json.loads(content)
		
		if content_json['login_status'] == 'success':
			print 'login success........'
			return 1
		else:
			print 'login fail.............'
			return 0
def friend_list_request(con,uid):
	obj={}
	obj['uid']=uid
	data=json.dumps(obj)
	send_packet(con,uid,3,data,len(data));
	command,content,content_len=rcv_packet(con)
	print content
	if content_len == -1:
		print 'connect server error.'
		return 0
	if command!=3 or len(content)==0:
		return 0
	else:
		content_json=json.loads(content)
		print content_json

def upload_user_image(con,uid):
	obj={}
	obj['uid']=uid
	fd=open("111.png","rb")
	img_data=fd.read()
	encoded_img_data=base64.b64encode(img_data)
	obj['img']=encoded_img_data

	fd.close()

	data=json.dumps(obj)
	print data
	send_packet(con,uid,5,data,len(data));
	command,content,content_len=rcv_packet(con)
	print content
	if content_len == -1:
		print 'connect server error.'
		return 0
	if command != 6 or len(content)==0:
		print 'command error'
		return 0
	else:
		content_json=json.loads(content)
		print content_json

def send_msg_to_friend(con,uid,fid,msg):
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
def connect_to_server():
	s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	s.connect((HOST,PORT))
	return s

def main():
	print sys.argv
	if len(sys.argv) is not 2:
		print 'please set uid '
		return 0
	uid=string.atoi(sys.argv[1])
		
	s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	s.connect((HOST,PORT))
	login(s,uid,123456)	
	friend_list_request(s,uid)
	upload_user_image(s,uid)
	
	rcv_thread=thread.start_new_thread(thread_rcv_msg,(s,uid))
	send_thread=thread.start_new_thread(thread_send_msg,(s,uid))
	while(1) :
		time.sleep(10)
	s.close()

if __name__=="__main__":
	main();
