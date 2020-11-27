package com.softcon.thetutorcpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

public class ChangePasswordActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private int insert1, insert2;

    private String UserEmailString;
    private String PasswordString;

    private int HttpUploadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        /* 인스턴스화 */
        final EditText password1EditText = findViewById(R.id.ChangePassword_EditText_new1);
        final EditText password2EditText = findViewById(R.id.ChangePassword_EditText_new2);
        final Button okButton = findViewById(R.id.ChangePassword_Button_done);
        final ImageButton backButton = findViewById(R.id.ChangePassword_ImageButton_back);

        final TextView alert1TextView = findViewById(R.id.ChangePassword_TextView_alert1);
        final TextView alert2TextView = findViewById(R.id.ChangePassword_TextView_alert2);

        ConstraintLayout CoverView = findViewById(R.id.ChangePasswordActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(ChangePasswordActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* 초기화 */
        insert1 = insert2 = 0;
        okButton.setEnabled(false);
        HttpUploadErrorCount = 0;

        password1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password1EditText.getText().toString().equals("")) {
                    insert1 = 0;
                } else {
                    insert1 = 1;
                }
                if (insert1 + insert2 == 2) {
                    if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (password1EditText.getText().toString().length() >= 8) {
                    alert1TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert1TextView.setVisibility(View.VISIBLE);
                }
                if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                    alert2TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert2TextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password2EditText.getText().toString().equals("")) {
                    insert2 = 0;
                } else {
                    insert2 = 1;
                }
                if (insert1 + insert2 == 2) {
                    if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (password1EditText.getText().toString().length() >= 8) {
                    alert1TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert1TextView.setVisibility(View.VISIBLE);
                }
                if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                    alert2TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert2TextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                String temp = pref.getString("user", "");
                /* AES 256 암호화 */
                try {
                    assert temp != null;
                    UserEmailString = AES256Chiper.AES_Encode(temp);
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    PasswordString = AES256Chiper.AES_Encode(password1EditText.getText().toString());
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                okButton.setEnabled(false);
                okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                okButton.setText("저장 중...");
                password1EditText.setTextColor(Color.parseColor("#818181"));
                password2EditText.setTextColor(Color.parseColor("#818181"));
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
                Intent intent = new Intent(ChangePasswordActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangePasswordActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/changePassword/");
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
            String myResult = builder.toString();
            Log.i("CHANGE PASSWORD", myResult);
            switch (myResult) {
                case "1": {
                    Intent intent = new Intent(ChangePasswordActivity.this, SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                }
                case "0": {
                    //
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(ChangePasswordActivity.this, NoInternetActivity.class);
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
}