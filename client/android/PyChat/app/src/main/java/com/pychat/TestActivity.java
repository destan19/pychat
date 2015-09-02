package com.pychat;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;


public class TestActivity extends ActionBarActivity {
    private TextView test_view;
    private Handler handler;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        test_view = (TextView)findViewById(R.id.test);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        test_view.setText(msg.obj.toString());
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        };
        thread = new Thread() {
            int timer = 0;
          @Override
            public void run(){
              while(true) {
                  try {
                      Thread.currentThread().sleep(1000);
                      timer++;
                      Message msg = new Message();
                      msg.obj = timer;
                      msg.what = 1;
                      handler.sendMessage(msg);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }
        };

        thread.start();
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
