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
import base64
host="127.0.0.1"
port=80
server=None
client_list=[]
outputs=[]

mq={}
user_dict={}
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
	global user_dict
	obj={}
	user=User.login(content_json['username'],content_json['password'])
	if user == None:
		obj['login_status']='fail'
	else:
		obj['login_status']='success'
		user_dict[user.loginid]=con
	print obj
	print 'user dict=',user_dict	
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
		
def heart_request_handle(con,content_json):
	obj={}
	user=User(conten_json['uid'])

def show_mq():
	print '=============================================='
	global mq
	for con in mq:
		print con.getpeername()	,'msg num:%d'%mq[con].qsize()
def chat_msg_handle(con,content_json):
	obj={}
	res_obj={}
	f_con=None
	global user_dict
	global mq
	global outputs
	uid=content_json['uid']
	fid=content_json['fid']
	msg=content_json['msg']
	obj['send_id']=uid
	obj['rcv_id']=fid
	obj['msg']=msg
	data=json.dumps(obj,indent=4)
	if user_dict.has_key(fid):
		f_con=user_dict[fid]
		print 'found friend id in user dict',fid
	if f_con is None:
		res_obj['status']='fail'
		res_obj['reson']='friend is not online'
	else:
		msg_packet=wrap_packet(f_con,0,22,data,len(data))	
		mq[f_con].put(msg_packet)
		if f_con not in outputs:
			outputs.append(f_con)
		res_obj['status']='success'
	res_data=json.dumps(res_obj,indent=4)
	res_packet=wrap_packet(con,0,21,res_data,len(res_data))
	return res_packet
	
def upload_user_image_handle(con,content_json):
	obj={}
	res_obj={}
	uid=content_json['uid']
	img_data=content_json['img']
	fd = open("images/%d.png"%uid,"wb")
	decoded_img_data=base64.b64decode(img_data)
	fd.write(decoded_img_data)
	fd.close()
	res_obj['status']='success'
	res_data=json.dumps(res_obj,indent=4)
	res_packet=wrap_packet(con,0,6,res_data,len(res_data))
	return res_packet
	
def user_image_request_handle (con,content_json):
	obj={}
	res_obj={}
	uid=content_json['uid']
	fd = open("images/%d.png"%uid,"rb")
	img_data=fd.read()
	encoded_img_data=base64.b64encode(img_data)
	fd.close()
	res_obj['img']=encoded_img_data
	res_data=json.dumps(res_obj,indent=4)
	res_packet=wrap_packet(con,0,8,res_data,len(res_data))
	return res_packet
def	user_info_request_handle(con,content_json):
	obj={}
	res_obj={}
	uid=content_json['uid']
	user=User(uid)
	res_obj['loginid']=user.loginid
	res_obj['nickname']=user.nickname
	res_obj['address']=user.address
	res_obj['phone_num']=user.phone_num
	res_obj['sex']=user.sex
	res_obj['signature']=user.signature
	res_obj['mail']=user.mail
	res_obj['online']=user.online
	res_obj['age']=user.age
	res_data=json.dumps(res_obj,indent=4)
	res_packet=wrap_packet(con,0,10,res_data,len(res_data))
	return res_packet
	
def rcv_and_handle_msg(con):
	handle={
		1:login_request_handle,
		3:friend_list_request_handle,
		5:upload_user_image_handle,
		7:user_image_request_handle,
		9:user_info_request_handle,
		20:chat_msg_handle
		
	}
	command,content,content_len=rcv_packet(con)
	print 'command=%d,content=%s,content_len=%d'%(command,content,content_len)

	if content_len == -1:
		print 'client is offline'
		return None
	content_json=json.loads(content)
	return handle.get(command)(con,content_json)

def main():
	load_conf()	
	init_server(host,port)
	inputs=[server]
	global outputs
	global user_dict
	timeout=20
	global	mq
	while 1:
		ra,wa,ex=select.select(inputs, outputs, inputs, timeout)
#		if not (ra or wa or ex):
#			print 'time out'
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
						print '##add client',s.getpeername()
					inputs.remove(s)
					del mq[s]
					s.close()
		for s in wa:
			try:
				msg=mq[s].get_nowait()
			except Queue.Empty:
				print s.getpeername(),'queue empty'
				outputs.remove(s)
			else:
				print '###########send ',repr(msg)
				s.send(msg)
		for s in ex:
			print 'exception on',s.getpeername()
			inputs.remove(s)
			if s in outputs:
				outputs.remove(s)
			s.close()
			del mq[s]

if __name__ == "__main__":
	main()

