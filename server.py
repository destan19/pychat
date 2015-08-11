import socket
import sys
import os
import ConfigParser
import string
import thread
import time
import string
import json
from user import User
from packet import send_packet
from packet import rcv_packet
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
	send_packet(con,0,2,data,len(data))
	
		
def friend_list_request_handle(con,content_json):
		print 'friend list request handle.........'

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
	
def main():
	print 'main.......'

if __name__ == "__main__":

	load_conf()	
	init_server(host,port)
	while 1:
		conn,addr=server.accept()
		client_list.append(conn)
#		print dir(conn)
		#print client_list
		print "client  connected.",conn.getpeername()
		show_client_list(client_list)
		thread.start_new_thread(thread_msg_handle,(conn,))
	
