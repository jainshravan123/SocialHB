package heartbeat.social.tcs.socialhb.activity.modules.quiz_activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.Dashboard;
import heartbeat.social.tcs.socialhb.activity.SignIn;
import heartbeat.social.tcs.socialhb.bean.QuizScore;
import heartbeat.social.tcs.socialhb.bean.SignInUser;
import heartbeat.social.tcs.socialhb.bean.User;
import heartbeat.social.tcs.socialhb.bean.UserProfile;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.session.SessionManager;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.FirebaseTokenStorage;
import heartbeat.social.tcs.socialhb.sqliteDb.ProfileDBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.QuizDBHelper;

public class QuizStartActivity extends AppCompatActivity {


    AppCompatButton btn_start_quiz;
    Toolbar toolbar;
    String TAG = "QuizStartActivity";
    SweetAlertDialog sAlertDialog;
    QuizScore quizScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        btn_start_quiz = (AppCompatButton) findViewById(R.id.btn_start_quiz);
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
        getSupportActionBar().setTitle("Instructions");


        QuizDBHelper check_quizDBHelper = new QuizDBHelper(getApplicationContext());
        if(check_quizDBHelper.checkQuizStartingDataExistence()){
            check_quizDBHelper.deleteQuizTable();
        }

        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sAlertDialog = new SweetAlertDialog(QuizStartActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                sAlertDialog.setTitleText("Authenticating...");
                sAlertDialog.setCancelable(false);
                sAlertDialog.show();

                try {
                    getStartingQuizData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getStartingQuizData() throws JSONException
    {

        String url = Web_API_Config.posting_starting_quiz_data;

        DBHelper dbHelper =new DBHelper(getApplicationContext());
        JSONObject user_json_obj = new JSONObject();
        user_json_obj.put("id", String.valueOf(dbHelper.getUserID()));
        user_json_obj.put("emp_id", String.valueOf(dbHelper.getEmpID()));

        final JSONObject quiz_json_obj = new JSONObject();
        quiz_json_obj.put("user", user_json_obj);


        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, quiz_json_obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                         Log.e(TAG, response.toString());
                            sAlertDialog.dismiss();

                            quizScore = new QuizScore();
                            quizScore.setArea_of_interest_cat_id(response.getInt("area_of_interest_cat_id"));
                            quizScore.setId(response.getInt("id"));
                            quizScore.setNo_of_qus(response.getInt("no_of_qus"));
                            quizScore.setQuiz_id(response.getString("quiz_id"));
                            quizScore.setScore(response.getInt("score"));
                            quizScore.setStart_time(response.getString("start_time"));
                            quizScore.setStatus(response.getInt("status"));

                            QuizDBHelper quizDBHelper = new QuizDBHelper(getApplicationContext());
                            quizDBHelper.addQuizStartingData(quizScore);

                            Intent startQuizIntent = new Intent(getApplicationContext(), QuizQusAnsActivity.class);
                            startActivity(startQuizIntent);


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


    //getting previous score details
    public void getScoreDetails()
    {

    }
}
