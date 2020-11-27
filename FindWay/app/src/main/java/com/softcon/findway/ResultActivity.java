package com.softcon.findway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends BaseActivity {

    /* 결과 출력 용 */
    ArrayList<ResultDataClass> directionResultList;

    /* 승강장 혼잡도 멘트 */
    static private String[] STATION_INFO = {"", "*역이 텅 비었어요", "*역에 사람이 조금 있어요", "*역이 조금 혼잡해요", "*역이 혼잡해서 열차를 놓칠 수 있어요", "*역이 매우 혼잡해요! 열차 놓치지 않도록 주의하세요"};
    static private String[] TRAIN_INFO = {"", "*열차가 텅 비었어요", "*앉아서 갈 수 있을만큼 여유로워요", "*앉을 자리는 없지만 여유는 있어요", "*열차가 혼잡해서 조금 힘들 수 있어요", "*열차가 매우 혼잡해 못 탈 가능성이 커요"};

    /* 열차 내 혼잡도 멘트 */

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        /* 초기화 */
        directionResultList = new ArrayList<>();

        /* 인스턴스화 */
        TextView startDayTimeTextView = findViewById(R.id.Result_TextView_start);
        TextView departureTextView = findViewById(R.id.Result_TextView_departure);
        TextView arrivalTextView = findViewById(R.id.Result_TextView_arrival);

        ListView listView = findViewById(R.id.Result_ListView);

        ResultDataAdapter resultDataAdapter = new ResultDataAdapter(this, directionResultList);
        listView.setAdapter(resultDataAdapter);

        /* 화면에 출발 일자, 시각, 출발, 도착점 표시 */
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        departureTextView.setText(pref.getString("departureName", ""));
        arrivalTextView.setText(pref.getString("arrivalName", ""));

        String startDisplayString = pref.getInt("startYear", 0) + "년 "
                + pref.getInt("startMonth", 0) + "월 "
                + pref.getInt("startDay", 0) + "일 "
                + pref.getInt("startHour", 0) + "시 "
                + pref.getInt("startMin", 0) + "분";

        startDayTimeTextView.setText(startDisplayString);

        /* 초기화 테스트 */
        directionResultList.add(new ResultDataClass(2, "출발", "0", "13:54", 4, "수서", 3, 0, "가락시장 방면(오금행) 승차", "", STATION_INFO[4]));
        directionResultList.add(new ResultDataClass(3, "", "2분", "", 3, "", 0, 0, "1개 역 이동", "", TRAIN_INFO[3]));
        directionResultList.add(new ResultDataClass(1, "13:56", "0", "13:59", 2, "가락시장", 3, 8, "하차 후 도보 2분", "송파역 방면(암사행) 승차", STATION_INFO[2]));
        directionResultList.add(new ResultDataClass(3, "", "5분", "", 1, "", 0, 0, "3개 역 이동", "", TRAIN_INFO[1]));

        resultDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
        intent.putExtra("openType", "init");
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        /* 도착점 초기화 */
        editor.putString("arrivalName", "");
        editor.putString("arrivalLine", "");

        /* 출발점 초기화 */
        editor.putString("departureName", "");
        editor.putString("departureLine", "");
        editor.apply();

        startActivity(intent);
        overridePendingTransition(R.anim.slideout, R.anim.slideup_slow);
        finish();
    }
}