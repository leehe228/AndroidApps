package com.softcon.thetutorcpp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DiagnosisActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String[] getData = new String[3];
    private String UserNameString;
    private float[] subTime = new float[10];
    private float[] dayTime = new float[7];
    private String[] subTimeTemp = new String[10];
    private String[] dayTimeTemp = new String[7];

    private TextView userNameTextView;

    private String UserEmailString;

    // DATA
    private String[] nameData = new String[10];
    private int numSubData;
    private String nameDataString;

    /* CHART */
    private LineChart lineChart, lineChartSub;

    private LineChart chart;

    private int HttpDownloadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        HttpDownloadErrorCount = 0;

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String temp = pref.getString("user", "");
        /* AES 256 암호화 */
        try {
            assert temp != null;
            UserEmailString = AES256Chiper.AES_Encode(temp);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        new Thread() {
            public void run() {
                HttpPostData();
                HttpPostData_GET_DATA();
            }
        }.start();

        /* 인스턴스화 */
        ImageButton backButton = findViewById(R.id.Diagnosis_ImageButton_back);
        TextView dateTextView = findViewById(R.id.Diagnosis_TextView_NowDetail);
        userNameTextView = findViewById(R.id.Diagnosis_TextView_name);
        //lineChart = findViewById(R.id.Diagnosis_ChartDay);
        lineChartSub = findViewById(R.id.Diagnosis_ChartSub);

        ConstraintLayout CoverView = findViewById(R.id.DiagnosisActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(DiagnosisActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* 날짜 가져오기 */
        Date currentTime = Calendar.getInstance().getTime();
        String date_text = new SimpleDateFormat("MM월 W째주", Locale.getDefault()).format(currentTime);

        /* 차트 */
        chart = findViewById(R.id.linechart);
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val)); } LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        chart.setData(data);

        /* 화면 표시 세팅*/
        dateTextView.setText(date_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiagnosisActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DiagnosisActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    /* 데이터 받아오기 */
    public void HttpPostData() {
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
                builder.append(str).append("/");
            }
            String myResult = builder.toString();
            Log.i("USER TIME", myResult);
            getData = myResult.split("/");
            // 단계
            if (getData[0] == null) {
                UserNameString = "정보를 불러오지 못했습니다.";
            } else {
                UserNameString = getData[0] + "님의 공부 기록입니다.";
            }
            dayTimeTemp = getData[1].split("&");
            subTimeTemp = getData[2].split("&");

            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(DiagnosisActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpDownloadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData();
                }
            }.start();
        }
    }

    public void HttpPostData_GET_DATA() {
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
                builder.append(str + "&");
            }
            String myResult = builder.toString();
            Log.i("GOT DATA", myResult);
            numSubData = Integer.parseInt(myResult.split("&")[0]);
            Log.i("NUMBER", Integer.toString(numSubData));

            nameDataString = myResult.split("&")[1];
            Message msg = initHandler.obtainMessage();
            initHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            new Thread() {
                public void run() {
                    HttpPostData_GET_DATA();
                }
            }.start();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            userNameTextView.setText(UserNameString);
        }
    };

    /* 초기 화면 구성 */
    @SuppressLint("HandlerLeak")
    Handler initHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {

        }
    };
}