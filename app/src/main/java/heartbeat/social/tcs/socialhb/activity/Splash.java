package heartbeat.social.tcs.socialhb.activity;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.session.SessionManager;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //Counter for holding splash screens
        new CountDownTimer(3000,1000) {

            @Override
            public void onFinish() {

                SessionManager sessionManager = new SessionManager(getApplicationContext());

                //Checking Session
                if(sessionManager.isLoggedIn()){

                    //Opening Dashboard
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                }else{
                    //Opening User SignIn Activity
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(intent);
                    finish();
                }





            }


            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }
}
