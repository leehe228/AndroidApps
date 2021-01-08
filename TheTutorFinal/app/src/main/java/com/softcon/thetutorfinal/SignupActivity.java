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

public class SignupActivity extends BaseActivity {

    private int insert1, insert3, insert4, insert5;
    private int step;
    private String userEmail, password, name, okCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* 초기화 */
        step = 1;
        insert1 = insert3 = insert4 = insert5 = 0;

        /* 인스턴스화 */
        final EditText emailEditText = (EditText) findViewById(R.id.Signup_EditText_email);
        final EditText password1EditText = (EditText) findViewById(R.id.Signup_EditText_password1);
        final EditText password2EditText = (EditText) findViewById(R.id.Signup_EditText_password2);
        final EditText nameEditText = (EditText) findViewById(R.id.Signup_EditText_name);
        final Button okButton = (Button) findViewById(R.id.Signup_Button_done);
        final TextView emailTextView = (TextView) findViewById(R.id.Signup_TextView_email);
        final TextView password1TextView = (TextView) findViewById(R.id.Signup_TextView_password1);
        final TextView password2TextView = (TextView) findViewById(R.id.Signup_TextView_password2);
        final TextView nameTextView = (TextView) findViewById(R.id.Signup_TextView_name);
        Button privacyButton = (Button) findViewById(R.id.Signup_Button_privacy);
        ImageButton backButton = (ImageButton) findViewById(R.id.Signup_ImageButton_back);

        // 경고 문구
        final TextView alert1TextView = (TextView) findViewById(R.id.Signup_TextView_alert1);
        final TextView alert2TextView = (TextView) findViewById(R.id.Signup_TextView_alert2);
        final TextView alert3TextView = (TextView) findViewById(R.id.Signup_TextView_alert3);

        /* 초기화 */
        okButton.setEnabled(false);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (step == 1) {
                    if (emailEditText.getText().toString().equals("")) {
                        insert1 = 0;
                    } else {
                        insert1 = 1;
                    }
                    if (insert1 + insert3 + insert4 + insert5 == 4) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                            if(password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8){
                                okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button));
                                okButton.setEnabled(true);
                            } else {
                                okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                                okButton.setEnabled(false);
                            }
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else if (step == 2) {
                    if (emailEditText.getText().toString().equals("")) {
                        insert1 = 0;
                    } else if (emailEditText.getText().toString().length() >= 6) {
                        insert1 = 1;
                    } else {
                        insert1 = 0;
                    }
                    if (insert1 == 1) {
                        okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button));
                        okButton.setEnabled(true);
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                }

                if (step == 1) {
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
            }
        });

        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        password1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password1EditText.getText().toString().equals("")) {
                    insert3 = 0;
                } else {
                    insert3 = 1;
                }
                if (insert1 + insert3 + insert4 + insert5 == 4) {
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if(password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8){
                            okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
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
                if (password2EditText.getText().toString().equals("")) {
                    insert4 = 0;
                } else {
                    insert4 = 1;
                }
                if (insert1 + insert3 + insert4 + insert5 == 4) {
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if(password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8){
                            okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
            }
        });

        password2EditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nameEditText.getText().toString().equals("")) {
                    insert5 = 0;
                } else {
                    insert5 = 1;
                }
                if (insert1 + insert3 + insert4 + insert5 == 4) {
                    if (password1EditText.getText().toString().equals(password2EditText.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if(password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8){
                            okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button));
                            okButton.setEnabled(true);
                        } else {
                            okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                            okButton.setEnabled(false);
                        }
                    } else {
                        okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SignupActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }

                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        alert1TextView.setVisibility(View.INVISIBLE);
                    } else {
                        alert1TextView.setVisibility(View.VISIBLE);
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
                //
            }
        });

        nameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_ENTER;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (step == 1) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                        if (password1EditText.getText().toString().equals(password2EditText.getText().toString())) {
                            if (password1EditText.getText().toString().length() >= 8 && password2EditText.getText().toString().length() >= 8) {
                                step = 2;
                                userEmail = emailEditText.getText().toString();
                                password = password1EditText.getText().toString();
                                name = nameEditText.getText().toString();

                                emailEditText.setText("");
                                emailEditText.setInputType(2);
                                password1EditText.setText("");
                                password2EditText.setText("");
                                nameEditText.setText("");
                                emailTextView.setText("확인 코드");
                                okButton.setText("확인");
                                emailEditText.setText("");
                                password1TextView.setVisibility(View.INVISIBLE);
                                password1EditText.setVisibility(View.INVISIBLE);
                                password2TextView.setVisibility(View.INVISIBLE);
                                password2EditText.setVisibility(View.INVISIBLE);
                                nameTextView.setVisibility(View.INVISIBLE);
                                nameEditText.setVisibility(View.INVISIBLE);
                                okButton.setEnabled(false);
                            }
                        }
                    }
                } else if (step == 2) {
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, HelloActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignupActivity.this, HelloActivity.class);
        startActivity(intent);
        finish();
    }

}