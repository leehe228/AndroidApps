package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.AutofillService;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AuctionActivity extends BaseActivity {

    private ImageView mainImageView;
    private Button dateView;
    // 날짜
    private String startDate, endDate;
    // 금액
    private int highPrice;
    private String dateString;

    private EditText priceEditText;
    private Button payButton;

    private String userEmail;
    private final String[] keys = new String[3];
    private final String[] data = new String[3];
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        // 인스턴스화
        mainImageView = findViewById(R.id.Auction_ImageView);
        dateView = findViewById(R.id.Auction_Button_onProcess);
        priceEditText = findViewById(R.id.Auction_EditText_price);
        payButton = findViewById(R.id.Auction_Button_pay);

        TextView highPriceTextView = findViewById(R.id.Auction_TextView_highPrice);

        // 값 받아오기
        highPrice = 260000;
        highPriceTextView.setText("최고입찰가 " + highPrice);

        SharedPreferences preferences = getSharedPreferences("com.AnonymousHippo.Palette.sharePreference", MODE_PRIVATE);
        userEmail = preferences.getString("userEmail", "");

        // 초기화
        endDate = "2021-02-28-24-00-00";
        dateString = "";

        new Thread() {
            public void run() {
                while(true){
                    Message msg = timeHandler.obtainMessage();
                    timeHandler.sendMessage(msg);

                    Date nowDate = new Date(System.currentTimeMillis());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                    String nowTime = dateFormat.format(nowDate);
                    String[] timeList = nowTime.split("-");
                    String[] endList = endDate.split("-");

                    String leftDay = String.valueOf(Integer.parseInt(endList[2]) - Integer.parseInt(timeList[2]));
                    String leftHour = String.valueOf(Integer.parseInt(endList[3]) - Integer.parseInt(timeList[3]));
                    String leftMin = String.valueOf(Integer.parseInt(endList[4]) - Integer.parseInt(timeList[4]));
                    String leftSec = String.valueOf(Integer.parseInt(endList[5]) - Integer.parseInt(timeList[5]));
                    dateString = "남은 시간 " + leftDay + "일 " + leftHour + "시간 " + leftMin + "분 " + leftSec + "초 ";
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }.start();

        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(priceEditText.length() == 0){
                    payButton.setText("입찰");
                }
                else{
                    int price = Integer.parseInt(priceEditText.getText().toString()) * 1000;
                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    payButton.setText(myFormatter.format(price) + "원 입찰");
                }

                if(Integer.parseInt(priceEditText.getText().toString()) * 1000 > highPrice){
                    payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button));
                    payButton.setEnabled(true);
                }
                else{
                    payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.basic_button_unclick));
                    payButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        payButton.setOnClickListener(v -> {
            /*new Thread() {
                public void run() {
                    keys[0] = "";
                    keys[1] = "";
                    result = HttpPostData.POST("account/getInfo/", keys, data);

                    if (result.equals("") || result.equals("-1") || result.equals("SEND_FAIL")) {

                    } else {

                    }
                }
            }.start();*/
        });
    }

    @SuppressLint("HandlerLeak")
    Handler timeHandler = new Handler() {
        @SuppressLint({"HandlerLeak", "SetTextI18n"})
        public void handleMessage(Message msg) {
            dateView.setText(dateString);
        }
    };
}