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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuitMembershipActivity extends AppCompatActivity {

    String userID;
    String myResult;
    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quit_membership);

        /* 인스턴스화 */
        final EditText et_passwd = (EditText)findViewById(R.id.edittext_passwd);
        final EditText et_email = (EditText)findViewById(R.id.edittext_name);

        /* 탈퇴 버튼 */
        Button bt_quit = (Button)findViewById(R.id.button_quit);
        bt_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_passwd.getText().toString().equals("") || et_email.getText().toString().equals("")){
                    //
                }
                else {
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    String _userID = pref.getString("userid", "");
                    userID = et_email.getText().toString();
                    if(_userID.equals(userID)) {
                        new Thread() {
                            public void run() {
                                HttpPostData();
                            }
                        }.start();
                    }
                }
            }
        });

        /* 뒤로가기 버튼 */
        ImageButton backButton = (ImageButton)findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://141.164.36.245:8000/account/delete/");
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
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            String myResult = builder.toString();
            System.out.println(myResult);

            //Shared Preference
            SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor2 = pref2.edit();
            editor2.putString("userid", "");
            editor2.putString("autoLogin", "false");

            //Push Alarm
            editor2.putString("push", "true");

            //Time Setting
            editor2.putInt("studyTime", 50);
            editor2.putInt("restTime", 10);
            editor2.commit();

            Toast.makeText(getApplicationContext(), "탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData
}