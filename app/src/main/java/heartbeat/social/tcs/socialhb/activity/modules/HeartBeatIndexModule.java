package heartbeat.social.tcs.socialhb.activity.modules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.quiz_activity.QuizStartActivity;

public class HeartBeatIndexModule extends AppCompatActivity {

    Button btn_go_to_quiz;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_beat_index_module);

        btn_go_to_quiz = (Button) findViewById(R.id.btn_go_to_quiz);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Heartbeat Index");


        btn_go_to_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), QuizStartActivity.class);
                startActivity(intent1);
            }
        });
    }
}
