package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SeeMoreActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private int numSubData;
    private String nameDataString;
    private String[] nameData = new String[10];

    private String UserEmailString;

    /* 버튼 */
    private Button[] subButton = new Button[10];

    private int HttpDownloadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String userTemp = pref.getString("user", "");

        HttpDownloadErrorCount = 0;

        /* AES 256 암호화 */
        try {
            assert userTemp != null;
            UserEmailString = AES256Chiper.AES_Encode(userTemp);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        /* 초기화 */
        new Thread() {
            public void run() {
                HttpPostData_SUB_DATA();
            }
        }.start();

        /* 인스턴스화 */
        final ImageButton backButton = findViewById(R.id.seeMore_ImageButton_back);
        subButton[0] = findViewById(R.id.seeMore_Button_sub0);
        subButton[1] = findViewById(R.id.seeMore_Button_sub1);
        subButton[2] = findViewById(R.id.seeMore_Button_sub2);
        subButton[3] = findViewById(R.id.seeMore_Button_sub3);
        subButton[4] = findViewById(R.id.seeMore_Button_sub4);
        subButton[5] = findViewById(R.id.seeMore_Button_sub5);
        subButton[6] = findViewById(R.id.seeMore_Button_sub6);
        subButton[7] = findViewById(R.id.seeMore_Button_sub7);
        subButton[8] = findViewById(R.id.seeMore_Button_sub8);
        subButton[9] = findViewById(R.id.seeMore_Button_sub9);

        ConstraintLayout CoverView = findViewById(R.id.SeeMoreActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(SeeMoreActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* 과목 버튼 */
        subButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 1);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 2);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 3);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 4);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 5);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 6);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 7);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 8);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        subButton[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyActivity.class);
                intent.putExtra("subject", 9);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SeeMoreActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void HttpPostData_SUB_DATA() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/loadSub/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "&");
            }
            String myResult = builder.toString();
            Log.i("GOT DATA", myResult);
            numSubData = Integer.parseInt(myResult.split("&")[0]);
            Log.i("NUMBER", Integer.toString(numSubData));

            nameDataString = myResult.split("&")[1];

            for (int j = 0; j < numSubData; j++) {
                nameData[j] = nameDataString.split("-")[j];
            }

            Message msg = writeHandler.obtainMessage();
            writeHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(SeeMoreActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpDownloadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_SUB_DATA();
                }
            }.start();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler writeHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            int i;
            for (i = 0; i < numSubData; i++) {
                subButton[i].setVisibility(View.VISIBLE);
                subButton[i].setText(nameData[i]);
                subButton[i].setEnabled(true);
            }
            for (; i < 10; i++) {
                subButton[i].setVisibility(View.INVISIBLE);
                subButton[i].setEnabled(false);
            }
        }
    };
}