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

public class FindPasswordActivity extends AppCompatActivity {

    private int step;
    String userEmail;
    String okCode;
    String myResult;
    String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_find_password);

        /* step setting */
        step = 1;

        /* 인스턴스화 */
        final TextView tv_email = (TextView)findViewById(R.id.tv_findpw_email);
        final EditText et_email = (EditText)findViewById(R.id.edittext_email);
        final Button bt_send = (Button)findViewById(R.id.button_findpw_send);

        final TextView tv_okCode = (TextView)findViewById(R.id.tv_findpw_okcode);
        final EditText et_okCode = (EditText)findViewById(R.id.edittext_okcode);
        final Button bt_confirm = (Button)findViewById(R.id.button_findpw_confirm);

        final TextView tv_passwd1 = (TextView)findViewById(R.id.tv_findpw_passwd1);
        final EditText et_passwd1 = (EditText)findViewById(R.id.edittext_passwd1);

        final TextView tv_passwd2 = (TextView)findViewById(R.id.tv_findpw_passwd2);
        final EditText et_passwd2 = (EditText)findViewById(R.id.edittext_passwd2);

        final Button bt_ok = (Button)findViewById(R.id.button_ok);

        /* STEP1 이메일 입력 */
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_email.setTextColor(Color.parseColor("#000000"));
                if(et_email.getText().toString().equals("")){
                    tv_email.setTextColor(Color.parseColor("#E50000"));
                }
                else{
                    step = 2;
                    tv_okCode.setVisibility(View.VISIBLE);
                    et_okCode.setVisibility(View.VISIBLE);
                    bt_confirm.setVisibility(View.VISIBLE);
                    userEmail = et_email.getText().toString();
                    new Thread() {
                        public void run() {
                            /* 서브 스레드 내에서 통신 개시 */
                            HttpPostData_SEND_EMAIL();
                        }
                    }.start();
                }
            }
        });

        /* STEP2 확인코드 입력*/
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(step == 2) {
                    tv_okCode.setTextColor(Color.parseColor("#000000"));
                    if (et_okCode.getText().toString().equals("")) {
                        tv_okCode.setTextColor(Color.parseColor("#E50000"));
                    } else if (et_okCode.getText().toString().equals(okCode)) {
                        step = 3;
                        tv_passwd1.setVisibility(View.VISIBLE);
                        tv_passwd2.setVisibility(View.VISIBLE);
                        et_passwd1.setVisibility(View.VISIBLE);
                        et_passwd2.setVisibility(View.VISIBLE);
                        bt_ok.setVisibility(View.VISIBLE);
                    } else {
                        //잘못된 코드 입력
                        tv_okCode.setTextColor(Color.parseColor("#E50000"));
                    }
                }
            }
        });

        /* STEP3 비밀번호 변경 */
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_passwd1.setTextColor(Color.parseColor("#000000"));
                tv_passwd2.setTextColor(Color.parseColor("#000000"));
                if(step == 3){
                    if(et_passwd1.getText().toString().equals("") || et_passwd2.getText().toString().equals("")){
                        if(et_passwd1.getText().toString().equals("")){
                            tv_passwd1.setTextColor(Color.parseColor("#E50000"));
                        }
                        if(et_passwd2.getText().toString().equals("")){
                            tv_passwd2.setTextColor(Color.parseColor("#E50000"));
                        }
                    }
                    else if(et_passwd1.getText().toString().equals(et_passwd2.getText().toString())){
                        newPassword = et_passwd1.getText().toString();
                        new Thread() {
                            public void run() {
                                /* 서브 스레드 내에서 통신 개시 */
                                HttpPostData_CHANGE_PASSWORD();
                            }
                        }.start();
                    }
                    else{
                        //
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

    public void HttpPostData_SEND_EMAIL() {
        try {
            URL url = new URL("http://141.164.36.245:8000/account/findPassword/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            StringBuffer buffer = new StringBuffer();
            buffer.append("userid").append("=").append(userEmail).append("&");
            buffer.append("emailType").append("=").append("FIND");
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
            if(!myResult.equals("0")) {
                System.out.println(myResult);
                okCode = myResult;
            }
            else {
                //fail
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } // try
    } // HttpPostData

    public void HttpPostData_CHANGE_PASSWORD() {
        try {
            URL url = new URL("http://141.164.36.245:8000/account/changePassword/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            try {
                newPassword = AES256Chiper.AES_Encode(newPassword);
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
            buffer.append("password").append("=").append(newPassword);

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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
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