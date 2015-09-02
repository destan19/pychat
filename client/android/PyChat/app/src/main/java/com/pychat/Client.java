package com.pychat;
import android.app.Activity;

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
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by derry on 2015/8/21.
 */
public class Client  {
    final static byte LOGIN_REQUEST_CMD = 1;
	final static byte LOGIN_RESPONSE_CMD = 2;
	final static byte FRIEND_LIST_REQUEST_CMD = 3;
	final static byte FRIEND_LIST_RESPONSE_CMD = 4;
	final static byte SEND_MSG_CMD = 20;
	final static byte MSG_RESPONSE_CMD = 21;

    final static int ACTIVITY_LOGIN = 1;
    final static int ACTIVITY_MAIN = 2;
    private MainActivity mainActivity = null;
	
	private String host = null;
	private int port = 80;
	private static Socket socket;
	public  OutputStream os;
	public  InputStream is;
	public DataInputStream dis;
	public DataOutputStream dos;
	private int uid;
	private int password;
	private List<Integer> friend_list =  null;
	private Queue<Packet> pkt_queue = new LinkedList<Packet>();;
	private int login_status = -1 ; /*登录状态*/
	RecvMsgThread rcv_thread ;
	MsgDistributeThread distribute_thread;
    private static Client client = null;

    /*只有一个实例*/
    public static Client getInstance() {
        if ( null == client) {
            client = new Client();
        }
        return client;
    }

	public Packet popPacket() {
		return this.pkt_queue.remove();
	}
	public void addPacket(Packet packet) {
		this.pkt_queue.add(packet);
	}
	public boolean pktEmpty() {
		return this.pkt_queue.isEmpty();
	}
    public int getUid() {
        return this.uid;
    }
    public void registerActivity(int activityId,Activity activity) {
        switch (activityId) {
            case ACTIVITY_LOGIN:
                break;
            case ACTIVITY_MAIN:
                this.mainActivity = (MainActivity)activity;
                break;
            default:
                break;
        }
    }
	/*
		return:
		0: 失败
		1: 成功
		-1:网络错误
	*/
	public int login(int username,int password) throws IOException,InterruptedException,JSONException{
		this.uid = username;
		this.password = password;
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
		while (true) {
			Thread.sleep(100);
			if (this.login_status == 1) {
				return 1;
			}
			else {
				return 0;
			}
		}
		
	}
	
	public void handleLoginResponse(Packet packet) throws  JSONException{
		JSONObject resp_json = new JSONObject(packet.getData());
		if (resp_json.getString("login_status").equals("success")) {
			System.out.println("login ............success.");
			this.login_status = 1;
		}
		else  {
			System.out.println("login ............fail.");
			this.login_status = 0;
		}
	}
	
	public void handleFriendListResponse(Packet packet) {
		System.out.println("friendlist="+packet.getData());
	}
	
	public void handleReceivedMsg(Packet packet) {
		System.out.println("recv msg="+packet.getData());
	}
	public void sendMsgToFriend(int fid,String msg) throws IOException,JSONException{
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
	
	public void getFriendList() throws IOException,JSONException {
		String req_str = null;
		JSONObject req_json = new JSONObject();
	
		req_json.put("uid",this.uid);
		
		req_str  = new String(req_json.toString());
		System.out.println("login json="+req_str);
		Packet req_packet =new Packet(req_str.length(),Client.FRIEND_LIST_REQUEST_CMD,this.uid,req_str);
		NetHelper.send_packet(this.os,req_packet);
	}
	/*
		收到消息后处理
	*/
	public void msgHandle(Packet packet) throws IOException,JSONException {
		System.out.println("handle msg cmd:"+packet.getCmd());
		switch (packet.getCmd()) {
			case LOGIN_RESPONSE_CMD:
				handleLoginResponse(packet);
				break;
			case FRIEND_LIST_RESPONSE_CMD:
				handleFriendListResponse(packet);
				break;
			case SEND_MSG_CMD:
				handleReceivedMsg(packet);
				break;
			default:
				System.out.println("invalid msg. cmd="+packet.getCmd());
		}
	}

	public int connectToServer(String host,int port)  {
		try {
			this.socket = new Socket(host, port);
			this.os = this.socket.getOutputStream();
			this.is = this.socket.getInputStream();
			this.dis = new DataInputStream(this.is);
			this.dos = new DataOutputStream(this.os);

            this.rcv_thread = new RecvMsgThread(this);
            this.distribute_thread = new MsgDistributeThread(this);
            rcv_thread.start();
            distribute_thread.start();
		}
		catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	public int disconnectServer() throws IOException {
		try {
			this.socket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	public static void json_test() throws  JSONException{
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
	/*
    public static void main(String []args) throws InterruptedException,JSONException{

		Client client = new Client(101,123456);
		try {
			client.connectToServer("192.168.66.134",1088);
		}
		catch (IOException e) {
			System.out.println("connect to server error.\n");
			return;
		}
		RecvMsgThread rcv_thread = new RecvMsgThread(client);
		MsgDistributeThread distribute_thread = new MsgDistributeThread(client);
		rcv_thread.start();
		distribute_thread.start();
		
		try {
			if  ( 0 == client.login()) {
				return;
			}
			else {
				System.out.println("client "+client.uid + " login to server success.");
				client.getFriendList();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("network not ok.");
			try {
				client.disconnectServer();
			}
			catch (IOException e2) {
				e2.printStackTrace();
			}
			return ;
		}

		while(true) {
			Thread.sleep(2000);
		}
	};*/
};

