

import java.net.*;

import java.io.*;
/**
 * Created by derry on 2015/8/21.
 */
public class Client {
	static Socket socket;
	
    public static void main(String []args) throws IOException {
        System.out.print("hello android");
		socket = new Socket("192.168.66.134", 1088);  
		System.out.println("socket = " + socket);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeUTF("hello server");
		while(true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			}
			dos.writeUTF("hello server22");
		}
   };
};

