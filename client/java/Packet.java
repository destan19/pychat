//import ByteUtil.ByteUtil;

/**
0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
|s| len |C| uid   | content...                           e
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
*/

public class Packet{
	private byte start;
	private int len;
	private String data;
	private byte end;
	private int uid;
	private byte cmd;
	public Packet(int len,byte cmd,int uid,String data) {
		this.start=(byte)0x13;
		this.end=(byte)0x86;
		this.len = len + 6;
		this.cmd = cmd;
		this.uid = uid;
		this.data = data;
	}
	public String getData() {
		return this.data;
	}
	byte[] getByteData() {
		int p = 0;
		byte []int_bytes= new byte[4];
		byte []byte_data = new byte[this.len+5]; 
		byte_data[p] = start;
		p += 1;
		int_bytes= ByteUtil.int2Byte(this.len,ByteUtil.BIG_ENDIAN);
		System.arraycopy( int_bytes, 0, byte_data, p, 4);
		p += 4;
		byte_data[p] = this.cmd;
		p+=1;
		int_bytes= ByteUtil.int2Byte(this.uid,ByteUtil.BIG_ENDIAN);
		System.arraycopy( int_bytes, 0, byte_data, p, 4);
		p+=4;
		System.arraycopy(this.data.getBytes(),0,byte_data, p, this.len - 6);
		p += this.len - 6;
		byte_data[p]=this.end;
		ByteUtil.showData(byte_data);
		return byte_data;
	}
}

