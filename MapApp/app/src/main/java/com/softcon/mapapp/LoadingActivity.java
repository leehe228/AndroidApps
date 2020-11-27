package com.softcon.mapapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class LoadingActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        /* 로딩 GIF */
        ImageView loadingView = findViewById(R.id.LoadingView);
        GlideDrawableImageViewTarget loadingImage = new GlideDrawableImageViewTarget(loadingView);
        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        Handler m2Handler = new Handler();
        m2Handler.postDelayed(new Runnable() {
            public void run() {
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                /* 도착점 초기화 */
                editor.putString("arrivalName", "");
                editor.putString("arrivalAddress", "");
                editor.putString("arrivalLat", "");
                editor.putString("arrivalLng", "");

                /* 출발점 초기화 */
                editor.putString("departureName", "");
                editor.putString("departureAddress", "");
                editor.putString("departureLat", "");
                editor.putString("departureLng", "");
                editor.apply();

                Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
                intent.putExtra("openType", "init");
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        }, 1000);
    }
}