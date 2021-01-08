package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelloActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        /* 인스턴스화 */
        Button signupButton = (Button) findViewById(R.id.Hello_Button_signup);
        Button loginButton = (Button) findViewById(R.id.Hello_Button_login);

        /* 회원가입 버튼 */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelloActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /* 로그인 버튼 */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelloActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}