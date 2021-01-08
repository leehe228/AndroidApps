package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mmin18.widget.RealtimeBlurView;

import java.io.Console;

public class SettingActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    int StudyTime;
    int RestTime;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        /* 버튼 정보 링킹 */
        ImageButton backButton = (ImageButton)findViewById(R.id.button_back);
        Button bt_changeEdu = (Button)findViewById(R.id.setting_changeedu);
        Button bt_studyTime = (Button)findViewById(R.id.setting_studytime);
        Button bt_restTime = (Button)findViewById(R.id.setting_resttime);
        //Button bt_myPlan = (Button)findViewById(R.id.setting_myplan);
        Button bt_Push = (Button)findViewById(R.id.setting_pushalarm);
        Button bt_changePassword = (Button)findViewById(R.id.setting_changepasswd);
        Button bt_dev = (Button)findViewById(R.id.setting_dev);
        Button bt_ask = (Button)findViewById(R.id.setting_ask);
        Button bt_logOut = (Button)findViewById(R.id.setting_logout);
        Button bt_quitMember = (Button)findViewById(R.id.setting_quitmember);

        final TextView tv_Push = (TextView)findViewById(R.id.setting_pushOnOff);
        final TextView tv_StudyTime = (TextView)findViewById(R.id.textview_studytime);
        final TextView tv_RestTime = (TextView)findViewById(R.id.textview_resttime);

        /* Blur */
        RealtimeBlurView blurview = (RealtimeBlurView)findViewById(R.id.blur);
        blurview.setVisibility(View.INVISIBLE);

        /* 공부/쉬는 시간 가져오기 */
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        StudyTime = (pref.getInt("studyTime", 50));
        RestTime = (pref.getInt("restTime", 10));

        String sTime = Integer.toString(StudyTime) + "분";
        String rTime = Integer.toString(RestTime) + "분";
        tv_StudyTime.setText(sTime);
        tv_RestTime.setText(rTime);

        /* 푸시 알림 텍스트 */
        SharedPreferences pref_push = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String push1 = pref_push.getString("push", "");
        if(push1.equals("false")){
            tv_Push.setText("꺼짐");
            tv_Push.setTextColor(Color.parseColor("#8D8D8D"));
        }
        else if (push1.equals("true")){
            tv_Push.setText("켜짐");
            tv_Push.setTextColor(Color.parseColor("#000000"));
        }

        /* 푸시 알림 버튼 */
        bt_Push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 푸시 알림 여부 */
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                String push = pref.getString("push", "");

                if(push.equals("true")){
                    tv_Push.setText("꺼짐");
                    tv_Push.setTextColor(Color.parseColor("#8D8D8D"));
                    //Shared Preference
                    SharedPreferences pref3 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = pref3.edit();
                    editor3.putString("push", "false");
                    editor3.commit();
                    Toast.makeText(getApplicationContext(), "푸시 알림 수신 거절", Toast.LENGTH_SHORT).show();
                }
                else {
                    tv_Push.setText("켜짐");
                    tv_Push.setTextColor(Color.parseColor("#000000"));
                    //Shared Preference
                    SharedPreferences pref3 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = pref3.edit();
                    editor3.putString("push", "true");
                    editor3.commit();
                    Toast.makeText(getApplicationContext(), "푸시 알림 수신 동의", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /* 정기일정 수정 */
        /*bt_myPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditPlanActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "지원 예정인 기능입니다.", Toast.LENGTH_SHORT).show();
            }
        });*/

        /* 학력 및 과목 변경 */
        bt_changeEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeSubjectActivity.class);
                startActivity(intent);
            }
        });

        /* 비밀번호 변경 */
        bt_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        /* 개발팀 */
        bt_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent);
            }
        });

        /* 문의하기(베타 - 피드백) */
        bt_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AskActivity.class);
                startActivity(intent);
            }
        });


        /* 로그아웃 */
        bt_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Shared Preference
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userid", "");
                editor.putString("autoLogin", "false");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                ActivityCompat.finishAffinity(SettingActivity.this);
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });

        /* 탈퇴 */
        bt_quitMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuitMembershipActivity.class);
                startActivity(intent);
            }
        });

        /* 뒤로 가기 버튼 (액티비티 종료) */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //버튼
    public void mStudyPopOpen(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, PopTimeActivity.class);
        intent.putExtra("type", "study");
        intent.putExtra("time", StudyTime);
        startActivityForResult(intent, 1);
        /* Blur */
        RealtimeBlurView blurview = (RealtimeBlurView)findViewById(R.id.blur);
        blurview.setVisibility(View.VISIBLE);
    }

    //버튼
    public void mRestPopOpen(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, PopTimeActivity.class);
        intent.putExtra("type", "rest");
        intent.putExtra("time", RestTime);
        startActivityForResult(intent, 2);
        /* Blur */
        RealtimeBlurView blurview = (RealtimeBlurView)findViewById(R.id.blur);
        blurview.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                int _result = Integer.parseInt(data.getStringExtra("result"));

                //Shared Preference
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("studyTime", _result);
                editor.commit();

                String s_result = Integer.toString(_result) + "분";
                final TextView tv_StudyTime = (TextView) findViewById(R.id.textview_studytime);
                tv_StudyTime.setText(s_result);
                /* Blur */
                RealtimeBlurView blurview = (RealtimeBlurView)findViewById(R.id.blur);
                blurview.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "공부시간이 설정되었습니다", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                int _result = Integer.parseInt(data.getStringExtra("result"));

                //Shared Preference
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("restTime", _result);
                editor.commit();

                String s_result = Integer.toString(_result) + "분";
                final TextView tv_RestTime = (TextView) findViewById(R.id.textview_resttime);
                tv_RestTime.setText(s_result);
                /* Blur */
                RealtimeBlurView blurview = (RealtimeBlurView)findViewById(R.id.blur);
                blurview.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "쉬는 시간이 설정되었습니다", Toast.LENGTH_LONG).show();
            }
        }
    }


}