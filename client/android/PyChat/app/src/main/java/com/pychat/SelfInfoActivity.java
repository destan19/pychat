package com.pychat;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;


public class SelfInfoActivity extends ActionBarActivity {


    private void initImages() {
        ImageView setting_img = (ImageView)findViewById(R.id.setting);
        setting_img.setImageResource(R.drawable.menu_setting);

        ImageView wallet_img = (ImageView)findViewById(R.id.wallet);
        wallet_img.setImageResource(R.drawable.menu_wallet);

        ImageView save_img = (ImageView)findViewById(R.id.mark);
        save_img.setImageResource(R.drawable.menu_save);

        ImageView photos_img = (ImageView)findViewById(R.id.photos);
        photos_img.setImageResource(R.drawable.menu_photo);

        ImageView fase_img = (ImageView)findViewById(R.id.face);
        fase_img.setImageResource(R.drawable.menu_face);
    }

   public View.OnTouchListener touchlistner =   new View.OnTouchListener() {
       Intent intent;
       public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setBackgroundDrawable(getResources().getDrawable(R.color.list_divider_color));

                    break;
                case MotionEvent.ACTION_UP:
                    v.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    int view_id = v.getId();
                    switch (view_id){
                        case R.id.lo_baseinfo:
                            // launch a new page
                            intent = new Intent(getApplicationContext(), BaseInfoActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.lo_photos:
                            intent = new Intent(getApplicationContext(), PhotosActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.lo_mark:
                            break;
                        case R.id.lo_wallet:
                            break;
                        case R.id.lo_face:
                            break;
                        case R.id.lo_setting:
                            break;
                        default:
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initImages();
        LinearLayout baseinfo_layout = (LinearLayout)findViewById(R.id.lo_baseinfo);
        LinearLayout photos_layout = (LinearLayout)findViewById(R.id.lo_photos);
        LinearLayout mark_layout = (LinearLayout)findViewById(R.id.lo_mark);
        LinearLayout wallet_layout = (LinearLayout)findViewById(R.id.lo_wallet);
        LinearLayout face_layout = (LinearLayout)findViewById(R.id.lo_face);
        LinearLayout setting_layout = (LinearLayout)findViewById(R.id.lo_setting);
        /*
        baseinfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.WHITE);
            }
        });*/
        baseinfo_layout.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        ImageView img = (ImageView)findViewById(R.id.self_photo);
        img.setImageResource(R.drawable.s2);


        baseinfo_layout.setOnTouchListener(touchlistner);
        photos_layout.setOnTouchListener(touchlistner);
        mark_layout.setOnTouchListener(touchlistner);
        wallet_layout.setOnTouchListener(touchlistner);
        face_layout.setOnTouchListener(touchlistner);
        setting_layout.setOnTouchListener(touchlistner);
    }
}
