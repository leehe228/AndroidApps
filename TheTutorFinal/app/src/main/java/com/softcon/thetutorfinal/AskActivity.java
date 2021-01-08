package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.graphics.Color;
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

public class AskActivity extends BaseActivity {

    private int insert1, insert2, insert3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        /* 인스턴스화 */
        final EditText emailEditText = (EditText) findViewById(R.id.Ask_EditText_email);
        final EditText titleEditText = (EditText) findViewById(R.id.Ask_EditText_title);
        final EditText contentEditText = (EditText) findViewById(R.id.Ask_EditText_content);

        final Button okButton = (Button) findViewById(R.id.Ask_Button_done);
        ImageButton backButton = (ImageButton) findViewById(R.id.Ask_ImageButton_back);

        final TextView numTextView = (TextView) findViewById(R.id.Ask_TextView_textNum);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailEditText.getText().toString().equals("")) {
                    insert1 = 0;
                } else {
                    insert1 = 1;
                }
                if (insert1 + insert2 + insert3 == 3) {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
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

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleEditText.getText().toString().equals("")) {
                    insert2 = 0;
                } else {
                    insert2 = 1;
                }
                if (insert1 + insert2 + insert3 == 3) {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        titleEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* 글자수 체크 */
                String inputText = "글자수 " + contentEditText.getText().toString().length() + "/200자";
                numTextView.setText(inputText);

                if (contentEditText.getText().toString().length() > 200) {
                    numTextView.setTextColor(Color.parseColor("#E50000"));
                } else {
                    numTextView.setTextColor(Color.parseColor("#000000"));
                }

                if (contentEditText.getText().toString().equals("") || contentEditText.getText().toString().length() > 200) {
                    insert3 = 0;
                } else {
                    insert3 = 1;
                }
                if (insert1 + insert2 + insert3 == 3) {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(AskActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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