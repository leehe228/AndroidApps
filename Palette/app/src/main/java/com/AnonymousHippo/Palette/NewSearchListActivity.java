package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class NewSearchListActivity extends BaseActivity {

    GridListAdapter adapter;

    private String result;
    private String userEmail;
    String[] keys = new String[2];
    String[] data = new String[2];

    ArrayList<String> recCodeList;
    ArrayList<Bitmap> recImageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search_list);

        GridView gridView = findViewById(R.id.NewSearch_GridView);
        adapter = new GridListAdapter();
        gridView.setAdapter(adapter);

        recCodeList = new ArrayList<>();
        recImageArrayList = new ArrayList<>();

        Intent intent = getIntent();
        String openType = intent.getStringExtra("openType");

        SharedPreferences preferences = getSharedPreferences("com.AnonymousHippo.Palette.sharePreference", MODE_PRIVATE);
        userEmail = preferences.getString("userEmail", "");

        // 데이터 받아오기
        keys[0] = "email";
        data[0] = userEmail;

        switch(openType){
            case "like":{
                new Thread() {
                    public void run() {
                        result = HttpPostData.POST("account/getLike/", keys, data);

                        if (result.equals("-1") || result.equals("SEND_FAIL")) {

                        }
                        else if(result.equals("")){

                        }
                        else {
                            String[] resultList = result.split("-");

                            Collections.addAll(recCodeList, resultList);

                            try {
                                for (String s : recCodeList) {
                                    java.net.URL url = new java.net.URL("http://141.164.40.63:8000/media/database/" + s + "/1.jpg");
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setDoInput(true);
                                    connection.connect();
                                    InputStream input = connection.getInputStream();
                                    recImageArrayList.add(BitmapFactory.decodeStream(input));
                                }
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < recCodeList.size(); i++) {
                                Gallery temp = new Gallery(recCodeList.get(i));
                                adapter.addItem(temp.getTITLE(), recImageArrayList.get(i), temp.getCODE());
                            }

                            Message msg = initHandler.obtainMessage();
                            initHandler.sendMessage(msg);
                        }
                    }
                }.start();
                break;
            }

            case "interest":{
                new Thread() {
                    public void run() {
                        result = HttpPostData.POST("account/getInfo/", keys, data);

                        if (result.equals("") || result.equals("-1") || result.equals("SEND_FAIL")) {

                        } else {
                            String interest = result.split("&")[2];
                            keys[0] = "interest";
                            data[0] = interest;
                            result = HttpPostData.POST("gallery/suggestion/", keys, data);

                            String[] resultList = result.split("-");

                            Collections.addAll(recCodeList, resultList);

                            try {
                                for (String s : recCodeList) {
                                    java.net.URL url = new java.net.URL("http://141.164.40.63:8000/media/database/" + s + "/1.jpg");
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setDoInput(true);
                                    connection.connect();
                                    InputStream input = connection.getInputStream();
                                    recImageArrayList.add(BitmapFactory.decodeStream(input));
                                }
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < recCodeList.size(); i++) {
                                Gallery temp = new Gallery(recCodeList.get(i));
                                adapter.addItem(temp.getTITLE(), recImageArrayList.get(i), temp.getCODE());
                            }

                            Message msg = initHandler.obtainMessage();
                            initHandler.sendMessage(msg);
                        }
                    }
                }.start();
                break;
            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), GalleryInfoActivity.class);
                intent.putExtra("CODE", recCodeList.get(position));
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    // FAB 닫힘 Handler
    @SuppressLint("HandlerLeak")
    Handler initHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}