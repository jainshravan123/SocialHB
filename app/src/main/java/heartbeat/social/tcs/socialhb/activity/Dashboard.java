package heartbeat.social.tcs.socialhb.activity;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.nav_drawer_activity.Notifications;
import heartbeat.social.tcs.socialhb.activity.nav_drawer_activity.Profile;
import heartbeat.social.tcs.socialhb.activity.nav_drawer_activity.Settings;
import heartbeat.social.tcs.socialhb.adapter.ModuleAdapter;
import heartbeat.social.tcs.socialhb.bean.Module;
import heartbeat.social.tcs.socialhb.bean.UserProfile;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.network.CheckInternetConnection;
import heartbeat.social.tcs.socialhb.session.SessionManager;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.FirebaseTokenStorage;
import heartbeat.social.tcs.socialhb.sqliteDb.ProfileDBHelper;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;

public class Dashboard extends AppCompatActivity {

    ProgressBar                prgBar1;
    RecyclerView               recyclerView1;
    Toolbar                    toolbar1;
    DrawerLayout               mDrawerLayout;
    ArrayList<Module>          modules;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    SweetAlertDialog sAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar1          = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout     = (DrawerLayout) findViewById(R.id.drawer_layout);
        prgBar1           = (ProgressBar) findViewById(R.id.prgBar1);
        recyclerView1     = (RecyclerView) findViewById(R.id.recycleView1);
        modules           =  new ArrayList<Module>();
        setSupportActionBar(toolbar1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        checkInternetConnection.showNetworkIdentifier(getApplicationContext(), Dashboard.this);

        if(checkInternetConnection.checkingInternetConnection(getApplicationContext())){
            //Getting Modules by hitting REST Web Service

            getModules();

        }
        else{
            prgBar1.setVisibility(View.GONE);
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();


                switch(id){


                    case R.id.navigation_item_share:
                        shareAppProcess();
                        return true;


                    case R.id.navigation_item_logout:
                         logoutProcess();
                         return true;


                    case R.id.navigation_item_settings:
                        Intent setting_intent = new Intent(getApplicationContext(), Settings.class);
                        startActivity(setting_intent);
                        return true;

                    case R.id.navigation_item_home:
                        return true;

                    case R.id.navigation_item_notification:
                        Intent intent1 = new Intent(getApplicationContext(), Notifications.class);
                        startActivity(intent1);
                        return true;


                    default:
                        return true;

                }


            }
        });


        //OnClick Listener on Profile View
        View headerLayout = navigationView.getHeaderView(0);
        RelativeLayout drawer_layout_relative = (RelativeLayout) headerLayout.findViewById(R.id.drawer_layout_relative);
        drawer_layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent1 = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent1);
            }
        });

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(getApplicationContext());
        TextView txtView1 = (TextView) headerLayout.findViewById(R.id.emp_name);
        TextView txtView2 = (TextView) headerLayout.findViewById(R.id.emp_email);
        txtView1.setText(profileDBHelper.getEmpFirstName() + " "+profileDBHelper.getEmpLastName());
        txtView2.setText(profileDBHelper.getEmpEmail());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.miCompose:
                Toast.makeText(getApplicationContext(), "Compose Clicked", Toast.LENGTH_LONG).show();
                return true;
            case R.id.miProfile:
                if(mStaggeredLayoutManager.getSpanCount() == 1){
                    item.setIcon(R.drawable.ic_view_list_white_48dp);
                    mStaggeredLayoutManager.setSpanCount(2);
                }else{
                    item.setIcon(R.drawable.ic_grid_on_white_48dp);
                    mStaggeredLayoutManager.setSpanCount(1);
                }
        }

        return super.onOptionsItemSelected(item);
    }


    public void logoutProcess(){


        sAlertDialog = new SweetAlertDialog(Dashboard.this, SweetAlertDialog.PROGRESS_TYPE);
        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sAlertDialog.setTitleText("Logging Out...");
        sAlertDialog.setCancelable(false);
        sAlertDialog.show();
        //Counter for holding splash screens
        new CountDownTimer(2000,1000) {

            @Override
            public void onFinish() {

                FirebaseTokenStorage firebaseTokenStorage = new FirebaseTokenStorage(getApplicationContext());

                if(firebaseTokenStorage.checkTokenExistence()) {
                    updateIDForFirebaseToken();
                }
                else{
                    logoutCommonTasks();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }


    private void shareAppProcess() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }




    public void getModules()
    {

        prgBar1.setVisibility(View.VISIBLE);

        String url = Web_API_Config.enabled_modules;
        Log.e("URL", url);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        prgBar1.setVisibility(View.GONE);
                        recyclerView1.setVisibility(View.VISIBLE);
                        Log.d("Module Response 1", response.toString());


                        try {


                            for (int i = 0; i < response.length(); i++) {
                                JSONObject s_module  = (JSONObject) response.get(i);
                                int module_id        = s_module.getInt("id");
                                String module_name   = s_module.getString("module_name");
                                String module_icon      = s_module.getString("module_icon");
                                int module_status    = s_module.getInt("status");

                                ModuleSelector moduleSelector = new ModuleSelector();
                                String imageName = moduleSelector.getModuleNameByModuleId(module_id);
                                String uri = "@drawable/"+imageName.toLowerCase();
                                int imageResource = getApplicationContext().getResources().getIdentifier(uri, null, getApplicationContext().getPackageName());

                                Module module = new Module();
                                module.setId(module_id);
                                module.setModule_name(module_name);
                                module.setModule_icon(module_icon);
                                module.setModule_status(module_status);
                                module.setModule_icon_id(imageResource);



                                if(module.getModule_status() == 1){
                                    modules.add(module);
                                }

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


        Volley.newRequestQueue(this).add(jsonArrayRequest);



        recyclerView1.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(2);
        recyclerView1.setLayoutManager(mStaggeredLayoutManager);

        ModuleAdapter moduleAdapter = new ModuleAdapter(modules, getApplicationContext());

        recyclerView1.setAdapter(moduleAdapter);

    }

    //Updating UserId for firebase notification token
    public void updateIDForFirebaseToken()
    {

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
            jsonObject.put("user_id", 0);
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

                        sAlertDialog.dismiss();

                       logoutCommonTasks();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);


    }

    public void logoutCommonTasks()
    {
        //Deleting User Data from SQLite
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.deleteUserTable();

        //Deleting User Profile Data from SQLite
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(getApplicationContext());
        profileDBHelper.deleteUserProfileTable();

        //Deleting Firebase Token Data from SQLite
        FirebaseTokenStorage firebaseTokenStorage = new FirebaseTokenStorage(getApplicationContext());
        firebaseTokenStorage.deleteTokenTable();

        //Deleting Session
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setLogin(false);

        //Redirecting to SignIn Activity
        Intent intent1 = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent1);
        finish();
    }



}
