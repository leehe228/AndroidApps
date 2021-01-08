package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        /* 로고 애니메이션 */
        Animation LogoAnimation = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.translate);
        final ImageView Loading_ImageView_logo = (ImageView) findViewById(R.id.Loading_ImageView_logo);
        Loading_ImageView_logo.startAnimation(LogoAnimation);

        /* 화면 전환 (2,000ms 후 전환)*/
        Handler changeHandler = new Handler();
        changeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* 자동 로그인 정보 불러오기 */
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                String result = pref.getString("autoLogin", "false");

                /* 인터넷 연결 정상 */
                if (Get_Internet(LoadingActivity.this) == 1 || Get_Internet(LoadingActivity.this) == 2) {
                    /* 자동 로그인 */
                    assert result != null : "NULL EXCEPTION!";
                    if (result.equals("true")) {
                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    /* 자동 로그인 안될 시 */
                    else {
                        Intent intent = new Intent(LoadingActivity.this, HelloActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                /* 인터넷 연결 안되는 경우 */
                else if (Get_Internet(LoadingActivity.this) == 0) {
                    Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 800);
    }

    /* 인터넷 연결 여부 체크 메소드 */
    public static int Get_Internet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            /* 와이파이로 연결된 경우 */
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            }
            /* 셀룰러로 연결된 경우 */
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return 2;
            }
        }
        /* 연결 안된 경우 */
        return 0;
    }
}