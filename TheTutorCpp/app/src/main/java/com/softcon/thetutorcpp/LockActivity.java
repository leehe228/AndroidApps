package com.softcon.thetutorcpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class LockActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    // PIN
    private int[] pin = new int[4];

    // PIN STATE
    private int pinState;

    private int PIN, answerPIN;
    private int wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        /* 초기화 */
        pinState = 0;
        PIN = 0;
        wrong = 0;
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        answerPIN = pref.getInt("PIN", 0);

        /* 인스턴스화 */
        final View num1View = findViewById(R.id.Lock_View_1);
        final View num2View = findViewById(R.id.Lock_View_2);
        final View num3View = findViewById(R.id.Lock_View_3);
        final View num4View = findViewById(R.id.Lock_View_4);

        final Button button1 = findViewById(R.id.Lock_Button_1);
        final Button button2 = findViewById(R.id.Lock_Button_2);
        final Button button3 = findViewById(R.id.Lock_Button_3);
        final Button button4 = findViewById(R.id.Lock_Button_4);
        final Button button5 = findViewById(R.id.Lock_Button_5);
        final Button button6 = findViewById(R.id.Lock_Button_6);
        final Button button7 = findViewById(R.id.Lock_Button_7);
        final Button button8 = findViewById(R.id.Lock_Button_8);
        final Button button9 = findViewById(R.id.Lock_Button_9);
        final Button button0 = findViewById(R.id.Lock_Button_0);
        final Button backSpaceButton = findViewById(R.id.Lock_Button_back);
        final Button clearButton = findViewById(R.id.Lock_Button_clear);
        final Button loginButton = findViewById(R.id.Lock_Button_changePIN);

        final TextView alertTextView = findViewById(R.id.Lock_TextView_alert);

        ConstraintLayout CoverView = findViewById(R.id.LockActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(LockActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* PIN 5회 틀릴 때 */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("autoLogin", "false");
                editor.putString("user", "");
                editor.apply();

                Intent intent = new Intent(LockActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 0;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 1;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 2;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 3;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 4;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 5;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 6;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 7;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 8;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        PIN = 0;
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin[pinState++] = 9;
                if (pinState == 1) {
                    num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 2) {
                    num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 3) {
                    num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                } else if (pinState == 4) {
                    num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_inserted));
                    for (int i = 0; i < 4; i++) {
                        PIN = PIN * 10 + pin[i];
                    }

                    if (PIN == answerPIN) {
                        Intent intent = new Intent(LockActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        pinState = 0;
                        PIN = 0;

                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                        PIN = 0;
                        wrong++;
                        alertTextView.setVisibility(View.VISIBLE);
                        if (wrong == 3) {
                            loginButton.setEnabled(true);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        backSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState != 0) {
                    pinState--;
                    if (pinState == 0) {
                        num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                    } else if (pinState == 1) {
                        num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                    } else if (pinState == 2) {
                        num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                    } else if (pinState == 3) {
                        num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinState = 0;
                num1View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                num2View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                num3View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));
                num4View.setBackground(ContextCompat.getDrawable(LockActivity.this, R.drawable.lock_notinserted));

                wrong++;
                alertTextView.setVisibility(View.VISIBLE);
                if (wrong == 3) {
                    loginButton.setEnabled(true);
                    loginButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}