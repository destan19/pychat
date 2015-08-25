import java.net.*;
import java.nio.*;
import java.io.*;
import org.json.JSONException;  
import org.json.JSONObject;  
import org.json.JSONStringer; 
import org.json.JSONArray; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
class NetHelper {

	public static  Packet rcv_packet(DataInputStream dis) throws IOException {
		try {
			byte start;
			int tmp_len;
			int length;
			start = dis.readByte();
			if ( start == 0x13 ) {
				tmp_len = dis.readInt();
				length = ByteUtil.convertEndian(tmp_len);
				System.out.println("response len="+length);
				byte []content_bytes = new byte[length];
				dis.read(content_bytes,0,length);
				//ByteUtil.showData(content_bytes);
				String content = new String(content_bytes);
				System.out.println("content = "+content);
			}
			System.out.println("start = " + start);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public int send_packet(DataInputStream dis,Packet packet) throws IOException  {
		try {
			byte start;
			int tmp_len;
			int length;
			start = dis.readByte();
			if ( start == 0x13 ) {
				tmp_len = dis.readInt();
				length = ByteUtil.convertEndian(tmp_len);
				System.out.println("response len="+length);
				byte []content_bytes = new byte[length];
				dis.read(content_bytes,0,length);
				//ByteUtil.showData(content_bytes);
				String content = new String(content_bytes);
				System.out.println("content = "+content);
			}
			System.out.println("start = " + start);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace(); 
		}
		return 0;
	}
	
	
}
