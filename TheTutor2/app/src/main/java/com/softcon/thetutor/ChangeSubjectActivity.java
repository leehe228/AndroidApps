package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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

public class ChangeSubjectActivity extends AppCompatActivity {

    private int[] subjects = new int[10];
    String education;
    String s_subject;
    int minS = 0;
    String[] data = new String[3];
    String[] subData = new String[10];

    private Spinner spinner;

    private Button sub1;
    private Button sub2;
    private Button sub3;
    private Button sub4;
    private Button sub5;
    private Button sub6;
    private Button sub7;
    private Button sub8;
    private Button sub9;
    private Button sub10;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_subject);

        try {
            Thread.sleep(200);
            new Thread() {
                public void run() {
                    HttpPostData_GET_DATA();
                }
            }.start();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        /* 인스턴스화 */
        Button bt_next = (Button)findViewById(R.id.button_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subjects[0] == 0 || subjects[1] == 0 || subjects[2] == 0 || subjects[3] == 0 || subjects[4] == 0 || subjects[5] == 0 || subjects[6] == 0 || subjects[7] == 0 || subjects[8] == 0 || subjects[9] == 0){
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

        ImageButton backButton = (ImageButton)findViewById(R.id.button_back);
        /* 뒤로 가기 버튼 (액티비티 종료) */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "수정된 정보를 저장하지 않습니다", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        /* 학력 선택 스피너 인스턴스화 */
        spinner = (Spinner)findViewById(R.id.spinner_education);
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
        sub1 = (Button)findViewById(R.id.subject1);
        sub2 = (Button)findViewById(R.id.subject2);
        sub3 = (Button)findViewById(R.id.subject3);
        sub4 = (Button)findViewById(R.id.subject4);
        sub5 = (Button)findViewById(R.id.subject5);
        sub6 = (Button)findViewById(R.id.subject6);
        sub7 = (Button)findViewById(R.id.subject7);
        sub8 = (Button)findViewById(R.id.subject8);
        sub9 = (Button)findViewById(R.id.subject9);
        sub10 = (Button)findViewById(R.id.subject10);


        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[0] == 0){
                    subjects[0] = 1;
                    sub1.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[0] == 1){
                    subjects[0] = 0;
                    sub1.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[1] == 0){
                    subjects[1] = 1;
                    sub2.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[1] == 1){
                    subjects[1] = 0;
                    sub2.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[2] == 0){
                    subjects[2] = 1;
                    sub3.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[2] == 1){
                    subjects[2] = 0;
                    sub3.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[3] == 0){
                    subjects[3] = 1;
                    sub4.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[3] == 1){
                    subjects[3] = 0;
                    sub4.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[4] == 0){
                    subjects[4] = 1;
                    sub5.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[4] == 1){
                    subjects[4] = 0;
                    sub5.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[5] == 0){
                    subjects[5] = 1;
                    sub6.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[5] == 1){
                    subjects[5] = 0;
                    sub6.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[6] == 0){
                    subjects[6] = 1;
                    sub7.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[6] == 1){
                    subjects[6] = 0;
                    sub7.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[7] == 0){
                    subjects[7] = 1;
                    sub8.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[7] == 1){
                    subjects[7] = 0;
                    sub8.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[8] == 0){
                    subjects[8] = 1;
                    sub9.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[8] == 1){
                    subjects[8] = 0;
                    sub9.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });

        sub10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjects[9] == 0){
                    subjects[9] = 1;
                    sub10.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
                }
                else if(subjects[9] == 1){
                    subjects[9] = 0;
                    sub10.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
                }
            }
        });
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
                Intent intent = new Intent(getApplicationContext(), ChangePrefSubjectActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData

    public void HttpPostData_GET_DATA() {
        try {
            //* 유저 정보 가져오기 */
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            String userID = pref.getString("userid", "");
            URL url = new URL("http://141.164.36.245:8000/account/userInfo/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            StringBuffer buffer = new StringBuffer();
            buffer.append("userid").append("=").append(userID);
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            int i = 0;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
                if(i <= 2) {
                    data[i] = str;
                    i++;
                }
            }
            subData = data[2].split("");
            String myResult = builder.toString();
            System.out.println(myResult);
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);

        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            spinner.setSelection(Integer.parseInt(data[1]));

            for(int j = 0;j<10;j++){
                subjects[j] = Integer.parseInt(subData[j]);
            }

            if(subjects[0] == 1){
                sub1.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[0] == 0){
                sub1.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[1] == 1){
                sub2.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[1] == 0){
                sub2.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[2] == 1){
                sub3.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[2] == 0){
                sub3.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[3] == 1){
                sub4.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[3] == 0){
                sub4.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[4] == 1){
                sub5.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[4] == 0){
                sub5.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[5] == 1){
                sub6.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[5] == 0){
                sub6.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[6] == 1){
                sub7.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[6] == 0){
                sub7.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[7] == 1){
                sub8.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[7] == 0){
                sub8.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[8] == 1){
                sub9.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[8] == 0){
                sub9.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }

            if(subjects[9] == 1){
                sub10.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.pushed_shape));
            }
            else if(subjects[9] == 0){
                sub10.setBackground(ContextCompat.getDrawable(ChangeSubjectActivity.this, R.drawable.unpushed_shape));
            }
        }
    };
}