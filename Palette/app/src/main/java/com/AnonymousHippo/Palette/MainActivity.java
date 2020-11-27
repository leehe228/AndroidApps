package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.mmin18.widget.RealtimeBlurView;
import me.relex.circleindicator.CircleIndicator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    // FAB State Flag
    private boolean FAB_FLAG;

    // FAB 버튼
    private ImageButton mainFAB;
    private ImageButton myFAB;
    private TextView myFABTextView;
    private ImageButton likeFAB;
    private TextView likeFABTextView;
    private ImageButton settingFAB;
    private TextView settingFABTextView;

    // Blur View
    private RealtimeBlurView mainBlurView;

    // FAB 애니메이션
    private Animation fab_open, fab_close;
    private Animation FABAnimation;

    // STT
    SpeechRecognizer mRecognizer;
    Intent sttIntent;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private ScrollView mainScrollView;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    //
    private ImageButton[] recImageList;

    private TextView[] recTextList;

    private Button[] moreButton;

    private final String[] keys = new String[10];
    private final String[] data = new String[10];
    private String result;
    private String userEmail;

    // 추천 리스트
    private ArrayList<String> recCodeList;
    private ArrayList<Gallery> recGalleryList;
    private ArrayList<Bitmap> recImageArrayList;
    private ArrayList<String> recCodeArrayList;

    /* Loading View */
    ImageView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 인스턴스화
        mainScrollView = findViewById(R.id.Main_ScrollView_main);
        mainBlurView = findViewById(R.id.Main_blurView);

        // FAB 인스턴스화
        mainFAB = findViewById(R.id.Main_FAB_main);
        myFAB = findViewById(R.id.Main_FAB_my);
        myFABTextView = findViewById(R.id.Main_FABText_my);
        likeFAB = findViewById(R.id.Main_FAB_like);
        likeFABTextView = findViewById(R.id.Main_FABText_like);
        settingFAB = findViewById(R.id.Main_FAB_setting);
        settingFABTextView = findViewById(R.id.Main_FABText_setting);
        Button searchButton = findViewById(R.id.Main_Button_search);

        mSwipeRefreshLayout = findViewById(R.id.Main_SwipeLayout_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        ViewPager mainViewPager = findViewById(R.id.Main_ViewPager_main);
        MainViewPagerAdapter scrollAdapter = new MainViewPagerAdapter(this);
        mainViewPager.setAdapter(scrollAdapter);
        CircleIndicator indicator = findViewById(R.id.Main_Indicator);
        indicator.setViewPager(mainViewPager);

        recImageList = new ImageButton[9];

        recImageList[0] = findViewById(R.id.Main_ImageView_Rec1);
        recImageList[1] = findViewById(R.id.Main_ImageView_Rec2);
        recImageList[2] = findViewById(R.id.Main_ImageView_Rec3);
        recImageList[3] = findViewById(R.id.Main_ImageView_Pop1);
        recImageList[4] = findViewById(R.id.Main_ImageView_Pop2);
        recImageList[5] = findViewById(R.id.Main_ImageView_Pop3);
        recImageList[6] = findViewById(R.id.Main_ImageView_New1);
        recImageList[7] = findViewById(R.id.Main_ImageView_New2);
        recImageList[8] = findViewById(R.id.Main_ImageView_New3);

        final int[] idList = new int[9];
        idList[0] = R.id.Main_ImageView_Rec1;
        idList[1] = R.id.Main_ImageView_Rec2;
        idList[2] = R.id.Main_ImageView_Rec3;
        idList[3] = R.id.Main_ImageView_Pop1;
        idList[4] = R.id.Main_ImageView_Pop2;
        idList[5] = R.id.Main_ImageView_Pop3;
        idList[6] = R.id.Main_ImageView_New1;
        idList[7] = R.id.Main_ImageView_New2;
        idList[8] = R.id.Main_ImageView_New3;

        recTextList = new TextView[9];

        recTextList[0] = findViewById(R.id.Main_TextView_Rec1);
        recTextList[1] = findViewById(R.id.Main_TextView_Rec2);
        recTextList[2] = findViewById(R.id.Main_TextView_Rec3);
        recTextList[3] = findViewById(R.id.Main_TextView_Pop1);
        recTextList[4] = findViewById(R.id.Main_TextView_Pop2);
        recTextList[5] = findViewById(R.id.Main_TextView_Pop3);
        recTextList[6] = findViewById(R.id.Main_TextView_New1);
        recTextList[7] = findViewById(R.id.Main_TextView_New2);
        recTextList[8] = findViewById(R.id.Main_TextView_New3);

        moreButton = new Button[3];

        moreButton[0] = findViewById(R.id.Main_Button_recMore);
        moreButton[1] = findViewById(R.id.Main_Button_popMore);
        moreButton[2] = findViewById(R.id.Main_Button_newMore);

        moreButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewSearchListActivity.class);
                intent.putExtra("openType", "interest");
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        moreButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewSearchListActivity.class);
                intent.putExtra("openType", "interest");
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        moreButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewSearchListActivity.class);
                intent.putExtra("openType", "interest");
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        ImageButton.OnClickListener onClickListener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("pushed", String.valueOf(v.getId()));
                for(int i = 0; i < 9; i++){
                    if(v.getId() == idList[i]){
                        Intent intent = new Intent(getApplicationContext(), GalleryInfoActivity.class);
                        intent.putExtra("CODE", recCodeArrayList.get(i));
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
            }
        };

        for(int i = 0; i < 9; i++){
            recImageList[i].setOnClickListener(onClickListener);
        }

        // 초기화
        FAB_FLAG = false;
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        recCodeList = new ArrayList<>();
        recGalleryList = new ArrayList<>();
        recImageArrayList = new ArrayList<>();
        recCodeArrayList = new ArrayList<>();

        // STT
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        // FAB 애니메이션
        /* 로고 애니메이션 */
        FABAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotating);
        FABAnimation.setFillAfter(true);

        loadingView = findViewById(R.id.LoadingView);
        GlideDrawableImageViewTarget loadingImage = new GlideDrawableImageViewTarget(loadingView);
        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        init();

        // 검색 버튼
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchListActivity.class);
                intent.putExtra("keyword", "");
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        // FAB 버튼 처리
        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 열린 상태이면 닫기
                if (FAB_FLAG) {
                    Message msg = FABCloseHandler.obtainMessage();
                    FABCloseHandler.sendMessage(msg);
                }

                // 닫힌 상태이면 열기
                else {
                    Message msg = FABOpenHandler.obtainMessage();
                    FABOpenHandler.sendMessage(msg);
                }
            }
        });

        // FAB Long Click
        mainFAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainFAB.setImageResource(R.drawable.color_circular);
                mainFAB.startAnimation(FABAnimation);

                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(sttIntent);
                return true;
            }
        });

        myFAB.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        likeFAB.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewSearchListActivity.class);
            intent.putExtra("openType", "like");
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        settingFAB.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        // 스크롤뷰 위치 수정 (검색 EditText 숨김)
        mainScrollView.post(new Runnable() {
            @Override
            public void run() {
                mainScrollView.scrollTo(0, 170);
            }
        });
    }

    public void init(){
        for(int i = 0; i < 9; i++){
            recImageList[i].setImageBitmap(null);
            recTextList[i].setText("");
        }

        loadingView.setVisibility(View.VISIBLE);

        SharedPreferences preferences = getSharedPreferences("com.AnonymousHippo.Palette.sharePreference", MODE_PRIVATE);
        userEmail = preferences.getString("userEmail", "");

        // 데이터 받아오기
        keys[0] = "email";
        data[0] = userEmail;

        recImageArrayList.clear();
        recGalleryList.clear();
        recCodeList.clear();
        recCodeArrayList.clear();

        new Thread() {
            public void run() {
                result = HttpPostData.POST("account/getInfo/", keys, data);

                if (result.equals("") || result.equals("-1") || result.equals("SEND_FAIL")) {

                } else {
                    String interest = result.split("&")[2];
                    keys[0] = "interest";
                    data[0] = interest;
                    result = HttpPostData.POST("gallery/suggestion/", keys, data);

                    String[] resultList = result.split("-");

                    Collections.addAll(recCodeList, resultList);

                    ArrayList<Integer> codeList = MyUtility.Random(resultList.length, 9);

                    for (Integer integer : codeList) {
                        Gallery temp = new Gallery(resultList[integer]);
                        recGalleryList.add(temp);
                        recCodeArrayList.add(temp.getCODE());
                    }

                    try {
                        for (Gallery gallery : recGalleryList) {
                            java.net.URL url = new java.net.URL("http://141.164.40.63:8000/media/database/" + gallery.getCODE() + "/1.jpg");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            recImageArrayList.add(BitmapFactory.decodeStream(input));
                        }

                        Message msg = initHandler.obtainMessage();
                        initHandler.sendMessage(msg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    // 뒤로가기 버튼
    @Override
    public void onBackPressed() {
        finish();
    }

    /* Blur View 클릭 함수 */
    public void onBlurClicked(View view) {
        Message msg = FABCloseHandler.obtainMessage();
        FABCloseHandler.sendMessage(msg);
    }

    /* Title 클릭 */
    public void onTOPClicked(View view) {
        Message msg = refreshHandler.obtainMessage();
        refreshHandler.sendMessage(msg);
    }

    // FAB 닫힘 Handler
    @SuppressLint("HandlerLeak")
    Handler FABCloseHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            myFAB.startAnimation(fab_close);
            likeFAB.startAnimation(fab_close);
            settingFAB.startAnimation(fab_close);

            mainBlurView.setVisibility(View.INVISIBLE);
            myFAB.setVisibility(View.INVISIBLE);
            myFABTextView.setVisibility(View.INVISIBLE);
            likeFAB.setVisibility(View.INVISIBLE);
            likeFABTextView.setVisibility(View.INVISIBLE);
            settingFAB.setVisibility(View.INVISIBLE);
            settingFABTextView.setVisibility(View.INVISIBLE);

            FAB_FLAG = false;
        }
    };

    // FAB 열림 Handler
    @SuppressLint("HandlerLeak")
    Handler FABOpenHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            myFAB.startAnimation(fab_open);
            likeFAB.startAnimation(fab_open);
            settingFAB.startAnimation(fab_open);

            mainBlurView.setVisibility(View.VISIBLE);
            myFAB.setVisibility(View.VISIBLE);
            myFABTextView.setVisibility(View.VISIBLE);
            likeFAB.setVisibility(View.VISIBLE);
            likeFABTextView.setVisibility(View.VISIBLE);
            settingFAB.setVisibility(View.VISIBLE);
            settingFABTextView.setVisibility(View.VISIBLE);

            FAB_FLAG = true;
        }
    };

    private final RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @SuppressLint("ShowToast")
        @Override
        public void onError(int error) {
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                case SpeechRecognizer.ERROR_CLIENT:
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                case SpeechRecognizer.ERROR_NO_MATCH:
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                case SpeechRecognizer.ERROR_SERVER:
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                default:
                    System.err.println(error);
                    mainFAB.setImageResource(R.drawable.fab_main);
                    mainFAB.clearAnimation();
                    break;
            }
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            String resultKeyword = "";
            for (String match : matches) {
                resultKeyword = resultKeyword.concat(match);
            }

            Intent intent = new Intent(getApplicationContext(), SearchListActivity.class);
            intent.putExtra("keyword", resultKeyword.trim());
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };

    @Override
    public void onRefresh() {
        Message msg = refreshHandler.obtainMessage();
        refreshHandler.sendMessage(msg);
        mSwipeRefreshLayout.setRefreshing(false);
        init();
    }

    @SuppressLint("HandlerLeak")
    Handler refreshHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            mainScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mainScrollView.scrollTo(0, 170);
                }
            });
        }
    };

    @SuppressLint("HandlerLeak")
    Handler initHandler = new Handler() {
        @Override
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            for(int i = 0; i < 9; i++){
                recImageList[i].setImageBitmap(recImageArrayList.get(i));
                recTextList[i].setText(recGalleryList.get(i).getTITLE());
            }
            loadingView.setVisibility(View.INVISIBLE);
        }
    };
}