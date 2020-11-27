package com.softcon.thetutorcpp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelloActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        /* 인스턴스화 */
        Button signUpButton = findViewById(R.id.Hello_Button_signup);
        Button loginButton = findViewById(R.id.Hello_Button_login);
        TextView titleTextView = findViewById(R.id.Hello_TextView_TOP);
        TextView subTitle1TextView = findViewById(R.id.Hello_TextView_text1);
        TextView subTitle2TextView = findViewById(R.id.Hello_TextView_text2);
        ImageView imageView = findViewById(R.id.Hello_ImageView_main);

        Animation TextAnimation = AnimationUtils.loadAnimation(HelloActivity.this, R.anim.text_up);
        titleTextView.startAnimation(TextAnimation);
        subTitle1TextView.startAnimation(TextAnimation);
        subTitle2TextView.startAnimation(TextAnimation);
        imageView.startAnimation(TextAnimation);

        /* 회원가입 버튼 */
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelloActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        /* 로그인 버튼 */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelloActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}