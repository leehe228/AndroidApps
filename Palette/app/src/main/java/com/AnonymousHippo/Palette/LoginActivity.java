package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginActivity extends BaseActivity {

    // 데이터 전송용 배열
    private final String[] keys = new String[2];
    private final String[] data = new String[2];
    String result;

    // 경고 TextView
    TextView alertTextView;
    TextView emailAlertTextView;

    // EditText 입력 감지 Flag
    Boolean insert1, insert2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 인스턴스화
        final Button okButton = findViewById(R.id.Login_Button_login);
        final Button findPasswordButton = findViewById(R.id.Login_Button_findPassword);
        final EditText emailEditText = findViewById(R.id.Login_EditText_email);
        final EditText passwordEditText = findViewById(R.id.Login_EditText_password);
        ImageButton backButton = findViewById(R.id.Login_ImageButton_back);
        alertTextView = findViewById(R.id.Login_TextView_bigAlert);
        emailAlertTextView = findViewById(R.id.Login_TextView_alert1);

        final CheckBox autoLoginCheckBox = findViewById(R.id.Login_CheckBox_autoLogin);

        // 초기화
        insert1 = insert2 = false;

        // 로그인 버튼
        okButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               final String email = emailEditText.getText().toString();

               // 암호화 인코딩
               String password;
               try {
                   password = AES256Chiper.AES_Encode(passwordEditText.getText().toString());
               } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
                       | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                   e.printStackTrace();
                   password = passwordEditText.getText().toString();
               }

               keys[0] = "email"; keys[1] = "password";
               data[0] = email; data[1] = password;

               new Thread() {
                   public void run() {
                       result = HttpPostData.POST("account/login/", keys, data);

                       Log.i("result : ", result);

                       switch (result) {
                           case "1":{
                               SharedPreferences preferences = getSharedPreferences("com.AnonymousHippo.Palette.sharePreference", MODE_PRIVATE);
                               SharedPreferences.Editor editor = preferences.edit();
                               editor.putString("userEmail", email);
                               editor.putBoolean("autoLogin", autoLoginCheckBox.isChecked());
                               editor.apply();

                               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                               startActivity(intent);
                               overridePendingTransition(0, 0);
                               finish();
                               break;
                           }

                           case "-1":
                           case "SEND_FAIL":
                           case "NO_DATA_RECEIVED": {
                               Message msg = mHandler.obtainMessage();
                               mHandler.sendMessage(msg);
                               break;
                           }
                       }
                   }
               }.start();
           }
        });

        // 비밀번호 찾기 버튼
        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // 이메일 EditText
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insert1 = android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches();

                if(insert1){
                    emailAlertTextView.setVisibility(View.INVISIBLE);
                }
                else{
                    emailAlertTextView.setVisibility(View.VISIBLE);
                }

                if(insert1 && insert2){
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button));
                    okButton.setClickable(true);
                }
                else{
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button_unclick));
                    okButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 비밀번호 EditText
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insert2 = !passwordEditText.getText().toString().equals("");

                if(insert1 && insert2){
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button));
                    okButton.setClickable(true);
                }
                else{
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button_unclick));
                    okButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 뒤로가기 버튼
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            alertTextView.setVisibility(View.VISIBLE);
        }
    };

    // 뒤로가기 버튼
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}