package com.pychat;
import java.io.*;
class MsgDistributeThread extends Thread{
	private Client client;
	public MsgDistributeThread(Client client) {
		this.client = client;
	}
	public void run(){
		String msg = new String();
		Packet packet = null;
		while(true) {
			if (!this.client.pktEmpty()) {
				packet = this.client.popPacket();
				try {
					this.client.msgHandle(packet);
				}
				catch (Exception ioe) {
					ioe.printStackTrace();
				}
				continue;
			}
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}