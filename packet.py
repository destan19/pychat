import os
import struct
import string

def send_packet(con,uid,command,data,length):
	start=0x13
	end=0x86
	format_str="=BiBi%dsB" %length
	packet=struct.pack(format_str,start,length+6,command,uid,data,end)
	print repr(packet)
	try:
		con.sendall(packet)
	except:
		print 'send packet error'
		return 0
	return 1
def wrap_packet(con,uid,command,data,length):
	start=0x13
	end=0x86
	format_str="=BiBi%dsB" %length
	packet=struct.pack(format_str,start,length+6,command,uid,data,end)
	return packet

def rcv_packet(con):
	while 1:
		try:
			data=con.recv(1)
		except:
			print 'recv data error'
			break
		if len(data)==0:
			print 'len of data is 0'
			break;
		start_token,=struct.unpack("B",data)
		if start_token==0x13:
			data=con.recv(4)
			length,=struct.unpack("i",data)
			data=con.recv(length)
			content_len=length-6
			format_str="=Bi%dsB"%content_len
			command,uid,content,end_token=struct.unpack(format_str,data)
#			print 'command=%d,uid=%d,content=%s,content len=%d'%(command,uid,content,content_len)
			if end_token == 0x86:
				return command,content,content_len
			print 'invalid end token'
		else:
			print 'invalid start token'
	return -1,-1,-1

