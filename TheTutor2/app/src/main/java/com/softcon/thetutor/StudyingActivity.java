package com.softcon.thetutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudyingActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private Button bt_pause, bt_play;
    private TextView total, left, subTime;
    private long totalTime;
    private int subj;
    private String Starttime;

    private int nowTime = 0;
    private int restLeftTime = 60;

    private boolean isstop;
    private boolean playBoolean;
    private boolean studyOrBreak;
    private int breakTime, studyTime;
    private TextView pauseAlert;
    private boolean noti;

    static String[] statements = {"배움의 길은 끝이 없다", "공부에는 특별한 비결이 없다. 오로지 성실하게 노력하는 길 밖에 없다", "노력해야만 수확이 있다.", "1년의 계획은 봄에 있고 평생의 계획은 근면함에 있다."};

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* 화면 꺼짐 방지 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_studying);

        /* 총 공부시간 불러오기 */
        SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        totalTime = prefTotal.getLong("totalTime", 0);

        /* 공부시간/쉬는시간 불러오기 */
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        studyTime = pref.getInt("studyTime", 50);
        breakTime = pref.getInt("restTime", 10);
        restLeftTime = 180;

        /* 공부시간 측정 */
        mHandler.sendEmptyMessage(0); // 앱 시작과 동시에 핸들러에 메세지 전달
        isstop = false;
        playBoolean = false;
        noti = false;

        /* 공부 시작 시간 */
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Starttime = format.format(today);

        /* 링크 */
        TextView studyment = (TextView)findViewById(R.id.statement);
        studyment.setText(statements[(int)(Math.random() * 4)]);

        TextView subName = (TextView)findViewById(R.id.textview_subjectname);

        Button bt_exit = (Button)findViewById(R.id.button_exit);
        bt_pause = (Button)findViewById(R.id.button_pauseandstart);
        bt_play = (Button)findViewById(R.id.button_playMode);

        total = (TextView)findViewById(R.id.textview_totalstudytime);
        subTime = (TextView)findViewById(R.id.textview_subjecttime);
        left = (TextView)findViewById(R.id.textview_lefttime);

        pauseAlert = (TextView)findViewById(R.id.pauseAlert);

        /* 하단 바 숨기기(몰입 모드) */
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Intent intent = getIntent();
        subj = intent.getExtras().getInt("subject");
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

        /* 다른 앱 모드 */
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playBoolean) {
                    playBoolean = true;
                    bt_play.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_gray));
                    bt_play.setText("열공 모드로 돌아가기");
                    Toast.makeText(getApplicationContext(), "휴대폰 사용 모드입니다. 공부에 도움이 되는 앱을 사용해주세요.", Toast.LENGTH_LONG).show();
                    createNotification(1);
                }
                else {
                    playBoolean = false;
                    bt_play.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_shape));
                    bt_play.setText("휴대폰 사용하기");
                    Toast.makeText(getApplicationContext(), "열공 모드로 전환되었습니다.", Toast.LENGTH_LONG).show();
                    removeNotification(1);
                }
            }
        });

        /* 공부 종료 버튼 */
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeMessages(0);
                Intent intent = new Intent(getApplicationContext(), AfterStudyActivity.class);
                intent.putExtra("subject", subj);
                intent.putExtra("total", 1);
                intent.putExtra("startTime", Starttime);
                removeNotification(1);
                removeNotification(2);
                startActivity(intent);
                finish();
            }
        });

        /* 공부 일시정지 버튼 */
        bt_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isstop==false) {
                    isstop = true;
                }
                else {
                    isstop = false;
                }
            }
        });

    }

    private void createNotification(int id) {
        if(id == 1){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

            builder.setSmallIcon(R.drawable.ic_main_nobackground);
            builder.setContentTitle("휴대폰 사용 모드입니다.");
            builder.setContentText("열공 모드로 돌아가려면 앱을 실행해주세요");
            // 사용자가 탭을 클릭하면 자동 제거
            builder.setAutoCancel(true);
            builder.setOngoing(true);

            // 알림 표시
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }

            // id값은
            // 정의해야하는 각 알림의 고유한 int값
            notificationManager.notify(id, builder.build());
        }
        else if(id == 2){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

            builder.setSmallIcon(R.drawable.ic_main_nobackground);
            builder.setContentTitle("열공 모드 실행중입니다.");
            builder.setContentText("1분 내 재시작하지 않으면 자동으로 측정이 종료됩니다.");
            // 사용자가 탭을 클릭하면 자동 제거
            builder.setAutoCancel(true);
            builder.setOngoing(true);

            // 알림 표시
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            // id값은
            // 정의해야하는 각 알림의 고유한 int값
            notificationManager.notify(id, builder.build());
        }

    }

    private void removeNotification(int id) {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(id);
    }

    @Override public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "비정상적으로 종료 시 공부시간이 기록되지 않을 수 있습니다. 마치기를 이용해주세요.", Toast.LENGTH_LONG).show();
    }

    Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            if(!playBoolean){
                if(isAppIsInBackground(StudyingActivity.this)){
                    if(!noti){
                        System.out.println("noti!");
                        createNotification(2);
                        noti = true;
                    }
                    isstop = true;
                }
            }

            System.out.println(isAppIsInBackground(StudyingActivity.this));
            if(!isstop) {
                if(noti){
                    removeNotification(2);
                    noti = false;
                }
                nowTime++;
                restLeftTime = 60;
                long nowTotal = totalTime + (nowTime / 60);
                bt_pause.setText("공부 일시정지");
                if(nowTime / 60 == 0){
                    subTime.setText(Integer.toString(nowTime % 60) + "초");
                }
                else{
                    subTime.setText(Integer.toString(nowTime /60) + "분 " + nowTime % 60 + "초");
                }

                if(nowTotal / 60 == 0){
                    total.setText(Long.toString(nowTotal % 60) + "분");
                }
                else{
                    total.setText(Long.toString(nowTotal / 60) + "시간 " + (nowTotal % 60) + "분");
                }

                left.setText(((studyTime*60 - nowTime))/60 + "분");
                bt_pause.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_shape));
                pauseAlert.setText("");
                mHandler.sendEmptyMessageDelayed(0, 1000);
                if(nowTime <= 0){
                    //
                }
            }
            else {
                restLeftTime--;
                pauseAlert.setText(restLeftTime + "초에 재시작하지 않으면 공부가 종료됩니다.");
                bt_pause.setText("공부 계속하기");
                left.setText(((studyTime * 60 - nowTime)) / 60 + "분");
                bt_pause.setBackground(ContextCompat.getDrawable(StudyingActivity.this, R.drawable.basic_button_gray));
                //stopHandler.sendEmptyMessage(0);
                mHandler.sendEmptyMessageDelayed(0, 1000);
                if(restLeftTime <= 0){
                    mHandler.removeMessages(0);
                    Intent intent = new Intent(getApplicationContext(), AfterStudyActivity.class);
                    intent.putExtra("subject", subj);
                    intent.putExtra("total", 1);
                    intent.putExtra("startTime", Starttime);
                    startActivity(intent);
                    if(noti){
                        removeNotification(2);
                        noti = false;
                    }
                    finish();
                }
            }
        }
    };

    private boolean isAppRunning(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++){
            if(procInfos.get(i).processName.equals(context.getPackageName())){
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