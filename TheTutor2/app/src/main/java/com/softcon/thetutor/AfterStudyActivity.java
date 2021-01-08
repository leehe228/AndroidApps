package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AfterStudyActivity extends AppCompatActivity {

    private int subj;
    private int totalTime, subjectTime;
    private String startTime, endTime;
    private int studyCon;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_after_study);

        /* 인스턴스 */
        TextView subName = (TextView)findViewById(R.id.textview_subName);
        TextView tv_subTime = (TextView)findViewById(R.id.textview_subTime);
        TextView tv_studyTime = (TextView)findViewById(R.id.textview_studyTime);
        RatingBar rb = (RatingBar)findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        subj = intent.getExtras().getInt("subject");
        totalTime = intent.getExtras().getInt("total");
        startTime = intent.getExtras().getString("startTime");

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        final String endTime = format.format(today);

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(startTime);
            d2 = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        final long diffMinutes = diff / (60 * 1000) % 60;

        tv_subTime.setText(diffMinutes + "분");
        tv_studyTime.setText(startTime + " ~ " + endTime);

        if(subj == 0){
            subName.setText("국어");
        }else if(subj == 1){
            subName.setText("수학");
        }else if(subj == 2){
            subName.setText("사회/역사");
        }else if(subj == 3){
            subName.setText("경제/경영");
        }else if(subj == 4){
            subName.setText("기술/가정");
        }else if(subj == 5){
            subName.setText("영어");
        }else if(subj == 6){
            subName.setText("예술/체육");
        }else if(subj == 7){
            subName.setText("한문/외국어");
        }else if(subj == 8){
            subName.setText("교양");
        }else if(subj == 9){
            subName.setText("기타");
        }

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                studyCon = (int)(rating * 2);
                if(studyCon > 0){

                }
            }
        });

        Button bt_next = (Button)findViewById(R.id.button_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(studyCon == 0){
                    Toast.makeText(getApplicationContext(), "방금 공부를 평가해주세요. (1점 이상)", Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    long totalTimeInt = pref.getLong("totalTime", 0) + diffMinutes;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putLong("totalTime", totalTimeInt);
                    editor.commit();
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "공부 평가를 해주세요", Toast.LENGTH_LONG).show();
        return;
    }
}