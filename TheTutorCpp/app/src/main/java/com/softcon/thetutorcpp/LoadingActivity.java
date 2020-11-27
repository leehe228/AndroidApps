package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoadingActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        /* 로고 애니메이션 */
        Animation LogoAnimation = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.translate);
        final ImageView Loading_ImageView_logo = findViewById(R.id.Loading_ImageView_logo);
        Loading_ImageView_logo.startAnimation(LogoAnimation);

        new Thread() {
            public void run() {
                HttpPostData();
            }
        }.start();
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            System.out.println("CANNOT CONNECT TO SERVER");
            Intent intent = new Intent(LoadingActivity.this, NoInternetActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            /* 자동 로그인 정보 불러오기 */
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            String result = pref.getString("autoLogin", "false");

            assert result != null;
            switch (result) {
                case "true": {
                    Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "lock": {
                    Intent intent = new Intent(LoadingActivity.this, LockActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "getSub": {
                    Intent intent = new Intent(LoadingActivity.this, GetSubjectActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "tutorial": {
                    Intent intent = new Intent(LoadingActivity.this, TutorialActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    break;
                }
                default: {
                    Intent intent = new Intent(LoadingActivity.this, HelloActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
    }
}

