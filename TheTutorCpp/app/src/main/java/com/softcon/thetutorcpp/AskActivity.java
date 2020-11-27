package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AskActivity extends BaseActivity {

    private int insert1, insert2, insert3;

    private String UserEmailString, TitleString, ContentString;

    private int HttpUploadErrorCount;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private EditText emailEditText;
    private EditText titleEditText;
    private  EditText contentEditText;
    private  Button okButton;
    private TextView numTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        /* 인스턴스화 */
        emailEditText = findViewById(R.id.Ask_EditText_email);
        titleEditText = findViewById(R.id.Ask_EditText_title);
        contentEditText = findViewById(R.id.Ask_EditText_content);
        ConstraintLayout CoverView = findViewById(R.id.AskActivity);
        Animation TextAnimation = AnimationUtils.loadAnimation(AskActivity.this, R.anim.layout_up);
        CoverView.startAnimation(TextAnimation);

        okButton = findViewById(R.id.Ask_Button_done);
        ImageButton backButton = findViewById(R.id.Ask_ImageButton_back);

        numTextView = findViewById(R.id.Ask_TextView_textNum);

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        emailEditText.setText(pref.getString("user", ""));

        /* 초기화 */
        insert2 = insert3 = 0;
        insert1 = 1;
        HttpUploadErrorCount = 0;

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailEditText.getText().toString().equals("")) {
                    insert1 = 0;
                } else {
                    insert1 = 1;
                }
                if (insert1 + insert2 + insert3 == 3) {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleEditText.getText().toString().equals("")) {
                    insert2 = 0;
                } else {
                    insert2 = 1;
                }
                if (insert1 + insert2 + insert3 == 3) {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* 글자수 체크 */
                String inputText = "글자수 " + contentEditText.getText().toString().length() + "/200자";
                numTextView.setText(inputText);

                if (contentEditText.getText().toString().length() > 200) {
                    numTextView.setTextColor(Color.parseColor("#E50000"));
                } else {
                    numTextView.setTextColor(Color.parseColor("#000000"));
                }

                if (contentEditText.getText().toString().equals("") || contentEditText.getText().toString().length() > 200) {
                    insert3 = 0;
                } else {
                    insert3 = 1;
                }
                if (insert1 + insert2 + insert3 == 3) {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEmailString = emailEditText.getText().toString();
                TitleString = titleEditText.getText().toString();
                ContentString = contentEditText.getText().toString();
                emailEditText.setEnabled(false);
                emailEditText.setTextColor(Color.parseColor("#818181"));
                titleEditText.setTextColor(Color.parseColor("#818181"));
                contentEditText.setTextColor(Color.parseColor("#818181"));
                titleEditText.setEnabled(false);
                contentEditText.setEnabled(false);
                okButton.setEnabled(false);
                okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button_unclick));
                okButton.setText("전송중");
                new Thread() {
                    public void run() {
                        HttpPostData();
                    }
                }.start();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AskActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();

    }

    /* 문의 */
    public void HttpPostData() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/sendFeedback/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "email" + "=" + UserEmailString + "&" + "title" + "=" + TitleString + "&" + "content" + "=" + ContentString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            String myResult = builder.toString();
            Log.i("FEEDBACK", myResult);
            switch (myResult) {
                case "1": {
                    // 전송 성공
                    Intent intent = new Intent(AskActivity.this, SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                }
                case "0": {
                    // 실패
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(AskActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpUploadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData();
                }
            }.start();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            // 다시 시도하도록 화면 초기화
            emailEditText.setEnabled(true);
            emailEditText.setTextColor(Color.parseColor("#000000"));
            titleEditText.setTextColor(Color.parseColor("#000000"));
            contentEditText.setTextColor(Color.parseColor("#000000"));
            titleEditText.setEnabled(true);
            contentEditText.setEnabled(true);
            okButton.setEnabled(true);
            okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button));
            okButton.setText("보내기");
        }
    };
}