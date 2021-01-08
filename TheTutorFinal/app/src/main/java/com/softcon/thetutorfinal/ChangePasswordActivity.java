package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class ChangePasswordActivity extends BaseActivity {

    private int insert1, insert2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        /* 인스턴스화 */
        final EditText password1EditText = (EditText) findViewById(R.id.ChangePassword_EditText_new1);
        final EditText password2EditText = (EditText) findViewById(R.id.ChangePassword_EditText_new2);
        final Button okButton = (Button) findViewById(R.id.ChangePassword_Button_done);
        final ImageButton backButton = (ImageButton) findViewById(R.id.ChangePassword_ImageButton_back);

        final TextView alert1TextView = (TextView) findViewById(R.id.ChangePassword_TextView_alert1);
        final TextView alert2TextView = (TextView) findViewById(R.id.ChangePassword_TextView_alert2);

        /* 초기화 */
        insert1 = insert2 = 0;
        okButton.setEnabled(false);

        password1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password1EditText.getText().toString().equals("")) {
                    insert1 = 0;
                } else {
                    insert1 = 1;
                }
                if (insert1 + insert2 == 2) {
                    if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (password1EditText.getText().toString().length() >= 8) {
                    alert1TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert1TextView.setVisibility(View.VISIBLE);
                }
                if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                    alert2TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert2TextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password2EditText.getText().toString().equals("")) {
                    insert2 = 0;
                } else {
                    insert2 = 1;
                }
                if (insert1 + insert2 == 2) {
                    if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(ChangePasswordActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (password1EditText.getText().toString().length() >= 8) {
                    alert1TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert1TextView.setVisibility(View.VISIBLE);
                }
                if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                    alert2TextView.setVisibility(View.INVISIBLE);
                } else {
                    alert2TextView.setVisibility(View.VISIBLE);
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