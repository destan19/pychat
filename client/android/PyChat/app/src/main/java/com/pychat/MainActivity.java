package com.pychat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private ListView lv_friend;
    private TextView msg_view;
    private Handler handler = null;

    private ArrayAdapter<String> adapter;
    public Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_friend = (ListView)findViewById(R.id.lv_friend);

        client = Client.getInstance();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);
        lv_friend.setAdapter(adapter);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CmdConstant.FRIEND_LIST_RESPONSE:
                        List <Integer> friend_list = client.getFriendList();
                        if (friend_list != null) {
                            for (int i = 0; i < friend_list.size(); i++) {
                                adapter.add(Integer.toString(friend_list.get(i)));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        client.registerActivity(Client.ACTIVITY_MAIN,this);
        try {
            client.requestFriendList();
        }catch ( IOException e) {
            return;
        }
    }
    public Handler getHandler() {
        return this.handler;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void handleFriendList(Packet packet)  {
        List <Integer> friend_list = client.getFriendList();
        if (friend_list != null) {
            for (int i = 0; i < friend_list.size(); i++) {
                adapter.add(Integer.toString(friend_list.get(i)));
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
