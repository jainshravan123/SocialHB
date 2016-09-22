package heartbeat.social.tcs.socialhb.activity.modules.quiz_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.bean.QuizAnswer;
import heartbeat.social.tcs.socialhb.bean.QuizQuestion;
import heartbeat.social.tcs.socialhb.bean.QuizQuestionAnswer;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.sqliteDb.QuizDBHelper;

public class QuizQusAnsActivity extends AppCompatActivity {

    Toolbar toolbar;
    String TAG = "QuizQusAnsActivity";
    ArrayList<QuizQuestionAnswer> quiz_questions_list;
    LinearLayout linearLayout1;
    ScrollView   scrollView1;
    ProgressBar  progressBar;
    TextView     txt_question;
    RadioGroup   radio_group_options;
    AppCompatButton btn_exit, btn_next;
    int question_number = 0;
    List<View> removeViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_qus_ans);

        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        scrollView1   = (ScrollView) findViewById(R.id.quiz_back_scroll_view);
        progressBar   = (ProgressBar) findViewById(R.id.prgBar);
        toolbar       = (Toolbar) findViewById(R.id.toolbar);
        txt_question  = (TextView) findViewById(R.id.txt_question);
        radio_group_options = (RadioGroup) findViewById(R.id.radio_group_options);
        btn_exit      = (AppCompatButton) findViewById(R.id.btn_exit);
        btn_next      = (AppCompatButton) findViewById(R.id.btn_next);
        removeViews   = new ArrayList<View>();
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

        try {
            getLimitedAmountQuizQus();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getLimitedAmountQuizQus() throws JSONException
    {

        QuizDBHelper quizDBHelper = new QuizDBHelper(getApplicationContext());

        String url = Web_API_Config.get_limited_qus_with_ans_without_right_ans + quizDBHelper.getQuizStartingData().getNo_of_qus();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                            Log.e(TAG, "Response : "+response.toString());
                            quiz_questions_list = new ArrayList<QuizQuestionAnswer>();

                            for(int i=0; i< response.length(); i++){
                                try {

                                    QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();

                                    JSONObject jsonObject = (JSONObject) response.get(i);

                                    JSONObject jsonObjectQus = jsonObject.getJSONObject("quizQuestion");
                                    QuizQuestion quizQuestion = new QuizQuestion();
                                    quizQuestion.setId(jsonObjectQus.getInt("id"));
                                    quizQuestion.setQuestion(jsonObjectQus.getString("question"));

                                    ArrayList<QuizAnswer> quizAnswers_list = new ArrayList<QuizAnswer>();
                                    JSONArray jsonAnsArray = jsonObject.getJSONArray("quizAnswer_list");

                                    for(int j=0; j<jsonAnsArray.length(); j++){
                                        JSONObject jsonObject_ans = (JSONObject) jsonAnsArray.get(j);
                                        QuizAnswer quizAnswer = new QuizAnswer();
                                        quizAnswer.setId(jsonObject_ans.getInt("id"));
                                        quizAnswer.setAnswer(jsonObject_ans.getString("answer"));
                                        quizAnswers_list.add(quizAnswer);
                                    }

                                    quizQuestionAnswer.setQuizQuestion(quizQuestion);
                                    quizQuestionAnswer.setQuizAnswer_list(quizAnswers_list);

                                    quiz_questions_list.add(quizQuestionAnswer);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        setViews();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


          Volley.newRequestQueue(this).add(jsonArrayRequest);

    }


    public void setViews()
    {
        progressBar.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.VISIBLE);
        scrollView1.setVisibility(View.VISIBLE);
        //setQuestionWithOptions();

        /*for(QuizQuestionAnswer quizQuestionAnswer : quiz_questions_list)
        {
                for(QuizAnswer quizAnswer : quizQuestionAnswer.getQuizAnswer_list())
                {
                    Log.e(TAG, "Quiz ANS ID : "+quizAnswer.getId() + " : Quiz Answer : "+quizAnswer.getAnswer());
                }
        }*/
        setFirstQuestionWithOptions();
    }


    public void setFirstQuestionWithOptions(){

        txt_question.setText(String.valueOf(question_number + 1) +". " + quiz_questions_list.get(question_number).getQuizQuestion().getQuestion().toString());
        for(QuizAnswer quizAnswer : quiz_questions_list.get(question_number).getQuizAnswer_list()){
            AppCompatRadioButton appCompatRadioButton = new AppCompatRadioButton(this);
            appCompatRadioButton.setId(quizAnswer.getId());
            appCompatRadioButton.setText(quizAnswer.getAnswer());
            appCompatRadioButton.setChecked(false);
            radio_group_options.addView(appCompatRadioButton);
            removeViews.add(appCompatRadioButton);
        }
        question_number++;
        setNextButtonEvent();
    }

    public void setNextButtonEvent()
    {


            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setQuestionWithOptions();

                }
            });
    }

    public void setQuestionWithOptions(){

        for(View radio_view : removeViews)
        {
            radio_group_options.removeView(radio_view);
        }

        removeViews.clear();


        Log.e(TAG, "List Size "+quiz_questions_list.size());
        if(question_number < quiz_questions_list.size())
        {
            txt_question.setText(String.valueOf(question_number + 1) +". " + quiz_questions_list.get(question_number).getQuizQuestion().getQuestion().toString());
            for(QuizAnswer quizAnswer : quiz_questions_list.get(question_number).getQuizAnswer_list()){
                Log.e(TAG, "Answer Before Print  : "+quizAnswer.getAnswer());
                AppCompatRadioButton appCompatRadioButton = new AppCompatRadioButton(this);
                appCompatRadioButton.setId(quizAnswer.getId());
                appCompatRadioButton.setText(quizAnswer.getAnswer());
                appCompatRadioButton.setChecked(false);
                radio_group_options.addView(appCompatRadioButton);
                removeViews.add(appCompatRadioButton);
            }
            question_number++;
        }else{
            btn_next.setText("Submit");
        }

    }

}
