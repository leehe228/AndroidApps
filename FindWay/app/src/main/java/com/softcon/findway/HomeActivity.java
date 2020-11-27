package com.softcon.findway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class HomeActivity extends BaseActivity implements OnMapReadyCallback {

    /* STEP */
    private int STEP;
    private TextView departureTextView;
    private TextView arrivalTextView;
    private TextView warningTextView;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    /* Google Map */
    private GoogleMap googleMap;
    LatLng[] markers;

    private int index;

    /* 현재 위치 표시 */
    private GpsTracker gpsTracker;

    /* 날짜 / 시간 */
    private int day, month, year;
    private int hour, min;
    private TextView startTimeTextView, startDayTextView;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    ArrayList<DataClass> resultList;
    private DataAdapter dataAdapter;
    private String[] SearchList;

    /* Loading */
    private LinearLayout hideLinearLayout;
    private ImageView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* 권한 허용 여부 체크 */
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        } else {

            checkRunTimePermission();
        }

        /* 초기화 */
        resultList = new ArrayList<>();
        STEP = 0;
        index = 0;

        /* 로딩 GIF */
        loadingView = findViewById(R.id.LoadingView);
        GlideDrawableImageViewTarget loadingImage = new GlideDrawableImageViewTarget(loadingView);
        Glide.with(this).load(R.drawable.loading).into(loadingImage);

        /* 인스턴스화 */
        hideLinearLayout = findViewById(R.id.Home_LinearLayout_hideMap);

        warningTextView = findViewById(R.id.Home_TextView_warning);

        departureTextView = findViewById(R.id.Home_TextView_departure);
        arrivalTextView = findViewById(R.id.Home_TextView_arrival);
        startDayTextView = findViewById(R.id.Home_TextView_startDay);
        startTimeTextView = findViewById(R.id.Home_TextView_startTime);

        Button changeDayButton = findViewById(R.id.Home_Button_changeDay);
        Button changeTimeButton = findViewById(R.id.Home_Button_changeTime);

        gpsTracker = new GpsTracker(HomeActivity.this);

        ListView searchListView = findViewById(R.id.Home_listView);
        dataAdapter = new DataAdapter(this, resultList);

        searchListView.setAdapter(dataAdapter);

        ImageButton userButton = findViewById(R.id.Home_ImageButton_User);

        /* open type intent 가져오기 */
        Intent intent = getIntent();
        String openType = Objects.requireNonNull(intent.getExtras()).getString("openType", "init");

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);

        // Loading Activity -> Home
        switch (openType) {
            case "init":
                Calendar calendar = Calendar.getInstance();

                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("startYear", calendar.get(Calendar.YEAR));
                editor.putInt("startMonth", calendar.get(Calendar.MONTH) + 1);
                editor.putInt("startDay", calendar.get(Calendar.DAY_OF_MONTH));
                editor.putInt("startHour", calendar.get(Calendar.HOUR));
                editor.putInt("startMin", calendar.get(Calendar.MINUTE));
                editor.apply();

                String dayDisplayString = calendar.get(Calendar.YEAR) + "년 " +
                        calendar.get(Calendar.MONTH) + "월 " +
                        calendar.get(Calendar.DAY_OF_MONTH) + "일";
                String timeDisplayString = calendar.get(Calendar.HOUR) + "시 " + calendar.get(Calendar.MINUTE) + "분";

                startDayTextView.setText(dayDisplayString);
                startTimeTextView.setText(timeDisplayString);
                break;
            // Search Activity -> Home (Cancel by Departure)
            case "cancel_departure":
                STEP = 0;
                arrivalTextView.setText("눌러서 검색");
                departureTextView.setText("");
                break;
            // Search Activity -> Home (Cancel by Arrival)
            case "cancel_arrival":
                STEP = 1;
                departureTextView.setText(pref.getString("departureName", ""));
                break;
            // Search Activity -> Home (by Departure)
            case "departure":
                STEP = 1;
                departureTextView.setText(pref.getString("departureName", ""));
                arrivalTextView.setText("");
                break;
            case "arrival":
                STEP = 2;
                break;
        }

        switch (STEP){
            case 0:{
                departureTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_blue));
                departureTextView.setClickable(true);
                departureTextView.setFocusable(true);
                departureTextView.setText("눌러서 검색");
                arrivalTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                arrivalTextView.setClickable(false);
                arrivalTextView.setFocusable(false);
                arrivalTextView.setText("");
                break;
            }
            case 1:{
                departureTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                departureTextView.setClickable(true);
                departureTextView.setFocusable(true);
                departureTextView.setText(pref.getString("departureName", ""));
                arrivalTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_blue));
                arrivalTextView.setClickable(true);
                arrivalTextView.setFocusable(true);
                arrivalTextView.setText("눌러서 검색");
                break;
            }
            case 2:{
                departureTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                departureTextView.setClickable(false);
                departureTextView.setFocusable(false);
                departureTextView.setText(pref.getString("departureName", ""));
                arrivalTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                arrivalTextView.setClickable(true);
                arrivalTextView.setFocusable(true);
                arrivalTextView.setText(pref.getString("arrivalName", ""));
            }
        }

        if (!Objects.equals(pref.getString("arrivalName", ""), "") && !Objects.equals(pref.getString("departureName", ""), "")) {
            Intent intent2 = new Intent(HomeActivity.this, ResultActivity.class);
            startActivity(intent2);
            overridePendingTransition(R.anim.slieup, R.anim.slideup_slow);
            finish();
        }

        /* 출발 보여줌 */
        String dayDisplayString = pref.getInt("startYear", 0) + "년 " +
                pref.getInt("startMonth", 0) + "월 " +
                pref.getInt("startDay", 0) + "일";
        String timeDisplayString = pref.getInt("startHour", 0) + "시 "
                + pref.getInt("startMin", 0) + "분";

        startDayTextView.setText(dayDisplayString);
        startTimeTextView.setText(timeDisplayString);

        /* MAP */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Home_mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        /* 현재 위치 표시 */
        Message msg = drawMyLocationHandler.obtainMessage();
        drawMyLocationHandler.sendMessage(msg);

        changeDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });

        changeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });

        /* ListView */
        searchListView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers[i], 16));
            }
        });

        searchListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if(STEP == 0){
                    editor.putString("departureName", SearchList[i].split(":")[0]);
                    editor.putString("departureLine", SearchList[i].split(":")[1]);
                    editor.apply();
                    departureTextView.setText(SearchList[i].split(":")[0]);
                    STEP = 1;
                }
                else if (STEP == 1){
                    editor.putString("arrivalName", SearchList[i].split(":")[0]);
                    editor.putString("arrivalLine", SearchList[i].split(":")[1]);
                    editor.apply();
                    arrivalTextView.setText(SearchList[i].split(":")[0]);
                    STEP = 2;
                }
                Message msg_ = mHandler.obtainMessage();
                mHandler.sendMessage(msg_);
                return false;
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            switch (STEP){
                case 0:{
                    departureTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_blue));
                    departureTextView.setClickable(true);
                    departureTextView.setFocusable(true);
                    departureTextView.setText("눌러서 검색");
                    arrivalTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                    arrivalTextView.setClickable(false);
                    arrivalTextView.setFocusable(false);
                    arrivalTextView.setText("");
                    break;
                }
                case 1:{
                    departureTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                    departureTextView.setClickable(true);
                    departureTextView.setFocusable(true);
                    departureTextView.setText(pref.getString("departureName", ""));
                    arrivalTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_blue));
                    arrivalTextView.setClickable(true);
                    arrivalTextView.setFocusable(true);
                    arrivalTextView.setText("눌러서 검색");
                    break;
                }
                case 2:{
                    departureTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                    departureTextView.setClickable(false);
                    departureTextView.setFocusable(false);
                    departureTextView.setText(pref.getString("departureName", ""));
                    arrivalTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape));
                    arrivalTextView.setClickable(true);
                    arrivalTextView.setFocusable(true);
                    arrivalTextView.setText(pref.getString("arrivalName", ""));
                }
            }

            if (!Objects.equals(pref.getString("arrivalName", ""), "") && !Objects.equals(pref.getString("departureName", ""), "")) {
                Intent intent2 = new Intent(HomeActivity.this, ResultActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slieup, R.anim.slideup_slow);
                finish();
            }

        }
    };

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        if (id == 0) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, datePickerListner, year, month, day);
        } else {
            hour = calendar.get(Calendar.HOUR);
            min = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(this, timePickerListener, hour, min, true);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth + 1;
            year = selectedYear;

            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("startDay", day);
            editor.putInt("startMonth", month);
            editor.putInt("startYear", year);
            editor.apply();

            String displayString = year + "년 " + month + "월 " + day + "일";
            startDayTextView.setText(displayString);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin) {
            hour = selectedHour;
            min = selectedMin;

            SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("startHour", selectedHour);
            editor.putInt("startMin", selectedMin);
            editor.apply();

            String displayString = hour + "시 " + min + "분";
            startTimeTextView.setText(displayString);
        }
    };

    public void onDepartureClicked(View view) {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        intent.putExtra("type", "departure");
        startActivity(intent);
        overridePendingTransition(R.anim.slieup, R.anim.slideup_slow);
        finish();
    }

    public void onArrivalClicked(View view) {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        intent.putExtra("type", "arrival");
        startActivity(intent);
        overridePendingTransition(R.anim.slieup, R.anim.slideup_slow);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @SuppressLint("HandlerLeak")
    Handler drawMyLocationHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            googleMap.clear();
            final double lat = gpsTracker.getLatitude();
            final double lng = gpsTracker.getLongitude();
            CircleOptions circleOptions = new CircleOptions().center(new LatLng(lat, lng))
                    .radius(1000)
                    .strokeWidth(0f)
                    .fillColor(Color.parseColor("#4F4286F5"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14));
            googleMap.addCircle(circleOptions);

            new Thread() {
                public void run() {
                    try {
                        HTTP_SEARCH_PLACE_LIST(lat, lng);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };

    /* Google Map API - SEARCH PLACE LIST */
    public void HTTP_SEARCH_PLACE_LIST(double lat, double lng) throws IOException, JSONException {
        String resultJson = "", subwayResultJson = "";

        BufferedReader in;
        /* Query Setting */
        String queryString = "key=" + getString(R.string.google_maps_key)
                + "&language=ko"
                + "&type=subway_station"
                + "&region=ko"
                + "&location=" + lat + "," + lng
                + "&radius=" + "1000";
        try {
            /* Google Map URL */
            String nearbySearchURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            URL obj = new URL(nearbySearchURL + queryString);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            /* Get Results */
            resultJson = stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

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
            subwayResultJson = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PARSE_JSON(resultJson, subwayResultJson);
    }

    /* Google API에서 수신한 JSON DATA 파싱해서 ListView에 inflate*/
    public void PARSE_JSON(String originalJSONString, String subwayJSONString) throws JSONException {
        JSONObject jsonObject = new JSONObject(originalJSONString);
        SearchList = new String[10];
        markers = new LatLng[10];
        index = 0;

        /* 결과 수신 여부 확인 */
        String okCheck = jsonObject.getString("status");
        Log.i("DATA STATUS", okCheck);

        /* 수신 되었다면 */
        if (okCheck.equals("OK")) {
            JSONArray resultArray = jsonObject.getJSONArray("results");

            int listLength = Math.min(resultArray.length(), 10);

            /* JSON에서 Results Tag List ListView ArrayList에 item 추가 */
            for (int i = 0; i < listLength; i++) {
                JSONObject innerObject = resultArray.getJSONObject(i);

                /* 이름과 상세 주소 ArrayList에 추가 */
                String name = innerObject.getString("name");
                String lat = innerObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = innerObject.getJSONObject("geometry").getJSONObject("location").getString("lng");

                /* 이름, lat, lng 리스트에 추가 */
                markers[i] = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                JSONObject subwayDataObject = new JSONObject(subwayJSONString);
                JSONArray subwayDataArray = subwayDataObject.getJSONArray("DATA");

                int subwayListLength = subwayDataArray.length();

                /* JSON에서 Results Tag List ListView ArrayList에 item 추가 */
                for (int j = 0; j < subwayListLength; j++) {
                    JSONObject subwayInnerObject = subwayDataArray.getJSONObject(j);

                    /* 이름과 호선 정보 ArrayList에 추가 */
                    String lineNum = subwayInnerObject.getString("line_num");
                    String stationName = subwayInnerObject.getString("station_nm");

                    if (stationName.contains(name)) {
                        resultList.add(new DataClass(stationName, lineNum));
                        SearchList[index++] = name + ":" + lineNum;
                    }
                }
            }
            Log.i("SearchList", Arrays.toString(SearchList));
            Log.i("Length", Integer.toString(listLength));
            /* Main Thread로 View inflate 넘김 */
            Message msg = nHandler.obtainMessage();
            nHandler.sendMessage(msg);
        }
        /* 검색 결과 없음 */
        else{
            Message msg = noResultHandler.obtainMessage();
            noResultHandler.sendMessage(msg);
        }
    }

    /* ListView Adapter 재설정 Handler */
    @SuppressLint("HandlerLeak")
    Handler nHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            dataAdapter.notifyDataSetChanged();
            if(SearchList != null){
                for (int i = 0; i < index; i++) {
                    googleMap.addMarker(new MarkerOptions().position(markers[i]).title(SearchList[i].split(":")[0]));
                }
            }

            hideLinearLayout.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
        }
    };

    /* ListView Adapter 재설정 Handler */
    @SuppressLint("HandlerLeak")
    Handler noResultHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            hideLinearLayout.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
            warningTextView.setVisibility(View.VISIBLE);
        }
    };

    /* GPS 메소드 */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(HomeActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(HomeActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(HomeActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(HomeActivity.this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLE_REQUEST_CODE) {
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {
                    checkRunTimePermission();
                }
            }
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}