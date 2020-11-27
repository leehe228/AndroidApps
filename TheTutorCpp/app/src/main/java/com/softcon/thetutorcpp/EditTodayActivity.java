package com.softcon.thetutorcpp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EditTodayActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String UserEmailString;

    private View mainColorView;
    private EditText mainEditText;
    private Button addButton, okButton;
    private TextView numIndicatingTextView;


    private int selectedSubIndex, numSub;

    private int numSubData;
    private String nameDataString;
    private String[] nameData;
    private String[] subjectData;
    private int[] numberOfEachSubject;

    private View[] colorView;
    private TextView[] nameView;
    private LinearLayout[] subLinearLayout;
    private ImageButton[] closeSubButton;

    // Adapter
    private List<String> items;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;

    private String subjectDataString;

    private int HttpDownloadErrorCount, HttpUploadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_today);

        /* 초기화 */
        selectedSubIndex = 0;
        numSub = 0;
        nameData = new String[10];
        subjectData = new String[12];
        numberOfEachSubject = new int[10];
        HttpDownloadErrorCount = HttpUploadErrorCount = 0;

        /* 인스턴스화 */
        spinner = findViewById(R.id.EditToday_Spinner);
        mainColorView = findViewById(R.id.EditToday_ColorView_main);
        mainEditText = findViewById(R.id.EditToday_EditText_main);
        addButton = findViewById(R.id.EditToday_Button_add);
        okButton = findViewById(R.id.EditToday_Button_done);
        numIndicatingTextView = findViewById(R.id.EditToday_TextView_numSub);
        okButton.setEnabled(false);
        TextView dateTextView = findViewById(R.id.EditToday_TextView_date);
        ImageButton backButton = findViewById(R.id.EditToday_ImageButton_back);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());

        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
        String display = month + "월 " + day + "일의 공부";
        dateTextView.setText(display);

        colorView = new View[12];
        nameView = new TextView[12];
        subLinearLayout = new LinearLayout[12];
        closeSubButton = new ImageButton[12];

        ConstraintLayout CoverView = findViewById(R.id.EditTodayActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(EditTodayActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        colorView[0] = findViewById(R.id.EditToday_ColorView_0);
        colorView[1] = findViewById(R.id.EditToday_ColorView_1);
        colorView[2] = findViewById(R.id.EditToday_ColorView_2);
        colorView[3] = findViewById(R.id.EditToday_ColorView_3);
        colorView[4] = findViewById(R.id.EditToday_ColorView_4);
        colorView[5] = findViewById(R.id.EditToday_ColorView_5);
        colorView[6] = findViewById(R.id.EditToday_ColorView_6);
        colorView[7] = findViewById(R.id.EditToday_ColorView_7);
        colorView[8] = findViewById(R.id.EditToday_ColorView_8);
        colorView[9] = findViewById(R.id.EditToday_ColorView_9);
        colorView[10] = findViewById(R.id.EditToday_ColorView_10);
        colorView[11] = findViewById(R.id.EditToday_ColorView_11);

        nameView[0] = findViewById(R.id.EditToday_TextView_sub0);
        nameView[1] = findViewById(R.id.EditToday_TextView_sub1);
        nameView[2] = findViewById(R.id.EditToday_TextView_sub2);
        nameView[3] = findViewById(R.id.EditToday_TextView_sub3);
        nameView[4] = findViewById(R.id.EditToday_TextView_sub4);
        nameView[5] = findViewById(R.id.EditToday_TextView_sub5);
        nameView[6] = findViewById(R.id.EditToday_TextView_sub6);
        nameView[7] = findViewById(R.id.EditToday_TextView_sub7);
        nameView[8] = findViewById(R.id.EditToday_TextView_sub8);
        nameView[9] = findViewById(R.id.EditToday_TextView_sub9);
        nameView[10] = findViewById(R.id.EditToday_TextView_sub10);
        nameView[11] = findViewById(R.id.EditToday_TextView_sub11);

        subLinearLayout[0] = findViewById(R.id.EditToday_LinearLayout_sub0);
        subLinearLayout[1] = findViewById(R.id.EditToday_LinearLayout_sub1);
        subLinearLayout[2] = findViewById(R.id.EditToday_LinearLayout_sub2);
        subLinearLayout[3] = findViewById(R.id.EditToday_LinearLayout_sub3);
        subLinearLayout[4] = findViewById(R.id.EditToday_LinearLayout_sub4);
        subLinearLayout[5] = findViewById(R.id.EditToday_LinearLayout_sub5);
        subLinearLayout[6] = findViewById(R.id.EditToday_LinearLayout_sub6);
        subLinearLayout[7] = findViewById(R.id.EditToday_LinearLayout_sub7);
        subLinearLayout[8] = findViewById(R.id.EditToday_LinearLayout_sub8);
        subLinearLayout[9] = findViewById(R.id.EditToday_LinearLayout_sub9);
        subLinearLayout[10] = findViewById(R.id.EditToday_LinearLayout_sub10);
        subLinearLayout[11] = findViewById(R.id.EditToday_LinearLayout_sub11);

        closeSubButton[0] = findViewById(R.id.EditToday_closeButton_sub0);
        closeSubButton[1] = findViewById(R.id.EditToday_closeButton_sub1);
        closeSubButton[2] = findViewById(R.id.EditToday_closeButton_sub2);
        closeSubButton[3] = findViewById(R.id.EditToday_closeButton_sub3);
        closeSubButton[4] = findViewById(R.id.EditToday_closeButton_sub4);
        closeSubButton[5] = findViewById(R.id.EditToday_closeButton_sub5);
        closeSubButton[6] = findViewById(R.id.EditToday_closeButton_sub6);
        closeSubButton[7] = findViewById(R.id.EditToday_closeButton_sub7);
        closeSubButton[8] = findViewById(R.id.EditToday_closeButton_sub8);
        closeSubButton[9] = findViewById(R.id.EditToday_closeButton_sub9);
        closeSubButton[10] = findViewById(R.id.EditToday_closeButton_sub10);
        closeSubButton[11] = findViewById(R.id.EditToday_closeButton_sub11);

        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String userTemp = pref.getString("user", "");

        /* AES 256 암호화 */
        try {
            assert userTemp != null;
            UserEmailString = AES256Chiper.AES_Encode(userTemp);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        /* 수신 */
        new Thread() {
            public void run() {
                HttpPostData_SUB_DATA();
                HttpPostData_GET_DATA();
            }
        }.start();

        /* 스피너 */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSubIndex = i;
                switch (i) {
                    case 0: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub0));
                        break;
                    }
                    case 1: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub1));
                        break;
                    }
                    case 2: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub2));
                        break;
                    }
                    case 3: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub3));
                        break;
                    }
                    case 4: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub4));
                        break;
                    }
                    case 5: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub5));
                        break;
                    }
                    case 6: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub6));
                        break;
                    }
                    case 7: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub7));
                        break;
                    }
                    case 8: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub8));
                        break;
                    }
                    case 9: {
                        mainColorView.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub9));
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mainEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mainEditText.getText().toString().length() > 0) {
                    addButton.setEnabled(true);
                    addButton.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.basic_button));
                } else {
                    addButton.setEnabled(false);
                    addButton.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.basic_button_unclick));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mainEditText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {
                Pattern ps = Pattern.compile(getString(R.string.Filter_KoNumEng));
                if (charSequence.equals("") || ps.matcher(charSequence).matches()) {
                    return charSequence;
                }
                return "";
            }
        }, new InputFilter.LengthFilter(12)});

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numSub < 12) {
                    subjectData[numSub++] = Integer.toString(selectedSubIndex) + ":" + mainEditText.getText().toString();
                    mainEditText.setText("");
                    addButton.setEnabled(false);
                    addButton.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.basic_button_unclick));

                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long nowTime = System.currentTimeMillis();
                Date nowDate = new Date(nowTime);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat todayDateToRecord = new SimpleDateFormat("yyyyDDD");
                String todayDateToRecordString = todayDateToRecord.format(nowDate);
                SharedPreferences prefTime = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefTime.edit();
                editor.putString("lastDate", todayDateToRecordString + "05");
                editor.putString("CHECKBOX", "000000000000");
                editor.apply();

                new Thread() {
                    public void run() {
                        HttpPostData_SAVE_TODO();
                        HttpPostData_TO_AI();
                    }
                }.start();
            }
        });

        closeSubButton[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 0; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 1; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 2; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 3; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 4; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 5; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[6].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 6; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[7].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 7; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[8].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 8; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[9].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 9; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[10].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 10; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        closeSubButton[11].setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (numSub > 0) {
                    numSub--;
                    for (int i = 11; i < subjectData.length - 1; i++) {
                        subjectData[i] = subjectData[i + 1];
                    }
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTodayActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    public void HttpPostData_SAVE_TODO() {
        try {
            subjectDataString = "";

            for (int i = 0; i < numSub; i++) {
                subjectDataString = subjectDataString.concat(subjectData[i]).concat("-");
            }

            Log.i("todoData", subjectDataString);

            URL url = new URL("http://158.247.192.119:8000/account/saveTodo/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString + "&" + "todoNum" + "=" + numSub + "&" + "todoName" + "=" + subjectDataString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "&");
            }
            String myResult = builder.toString();
            Log.i("RESULT", myResult);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(EditTodayActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpDownloadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_SAVE_TODO();
                }
            }.start();
        }
    }

    public void HttpPostData_SUB_DATA() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/loadSub/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "&");
            }
            String myResult = builder.toString();
            Log.i("GOT DATA", myResult);
            numSubData = Integer.parseInt(myResult.split("&")[0]);
            Log.i("NUMBER", Integer.toString(numSubData));

            nameDataString = myResult.split("&")[1];

            for (int j = 0; j < numSubData; j++) {
                nameData[j] = nameDataString.split("-")[j];
            }

            items = new ArrayList<String>();
            items.addAll(Arrays.asList(nameData).subList(0, numSubData));
            adapter = new ArrayAdapter<>(
                    EditTodayActivity.this, R.layout.layout_file, items
            );
            spinner.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
        }
    }

    public void HttpPostData_TO_AI() {
        String recommendationString = "";
        for (int i = 0; i < 10; i++) {
            if (numberOfEachSubject[i] != 0) {
                try {
                    URL url = new URL("http://158.247.192.119:8000/account/goodMorning/");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setDoOutput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
                    PrintWriter writer = new PrintWriter(outStream);
                    String buffer = "userid" + "=" + UserEmailString + "&" + "numSub" + "=" + numberOfEachSubject[i] + "&" + "nameSub" + "=" + i;
                    writer.write(buffer);
                    writer.flush();
                    // 서버에서 전송받기
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                    }
                    String myResult = builder.toString();
                    Log.i("SAVE TODO INFO", myResult);
                    recommendationString = recommendationString.concat(myResult).concat("&");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        SharedPreferences prefTotal = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefTotal.edit();
        Log.i("TODO RECOMMEND TIME", recommendationString);
        editor.putString("TODOTIME", recommendationString);
        editor.apply();

        Message msg = reHandler.obtainMessage();
        reHandler.sendMessage(msg);
    }

    public void HttpPostData_GET_DATA() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/loadTodo/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString;
            writer.write(buffer);
            writer.flush();
            // 서버에서 전송받기
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "&");
            }
            String myResult = builder.toString();
            Log.i("GOT DATA3", myResult);
            numSub = Integer.parseInt(myResult.split("&")[0]);
            Log.i("NUMBER3", Integer.toString(numSub));

            subjectData = new String[12];
            int i = 0;
            for (; i < numSub; i++) {
                subjectData[i] = myResult.split("&")[1].split("-")[i];
            }
            for (; i < 12; i++) {
                subjectData[i] = "";
            }
            System.out.println("GOT DATA 4" + myResult.split("&")[1]);
            Message msg = mHandler.obtainMessage();
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "변경사항을 저장하지 않습니다", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(EditTodayActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @SuppressLint("HandlerLeak")
    Handler reHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            Intent intent = new Intent(EditTodayActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            numIndicatingTextView.setText(numSub + "/12개");

            if (numSub == 12) {
                addButton.setText("추가할 수 없습니다");
                addButton.setEnabled(false);
                mainEditText.setEnabled(false);
                spinner.setEnabled(false);
                mainColorView.setVisibility(View.INVISIBLE);
                addButton.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.basic_button_unclick));
            } else {
                mainEditText.setEnabled(true);
                spinner.setEnabled(true);
                mainColorView.setVisibility(View.VISIBLE);
                addButton.setText("추가하기");
            }

            if (numSub == 0) {
                okButton.setEnabled(false);
                okButton.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.basic_button_unclick));
            } else if (numSub >= 1) {
                okButton.setEnabled(true);
                okButton.setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.basic_button));
            }

            numberOfEachSubject = new int[10];
            int i;

            for (i = 0; i < numSub; i++) {
                String sub = subjectData[i].split(":")[0];
                String subData = subjectData[i].split(":")[1];
                System.out.println("DATA PUSHED : " + numSub + ", " + sub + ", " + subData + ", " + nameData[Integer.parseInt(sub)]);
                numberOfEachSubject[Integer.parseInt(sub)] = numberOfEachSubject[Integer.parseInt(sub)] + 1;
                switch (sub) {
                    case "0": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub0));
                        break;
                    }
                    case "1": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub1));
                        break;
                    }
                    case "2": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub2));
                        break;
                    }
                    case "3": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub3));
                        break;
                    }
                    case "4": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub4));
                        break;
                    }
                    case "5": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub5));
                        break;
                    }
                    case "6": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub6));
                        break;
                    }
                    case "7": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub7));
                        break;
                    }
                    case "8": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub8));
                        break;
                    }
                    case "9": {
                        colorView[i].setBackground(ContextCompat.getDrawable(EditTodayActivity.this, R.drawable.sub9));
                        break;
                    }
                }
                nameView[i].setText(nameData[Integer.parseInt(sub)] + " " + subData);
                subLinearLayout[i].setVisibility(View.VISIBLE);
            }

            for (; i < 12; i++) {
                subLinearLayout[i].setVisibility(View.INVISIBLE);
            }

            for (int j = 0; j < 10; j++) {
                System.out.print(j + " : " + numberOfEachSubject[j] + "/ ");
            }
            System.out.print("\n");
        }
    };
}