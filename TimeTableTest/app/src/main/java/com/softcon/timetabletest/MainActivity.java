package com.softcon.timetabletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private float oldXvalue;
    private float oldYvalue ;
    private boolean check;

    float moveX, moveY;
    private int startX, startY;
    private int indexX, indexY;

    private int[][] a = new int[7][4];
    private int[][] b = new int[7][4];

    private int sub;

    private int maxX, maxY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        check = false;
        sub = 1;
        maxX = maxY = -1;

        /* TIME TABLE */
        final TextView t5_1 = (TextView)findViewById(R.id.timetable_5_1);
        final TextView t5_2 = (TextView)findViewById(R.id.timetable_5_2);
        final TextView t5_3 = (TextView)findViewById(R.id.timetable_5_3);
        final TextView t5_4 = (TextView)findViewById(R.id.timetable_5_4);
        final TextView t5_5 = (TextView)findViewById(R.id.timetable_5_5);
        final TextView t5_6 = (TextView)findViewById(R.id.timetable_5_6);

        final TextView t6_1 = (TextView)findViewById(R.id.timetable_6_1);
        final TextView t6_2 = (TextView)findViewById(R.id.timetable_6_2);
        final TextView t6_3 = (TextView)findViewById(R.id.timetable_6_3);
        final TextView t6_4 = (TextView)findViewById(R.id.timetable_6_4);
        final TextView t6_5 = (TextView)findViewById(R.id.timetable_6_5);
        final TextView t6_6 = (TextView)findViewById(R.id.timetable_6_6);

        final TextView t7_1 = (TextView)findViewById(R.id.timetable_7_1);
        final TextView t7_2 = (TextView)findViewById(R.id.timetable_7_2);
        final TextView t7_3 = (TextView)findViewById(R.id.timetable_7_3);
        final TextView t7_4 = (TextView)findViewById(R.id.timetable_7_4);
        final TextView t7_5 = (TextView)findViewById(R.id.timetable_7_5);
        final TextView t7_6 = (TextView)findViewById(R.id.timetable_7_6);

        final TextView t8_1 = (TextView)findViewById(R.id.timetable_8_1);
        final TextView t8_2 = (TextView)findViewById(R.id.timetable_8_2);
        final TextView t8_3 = (TextView)findViewById(R.id.timetable_8_3);
        final TextView t8_4 = (TextView)findViewById(R.id.timetable_8_4);
        final TextView t8_5 = (TextView)findViewById(R.id.timetable_8_5);
        final TextView t8_6 = (TextView)findViewById(R.id.timetable_8_6);

        final TextView locX = (TextView)findViewById(R.id.locationViewX);
        final TextView locY = (TextView)findViewById(R.id.locationViewY);

        final Button subButton = (Button)findViewById(R.id.sub);
        final Button clearButton = (Button)findViewById(R.id.clear);

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sub == 1){
                    sub = 0;
                    subButton.setText("YELLOW");
                }
                else if(sub == 0){
                    sub = 1;
                    subButton.setText("RED");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 7; i++){
                    for(int j = 0; j < 4; j++){
                        a[i][j] = 0;
                        b[i][j] = 0;
                    }
                }
            }
        });

        LinearLayout touchL = (LinearLayout)findViewById(R.id.touchZone);
        touchL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){

                int parentWidth = ((ViewGroup)v.getParent()).getWidth();    // 부모 View 의 Width
                int parentHeight = ((ViewGroup)v.getParent()).getHeight();    // 부모 View 의 Height

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    // 뷰 누름
                    oldXvalue = event.getX();
                    oldYvalue = event.getY();
                    Log.d("viewTest", "oldXvalue : "+ oldXvalue + " oldYvalue : " + oldYvalue);    // View 내부에서 터치한 지점의 상대 좌표값.
                    Log.d("viewTest", "v.getX() : "+v.getX());    // View 의 좌측 상단이 되는 지점의 절대 좌표값.
                    Log.d("viewTest", "RawX : " + event.getRawX() +" RawY : " + event.getRawY());    // View 를 터치한 지점의 절대 좌표값.
                    Log.d("viewTest", "v.getHeight : " + v.getHeight() + " v.getWidth : " + v.getWidth());    // View 의 Width, Height

                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    // 뷰 이동 중
                    //locX.setText(Integer.toString((int) (event.getRawX() - v.getX())));
                    //locY.setText(Integer.toString((int) (event.getRawY() - v.getY())));

                    int nowX = (int)(event.getRawX() - v.getX());
                    int nowY = (int) (event.getRawY() - v.getY());

                    int totalWidth = v.getWidth();
                    int totalHeight = v.getHeight();

                    int widthFric = totalWidth / 7;
                    int heightFric = totalHeight / 4;

                    indexX = nowX / widthFric;
                    indexY = nowY / heightFric;

                    if(!check){
                        check = true;
                        startX = indexX;
                        startY = indexY;
                        System.out.println("start : " + startX + ", " + startY);
                    }

                    System.out.println("start : " + startX + ", " + startY);
                    System.out.println("end : " + indexX + ", " + indexY);

                    if(maxX < indexX){
                        maxX = indexX;
                    }
                    if(maxY < indexY){
                        maxY = indexY;
                    }

                    locX.setText(Integer.toString(indexX) + "(" + Integer.toString((int) (event.getRawX() - v.getX())) + "/" + maxX + "), ");
                    locY.setText(Integer.toString(indexY) + "(" + Integer.toString((int) (event.getRawX() - v.getX())) + "/" + maxY + ")");

                    if(sub == 1){
                        if(check){
                            for(int j = startY; j <= maxY; j++){
                                if(j == startY){
                                    for(int i = 1; i < 7; i++){
                                        a[i][j] = 0;
                                    }
                                }
                                else if(j == maxY){
                                    for(int i = 1; i <= maxX; i++){
                                         a[i][j] = 0;
                                    }
                                }
                                else{
                                    for(int i = 1; i < 7; i++){
                                        a[i][j] = 0;
                                    }
                                }
                            }

                            for(int j = startY; j <= indexY; j++){
                                if(j == startY){
                                    if(indexY == startY){
                                        for(int i = startX; i <= indexX; i++){
                                            a[i][j] = 1;
                                        }
                                    }
                                    else{
                                        for(int i = startX; i < 7; i++){
                                            a[i][j] = 1;
                                        }
                                    }
                                }
                                else if(j == indexY){
                                    for(int i = 1; i <= indexX; i++){
                                        a[i][j] = 1;
                                    }
                                }
                                else{
                                    for(int i = 1; i < 7; i++){
                                        a[i][j] = 1;
                                    }
                                }
                            }
                        }
                    }
                    else if(sub == 0){
                        if(check) {
                            for(int j = startY; j <= maxY; j++){
                                if(j == startY){
                                    for(int i = 1; i < 7; i++){
                                        b[i][j] = 0;
                                    }
                                }
                                else if(j == maxY){
                                    for(int i = 1; i <= maxX; i++){
                                        b[i][j] = 0;
                                    }
                                }
                                else{
                                    for(int i = 1; i < 7; i++){
                                        b[i][j] = 0;
                                    }
                                }
                            }

                            for (int j = startY; j <= indexY; j++) {
                                if (j == startY) {
                                    if (indexY == startY) {
                                        for (int i = startX; i <= indexX; i++) {
                                            b[i][j] = 1;
                                        }
                                    } else {
                                        for (int i = startX; i < 7; i++) {
                                            b[i][j] = 1;
                                        }
                                    }
                                } else if (j == indexY) {
                                    for (int i = 1; i <= indexX; i++) {
                                        b[i][j] = 1;
                                    }
                                } else {
                                    for (int i = 1; i < 7; i++) {
                                        b[i][j] = 1;
                                    }
                                }
                            }
                        }
                    }

                    if(a[1][0] == 1){
                        t5_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[1][0] == 1){
                        t5_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t5_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[2][0] == 1){
                        t5_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[2][0] == 1){
                        t5_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t5_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[3][0] == 1){
                        t5_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[3][0] == 1){
                        t5_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t5_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[4][0] == 1){
                        t5_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[4][0] == 1){
                        t5_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t5_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[5][0] == 1){
                        t5_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[5][0] == 1){
                        t5_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t5_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[6][0] == 1){
                        t5_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[6][0] == 1){
                        t5_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t5_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }

                    if(a[1][1] == 1){
                        t6_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[1][1] == 1){
                        t6_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t6_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[2][1] == 1){
                        t6_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[2][1] == 1){
                        t6_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t6_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[3][1] == 1){
                        t6_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[3][1] == 1){
                        t6_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t6_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[4][1] == 1){
                        t6_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[4][1] == 1){
                        t6_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t6_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[5][1] == 1){
                        t6_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[5][1] == 1){
                        t6_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t6_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[6][1] == 1){
                        t6_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[6][1] == 1){
                        t6_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t6_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }

                    if(a[1][2] == 1){
                        t7_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[1][2] == 1){
                        t7_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t7_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[2][2] == 1){
                        t7_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[2][2] == 1){
                        t7_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t7_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[3][2] == 1){
                        t7_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[3][2] == 1){
                        t7_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t7_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[4][2] == 1){
                        t7_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[4][2] == 1){
                        t7_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t7_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[5][2] == 1){
                        t7_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[5][2] == 1){
                        t7_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t7_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[6][2] == 1){
                        t7_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[6][2] == 1){
                        t7_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t7_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }

                    if(a[1][3] == 1){
                        t8_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[1][3] == 1){
                        t8_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t8_1.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[2][3] == 1){
                        t8_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[2][3] == 1){
                        t8_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t8_2.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[3][3] == 1){
                        t8_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[3][3] == 1){
                        t8_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t8_3.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[4][3] == 1){
                        t8_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[4][3] == 1){
                        t8_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t8_4.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[5][3] == 1){
                        t8_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[5][3] == 1){
                        t8_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t8_5.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }
                    if(a[6][3] == 1){
                        t8_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_pink));
                    }
                    else if(b[6][3] == 1){
                        t8_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.sub_yellow));
                    }
                    else{
                        t8_6.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.basic_timetable));
                    }

                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    check = false;
                    maxX = maxY = -1;
                }
                return true;
            }
        });
    }
}