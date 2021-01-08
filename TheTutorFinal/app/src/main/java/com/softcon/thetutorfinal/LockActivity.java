package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LockActivity extends AppCompatActivity {

    // PIN
    private int[] pin = new int[4];

    // PIN STATE
    private int pinState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        /* 초기화 */
        pinState = 0;

        /* 인스턴스화 */
        final View num1View = (View) findViewById(R.id.Lock_View_1);
        final View num2View = (View) findViewById(R.id.Lock_View_2);
        final View num3View = (View) findViewById(R.id.Lock_View_3);
        final View num4View = (View) findViewById(R.id.Lock_View_4);

        final Button button1 = (Button) findViewById(R.id.Lock_Button_1);
        final Button button2 = (Button) findViewById(R.id.Lock_Button_2);
        final Button button3 = (Button) findViewById(R.id.Lock_Button_3);
        final Button button4 = (Button) findViewById(R.id.Lock_Button_4);
        final Button button5 = (Button) findViewById(R.id.Lock_Button_5);
        final Button button6 = (Button) findViewById(R.id.Lock_Button_6);
        final Button button7 = (Button) findViewById(R.id.Lock_Button_7);
        final Button button8 = (Button) findViewById(R.id.Lock_Button_8);
        final Button button9 = (Button) findViewById(R.id.Lock_Button_9);
        final Button button0 = (Button) findViewById(R.id.Lock_Button_0);
        final Button backSpaceButton = (Button) findViewById(R.id.Lock_Button_back);
        final Button clearButton = (Button) findViewById(R.id.Lock_Button_clear);

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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
                    Log.i("PIN : ", Integer.toString(pin[0]) + Integer.toString(pin[1]) + Integer.toString(pin[2]) + Integer.toString(pin[3]));
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        backSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinState != 0){
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
            }
        });
    }
}