import socket
import select
import sys
import os
import ConfigParser
import string
import thread
import time
import string
import Queue
import json
from user import User
from packet import send_packet
from packet import rcv_packet
from packet import wrap_packet
host="127.0.0.1"
port=80
server=None
client_list=[]

def load_conf():
	cp=ConfigParser.ConfigParser()
	cp.read("server.conf")
#	secs=cp.sections()
#	print "setions:",secs
#	server_items=cp.items('server')
#	print 'server items=',server_items
#	server_opts=cp.options('server');
	c_host=cp.get("server","host")
	c_port=cp.get("server","port")
	global host,g_port
	host=c_host
	port=string.atoi(c_port)

def init_server(host,port):
	global server
	print 'host:%s,port:%d'%(host,port)
	server=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	server.bind((host,port));
	server.listen(1)

def show_client_list(list):
	for i,client in enumerate(list):
		print conn.getpeername()

def login_request_handle(con,content_json):
	obj={}
	user=User.login(content_json['username'],content_json['password'])
	if user == None:
		obj['login_status']='fail'
	else:
		obj['login_status']='success'
	print obj
	data=json.dumps(obj)	
	packet=wrap_packet(con,0,2,data,len(data))
	return packet
	
		
def friend_list_request_handle(con,content_json):
	obj={}
	friend_list=[]
	num=0
	user=User(content_json['uid'])
	friend_list=user.get_friend_list()
	friend_obj={}
	obj['list']=[]
	for i,friend_id in enumerate(friend_list):		
		num+=1
		friend=User(friend_id)
		friend_obj['loginid']=friend.loginid
		friend_obj['nickname']=friend.nickname
		friend_obj['address']=friend.address
		friend_obj['phone_num']=friend.phone_num
		friend_obj['sex']=friend.sex
		friend_obj['signature']=friend.signature
		friend_obj['mail']=friend.mail
		friend_obj['online']=friend.online
		friend_obj['age']=friend.age
		obj['list'].append(friend_obj)
	obj['friend_num']=num
	data=json.dumps(obj,indent=4)
	print data

#	send_packet(con,0,4,data,len(data))
	packet=wrap_packet(con,0,4,data,len(data))
	return packet
		
def rcv_and_handle_msg(con):
	handle={
		1:login_request_handle,
		3:friend_list_request_handle
	}
	command,content,content_len=rcv_packet(con)
	print 'command=%d,content=%s,content_len=%d'%(command,content,content_len)

	if content_len == -1:
		print 'client is offline'
		con.close()
	content_json=json.loads(content)
	return handle.get(command)(con,content_json)
'''
def thread_msg_handle(con):
	handle={
		1:login_request_handle,
		3:friend_list_request_handle
	}
	command,content,content_len=rcv_packet(con)
	print 'command=%d,content=%s,content_len=%d'%(command,content,content_len)

	if content_len == -1:
		print 'client is offline'
		con.close()
	content_json=json.loads(content)
	handle.get(command)(con,content_json)
	#con.close()
'''
'''	
def main():
	load_conf()	
	init_server(host,port)
	while 1:
		conn,addr=server.accept()
		client_list.append(conn)
		print "client  connected.",conn.getpeername()
		show_client_list(client_list)
		thread.start_new_thread(thread_msg_handle,(conn,))
'''
def main():
	load_conf()	
	init_server(host,port)
	inputs=[server]
	outputs=[]
	timeout=20
	mq={}
	while 1:
		ra,wa,ex=select.select(inputs, outputs, inputs, timeout)
		if not (ra or wa or ex):
			print 'time out'
			break;
		for s in ra:
			if s is server:
				conn,addr=server.accept()
				print "client  connected.",conn.getpeername()
				conn.setblocking(0)
				inputs.append(conn)
				mq[conn]=Queue.Queue()
			else:
				resp=rcv_and_handle_msg(s)
				if resp:
					mq[s].put(resp)
					if s not in outputs:
						outputs.append(s)
				else:
					if s in outputs:
						outputs.remove(s)
					inputs.remove(s)
					s.close()
					del mq[s]
		for s in wa:
			try:
				msg=mq[s].get_nowait()
			except Queue.Empty:
				print s.getpeername(),'queue empty'
				outputs.remove(s)
			else:
				s.send(msg)

if __name__ == "__main__":
	main()

	
