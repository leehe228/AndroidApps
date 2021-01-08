package com.softcon.thetutorfinal;
/* * * * *
 * THE TUTOR Ver3
 * Developed by HOEUN LEE (SOFTCON INC.)
 * All Right Reserved 2020
 * * * * */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PopDdayActivity extends BaseActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String tday;
    private long calDateDays;
    private String temp;

    private int insert1, insert2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_dday);

        /* 인스턴스화 */
        ImageButton backButton = (ImageButton) findViewById(R.id.DDay_ImageButton_back);
        final EditText nameEditText = (EditText) findViewById(R.id.DDay_EditText_name);
        DatePicker datePicker = (DatePicker) findViewById(R.id.DDay_DatePicker);
        final Button okButton = (Button) findViewById(R.id.DDay_Button_done);
        final TextView ddayTextView = (TextView) findViewById(R.id.DDay_TextView_DDay);

        /* 초기화 */
        // 디데이 세팅
        SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String name_temp = pref2.getString("ddayName", "");

        // 한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        /* 오늘 날짜 가져오기 */
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
        tday = year + "-" + month + "-"  + day;

        nameEditText.setText(name_temp);

        /* DatePicker */
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
        new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                temp= String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

                try{ // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.println(tday + "/" + temp);
                    // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
                    Date FirstDate = format.parse(tday);
                    Date SecondDate = format.parse(temp);
                    System.out.println(FirstDate + "/" + SecondDate);

                    long t1 = FirstDate.getTime();
                    long t2 = SecondDate.getTime();
                    long calDate = t1 - t2;
                    System.out.println("t1 : " + t1 + " t2 : " + t2);

                    calDateDays = calDate / (24*60*60*1000);
                    System.out.println(calDateDays);
                    if(calDateDays == 0) {
                        ddayTextView.setText("오늘 이후여야 합니다.");
                        insert1 = 0;
                    }
                    else if(calDateDays > 0){
                        ddayTextView.setText("오늘 이후여야 합니다.");
                        insert1 = 0;
                    }
                    else{
                        ddayTextView.setText("D-" + Long.toString(Math.abs(calDateDays)));
                        insert1 = 1;
                    }
                    if(insert1 + insert2 == 2){
                        okButton.setBackground(ContextCompat.getDrawable(PopDdayActivity.this, R.drawable.basic_button));
                        okButton.setEnabled(true);
                    }
                    else{
                        okButton.setBackground(ContextCompat.getDrawable(PopDdayActivity.this, R.drawable.basic_button_unclick));
                        okButton.setEnabled(false);
                    }
                }
                catch(ParseException e)
                {
                    // 예외 처리
                }
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(nameEditText.getText().toString().equals("")){
                    insert2 = 0;
                }
                else{
                    insert2 = 1;
                }
                if(insert1 + insert2 == 2){
                    okButton.setBackground(ContextCompat.getDrawable(PopDdayActivity.this, R.drawable.basic_button));
                    okButton.setEnabled(true);
                }
                else{
                    okButton.setBackground(ContextCompat.getDrawable(PopDdayActivity.this, R.drawable.basic_button_unclick));
                    okButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopDdayActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopDdayActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PopDdayActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}