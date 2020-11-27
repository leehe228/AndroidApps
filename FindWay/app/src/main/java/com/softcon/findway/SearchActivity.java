package com.softcon.findway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class SearchActivity extends BaseActivity {

    ArrayList<DataClass> dataList;

    private DataAdapter dataAdapter;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String type;
    private TextView warningTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* 초기화 */
        dataList = new ArrayList<DataClass>();
        String[] saveList = new String[2];

        /* 인스턴스화 */
        final EditText searchEditText = findViewById(R.id.Search_EditText_place);
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        /* 액티비티 열릴 때 포커스 주기 */
        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        ListView searchListView = findViewById(R.id.Search_ListView);
        dataAdapter = new DataAdapter(this, dataList);

        TextView titleTextView = findViewById(R.id.Search_TextView_title);
        warningTextView = findViewById(R.id.Search_TextView_warning);

        searchListView.setAdapter(dataAdapter);

        /* 출발지, 도착지, 장소 정보 가져오기 */
        Intent intent = getIntent();
        type = Objects.requireNonNull(intent.getExtras()).getString("type", "");

        /* 도착, 출발에 따른 화면 설정 */
        if (type.equals("arrival")) {
            titleTextView.setText("도착역");
        } else if (type.equals("departure")) {
            titleTextView.setText("출발역");
        } else {
            titleTextView.setText("검색 결과");
        }

        /* Text Watcher */
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dataList.clear();

                if (searchEditText.getText().toString().length() > 0) {
                    warningTextView.setVisibility(View.INVISIBLE);
                    SearchInJSON(searchEditText.getText().toString());
                } else if (searchEditText.getText().toString().length() == 0) {
                    warningTextView.setVisibility(View.VISIBLE);
                    warningTextView.setText("검색어를 입력하세요");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* 한글만 입력되도록 필터 설정 */
        searchEditText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {
                Pattern ps = Pattern.compile(getString(R.string.KoFilter));
                if (charSequence.equals("") || ps.matcher(charSequence).matches()) {
                    return charSequence;
                }
                Toast.makeText(getApplicationContext(), "한글만 입력할 수 있어요", Toast.LENGTH_SHORT).show();
                return "";
            }
        }, new InputFilter.LengthFilter(10)});

        /* ITEM CLICK */
        searchListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (type.equals("departure")) {
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("departureName", dataAdapter.getItem(i).getTitle());
                    editor.putString("departureLine", dataAdapter.getItem(i).getLine());
                    editor.apply();

                    Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                    intent.putExtra("openType", "departure");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slideout, R.anim.slideup_slow);
                    finish();

                } else if (type.equals("arrival")) {
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("arrivalName", dataAdapter.getItem(i).getTitle());
                    editor.putString("arrivalLine", dataAdapter.getItem(i).getLine());
                    editor.apply();

                    Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                    intent.putExtra("openType", "arrival");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slideout, R.anim.slideup_slow);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        intent.putExtra("openType", "cancel_" + type);

        if (type.equals("arrival")) {
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("arrivalName", "");
            editor.putString("arrivalLine", "");
            editor.apply();
        } else if (type.equals("departure")) {
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("departureName", "");
            editor.putString("departureLine", "");
            editor.apply();
        }

        startActivity(intent);
        overridePendingTransition(R.anim.slideout, R.anim.slideup_slow);
        finish();
    }

    public void SearchInJSON(String keyword) {
        AssetManager assetManager = getAssets();

        try {
            InputStream inputStream = assetManager.open("jsons/subway.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            /* Get Results */
            String resultJson = buffer.toString();
            PARSE_JSON(resultJson, keyword);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void PARSE_JSON(String originalJSONString, String keyword) throws JSONException {
        JSONObject jsonObject = new JSONObject(originalJSONString);

        JSONArray resultArray = jsonObject.getJSONArray("DATA");

        int ListLength = resultArray.length();

        /* JSON에서 Results Tag List ListView ArrayList에 item 추가 */
        for (int i = 0; i < ListLength; i++) {
            JSONObject innerObject = resultArray.getJSONObject(i);

            /* 이름과 호선 정보 ArrayList에 추가 */
            String lineNum = innerObject.getString("line_num");
            String stationName = innerObject.getString("station_nm");

            if (stationName.contains(keyword)) {
                dataList.add(new DataClass(stationName, lineNum));
            }
        }

        Message msg = mHandler.obtainMessage();
        mHandler.sendMessage(msg);
    }

    /* ListView Adapter 재설정 Handler */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            dataAdapter.notifyDataSetChanged();
        }
    };
}