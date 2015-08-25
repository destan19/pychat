public class ByteUtil {
	static int LITTLE_ENDIAN = 0;
	static int BIG_ENDIAN = 1;
    public static byte[] int2Byte(int intValue,int endian) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
        }
		if (BIG_ENDIAN == endian) {
			byte[] b2 = new byte[4];
			for (int i = 0;i < 4;i++)
				b2[3-i] = b[i];
			return b2;
		}
        return b;
    }
    public static int byte2Int(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }
	public static byte[] bytes2Bytes(byte[] b1,byte[] b2){
		byte[] ret = new byte[b1.length+b2.length];
		System.arraycopy(b1, 0, ret, 0, b1.length);
		System.arraycopy(b2, 0, ret, b1.length, b2.length);
		return ret;
	}    

	public static byte[] copyBytes(byte[] b1,int start,int length){
		byte[] ret = new byte[length];
		if (start + length > b1.length) return null;      
		System.arraycopy(b1, start, ret, 0, length);
		return ret;
	}  

	public static void showData(byte[] b) {
		int i;
		for (i = 0; i < b.length;i++) {
			if ( b[i] < 0 )  
				System.out.print(Integer.toHexString((0x7f&b[i])+128)+" ");
			else 
				System.out.print(Integer.toHexString(0x7f&b[i])+" ");
		}
		System.out.println("\n");
	}
}