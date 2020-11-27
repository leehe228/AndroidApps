package com.softcon.thetutorcpp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class DeveloperActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        /* 애니메이션 */
        Animation LogoAnimation = AnimationUtils.loadAnimation(DeveloperActivity.this, R.anim.translate);
        final ImageView Loading_ImageView_logo = findViewById(R.id.Developer_ImageView_logo);
        Loading_ImageView_logo.startAnimation(LogoAnimation);

        ConstraintLayout CoverView = findViewById(R.id.DeveloperActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(DeveloperActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        ImageButton backButton = findViewById(R.id.Developer_ImageButton_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeveloperActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeveloperActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();

    }
}