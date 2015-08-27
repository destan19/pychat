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
public class Client{
	final static byte LOGIN_REQUEST_CMD = 1;
	final static byte FRIEND_LIST_REQUEST_CMD = 3;
	final static byte SEND_MSG_CMD = 20;
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

	/*
		return:
		0: 失败
		1: 成功
		-1:网络错误
	*/
	public int login() throws IOException{
		int ret = 0;
		String login_json_str = null;
		JSONObject login_json = new JSONObject();
		JSONObject resp_json = null;
		login_json.put("username",this.uid);
		login_json.put("password",this.password);
		
		login_json_str  = new String(login_json.toString());
		System.out.println("login json="+login_json_str);
		Packet login_packet=new Packet(login_json_str.length(),Client.LOGIN_REQUEST_CMD,this.uid,login_json_str);
		ret = NetHelper.send_packet(this.os,login_packet);
		if ( 0 == ret ) 
			return 0;
		
		Packet resp_packet = NetHelper.rcv_packet(this.dis);
		if ( null == resp_packet) {
			return -1;
		}
		else {
			resp_json = new JSONObject(resp_packet.getData());
			if (resp_json.getString("login_status").equals("success")) {
				System.out.println("login ............success.");
				return 1;
			}
			else  {
				System.out.println("login ............fail.");
				return 0;
			}
		}
		
	}
	public void send_msg_to_friend(int fid,String msg) throws IOException{
		String msg_json_str = null;
		JSONObject obj = new JSONObject();
		obj.put("uid",this.uid);
		obj.put("fid",fid);
		obj.put("msg",msg);

		msg_json_str  = new String(obj.toString());
		System.out.println("msg json="+msg_json_str);
		Packet msg_packet=new Packet(msg_json_str.length(),Client.SEND_MSG_CMD,this.uid,msg_json_str);
		NetHelper.send_packet(this.os,msg_packet);
	}
	
	public void rcv_msg() throws IOException {
		Packet resp_packet = NetHelper.rcv_packet(this.dis);
		if ( null == resp_packet) {
			return;
		}
		else {
			System.out.println("rcv msg:"+resp_packet.getData());
		}
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
	
    public static void main(String []args) throws InterruptedException{

		Client client = new Client(101,123456);
		try {
			client.connect_to_server("192.168.66.134",1088);
		}
		catch (IOException e) {
			System.out.println("connect to server error.\n");
			return;
		}

		try {
			if  ( 0 == client.login()) 
				return;
			Chat chat = new Chat(client,102);
			chat.start();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("network not ok.");
			try {
				client.disconnect_server();
			}
			catch (IOException e2) {
				e2.printStackTrace();
			}
			return ;
		}

		while(true) {
			try {
				client.rcv_msg();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			Thread.sleep(200);
			System.out.println("main loop...............");
		}
	};
};

