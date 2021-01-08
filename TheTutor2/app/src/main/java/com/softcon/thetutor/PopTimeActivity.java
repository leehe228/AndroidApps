package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PopTimeActivity extends Activity {

    int time;
    String s_time;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_time);

        Intent intent = getIntent();

        /* 정보 받아오기 */
        type = intent.getExtras().getString("type");
        time = intent.getExtras().getInt("time");
        s_time = Integer.toString(time) + "분";

        /* 버튼 정보 링킹 */
        Button okButton = (Button)findViewById(R.id.button_ok);
        Button minusButton = (Button)findViewById(R.id.button_minus);
        Button plusButton = (Button)findViewById(R.id.button_plus);

        final TextView tv_time = (TextView)findViewById(R.id.textview_min);
        final TextView tv_title = (TextView)findViewById(R.id.popup_title);

        tv_time.setText(s_time);

        if(type.equals("study")){
            tv_title.setText("공부 시간 설정");
        }
        else if(type.equals("rest")){
            tv_title.setText("쉬는 시간 설정");
        }

        /* 증가 */
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("study")){
                    if(time < 120){
                        time = time + 5;
                    }
                }
                else if(type.equals("rest")){
                    if(time < 30){
                        time = time + 5;
                    }
                }
                s_time = Integer.toString(time) + "분";
                tv_time.setText(s_time);
            }
        });

        /* 감소 */
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("study")){
                    if(time > 30){
                        time = time - 5;
                    }
                }
                else if(type.equals("rest")){
                    if(time > 5){
                        time = time - 5;
                    }
                }
                s_time = Integer.toString(time) + "분";
                tv_time.setText(s_time);
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", Integer.toString(time));
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}