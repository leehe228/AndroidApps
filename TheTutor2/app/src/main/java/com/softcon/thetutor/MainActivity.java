package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mmin18.widget.RealtimeBlurView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";
    private String tday;
    private String temp;
    private long calDateDays;

    private int fabState;

    private Animation fab_open, fab_close;

    private TextView[][] timeTable = new TextView[24][6];

    private String NowYearDayHourString;
    private String LastYearDayHourString;
    private String LastYearDayString;

    /* notification */
    private String isNoti;
    private String notiTitle;
    private String notiContent;
    private String notiDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Intent init_intent = getIntent();
        String initValue = init_intent.getExtras().getString("mainOpenType", "0");
        if(initValue.equals("noti")){
            /* 메인화면 전 이벤트 */
            /* 이벤트 노티피케이션 이벤트 */
            SharedPreferences prefNoti = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            String todayDate = prefNoti.getString("notiDenyDate", "00000000");
            Intent getintent = getIntent();
            isNoti = getintent.getExtras().getString("isNoti", "0");
            notiTitle = getintent.getExtras().getString("notiTitle", "0");
            notiContent = getintent.getExtras().getString("notiContent", "0");
            notiDate = getintent.getExtras().getString("notiDate", "00000000");

            Log.i("notiDenyDate", todayDate);
            Log.i("notiDate", notiDate);
            if(isNoti.equals("1")){
                if(todayDate.equals(notiDate)){
                    //
                }
                else{
                    if(isNoti.equals("1")){
                        Intent notiintent = new Intent(getApplicationContext(), EventNotificationActivity.class);
                        notiintent.putExtra("notiDate", notiDate);
                        notiintent.putExtra("notiTitle", notiTitle);
                        notiintent.putExtra("notiContent", notiContent);
                        startActivity(notiintent);
                    }
                }
            }
        }
        else if(initValue.equals("dday")){

        }
        else{

        }

        /* 현재 시간 가져오기 */
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat NowYearDayHour = new SimpleDateFormat("yyyyDDDkk");
        SimpleDateFormat LastYearDay = new SimpleDateFormat("yyyyDDD");
        NowYearDayHourString = NowYearDayHour.format(date);
        LastYearDayString = LastYearDay.format(date);
        System.out.println(NowYearDayHourString);

        /* 날짜 바뀌면 입력받기 */
        SharedPreferences prefToday = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String LastYearDayHourString = prefToday.getString("lastDate", "-1");
        if(LastYearDayHourString.equals("-1")){
            LastYearDayHourString = NowYearDayHourString;
        }

        System.out.println(NowYearDayHourString + "/" + LastYearDayHourString);

        /* 날이 바뀔 때 */
        if(Integer.parseInt(NowYearDayHourString) - Integer.parseInt(LastYearDayHourString) >= 100){
            System.out.println("Date Changed!");
            System.out.println(Integer.parseInt(NowYearDayHourString) - Integer.parseInt(LastYearDayHourString));
            Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
            startActivity(intent);
            //Shared Preference
            SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefTotal.edit();
            editor.putLong("totalTime", 0);
            editor.commit();
        }

        //Shared Preference
        SharedPreferences prefTime = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefTime.edit();
        editor.putString("lastDate", LastYearDayString + "05");
        editor.commit();

        /* 전체 공부 시간 가져오기 */
        SharedPreferences prefTotal2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        long totalTime = prefTotal2.getLong("totalTime", 0);
        TextView tv_time = (TextView)findViewById(R.id.textview_time);
        tv_time.setText(Long.toString(totalTime / 60) + "H " + Long.toString(totalTime % 60) + "M");

        /* TIME TABLE */
        TextView t5_1 = (TextView)findViewById(R.id.timetable_5_1);
        TextView t5_2 = (TextView)findViewById(R.id.timetable_5_2);
        TextView t5_3 = (TextView)findViewById(R.id.timetable_5_3);
        TextView t5_4 = (TextView)findViewById(R.id.timetable_5_4);
        TextView t5_5 = (TextView)findViewById(R.id.timetable_5_5);
        TextView t5_6 = (TextView)findViewById(R.id.timetable_5_6);

        TextView t6_1 = (TextView)findViewById(R.id.timetable_6_1);
        TextView t6_2 = (TextView)findViewById(R.id.timetable_6_2);
        TextView t6_3 = (TextView)findViewById(R.id.timetable_6_3);
        TextView t6_4 = (TextView)findViewById(R.id.timetable_6_4);
        TextView t6_5 = (TextView)findViewById(R.id.timetable_6_5);
        TextView t6_6 = (TextView)findViewById(R.id.timetable_6_6);

        TextView t7_1 = (TextView)findViewById(R.id.timetable_7_1);
        TextView t7_2 = (TextView)findViewById(R.id.timetable_7_2);
        TextView t7_3 = (TextView)findViewById(R.id.timetable_7_3);
        TextView t7_4 = (TextView)findViewById(R.id.timetable_7_4);
        TextView t7_5 = (TextView)findViewById(R.id.timetable_7_5);
        TextView t7_6 = (TextView)findViewById(R.id.timetable_7_6);

        TextView t8_1 = (TextView)findViewById(R.id.timetable_8_1);
        TextView t8_2 = (TextView)findViewById(R.id.timetable_8_2);
        TextView t8_3 = (TextView)findViewById(R.id.timetable_8_3);
        TextView t8_4 = (TextView)findViewById(R.id.timetable_8_4);
        TextView t8_5 = (TextView)findViewById(R.id.timetable_8_5);
        TextView t8_6 = (TextView)findViewById(R.id.timetable_8_6);

        TextView t9_1 = (TextView)findViewById(R.id.timetable_9_1);
        TextView t9_2 = (TextView)findViewById(R.id.timetable_9_2);
        TextView t9_3 = (TextView)findViewById(R.id.timetable_9_3);
        TextView t9_4 = (TextView)findViewById(R.id.timetable_9_4);
        TextView t9_5 = (TextView)findViewById(R.id.timetable_9_5);
        TextView t9_6 = (TextView)findViewById(R.id.timetable_9_6);

        TextView t10_1 = (TextView)findViewById(R.id.timetable_10_1);
        TextView t10_2 = (TextView)findViewById(R.id.timetable_10_2);
        TextView t10_3 = (TextView)findViewById(R.id.timetable_10_3);
        TextView t10_4 = (TextView)findViewById(R.id.timetable_10_4);
        TextView t10_5 = (TextView)findViewById(R.id.timetable_10_5);
        TextView t10_6 = (TextView)findViewById(R.id.timetable_10_6);

        TextView t11_1 = (TextView)findViewById(R.id.timetable_11_1);
        TextView t11_2 = (TextView)findViewById(R.id.timetable_11_2);
        TextView t11_3 = (TextView)findViewById(R.id.timetable_11_3);
        TextView t11_4 = (TextView)findViewById(R.id.timetable_11_4);
        TextView t11_5 = (TextView)findViewById(R.id.timetable_11_5);
        TextView t11_6 = (TextView)findViewById(R.id.timetable_11_6);

        TextView t12_1 = (TextView)findViewById(R.id.timetable_12_1);
        TextView t12_2 = (TextView)findViewById(R.id.timetable_12_2);
        TextView t12_3 = (TextView)findViewById(R.id.timetable_12_3);
        TextView t12_4 = (TextView)findViewById(R.id.timetable_12_4);
        TextView t12_5 = (TextView)findViewById(R.id.timetable_12_5);
        TextView t12_6 = (TextView)findViewById(R.id.timetable_12_6);

        TextView t13_1 = (TextView)findViewById(R.id.timetable_13_1);
        TextView t13_2 = (TextView)findViewById(R.id.timetable_13_2);
        TextView t13_3 = (TextView)findViewById(R.id.timetable_13_3);
        TextView t13_4 = (TextView)findViewById(R.id.timetable_13_4);
        TextView t13_5 = (TextView)findViewById(R.id.timetable_13_5);
        TextView t13_6 = (TextView)findViewById(R.id.timetable_13_6);

        TextView t14_1 = (TextView)findViewById(R.id.timetable_14_1);
        TextView t14_2 = (TextView)findViewById(R.id.timetable_14_2);
        TextView t14_3 = (TextView)findViewById(R.id.timetable_14_3);
        TextView t14_4 = (TextView)findViewById(R.id.timetable_14_4);
        TextView t14_5 = (TextView)findViewById(R.id.timetable_14_5);
        TextView t14_6 = (TextView)findViewById(R.id.timetable_14_6);

        TextView t15_1 = (TextView)findViewById(R.id.timetable_15_1);
        TextView t15_2 = (TextView)findViewById(R.id.timetable_15_2);
        TextView t15_3 = (TextView)findViewById(R.id.timetable_15_3);
        TextView t15_4 = (TextView)findViewById(R.id.timetable_15_4);
        TextView t15_5 = (TextView)findViewById(R.id.timetable_15_5);
        TextView t15_6 = (TextView)findViewById(R.id.timetable_15_6);

        TextView t16_1 = (TextView)findViewById(R.id.timetable_16_1);
        TextView t16_2 = (TextView)findViewById(R.id.timetable_16_2);
        TextView t16_3 = (TextView)findViewById(R.id.timetable_16_3);
        TextView t16_4 = (TextView)findViewById(R.id.timetable_16_4);
        TextView t16_5 = (TextView)findViewById(R.id.timetable_16_5);
        TextView t16_6 = (TextView)findViewById(R.id.timetable_16_6);

        TextView t17_1 = (TextView)findViewById(R.id.timetable_17_1);
        TextView t17_2 = (TextView)findViewById(R.id.timetable_17_2);
        TextView t17_3 = (TextView)findViewById(R.id.timetable_17_3);
        TextView t17_4 = (TextView)findViewById(R.id.timetable_17_4);
        TextView t17_5 = (TextView)findViewById(R.id.timetable_17_5);
        TextView t17_6 = (TextView)findViewById(R.id.timetable_17_6);

        TextView t18_1 = (TextView)findViewById(R.id.timetable_18_1);
        TextView t18_2 = (TextView)findViewById(R.id.timetable_18_2);
        TextView t18_3 = (TextView)findViewById(R.id.timetable_18_3);
        TextView t18_4 = (TextView)findViewById(R.id.timetable_18_4);
        TextView t18_5 = (TextView)findViewById(R.id.timetable_18_5);
        TextView t18_6 = (TextView)findViewById(R.id.timetable_18_6);

        TextView t19_1 = (TextView)findViewById(R.id.timetable_19_1);
        TextView t19_2 = (TextView)findViewById(R.id.timetable_19_2);
        TextView t19_3 = (TextView)findViewById(R.id.timetable_19_3);
        TextView t19_4 = (TextView)findViewById(R.id.timetable_19_4);
        TextView t19_5 = (TextView)findViewById(R.id.timetable_19_5);
        TextView t19_6 = (TextView)findViewById(R.id.timetable_19_6);

        TextView t20_1 = (TextView)findViewById(R.id.timetable_20_1);
        TextView t20_2 = (TextView)findViewById(R.id.timetable_20_2);
        TextView t20_3 = (TextView)findViewById(R.id.timetable_20_3);
        TextView t20_4 = (TextView)findViewById(R.id.timetable_20_4);
        TextView t20_5 = (TextView)findViewById(R.id.timetable_20_5);
        TextView t20_6 = (TextView)findViewById(R.id.timetable_20_6);

        TextView t21_1 = (TextView)findViewById(R.id.timetable_21_1);
        TextView t21_2 = (TextView)findViewById(R.id.timetable_21_2);
        TextView t21_3 = (TextView)findViewById(R.id.timetable_21_3);
        TextView t21_4 = (TextView)findViewById(R.id.timetable_21_4);
        TextView t21_5 = (TextView)findViewById(R.id.timetable_21_5);
        TextView t21_6 = (TextView)findViewById(R.id.timetable_21_6);

        TextView t22_1 = (TextView)findViewById(R.id.timetable_22_1);
        TextView t22_2 = (TextView)findViewById(R.id.timetable_22_2);
        TextView t22_3 = (TextView)findViewById(R.id.timetable_22_3);
        TextView t22_4 = (TextView)findViewById(R.id.timetable_22_4);
        TextView t22_5 = (TextView)findViewById(R.id.timetable_22_5);
        TextView t22_6 = (TextView)findViewById(R.id.timetable_22_6);

        TextView t23_1 = (TextView)findViewById(R.id.timetable_23_1);
        TextView t23_2 = (TextView)findViewById(R.id.timetable_23_2);
        TextView t23_3 = (TextView)findViewById(R.id.timetable_23_3);
        TextView t23_4 = (TextView)findViewById(R.id.timetable_23_4);
        TextView t23_5 = (TextView)findViewById(R.id.timetable_23_5);
        TextView t23_6 = (TextView)findViewById(R.id.timetable_23_6);

        TextView t0_1 = (TextView)findViewById(R.id.timetable_0_1);
        TextView t0_2 = (TextView)findViewById(R.id.timetable_0_2);
        TextView t0_3 = (TextView)findViewById(R.id.timetable_0_3);
        TextView t0_4 = (TextView)findViewById(R.id.timetable_0_4);
        TextView t0_5 = (TextView)findViewById(R.id.timetable_0_5);
        TextView t0_6 = (TextView)findViewById(R.id.timetable_0_6);

        TextView t1_1 = (TextView)findViewById(R.id.timetable_1_1);
        TextView t1_2 = (TextView)findViewById(R.id.timetable_1_2);
        TextView t1_3 = (TextView)findViewById(R.id.timetable_1_3);
        TextView t1_4 = (TextView)findViewById(R.id.timetable_1_4);
        TextView t1_5 = (TextView)findViewById(R.id.timetable_1_5);
        TextView t1_6 = (TextView)findViewById(R.id.timetable_1_6);

        TextView t2_1 = (TextView)findViewById(R.id.timetable_2_1);
        TextView t2_2 = (TextView)findViewById(R.id.timetable_2_2);
        TextView t2_3 = (TextView)findViewById(R.id.timetable_2_3);
        TextView t2_4 = (TextView)findViewById(R.id.timetable_2_4);
        TextView t2_5 = (TextView)findViewById(R.id.timetable_2_5);
        TextView t2_6 = (TextView)findViewById(R.id.timetable_2_6);

        TextView t3_1 = (TextView)findViewById(R.id.timetable_3_1);
        TextView t3_2 = (TextView)findViewById(R.id.timetable_3_2);
        TextView t3_3 = (TextView)findViewById(R.id.timetable_3_3);
        TextView t3_4 = (TextView)findViewById(R.id.timetable_3_4);
        TextView t3_5 = (TextView)findViewById(R.id.timetable_3_5);
        TextView t3_6 = (TextView)findViewById(R.id.timetable_3_6);

        TextView t4_1 = (TextView)findViewById(R.id.timetable_4_1);
        TextView t4_2 = (TextView)findViewById(R.id.timetable_4_2);
        TextView t4_3 = (TextView)findViewById(R.id.timetable_4_3);
        TextView t4_4 = (TextView)findViewById(R.id.timetable_4_4);
        TextView t4_5 = (TextView)findViewById(R.id.timetable_4_5);
        TextView t4_6 = (TextView)findViewById(R.id.timetable_4_6);

        t12_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
        t12_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
        t12_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
        t12_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
        t12_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
        t13_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
        t13_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));

        t15_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_lightgreen));
        t15_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_lightgreen));
        t16_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_lightgreen));
        t16_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_lightgreen));
        t16_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_lightgreen));
        t16_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_lightgreen));
        t16_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_lightgreen));

        /* FAB STATE INITIALIZING */
        fabState = 0;
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        /* FAB INSTANCE */
        final ImageButton FAB_main = (ImageButton)findViewById(R.id.FAB_main);
        final ImageButton FAB_timetable = (ImageButton)findViewById(R.id.FAB_timetable);
        final ImageButton FAB_todo = (ImageButton)findViewById(R.id.FAB_todo);
        final ImageButton FAB_study = (ImageButton)findViewById(R.id.FAB_study);
        final ImageButton FAB_setting = (ImageButton)findViewById(R.id.FAB_setting);

        /* FAB TEXTVIEW INSTANCE*/
        final TextView FABTV_timetable = (TextView)findViewById(R.id.FABTV_timetable);
        final TextView FABTV_todo = (TextView)findViewById(R.id.FABTV_todo);
        final TextView FABTV_study = (TextView)findViewById(R.id.FABTV_study);
        final TextView FABTV_setting = (TextView)findViewById(R.id.FABTV_setting);

        /* BLUR INSTANCE */
        final RealtimeBlurView blurview = (RealtimeBlurView)findViewById(R.id.blur);
        blurview.setVisibility(View.INVISIBLE);

        /* 유저 정보 가져오기 */
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String result = pref.getString("userid", "");

        /* 현재 날짜 불러오기 */
        TextView textview_date = (TextView)findViewById(R.id.textview_date);
        TextView textview_dday = (TextView)findViewById(R.id.textview_dday);
        TextView textview_time = (TextView)findViewById(R.id.textview_time);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
        String display = year + "년 " + month + "월 " + day + "일";
        tday = year + "-" + month + "-" + day;
        textview_date.setText(display);

        /* 디데이 세팅 */
        SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        temp = pref2.getString("dday", "exception");
        String ddayName = pref2.getString("ddayName", "여기를 눌러 설정하세요");

        if(temp.equals("exception")){
            textview_dday.setText("여기를 눌러 디데이 설정");
        }
        else{
            try{ // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
                Date FirstDate = format.parse(tday);
                Date SecondDate = format.parse(temp);

                // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
                // 연산결과 -950400000. long type 으로 return 된다.
                long t1 = FirstDate.getTime();
                long t2 = SecondDate.getTime();
                long calDate = t1 - t2;

                // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
                // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
                calDateDays = calDate / (24*60*60*1000);
                if(calDateDays > 0){
                    textview_dday.setText(ddayName + " D+" + Long.toString(Math.abs(calDateDays)));
                }
                else if(calDateDays == 0){
                    textview_dday.setText(ddayName + " D-DAY!");
                }
                else{
                    textview_dday.setText(ddayName + " D-" + Long.toString(Math.abs(calDateDays)));
                }

            }
            catch(ParseException e)
            {
                // 예외 처리
            }
        }

        /* 버튼 정보 링킹 */
        Button bt_seeMore = (Button)findViewById(R.id.button_seeMore);
        Button bt_recommend = (Button)findViewById(R.id.button_recommendSubject);
        Button bt_next = (Button)findViewById(R.id.button_nextSubject);

        /* 추천 공부 */
        bt_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudyingActivity.class);
                intent.putExtra("subject", 0);
                startActivity(intent);
            }
        });

        /* 다음 공부 (임시 구현) */
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudyingActivity.class);
                intent.putExtra("subject", 1);
                startActivity(intent);
            }
        });

        /* 더보기 */
        bt_seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SeeMoreActivity.class);
                startActivity(intent);
            }
        });

        /* FAB - MAIN EVENT*/
        FAB_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabState == 1){
                    fabState = 0;
                    blurview.setVisibility(View.INVISIBLE);
                    FAB_timetable.setVisibility(View.INVISIBLE);
                    FAB_todo.setVisibility(View.INVISIBLE);
                    FAB_study.setVisibility(View.INVISIBLE);
                    FAB_setting.setVisibility(View.INVISIBLE);
                    FABTV_timetable.setVisibility(View.INVISIBLE);
                    FABTV_todo.setVisibility(View.INVISIBLE);
                    FABTV_study.setVisibility(View.INVISIBLE);
                    FABTV_setting.setVisibility(View.INVISIBLE);

                    FAB_timetable.startAnimation(fab_close);
                    FAB_todo.startAnimation(fab_close);
                    FAB_study.startAnimation(fab_close);
                    FAB_setting.startAnimation(fab_close);
                }
                else if(fabState == 0){
                    fabState = 1;
                    blurview.setVisibility(View.VISIBLE);
                    FAB_timetable.setVisibility(View.VISIBLE);
                    FAB_todo.setVisibility(View.VISIBLE);
                    FAB_study.setVisibility(View.VISIBLE);
                    FAB_setting.setVisibility(View.VISIBLE);
                    FABTV_timetable.setVisibility(View.VISIBLE);
                    FABTV_todo.setVisibility(View.VISIBLE);
                    FABTV_study.setVisibility(View.VISIBLE);
                    FABTV_setting.setVisibility(View.VISIBLE);

                    FAB_timetable.startAnimation(fab_open);
                    FAB_todo.startAnimation(fab_open);
                    FAB_study.startAnimation(fab_open);
                    FAB_setting.startAnimation(fab_open);
                }
            }
        });

        /* FAB - TIME TABLE */
        FAB_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
                startActivity(intent);
            }
        });

        /* FAB - TO DO*/
        FAB_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
                startActivity(intent);
            }
        });

        /* FAB - STUDYING */
        FAB_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DiagnosisActivity.class);
                startActivity(intent);
            }
        });

        /* FAB - SETTING */
        FAB_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onTextViewClicked(View view){
        Intent intent = new Intent(getApplicationContext(), DdayActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBlurClicked(View view){
        if(fabState == 1){
            fabState = 0;

            /* FAB INSTANCE */
            final ImageButton FAB_main = (ImageButton)findViewById(R.id.FAB_main);
            final ImageButton FAB_timetable = (ImageButton)findViewById(R.id.FAB_timetable);
            final ImageButton FAB_todo = (ImageButton)findViewById(R.id.FAB_todo);
            final ImageButton FAB_study = (ImageButton)findViewById(R.id.FAB_study);
            final ImageButton FAB_setting = (ImageButton)findViewById(R.id.FAB_setting);

            /* FAB TEXTVIEW INSTANCE*/
            final TextView FABTV_timetable = (TextView)findViewById(R.id.FABTV_timetable);
            final TextView FABTV_todo = (TextView)findViewById(R.id.FABTV_todo);
            final TextView FABTV_study = (TextView)findViewById(R.id.FABTV_study);
            final TextView FABTV_setting = (TextView)findViewById(R.id.FABTV_setting);

            /* BLUR INSTANCE */
            final RealtimeBlurView blurview = (RealtimeBlurView)findViewById(R.id.blur);

            blurview.setVisibility(View.INVISIBLE);
            FAB_timetable.setVisibility(View.INVISIBLE);
            FAB_todo.setVisibility(View.INVISIBLE);
            FAB_study.setVisibility(View.INVISIBLE);
            FAB_setting.setVisibility(View.INVISIBLE);
            FABTV_timetable.setVisibility(View.INVISIBLE);
            FABTV_todo.setVisibility(View.INVISIBLE);
            FABTV_study.setVisibility(View.INVISIBLE);
            FABTV_setting.setVisibility(View.INVISIBLE);

            FAB_timetable.startAnimation(fab_close);
            FAB_todo.startAnimation(fab_close);
            FAB_study.startAnimation(fab_close);
            FAB_setting.startAnimation(fab_close);
        }
    }
}
