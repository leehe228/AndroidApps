package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

import static java.nio.charset.StandardCharsets.UTF_8;

public class SignupActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    String UserID;
    String emailDot;
    String Password;
    String Password2;
    String Name;
    String myResult;
    String okCode;
    private int step;
    private String tempPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        /* EditText 정보 링킹 */
        final EditText edittext_userid = (EditText) findViewById(R.id.edittext_email);
        final EditText edittext_emaildot = (EditText) findViewById(R.id.edittext_email_dot);
        final EditText edittext_password = (EditText) findViewById(R.id.edittext_passwd);
        final EditText edittext_password2 = (EditText) findViewById(R.id.edittext_passwd2);
        final EditText edittext_name = (EditText) findViewById(R.id.edittext_name);

        final TextView textview_at = (TextView) findViewById(R.id.textview_at);
        textview_at.setText("@");

        step = 1;

        /* 엔터키 막기 */
        edittext_userid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        edittext_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        edittext_password2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        edittext_name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        /* 텍스트뷰 정보 링킹 */
        final TextView tv_email = (TextView) findViewById(R.id.tv_signup_email);
        final TextView tv_passwd1 = (TextView) findViewById(R.id.tv_signup_passwd1);
        final TextView tv_passwd2 = (TextView) findViewById(R.id.tv_signup_passwd2);
        final TextView tv_name = (TextView) findViewById(R.id.tv_signup_name);

        /* 버튼 정보 링킹 */
        final Button button_next = (Button) findViewById(R.id.button_next);
        ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
        Button bt_privacy = (Button) findViewById(R.id.button_privacypolicy);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_privacy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 개발자 테스트용 */
                if (edittext_userid.getText().toString().equals("cnsa20010228")) {
                    Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userid", "testUser");
                    editor.putString("autoLogin", "true");
                    editor.commit();
                    intent.putExtra("step", 1);
                    startActivity(intent);
                    finish();
                }
                if (step == 1) {
                    UserID = edittext_userid.getText().toString();
                    emailDot = edittext_emaildot.getText().toString();
                    Password = edittext_password.getText().toString();
                    Password2 = edittext_password2.getText().toString();
                    Name = edittext_name.getText().toString();

                    tv_email.setTextColor(Color.parseColor("#000000"));
                    tv_passwd1.setTextColor(Color.parseColor("#000000"));
                    tv_passwd2.setTextColor(Color.parseColor("#000000"));
                    tv_name.setTextColor(Color.parseColor("#000000"));

                    if (UserID.toString().equals("") || emailDot.toString().equals("") || Password.toString().equals("") || Password2.toString().equals("") || Name.toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                        if (UserID.toString().equals("") || emailDot.toString().equals("")) {
                            tv_email.setTextColor(Color.parseColor("#E50000"));
                        }
                        if (Password.toString().equals("")) {
                            tv_passwd1.setTextColor(Color.parseColor("#E50000"));
                        }
                        if (Password2.toString().equals("")) {
                            tv_passwd2.setTextColor(Color.parseColor("#E50000"));
                        }
                        if (Name.toString().equals("")) {
                            tv_name.setTextColor(Color.parseColor("#E50000"));
                        }
                    } else if (Password.toString().equals(Password2.toString())) {
                        Toast.makeText(getApplicationContext(), "이메일로 전송된 코드를 입력해주세요", Toast.LENGTH_SHORT).show();
                        UserID = UserID + "@" + emailDot;
                        System.out.println(UserID);
                        button_next.setText("확인");
                        tv_email.setText("확인 코드(6자리)");

                        tv_passwd1.setVisibility(View.INVISIBLE);
                        tv_passwd2.setVisibility(View.INVISIBLE);
                        tv_name.setVisibility(View.INVISIBLE);
                        textview_at.setVisibility(View.INVISIBLE);
                        edittext_emaildot.setVisibility(View.INVISIBLE);
                        edittext_userid.setInputType(2);
                        edittext_password.setVisibility(View.INVISIBLE);
                        edittext_password2.setVisibility(View.INVISIBLE);
                        edittext_name.setVisibility(View.INVISIBLE);
                        edittext_userid.setText("");
                        step = 2;
                        new Thread() {
                            public void run() {
                                HttpPostData_SEND_EMAIL();
                            }
                        }.start();
                    } else {
                        tv_email.setTextColor(Color.parseColor("#000000"));
                        tv_passwd1.setTextColor(Color.parseColor("#E50000"));
                        tv_passwd2.setTextColor(Color.parseColor("#E50000"));
                        tv_name.setTextColor(Color.parseColor("#000000"));
                    }
                } else if (step == 2) {
                    String _okCode = edittext_userid.getText().toString();
                    if (okCode.equals(_okCode)) {
                        new Thread() {
                            public void run() {
                                HttpPostData();
                            }
                        }.start();
                    } else {
                        Toast.makeText(getApplicationContext(), "확인 코드가 틀립니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://141.164.36.245:8000/account/signup/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송

            try {
                tempPassword = AES256Chiper.AES_Encode(Password);
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
            buffer.append("userid").append("=").append(UserID).append("&");
            ;
            buffer.append("password").append("=").append(tempPassword).append("&");
            ;
            buffer.append("name").append("=").append(Name);
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
                builder.append(str + "\n");
            }
            myResult = builder.toString();
            System.out.println(myResult);
            if (!myResult.equals(null) && myResult.equals("1\n")) {
                //Shared Preference
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userid", UserID);
                editor.putString("autoLogin", "step1");
                editor.putString("push", "true");
                editor.putInt("studyTime", 50);
                editor.putInt("restTime", 10);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                intent.putExtra("step", 1);
                startActivity(intent);
                finish();
            } else {
                //
            }
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // try
    } // HttpPostData

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
            buffer.append("userid").append("=").append(UserID).append("&");
            buffer.append("emailType").append("=").append("REGS");
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
            if (!myResult.equals("0")) {
                System.out.println(myResult);
                okCode = myResult;
            } else {
                //fail
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } // try
    } // HttpPostData
}