package com.softcon.thetutorcpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class SetLockActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String lockState;

    // PIN
    private int[] pin = new int[4];

    // PIN STATE
    private int pinState;

    private int PIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);

        /* 초기화 */
        PIN = 0;

        /* 인스턴스화 */
        ImageButton backButton = findViewById(R.id.SetLock_ImageButton_back);

        final View num1View = findViewById(R.id.SetLock_View_1);
        final View num2View = findViewById(R.id.SetLock_View_2);
        final View num3View = findViewById(R.id.SetLock_View_3);
        final View num4View = findViewById(R.id.SetLock_View_4);

        final Button button1 = findViewById(R.id.SetLock_Button_1);
        final Button button2 = findViewById(R.id.SetLock_Button_2);
        final Button button3 = findViewById(R.id.SetLock_Button_3);
        final Button button4 = findViewById(R.id.SetLock_Button_4);
        final Button button5 = findViewById(R.id.SetLock_Button_5);
        final Button button6 = findViewById(R.id.SetLock_Button_6);
        final Button button7 = findViewById(R.id.SetLock_Button_7);
        final Button button8 = findViewById(R.id.SetLock_Button_8);
        final Button button9 = findViewById(R.id.SetLock_Button_9);
        final Button button0 = findViewById(R.id.SetLock_Button_0);
        final Button backSpaceButton = findViewById(R.id.SetLock_Button_back);
        final Button clearButton = findViewById(R.id.SetLock_Button_clear);

        final Button okButton = findViewById(R.id.SetLock_Button_done);
        final Button unlockButton = findViewById(R.id.SetLock_Button_delete);

        ConstraintLayout CoverView = findViewById(R.id.SetLockActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(SetLockActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* 초기화 */
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        lockState = pref.getString("autoLogin", "true");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SetLockActivity.this, "변경사항이 저장되지 않습니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetLockActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 4; i++) {
                    PIN = PIN * 10 + pin[i];
                }
                Log.i("PIN", Integer.toString(PIN));
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("autoLogin", "lock");
                editor.putInt("PIN", PIN);
                editor.apply();

                Intent intent = new Intent(SetLockActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("autoLogin", "true");
                editor.apply();

                Intent intent = new Intent(SetLockActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 0;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 1;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 2;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 3;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 4;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 5;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 6;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 7;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 8;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState < 4) {
                    pin[pinState++] = 9;
                    if (pinState == 1) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 2) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 3) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    } else if (pinState == 4) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_inserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        backSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinState != 0) {
                    pinState--;
                    if (pinState == 0) {
                        num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));
                    } else if (pinState == 1) {
                        num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));
                    } else if (pinState == 2) {
                        num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));
                    } else if (pinState == 3) {
                        num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));
                    }
                }

                if (pinState == 4) {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                } else {
                    okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinState = 0;
                num1View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));
                num2View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));
                num3View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));
                num4View.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.lock_notinserted));

                okButton.setEnabled(false);
                okButton.setBackground(ContextCompat.getDrawable(SetLockActivity.this, R.drawable.basic_button_unclick));
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(SetLockActivity.this, "변경사항이 저장되지 않습니다", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SetLockActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }


}