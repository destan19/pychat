
import java.io.*;
class NetHelper {
	/**
	range     len               desc
	1         1                 开头
	2~5       4                 长度
	6         1                 命令
	7~11      4                 uid
	12~       长度-1-4-1-4      内容
	最后一位                    结尾
	*/
	public static  Packet rcv_packet(DataInputStream dis) throws IOException {
		try {
			byte start;
			int tmp_len;
			int length;
			int uid = 0;
			Packet packet = null;
			start = dis.readByte();
			if ( start == 0x13 ) {
				tmp_len = dis.readInt();
				length = ByteUtil.convertEndian(tmp_len);
				System.out.println("response len="+length);
				byte []content_bytes = new byte[length];
				byte []data_bytes= new byte[length - 6];
				byte command;
				byte []uid_bytes = new byte[4];
				dis.read(content_bytes,0,length);
				command = content_bytes[0];
				System.arraycopy(content_bytes, 1, uid_bytes, 0, 4);
				System.arraycopy(content_bytes,5,data_bytes,0,length - 6);
				uid = ByteUtil.byte2Int(uid_bytes);
				ByteUtil.showData(content_bytes);
				String data = new String(data_bytes);
				System.out.println("data = "+data);
				packet = new Packet(data.length(),command,123,data);
				System.out.println("recv data=");
				packet.getByteData();
			}
		} catch (InterruptedException e) {
			e.printStackTrace(); 
			return null;
		}
		return packet;
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
