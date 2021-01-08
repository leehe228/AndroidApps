package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class LoginActivity extends BaseActivity {

    private int insert1, insert2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* 인스턴스화 */
        final EditText emailEditText = (EditText)findViewById(R.id.Login_EditText_email);
        final EditText passwordEditText = (EditText)findViewById(R.id.Login_EditText_password);
        Button findPasswordButton = (Button)findViewById(R.id.Login_Button_findPassword);
        final Button loginButton = (Button)findViewById(R.id.Login_Button_done);
        ImageButton backButton = (ImageButton)findViewById(R.id.Login_ImageButton_back);
        final TextView alertTextView = (TextView) findViewById(R.id.Login_TextView_alert1);

        /* 초기화 */
        insert1 = insert2 = 0;
        loginButton.setEnabled(false);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailEditText.getText().toString().equals("")){
                    insert1 = 0;
                }
                else{
                    insert1 = 1;
                }
                if(insert1 + insert2 == 2){
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()){
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button));
                        loginButton.setEnabled(true);
                    } else {
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                        loginButton.setEnabled(false);
                    }
                }
                else{
                    loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                    loginButton.setEnabled(false);
                }

                if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()){
                    alertTextView.setVisibility(View.INVISIBLE);
                } else {
                    alertTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordEditText.getText().toString().equals("")){
                    insert2 = 0;
                }
                else{
                    insert2 = 1;
                }
                if(insert1 + insert2 == 2){
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()){
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button));
                        loginButton.setEnabled(true);
                    } else {
                        loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                        loginButton.setEnabled(false);
                    }
                }
                else{
                    loginButton.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.basic_button_unclick));
                    loginButton.setEnabled(false);
                }

                if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()){
                    alertTextView.setVisibility(View.INVISIBLE);
                } else {
                    alertTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HelloActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, HelloActivity.class);
        startActivity(intent);
        finish();
    }
}