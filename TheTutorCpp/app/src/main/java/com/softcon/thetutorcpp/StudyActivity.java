package com.softcon.thetutorcpp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class StudyActivity extends AppCompatActivity {

    /* 속담 */
    static String[] statements = {"배움의 길은 끝이 없다", "공부에는 특별한 비결이 없다. 오로지 성실하게 노력하는 길 밖에 없다", "노력해야만 수확이 있다.", "1년의 계획은 봄에 있고 평생의 계획은 근면함에 있다."};

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private long totalTime;
    private String StartTime;
    private int nowTime, restLeftTime;

    /* 전역변수 */
    private Button pauseButton;
    private TextView alertTextView;
    private TextView totalTimeTextView;
    private TextView subTimeTextView;
    private int subj;
    private TextView subNameTextView;

    private boolean isStop, playBoolean, noti;

    /* 몰입모드 */
    private View decorView;
    private int uiOption;

    private int numSubData;
    private String nameDataString;
    private String[] nameData = new String[10];

    private String UserEmailString;

    private int HttpDownloadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String userTemp = pref.getString("user", "");

        HttpDownloadErrorCount = 0;

        /* AES 256 암호화 */
        try {
            assert userTemp != null;
            UserEmailString = AES256Chiper.AES_Encode(userTemp);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        // 과목명 표시
        Intent intent = getIntent();
        subj = Objects.requireNonNull(intent.getExtras()).getInt("subject", 0);

        /* 초기화 */
        new Thread() {
            public void run() {
                HttpPostData_SUB_DATA();
            }
        }.start();

        /* 화면 꺼짐 방지 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_study);

        /* 몰입 모드 (하단 소프트바 숨기기) */
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        /* 인스턴스화 */
        final Button modeButton = findViewById(R.id.Studying_Button_PhoneMode);
        pauseButton = findViewById(R.id.Studying_Button_PauseStudy);
        Button finishButton = findViewById(R.id.Studying_Button_FinishStudy);

        TextView statementTextView = findViewById(R.id.Studying_TextView_statement);
        alertTextView = findViewById(R.id.Studying_TextView_Alert);
        totalTimeTextView = findViewById(R.id.Studying_TextView_totalTime);
        subTimeTextView = findViewById(R.id.Studying_TextView_subTime);
        subNameTextView = findViewById(R.id.Studying_TextView_subName);

        ConstraintLayout CoverView = findViewById(R.id.StudyActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(StudyActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        /* 초기화 */
        // 총 공부시간 불러오기
        SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        totalTime = prefTotal.getLong("totalTime", 0);
        isStop = playBoolean = noti = false;
        statementTextView.setText(statements[(int) (Math.random() * 4)]);

        // 타이머 시작
        mHandler.sendEmptyMessage(0);

        // 공부 시작 시간 측정
        Date today = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        StartTime = format.format(today);

        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playBoolean) {
                    modeButton.setBackground(ContextCompat.getDrawable(StudyActivity.this, R.drawable.basic_button_darkgray));
                    modeButton.setText("휴대폰 사용하기");
                    Toast.makeText(getApplicationContext(), "열공 모드로 전환되었습니다.", Toast.LENGTH_LONG).show();
                    removeNotification(1);
                } else {
                    modeButton.setBackground(ContextCompat.getDrawable(StudyActivity.this, R.drawable.basic_button_gray));
                    modeButton.setText("열공 모드로 돌아가기");
                    Toast.makeText(getApplicationContext(), "휴대폰 사용 모드입니다. 공부에 도움이 되는 앱을 사용해주세요.", Toast.LENGTH_LONG).show();
                    createNotification(1);
                }
                playBoolean = !playBoolean;
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStop = !isStop;
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeMessages(0);
                Intent intent = new Intent(getApplicationContext(), AfterStudyActivity.class);
                intent.putExtra("subject", subj);
                intent.putExtra("totalMin", nowTime / 60);
                intent.putExtra("totalSec", nowTime % 60);
                intent.putExtra("startTime", StartTime);
                removeNotification(1);
                removeNotification(2);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "비정상적으로 종료 시 공부시간이 기록되지 않을 수 있습니다. 마치기를 이용해주세요.", Toast.LENGTH_LONG).show();
    }

    private void createNotification(int id) {
        if (id == 1) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setContentTitle("휴대폰 사용 모드입니다.");
            builder.setContentText("열공 모드로 돌아가려면 앱을 실행해주세요.");

            builder.setAutoCancel(true);
            builder.setOngoing(true);

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            notificationManager.notify(id, builder.build());
        } else if (id == 2) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setContentTitle("열공 모드 실행중입니다.");
            builder.setContentText("공부 시간 측정이 일시정지 되었습니다.");

            builder.setAutoCancel(true);
            builder.setOngoing(true);

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            notificationManager.notify(id, builder.build());
        }
    }

    private void removeNotification(int id) {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(id);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            if (!playBoolean) {
                if (isAppIsInBackground(StudyActivity.this)) {
                    if (!noti) {
                        createNotification(2);
                        noti = true;
                    }
                    restLeftTime = 60;
                    isStop = true;
                }
            }

            if (!isStop) {
                if (noti) {
                    removeNotification(2);
                    noti = false;
                }
                nowTime++;
                long nowTotal = totalTime + (nowTime / 60);
                restLeftTime = 60;
                pauseButton.setText("공부 일시정지");
                if (nowTime / 60 == 0) {
                    subTimeTextView.setText(nowTime % 60 + "초");
                } else {
                    subTimeTextView.setText(nowTime / 60 + "분 " + nowTime % 60 + "초");
                }

                if (nowTotal / 60 == 0) {
                    totalTimeTextView.setText(nowTotal % 60 + "분");
                } else {
                    totalTimeTextView.setText(nowTotal / 60 + "시간 " + (nowTotal % 60) + "분");
                }
                pauseButton.setBackground(ContextCompat.getDrawable(StudyActivity.this, R.drawable.basic_button_darkgray));
                alertTextView.setText("");
                mHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                if (!playBoolean) {
                    restLeftTime--;
                    alertTextView.setText(restLeftTime + "초에 재시작하지 않으면 공부가 종료됩니다.");
                }
                pauseButton.setText("공부 계속하기");
                pauseButton.setBackground(ContextCompat.getDrawable(StudyActivity.this, R.drawable.basic_button_gray));
                mHandler.sendEmptyMessageDelayed(0, 1000);
                if (restLeftTime <= 0) {
                    mHandler.removeMessages(0);
                    Intent intent = new Intent(getApplicationContext(), AfterStudyActivity.class);
                    intent.putExtra("subject", subj);
                    intent.putExtra("totalMin", nowTime / 60);
                    intent.putExtra("totalSec", nowTime % 60);
                    intent.putExtra("startTime", StartTime);
                    if (noti) {
                        removeNotification(2);
                        noti = false;
                    }
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        }
    };

    private boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++)
            if (procInfos.get(i).processName.equals(context.getPackageName())) {
                return true;
            }
        return false;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }
        return isInBackground;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            decorView.setSystemUiVisibility(uiOption);
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
                builder.append(str + "&");
            }
            String myResult = builder.toString();
            Log.i("GOT DATA", myResult);
            numSubData = Integer.parseInt(myResult.split("&")[0]);
            Log.i("NUMBER", Integer.toString(numSubData));

            nameDataString = myResult.split("&")[1];

            for (int j = 0; j < numSubData; j++) {
                nameData[j] = nameDataString.split("-")[j];
            }

            Message msg = writeHandler.obtainMessage();
            writeHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 5) {
                Intent intent = new Intent(StudyActivity.this, NoInternetActivity.class);
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

    @SuppressLint("HandlerLeak")
    Handler writeHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            subNameTextView.setText(nameData[subj]);
        }
    };
}