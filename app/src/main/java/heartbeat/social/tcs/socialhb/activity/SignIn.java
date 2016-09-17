package heartbeat.social.tcs.socialhb.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.bean.Device;
import heartbeat.social.tcs.socialhb.bean.SignInUser;
import heartbeat.social.tcs.socialhb.bean.User;
import heartbeat.social.tcs.socialhb.bean.UserProfile;
import heartbeat.social.tcs.socialhb.bean.UserStatus;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.network.CheckInternetConnection;
import heartbeat.social.tcs.socialhb.session.SessionManager;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.FirebaseTokenStorage;
import heartbeat.social.tcs.socialhb.sqliteDb.ProfileDBHelper;

public class SignIn extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button   btnLogin;
    SweetAlertDialog sAlertDialog;
    String TAG    = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin    = (Button)   findViewById(R.id.login_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                SignInUser signInUser = new SignInUser();
                User       user       = new User();
                user.setUsername(username);
                signInUser.setUser(user);
                signInUser.setPassword(password);


                //Checking Internet Connection for SignIn Activity
                CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
                checkInternetConnection.showNetworkIdentifier(getApplicationContext(), SignIn.this);

                if(checkInternetConnection.checkingInternetConnection(getApplicationContext()))
                {

                    if(checkSignInFields(signInUser)) {

                        sAlertDialog = new SweetAlertDialog(SignIn.this, SweetAlertDialog.PROGRESS_TYPE);
                        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sAlertDialog.setTitleText("Authenticating...");
                        sAlertDialog.setCancelable(false);
                        sAlertDialog.show();

                        //calling function for signing process
                        try {
                            signInProcess(signInUser);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        String msg = checkSignInFieldsErrorMsg(signInUser);
                        new SweetAlertDialog(SignIn.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(msg)
                                .show();
                    }
                }
            }
        });

    }



    //Sign In Process using Volley
    public void signInProcess(final SignInUser sign_in_user) throws JSONException {

        String url = Web_API_Config.user_login_api;

        Log.e(TAG + "URL : ", url);

        JSONObject user_json_obj = new JSONObject();
        user_json_obj.put("username", sign_in_user.getUser().getUsername());

        final JSONObject sign_in_json_obj = new JSONObject();
        sign_in_json_obj.put("user", user_json_obj);
        sign_in_json_obj.put("password", sign_in_user.getPassword());




        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, sign_in_json_obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            Log.e(TAG, response.toString());
                            String status = response.getString("success").toString();

                            if(status.equals("1")){

                                JSONObject user_obj = response.getJSONObject("user");

                                SignInUser signInUser = new SignInUser();
                                int user_id   = Integer.parseInt(user_obj.getString("id"));
                                String emp_id = user_obj.getString("emp_id");
                                Log.e(TAG, "User ID : "+user_id);
                                Log.e(TAG, "EMP  ID : "+emp_id);
                                User user = new User();
                                user.setId(user_id);
                                user.setEmp_id(emp_id);
                                signInUser.setUser(user);

                                //Storing User Data into SQLite
                                DBHelper dbHelper = new DBHelper(getApplicationContext());
                                dbHelper.addUserData(signInUser);


                                FirebaseTokenStorage firebaseTokenStorage = new FirebaseTokenStorage(getApplicationContext());


                                if(firebaseTokenStorage.checkTokenExistence())
                                {
                                    Log.e(TAG, "Token : "+firebaseTokenStorage.getToken());
                                    //Updating UserId for firebase token
                                    updateIDForFirebaseToken();
                                }else{

                                    Log.e(TAG, "Token : " + "No Token");
                                    //Getting User Profile Data
                                    getUserProfileDataAndStoreIntoSQLiteDB();
                                }






                            }else{

                                sAlertDialog.dismiss();
                                //Getting Error Message
                                String errorMsg = response.getString("errMsg");

                                //
                                new SweetAlertDialog(SignIn.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(errorMsg)
                                        .setContentText("Try Again")
                                        .show();
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


    //Updating UserId for firebase notification token
    public void updateIDForFirebaseToken()
    {

        //Getting user id from SQLite
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String user_id    = dbHelper.getUserID();

        //Getting token from SQLite
        FirebaseTokenStorage firebaseTokenStorage = new FirebaseTokenStorage(getApplicationContext());
        String token = firebaseTokenStorage.getToken();

        //Getting Wifi Address
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo          = wifiManager.getConnectionInfo();
        String mac_id           = wInfo.getMacAddress();


        String url = Web_API_Config.firebase_update_user_id_for_registered_token_API;



        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", 0);
            jsonObject.put("user_id", user_id);
            jsonObject.put("token", token);
            jsonObject.put("mac_id", mac_id);
            jsonObject.put("success", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        Log.e(TAG, "Update Response : "+response.toString());

                        //Getting User Profile Data
                        getUserProfileDataAndStoreIntoSQLiteDB();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);


    }


    public void getUserProfileDataAndStoreIntoSQLiteDB()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String id = dbHelper.getUserID();

        String url = Web_API_Config.user_profile_api+id;

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        sAlertDialog.dismiss();

                        try {
                            int s_id                     = response.getInt("id");
                            int s_user_id                = response.getInt("user_id");
                            String s_emp_id              = response.getString("emp_id");
                            String s_first_name          = response.getString("first_name");
                            String s_last_name           = response.getString("last_name");
                            String s_home_contact_number = response.getString("home_contact_number");
                            String s_home_country_code   = response.getString("home_country_code");
                            String s_work_contact_number = response.getString("work_contact_number");
                            String s_work_country_code   = response.getString("work_country_code");
                            String s_official_email      =  response.getString("official_email");
                            String s_personal_email      = response.getString("personal_email");
                            String s_offc_add            = response.getString("offc_add");
                            String s_resedential_add     = response.getString("resedential_add");
                            String s_profile_image       = response.getString("profile_image");
                            String s_profile_back_image  = response.getString("profile_back_image");


                            UserProfile userProfile = new UserProfile();
                            userProfile.setId(s_id);
                            userProfile.setEmp_id(s_emp_id);
                            userProfile.setFirst_name(s_first_name);
                            userProfile.setLast_name(s_last_name);
                            userProfile.setOfficial_email(s_official_email);

                            //Storing User Profile Data
                            ProfileDBHelper profileDBHelper = new ProfileDBHelper(getApplicationContext());
                            profileDBHelper.addUserProfileData(userProfile);

                            //Set session
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.setLogin(true);

                            //Staring Dashboard
                            Intent intent1 = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent1);
                            finish();

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






    //Checking Empty Fields Error message in Signin Activity
    public String checkSignInFieldsErrorMsg(SignInUser signInUser){

        String msg = "";
        if((signInUser.getUser().getUsername().equals("")) && (signInUser.getPassword().equals(""))){
            msg = "Please fill both of the fields";
        }
        else if((signInUser.getUser().getUsername().equals(""))){
            msg = "Username is empty";
        }
        else if((signInUser.getPassword().equals(""))){
            msg = "Password is empty";
        }

        return msg;
    }

    //Checking Whether the SignIn fields are empty or not
    public boolean checkSignInFields(SignInUser signInUser){
        if(signInUser.getUser().getUsername().equals("") || signInUser.getPassword().equals(""))
        {
            return false;
        }
        return true;
    }
}
