import socket
import json
from packet import send_packet
from packet import rcv_packet
HOST='192.168.17.134'
PORT=80


def login(con,uid,password):
	#data="username=%s,password=%s"%(uid,password)	
	obj={}
	obj['username']=uid
	obj['password']=password
	data=json.dumps(obj)
	send_packet(con,uid,1,data,len(data));
	command,content,content_len=rcv_packet(con)
	if content_len == -1:
		print 'connect server error.'
	
def main():
	s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	s.connect((HOST,PORT))
	login(s,199,123456)	
	s.close()

	
if __name__=="__main__":
	main();
