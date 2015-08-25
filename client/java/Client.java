

import java.net.*;
import java.nio.*;
import java.io.*;
/**
 * Created by derry on 2015/8/21.
 */
public class Client {
	static Socket socket;
	public static void bytebuf_test()
	{
		 ByteBuffer buffer= ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asIntBuffer().put(1);
        System.out.println(buffer.array()[3]);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println(buffer.array()[3]);
	}
    public static void main(String []args) throws IOException {

	
		
        System.out.print("hello android");
		socket = new Socket("192.168.66.134", 1088);  
		System.out.println("socket = " + socket);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		DataOutputStream dos = new DataOutputStream(os);
//		dos.writeUTF("hello server");
		
		byte a=1;
		String str=new String("{\"username\":101,\"password\":123456}");
		byte []str2 = str.getBytes();

		System.out.println("send len:"+str.length());
		Packet packet=new Packet(str.length(),(byte)1,512413455,str);

		byte []send_data=packet.getByteData();
		
		os.write(send_data);
		try {
			Thread.sleep(3000);
			socket.close();
			return;
		}
		catch (InterruptedException e) {
				e.printStackTrace(); 
		}
		
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

