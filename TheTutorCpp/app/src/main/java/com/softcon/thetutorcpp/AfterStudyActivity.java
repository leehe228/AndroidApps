package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AfterStudyActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private int studyCon;
    private String UserEmailString;
    private int subjectInt, totalTimeMinInt;
    private String startHour, startMin, endHour, endMin;

    private String[] nameData = new String[10];

    private String subTimeString, dayTimeString;

    private TextView subjectTextView;

    private int HttpDownloadErrorCount, HttpUploadErrorCount;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_study);

        HttpDownloadErrorCount = HttpUploadErrorCount = 0;

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String userEmailStringTemp = pref.getString("user", "");
        String TIMETABLE = pref.getString("TIMETABLE", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        /* AES 256 암호화 */
        try {
            assert userEmailStringTemp != null;
            UserEmailString = AES256Chiper.AES_Encode(userEmailStringTemp);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        /* 초기화 */
        new Thread() {
            public void run() {
                HttpPostData_SUB_DATA();
                HttpPostData_GET_TIME();
            }
        }.start();

        /* 인스턴스화 */
        subjectTextView = findViewById(R.id.AfterStudying_TextView_subName);
        TextView minTextView = findViewById(R.id.AfterStudying_TextView_studyTime);
        TextView studyTimeTextView = findViewById(R.id.AfterStudying_TextView_time);
        RatingBar ratingBar = findViewById(R.id.AfterStudying_ratingBar);
        final Button okButton = findViewById(R.id.AfterStudying_Button_done);
        TextView text1TextView = findViewById(R.id.AfterStudying_TextView_text1);
        TextView text2TextView = findViewById(R.id.AfterStudying_TextView_text2);

        Intent intent = getIntent();
        subjectInt = Objects.requireNonNull(intent.getExtras()).getInt("subject");
        totalTimeMinInt = Objects.requireNonNull(intent.getExtras()).getInt("totalMin");
        final int totalTimeSecInt = intent.getExtras().getInt("totalSec");
        String startTimeString = intent.getExtras().getString("startTime");

        /* 애니메이션 */
        Animation TextAnimation = AnimationUtils.loadAnimation(AfterStudyActivity.this, R.anim.text_up_little);
        subjectTextView.startAnimation(TextAnimation);
        minTextView.startAnimation(TextAnimation);
        studyTimeTextView.startAnimation(TextAnimation);
        ratingBar.startAnimation(TextAnimation);
        text1TextView.startAnimation(TextAnimation);
        text2TextView.startAnimation(TextAnimation);

        ConstraintLayout CoverView = findViewById(R.id.AfterStudyActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(AfterStudyActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        if (totalTimeMinInt < 5) {
            ratingBar.setVisibility(View.INVISIBLE);
            ratingBar.setEnabled(false);
            text2TextView.setText("5분 미만의 공부는 반영되지 않습니다.");
            okButton.setEnabled(true);
            okButton.setBackground(ContextCompat.getDrawable(AfterStudyActivity.this, R.drawable.basic_button));
        } else {
            okButton.setEnabled(false);
        }

        Date today = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String endTimeString = format.format(today);

        if (totalTimeMinInt == 0) {
            minTextView.setText(totalTimeSecInt + "초");
        } else {
            minTextView.setText(totalTimeMinInt + "분 " + totalTimeSecInt + "초");
        }

        studyTimeTextView.setText(startTimeString + " ~ " + endTimeString);
        assert startTimeString != null;
        startHour = startTimeString.substring(0, 2);
        startMin = startTimeString.substring(3, 5);
        endHour = endTimeString.substring(0, 2);
        endMin = endTimeString.substring(3, 5);

        /* 시간표 체크 */
        int startHourToCheck, startMinToCheck, endHourToCheck, endMinToCheck, startPoint, endPoint;
        int temp;

        temp = Integer.parseInt(startHour) - 5;
        if (temp < 0) temp += 24;
        startHourToCheck = temp;

        startMinToCheck = Integer.parseInt(startMin) / 10;

        temp = Integer.parseInt(endHour) - 5;
        if (temp < 0) temp += 24;
        endHourToCheck = temp;

        endMinToCheck = Integer.parseInt(endMin) / 10;

        startPoint = startHourToCheck * 6 + startMinToCheck;
        endPoint = endHourToCheck * 6 + endMinToCheck;

        assert TIMETABLE != null;
        char[] TIMETABLE_CHAR = TIMETABLE.toCharArray();
        for (int i = startPoint; i <= endPoint; i++) {
            TIMETABLE_CHAR[i] = (char) ('A' + subjectInt);
        }
        TIMETABLE = String.valueOf(TIMETABLE_CHAR);

        if (totalTimeMinInt >= 5) {
            /* 시간표 체크 */
            SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor2 = pref2.edit();
            editor2.putString("TIMETABLE", TIMETABLE);
            editor2.apply();
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                studyCon = (int) (v * 2);
                if (studyCon == 0) {
                    okButton.setEnabled(false);
                    okButton.setBackground(ContextCompat.getDrawable(AfterStudyActivity.this, R.drawable.basic_button_unclick));
                } else {
                    okButton.setEnabled(true);
                    okButton.setBackground(ContextCompat.getDrawable(AfterStudyActivity.this, R.drawable.basic_button));
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalTimeMinInt < 5) {
                    Intent intent = new Intent(AfterStudyActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }

                if (studyCon != 0) {
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    long totalTimeInt = pref.getLong("totalTime", 0) + totalTimeMinInt;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putLong("totalTime", totalTimeInt);
                    editor.apply();

                    if (totalTimeMinInt >= 5) {
                        new Thread() {
                            public void run() {
                                HttpPostData();
                                HttpPostData_UPDATE_TIME();
                            }
                        }.start();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "공부 평가 후 확인 버튼을 눌러주세요", Toast.LENGTH_LONG).show();
    }

    /* 서버 학습 데이터 */
    public void HttpPostData() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/learn/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString + "&" + "subject" + "=" + subjectInt + "&" + "startHour" + "=" + startHour + "&" + "startMin" + "=" + startMin;
            buffer = buffer + "&" + "endHour" + "=" + endHour + "&" + "endMin" + "=" + endMin + "&" + "score" + "=" + (studyCon - 1);
            writer.write(buffer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(AfterStudyActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpUploadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData();
                }
            }.start();
        }
    }

    // 공부 데이터 업로드
    public void HttpPostData_UPDATE_TIME() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/updateTime/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString + "&" + "subTime" + "=" + subTimeString + "&" + "dayTime" + "=" + dayTimeString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            Intent intent = new Intent(AfterStudyActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(AfterStudyActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpUploadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_UPDATE_TIME();
                }
            }.start();
        }
    }

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
            int numSubData = Integer.parseInt(myResult.split("&")[0]);
            String nameDataString = myResult.split("&")[1];
            for (int j = 0; j < numSubData; j++) {
                nameData[j] = nameDataString.split("-")[j];
            }
            Message msg = writeHandler.obtainMessage();
            writeHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(AfterStudyActivity.this, NoInternetActivity.class);
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

    /* 사용자 공부 시간 다운로드 */
    public void HttpPostData_GET_TIME() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/userTime/");
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
            subTimeString = myResult.split("&")[2];
            dayTimeString = myResult.split("&")[1];

            Message msg = weekHandler.obtainMessage();
            weekHandler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(AfterStudyActivity.this, NoInternetActivity.class);
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

    /* 과목 이름 화면 표시 핸들러 */
    @SuppressLint("HandlerLeak")
    Handler writeHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            subjectTextView.setText(nameData[subjectInt]);
        }
    };

    /* 사용자 공부 시간 업데이트 후 업로드 */
    @SuppressLint("HandlerLeak")
    Handler weekHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            String[] subSplited = subTimeString.split("-");
            String[] daySplited = dayTimeString.split("-");

            subSplited[subjectInt] = Integer.toString(Integer.parseInt(subSplited[subjectInt]) + totalTimeMinInt);

            Date todayDate = new Date();
            @SuppressLint("SimpleDateFormat") DateFormat weekFormat = new SimpleDateFormat("E");
            String week = weekFormat.format(todayDate);

            switch (week) {
                case "Mon":
                case "월":
                    daySplited[0] = Integer.toString(Integer.parseInt(daySplited[0]) + totalTimeMinInt);
                    break;
                case "Tue":
                case "화":
                    daySplited[1] = Integer.toString(Integer.parseInt(daySplited[1]) + totalTimeMinInt);
                    break;
                case "Wed":
                case "수":
                    daySplited[2] = Integer.toString(Integer.parseInt(daySplited[2]) + totalTimeMinInt);
                    break;
                case "Thu":
                case "목":
                    daySplited[3] = Integer.toString(Integer.parseInt(daySplited[3]) + totalTimeMinInt);
                    break;
                case "Fri":
                case "금":
                    daySplited[4] = Integer.toString(Integer.parseInt(daySplited[4]) + totalTimeMinInt);
                    break;
                case "Sat":
                case "토":
                    daySplited[5] = Integer.toString(Integer.parseInt(daySplited[5]) + totalTimeMinInt);
                    break;
                case "Sun":
                case "일":
                    daySplited[6] = Integer.toString(Integer.parseInt(daySplited[6]) + totalTimeMinInt);
                    break;
            }

            subTimeString = subSplited[0] + "-" + subSplited[1] + "-" + subSplited[2] + "-" + subSplited[3] + "-" + subSplited[4] + "-" + subSplited[5];
            subTimeString = subTimeString + "-" + subSplited[6] + "-" + subSplited[7] + "-" + subSplited[8] + "-" + subSplited[9];
            dayTimeString = daySplited[0] + "-" + daySplited[1] + "-" + daySplited[2] + "-" + daySplited[3] + "-" + daySplited[4] + "-" + daySplited[5] + "-" + daySplited[6];
        }
    };


}