package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mmin18.widget.RealtimeBlurView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private boolean FABState;
    private Animation fab_open, fab_close;

    private ImageButton studyFAB;
    private ImageButton todoFAB;
    private ImageButton diagnosisFAB;
    private ImageButton settingFAB;

    private TextView studyFABTextView;
    private TextView todoFABTextView;
    private TextView diagnosisFABTextView;
    private TextView settingFABTextView;

    private RealtimeBlurView blurView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* INIT SETTING */
        FABState = false;
        fab_open = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_close);

        /* 인스턴스화 */
        final TextView dateTextView = (TextView) findViewById(R.id.Main_TextView_date);
        final TextView totalTimeTextView = (TextView) findViewById(R.id.Main_TextView_total);
        final TextView ddayTextView = (TextView) findViewById(R.id.Main_TextView_dday);

        blurView = (RealtimeBlurView) findViewById(R.id.Main_blurView);

        final Button recommend1Button = (Button) findViewById(R.id.Main_Button_recommend1);
        final Button recommend2Button = (Button) findViewById(R.id.Main_Button_recommend2);
        final Button seeMoreButton = (Button) findViewById(R.id.Main_Button_seeMore);

        // FAB
        ImageButton mainFAB = (ImageButton) findViewById(R.id.FAB_main);
        studyFAB = (ImageButton) findViewById(R.id.FAB_study);
        todoFAB = (ImageButton) findViewById(R.id.FAB_todo);
        diagnosisFAB = (ImageButton) findViewById(R.id.FAB_diagnosis);
        settingFAB = (ImageButton) findViewById(R.id.FAB_setting);

        studyFABTextView = (TextView) findViewById(R.id.FABTV_study);
        todoFABTextView = (TextView) findViewById(R.id.FABTV_todo);
        diagnosisFABTextView = (TextView) findViewById(R.id.FABTV_diagnosis);
        settingFABTextView = (TextView) findViewById(R.id.FABTV_setting);


        /* 메인 화면 onCreate 이전 단계 */

        /* 날짜 바뀜 체크 */
        long nowTime = System.currentTimeMillis();
        Date nowDate = new Date(nowTime);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat todayDate = new SimpleDateFormat("yyyyDDDkk");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat todayDateToRecord = new SimpleDateFormat("yyyyDDD");

        // 날짜 바뀜 체크용 전역변수
        String todayDateString = todayDate.format(nowDate);
        String todayDateToRecordString = todayDateToRecord.format(nowDate);

        // 첫 접속 시
        SharedPreferences prefToday = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String recordLastDate = prefToday.getString("lastDate", "-1");

        assert recordLastDate != null;
        if (recordLastDate.equals("-1")) {
            recordLastDate = todayDateString;
        }

        // 날짜가 바뀌었다면 (05:00 기준)
        if (Integer.parseInt(todayDateString) - Integer.parseInt(recordLastDate) >= 100) {
            Log.i("log", "day changed!");
            Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
            startActivity(intent);
            SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefTotal.edit();
            editor.putLong("totalTime", 0);
            editor.apply();
        }
        SharedPreferences prefTime = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefTime.edit();
        editor.putString("lastDate", todayDateToRecordString + "05");
        editor.apply();

        /* 전체 공부 시간 가져오기 */
        SharedPreferences prefTotal2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        long totalTime = prefTotal2.getLong("totalTime", 0);
        totalTimeTextView.setText(Long.toString(totalTime / 60) + "H " + Long.toString(totalTime % 60) + "M");

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
                    ddayTextView.setText(ddayName + " D+" + Long.toString(Math.abs(calDateDays)));
                } else if (calDateDays == 0) {
                    ddayTextView.setText(ddayName + " D-DAY!");
                } else {
                    ddayTextView.setText(ddayName + " D-" + Long.toString(Math.abs(calDateDays)));
                }

            } catch (ParseException e) {
                // 예외 처리
            }
        }


        /* 시간표 */


        /* TO DO */



        /* 초기 세팅 완료 */

        /* 공부 시작 버튼 */
        recommend1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 1);
                startActivity(intent);
                finish();
            }
        });

        recommend2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudyingActivity.class);
                intent.putExtra("subject", 1);
                startActivity(intent);
                finish();
            }
        });

        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SeeMoreActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /* FAB */
        studyFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        todoFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodayActivity.class);
                startActivity(intent);
            }
        });

        diagnosisFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiagnosisActivity.class);
                startActivity(intent);
            }
        });

        settingFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        /* MAIN FAB */
        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 열린 경우 닫기
                if (FABState) {
                    FABState = false;
                    blurView.setVisibility(View.INVISIBLE);
                    studyFAB.startAnimation(fab_close);
                    todoFAB.startAnimation(fab_close);
                    diagnosisFAB.startAnimation(fab_close);
                    settingFAB.startAnimation(fab_close);

                    studyFAB.setVisibility(View.INVISIBLE);
                    studyFABTextView.setVisibility(View.INVISIBLE);
                    todoFAB.setVisibility(View.INVISIBLE);
                    todoFABTextView.setVisibility(View.INVISIBLE);
                    diagnosisFAB.setVisibility(View.INVISIBLE);
                    diagnosisFABTextView.setVisibility(View.INVISIBLE);
                    settingFAB.setVisibility(View.INVISIBLE);
                    settingFABTextView.setVisibility(View.INVISIBLE);
                }
                // 닫힌 경우 열기
                else {
                    FABState = true;
                    blurView.setVisibility(View.VISIBLE);
                    studyFAB.startAnimation(fab_open);
                    todoFAB.startAnimation(fab_open);
                    diagnosisFAB.startAnimation(fab_open);
                    settingFAB.startAnimation(fab_open);

                    studyFAB.setVisibility(View.VISIBLE);
                    studyFABTextView.setVisibility(View.VISIBLE);
                    todoFAB.setVisibility(View.VISIBLE);
                    todoFABTextView.setVisibility(View.VISIBLE);
                    diagnosisFAB.setVisibility(View.VISIBLE);
                    diagnosisFABTextView.setVisibility(View.VISIBLE);
                    settingFAB.setVisibility(View.VISIBLE);
                    settingFABTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /* 디데이 설정 TextView 클릭 메소드 */
    public void onTextViewClicked(View view) {
        Intent intent = new Intent(MainActivity.this, PopDdayActivity.class);
        startActivity(intent);
        finish();
    }

    /* Blur View 클릭 메소드 (FAB 닫힘) */
    public void onBlurClicked(View view) {
        FABState = false;
        blurView.setVisibility(View.INVISIBLE);
        studyFAB.startAnimation(fab_close);
        todoFAB.startAnimation(fab_close);
        diagnosisFAB.startAnimation(fab_close);
        settingFAB.startAnimation(fab_close);

        studyFAB.setVisibility(View.INVISIBLE);
        studyFABTextView.setVisibility(View.INVISIBLE);
        todoFAB.setVisibility(View.INVISIBLE);
        todoFABTextView.setVisibility(View.INVISIBLE);
        diagnosisFAB.setVisibility(View.INVISIBLE);
        diagnosisFABTextView.setVisibility(View.INVISIBLE);
        settingFAB.setVisibility(View.INVISIBLE);
        settingFABTextView.setVisibility(View.INVISIBLE);
    }
}