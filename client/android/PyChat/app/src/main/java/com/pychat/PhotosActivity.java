package com.pychat;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.LogRecord;

import com.pychat.TalkMsg;
import android.os.Handler;
import android.os.Message;

public class PhotosActivity extends ActionBarActivity implements AbsListView.OnScrollListener {
     private LinearLayout.LayoutParams ff_parms = new LinearLayout.LayoutParams(
             LinearLayout.LayoutParams.FILL_PARENT,
             LinearLayout.LayoutParams.FILL_PARENT
     );
    private LinearLayout.LayoutParams fw_parms = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
            );
    private  TextView mFooterView;
    private  TextView mDebugView;
    private  MyAdapter adapter;
    List<TalkMsg> TalkMsgList = null;

    public static void json_test() throws JSONException {

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

    }

    public JSONObject  parse_json(String json_str) throws JSONException{

        JSONObject list_obj_new = new JSONObject(json_str);
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
        return list_obj_new;
    }


   public  void initView() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
       layout.setGravity(Gravity.CENTER);
       layout.setBackgroundDrawable(getResources().getDrawable(R.color.white));
       mFooterView = new TextView(this);
        mDebugView = (TextView)findViewById(R.id.debug_text);
       mFooterView.setGravity(Gravity.CENTER);
       mFooterView.setText("loading...");
        layout.addView(mFooterView, fw_parms);
       LinearLayout loading_layout = new LinearLayout(this);
       loading_layout.addView(layout, ff_parms);

       ListView lv_content = (ListView)findViewById(R.id.lv_content);
       lv_content.addFooterView(loading_layout);


       adapter = new  MyAdapter(this);
   //    new NetworkThread().start();
       try {
           TalkMsgList = getTalkMsgObjList(5);
       }catch (Exception e) {
           e.printStackTrace();
       }

     //  mDebugView.setText(Integer.toString(TalkMsgList.size()));
       lv_content.setAdapter(adapter);
       lv_content.setOnScrollListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        initView();

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            mDebugView.setText("handle message");
            if (msg.what == 1) {
                adapter.notifyDataSetChanged();
                mDebugView.setText("data changed.");
                return;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                                      int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) { // reach the bottom
            mDebugView.setText("loading....");
            new NetworkThread().start();
            }


     //       mDebugView.setText(Integer.toString(TalkMsgList.size()));

     //   String text =Integer.toString(firstVisibleItem) +"----" +
     //           Integer.toString(visibleItemCount)+ "---"+Integer.toString(totalItemCount);
    //   mDebugView.setText(text);
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


    public class  MyAdapter extends BaseAdapter {

        private Context context;

        public MyAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return TalkMsgList.size();
        }

        @Override
        public Object getItem(int position) {
            return TalkMsgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.listview_item,null);

            TextView tv_friend_name = (TextView)convertView.findViewById(R.id.tv_friend_name);
            tv_friend_name.setText(TalkMsgList.get(position).friend_name);


            TextView textView = (TextView)convertView.findViewById(R.id.tv_content);
            textView.setText(TalkMsgList.get(position).content);

            ImageView iv_comment = (ImageView)convertView.findViewById(R.id.iv_comment);
            iv_comment.setImageResource(R.drawable.comment);

            ImageView iv_up = (ImageView)convertView.findViewById(R.id.iv_up);
            iv_up.setImageResource(R.drawable.up);
            textView.setTextColor(getResources().getColor(R.color.black));
            ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_friend_photo);
            if (position %2 == 0)
                 imageView.setImageResource(R.drawable.menu_wallet);
            else
                imageView.setImageResource(R.drawable.menu_save);


            return convertView;
        }
    }

    public List<TalkMsg> getTalkMsgObjList(int num)  throws  Exception{
        System.out.println("get talk list...");
        int i;
        List<TalkMsg> talk_list= new ArrayList<TalkMsg>();
        TalkMsg talk_msg ;
        if (num <=5) {
            for (i = 0; i < num+5 ; i++) {
                talk_msg = new TalkMsg();
                talk_msg.friend_name = "derry" + Integer.toString(i);
                talk_msg.content = Integer.toString(i) + "--Hunger Games fans may get to find out for themselves " +
                        "if the odds are forever in their favor.A 100-acre theme park dedicated to" +
                        " the blockbuster movie franchise starring Jennifer Lawrence and Liam Hemsworth" +
                        " is set to open in Atlanta by 2019, " +
                        "Lions Gate Entertainment Corp confirmed on Monday.It will feature specially ";
                talk_list.add(talk_msg);
            }
        }
        else {

                HttpGet httpGet = new HttpGet("http://test.pychat.xyz:8000/api/talk_msg_list?msg_num=" + Integer.toString(num + 10));
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);   //È¡µÃhttpÏìÓ¦

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    talk_msg = new TalkMsg();
               //     mDebugView.setText("handle 222");
                    String result;
                   result = EntityUtils.toString(httpResponse.getEntity());

                   // mDebugView.setText("handle 111");
              //     mDebugView.setText("result="+result+"----");
                    JSONObject obj_new = new JSONObject(result);
                  //  talk_msg.friend_name = obj_new.getString("name");
                 //   talk_msg.content = obj_new.getString("content");

                    JSONArray msgArr = obj_new.getJSONArray("list");
                    for (i = 0 ;i < msgArr.length();i++) {
                        JSONObject msg = msgArr.getJSONObject(i);
                        talk_msg = new TalkMsg();
                        talk_msg.friend_name =  msg.getString("name");
                        talk_msg.content = msg.getString("content");
                        talk_list.add(talk_msg);
                    }
                } else {
                    mDebugView.setText("null");
                    return null;
                }
        }
        return talk_list;
    }

    public class NetworkThread extends Thread {
        @Override
        public void run() {

            try {
                if (null == TalkMsgList)
                    TalkMsgList = getTalkMsgObjList(10);
                else
                    TalkMsgList = getTalkMsgObjList(TalkMsgList.size());

            }catch (Exception e) {

                return;
            }

            if (TalkMsgList != null) {
                handler.sendEmptyMessage(1);
            }
            else {
                mDebugView.setText("network not ok....");
            }
    }

}
}

