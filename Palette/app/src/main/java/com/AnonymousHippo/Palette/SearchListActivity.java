package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class SearchListActivity extends BaseActivity {

    private String result;
    ArrayList<String> resultList;
    private SearchAdapter resultAdapter;
    ArrayList<String> codeList;
    ArrayList<Bitmap> imageList;

    ArrayList<String> adapterInfoList;
    ArrayList<Bitmap> adapterImageList;
    ArrayList<String> adapterCodeList;

    /* Warning TextView */
    TextView warningTextView;

    /* Loading View */
    ImageView loadingView;

    ListView listView;

    private EditText searchEditText;

    private InputMethodManager imm;

    private String keyword;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        // 초기화
        listView = findViewById(R.id.Search_ListView);
        searchEditText = findViewById(R.id.Search_EditText_search);
        resultList = new ArrayList<>();
        codeList = new ArrayList<>();
        imageList = new ArrayList<>();
        warningTextView = findViewById(R.id.Search_TextView_noResult);
        count = 0;

        adapterInfoList = new ArrayList<>();
        adapterImageList = new ArrayList<>();
        adapterCodeList = new ArrayList<>();

        searchEditText.requestFocus();

        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        searchEditText.setText(keyword);

        // 키보드 올리기
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.showSoftInput(searchEditText, 0);

        /* 로딩 GIF */
        loadingView = findViewById(R.id.LoadingView);
        GlideDrawableImageViewTarget loadingImage = new GlideDrawableImageViewTarget(loadingView);
        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        resultAdapter = new SearchAdapter(this, adapterInfoList, adapterImageList);
        listView.setAdapter(resultAdapter);

        new Thread() {
            public void run() {
                result = HttpPostData.DOWNLOAD_JSON("media/test/database.json");

                try {
                    PARSE_JSON(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg1 = noResultHandler.obtainMessage();
                    noResultHandler.sendMessage(msg1);
                }
            }
        }.start();

        // EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterInfoList.clear();
                adapterImageList.clear();
                adapterCodeList.clear();
                loadingView.setVisibility(View.VISIBLE);
                if (searchEditText.getText().toString().length() > 0) {
                    Search();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(searchEditText.getText().length() == 0){
                    adapterInfoList.clear();
                    adapterImageList.clear();
                    adapterCodeList.clear();
                    Search();
                }
            }
        });

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), GalleryInfoActivity.class);
                intent.putExtra("CODE", adapterCodeList.get(i));
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void PARSE_JSON(String originalJSONString) throws JSONException {
        count = 0;

        JSONObject jsonObject = new JSONObject(originalJSONString);

        JSONArray resultArray = jsonObject.getJSONArray("DATA");

        int ListLength = resultArray.length();

        /* JSON에서 Results Tag List ListView ArrayList에 item 추가 */
        for (int i = 0; i < ListLength; i++) {
            JSONObject innerObject = resultArray.getJSONObject(i);

            /* 이름과 상세 주소 ArrayList에 추가 */
            String code = innerObject.getString("CODE");
            String name = innerObject.getString("TITLE");
            String creator = innerObject.getString("CREATOR");
//            String information = innerObject.getString("INFO");
//            String amount = innerObject.getString("AMOUNT");
//            String artTitles = innerObject.getString("ARTTITLES");
//            String artContents = innerObject.getString("ARTCONTENTS");
            String dueDay = innerObject.getString("DUEDATE");
            String category = innerObject.getString("CATEGORY");

            String[] titles = {"사진전", "가구전시", "유아", "미디어 아트", "학생 작품", "제품 디자인", "캐릭터", "패션", "레고 전시"};

            char[] cat = category.toCharArray();
            int countIndex = 0;
            String catString = "";

            for (int j = 0; j < cat.length; j++) {
                if (cat[j] == '1' && countIndex == 0) {
                    catString = catString.concat(titles[j]);
                    countIndex++;
                } else if (cat[j] == '1' && countIndex > 0) {
                    catString = catString.concat(", " + titles[j]);
                    countIndex++;
                }
            }

            Bitmap img;

            try {
                java.net.URL url = new java.net.URL("http://141.164.40.63:8000/media/database/" + code + "/" + "1.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                img = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                img = null;
            }

            resultList.add(name + "-" + creator + "  ~" + dueDay + "  " + catString);
            codeList.add(code);
            imageList.add(img);
            count++;
        }
        Message msg = finishHandler.obtainMessage();
        finishHandler.sendMessage(msg);
    }

    private void Search() {
        String keyword = searchEditText.getText().toString();
        int c = 0;

        for (int i = 0; i < count; i++) {
            String name = resultList.get(i).split("-")[0];
            String creator = resultList.get(i).split("-")[1];

            if (keyword.length() != 0 && CheckSame(keyword, name + creator) > 1) {
                adapterInfoList.add(resultList.get(i));
                adapterImageList.add(imageList.get(i));
                adapterCodeList.add(codeList.get(i));
                c++;
            }

        }

        if (c == 0) {
            Message msg = noResultHandler.obtainMessage();
            noResultHandler.sendMessage(msg);
        } else {
            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);
        }

    }

    public int CheckSame(String str1, String str2){
        int c = 0;

        char[] str1Array = str1.replace(" ", "").toCharArray();
        char[] str2Array = str2.replace(" ", "").toCharArray();

        for (char item : str1Array) {
            for (char value : str2Array) {
                if (item == value) {
                    c++;
                }
            }
        }

        return c;
    }

    /* ListView Adapter 재설정 Handler */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            resultAdapter.notifyDataSetChanged();
            warningTextView.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
        }
    };

    /* ListView Adapter 결과 없음 */
    @SuppressLint("HandlerLeak")
    Handler noResultHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            resultAdapter.notifyDataSetChanged();
            loadingView.setVisibility(View.INVISIBLE);
            warningTextView.setVisibility(View.VISIBLE);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler finishHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            loadingView.setVisibility(View.INVISIBLE);
            Search();
        }
    };
}