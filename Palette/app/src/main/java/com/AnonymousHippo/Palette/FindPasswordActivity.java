package com.AnonymousHippo.Palette;

import android.content.Intent;
import android.os.Bundle;

public class FindPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}