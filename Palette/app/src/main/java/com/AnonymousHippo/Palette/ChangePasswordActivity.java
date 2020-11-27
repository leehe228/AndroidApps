package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ChangePasswordActivity extends BaseActivity {

    // 데이터 전송용 배열
    private final String[] keys = new String[3];
    private final String[] data = new String[3];
    String result;

    // 채워짐 플래그
    boolean insert1, insert2, insert3;

    private TextView bigAlertTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //인스턴스화
        final EditText oldPasswordEditText = findViewById(R.id.ChangePassword_EditText_old);
        final EditText newPassword1EditText = findViewById(R.id.ChangePassword_EditText_new1);
        final EditText newPassword2EditText = findViewById(R.id.ChangePassword_EditText_new2);
        final Button okButton = findViewById(R.id.ChangePassword_Button_done);
        ImageButton backButton = findViewById(R.id.ChangePassword_ImageButton_back);

        final TextView alert1TextView = findViewById(R.id.ChangePassword_TextView_alert1);
        final TextView alert2TextView = findViewById(R.id.ChangePassword_TextView_alert2);
        bigAlertTextView = findViewById(R.id.ChangePassword_TextView_alert);

        // 초기화
        insert1 = insert2 = insert3 = false;

        // 변경 버튼
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("com.AnonymousHippo.Palette.sharePreference", MODE_PRIVATE);
                String email = preferences.getString("userEmail", "");
                String oldPassword;
                String newPassword;

                try {
                    oldPassword = AES256Chiper.AES_Encode(oldPasswordEditText.getText().toString());
                    newPassword = AES256Chiper.AES_Encode(newPassword1EditText.getText().toString());
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
                        | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                    oldPassword = oldPasswordEditText.getText().toString();
                    newPassword = newPassword1EditText.getText().toString();
                }

                keys[0] = "email"; keys[1] = "oldPassword"; keys[2] = "newPassword";
                data[0] = email; data[1] = oldPassword; data[2] = newPassword;

                new Thread() {
                    public void run() {
                        result = HttpPostData.POST("account/changePassword/", keys, data);

                        switch (result) {
                            case "1":{
                                Message msg = dialogHandler.obtainMessage();
                                dialogHandler.sendMessage(msg);
                                break;
                            }

                            case "-1":
                            case "SEND_FAIL":
                            case "NO_DATA_RECEIVED": {
                                Message msg = mHandler.obtainMessage();
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                }.start();
            }
        });

        // old Password EditText
        oldPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insert1 = !oldPasswordEditText.getText().toString().equals("");

                if(insert1 && insert2 && insert3){
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

        // new Password1 EditText
        newPassword1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insert2 = newPassword1EditText.getText().toString().length() >= 8;

                if(insert2){
                    alert1TextView.setVisibility(View.INVISIBLE);
                }
                else{
                    alert1TextView.setVisibility(View.VISIBLE);
                }

                if(insert1 && insert2 && insert3){
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

        // new Password2 EditText
        newPassword2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insert3 = newPassword2EditText.getText().toString().equals(newPassword1EditText.getText().toString());

                if(insert3){
                    alert2TextView.setVisibility(View.INVISIBLE);
                }
                else{
                    alert2TextView.setVisibility(View.VISIBLE);
                }

                if(insert1 && insert2 && insert3){
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
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            bigAlertTextView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onBackPressed(){
        finish();
    }

    @SuppressLint("HandlerLeak")
    Handler dialogHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
            builder.setTitle("변경했습니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };
}