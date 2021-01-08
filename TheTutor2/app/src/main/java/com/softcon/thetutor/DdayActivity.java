package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DdayActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    private String tday;
    private long calDateDays;
    private String temp;

    Calendar minDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dday);

        // 한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        /* 디데이 세팅 */
        SharedPreferences pref2 = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        String name_temp = pref2.getString("ddayName", "");

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

        /* 인스턴스화 */
        Button okButton = (Button)findViewById(R.id.button_next);
        final EditText et_name = (EditText)findViewById(R.id.edittext_ddayname);
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        final TextView nowdday = (TextView)findViewById(R.id.tv_nowdday);
        final TextView alarm = (TextView)findViewById(R.id.tv_ddayAlarm);

        et_name.setText(name_temp);

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

                    // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
                    // 연산결과 -950400000. long type 으로 return 된다.
                    long t1 = FirstDate.getTime();
                    long t2 = SecondDate.getTime();
                    long calDate = t1 - t2;
                    System.out.println("t1 : " + t1 + " t2 : " + t2);

                    // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
                    // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
                    calDateDays = calDate / (24*60*60*1000);
                    System.out.println(calDateDays);
                    if(calDateDays == 0) {
                        nowdday.setText("오늘입니다!");
                        alarm.setText("오늘 이후여야 합니다");
                    }
                    else if(calDateDays > 0){
                        nowdday.setText("D+" + Long.toString(Math.abs(calDateDays)));
                        alarm.setText("오늘 이후여야 합니다");
                    }
                    else{
                        nowdday.setText("D-" + Long.toString(Math.abs(calDateDays)));
                        alarm.setText("");
                    }

                }
                catch(ParseException e)
                {
                    // 예외 처리
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_LONG).show();
                }
                else{
                    if(calDateDays < 0){
                        String ddayName = et_name.getText().toString();
                        //Shared Preference
                        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("dday", temp);
                        editor.putString("ddayName", ddayName);
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("mainOpenType", "dday");
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "오늘 이후여야 합니다", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        ImageButton backButton = (ImageButton)findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mainOpenType", "dday");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("mainOpenType", "dday");
        startActivity(intent);
        finish();
    }
}