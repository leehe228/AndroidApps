package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    /* 유저 정보 변수 */
    String UserID;
    String Password;
    String myResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        final EditText edittext_id = (EditText)findViewById(R.id.edittext_email);
        final EditText edittext_password = (EditText)findViewById(R.id.edittext_passwd);

        /* 엔터키 막기 */
        edittext_id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER)
                {
                    return true;
                }
                return false;
            }
        });

        edittext_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER)
                {
                    return true;
                }
                return false;
            }
        });

        /* 버튼 인스턴스화  */
        final Button button_login = (Button) findViewById(R.id.button_login);
        final ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        Button bt_find = (Button)findViewById(R.id.button_findpw);

        /* 텍스트뷰 인스턴스화 */
        final TextView tv_email = (TextView)findViewById(R.id.tv_login_email);
        final TextView tv_passwd = (TextView)findViewById(R.id.tv_login_passwd);

        /* 뒤로가기 버튼 */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /* 비밀번호 찾기 */
        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        /* 로그인 버튼 */
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserID = edittext_id.getText().toString();
                Password = edittext_password.getText().toString();
                tv_email.setTextColor(Color.parseColor("#000000"));
                tv_passwd.setTextColor(Color.parseColor("#000000"));
                /* 빈 칸이 있을 경우 */
                if(UserID.toString().equals("") || Password.toString().equals("")){
                    Toast.makeText(getApplicationContext(),"모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    if(UserID.toString().equals("")){
                        tv_email.setTextColor(Color.parseColor("#E50000"));
                    }
                    if(Password.toString().equals("")){
                        tv_passwd.setTextColor(Color.parseColor("#E50000"));
                    }
                }
                /* 빈 칸이 없는 경우 */
                else{
                    new Thread() {
                        public void run() {
                            /* 서브 스레드 내에서 통신 개시 */
                            HttpPostData();
                        }
                    }.start();
                }
            }
        });
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://141.164.36.245:8000/account/login/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송

            try {
                Password = AES256Chiper.AES_Encode(Password);
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("userid").append("=").append(UserID).append("&");
            buffer.append("password").append("=").append(Password);

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
            myResult = builder.toString();
            System.out.println(myResult);
            if(myResult != null && myResult.equals("-1")){
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            else if(myResult != null && myResult.equals("1")) {
                //Shared Preference
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userid", UserID);
                editor.putString("autoLogin", "true");
                editor.commit();

                //Push Alarm
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor.putString("push", "true");
                editor.commit();

                //Time Setting
                SharedPreferences pref3 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor3 = pref3.edit();
                editor.putInt("studyTime", 50);
                editor.putInt("restTime", 10);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"로그인 성공", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }
            else if(myResult != null && myResult.toString().equals("0")){
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } // try
    } // HttpPostData
}