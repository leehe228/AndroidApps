package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ChangePasswordActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";
    private String pw_current;
    private String pw_new;
    private String pw_new2;
    String myResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password);

        /* 인스턴스화 */
        final TextView tv_current = (TextView)findViewById(R.id.textview_current);
        final EditText et_current = (EditText)findViewById(R.id.edittext_changepw_current);

        final TextView tv_new1 = (TextView)findViewById(R.id.textview_newpassword1);
        final EditText et_new1 = (EditText)findViewById(R.id.edittext_passwd1);

        final TextView tv_new2 = (TextView)findViewById(R.id.textview_newpassword2);
        final EditText et_new2 = (EditText)findViewById(R.id.edittext_passwd2);

        Button bt_next = (Button)findViewById(R.id.button_next);

        /* 로그인 버튼 */
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_current.setTextColor(Color.parseColor("#000000"));
                tv_new1.setTextColor(Color.parseColor("#000000"));
                tv_new2.setTextColor(Color.parseColor("#000000"));

                if(et_current.getText().toString().equals("") || et_new1.getText().toString().equals("") || et_new2.getText().toString().equals("")){
                    if(et_current.getText().toString().equals("")){
                        tv_current.setTextColor(Color.parseColor("#E50000"));
                    }
                    if(et_new1.getText().toString().equals("")){
                        tv_new1.setTextColor(Color.parseColor("#E50000"));
                    }
                    if(et_new2.getText().toString().equals("")){
                        tv_new2.setTextColor(Color.parseColor("#E50000"));
                    }
                }
                /* 빈 칸이 없는 경우 */
                else{
                    pw_current = et_current.getText().toString();
                    pw_new = et_new1.getText().toString();
                    pw_new2 = et_new2.getText().toString();
                    if(pw_new.equals(pw_new2)) {
                        new Thread() {
                            public void run() {
                                /* 서브 스레드 내에서 통신 개시 */
                                HttpPostData();
                            }
                        }.start();
                    }
                }
            }
        });

        ImageButton backButton = (ImageButton)findViewById(R.id.button_back);
        /* 뒤로 가기 버튼 (액티비티 종료) */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void HttpPostData() {
        try {
            /* 유저 정보 가져오기 */
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            String userEmail = pref.getString("userid", "");

            URL url = new URL("http://141.164.36.245:8000/account/changePassword/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            try {
                pw_new = AES256Chiper.AES_Encode(pw_new);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append("userid").append("=").append(userEmail).append("&");
            buffer.append("password").append("=").append(pw_new);

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
            if(myResult != null && myResult.equals("1")) {
                System.out.println(myResult);
                finish();
            }
            else {
                //fail
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } // try
    } // HttpPostData
}