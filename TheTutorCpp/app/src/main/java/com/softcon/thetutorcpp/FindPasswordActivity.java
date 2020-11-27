package com.softcon.thetutorcpp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class FindPasswordActivity extends BaseActivity {

    private int insert1, insert2;

    private int step;

    private String UserEmailString, PasswordString;
    private String AnswerOkCodeString;

    private int HttpUploadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        HttpUploadErrorCount = 0;

        /* 인스턴스화 */
        final EditText emailEditText = findViewById(R.id.FindPassword_EditText_email);
        final EditText okCodeEditText = findViewById(R.id.FindPassword_EditText_okCode);
        final EditText password1EditText = findViewById(R.id.FindPassword_EditText_password1);
        final EditText password2EditText = findViewById(R.id.FindPassword_EditText_password2);

        final Button emailSendButton = findViewById(R.id.FindPassword_Button_send);
        final Button okCodeButton = findViewById(R.id.FindPassword_Button_okCode);
        final Button okButton = findViewById(R.id.FindPassword_Button_done);
        final ImageButton backButton = findViewById(R.id.FindPassword_ImageButton_back);

        final TextView okCodeTextView = findViewById(R.id.FindPassword_TextView_okCode);
        final TextView password1TextView = findViewById(R.id.FindPassword_TextView_password1);
        final TextView password2TextView = findViewById(R.id.FindPassword_TextView_password2);

        final LinearLayout okCodeLinearLayout = findViewById(R.id.FindPassword_LinearLayout_okCode);

        final TextView alert1TextView = findViewById(R.id.FindPassword_TextView_alert1);
        final TextView alert2TextView = findViewById(R.id.FindPassword_TextView_alert2);
        final TextView alert3TextView = findViewById(R.id.FindPassword_TextView_alert3);

        ConstraintLayout CoverView = findViewById(R.id.FindPasswordActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(FindPasswordActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* 초기화 */
        insert1 = insert2 = 0;
        step = 1;
        emailSendButton.setEnabled(false);
        okCodeButton.setEnabled(false);
        okButton.setEnabled(false);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        emailSendButton.setEnabled(true);
                        emailSendButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button));
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        emailSendButton.setEnabled(false);
                        emailSendButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                        alert1TextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (step == 2) {
                    if (okCodeEditText.getText().toString().length() == 6) {
                        okCodeButton.setEnabled(true);
                        okCodeButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button));
                    } else {
                        okCodeButton.setEnabled(false);
                        okCodeButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (step == 3) {
                    if (password1EditText.getText().toString().equals("")) {
                        insert1 = 0;
                    } else {
                        insert1 = 1;
                    }

                    if (insert1 + insert2 == 2 && password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                        if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                            okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }

                    if (password1EditText.getText().toString().length() >= 8) {
                        alert2TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert2TextView.setVisibility(View.VISIBLE);
                    }
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                        alert3TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert3TextView.setVisibility(View.VISIBLE);
                    }
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
                if (step == 3) {
                    if (password2EditText.getText().toString().equals("")) {
                        insert2 = 0;
                    } else {
                        insert2 = 1;
                    }

                    if (insert1 + insert2 == 2 && password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                        if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                            okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }

                    if (password1EditText.getText().toString().length() >= 8) {
                        alert2TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert2TextView.setVisibility(View.VISIBLE);
                    }
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                        alert3TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert3TextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send email
                UserEmailString = emailEditText.getText().toString();
                step = 2;
                alert1TextView.setVisibility(View.INVISIBLE);
                emailSendButton.setEnabled(false);
                emailEditText.setEnabled(false);
                emailSendButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                emailEditText.setTextColor(Color.parseColor("#818181"));
                okCodeTextView.setVisibility(View.VISIBLE);
                okCodeButton.setVisibility(View.VISIBLE);
                okCodeEditText.setVisibility(View.VISIBLE);
                okCodeLinearLayout.setVisibility(View.VISIBLE);
                okCodeButton.setEnabled(false);
                new Thread() {
                    public void run() {
                        HttpPostData_SEND();
                    }
                }.start();
                Toast.makeText(getApplicationContext(), "메일이 전송되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        okCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AnswerOkCodeString.equals(okCodeEditText.getText().toString())) {
                    step = 3;
                    alert2TextView.setVisibility(View.INVISIBLE);
                    okCodeButton.setEnabled(false);
                    okCodeEditText.setEnabled(false);
                    okCodeEditText.setTextColor(Color.parseColor("#818181"));
                    okCodeButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                    password1EditText.setVisibility(View.VISIBLE);
                    password1TextView.setVisibility(View.VISIBLE);
                    password2EditText.setVisibility(View.VISIBLE);
                    password2TextView.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    okButton.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "확인 코드가 틀립니다.", Toast.LENGTH_LONG).show();
                }

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                    /* AES 256 암호화 */
                    try {
                        UserEmailString = AES256Chiper.AES_Encode(emailEditText.getText().toString());
                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                        e.printStackTrace();
                    }
                    try {
                        PasswordString = AES256Chiper.AES_Encode(password1EditText.getText().toString());
                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                        e.printStackTrace();
                    }
                    okButton.setEnabled(false);
                    okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                    okButton.setText("저장 중...");
                    password1EditText.setTextColor(Color.parseColor("#818181"));
                    password2EditText.setTextColor(Color.parseColor("#818181"));
                    new Thread() {
                        public void run() {
                            HttpPostData();
                        }
                    }.start();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    /* 메일 전송 */
    public void HttpPostData_SEND() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/sendCode/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString + "&" + "emailType" + "=" + "FIND";
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
            Log.i("OK CODE", myResult);
            AnswerOkCodeString = myResult;
        } catch (Exception e) {
            e.printStackTrace();
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(FindPasswordActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpUploadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_SEND();
                }
            }.start();
        }
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
                    Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                case "0": {
                    // 실패
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(FindPasswordActivity.this, NoInternetActivity.class);
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