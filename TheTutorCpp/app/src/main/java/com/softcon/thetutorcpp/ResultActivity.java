package com.softcon.thetutorcpp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        /* 인스턴스화 */
        ImageButton backButton = findViewById(R.id.Result_ImageButton_back);

        ConstraintLayout CoverView = findViewById(R.id.ResultActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(ResultActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

}