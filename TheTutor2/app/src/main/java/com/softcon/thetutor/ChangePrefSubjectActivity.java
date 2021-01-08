package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangePrefSubjectActivity extends AppCompatActivity {

    int[] prefS = new int[10];
    private String pref_sub;
    int minS = 0;

    private int[] subjects = new int[10];
    String[] data = new String[15];
    String[] subData = new String[10];

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout layout5;
    private LinearLayout layout6;
    private LinearLayout layout7;
    private LinearLayout layout8;
    private LinearLayout layout9;
    private LinearLayout layout10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pref_subject);

        try {
            Thread.sleep(200);
            new Thread() {
                public void run() {
                    HttpPostData_GET_DATA();
                }
            }.start();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        /* 인스턴스화 */
        RatingBar rb1 = (RatingBar)findViewById(R.id.ratingBar1);
        RatingBar rb2 = (RatingBar)findViewById(R.id.ratingBar2);
        RatingBar rb3 = (RatingBar)findViewById(R.id.ratingBar3);
        RatingBar rb4 = (RatingBar)findViewById(R.id.ratingBar4);
        RatingBar rb5 = (RatingBar)findViewById(R.id.ratingBar5);
        RatingBar rb6 = (RatingBar)findViewById(R.id.ratingBar6);
        RatingBar rb7 = (RatingBar)findViewById(R.id.ratingBar7);
        RatingBar rb8 = (RatingBar)findViewById(R.id.ratingBar8);
        RatingBar rb9 = (RatingBar)findViewById(R.id.ratingBar9);
        RatingBar rb10 = (RatingBar)findViewById(R.id.ratingBar10);

        layout1 = (LinearLayout)findViewById(R.id.sub_layer1);
        layout2 = (LinearLayout)findViewById(R.id.sub_layer2);
        layout3 = (LinearLayout)findViewById(R.id.sub_layer3);
        layout4 = (LinearLayout)findViewById(R.id.sub_layer4);
        layout5 = (LinearLayout)findViewById(R.id.sub_layer5);
        layout6 = (LinearLayout)findViewById(R.id.sub_layer6);
        layout7 = (LinearLayout)findViewById(R.id.sub_layer7);
        layout8 = (LinearLayout)findViewById(R.id.sub_layer8);
        layout9 = (LinearLayout)findViewById(R.id.sub_layer9);
        layout10 = (LinearLayout)findViewById(R.id.sub_layer10);

        Button bt_next = (Button)findViewById(R.id.button_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefS[0] == 0 || prefS[1] == 0 || prefS[2] == 0 || prefS[3] == 0 || prefS[4] == 0 || prefS[5] == 0 || prefS[6] == 0 || prefS[7] == 0 || prefS[8] == 0 || prefS[9] == 0){
                    System.out.println("최소 한 개 이상 선택하시오");
                }
                else{
                    pref_sub = Integer.toString(prefS[0]) + Integer.toString(prefS[1]) + Integer.toString(prefS[2]) + Integer.toString(prefS[3]);
                    pref_sub = pref_sub + Integer.toString(prefS[4]) + Integer.toString(prefS[5]) + Integer.toString(prefS[6]) + Integer.toString(prefS[7]);
                    pref_sub = pref_sub + Integer.toString(prefS[8]) + Integer.toString(prefS[9]);
                    System.out.println(pref_sub);
                    new Thread() {
                        public void run() {
                            HttpPostData();
                        }
                    }.start();
                }
            }
        });

        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[0] = (int)(rating);
            }
        });

        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[1] = (int)(rating);
            }
        });

        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[2] = (int)(rating);
            }
        });

        rb4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[3] = (int)(rating);
            }
        });

        rb5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[4] = (int)(rating);
            }
        });

        rb6.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[5] = (int)(rating);
            }
        });

        rb7.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[6] = (int)(rating);
            }
        });

        rb8.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[7] = (int)(rating);
            }
        });

        rb9.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[8] = (int)(rating);
            }
        });

        rb10.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                prefS[9] = (int)(rating);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "정보를 입력해주세요!", Toast.LENGTH_LONG).show();
        return;
    }

    public void HttpPostData() {
        try {
            String conc_time = data[5];
            //* 유저 정보 가져오기 */
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            String userID = pref.getString("userid", "");
            URL url = new URL("http://141.164.36.245:8000/account/profile2/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            StringBuffer buffer = new StringBuffer();
            buffer.append("userid").append("=").append(userID).append("&");
            buffer.append("pref_sub").append("=").append(pref_sub).append("&");
            buffer.append("conc_time").append("=").append(conc_time);
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            String myResult = builder.toString();
            if(myResult.equals("1")){
                finish();
            }
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData

    public void HttpPostData_GET_DATA() {
        try {
            //* 유저 정보 가져오기 */
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            String userID = pref.getString("userid", "");
            URL url = new URL("http://141.164.36.245:8000/account/userInfo/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            StringBuffer buffer = new StringBuffer();
            buffer.append("userid").append("=").append(userID);
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            int i = 0;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
                data[i] = str;
                i++;

            }
            String myResult = builder.toString();
            System.out.println(myResult);
            subData = data[2].split("");
            System.out.println("subData : " + subData);
            System.out.println("data[3] : " + data[2]);
            handler.sendEmptyMessage(0);
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
            params2.height = 0;

            for(int j = 0;j<10;j++){
                subjects[j] = Integer.parseInt(subData[j]);
            }

            System.out.println(subjects);

            if(subjects[0] == 1){
                layout1.setVisibility(View.VISIBLE);
            }
            else if(subjects[0] == 0){
                layout1.setVisibility(View.INVISIBLE);
                layout1.setLayoutParams(params2);
            }

            if(subjects[1] == 1){
                layout2.setVisibility(View.VISIBLE);
            }
            else if(subjects[1] == 0){
                layout2.setVisibility(View.INVISIBLE);
                layout2.setLayoutParams(params2);
            }

            if(subjects[2] == 1){
                layout3.setVisibility(View.VISIBLE);
            }
            else if(subjects[2] == 0){
                layout3.setVisibility(View.INVISIBLE);
                layout3.setLayoutParams(params2);
            }

            if(subjects[3] == 1){
                layout4.setVisibility(View.VISIBLE);
            }
            else if(subjects[3] == 0){
                layout4.setVisibility(View.INVISIBLE);
                layout4.setLayoutParams(params2);
            }

            if(subjects[4] == 1){
                layout5.setVisibility(View.VISIBLE);
            }
            else if(subjects[4] == 0){
                layout5.setVisibility(View.INVISIBLE);
                layout5.setLayoutParams(params2);
            }

            if(subjects[5] == 1){
                layout6.setVisibility(View.VISIBLE);
            }
            else if(subjects[5] == 0){
                layout6.setVisibility(View.INVISIBLE);
                layout6.setLayoutParams(params2);
            }

            if(subjects[6] == 1){
                layout7.setVisibility(View.VISIBLE);
            }
            else if(subjects[6] == 0){
                layout7.setVisibility(View.INVISIBLE);
                layout7.setLayoutParams(params2);
            }

            if(subjects[7] == 1){
                layout8.setVisibility(View.VISIBLE);
            }
            else if(subjects[7] == 0){
                layout8.setVisibility(View.INVISIBLE);
                layout8.setLayoutParams(params2);
            }

            if(subjects[8] == 1){
                layout9.setVisibility(View.VISIBLE);
            }
            else if(subjects[8] == 0){
                layout9.setVisibility(View.INVISIBLE);
                layout9.setLayoutParams(params2);
            }

            if(subjects[9] == 1){
                layout10.setVisibility(View.VISIBLE);
            }
            else if(subjects[9] == 0){
                layout10.setVisibility(View.INVISIBLE);
                layout10.setLayoutParams(params2);
            }
        }
    };
}