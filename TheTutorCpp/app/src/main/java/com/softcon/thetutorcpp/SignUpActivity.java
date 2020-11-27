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

public class SignUpActivity extends BaseActivity {

    private int insert1, insert3, insert4, insert5;
    private int step;
    private String UserEmailString, PasswordString, NameString, OkCodeString, UserEmailStringtoSave;
    private String AnswerOkCodeString;

    private Button okButton;
    private TextView emailTextView;
    private TextView password1TextView;
    private TextView password2TextView;
    private TextView nameTextView;
    private EditText emailEditText;
    private EditText password1EditText;
    private EditText password2EditText;
    private EditText nameEditText;
    private TextView bigAlertTextView;

    private int HttpUploadErrorCount;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /* 초기화 */
        step = 1;
        insert1 = insert3 = insert4 = insert5 = 0;
        HttpUploadErrorCount = 0;

        /* 인스턴스화 */
        emailEditText = findViewById(R.id.Signup_EditText_email);
        password1EditText = findViewById(R.id.Signup_EditText_password1);
        password2EditText = findViewById(R.id.Signup_EditText_password2);
        nameEditText = findViewById(R.id.Signup_EditText_name);
        okButton = findViewById(R.id.Signup_Button_done);
        emailTextView = findViewById(R.id.Signup_TextView_email);
        password1TextView = findViewById(R.id.Signup_TextView_password1);
        password2TextView = findViewById(R.id.Signup_TextView_password2);
        nameTextView = findViewById(R.id.Signup_TextView_name);
        Button privacyButton = findViewById(R.id.Signup_Button_privacy);
        ImageButton backButton = findViewById(R.id.Signup_ImageButton_back);
        bigAlertTextView = findViewById(R.id.Signup_TextView_bigAlert);

