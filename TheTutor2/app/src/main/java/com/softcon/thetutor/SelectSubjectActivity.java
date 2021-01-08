package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SelectSubjectActivity extends AppCompatActivity {

    int[] subjects = new int[10];
    String education;
    String s_subject;
    int minS = 0;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_subject);

        /* 인스턴스화 */
        Button bt_next = (Button)findViewById(R.id.button_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minS = subjects[0] + subjects[1] + subjects[2] + subjects[3] + subjects[4] + subjects[5] + subjects[6] + subjects[7] + subjects[8] + subjects[9];
                if(minS == 0){
                    System.out.println("최소 한 개 이상 선택하시오");
                }
                else{
                    s_subject = Integer.toString(subjects[0]) + Integer.toString(subjects[1]) + Integer.toString(subjects[2]) + Integer.toString(subjects[3]);
                    s_subject = s_subject + Integer.toString(subjects[4]) + Integer.toString(subjects[5]) + Integer.toString(subjects[6]) + Integer.toString(subjects[7]);
                    s_subject = s_subject + Integer.toString(subjects[8]) + Integer.toString(subjects[9]);
                    System.out.println(s_subject);
                    new Thread() {
                        public void run() {
                            HttpPostData();
                        }
                    }.start();
                }
            }
        });

        /* 학력 선택 스피너 인스턴스화 */
        Spinner spinner = (Spinner)findViewById(R.id.spinner_education);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                parent.getItemAtPosition(position);
                education = Integer.toString(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        /* 과목 선택 버튼 인스턴스화 */
        final Button sub1 = (Button)findViewById(R.id.subject1);
        final Button sub2 = (Button)findViewById(R.id.subject2);
        final Button sub3 = (Button)findViewById(R.id.subject3);
        final Button sub4 = (Button)findViewById(R.id.subject4);
        final Button sub5 = (Button)findViewById(R.id.subject5);
        final Button sub6 = (Button)findViewById(R.id.subject6);
        final Button sub7 = (Button)findViewById(R.id.subject7);
        final Button sub8 = (Button)findViewById(R.id.subject8);
        final Button sub9 = (Button)findViewById(R.id.subject9);
        final Button sub10 = (Button)findViewById(R.id.subject10);


        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[0] == 0){
                    subjects[0] = 1;
                    sub1.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[0] == 1){
                    subjects[0] = 0;
                    sub1.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[1] == 0){
                    subjects[1] = 1;
                    sub2.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[1] == 1){
                    subjects[1] = 0;
                    sub2.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[2] == 0){
                    subjects[2] = 1;
                    sub3.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[2] == 1){
                    subjects[2] = 0;
                    sub3.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[3] == 0){
                    subjects[3] = 1;
                    sub4.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[3] == 1){
                    subjects[3] = 0;
                    sub4.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[4] == 0){
                    subjects[4] = 1;
                    sub5.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[4] == 1){
                    subjects[4] = 0;
                    sub5.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[5] == 0){
                    subjects[5] = 1;
                    sub6.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[5] == 1){
                    subjects[5] = 0;
                    sub6.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[6] == 0){
                    subjects[6] = 1;
                    sub7.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[6] == 1){
                    subjects[6] = 0;
                    sub7.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[7] == 0){
                    subjects[7] = 1;
                    sub8.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[7] == 1){
                    subjects[7] = 0;
                    sub8.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[8] == 0){
                    subjects[8] = 1;
                    sub9.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[8] == 1){
                    subjects[8] = 0;
                    sub9.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[9] == 0){
                    subjects[9] = 1;
                    sub10.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[9] == 1){
                    subjects[9] = 0;
                    sub10.setBackground(ContextCompat.getDrawable(SelectSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "정보를 입력해주세요!", Toast.LENGTH_LONG).show();
        return;
    }

    public void HttpPostData() {
        try {
            //* 유저 정보 가져오기 */
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            String userID = pref.getString("userid", "");
            URL url = new URL("http://141.164.36.245:8000/account/profile1/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            StringBuffer buffer = new StringBuffer();
            buffer.append("userid").append("=").append(userID).append("&");
            buffer.append("education").append("=").append(education).append("&");
            buffer.append("subject").append("=").append(s_subject);
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            String myResult = builder.toString();
            if(myResult.equals("1")){
                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                intent.putExtra("step", 3);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref2.edit();
                editor.putString("autoLogin", "step2");
                editor.commit();
                startActivity(intent);
                finish();
            }
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData

}