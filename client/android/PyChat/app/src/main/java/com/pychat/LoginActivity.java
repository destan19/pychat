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

public class LoginActivity extends ActionBarActivity {
    private Button login_button;
    private TextView result_view;
    private EditText username_edit;
    private EditText password_edit;

    public Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        login_button = (Button)findViewById(R.id.btn_login);
        client = Client.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_edit =  (EditText)findViewById(R.id.et_username);
                password_edit = (EditText)findViewById(R.id.et_password);
                result_view = (TextView)findViewById(R.id.result);

                String username = username_edit.getText().toString();
                String password = password_edit.getText().toString();

                if (username.length() == 0 || password.length() == 0) {
                    result_view.setText("please input username or password.");
                    return;
                }
                if (username.length() > 16 ) {
                    result_view.setText("username is too long.");
                    return;
                }
                if (password.length() > 32) {
                    result_view.setText("password is too long.");
                    return;
                }
                if (1 == client.connectToServer("192.168.66.134", 1088)) {
                    try {
                        if (1 == client.login(Integer.parseInt(username), Integer.parseInt(password))) {
                            result_view.setText("login success.");
                            // launch main activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            result_view.setText("username or password error.");
                        }
                    } catch (Exception e) {
                        result_view.setText("network not ok." + username.length());
                        return;
                    }
                }
                else {
                    result_view.setText("connect to server error.");
                }
            }
        });
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
