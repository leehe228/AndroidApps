package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.github.mmin18.widget.RealtimeBlurView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;


public class NewGalleryActivity extends BaseActivity implements OnGestureListener {

    // 제스처
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureScanner;

    /* 몰입모드 */
    private View decorView;
    private int uiOption;

    // 저장한 전시회 Flag
    private boolean SAVED;

    // 화면 회전 FLAG
    private boolean SCREEN_ROTATION;

    // 정보 표시 FLAG
    private boolean INFO_FLAG;
    private boolean PLAY_FLAG;
    private boolean REMOTE_FLAG;
    private boolean BGM_PLAY_FLAG;

    //
    private RealtimeBlurView mainBlurView, backgroundBlurView;
    private LinearLayout mainInfoView, backgroundInfoView;
    private ImageView mainImageView, backgroundImageView;
    private TextView titleTextView;
    private TextView contentTextView;

    private TextView infoTitleTextView, infoCreatorTextView, infoContentTextView, infoCategoryTextView;

    // 애니메이션
    private Animation slideUpAnimation;
    private Animation fade_in, fade_out;

    // 이미지 List
    private ArrayList<Bitmap> IMAGES;
    private ArrayList<String> TITLES;
    private ArrayList<String> CONTENTS;

    // 이미지 index
    private int INDEX, MAX_INDEX;

    // 건너뛰기 버튼
    private Handler leftHandler, rightHandler;

    LinearLayout remoteController;
    LinearLayout remoteController2;

    // TTS
    private String nowToRead;
    private int NUMBER;

    // BGM
    private static MediaPlayer mediaPlayer;
    private ImageButton bgmButton;

    // 갤러리 객체
    Gallery gallery;

    // 배경화면
    Bitmap backgroundImage = null;

