package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StudyingActivity extends AppCompatActivity {

    /* 속담 */
    static String[] statements = {"배움의 길은 끝이 없다", "공부에는 특별한 비결이 없다. 오로지 성실하게 노력하는 길 밖에 없다", "노력해야만 수확이 있다.", "1년의 계획은 봄에 있고 평생의 계획은 근면함에 있다."};

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private long totalTime;
    private String StartTime;
    private int nowTime, restLeftTime;

    /* 전역변수 */
    private Button pauseButton;
    private Button finishButton;
    private TextView alertTextView;
    private TextView totalTimeTextView;
    private TextView subTimeTextView;
    private int subj;

    private boolean isStop, playBoolean, noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_studying);

        /* 화면 꺼짐 방지 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_studying);

        /* 몰입 모드 (하단 소프트바 숨기기) */
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        /* 인스턴스화 */
        final Button modeButton = (Button) findViewById(R.id.Studying_Button_PhoneMode);
        pauseButton = (Button) findViewById(R.id.Studying_Button_PauseStudy);
        finishButton = (Button) findViewById(R.id.Studying_Button_FinishStudy);

        TextView statementTextView = (TextView) findViewById(R.id.Studying_TextView_statement);
        alertTextView = (TextView) findViewById(R.id.Studying_TextView_Alert);
        totalTimeTextView = (TextView) findViewById(R.id.Studying_TextView_totalTime);
        subTimeTextView = (TextView) findViewById(R.id.Studying_TextView_subTime);
        TextView subNameTextView = (TextView) findViewById(R.id.Studying_TextView_subName);

        /* 초기화 */
        // 총 공부시간 불러오기
        SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        totalTime = prefTotal.getLong("totalTime", 0);
        isStop = playBoolean = noti = false;

        // 타이머 시작
        mHandler.sendEmptyMessage(0);

        // 공부 시작 시간 측정
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        StartTime = format.format(today);

        // 과목명 표시
        Intent intent = getIntent();
        subj = intent.getExtras().getInt("subject");
        if (subj == 0) {
            subNameTextView.setText("국어");
        } else if (subj == 1) {
            subNameTextView.setText("수학");
        } else if (subj == 2) {
            subNameTextView.setText("사회/역사");
        } else if (subj == 3) {
            subNameTextView.setText("경제/경영");
        } else if (subj == 4) {
            subNameTextView.setText("기술/가정");
        } else if (subj == 5) {
            subNameTextView.setText("영어");
        } else if (subj == 6) {
            subNameTextView.setText("예술/체육");
        } else if (subj == 7) {
            subNameTextView.setText("한문/외국어");
        } else if (subj == 8) {
            subNameTextView.setText("교양");
        } else if (subj == 9) {
            subNameTextView.setText("기타");
        }

        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playBoolean) {
                    modeButton.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_darkgray));
                    modeButton.setText("휴대폰 사용하기");
                    Toast.makeText(getApplicationContext(), "열공 모드로 전환되었습니다.", Toast.LENGTH_LONG).show();
                    removeNotification(1);
                } else {
                    modeButton.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_gray));
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
                intent.putExtra("total", 1);
                intent.putExtra("startTime", StartTime);
                removeNotification(1);
                removeNotification(2);
                startActivity(intent);
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
            builder.setContentText("열공 모드로 돌아가려면 앱을 실행해주세요");

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
            builder.setContentText("1분 내 재시작하지 않으면 자동으로 측정이 종료됩니다.");

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

    Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            if (!playBoolean) {
                if (isAppIsInBackground(StudyingActivity.this)) {
                    if (!noti) {
                        System.out.println("noti!");
                        createNotification(2);
                        noti = true;
                    }
                    isStop = true;
                }
            }

            System.out.println(isAppIsInBackground(StudyingActivity.this));
            if (!isStop) {
                if (noti) {
                    removeNotification(2);
                    noti = false;
                }
                nowTime++;
                long nowTotal = totalTime + (nowTime / 60);
                pauseButton.setText("공부 일시정지");
                if (nowTime / 60 == 0) {
                    subTimeTextView.setText(Integer.toString(nowTime % 60) + "초");
                } else {
                    subTimeTextView.setText(Integer.toString(nowTime / 60) + "분 " + nowTime % 60 + "초");
                }

                if (nowTotal / 60 == 0) {
                    totalTimeTextView.setText(Long.toString(nowTotal % 60) + "분");
                } else {
                    totalTimeTextView.setText(Long.toString(nowTotal / 60) + "시간 " + (nowTotal % 60) + "분");
                }
                pauseButton.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_darkgray));
                alertTextView.setText("");
                mHandler.sendEmptyMessageDelayed(0, 1000);
                if (nowTime <= 0) {
                    //
                }
            } else {
                restLeftTime--;
                alertTextView.setText(restLeftTime + "초에 재시작하지 않으면 공부가 종료됩니다.");
                pauseButton.setText("공부 계속하기");
                pauseButton.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_gray));
                //stopHandler.sendEmptyMessage(0);
                mHandler.sendEmptyMessageDelayed(0, 1000);
                if (restLeftTime <= 0) {
                    mHandler.removeMessages(0);
                    Intent intent = new Intent(getApplicationContext(), AfterStudyActivity.class);
                    intent.putExtra("subject", subj);
                    intent.putExtra("total", 1);
                    intent.putExtra("startTime", StartTime);
                    startActivity(intent);
                    if (noti) {
                        removeNotification(2);
                        noti = false;
                    }
                    finish();
                }
            }
        }
    };

    private boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
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
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}