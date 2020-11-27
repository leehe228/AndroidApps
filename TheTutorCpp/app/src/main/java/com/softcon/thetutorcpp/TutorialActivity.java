package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class TutorialActivity extends BaseActivity {

    private int tutorialState;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        /* 초기화 */
        tutorialState = 0;

        /* 인스턴스화 */
        final Button okButton = findViewById(R.id.Tutorial_Button_ok);
        final TextView titleTextView = findViewById(R.id.Tutorial_TextView_TOP);
        final TextView content1TextView = findViewById(R.id.Tutorial_TextView_text1);
        final TextView content2TextView = findViewById(R.id.Tutorial_TextView_text2);

        final ConstraintLayout CoverView = findViewById(R.id.TutorialActivity);
        final Animation LayoutUpAnimation = AnimationUtils.loadAnimation(TutorialActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);
        okButton.setEnabled(false);

        Handler m2Handler = new Handler();
        m2Handler.postDelayed(new Runnable() {
            public void run() {
                okButton.setEnabled(true);
                okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button));
            }
        }, 500);

        okButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Handler mHandler = new Handler();
                if (tutorialState == 0) {
                    titleTextView.setText("TO DO 작성하기");
                    content1TextView.setText("하루가 시작하기 전 할 일들을 작성하고");
                    content2TextView.setText("터치하여 중요도를 표시하세요");
                    okButton.setEnabled(false);
                    okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button_unclick));
                    CoverView.startAnimation(LayoutUpAnimation);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            okButton.setEnabled(true);
                            okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button));
                        }
                    }, 500);
                } else if (tutorialState == 1) {
                    titleTextView.setText("추천 시간표 확인하기");
                    content1TextView.setText("여러분이 공부를 더 잘 할 수 있도록");
                    content2TextView.setText("최적의 시간표를 작성해드립니다");
                    okButton.setEnabled(false);
                    okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button_unclick));
                    CoverView.startAnimation(LayoutUpAnimation);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            okButton.setEnabled(true);
                            okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button));
                        }
                    }, 500);
                } else if (tutorialState == 2) {
                    titleTextView.setText("학습 진단 확인하기");
                    content1TextView.setText("1주일 간 공부량을 확인해보세요");
                    content2TextView.setText("취약점을 찾고 개선해나가세요");
                    okButton.setEnabled(false);
                    okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button_unclick));
                    CoverView.startAnimation(LayoutUpAnimation);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            okButton.setEnabled(true);
                            okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button));
                        }
                    }, 500);
                } else if (tutorialState == 3) {
                    titleTextView.setText("홈 화면 둘러보기");
                    content1TextView.setText("시간표를 터치해 추천 시간표를 바로 보고");
                    content2TextView.setText("할 공부와 추천 공부를 한 눈에 확인하세요");
                    okButton.setEnabled(false);
                    okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button_unclick));
                    CoverView.startAnimation(LayoutUpAnimation);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            okButton.setEnabled(true);
                            okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button));
                        }
                    }, 500);

                } else if (tutorialState == 4) {
                    titleTextView.setText("설정 둘러보기");
                    content1TextView.setText("설정 탭에는 다양한 기능들이 있습니다.");
                    content2TextView.setText("필요한 기능들을 활용해보세요!");
                    okButton.setText("시작하기");
                    okButton.setEnabled(false);
                    okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button_unclick));
                    CoverView.startAnimation(LayoutUpAnimation);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            okButton.setEnabled(true);
                            okButton.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.basic_button));
                        }
                    }, 500);

                } else if (tutorialState == 5) {
                    //TODO 실제 해제
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("autoLogin", "true");
                    editor.apply();
                    Intent intent = new Intent(TutorialActivity.this, HomeActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                }
                tutorialState++;
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}