        ConstraintLayout CoverView = findViewById(R.id.SignUpActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        // 경고 문구
        final TextView alert1TextView = findViewById(R.id.Signup_TextView_alert1);
        final TextView alert2TextView = findViewById(R.id.Signup_TextView_alert2);
        final TextView alert3TextView = findViewById(R.id.Signup_TextView_alert3);

        /* 초기화 */
        okButton.setEnabled(false);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (step == 1) {
                    if (emailEditText.getText().toString().equals("")) {
                        insert1 = 0;
                    } else {
                        insert1 = 1;
                    }
                    if (insert1 + insert3 + insert4 + insert5 == 4) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                            if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                                okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button));
                                okButton.setEnabled(true);
                            } else {
                                okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                                okButton.setEnabled(false);
                            }
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else if (step == 2) {
                    if (emailEditText.getText().toString().equals("")) {
                        insert1 = 0;
                    } else if (emailEditText.getText().toString().length() >= 6) {
                        insert1 = 1;
                    } else {
                        insert1 = 0;
                    }
                    if (insert1 == 1) {
                        okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button));
                        okButton.setEnabled(true);
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                }

                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
            }
        });

        password1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password1EditText.getText().toString().equals("")) {
                    insert3 = 0;
                } else {
                    insert3 = 1;
                }
                if (insert1 + insert3 + insert4 + insert5 == 4) {
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
            }
        });

        password2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password2EditText.getText().toString().equals("")) {
                    insert4 = 0;
                } else {
                    insert4 = 1;
                }
                if (insert1 + insert3 + insert4 + insert5 == 4) {
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nameEditText.getText().toString().equals("")) {
                    insert5 = 0;
                } else {
                    insert5 = 1;
                }
                if (insert1 + insert3 + insert4 + insert5 == 4) {
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                            if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                                step = 2;
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
                                NameString = nameEditText.getText().toString();
                                UserEmailStringtoSave = emailEditText.getText().toString();

                                okButton.setText("메일 발송 중...");
                                emailEditText.setTextColor(Color.parseColor("#818181"));
                                password1EditText.setTextColor(Color.parseColor("#818181"));
                                password2EditText.setTextColor(Color.parseColor("#818181"));
                                nameEditText.setTextColor(Color.parseColor("#818181"));
                                emailEditText.setEnabled(false);
                                password1EditText.setEnabled(false);
                                password2EditText.setEnabled(false);
                                nameEditText.setEnabled(false);
                                okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                                okButton.setEnabled(false);

                                new Thread() {
                                    public void run() {
                                        HttpPostData_SEND();
                                    }
                                }.start();
                            }
                        }
                    }
                } else if (step == 2) {
                    OkCodeString = emailEditText.getText().toString();
                    if (AnswerOkCodeString.equals(OkCodeString) && !AnswerOkCodeString.equals("")) {
                        okButton.setEnabled(false);
                        okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
                        okButton.setText("회원 가입 중...");
                        emailEditText.setTextColor(Color.parseColor("#818181"));
                        new Thread() {
                            public void run() {
                                HttpPostData();
                            }
                        }.start();
                    } else {
                        Toast.makeText(getApplicationContext(), "확인 코드가 틀립니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, HelloActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, HelloActivity.class);
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
            String buffer = "userid" + "=" + UserEmailStringtoSave + "&" + "emailType" + "=" + "REGS";
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
            if (myResult.equals("0")) {
                Message msg = mHandler.obtainMessage();
                mHandler.sendMessage(msg);
            } else {
                HttpUploadErrorCount = 0;
                Message msg = nextHandler.obtainMessage();
                nextHandler.sendMessage(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(SignUpActivity.this, NoInternetActivity.class);
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

    /* 가입 */
    public void HttpPostData() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/signup/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString + "&" + "password" + "=" + PasswordString + "&" + "name" + "=" + NameString;
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
            Log.i("SIGN UP", myResult);
            switch (myResult) {
                case "1": {
                    // 회원가입 성공
                    //Shared Preference
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user", UserEmailStringtoSave);
                    editor.putString("autoLogin", "getSub");
                    editor.putString("push", "true");
                    editor.putString("theme", "dark");
                    editor.apply();

                    Intent intent = new Intent(SignUpActivity.this, GetSubjectActivity.class);
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
                Intent intent = new Intent(SignUpActivity.this, NoInternetActivity.class);
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
            step = 1;
            okButton.setEnabled(false);
            insert1 = insert3 = insert4 = insert5 = 0;
            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button_unclick));
            okButton.setText("확인 코드 전송");
            emailTextView.setText("이메일");
            emailEditText.setText("");
            emailEditText.setInputType(1);
            password1EditText.setText("");
            password2EditText.setText("");
            nameEditText.setText("");
            emailEditText.setText("");
            emailTextView.setEnabled(true);
            password1EditText.setEnabled(true);
            password2EditText.setEnabled(true);
            nameEditText.setEnabled(true);
            password1TextView.setVisibility(View.VISIBLE);
            password1EditText.setVisibility(View.VISIBLE);
            password2TextView.setVisibility(View.VISIBLE);
            password2EditText.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.VISIBLE);

            emailEditText.setTextColor(Color.parseColor("#000000"));
            password1EditText.setTextColor(Color.parseColor("#000000"));
            password2EditText.setTextColor(Color.parseColor("#000000"));
            nameEditText.setTextColor(Color.parseColor("#000000"));

            bigAlertTextView.setVisibility(View.VISIBLE);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler nextHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            // 메일 전송 완료 시
            okButton.setEnabled(true);
            okButton.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.basic_button));
            okButton.setText("확인");
            emailTextView.setText("확인 코드");
            emailEditText.setText("");
            emailEditText.setInputType(2);
            password1EditText.setText("");
            password2EditText.setText("");
            nameEditText.setText("");
            emailEditText.setText("");
            emailEditText.setEnabled(true);
            password1EditText.setEnabled(false);
            password2EditText.setEnabled(false);
            nameEditText.setEnabled(false);
            emailEditText.setTextColor(Color.parseColor("#000000"));
            password1TextView.setVisibility(View.INVISIBLE);
            password1EditText.setVisibility(View.INVISIBLE);
            password2TextView.setVisibility(View.INVISIBLE);
            password2EditText.setVisibility(View.INVISIBLE);
            nameTextView.setVisibility(View.INVISIBLE);
            nameEditText.setVisibility(View.INVISIBLE);
        }
    };
}