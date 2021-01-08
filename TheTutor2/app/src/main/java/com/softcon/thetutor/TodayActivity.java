package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class TodayActivity extends AppCompatActivity {

    private Button addButton;
    private Button saveButton;
    private Button deleteButton;
    private Spinner subSpinner;
    private View subColorView;
    private EditText detail;

    private ListView listView;
    private ListViewAdapter adapter;

    private int selectedSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_today);

        /* 초기화 */
        selectedSub = 0;

        /* 인스턴스화 */
        addButton = (Button)findViewById(R.id.button_add);
        saveButton = (Button)findViewById(R.id.button_save);
        deleteButton = (Button)findViewById(R.id.button_delete);
        subSpinner = (Spinner)findViewById(R.id.spinner_sub);
        subColorView = (View)findViewById(R.id.view_subColor);
        detail = (EditText)findViewById(R.id.edittext_detail);

        adapter = new ListViewAdapter();
        listView = (ListView)findViewById(R.id.ListView);

        /* ListView Adapter 연결*/
        listView.setAdapter(adapter);

        /* Spinner */
        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                parent.getItemAtPosition(position);
                selectedSub = position;
                if(position == 0){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub0));
                }
                else if(position == 1){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub1));
                }
                else if(position == 2){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub2));
                }
                else if(position == 3){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub3));
                }
                else if(position == 4){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub4));
                }
                else if(position == 5){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub5));
                }
                else if(position == 6){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub6));
                }
                else if(position == 7){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub7));
                }
                else if(position == 8){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub8));
                }
                else if(position == 9){
                    subColorView.setBackground(ContextCompat.getDrawable(TodayActivity.this, R.drawable.sub9));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        /* 추가 버튼 */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    System.out.println(selectedSub);
                    if(selectedSub == 0){
                        adapter.addItem(R.drawable.sub0, "국어", detail.getText().toString());
                    }
                    else if(selectedSub == 1){
                        adapter.addItem(R.drawable.sub1, "수학", detail.getText().toString());
                    }
                    else if(selectedSub == 2){
                        adapter.addItem(R.drawable.sub2, "사회/역사", detail.getText().toString());
                    }
                    else if(selectedSub == 3){
                        adapter.addItem(R.drawable.sub3, "경제/경영", detail.getText().toString());
                    }
                    else if(selectedSub == 4){
                        adapter.addItem(R.drawable.sub4, "기술/가정", detail.getText().toString());
                    }
                    else if(selectedSub == 5){
                        adapter.addItem(R.drawable.sub5, "영어", detail.getText().toString());
                    }
                    else if(selectedSub == 6){
                        adapter.addItem(R.drawable.sub6, "예술/체육", detail.getText().toString());
                    }
                    else if(selectedSub == 7){
                        adapter.addItem(R.drawable.sub7, "한문/외국어", detail.getText().toString());
                    }
                    else if(selectedSub == 8){
                        adapter.addItem(R.drawable.sub8, "교양", detail.getText().toString());
                    }
                    else if(selectedSub == 9){
                        adapter.addItem(R.drawable.sub9, "기타", detail.getText().toString());
                    }
                    detail.setText("");
                    /* 키보드 내리기 */
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(detail.getWindowToken(), 0);
                    /* Adapter Update */
                    adapter.notifyDataSetChanged();
                }
            }
        });

        /* 저장 버튼 */
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "시간표를 작성해주세요!", Toast.LENGTH_LONG).show();
        return;
    }
}