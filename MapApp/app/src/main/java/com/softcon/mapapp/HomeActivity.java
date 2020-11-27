package com.softcon.mapapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    ArrayList<DataClass> myPlaceList;
    ArrayList<DataClass> recentList;

    /* Loading */
    ImageView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.InitializeListView();

        /* 로딩 GIF */
        loadingView = findViewById(R.id.LoadingView);
        final GlideDrawableImageViewTarget loadingImage = new GlideDrawableImageViewTarget(loadingView);
        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        /* EditText Focus Var */
        final Boolean[] editTextBoolean = {false, false};

        /* 인스턴스화 */
        ListView myPlaceListView = findViewById(R.id.Home_ListView_myPlace);
        ListView recentListView = findViewById(R.id.Home_ListView_recent);

        final EditText departureEditText = findViewById(R.id.Home_EditText_departure);
        final EditText arrivalEditText = findViewById(R.id.Home_EditText_arrival);

        final DataAdapter myPlaceAdapter = new DataAdapter(this, myPlaceList);
        final DataAdapter recentAdapter = new DataAdapter(this, recentList);

        /* Adapter Linking */
        myPlaceListView.setAdapter(myPlaceAdapter);
        recentListView.setAdapter(recentAdapter);

        departureEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        arrivalEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        /* open type intent 가져오기 */
        Intent intent = getIntent();
        String openType = Objects.requireNonNull(intent.getExtras()).getString("openType", "init");

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        // Loading Activity -> Home
        switch (openType) {
            case "init":

                break;
            // Search Activity -> Home (Cancel by Departure)
            case "cancel_departure":

                break;
            // Search Activity -> Home (Cancel by Arrival)
            case "cancel_arrival":

                break;
            // Search Activity -> Home (by Departure)
            case "departure":
            case "arrival":
                arrivalEditText.setText(pref.getString("arrivalName", ""));
                departureEditText.setText(pref.getString("departureName", ""));
                break;
        }

        /* 출발, 도착 모두 입력 시 넘어가기 */
        if (!Objects.equals(pref.getString("arrivalName", ""), "") && !Objects.equals(pref.getString("arrivalName", ""), "")) {
            //
        }

        /* 비어있는지 다시 체크 */
        if (departureEditText.getText().toString().length() > 0) {
            editTextBoolean[0] = true;
        }
        if (arrivalEditText.getText().toString().length() > 0) {
            editTextBoolean[1] = true;
        }

        /* Text Watcher */
        departureEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextBoolean[0] = departureEditText.getText().toString().length() > 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        departureEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (editTextBoolean[0]) {
                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        intent.putExtra("type", "departure");
                        intent.putExtra("place", departureEditText.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }

                return false;
            }
        });

        arrivalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextBoolean[1] = arrivalEditText.getText().toString().length() > 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        arrivalEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (editTextBoolean[1]) {
                        loadingView.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        intent.putExtra("type", "arrival");
                        intent.putExtra("place", arrivalEditText.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
                return false;
            }
        });

        /* ListView */

    }

    public void InitializeListView() {
        myPlaceList = new ArrayList<>();
        recentList = new ArrayList<>();

        myPlaceList.add(new DataClass("광진캠퍼스시티", "서울특별시 광진구 동일로 178"));
        myPlaceList.add(new DataClass("연화마을 8단지", "충청남도 아산시 배방읍 연화로 36"));
        myPlaceList.add(new DataClass("광진캠퍼스시티", "서울특별시 광진구 동일로 178"));

        recentList.add(new DataClass("광진캠퍼스시티", "서울특별시 광진구 동일로 178"));
        recentList.add(new DataClass("연화마을 8단지", "충청남도 아산시 배방읍 연화로 36"));
        recentList.add(new DataClass("광진캠퍼스시티", "서울특별시 광진구 동일로 178"));
    }
}