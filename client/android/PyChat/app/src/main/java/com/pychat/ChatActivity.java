package com.pychat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.os.StrictMode;

import org.json.JSONException;

import java.io.IOException;
import java.net.Socket;

public class ChatActivity extends ActionBarActivity {
    EditText et_send = null;
    Button btn_msg = null;
    private int fid = 0;
    private Client client = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();
        fid = bundle.getInt("fid");
        client = Client.getInstance();
        et_send = (EditText)findViewById(R.id.MessageText);
        btn_msg = (Button)findViewById(R.id.MessageButton);
        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.sendMsgToFriend(fid, et_send.getText().toString());
                    et_send.setText("");
                }catch (Exception e) {
                    return;
                }
            }
        });
        et_send.setText(Integer.toString(fid));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
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
