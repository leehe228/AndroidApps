package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class SeeMoreActivity extends AppCompatActivity {

    private Button[] subButton = new Button[10];
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_see_more);

        /* 인스턴스화 */
        subButton[0] = (Button)findViewById(R.id.subject1);
        subButton[1] = (Button)findViewById(R.id.subject2);
        subButton[2] = (Button)findViewById(R.id.subject3);
        subButton[3] = (Button)findViewById(R.id.subject4);
        subButton[4] = (Button)findViewById(R.id.subject5);
        subButton[5] = (Button)findViewById(R.id.subject6);
        subButton[6] = (Button)findViewById(R.id.subject7);
        subButton[7] = (Button)findViewById(R.id.subject8);
        subButton[8] = (Button)findViewById(R.id.subject9);
        subButton[9] = (Button)findViewById(R.id.subject10);

        backButton = (ImageButton)findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 공부 시작 */
        subButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 0);
                startActivity(intent);
                finish();
            }
        });

        subButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 1);
                startActivity(intent);
                finish();
            }
        });

        subButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 2);
                startActivity(intent);
                finish();
            }
        });

        subButton[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 3);
                startActivity(intent);
                finish();
            }
        });

        subButton[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 4);
                startActivity(intent);
                finish();
            }
        });

        subButton[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 5);
                startActivity(intent);
                finish();
            }
        });

        subButton[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 6);
                startActivity(intent);
                finish();
            }
        });

        subButton[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 7);
                startActivity(intent);
                finish();
            }
        });

        subButton[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 8);
                startActivity(intent);
                finish();
            }
        });

        subButton[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeMoreActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 9);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}