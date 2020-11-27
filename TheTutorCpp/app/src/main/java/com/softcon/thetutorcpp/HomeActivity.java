package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mmin18.widget.RealtimeBlurView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

public class HomeActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String UserEmailString;

    private String FABState;
    private Animation fab_open, fab_close;

    // FAB
    private ImageButton studyFAB, diagnosisFAB, settingFAB, todoFAB;
    private TextView studyFABTextView, diagnosisFABTextView, settingFABTextView, todoFABTextView;

    private RealtimeBlurView blurView;

    private Button recommend1Button;
    private Button recommend2Button;

    /* 추천 공부 버튼 과목 배열 */
    private int[] recommendSub = new int[2];

    private int numTodoData;
    private String todoNamesString;
    private String[] nameData = new String[10];

    private View[] timeTableView;
    private String TIMETABLE;
    private String TIMETABLE_RECOMMEND;
    private TextView timetableTitle;

    // 달성률
    private TextView scoreTextView;

    private String CHECKBOX;

    //TO DO
    private View[] todoColorView;
    private TextView[] todoTextView;
    private CheckBox[] todoCheckBox;

    private int HttpDownloadErrorCount;

    //TIME TABLE TOUCH
    private LinearLayout TimeTableLinearLayout;

    private boolean TIMETABLEstate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* INIT SETTING */
        FABState = "false";
        fab_open = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fab_close);
        HttpDownloadErrorCount = 0;
        TIMETABLEstate = true;

        timeTableView = new View[144];
        todoCheckBox = new CheckBox[12];
        todoColorView = new View[12];
        todoTextView = new TextView[12];
        scoreTextView = findViewById(R.id.Main_TextView_Score);
        TimeTableLinearLayout = findViewById(R.id.Main_LinearLayout_CENTER1);
        timetableTitle = findViewById(R.id.Main_TimeTableTitle);

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String userTemp = pref.getString("user", "");
        //시간표
        TIMETABLE = pref.getString("TIMETABLE", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        CHECKBOX = pref.getString("CHECKBOX", "000000000000");
        TIMETABLE_RECOMMEND = pref.getString("TIMETABLE_RECOMMEND", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        /* AES 256 암호화 */
        try {
            assert userTemp != null;
            UserEmailString = AES256Chiper.AES_Encode(userTemp);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        /* 초기화 */
        new Thread() {
            public void run() {
                //HttpPostData_SUB_DATA();
                //HttpPostData_SUB_DATA2();
            }
        }.start();

        // 추천 과목 번호 //TODO
        recommendSub[0] = 0;
        recommendSub[1] = 1;

        /* 인스턴스화 */
        final TextView dateTextView = findViewById(R.id.Main_TextView_date);
        final TextView totalTimeTextView = findViewById(R.id.Main_TextView_total);
        final TextView ddayTextView = findViewById(R.id.Main_TextView_dday);

        blurView = findViewById(R.id.Main_blurView);

        recommend1Button = findViewById(R.id.Main_Button_recommend1);
        recommend2Button = findViewById(R.id.Main_Button_recommend2);
        Button seeMoreButton = findViewById(R.id.Main_Button_seeMore);

        LinearLayout alarmLinearLayout = findViewById(R.id.Main_LinearLayout_Alarm);
        Animation movingAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.translate);
        alarmLinearLayout.startAnimation(movingAnimation);

        // FAB
        ImageButton mainFAB = findViewById(R.id.FAB_main);
        studyFAB = findViewById(R.id.FAB_study);
        diagnosisFAB = findViewById(R.id.FAB_diagnosis);
        settingFAB = findViewById(R.id.FAB_setting);
        todoFAB = findViewById(R.id.FAB_todo);

        studyFABTextView = findViewById(R.id.FABTV_study);
        diagnosisFABTextView = findViewById(R.id.FABTV_diagnosis);
        settingFABTextView = findViewById(R.id.FABTV_setting);
        todoFABTextView = findViewById(R.id.FABTV_todo);

        Animation TextAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.text_up_little);
        recommend1Button.startAnimation(TextAnimation);
        recommend2Button.startAnimation(TextAnimation);
        seeMoreButton.startAnimation(TextAnimation);

        /* 메인 화면 onCreate 이전 단계 */

        /* 날짜 바뀜 체크 */
        long nowTime = System.currentTimeMillis();
        Date nowDate = new Date(nowTime);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat todayDate = new SimpleDateFormat("yyyyDDDkk");

        // 날짜 바뀜 체크용 전역변수
        String todayDateString = todayDate.format(nowDate);

        // 첫 접속 시
        SharedPreferences prefToday = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String recordLastDate = prefToday.getString("lastDate", "-1");

        assert recordLastDate != null;
        if (recordLastDate.equals("-1")) {
            SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefTotal.edit();
            editor.putLong("totalTime", 0);
            editor.putString("TIMETABLE", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            editor.putString("CHECKBOX", "000000000000");
            editor.putString("TIMETABLE_RECOMMEND", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            editor.putString("TODOTIME", "");
            editor.apply();
            //Intent intent = new Intent(HomeActivity.this, TodayActivity.class);
            //startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
        // 날짜가 바뀌었다면 (05:00 기준)
        else if (Integer.parseInt(todayDateString) - Integer.parseInt(recordLastDate) >= 100) {
            Log.i("log", "day changed!");
            SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefTotal.edit();
            editor.putLong("totalTime", 0);
            editor.putString("TIMETABLE", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            editor.putString("CHECKBOX", "000000000000");
            editor.putString("TIMETABLE_RECOMMEND", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            editor.putString("TODOTIME", "");
            editor.apply();
            //Intent intent = new Intent(HomeActivity.this, TodayActivity.class);
            //startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }

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

        /* 시간표 */
        //TEST
        TIMETABLE_RECOMMEND = "000000000000000000000000000AAAAAAA000000BBBB0000000CCCCCCCCCC00000000000DDDD00000000EEE0000FF0000FFFF0000000000000000000000000000000000000000000";

        // 시간표 ( 5:00 AM 기준)
        timeTableView[0] = findViewById(R.id.Home_5_1);
        timeTableView[1] = findViewById(R.id.Home_5_2);
        timeTableView[2] = findViewById(R.id.Home_5_3);
        timeTableView[3] = findViewById(R.id.Home_5_4);
        timeTableView[4] = findViewById(R.id.Home_5_5);
        timeTableView[5] = findViewById(R.id.Home_5_6);

        timeTableView[6] = findViewById(R.id.Home_6_1);
        timeTableView[7] = findViewById(R.id.Home_6_2);
        timeTableView[8] = findViewById(R.id.Home_6_3);
        timeTableView[9] = findViewById(R.id.Home_6_4);
        timeTableView[10] = findViewById(R.id.Home_6_5);
        timeTableView[11] = findViewById(R.id.Home_6_6);

        timeTableView[12] = findViewById(R.id.Home_7_1);
        timeTableView[13] = findViewById(R.id.Home_7_2);
        timeTableView[14] = findViewById(R.id.Home_7_3);
        timeTableView[15] = findViewById(R.id.Home_7_4);
        timeTableView[16] = findViewById(R.id.Home_7_5);
        timeTableView[17] = findViewById(R.id.Home_7_6);

        timeTableView[18] = findViewById(R.id.Home_8_1);
        timeTableView[19] = findViewById(R.id.Home_8_2);
        timeTableView[20] = findViewById(R.id.Home_8_3);
        timeTableView[21] = findViewById(R.id.Home_8_4);
        timeTableView[22] = findViewById(R.id.Home_8_5);
        timeTableView[23] = findViewById(R.id.Home_8_6);

        timeTableView[24] = findViewById(R.id.Home_9_1);
        timeTableView[25] = findViewById(R.id.Home_9_2);
        timeTableView[26] = findViewById(R.id.Home_9_3);
        timeTableView[27] = findViewById(R.id.Home_9_4);
        timeTableView[28] = findViewById(R.id.Home_9_5);
        timeTableView[29] = findViewById(R.id.Home_9_6);

        timeTableView[30] = findViewById(R.id.Home_10_1);
        timeTableView[31] = findViewById(R.id.Home_10_2);
        timeTableView[32] = findViewById(R.id.Home_10_3);
        timeTableView[33] = findViewById(R.id.Home_10_4);
        timeTableView[34] = findViewById(R.id.Home_10_5);
        timeTableView[35] = findViewById(R.id.Home_10_6);

        timeTableView[36] = findViewById(R.id.Home_11_1);
        timeTableView[37] = findViewById(R.id.Home_11_2);
        timeTableView[38] = findViewById(R.id.Home_11_3);
        timeTableView[39] = findViewById(R.id.Home_11_4);
        timeTableView[40] = findViewById(R.id.Home_11_5);
        timeTableView[41] = findViewById(R.id.Home_11_6);

        timeTableView[42] = findViewById(R.id.Home_12_1);
        timeTableView[43] = findViewById(R.id.Home_12_2);
        timeTableView[44] = findViewById(R.id.Home_12_3);
        timeTableView[45] = findViewById(R.id.Home_12_4);
        timeTableView[46] = findViewById(R.id.Home_12_5);
        timeTableView[47] = findViewById(R.id.Home_12_6);

        timeTableView[48] = findViewById(R.id.Home_13_1);
        timeTableView[49] = findViewById(R.id.Home_13_2);
        timeTableView[50] = findViewById(R.id.Home_13_3);
        timeTableView[51] = findViewById(R.id.Home_13_4);
        timeTableView[52] = findViewById(R.id.Home_13_5);
        timeTableView[53] = findViewById(R.id.Home_13_6);

        timeTableView[54] = findViewById(R.id.Home_14_1);
        timeTableView[55] = findViewById(R.id.Home_14_2);
        timeTableView[56] = findViewById(R.id.Home_14_3);
        timeTableView[57] = findViewById(R.id.Home_14_4);
        timeTableView[58] = findViewById(R.id.Home_14_5);
        timeTableView[59] = findViewById(R.id.Home_14_6);

        timeTableView[60] = findViewById(R.id.Home_15_1);
        timeTableView[61] = findViewById(R.id.Home_15_2);
        timeTableView[62] = findViewById(R.id.Home_15_3);
        timeTableView[63] = findViewById(R.id.Home_15_4);
        timeTableView[64] = findViewById(R.id.Home_15_5);
        timeTableView[65] = findViewById(R.id.Home_15_6);

        timeTableView[66] = findViewById(R.id.Home_16_1);
        timeTableView[67] = findViewById(R.id.Home_16_2);
        timeTableView[68] = findViewById(R.id.Home_16_3);
        timeTableView[69] = findViewById(R.id.Home_16_4);
        timeTableView[70] = findViewById(R.id.Home_16_5);
        timeTableView[71] = findViewById(R.id.Home_16_6);

        timeTableView[72] = findViewById(R.id.Home_17_1);
        timeTableView[73] = findViewById(R.id.Home_17_2);
        timeTableView[74] = findViewById(R.id.Home_17_3);
        timeTableView[75] = findViewById(R.id.Home_17_4);
        timeTableView[76] = findViewById(R.id.Home_17_5);
        timeTableView[77] = findViewById(R.id.Home_17_6);

        timeTableView[78] = findViewById(R.id.Home_18_1);
        timeTableView[79] = findViewById(R.id.Home_18_2);
        timeTableView[80] = findViewById(R.id.Home_18_3);
        timeTableView[81] = findViewById(R.id.Home_18_4);
        timeTableView[82] = findViewById(R.id.Home_18_5);
        timeTableView[83] = findViewById(R.id.Home_18_6);

        timeTableView[84] = findViewById(R.id.Home_19_1);
        timeTableView[85] = findViewById(R.id.Home_19_2);
        timeTableView[86] = findViewById(R.id.Home_19_3);
        timeTableView[87] = findViewById(R.id.Home_19_4);
        timeTableView[88] = findViewById(R.id.Home_19_5);
        timeTableView[89] = findViewById(R.id.Home_19_6);

        timeTableView[90] = findViewById(R.id.Home_20_1);
        timeTableView[91] = findViewById(R.id.Home_20_2);
        timeTableView[92] = findViewById(R.id.Home_20_3);
        timeTableView[93] = findViewById(R.id.Home_20_4);
        timeTableView[94] = findViewById(R.id.Home_20_5);
        timeTableView[95] = findViewById(R.id.Home_20_6);

        timeTableView[96] = findViewById(R.id.Home_21_1);
        timeTableView[97] = findViewById(R.id.Home_21_2);
        timeTableView[98] = findViewById(R.id.Home_21_3);
        timeTableView[99] = findViewById(R.id.Home_21_4);
        timeTableView[100] = findViewById(R.id.Home_21_5);
        timeTableView[101] = findViewById(R.id.Home_21_6);

        timeTableView[102] = findViewById(R.id.Home_22_1);
        timeTableView[103] = findViewById(R.id.Home_22_2);
        timeTableView[104] = findViewById(R.id.Home_22_3);
        timeTableView[105] = findViewById(R.id.Home_22_4);
        timeTableView[106] = findViewById(R.id.Home_22_5);
        timeTableView[107] = findViewById(R.id.Home_22_6);

        timeTableView[108] = findViewById(R.id.Home_23_1);
        timeTableView[109] = findViewById(R.id.Home_23_2);
        timeTableView[110] = findViewById(R.id.Home_23_3);
        timeTableView[111] = findViewById(R.id.Home_23_4);
        timeTableView[112] = findViewById(R.id.Home_23_5);
        timeTableView[113] = findViewById(R.id.Home_23_6);

        timeTableView[114] = findViewById(R.id.Home_24_1);
        timeTableView[115] = findViewById(R.id.Home_24_2);
        timeTableView[116] = findViewById(R.id.Home_24_3);
        timeTableView[117] = findViewById(R.id.Home_24_4);
        timeTableView[118] = findViewById(R.id.Home_24_5);
        timeTableView[119] = findViewById(R.id.Home_24_6);

        timeTableView[120] = findViewById(R.id.Home_1_1);
        timeTableView[121] = findViewById(R.id.Home_1_2);
        timeTableView[122] = findViewById(R.id.Home_1_3);
        timeTableView[123] = findViewById(R.id.Home_1_4);
        timeTableView[124] = findViewById(R.id.Home_1_5);
        timeTableView[125] = findViewById(R.id.Home_1_6);

        timeTableView[126] = findViewById(R.id.Home_2_1);
        timeTableView[127] = findViewById(R.id.Home_2_2);
        timeTableView[128] = findViewById(R.id.Home_2_3);
        timeTableView[129] = findViewById(R.id.Home_2_4);
        timeTableView[130] = findViewById(R.id.Home_2_5);
        timeTableView[131] = findViewById(R.id.Home_2_6);

        timeTableView[132] = findViewById(R.id.Home_3_1);
        timeTableView[133] = findViewById(R.id.Home_3_2);
        timeTableView[134] = findViewById(R.id.Home_3_3);
        timeTableView[135] = findViewById(R.id.Home_3_4);
        timeTableView[136] = findViewById(R.id.Home_3_5);
        timeTableView[137] = findViewById(R.id.Home_3_6);

        timeTableView[138] = findViewById(R.id.Home_4_1);
        timeTableView[139] = findViewById(R.id.Home_4_2);
        timeTableView[140] = findViewById(R.id.Home_4_3);
        timeTableView[141] = findViewById(R.id.Home_4_4);
        timeTableView[142] = findViewById(R.id.Home_4_5);
        timeTableView[143] = findViewById(R.id.Home_4_6);


        // TO DO 인스턴스
        todoColorView[0] = findViewById(R.id.Home_TODO_view1);
        todoColorView[1] = findViewById(R.id.Home_TODO_view2);
        todoColorView[2] = findViewById(R.id.Home_TODO_view3);
        todoColorView[3] = findViewById(R.id.Home_TODO_view4);
        todoColorView[4] = findViewById(R.id.Home_TODO_view5);
        todoColorView[5] = findViewById(R.id.Home_TODO_view6);
        todoColorView[6] = findViewById(R.id.Home_TODO_view7);
        todoColorView[7] = findViewById(R.id.Home_TODO_view8);
        todoColorView[8] = findViewById(R.id.Home_TODO_view9);
        todoColorView[9] = findViewById(R.id.Home_TODO_view10);
        todoColorView[10] = findViewById(R.id.Home_TODO_view11);
        todoColorView[11] = findViewById(R.id.Home_TODO_view12);

        todoTextView[0] = findViewById(R.id.Home_TODO_TextView1);
        todoTextView[1] = findViewById(R.id.Home_TODO_TextView2);
        todoTextView[2] = findViewById(R.id.Home_TODO_TextView3);
        todoTextView[3] = findViewById(R.id.Home_TODO_TextView4);
        todoTextView[4] = findViewById(R.id.Home_TODO_TextView5);
        todoTextView[5] = findViewById(R.id.Home_TODO_TextView6);
        todoTextView[6] = findViewById(R.id.Home_TODO_TextView7);
        todoTextView[7] = findViewById(R.id.Home_TODO_TextView8);
        todoTextView[8] = findViewById(R.id.Home_TODO_TextView9);
        todoTextView[9] = findViewById(R.id.Home_TODO_TextView10);
        todoTextView[10] = findViewById(R.id.Home_TODO_TextView11);
        todoTextView[11] = findViewById(R.id.Home_TODO_TextView12);

        todoCheckBox[0] = findViewById(R.id.Home_TODO_Checkbox1);
        todoCheckBox[1] = findViewById(R.id.Home_TODO_Checkbox2);
        todoCheckBox[2] = findViewById(R.id.Home_TODO_Checkbox3);
        todoCheckBox[3] = findViewById(R.id.Home_TODO_Checkbox4);
        todoCheckBox[4] = findViewById(R.id.Home_TODO_Checkbox5);
        todoCheckBox[5] = findViewById(R.id.Home_TODO_Checkbox6);
        todoCheckBox[6] = findViewById(R.id.Home_TODO_Checkbox7);
        todoCheckBox[7] = findViewById(R.id.Home_TODO_Checkbox8);
        todoCheckBox[8] = findViewById(R.id.Home_TODO_Checkbox9);
        todoCheckBox[9] = findViewById(R.id.Home_TODO_Checkbox10);
        todoCheckBox[10] = findViewById(R.id.Home_TODO_Checkbox11);
        todoCheckBox[11] = findViewById(R.id.Home_TODO_Checkbox12);

        // 체크박스
        for (int i = 0; i < 12; i++) {
            char temp = CHECKBOX.charAt(i);
            if (temp == '0') {
                todoCheckBox[i].setChecked(false);
            } else if (temp == '1') {
                todoCheckBox[i].setChecked(true);
            }
        }

        // 체크박스 저장
        todoCheckBox[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[0] = '1';
                } else {
                    CHECKBOKX_CHAR[0] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[1] = '1';
                } else {
                    CHECKBOKX_CHAR[1] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[2] = '1';
                } else {
                    CHECKBOKX_CHAR[2] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[3] = '1';
                } else {
                    CHECKBOKX_CHAR[3] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[4] = '1';
                } else {
                    CHECKBOKX_CHAR[4] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[5] = '1';
                } else {
                    CHECKBOKX_CHAR[5] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[6] = '1';
                } else {
                    CHECKBOKX_CHAR[6] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[7] = '1';
                } else {
                    CHECKBOKX_CHAR[7] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[8].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[8] = '1';
                } else {
                    CHECKBOKX_CHAR[8] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) sum / (float) numTodoData * 100.0) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }
                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[9].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[9] = '1';
                } else {
                    CHECKBOKX_CHAR[9] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) (sum) / (float) (numTodoData) * 100) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[10].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[10] = '1';
                } else {
                    CHECKBOKX_CHAR[10] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) (sum) / (float) (numTodoData) * 100) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 체크박스 저장
        todoCheckBox[11].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                char[] CHECKBOKX_CHAR = CHECKBOX.toCharArray();
                if (b) {
                    CHECKBOKX_CHAR[11] = '1';
                } else {
                    CHECKBOKX_CHAR[11] = '0';
                }
                // 달성율
                int sum = 0;
                for (int j = 0; j < numTodoData; j++) {
                    if (CHECKBOKX_CHAR[j] == '1') sum += 1;
                }
                scoreTextView.setText("달성률 " + (int) ((float) (sum) / (float) (numTodoData) * 100) + "%");
                if (sum == numTodoData) {
                    scoreTextView.setTextColor(Color.parseColor("#2FC644"));
                } else {
                    scoreTextView.setTextColor(Color.parseColor("#000000"));
                }

                CHECKBOX = String.valueOf(CHECKBOKX_CHAR);
                SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = pref2.edit();
                editor2.putString("CHECKBOX", CHECKBOX);
                editor2.apply();
            }
        });

        // 시간표 색칠
        Message msg = draw1Handler.obtainMessage();
        draw1Handler.sendMessage(msg);

        /* 추천 과목 세팅 */


        /* 초기 세팅 완료 */


        /* 공부 시작 버튼 */
        recommend1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, StudyActivity.class);
                intent.putExtra("subject", recommendSub[0]);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        recommend2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, StudyActivity.class);
                intent.putExtra("subject", recommendSub[1]);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        /* 과목 더보기 */
        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SeeMoreActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        /* FAB */
        studyFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ResultActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        todoFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EditTodayActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        diagnosisFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DiagnosisActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        settingFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
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
                /*else if (FABState.equals("long")){
                    FABState = "true";
                }
                // 닫힌 경우 코칭 페이지로
                else if (FABState.equals("false")){
                    Intent intent = new Intent(HomeActivity.this, CoachingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }*/
                else{
                    FABState = "true";

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
                }
            }
        });

        /*mainFAB.setOnLongClickListener(new View.OnLongClickListener() {
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
        });*/
    }

    /* 디데이 설정 TextView 클릭 메소드 */
    public void onTextViewClicked(View view) {
        Intent intent = new Intent(HomeActivity.this, DDayActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    /* TIME TABLE 전환 */
    public void onTimeTableClicked(View view) {
        Handler mHandler = new Handler();
        if (TIMETABLEstate) {
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    Message msg = draw2Handler.obtainMessage();
                    draw2Handler.sendMessage(msg);
                }
            }, 100);

        } else {
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    Message msg = draw1Handler.obtainMessage();
                    draw1Handler.sendMessage(msg);
                }
            }, 100);

        }
        TIMETABLEstate = !TIMETABLEstate;

        Animation switchingMotion1 = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.slide_over);
        final Animation switchingMotion2 = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.slide_in);
        TimeTableLinearLayout.startAnimation(switchingMotion1);

        Handler m2Handler = new Handler();
        m2Handler.postDelayed(new Runnable() {
            public void run() {
                TimeTableLinearLayout.startAnimation(switchingMotion2);
            }
        }, 50);
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

    @SuppressLint("SetTextI18n")
    public void HttpPostData_SUB_DATA() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/loadSub/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str).append("&");
            }
            String myResult = builder.toString();
            Log.i("GOT DATA", myResult);
            int numSubData = Integer.parseInt(myResult.split("&")[0]);
            Log.i("NUMBER", Integer.toString(numSubData));

            String nameDataString = myResult.split("&")[1];

            for (int j = 0; j < numSubData; j++) {
                nameData[j] = nameDataString.split("-")[j];
            }
            String temp1 = "추천 : " + nameData[recommendSub[0]];
            String temp2 = "추천 : " + nameData[recommendSub[1]];
            recommend1Button.setText(temp1);
            recommend2Button.setText(temp2);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(HomeActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpDownloadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_SUB_DATA();
                }
            }.start();
        }
    }

    @SuppressLint("SetTextI18n")
    public void HttpPostData_SUB_DATA2() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/loadTodo/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str).append("&");
            }
            String myResult = builder.toString();
            Log.i("GOT DATA2", myResult);
            numTodoData = Integer.parseInt(myResult.split("&")[0]);
            Log.i("NUMBER2", Integer.toString(numTodoData));

            todoNamesString = myResult.split("&")[1];

            Message msg = initHandler.obtainMessage();
            initHandler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(HomeActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpDownloadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_SUB_DATA2();
                }
            }.start();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler initHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            for (int j = 0; j < numTodoData; j++) {
                todoTextView[j].setText(nameData[Integer.parseInt(todoNamesString.split("-")[j].split(":")[0])] + " " + todoNamesString.split("-")[j].split(":")[1]);
                todoCheckBox[j].setVisibility(View.VISIBLE);
                switch (todoNamesString.split("-")[j].split(":")[0]) {
                    case "0": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub0));
                        break;
                    }
                    case "1": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub1));
                        break;
                    }
                    case "2": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub2));
                        break;
                    }
                    case "3": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub3));
                        break;
                    }
                    case "4": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub4));
                        break;
                    }
                    case "5": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub5));
                        break;
                    }
                    case "6": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub6));
                        break;
                    }
                    case "7": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub7));
                        break;
                    }
                    case "8": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub8));
                        break;
                    }
                    case "9": {
                        todoColorView[j].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub9));
                        break;
                    }
                }
            }

            char[] CHECKBOX_TEMP = CHECKBOX.toCharArray();
            // 달성율
            int sum_temp = 0;
            for (int j = 0; j < numTodoData; j++) {
                if (CHECKBOX_TEMP[j] == '1') sum_temp += 1;
            }
            scoreTextView.setText("달성률 " + (int) ((float) (sum_temp) / (float) (numTodoData) * 100) + "%");
            if ((int) ((float) (sum_temp) / (float) (numTodoData) * 100) == 100) {
                scoreTextView.setTextColor(Color.parseColor("#2FC644"));
            } else {
                scoreTextView.setTextColor(Color.parseColor("#000000"));
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler draw1Handler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            timetableTitle.setText("TIME TABLE");
            for (int i = 0; i < 144; i++) {
                assert TIMETABLE != null;
                char temp = TIMETABLE.charAt(i);
                switch (temp) {
                    case 'A': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub0));
                        break;
                    }
                    case 'B': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub1));
                        break;
                    }
                    case 'C': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub2));
                        break;
                    }
                    case 'D': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub3));
                        break;
                    }
                    case 'E': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub4));
                        break;
                    }
                    case 'F': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub5));
                        break;
                    }
                    case 'G': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub6));
                        break;
                    }
                    case 'H': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub7));
                        break;
                    }
                    case 'I': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub8));
                        break;
                    }
                    case 'J': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub9));
                        break;
                    }
                    default: {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.table));
                    }
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler draw2Handler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            timetableTitle.setText("추천 시간표");
            for (int i = 0; i < 144; i++) {
                assert TIMETABLE_RECOMMEND != null;
                char temp = TIMETABLE_RECOMMEND.charAt(i);
                switch (temp) {
                    case 'A': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub0));
                        break;
                    }
                    case 'B': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub1));
                        break;
                    }
                    case 'C': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub2));
                        break;
                    }
                    case 'D': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub3));
                        break;
                    }
                    case 'E': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub4));
                        break;
                    }
                    case 'F': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub5));
                        break;
                    }
                    case 'G': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub6));
                        break;
                    }
                    case 'H': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub7));
                        break;
                    }
                    case 'I': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub8));
                        break;
                    }
                    case 'J': {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.sub9));
                        break;
                    }
                    default: {
                        timeTableView[i].setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.table));
                    }
                }
            }
        }
    };
}