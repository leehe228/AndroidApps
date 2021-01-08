package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TutorialActivity extends AppCompatActivity {

    int step;
    String educationData, subjectData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_turorial);

        /* 로고 애니메이션 */
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        ImageView imgview = (ImageView)findViewById(R.id.imageview_logo);
        imgview.startAnimation(animation);

        /* 인스턴스화 */
        TextView tv_title = (TextView)findViewById(R.id.tutorial_title);
        TextView tv_con1 = (TextView)findViewById(R.id.textview_contents1);
        TextView tv_con2 = (TextView)findViewById(R.id.textview_contents2);
        Button bt_next = (Button)findViewById(R.id.button_next);

        /* 진행 정보 입력받기 */
        Intent intent = getIntent();
        step = intent.getExtras().getInt("step");
        System.out.println(step);

        tv_con1.setVisibility(View.VISIBLE);
        tv_con2.setVisibility(View.VISIBLE);

        if(step == 1){
            tv_title.setText("안녕하세요!");
            tv_con1.setText("인공지능 개인 튜터는 여러분이 공부를");
            tv_con2.setText("효율적으로 할 수 있도록 도와주고 싶어합니다.");
        }
        else if(step == 2){
            tv_title.setText("몇 가지 조사를 할 거예요");
            tv_con1.setText("여러분에게 맞춤형 서비스를 제공하기 위함이니");
            tv_con2.setText("다음 질문에 따라 솔직하게 답해주세요.");
        }
        else if(step == 3){
            educationData = intent.getExtras().getString("educationData");
            subjectData = intent.getExtras().getString("subjectData");
            tv_title.setText("여러분의 취향은 무엇인가요");
            tv_con1.setText("과목별 선호도와 개인적으로 집중되는 시간을");
            tv_con2.setText("솔직하게 답변해주세요");
        }
        else if(step == 4){
            tv_title.setText("이제 시작해볼까요?");
            tv_con1.setVisibility(View.INVISIBLE);
            tv_con2.setVisibility(View.INVISIBLE);
        }

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(step == 1){
                    Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                    intent.putExtra("step", 2);
                    startActivity(intent);
                    finish();
                }
                else if(step == 2){
                    Intent intent = new Intent(getApplicationContext(), SelectSubjectActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(step == 3){
                    Intent intent = new Intent(getApplicationContext(), GetPrefSubjectActivity.class);
                    startActivity(intent);
                    finish();
                }else if(step == 4){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "튜토리얼을 마쳐야 합니다!", Toast.LENGTH_LONG).show();
        return;
    }
}