package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventNotificationActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private CheckBox checkBox;

    private TextView title;
    private TextView content;

    private String notiDate;
    private String notiTitle;
    private String notiContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_notification);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        final String todayTime = format1.format(time);

        Intent getintent = getIntent();
        notiDate = getintent.getExtras().getString("notiDate", "00000000");
        notiTitle = getintent.getExtras().getString("notiTitle", "이벤트가 없습니다.");
        notiContent = getintent.getExtras().getString("notiContent", "");

        title = (TextView)findViewById(R.id.eventTitle);
        content = (TextView)findViewById(R.id.eventContent);

        title.setText(notiTitle);
        content.setText(notiContent + "\n\n" + notiDate);

        /* close button */
        ImageButton closeButton = (ImageButton) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox = (CheckBox)findViewById(R.id.checkBox);
                if(checkBox.isChecked()){
                    //Shared Preference
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("notiDenyDate", notiDate);
                    editor.commit();
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        if(checkBox.isChecked()){
            //Shared Preference
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("notiDenyDate", notiDate);
            editor.commit();
        }
        finish();
    }
}