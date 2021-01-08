package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class GetPrefTimeActivity extends AppCompatActivity {

    int[] prefS = new int[6];
    private String pref_sub;
    private String conc_time;
    int minS = 0;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_get_pref_time);

        Intent intent = getIntent();
        pref_sub = intent.getExtras().getString("prefSubData");

        /* 인스턴스화 */
        RatingBar rb1 = (RatingBar)findViewById(R.id.ratingBar1);
        RatingBar rb2 = (RatingBar)findViewById(R.id.ratingBar2);
        RatingBar rb3 = (RatingBar)findViewById(R.id.ratingBar3);
        RatingBar rb4 = (RatingBar)findViewById(R.id.ratingBar4);
        RatingBar rb5 = (RatingBar)findViewById(R.id.ratingBar5);
        RatingBar rb6 = (RatingBar)findViewById(R.id.ratingBar6);

        Button bt_next = (Button)findViewById(R.id.button_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefS[0] == 0 || prefS[1] == 0 || prefS[2] == 0 || prefS[3] == 0 || prefS[4] == 0 || prefS[5] == 0){
                    System.out.println("최소 한 개 이상 선택하시오");
                }
                else{
                    conc_time = Integer.toString(prefS[0]) + Integer.toString(prefS[1]) + Integer.toString(prefS[2]) + Integer.toString(prefS[3]);
                    conc_time = conc_time + Integer.toString(prefS[4]) + Integer.toString(prefS[5]);
                    System.out.println(conc_time);
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

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "정보를 입력해주세요!", Toast.LENGTH_LONG).show();
        return;
    }

    public void HttpPostData() {
        try {
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
            if(!myResult.equals(null) && myResult.equals("1")){
                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                intent.putExtra("step", 4);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref2.edit();
                editor.putString("autoLogin", "true");
                editor.commit();
                startActivity(intent);
                finish();
            }
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData
}