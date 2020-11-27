package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mmin18.widget.RealtimeBlurView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CoachingActivity extends BaseActivity {

    private String FABState;
    private Animation fab_open, fab_close;

    // FAB
    private ImageButton studyFAB, diagnosisFAB, settingFAB, todoFAB;
    private TextView studyFABTextView, diagnosisFABTextView, settingFABTextView, todoFABTextView;

    private RealtimeBlurView blurView;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaching);

        /* INIT SETTING */
        FABState = "false";
        fab_open = AnimationUtils.loadAnimation(CoachingActivity.this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(CoachingActivity.this, R.anim.fab_close);

        /* 인스턴스화 */
        final TextView dateTextView = findViewById(R.id.Coaching_TextView_date);
        final TextView totalTimeTextView = findViewById(R.id.Coaching_TextView_total);
        final TextView ddayTextView = findViewById(R.id.Coaching_TextView_dday);
        Spinner spinner = findViewById(R.id.Coaching_spinner);

        // FAB
        ImageButton mainFAB = findViewById(R.id.FAB_Coaching);
        studyFAB = findViewById(R.id.FAB_study);
        diagnosisFAB = findViewById(R.id.FAB_diagnosis);
        settingFAB = findViewById(R.id.FAB_setting);
        todoFAB = findViewById(R.id.FAB_todo);

        studyFABTextView = findViewById(R.id.FABTV_study);
        diagnosisFABTextView = findViewById(R.id.FABTV_diagnosis);
        settingFABTextView = findViewById(R.id.FABTV_setting);
        todoFABTextView = findViewById(R.id.FABTV_todo);

        blurView = findViewById(R.id.Coaching_blurView);

        /* 전체 공부 시간 가져오기 */
        SharedPreferences prefTotal2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        long totalTime = prefTotal2.getLong("totalTime", 0);
        totalTimeTextView.setText(totalTime / 60 + "H " + totalTime % 60 + "M");

        /* 현재 날짜 설정 */
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
        String display = year + "년 " + month + "월 " + day + "일";
        String tdayFormat = year + "-" + month + "-" + day;
        dateTextView.setText(display);

        /* 디데이 설정 */
        SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String ddayDate = pref2.getString("dday", "exception");
        String ddayName = pref2.getString("ddayName", "");

        assert ddayDate != null;
        if (ddayDate.equals("exception")) {
            ddayTextView.setText("여기를 눌러 디데이 설정");
        } else {
            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date FirstDate = format.parse(tdayFormat);
                Date SecondDate = format.parse(ddayDate);
                assert FirstDate != null;
                long t1 = FirstDate.getTime();
                assert SecondDate != null;
                long t2 = SecondDate.getTime();
                long calDate = t1 - t2;
                long calDateDays = calDate / (24 * 60 * 60 * 1000);
                if (calDateDays > 0) {
                    ddayTextView.setText(ddayName + " D+" + Math.abs(calDateDays));
                } else if (calDateDays == 0) {
                    ddayTextView.setText(ddayName + " D-DAY!");
                } else {
                    ddayTextView.setText(ddayName + " D-" + Math.abs(calDateDays));
                }

            } catch (ParseException e) {
                // 예외 처리
            }
        }

        /* FAB */
        studyFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoachingActivity.this, ResultActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        todoFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoachingActivity.this, EditTodayActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        diagnosisFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoachingActivity.this, DiagnosisActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        settingFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoachingActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        /* 스피너 */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /* MAIN FAB */
        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 열린 경우 닫기
                if (FABState.equals("true")) {
                    FABState = "false";
                    blurView.setVisibility(View.INVISIBLE);

                    studyFAB.startAnimation(fab_close);
                    diagnosisFAB.startAnimation(fab_close);
                    settingFAB.startAnimation(fab_close);
                    todoFAB.startAnimation(fab_close);

                    studyFAB.setEnabled(false);
                    studyFAB.setVisibility(View.INVISIBLE);
                    studyFABTextView.setVisibility(View.INVISIBLE);
                    todoFAB.setEnabled(false);
                    todoFAB.setVisibility(View.INVISIBLE);
                    todoFABTextView.setVisibility(View.INVISIBLE);
                    diagnosisFAB.setEnabled(false);
                    diagnosisFAB.setVisibility(View.INVISIBLE);
                    diagnosisFABTextView.setVisibility(View.INVISIBLE);
                    settingFAB.setEnabled(false);
                    settingFAB.setVisibility(View.INVISIBLE);
                    settingFABTextView.setVisibility(View.INVISIBLE);
                }
                else if (FABState.equals("long")){
                    FABState = "true";
                }
                // 닫힌 경우 코칭 페이지로
                else if (FABState.equals("false")){
                    Intent intent = new Intent(CoachingActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });

        mainFAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FABState = "long";

                blurView.setVisibility(View.VISIBLE);

                studyFAB.startAnimation(fab_open);
                diagnosisFAB.startAnimation(fab_open);
                settingFAB.startAnimation(fab_open);
                todoFAB.startAnimation(fab_open);

                studyFAB.setEnabled(true);
                studyFAB.setVisibility(View.VISIBLE);
                studyFABTextView.setVisibility(View.VISIBLE);
                todoFAB.setEnabled(true);
                todoFAB.setVisibility(View.VISIBLE);
                todoFABTextView.setVisibility(View.VISIBLE);
                diagnosisFAB.setEnabled(true);
                diagnosisFAB.setVisibility(View.VISIBLE);
                diagnosisFABTextView.setVisibility(View.VISIBLE);
                settingFAB.setEnabled(true);
                settingFAB.setVisibility(View.VISIBLE);
                settingFABTextView.setVisibility(View.VISIBLE);

                return false;
            }
        });
    }

    /* 디데이 설정 TextView 클릭 메소드 */
    public void onTextViewClicked(View view) {
        Intent intent = new Intent(CoachingActivity.this, DDayActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    /* 모의고사 클릭 시 */
    public void onExam1Clicked(View view){
        Intent intent = new Intent(CoachingActivity.this, ExamDetailActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    /* Blur View 클릭 메소드 (FAB 닫힘) */
    public void onBlurClicked(View view) {
        FABState = "false";
        blurView.setVisibility(View.INVISIBLE);

        studyFAB.startAnimation(fab_close);
        diagnosisFAB.startAnimation(fab_close);
        settingFAB.startAnimation(fab_close);
        todoFAB.startAnimation(fab_close);

        studyFAB.setEnabled(false);
        studyFAB.setVisibility(View.INVISIBLE);
        studyFABTextView.setVisibility(View.INVISIBLE);
        todoFAB.setEnabled(false);
        todoFAB.setVisibility(View.INVISIBLE);
        todoFABTextView.setVisibility(View.INVISIBLE);
        diagnosisFAB.setEnabled(false);
        diagnosisFAB.setVisibility(View.INVISIBLE);
        diagnosisFABTextView.setVisibility(View.INVISIBLE);
        settingFAB.setEnabled(false);
        settingFAB.setVisibility(View.INVISIBLE);
        settingFABTextView.setVisibility(View.INVISIBLE);
    }
}