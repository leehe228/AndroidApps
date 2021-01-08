package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PrivacyPolicyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        /* 인스턴스화 */
        ImageButton backButton = (ImageButton)findViewById(R.id.PrivacyPolicy_ImageButton_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}