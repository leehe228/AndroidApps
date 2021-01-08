package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class AskActivity extends AppCompatActivity {

    private TextView tv_email;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_textNum;

    private EditText et_email;
    private EditText et_title;
    private EditText et_content;

    private Button sendButton;

    private String input;
    private int inputNum;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ask);

        /* 뒤로가기 버튼 */
        ImageButton backButton = (ImageButton)findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* 인스턴스화 */
        sendButton = (Button)findViewById(R.id.button_send);
        tv_email = (TextView)findViewById(R.id.tv_ask_email);
        tv_title = (TextView)findViewById(R.id.tv_ask_title);
        tv_content = (TextView)findViewById(R.id.tv_ask_content);
        tv_textNum = (TextView)findViewById(R.id.tv_ask_textNum);
        et_email = (EditText)findViewById(R.id.edittext_ask_email);
        et_title = (EditText)findViewById(R.id.edittext_ask_title);
        et_content = (EditText)findViewById(R.id.edittext_ask_content);

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        et_email.setText(pref.getString("userid", ""));

        /* 보내기 버튼 */
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_email.setTextColor(Color.parseColor("#000000"));
                tv_title.setTextColor(Color.parseColor("#000000"));
                tv_content.setTextColor(Color.parseColor("#000000"));
                if(et_email.getText().toString().equals("") || et_title.getText().toString().equals("") || et_content.getText().toString().equals("")){
                    if(et_email.getText().toString().equals("")){
                        tv_email.setTextColor(Color.parseColor("#E50000"));
                    }
                    if(et_title.getText().toString().equals("")){
                        tv_title.setTextColor(Color.parseColor("#E50000"));
                    }
                    if(et_content.getText().toString().equals("")){
                        tv_content.setTextColor(Color.parseColor("#E50000"));
                    }
                }
                else{
                    if(inputNum > 100){
                        Toast.makeText(getApplicationContext(), "본문은 100자 이하여야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new Thread() {
                            public void run() {
                                /* 서브 스레드 내에서 통신 개시 */
                                HttpPostData();
                            }
                        }.start();
                        Toast.makeText(getApplicationContext(), "전송 완료! 빠른 시일 내 답변드리겠습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input = et_content.getText().toString();
                tv_textNum.setText("글자수 " + input.length() + "/200자");
                inputNum = input.length();

                if(inputNum > 100){
                    tv_textNum.setTextColor(Color.parseColor("#E50000"));
                }
                else{
                    tv_textNum.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://141.164.36.245:8000/account/sendFeedback/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 서버로 값 전송
            StringBuffer buffer = new StringBuffer();
            buffer.append("email").append("=").append(et_email.getText().toString()).append("&");
            buffer.append("title").append("=").append(et_title.getText().toString()).append("&");
            buffer.append("content").append("=").append(et_content.getText().toString());
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } // try
    } // HttpPostData
}