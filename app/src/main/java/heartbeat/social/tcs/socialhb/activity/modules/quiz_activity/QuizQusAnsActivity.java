package heartbeat.social.tcs.socialhb.activity.modules.quiz_activity;

import android.content.Intent;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.SignIn;
import heartbeat.social.tcs.socialhb.activity.modules.HeartBeatIndexModule;
import heartbeat.social.tcs.socialhb.bean.CSRInit;
import heartbeat.social.tcs.socialhb.bean.Module;
import heartbeat.social.tcs.socialhb.bean.QuizAnswer;
import heartbeat.social.tcs.socialhb.bean.QuizQuestion;
import heartbeat.social.tcs.socialhb.bean.QuizQuestionAnswer;
import heartbeat.social.tcs.socialhb.bean.QuizScore;
import heartbeat.social.tcs.socialhb.bean.QuizSelectedAnswer;
import heartbeat.social.tcs.socialhb.bean.SignInUser;
import heartbeat.social.tcs.socialhb.bean.User;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.FirebaseTokenStorage;
import heartbeat.social.tcs.socialhb.sqliteDb.QuizDBHelper;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;

public class QuizQusAnsActivity extends AppCompatActivity {

    Toolbar toolbar;
    String TAG = "QuizQusAnsActivity";
    ArrayList<QuizQuestionAnswer> quiz_questions_list;
    ArrayList<QuizSelectedAnswer> selected_question_answer_list;
    LinearLayout linearLayout1;
    ScrollView   scrollView1;
    ProgressBar  progressBar;
    TextView     txt_question;
    RadioGroup   radio_group_options;
    AppCompatButton btn_exit, btn_next;
    int question_number = 0;
    List<View> removeViews;
    SweetAlertDialog submit_alert_dialog;
    QuizScore quizScore_response;

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
                //finish();
                quitFromQuiz();
            }
        });
        getSupportActionBar().setTitle("Quiz");

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitFromQuiz();
            }
        });


        QuizDBHelper quizDBHelper = new QuizDBHelper(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "Quiz Records : " + quizDBHelper.getQuizStartDataCountTrial(), Toast.LENGTH_LONG).show();

        selected_question_answer_list = new ArrayList<QuizSelectedAnswer>();

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
                            Log.e(TAG, "Complete API Response : "+response.toString());
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

        txt_question.setText(String.valueOf(question_number + 1) + ". " + quiz_questions_list.get(question_number).getQuizQuestion().getQuestion().toString());
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


                    int i = 0;
                    for (QuizAnswer quizAnswer : quiz_questions_list.get(question_number - 1).getQuizAnswer_list()) {
                        if (quizAnswer.getId() == radio_group_options.getCheckedRadioButtonId()) {
                            i = 1;
                        }
                    }

                    if (i == 1) {
                        setQuestionWithOptions();
                        Log.e(TAG, "Selected Answer ID   : " + radio_group_options.getCheckedRadioButtonId());
                    } else {
                        Log.e(TAG, "No Answer Selected");
                        new SweetAlertDialog(QuizQusAnsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Please select one option")
                                .setContentText("Try Again")
                                .show();
                    }
                }
            });
    }

    public void setQuestionWithOptions(){


        QuizQuestion selected_quizQuestion = new QuizQuestion();
        QuizAnswer   selected_quizAnswer   = new QuizAnswer();
        selected_quizQuestion.setId(quiz_questions_list.get(question_number-1).getQuizQuestion().getId());
        selected_quizAnswer.setId(radio_group_options.getCheckedRadioButtonId());
        QuizSelectedAnswer quizSelectedAnswer = new QuizSelectedAnswer();
        quizSelectedAnswer.setQuizQuestion(selected_quizQuestion);
        quizSelectedAnswer.setQuizAnswer(selected_quizAnswer);

        selected_question_answer_list.add(quizSelectedAnswer);


        for(View radio_view : removeViews)
        {
            radio_group_options.removeView(radio_view);
        }

        removeViews.clear();


       // Log.e(TAG, "List Size "+quiz_questions_list.size());
        if(question_number < quiz_questions_list.size())
        {
            txt_question.setText(String.valueOf(question_number + 1) +". " + quiz_questions_list.get(question_number).getQuizQuestion().getQuestion().toString());
            for(QuizAnswer quizAnswer : quiz_questions_list.get(question_number).getQuizAnswer_list()){
               // Log.e(TAG, "Answer Before Print  : "+quizAnswer.getAnswer());
                AppCompatRadioButton appCompatRadioButton = new AppCompatRadioButton(this);
                appCompatRadioButton.setId(quizAnswer.getId());
                appCompatRadioButton.setText(quizAnswer.getAnswer());
                appCompatRadioButton.setChecked(false);
                radio_group_options.addView(appCompatRadioButton);
                removeViews.add(appCompatRadioButton);
            }
            question_number++;
            if(question_number == quiz_questions_list.size()){
                btn_next.setText("Submit");
            }
        }else{
            /*try {
                getQuizScore();
            } catch (JSONException e) {
                e.printStackTrace();
            } */
            txt_question.setText("");
            submitQuiz();
           // btn_next.setText("Submit");
        }



    }

    public void submitQuiz()
    {
        submit_alert_dialog = new SweetAlertDialog(QuizQusAnsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        submit_alert_dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        submit_alert_dialog.setTitleText("Submitting Data...");
        submit_alert_dialog.setCancelable(false);
        submit_alert_dialog.show();

        try {
            getQuizScore();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public QuizScore getQuizScore() throws JSONException {

        JSONArray jsonArray = new JSONArray();
        for(QuizSelectedAnswer quizSelectedAnswer : selected_question_answer_list){

            JSONObject jsonObject_qus_ans_obj = new JSONObject();

            JSONObject jsonObject_qus_id = new JSONObject();
            jsonObject_qus_id.put("id", quizSelectedAnswer.getQuizQuestion().getId());


            JSONObject jsonObject_ans_id = new JSONObject();
            jsonObject_ans_id.put("id", quizSelectedAnswer.getQuizAnswer().getId());

            jsonObject_qus_ans_obj.put("quizQuestion", jsonObject_qus_id);
            jsonObject_qus_ans_obj.put("quizAnswer", jsonObject_ans_id);

            jsonArray.put(jsonObject_qus_ans_obj);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("quizSelectedAnswers", jsonArray);

       String url = Web_API_Config.calculating_quiz_score;

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject score_response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.e(TAG, "Calculated Score Object : " + score_response);

                            quizScore_response = new QuizScore();
                            quizScore_response.setId(Integer.parseInt(String.valueOf(score_response.getInt("id"))));
                            quizScore_response.setScore(Integer.parseInt(String.valueOf(score_response.getInt("score"))));
                            quizScore_response.setArea_of_interest_cat_id(Integer.parseInt(String.valueOf(score_response.getInt("area_of_interest_cat_id"))));

                            submitQuizScore();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
        return quizScore_response;
    }

    public void submitQuizScore() throws JSONException {


        DBHelper dbHelper = new DBHelper(getApplicationContext());
        QuizDBHelper quizDBHelper = new QuizDBHelper(getApplicationContext());

        JSONObject user_jsonObject = new JSONObject();
        user_jsonObject.put("id", dbHelper.getUserID());
        user_jsonObject.put("emp_id", dbHelper.getEmpID());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("quiz_id", quizDBHelper.getQuizStartingData().getQuiz_id());
        jsonObject.put("user", user_jsonObject);
        jsonObject.put("score", quizScore_response.getScore());
        jsonObject.put("start_time", "06/09/16 01:06:11");
        jsonObject.put("status", 1);
        jsonObject.put("no_of_qus", quizDBHelper.getQuizStartingData().getNo_of_qus());
        jsonObject.put("area_of_interest_cat_id", quizScore_response.getArea_of_interest_cat_id());

        String url = Web_API_Config.posting_final_quiz_score;

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            if(Integer.parseInt(String.valueOf(response.getInt("status"))) == 1)
                            {
                                showSuccessMessage();
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);




    }

    public void showSuccessMessage()
    {
        submit_alert_dialog.cancel();
        SweetAlertDialog quiz_submit_successfull_dialog;
        quiz_submit_successfull_dialog = new SweetAlertDialog(QuizQusAnsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        quiz_submit_successfull_dialog.setTitleText("Successfully Completed Quiz");
        quiz_submit_successfull_dialog.setContentText("Score : " + quizScore_response.getScore());
        quiz_submit_successfull_dialog.show();

        quiz_submit_successfull_dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent quizStartIntent = new Intent(getApplicationContext(), HeartBeatIndexModule.class);
                startActivity(quizStartIntent);
            }
        });

    }


    @Override
    public void onBackPressed() {

        quitFromQuiz();

    }

    public void quitFromQuiz()
    {
        SweetAlertDialog quiz_quit_dialog;
        quiz_quit_dialog = new SweetAlertDialog(QuizQusAnsActivity.this, SweetAlertDialog.WARNING_TYPE);
        quiz_quit_dialog.setTitleText("Do you want to Quit");
        quiz_quit_dialog.setContentText("Are you Sure");
        quiz_quit_dialog.show();

        quiz_quit_dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                QuizDBHelper quizDBHelper = new QuizDBHelper(getApplicationContext());
                quizDBHelper.deleteQuizTable();

                Intent quizStartIntent = new Intent(getApplicationContext(), HeartBeatIndexModule.class);
                startActivity(quizStartIntent);
            }
        });
    }
}
