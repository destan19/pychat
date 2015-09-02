package com.pychat;
import java.net.*;
import java.nio.*;
import java.io.*;

class RecvMsgThread extends Thread{
	private Client client = null;
	public RecvMsgThread(Client client) {	
		this.client = client;
	}
	
	public void run(){
		String msg = new String();
		Packet resp_packet = null;
		while(true) {
			try {
				 resp_packet = NetHelper.rcv_packet(this.client.dis);
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
			if ( null == resp_packet) {
				return ;
			}
			else {
				this.client.addPacket(resp_packet);
			}
		}
	}

}

