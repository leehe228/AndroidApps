package com.softcon.mapapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends BaseActivity implements OnMapReadyCallback {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String type;
    ArrayList<DataClass> resultList;
    private DataAdapter resultAdapter;
    private String[] SearchList;

    /* Map */
    private GoogleMap googleMap;
    LatLng[] markers;
    LatLng initMarker;

    private int ListLength;

    /* Loading View */
    ImageView loadingView;

    /* Warning TextView */
    TextView warningTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* 초기화 */
        resultList = new ArrayList<>();
        final Boolean[] editTextBoolean = {false};
        SearchList = new String[10];
        markers = new LatLng[10];

        /* 로딩 GIF */
        loadingView = findViewById(R.id.LoadingView);
        GlideDrawableImageViewTarget loadingImage = new GlideDrawableImageViewTarget(loadingView);
        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        /* 인스턴스화 */
        TextView titleTextView = findViewById(R.id.Search_TextView_title);
        final EditText searchEditText = findViewById(R.id.Search_EditText_place);
        final ListView searchListView = findViewById(R.id.Search_ListView);
        warningTextView = findViewById(R.id.Search_TextView_warning);

        /* Map */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Search_Map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        resultAdapter = new DataAdapter(this, resultList);
        searchListView.setAdapter(resultAdapter);

        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        /* 출발지, 도착지, 장소 정보 가져오기 */
        Intent intent = getIntent();
        type = Objects.requireNonNull(intent.getExtras()).getString("type", "");
        final String place = Objects.requireNonNull(intent.getExtras()).getString("place", "");

        /* 도착, 출발에 따른 화면 설정 */
        if (type.equals("arrival")) {
            titleTextView.setText("도착역");
        } else if (type.equals("departure")) {
            titleTextView.setText("출발역");
        } else {
            titleTextView.setText("검색 결과");
        }
        searchEditText.setText(place);

        new Thread() {
            public void run() {
                HTTP_SEARCH_PLACE_LIST(place);
            }
        }.start();

        /* Text Watcher */
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextBoolean[0] = searchEditText.getText().toString().length() > 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (editTextBoolean[0]) {
                        resultList.clear();
                        SearchList = new String[10];
                        System.out.println(searchEditText.getText().toString());
                        warningTextView.setVisibility(View.INVISIBLE);
                        googleMap.clear();
                        loadingView.setVisibility(View.VISIBLE);
                        new Thread() {
                            public void run() {
                                HTTP_SEARCH_PLACE_LIST(searchEditText.getText().toString());
                            }
                        }.start();
                    }
                }
                return false;
            }
        });

        /* ListView 클릭 */
        searchListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(resultList.get(i).getTitle());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers[i], 16));
            }
        });

        searchListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (type.equals("departure")) {
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("departureName", SearchList[i].split(":")[0]);
                    editor.putString("departureAddress", SearchList[i].split(":")[1]);
                    editor.putString("departureLat", SearchList[i].split(":")[2]);
                    editor.putString("departureLng", SearchList[i].split(":")[3]);
                    editor.apply();

                    Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                    intent.putExtra("openType", "departure");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                } else if (type.equals("arrival")) {
                    SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("arrivalName", SearchList[i].split(":")[0]);
                    editor.putString("arrivalAddress", SearchList[i].split(":")[1]);
                    editor.putString("arrivalLat", SearchList[i].split(":")[2]);
                    editor.putString("arrivalLng", SearchList[i].split(":")[3]);
                    editor.apply();

                    Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                    intent.putExtra("openType", "arrival");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        intent.putExtra("openType", "cancel_" + type);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    /* Google Map API - SEARCH PLACE LIST */
    public void HTTP_SEARCH_PLACE_LIST(String placeString) {
        BufferedReader in;
        /* Query Setting */
        String queryString = "query=" + placeString + "&key=" + getString(R.string.google_maps_key) + "&language=ko" + "&type=subway_station" + "&region=ko";
        try {
            URL obj = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?" + queryString);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            /* Get Results */
            String resultJson = stringBuilder.toString();
            PARSE_JSON(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Google API에서 수신한 JSON DATA 파싱해서 ListView에 inflate*/
    public void PARSE_JSON(String originalJSONString) throws JSONException {
        JSONObject jsonObject = new JSONObject(originalJSONString);

        /* 결과 수신 여부 확인 */
        String okCheck = jsonObject.getString("status");
        Log.i("DATA STATUS", okCheck);

        /* 수신 되었다면 */
        if (okCheck.equals("OK")) {
            JSONArray resultArray = jsonObject.getJSONArray("results");

            /* 검색 결과 최대 10개로 제한 */
            ListLength = Math.min(resultArray.length(), 10);

            /* JSON에서 Results Tag List ListView ArrayList에 item 추가 */
            for (int i = 0; i < ListLength; i++) {
                JSONObject innerObject = resultArray.getJSONObject(i);

                /* 이름과 상세 주소 ArrayList에 추가 */
                String name = innerObject.getString("name");
                String formattedAddress = innerObject.getString("formatted_address");
                String lat = innerObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = innerObject.getJSONObject("geometry").getJSONObject("location").getString("lng");
                System.out.println(name + ", " + formattedAddress + ", lat : " + lat + ", lng : " + lng);
                resultList.add(new DataClass(name, formattedAddress));

                /* 이름, lat, lng 리스트에 추가 */
                SearchList[i] = name + ":" + formattedAddress + ":" + lat + ":" + lng;
                markers[i] = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                if (i == 0) {
                    initMarker = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                }

            }

            /* Main Thread로 View inflate 넘김 */
            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);

            Message msg2 = markerHandler.obtainMessage();
            markerHandler.sendMessage(msg2);
        }
        /* 검색 결과 없음 */
        else {
            Message msg = noResultHandler.obtainMessage();
            noResultHandler.sendMessage(msg);
        }
    }

    /* ListView Adapter 재설정 Handler */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            resultAdapter.notifyDataSetChanged();
            loadingView.setVisibility(View.INVISIBLE);
        }
    };

    /* ListView Adapter 결과 없음 */
    @SuppressLint("HandlerLeak")
    Handler noResultHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.566535, 126.9779692), 10));
            resultAdapter.notifyDataSetChanged();
            loadingView.setVisibility(View.INVISIBLE);
            warningTextView.setVisibility(View.VISIBLE);
        }
    };

    /* ListView Adapter 결과 없음 */
    @SuppressLint("HandlerLeak")
    Handler markerHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initMarker, 13));
            for (int i = 0; i < ListLength; i++) {
                googleMap.addMarker(new MarkerOptions().position(markers[i]).title(SearchList[i].split(":")[0]));
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

}