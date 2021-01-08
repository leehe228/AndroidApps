package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class FindPasswordActivity extends BaseActivity {

    private int insert1, insert2;

    private int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        /* 인스턴스화 */
        final EditText emailEditText = (EditText) findViewById(R.id.FindPassword_EditText_email);
        final EditText okCodeEditText = (EditText) findViewById(R.id.FindPassword_EditText_okCode);
        final EditText password1EditText = (EditText) findViewById(R.id.FindPassword_EditText_password1);
        final EditText password2EditText = (EditText) findViewById(R.id.FindPassword_EditText_password2);

        final Button emailSendButton = (Button) findViewById(R.id.FindPassword_Button_send);
        final Button okCodeButton = (Button) findViewById(R.id.FindPassword_Button_okCode);
        final Button okButton = (Button) findViewById(R.id.FindPassword_Button_done);
        final ImageButton backButton = (ImageButton) findViewById(R.id.FindPassword_ImageButton_back);

        final TextView okCodeTextView = (TextView) findViewById(R.id.FindPassword_TextView_okCode);
        final TextView password1TextView = (TextView) findViewById(R.id.FindPassword_TextView_password1);
        final TextView password2TextView = (TextView) findViewById(R.id.FindPassword_TextView_password2);

        final LinearLayout okCodeLinearLayout = (LinearLayout) findViewById(R.id.FindPassword_LinearLayout_okCode);

        final TextView alert1TextView = (TextView) findViewById(R.id.FindPassword_TextView_alert1);
        final TextView alert2TextView = (TextView) findViewById(R.id.FindPassword_TextView_alert2);
        final TextView alert3TextView = (TextView) findViewById(R.id.FindPassword_TextView_alert3);


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
                if(step == 1){
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()){
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

        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        okCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(step == 2){
                    if(okCodeEditText.getText().toString().length() == 6){
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

        okCodeEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        password1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(step == 3){
                    if (password1EditText.getText().toString().equals("")) {
                        insert1 = 0;
                    } else {
                        insert1 = 1;
                    }

                    if(insert1 + insert2 == 2 && password1EditText.getText().toString().equals(password2EditText.getText().toString())){
                        if(password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8){
                            okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else{
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

        password1EditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        password2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(step == 3){
                    if (password2EditText.getText().toString().equals("")) {
                        insert2 = 0;
                    } else {
                        insert2 = 1;
                    }

                    if(insert1 + insert2 == 2 && password1EditText.getText().toString().equals(password2EditText.getText().toString())){
                        if(password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8){
                            okButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else{
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

        password2EditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send email
                step = 2;
                alert1TextView.setVisibility(View.INVISIBLE);
                emailSendButton.setEnabled(false);
                emailEditText.setEnabled(false);
                emailSendButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                okCodeTextView.setVisibility(View.VISIBLE);
                okCodeButton.setVisibility(View.VISIBLE);
                okCodeEditText.setVisibility(View.VISIBLE);
                okCodeLinearLayout.setVisibility(View.VISIBLE);
                okCodeButton.setEnabled(false);
                Toast.makeText(getApplicationContext(), "메일이 전송되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        okCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = 3;
                alert2TextView.setVisibility(View.INVISIBLE);
                okCodeButton.setEnabled(false);
                okCodeEditText.setEnabled(false);
                okCodeButton.setBackground(ContextCompat.getDrawable(FindPasswordActivity.this, R.drawable.basic_button_unclick));
                password1EditText.setVisibility(View.VISIBLE);
                password1TextView.setVisibility(View.VISIBLE);
                password2EditText.setVisibility(View.VISIBLE);
                password2TextView.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
                okButton.setEnabled(false);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "같은 비밀번호를 두 번 입력해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}