import socket
import json
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
	
def main():
	s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	s.connect((HOST,PORT))
	login(s,199,123456)	
	s.close()

if __name__=="__main__":
	main();
