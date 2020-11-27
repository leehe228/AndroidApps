package com.AnonymousHippo.Palette;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DeveloperActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        // 인스턴스화
        ImageButton backButton = findViewById(R.id.Developer_ImageButton_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}