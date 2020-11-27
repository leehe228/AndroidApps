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

public class LoginActivity extends BaseActivity {

    private int insert1, insert2;

    private EditText emailEditText;
    private EditText passwordEditText;
    private String UserEmailString, PasswordString, UserEmailStringtoSave;
    private Button loginButton;
    private TextView bigAlertTextView;

    private int HttpUploadErrorCount;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HttpUploadErrorCount = 0;

        /* 인스턴스화 */
        emailEditText = findViewById(R.id.Login_EditText_email);
        passwordEditText = findViewById(R.id.Login_EditText_password);
        Button findPasswordButton = findViewById(R.id.Login_Button_findPassword);
        loginButton = findViewById(R.id.Login_Button_done);
        ImageButton backButton = findViewById(R.id.Login_ImageButton_back);
        final TextView alertTextView = findViewById(R.id.Login_TextView_alert1);
        bigAlertTextView = findViewById(R.id.Login_TextView_bigAlert);

        ConstraintLayout CoverView = findViewById(R.id.LoginActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* 초기화 */
        insert1 = insert2 = 0;
        loginButton.setEnabled(false);

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
                if (insert1 + insert2 == 2) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button));
                        loginButton.setEnabled(true);
                    } else {
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                        loginButton.setEnabled(false);
                    }
                } else {
                    loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                    loginButton.setEnabled(false);
                }

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                    alertTextView.setVisibility(View.INVISIBLE);
                } else {
                    alertTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordEditText.getText().toString().equals("")) {
                    insert2 = 0;
                } else {
                    insert2 = 1;
                }
                if (insert1 + insert2 == 2) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button));
                        loginButton.setEnabled(true);
                    } else {
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                        loginButton.setEnabled(false);
                    }
                } else {
                    loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                    loginButton.setEnabled(false);
                }

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                    alertTextView.setVisibility(View.INVISIBLE);
                } else {
                    alertTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                /* AES 256 암호화 */
                try {
                    UserEmailString = AES256Chiper.AES_Encode(emailEditText.getText().toString());
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    PasswordString = AES256Chiper.AES_Encode(passwordEditText.getText().toString());
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                UserEmailStringtoSave = emailEditText.getText().toString();
                loginButton.setEnabled(false);
                loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                loginButton.setText("로그인 중...");
                emailEditText.setTextColor(Color.parseColor("#818181"));
                passwordEditText.setTextColor(Color.parseColor("#818181"));
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
                Intent intent = new Intent(LoginActivity.this, HelloActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, HelloActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/login/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString + "&" + "password" + "=" + PasswordString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            try {
                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            String myResult = builder.toString();
            Log.i("LOGIN", myResult);
            switch (myResult) {
                case "-1": {
                    // 계정 없음
                    loginButton.setEnabled(true);
                    loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button));
                    loginButton.setText("로그인");
                    break;
                }
                case "1": {
                    // 로그인 성공
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user", UserEmailStringtoSave);
                    editor.putString("autoLogin", "tutorial");
                    editor.putString("push", "true");
                    editor.putString("theme", "dark");
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, TutorialActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                }
                case "0": {
                    // 정보 틀림
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(LoginActivity.this, NoInternetActivity.class);
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
            loginButton.setEnabled(true);
            loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button));
            loginButton.setText("로그인");
            bigAlertTextView.setVisibility(View.VISIBLE);
            emailEditText.setTextColor(Color.parseColor("#000000"));
            passwordEditText.setTextColor(Color.parseColor("#000000"));
        }
    };
}