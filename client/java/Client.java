

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
/**
 * Created by derry on 2015/8/21.
 */
public class Client {
	private String host = null;
	private int port = 80;
	private static Socket socket;
	private OutputStream os;
	private InputStream is;
	private DataInputStream dis;
	private DataOutputStream dos;
	private int uid;
	private int password;
	
	public Client(int uid,int password) {
		this.uid = uid;
		this.password = password;
		System.out.println("construct a client.\n");
	}
	
	public int login() throws IOException{
		byte a=1;
		String str=new String("{\"username\":101,\"password\":123456}");
		byte []str2 = str.getBytes();
		System.out.println("send len:"+str.length());
		Packet packet=new Packet(str.length(),(byte)1,512413455,str);
		byte []send_data=packet.getByteData();
		this.os.write(send_data);
		return 1;
	}
		
	public int connect_to_server(String host,int port) throws IOException {
		try {
			this.socket = new Socket(host, port);  
			this.os = this.socket.getOutputStream();
			this.is = this.socket.getInputStream();
			this.dis = new DataInputStream(this.is);
			this.dos = new DataOutputStream(this.os);
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	public int disconnect_server() throws IOException {
		try {
			this.socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	public static void json_test(){
		JSONObject user_obj = new JSONObject();  
        try {  
            user_obj.put("username", "dxt");  
            user_obj.put("password", "123456");  
			user_obj.put("age",20);
        } catch (JSONException e) {  
            e.printStackTrace();  
        }  
        String  json_str=user_obj.toString();
		
        System.out.println("json="+json_str);
		JSONObject obj_new = new JSONObject(json_str);
		try {
			String username = null;
			String password = null;
			int age = 0;
			username = obj_new.getString("username");
			password = obj_new.getString("password");
			age = obj_new.getInt("age");
			System.out.println("username = "+username+" password="+password+" age="+age);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		JSONObject list_obj = new JSONObject();
		String list_str = null;
		try {
			list_obj.put("uid",101);
			JSONArray json_arr = new JSONArray();
			int i;
			for (i = 0;i < 10;i++) {
				JSONObject f_obj = new JSONObject();
				f_obj.put("fid",100+i);
				f_obj.put("name","derry");
				json_arr.put(f_obj);
			}
			list_obj.put("list",json_arr);
			list_str = list_obj.toString();
			System.out.println(list_str);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		JSONObject list_obj_new = new JSONObject(list_str);
		JSONArray arr_obj = new JSONArray();
		int uid = list_obj_new.getInt("uid");
		arr_obj = list_obj_new.getJSONArray("list");
		List<JSONObject> list = new ArrayList<JSONObject>();
		try {
			for (int i = 0; i <  arr_obj.length();i++) {
				JSONObject object =  (JSONObject)arr_obj.opt(i);
				list.add(object);
				System.out.println("fid="+object.getInt("fid"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		}
	
    public static void main(String []args) throws IOException {

	Client client = new Client(101,123456);
	client.connect_to_server("192.168.66.134",1088);
	client.login();
	
	while(true) {
		client.disconnect_server();
		break;
		
	}
   };
};

