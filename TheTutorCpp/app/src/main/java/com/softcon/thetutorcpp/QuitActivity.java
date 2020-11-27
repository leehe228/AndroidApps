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
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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

public class QuitActivity extends BaseActivity {

    private int insert1, insert2;

    private Button okButton;
    private EditText emailEditText, passwordEditText;
    private TextView text3TextView;

    private String UserEmailString, PasswordString;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private int HttpUploadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit);

        /* 초기화 */
        insert1 = insert2 = 0;
        HttpUploadErrorCount = 0;

        /* 인스턴스화 */
        final ImageButton backButton = findViewById(R.id.Quit_ImageButton_back);
        okButton = findViewById(R.id.Quit_Button_done);
        emailEditText = findViewById(R.id.Quit_EditText_email);
        passwordEditText = findViewById(R.id.Quit_EditText_password);
        text3TextView = findViewById(R.id.Quit_TextView_text3);

        ConstraintLayout CoverView = findViewById(R.id.QuitActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(QuitActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                    insert1 = 1;
                } else {
                    insert1 = 0;
                }

                if (insert1 + insert2 == 2) {
                    okButton.setBackground(ContextCompat.getDrawable(QuitActivity.this, R.drawable.basic_button_red));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(QuitActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (passwordEditText.getText().toString().length() > 0) {
                    insert2 = 1;
                } else {
                    insert2 = 0;
                }

                if (insert1 + insert2 == 2) {
                    okButton.setBackground(ContextCompat.getDrawable(QuitActivity.this, R.drawable.basic_button_red));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(QuitActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okButton.setBackground(ContextCompat.getDrawable(QuitActivity.this, R.drawable.basic_button_unclick));
                okButton.setEnabled(false);
                okButton.setText("정보 확인 중...");
                emailEditText.setEnabled(false);
                passwordEditText.setEnabled(false);
                emailEditText.setTextColor(Color.parseColor("#818181"));
                passwordEditText.setTextColor(Color.parseColor("#818181"));
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
                Intent intent = new Intent(QuitActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
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
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            Thread.sleep(500);
            String myResult = builder.toString();
            Log.i("LOGIN to QUIT", myResult);
            switch (myResult) {
                case "-1": {
                    // 계정 없음
                }
                case "0": {
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                    break;
                }
                case "1": {
                    okButton.setText("탈퇴 진행 중...");
                    // 탈퇴 진행
                    HttpUploadErrorCount = 0;
                    Message msg = handShakeHandler.obtainMessage();
                    handShakeHandler.sendMessage(msg);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(QuitActivity.this, NoInternetActivity.class);
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

    public void HttpPostData_QUIT() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/delete/");
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
                builder.append(str);
            }
            String myResult = builder.toString();
            Log.i("QUIT", myResult);
            Thread.sleep(500);
            switch (myResult) {
                case "1": {
                    // 탈퇴 성공
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user", "");
                    editor.putString("autoLogin", "false");
                    editor.apply();

                    Intent intent = new Intent(QuitActivity.this, LoadingActivity.class);
                    ActivityCompat.finishAffinity(QuitActivity.this);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                case "0": {
                    // 탈퇴 실패

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(QuitActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpUploadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_QUIT();
                }
            }.start();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            // 다시 시도하도록 화면 초기화
            okButton.setEnabled(true);
            text3TextView.setText("입력한 정보를 다시 한 번 확인해주세요.");
            emailEditText.setEnabled(true);
            passwordEditText.setEnabled(true);
            emailEditText.setTextColor(Color.parseColor("#000000"));
            passwordEditText.setTextColor(Color.parseColor("#000000"));
            okButton.setBackground(ContextCompat.getDrawable(QuitActivity.this, R.drawable.basic_button));
            okButton.setText("탈퇴하기");
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handShakeHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            new Thread() {
                public void run() {
                    HttpPostData_QUIT();
                }
            }.start();
        }
    };
}