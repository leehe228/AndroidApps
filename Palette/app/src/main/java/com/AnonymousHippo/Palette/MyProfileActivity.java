package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class MyProfileActivity extends BaseActivity {

    // 데이터 수신용 배열
    private final String[] keys = new String[1];
    private final String[] data = new String[1];
    String result;
    private String userEmail;
    private String userName;
    private String userAge;
    private String userInterest;

    String[] titles = {"사진전", "가구전시", "유아", "미디어 아트", "학생 작품", "제품 디자인", "캐릭터", "패션", "레고 전시"};

    /* Loading */
    ImageView loadingView;

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView ageTextView;
    private TextView interestTextView;
    private TextView billingTextView;

    private Button editButton;

    private TextView alertTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        /* 로딩 GIF */
        loadingView = findViewById(R.id.LoadingView);
        final GlideDrawableImageViewTarget loadingImage = new GlideDrawableImageViewTarget(loadingView);
        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        // 인스턴스화
        ImageButton backButton = findViewById(R.id.MyProfile_ImageButton_back);
        editButton = findViewById(R.id.MyProfile_Button_edit);

        nameTextView = findViewById(R.id.MyProfile_TextView_userName);
        emailTextView = findViewById(R.id.MyProfile_TextView_userEmail);
        ageTextView = findViewById(R.id.MyProfile_TextView_userAge);
        interestTextView = findViewById(R.id.MyProfile_TextView_userInterest);
        billingTextView = findViewById(R.id.MyProfile_TextView_userBilling);

        alertTextView = findViewById(R.id.MyProfile_TextView_Alert);

        // 초기화
        SharedPreferences preferences = getSharedPreferences("com.AnonymousHippo.Palette.sharePreference", MODE_PRIVATE);
        userEmail = preferences.getString("userEmail", "");

        keys[0] = "email";
        data[0] = userEmail;

        // 데이터 수신
        new Thread() {
            public void run() {
                result = HttpPostData.POST("account/getInfo/", keys, data);

                //loadingView.setVisibility(View.INVISIBLE);

                if(result.equals("") || result.equals("-1") || result.equals("SEND_FAIL")){
                    Message msg = failHandler.obtainMessage();
                    failHandler.sendMessage(msg);
                } else{
                    Log.i("result", result);
                    userName = result.split("&")[0];
                    userAge = result.split("&")[1];
                    char[] ch = result.split("&")[2].toCharArray();
                    userInterest = "";

                    int count = 0;

                    for (int i = 0; i < ch.length; i++) {
                        if (ch[i] == '1' && count == 0) {
                            userInterest = userInterest.concat(titles[i]);
                            count++;
                        }
                        else if(ch[i] == '1' && count > 0){
                            userInterest = userInterest.concat(", " + titles[i]);
                            count++;
                        }
                    }

                    Message msg = updateHandler.obtainMessage();
                    updateHandler.sendMessage(msg);
                }
            }
        }.start();

        // 수정 버튼
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditMyProfileActivity.class);
                intent.putExtra("name", nameTextView.getText().toString());
                intent.putExtra("age", ageTextView.getText().toString());
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        // 뒤로가기 버튼
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    // TextView 정보 표시 Handler
    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            // TODO
            emailTextView.setText(userEmail);
            nameTextView.setText(userName);
            ageTextView.setText(userAge);
            interestTextView.setText(userInterest);

            loadingView.setVisibility(View.INVISIBLE);

            editButton.setEnabled(true);
            editButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button));
        }
    };

    @SuppressLint("HandlerLeak")
    Handler failHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            // TODO
            alertTextView.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.INVISIBLE);
            nameTextView.setVisibility(View.INVISIBLE);
            ageTextView.setVisibility(View.INVISIBLE);
            interestTextView.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}