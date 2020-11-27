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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class EditMyProfileActivity extends BaseActivity{

    private boolean insert1, insert2;

    // 데이터 전송용 배열
    private final String[] keys = new String[3];
    private final String[] data = new String[3];
    String result;

    private EditText ageEditText;
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        // 인스턴스화
        Button cancelButton = findViewById(R.id.EditProfile_Button_cancel);
        final Button okButton = findViewById(R.id.EditProfile_Button_done);

        nameEditText = findViewById(R.id.EditProfile_EditText_name);
        ageEditText = findViewById(R.id.EditProfile_EditText_age);

        // 초기화
        insert1 = insert2 = false;
        SharedPreferences preferences = getSharedPreferences("com.AnonymousHippo.Palette.sharePreference", MODE_PRIVATE);
        String userEmail = preferences.getString("userEmail", "");

        Intent intent = getIntent();
        nameEditText.setText(intent.getStringExtra("name"));
        ageEditText.setText(intent.getStringExtra("age"));

        if(nameEditText.getText().toString().length() != 0){
            insert1 = true;
        }
        if(ageEditText.getText().toString().length() != 0){
            insert2 = true;
        }

        keys[0] = "email";
        data[0] = userEmail;

        // 저장 버튼
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keys[1] = "name"; keys[2] = "age";
                data[1] = nameEditText.getText().toString(); data[2] = ageEditText.getText().toString();

                new Thread() {
                    public void run() {
                        result = HttpPostData.POST("account/changeInfo/", keys, data);

                        Log.i("result : ", result);

                        switch (result) {
                            case "1":{
                                Message msg = dialogHandler.obtainMessage();
                                dialogHandler.sendMessage(msg);
                                break;
                            }

                            case "-1":
                            case "SEND_FAIL":
                            case "NO_DATA_RECEIVED": {

                            }
                            }
                    }
                }.start();
            }
        });

        // 이름
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insert1 = !nameEditText.getText().toString().equals("");

                if(insert1 && insert2){
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button));
                    okButton.setEnabled(true);
                }
                else{
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 나이
        ageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insert2 = !ageEditText.getText().toString().equals("");

                if(insert1 && insert2){
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button));
                    okButton.setEnabled(true);
                }
                else{
                    okButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 취소 버튼
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    /* 프로필 사진 변경 */
    public void onProfileImageClicked(View view) {
        // TODO 갤러리 열어서 사진 가져와 저장, 이후 DB에 저장
    }

    @SuppressLint("HandlerLeak")
    Handler dialogHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditMyProfileActivity.this);
            builder.setTitle("저장했습니다");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };
}