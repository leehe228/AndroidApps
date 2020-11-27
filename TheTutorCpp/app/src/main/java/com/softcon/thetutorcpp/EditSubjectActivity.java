package com.softcon.thetutorcpp;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EditSubjectActivity extends BaseActivity {

    private ImageButton[] closeSubButton = new ImageButton[10];
    private TextView[] subTextView = new TextView[10];
    private LinearLayout[] subLinearLayout = new LinearLayout[10];

    // DATA
    private String[] nameData = new String[10];
    private int numSubData;
    private String nameDataString;

    private TextView numSub;
    private View ColorView;
    private EditText addEditText;
    private Button enterButton, okButton;

    private String UserEmailString;

    private int insert;

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private int HttpDownloadErrorCount, HttpUploadErrorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        /* 초기화 */
        insert = 0;
        HttpDownloadErrorCount = HttpUploadErrorCount = 0;

        /* 데이터 받아오기 */
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String temp = pref.getString("user", "");
        /* AES 256 암호화 */
        try {
            assert temp != null;
            UserEmailString = AES256Chiper.AES_Encode(temp);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        new Thread() {
            public void run() {
                HttpPostData_GET_DATA();
            }
        }.start();

        /* 인스턴스화 */
        enterButton = findViewById(R.id.EditSub_Button_enter);
        okButton = findViewById(R.id.EditSub_Button_done);
        ImageButton backButton = findViewById(R.id.EditSub_ImageButton_back);

        ConstraintLayout CoverView = findViewById(R.id.EditSubjectActivity);
        Animation LayoutUpAnimation = AnimationUtils.loadAnimation(EditSubjectActivity.this, R.anim.layout_up);
        CoverView.startAnimation(LayoutUpAnimation);

        // 닫기 버튼
        closeSubButton[0] = findViewById(R.id.EditSub_closeButton_sub0);
        closeSubButton[1] = findViewById(R.id.EditSub_closeButton_sub1);
        closeSubButton[2] = findViewById(R.id.EditSub_closeButton_sub2);
        closeSubButton[3] = findViewById(R.id.EditSub_closeButton_sub3);
        closeSubButton[4] = findViewById(R.id.EditSub_closeButton_sub4);
        closeSubButton[5] = findViewById(R.id.EditSub_closeButton_sub5);
        closeSubButton[6] = findViewById(R.id.EditSub_closeButton_sub6);
        closeSubButton[7] = findViewById(R.id.EditSub_closeButton_sub7);
        closeSubButton[8] = findViewById(R.id.EditSub_closeButton_sub8);
        closeSubButton[9] = findViewById(R.id.EditSub_closeButton_sub9);

        // 과목 이름
        subTextView[0] = findViewById(R.id.EditSub_TextView_sub0);
        subTextView[1] = findViewById(R.id.EditSub_TextView_sub1);
        subTextView[2] = findViewById(R.id.EditSub_TextView_sub2);
        subTextView[3] = findViewById(R.id.EditSub_TextView_sub3);
        subTextView[4] = findViewById(R.id.EditSub_TextView_sub4);
        subTextView[5] = findViewById(R.id.EditSub_TextView_sub5);
        subTextView[6] = findViewById(R.id.EditSub_TextView_sub6);
        subTextView[7] = findViewById(R.id.EditSub_TextView_sub7);
        subTextView[8] = findViewById(R.id.EditSub_TextView_sub8);
        subTextView[9] = findViewById(R.id.EditSub_TextView_sub9);

        // Linear Layout
        subLinearLayout[0] = findViewById(R.id.EditSub_LinearLayout_sub0);
        subLinearLayout[1] = findViewById(R.id.EditSub_LinearLayout_sub1);
        subLinearLayout[2] = findViewById(R.id.EditSub_LinearLayout_sub2);
        subLinearLayout[3] = findViewById(R.id.EditSub_LinearLayout_sub3);
        subLinearLayout[4] = findViewById(R.id.EditSub_LinearLayout_sub4);
        subLinearLayout[5] = findViewById(R.id.EditSub_LinearLayout_sub5);
        subLinearLayout[6] = findViewById(R.id.EditSub_LinearLayout_sub6);
        subLinearLayout[7] = findViewById(R.id.EditSub_LinearLayout_sub7);
        subLinearLayout[8] = findViewById(R.id.EditSub_LinearLayout_sub8);
        subLinearLayout[9] = findViewById(R.id.EditSub_LinearLayout_sub9);

        // 과목수 표시
        numSub = findViewById(R.id.EditSub_TextView_numSub);

        // 추가하는 곳
        ColorView = findViewById(R.id.EditSub_ColorView_main);
        addEditText = findViewById(R.id.EditSub_EditText_main);

        closeSubButton[0].setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 0; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 1; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 2; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 3; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 4; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                /*if (nameData.length - 1 - 5 >= 0)
                    System.arraycopy(nameData, 6, nameData, 5, nameData.length - 1 - 5);*/
                for (int i = 5; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 6; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 7; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSubData--;
                for (int i = 8; i < nameData.length - 1; i++) {
                    nameData[i] = nameData[i + 1];
                }
                Message msg = deleteHandler.obtainMessage();
                deleteHandler.sendMessage(msg);
            }
        });

        closeSubButton[9].setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (numSubData > 0) {
                    numSubData--;
                    for (int i = 9; i < nameData.length - 1; i++) {
                        nameData[i] = nameData[i + 1];
                    }
                    Message msg = deleteHandler.obtainMessage();
                    deleteHandler.sendMessage(msg);
                }
            }
        });

        addEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (addEditText.getText().toString().length() > 0) {
                    insert = 1;
                } else {
                    insert = 0;
                }
                if (insert == 1) {
                    enterButton.setEnabled(true);
                    enterButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button));
                    enterButton.setText("추가하기");
                    addEditText.setEnabled(true);
                } else {
                    enterButton.setEnabled(false);
                    enterButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button_unclick));
                    enterButton.setText("추가하기");
                    addEditText.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addEditText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {
                Pattern ps = Pattern.compile(getString(R.string.Filter_KoNumEng));
                if (charSequence.equals("") || ps.matcher(charSequence).matches() || charSequence.equals(" ")) {
                    return charSequence;
                }
                return "";
            }
        }, new InputFilter.LengthFilter(12)});

        enterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (numSubData < 10) {
                    nameData[numSubData++] = addEditText.getText().toString();
                    insert = 0;

                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i;
                nameDataString = "";
                for (i = 0; i < numSubData - 1; i++) {
                    nameDataString = nameDataString.concat(nameData[i]).concat("-");
                }
                nameDataString = nameDataString.concat(nameData[i]);
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                String temp = pref.getString("user", "");
                /* AES 256 암호화 */
                try {
                    assert temp != null;
                    UserEmailString = AES256Chiper.AES_Encode(temp);
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                new Thread() {
                    public void run() {
                        HttpPostData();
                    }
                }.start();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "변경사항을 저장하지 않습니다", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditSubjectActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "변경사항을 저장하지 않습니다", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(EditSubjectActivity.this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            enterButton.setEnabled(false);
            enterButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button_unclick));

            addEditText.setText("");
            numSub.setText("과목수 " + numSubData + "/10개");

            if (numSubData == 10) {
                enterButton.setText("추가할 수 없습니다");
                addEditText.setEnabled(false);
                ColorView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                addEditText.setEnabled(true);
                enterButton.setText("추가하기");
            }

            if (numSubData == 0) {
                okButton.setEnabled(false);
                okButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button_unclick));
            } else if (numSubData >= 1) {
                okButton.setEnabled(true);
                okButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button));
            }

            if (numSubData == 0) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub0));
            } else if (numSubData == 1) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub1));
            } else if (numSubData == 2) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub2));
            } else if (numSubData == 3) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub3));
            } else if (numSubData == 4) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub4));
            } else if (numSubData == 5) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub5));
            } else if (numSubData == 6) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub6));
            } else if (numSubData == 7) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub7));
            } else if (numSubData == 8) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub8));
            } else if (numSubData == 9) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub9));
            }

            int i;
            for (i = 0; i < numSubData; i++) {
                subLinearLayout[i].setVisibility(View.VISIBLE);
                subTextView[i].setText(nameData[i]);
            }
            for (; i < 10; i++) {
                subLinearLayout[i].setVisibility(View.INVISIBLE);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler deleteHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            enterButton.setEnabled(false);
            enterButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button_unclick));

            addEditText.setText("");
            numSub.setText("과목수 " + numSubData + "/10개");

            if (numSubData == 10) {
                enterButton.setText("추가할 수 없습니다");
                addEditText.setEnabled(false);
                ColorView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                addEditText.setEnabled(true);
                enterButton.setText("추가하기");
            }

            if (numSubData == 0) {
                okButton.setEnabled(false);
                okButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button_unclick));
            } else if (numSubData >= 1) {
                okButton.setEnabled(true);
                okButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button));
            }

            if (numSubData == 0) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub0));
            } else if (numSubData == 1) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub1));
            } else if (numSubData == 2) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub2));
            } else if (numSubData == 3) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub3));
            } else if (numSubData == 4) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub4));
            } else if (numSubData == 5) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub5));
            } else if (numSubData == 6) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub6));
            } else if (numSubData == 7) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub7));
            } else if (numSubData == 8) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub8));
            } else if (numSubData == 9) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub9));
            }

            int i;
            for (i = 0; i < numSubData; i++) {
                subLinearLayout[i].setVisibility(View.VISIBLE);
                subTextView[i].setText(nameData[i]);
            }
            for (; i < 10; i++) {
                subLinearLayout[i].setVisibility(View.INVISIBLE);
            }
        }
    };

    public void HttpPostData_GET_DATA() {
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
            Message msg = initHandler.obtainMessage();
            initHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpDownloadErrorCount > 3) {
                Intent intent = new Intent(EditSubjectActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpDownloadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData_GET_DATA();
                }
            }.start();
        }
    }

    public void HttpPostData() {
        try {
            URL url = new URL("http://158.247.192.119:8000/account/saveSub/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            String buffer = "userid" + "=" + UserEmailString + "&" + "subNum" + "=" + numSubData + "&" + "subName" + "=" + nameDataString;
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
            Log.i("SAVE SUBJECTS INFO", myResult);
            switch (myResult) {
                case "1": {
                    Intent intent = new Intent(EditSubjectActivity.this, SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                }
                case "0": {
                    // 실패
                    Message msg = reHandler.obtainMessage();
                    reHandler.sendMessage(msg);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP ERROR OCCURRED", "retrying...");
            if (HttpUploadErrorCount > 3) {
                Intent intent = new Intent(EditSubjectActivity.this, NoInternetActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            HttpUploadErrorCount++;
            new Thread() {
                public void run() {
                    HttpPostData();
                }
            }.start();
        }
    }

    /* 실패 시 재시도 용 */
    @SuppressLint("HandlerLeak")
    Handler reHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            enterButton.setText("추가하기");
        }
    };

    /* 초기 화면 구성 */
    @SuppressLint("HandlerLeak")
    Handler initHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            numSub.setText("과목수 " + numSubData + "/10개");

            for (int j = 0; j < numSubData; j++) {
                nameData[j] = nameDataString.split("-")[j];
            }

            if (numSubData == 10) {
                enterButton.setText("추가할 수 없습니다");
                addEditText.setEnabled(false);
                ColorView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                addEditText.setEnabled(true);
                enterButton.setText("추가하기");
            }

            if (numSubData == 0) {
                okButton.setEnabled(false);
                okButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button_unclick));
            } else if (numSubData >= 1) {
                okButton.setEnabled(true);
                okButton.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.basic_button));
            }

            if (numSubData == 0) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub0));
            } else if (numSubData == 1) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub1));
            } else if (numSubData == 2) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub2));
            } else if (numSubData == 3) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub3));
            } else if (numSubData == 4) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub4));
            } else if (numSubData == 5) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub5));
            } else if (numSubData == 6) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub6));
            } else if (numSubData == 7) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub7));
            } else if (numSubData == 8) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub8));
            } else if (numSubData == 9) {
                ColorView.setBackground(ContextCompat.getDrawable(EditSubjectActivity.this, R.drawable.sub9));
            }

            int i;
            for (i = 0; i < numSubData; i++) {
                subLinearLayout[i].setVisibility(View.VISIBLE);
                subTextView[i].setText(nameData[i]);
            }
            for (; i < 10; i++) {
                subLinearLayout[i].setVisibility(View.INVISIBLE);
            }
        }
    };
}