    // 정보 표시 위젯
    LinearLayout informationLinearLayout;
    private Button enterButton;
    private Button enterVRButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gallery);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        /* 화면 꺼짐 방지 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_gallery);

        /* 몰입 모드 (하단 소프트바 숨기기) */
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // 인스턴스화
        enterButton = findViewById(R.id.Gallery_Button_enter);
        enterVRButton = findViewById(R.id.Gallery_Button_enterVR);

        gestureScanner = new GestureDetector(this);

        mainImageView = findViewById(R.id.Gallery_ImageView_main);

        ImageButton leftButton = findViewById(R.id.Gallery_ImageButton_left);
        ImageButton rightButton = findViewById(R.id.Gallery_ImageButton_right);
        ImageButton closeButton = findViewById(R.id.Gallery_ImageButton_close);
        final ImageButton infoButton = findViewById(R.id.Gallery_ImageButton_info);
        final ImageButton plusButton = findViewById(R.id.Gallery_ImageButton_plus);

        mainBlurView = findViewById(R.id.Gallery_blurView_main);
        mainInfoView = findViewById(R.id.Gallery_LinearLayout_info);

        remoteController = findViewById(R.id.Gallery_RemoteController);
        remoteController2 = findViewById(R.id.Gallery_RemoteController2);

        bgmButton = findViewById(R.id.Gallery_ImageButton_BGM);

        titleTextView = findViewById(R.id.Gallery_TextView_title);
        contentTextView = findViewById(R.id.Gallery_TextView_content);

        backgroundBlurView = findViewById(R.id.Gallery_BlurView_background);
        backgroundImageView = findViewById(R.id.Gallery_ImageView_background);

        infoTitleTextView = findViewById(R.id.Gallery_TextView_infoTitle);
        infoCreatorTextView = findViewById(R.id.Gallery_TextView_infoCreator);
        infoContentTextView = findViewById(R.id.Gallery_TextView_infoContent);
        infoCategoryTextView = findViewById(R.id.Gallery_TextView_infoCategory);

        // 초기화
        SAVED = false;
        INFO_FLAG = false;
        PLAY_FLAG = false;
        REMOTE_FLAG = false;
        BGM_PLAY_FLAG = false;

        // 갤러리 객체 생성
        Intent intent = getIntent();
        final String CODE = intent.getStringExtra("CODE");
        gallery = new Gallery(CODE);
        NUMBER = gallery.getNUMBER();

        // 인덱스 세팅
        INDEX = 0;

        // 애니메이션
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        // 빠른 전환 딜레이 (Milli Sec)
        final int FAST_CHANGE_DELAY = 150;

        // BGM
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm1_repeat);
        mediaPlayer.setLooping(true);

        IMAGES = new ArrayList<>();

        final String finalCODE = CODE;
        new Thread() {
            public void run() {
                try {
                    java.net.URL url = new java.net.URL( "http://141.164.40.63:8000/media/database/" + finalCODE + "/" + "1.jpg");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    backgroundImage = BitmapFactory.decodeStream(input);
                    Message msg = informationHandler.obtainMessage();
                    informationHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    backgroundImage = null;
                }
            }
        }.start();

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler leftHandler;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (leftHandler != null)
                            return true;
                        leftHandler = new Handler();
                        leftHandler.postDelayed(leftAction, FAST_CHANGE_DELAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (leftHandler == null) return true;
                        leftHandler.removeCallbacks(leftAction);
                        leftHandler = null;
                        break;
                }
                return false;
            }

            final Runnable leftAction = new Runnable() {
                @Override
                public void run() {

                    if (INDEX > 0) {
                        INDEX--;
                        Message msg = mHandler.obtainMessage();
                        mHandler.sendMessage(msg);
                        leftHandler.postDelayed(this, FAST_CHANGE_DELAY);
                    }
                }
            };
        });

        // Right Button
        rightButton.setOnTouchListener(new View.OnTouchListener() {

            private Handler rightHandler;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (rightHandler != null)
                            return true;
                        rightHandler = new Handler();
                        rightHandler.postDelayed(rightAction, FAST_CHANGE_DELAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (rightHandler == null) return true;
                        rightHandler.removeCallbacks(rightAction);
                        rightHandler = null;
                        break;
                }
                return false;
            }

            final Runnable rightAction = new Runnable() {
                @Override
                public void run() {

                    if (INDEX < MAX_INDEX - 1) {
                        INDEX++;
                        Message msg = mHandler.obtainMessage();
                        mHandler.sendMessage(msg);
                        rightHandler.postDelayed(this, FAST_CHANGE_DELAY);
                    }
                }
            };
        });

        // BGM 플레이
        bgmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BGM_PLAY_FLAG) {
                    bgmButton.setImageResource(R.drawable.start);
                    mediaPlayer.pause();
                    BGM_PLAY_FLAG = false;
                } else {
                    bgmButton.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    BGM_PLAY_FLAG = true;
                }
            }
        });

        // Close Button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BGM Kill
                mediaPlayer.stop();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        // Plus Button
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SAVED) {
                    plusButton.setImageResource(R.drawable.plus);
                    SAVED = false;
                } else {
                    plusButton.setImageResource(R.drawable.checked);
                    SAVED = true;
                }
            }
        });

        // 재생 button
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PLAY_FLAG) {
                    infoButton.setImageResource(R.drawable.start);
                    PLAY_FLAG = false;
                } else {
                    infoButton.setImageResource(R.drawable.pause);
                    PLAY_FLAG = true;
                }
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundImageView.setVisibility(View.INVISIBLE);
                infoTitleTextView.setVisibility(View.INVISIBLE);
                infoContentTextView.setVisibility(View.INVISIBLE);
                infoCreatorTextView.setVisibility(View.INVISIBLE);
                infoCategoryTextView.setVisibility(View.INVISIBLE);
                enterButton.setVisibility(View.INVISIBLE);
                enterVRButton.setVisibility(View.INVISIBLE);
                backgroundBlurView.setVisibility(View.INVISIBLE);
                backgroundInfoView.setVisibility(View.INVISIBLE);
            }
        });

        enterVRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    } // end of onCreate

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("onConfigurationChanged", "onConfigurationChanged");

        // 세로 전환 시
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("onConfigurationChanged", "Configuration.ORIENTATION_PORTRAIT");
            SCREEN_ROTATION = false;
        }

        // 가로 전환 시
        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("onConfigurationChanged", "Configuration.ORIENTATION_LANDSCAPE");
            decorView.setSystemUiVisibility(uiOption);
            SCREEN_ROTATION = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if(!REMOTE_FLAG){
            REMOTE_FLAG = true;
            Message msg = RCOnHandler.obtainMessage();
            RCOnHandler.sendMessage(msg);
        }
        return gestureScanner.onTouchEvent(me);
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void onBlurClicked(View view) {
        INFO_FLAG = false;
        mainInfoView.setAlpha(0f);
        mainInfoView.setVisibility(View.INVISIBLE);
        mainBlurView.setVisibility(View.INVISIBLE);
    }

    // Background -> Foreground 올라왔을 때 몰입 모드 재적용
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            decorView.setSystemUiVisibility(uiOption);
        } else{
            mediaPlayer.stop();
            bgmButton.setImageResource(R.drawable.start);
            PLAY_FLAG = false;
        }
    }

    // 초기 정보 표시 핸들러
    @SuppressLint("HandlerLeak")
    android.os.Handler informationHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            backgroundImageView.setImageBitmap(backgroundImage);
            infoTitleTextView.setText(gallery.getTITLE());
            infoContentTextView.setText(gallery.getINFORMATION());
            infoCreatorTextView.setText(gallery.getCREATOR());
            infoCategoryTextView.setText(gallery.getCATEGORY());
        }
    };

    // 사진 변경 핸들러
    @SuppressLint("HandlerLeak")
    android.os.Handler mHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            mainImageView.setImageBitmap(IMAGES.get(INDEX));
            titleTextView.setText(TITLES.get(INDEX));
            contentTextView.setText(CONTENTS.get(INDEX));
        }
    };

    // 리모컨 온
    @SuppressLint("HandlerLeak")
    android.os.Handler RCOnHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            remoteController.startAnimation(fade_in);
            remoteController2.startAnimation(fade_in);
            remoteController.setVisibility(View.VISIBLE);
            remoteController2.setVisibility(View.VISIBLE);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    remoteController.startAnimation(fade_out);
                    remoteController2.startAnimation(fade_out);
                    remoteController.setVisibility(View.INVISIBLE);
                    remoteController2.setVisibility(View.INVISIBLE);
                    REMOTE_FLAG = false;
                }
            }, 5000); // 0.5초후
        }
    };

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();

                if (INDEX == MAX_INDEX - 1) {
                    Toast.makeText(getApplicationContext(), "마지막 작품입니다", Toast.LENGTH_SHORT).show();
                }
                if (INDEX < MAX_INDEX - 1) {
                    INDEX++;
                }

                Message msg = mHandler.obtainMessage();
                mHandler.sendMessage(msg);
            }
            // left to right swipe
            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();

                if (INDEX == 0) {
                    Toast.makeText(getApplicationContext(), "첫 번째 작품입니다", Toast.LENGTH_SHORT).show();
                }
                if (INDEX > 0) {
                    INDEX--;
                }

                Message msg = mHandler.obtainMessage();
                mHandler.sendMessage(msg);
            }
            // down to up swipe
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getApplicationContext(), "Swipe up", Toast.LENGTH_SHORT).show();

                mainInfoView.setAlpha(1.0f);
                mainInfoView.startAnimation(slideUpAnimation);
                mainInfoView.setVisibility(View.VISIBLE);
                mainBlurView.setVisibility(View.VISIBLE);

                INFO_FLAG = true;
            }
            // up to down swipe
            if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getApplicationContext(), "Swipe down", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ignored) {

        }
        return true;
    }
}