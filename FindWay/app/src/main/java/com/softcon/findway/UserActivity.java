package com.softcon.findway;

import android.content.Intent;
import android.os.Bundle;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(UserActivity.this, HomeActivity.class);
        intent.putExtra("openType", "init");
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}