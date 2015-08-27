import java.net.*;
import java.nio.*;
import java.io.*;

class Chat extends Thread{
	private int fid = 0;
	private Client client = null;
	public Chat(Client client,int fid) {	
		System.out.println("Chat with friend "+ this.fid);
		this.fid = fid;
		this.client = client;
	}
	public void run(){
		System.out.println("begin chat\n >>");
		String msg = new String();
		while(true) {
			msg = "hello";
			try {
				this.client.send_msg_to_friend(this.fid,new String(msg));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println(">>");
			try {
				Thread.sleep(3000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
