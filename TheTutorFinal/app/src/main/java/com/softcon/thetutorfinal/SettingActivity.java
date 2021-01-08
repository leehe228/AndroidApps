package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /* 인스턴스화 */
        Button changeSubjectButton = (Button) findViewById(R.id.Setting_Button_ChangeSubjects);
        Button pushButton = (Button) findViewById(R.id.Setting_Button_Push);
        Button changePasswordButton = (Button) findViewById(R.id.Setting_Button_ChangePassword);
        Button developerButton = (Button) findViewById(R.id.Setting_Button_Developer);
        Button sendFeedbackButton = (Button) findViewById(R.id.Setting_Button_SendFeedback);
        Button logoutButton = (Button) findViewById(R.id.Setting_Button_Logout);
        Button quitButton = (Button) findViewById(R.id.Setting_Button_Quit);
        ImageButton backButton = (ImageButton) findViewById(R.id.Setting_ImageButton_back);

        TextView pushTextView = (TextView) findViewById(R.id.Setting_TextView_Push);

        changeSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        developerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, DeveloperActivity.class);
                startActivity(intent);
            }
        });

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AskActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, HelloActivity.class);
                ActivityCompat.finishAffinity(SettingActivity.this);
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, QuitActivity.class);
                startActivity(intent);
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