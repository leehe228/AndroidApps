package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryInfoActivity extends BaseActivity {

    Gallery gallery;

    Bitmap backgroundImage = null;

    ImageView backgroundImageView;
    ImageView mainImageView;
    TextView infoTitleTextView;
    TextView creatorTextView;
    TextView infoContentTextView;

    private int NUMBER;

    private ArrayList<Bitmap> IMAGES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_info);
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        // 인스턴스화
        backgroundImageView = findViewById(R.id.GalleryInfo_ImageView_background);
        infoTitleTextView = findViewById(R.id.GalleryInfo_TextView_title);
        creatorTextView = findViewById(R.id.GalleryInfo_TextView_creator);
        infoContentTextView = findViewById(R.id.GalleryInfo_TextView_content);
        mainImageView = findViewById(R.id.GalleryInfo_ImageView_main);

        Button enterButton = findViewById(R.id.GalleryInfo_Button_enter);
        Button enterVRButton = findViewById(R.id.GalleryInfo_Button_enterVR);

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(null, 0);

        IMAGES = new ArrayList<>();

        // onCreate
        Intent intent = getIntent();
        final String CODE = intent.getStringExtra("CODE");
        gallery = new Gallery(CODE);
        NUMBER = gallery.getNUMBER();

        final String finalCODE = CODE;
        new Thread() {
            public void run() {
                try {
                    java.net.URL url = new java.net.URL( "http://141.164.40.63:8000/media/database/" + finalCODE + "/" + "1.jpg");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    backgroundImage = BitmapFactory.decodeStream(input);
                    Message msg = initHandler.obtainMessage();
                    initHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    backgroundImage = null;
                }
            }
        }.start();

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                intent.putExtra("CODE", CODE);
                intent.putExtra("TITLES", gallery.getART_TITLES());
                intent.putExtra("CONTENTS", gallery.getART_CONTENT());
                intent.putExtra("NUMBER", gallery.getNUMBER());
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        enterVRButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "곧 업데이트 됩니다", Toast.LENGTH_LONG);
                for(int i = 1; i < gallery.getNUMBER(); i++) {
                    final int finalI = i;
                    new Thread() {
                        public void run() {
                            try {

                                java.net.URL url = new java.net.URL("http://141.164.40.63:8000/media/database/" + finalCODE + "/" + finalI + ".jpg");
                                Log.i("i", String.valueOf(finalI));
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream input = connection.getInputStream();
                                IMAGES.add(BitmapFactory.decodeStream(input));

                            } catch (IOException e) {
                                e.printStackTrace();
                                IMAGES.add(null);
                            }
                        }
                    }.start();
                }
            }
        });
    }

    /* 실패 시 */
    @SuppressLint("HandlerLeak")
    Handler failHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {

        }
    };

    /* 화면 세팅 */
    @SuppressLint("HandlerLeak")
    Handler initHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            /*Log.e("WAIT TO DOWNLOAD", "");
            while(!gallery.getIsREADY());
            Log.e("SUCCESS!", "");*/
            //backgroundImage = gallery.getPRIMARY_IMAGES();
            backgroundImageView.setImageBitmap(backgroundImage);
            mainImageView.setImageBitmap(backgroundImage);
            infoTitleTextView.setText(gallery.getTITLE());
            infoContentTextView.setText(gallery.getINFORMATION());
            creatorTextView.setText(gallery.getCREATOR());
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