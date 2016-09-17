package heartbeat.social.tcs.socialhb.activity.modules.quiz_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.sqliteDb.QuizDBHelper;

public class QuizQusAnsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_qus_ans);

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
        getSupportActionBar().setTitle("Quiz");


        QuizDBHelper quizDBHelper = new QuizDBHelper(getApplicationContext());
        Toast.makeText(getApplicationContext(), "Quiz Records : " + quizDBHelper.getQuizStartDataCountTrial(), Toast.LENGTH_LONG).show();

    }


    public void getLimitedAmountQuizQus()
    {

        QuizDBHelper quizDBHelper = new QuizDBHelper(getApplicationContext());

        String url = Web_API_Config.get_limited_qus_with_ans_without_right_ans + quizDBHelper.getQuizStartingData().getNo_of_qus();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


          Volley.newRequestQueue(this).add(jsonArrayRequest);


    }


}
