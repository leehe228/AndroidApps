package com.softcon.thetutorcpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class SettingActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String booleanNoti, booleanLock, booleanTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /* 인스턴스화 */
        Button changeSubjectButton = findViewById(R.id.Setting_Button_ChangeSubjects);
        Button pushButton = findViewById(R.id.Setting_Button_Push);
        Button lockButton = findViewById(R.id.Setting_Button_PIN);
        Button changePasswordButton = findViewById(R.id.Setting_Button_ChangePassword);
        Button developerButton = findViewById(R.id.Setting_Button_Developer);
        Button sendFeedbackButton = findViewById(R.id.Setting_Button_SendFeedback);
        Button logoutButton = findViewById(R.id.Setting_Button_Logout);
        Button quitButton = findViewById(R.id.Setting_Button_Quit);
        ImageButton backButton = findViewById(R.id.Setting_ImageButton_back);
        Button themeButton = findViewById(R.id.Setting_Button_Theme);

        final TextView pushTextView = findViewById(R.id.Setting_TextView_Push);
        TextView lockTextView = findViewById(R.id.Setting_TextView_PIN);
        final TextView themeTextView = findViewById(R.id.Setting_TextView_Theme);

        /* 데이터 로드 */
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        booleanNoti = pref.getString("push", "false");
        booleanLock = pref.getString("autoLogin", "true");
        booleanTheme = pref.getString("theme", "dark");

        if (booleanNoti.equals("true")) {
            pushTextView.setText("켜짐");
        } else {
            pushTextView.setText("꺼짐");
        }

        if (booleanLock.equals("lock")) {
            lockTextView.setText("켜짐");
        } else {
            lockTextView.setText("꺼짐");
        }

        if (booleanTheme.equals("dark")){
            themeTextView.setText("어두움");
        } else {
            themeTextView.setText("밝음");
        }

        changeSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EditSubjectActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (booleanTheme.equals("dark")){
                    booleanTheme = "light";
                    themeTextView.setText("밝음");
                    Toast.makeText(SettingActivity.this, "밝은 테마로 변경되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    booleanTheme = "dark";
                    themeTextView.setText("어두움");
                    Toast.makeText(SettingActivity.this, "어두운 테마로 변경되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booleanNoti.equals("true")) {
                    booleanNoti = "false";
                    pushTextView.setText("꺼짐");
                    Toast.makeText(SettingActivity.this, "푸시 알림 수신 꺼짐", Toast.LENGTH_SHORT).show();
                } else {
                    booleanNoti = "true";
                    pushTextView.setText("켜짐");
                    Toast.makeText(SettingActivity.this, "푸시 알림 수신 켜짐", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("push", booleanNoti);
                editor.apply();
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, SetLockActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        developerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DeveloperActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AskActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user", "");
                editor.putString("autoLogin", "false");
                editor.apply();

                Toast.makeText(SettingActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SettingActivity.this, LoadingActivity.class);
                ActivityCompat.finishAffinity(SettingActivity.this);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, QuitActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}