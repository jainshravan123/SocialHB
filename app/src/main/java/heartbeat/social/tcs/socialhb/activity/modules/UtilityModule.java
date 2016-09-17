package heartbeat.social.tcs.socialhb.activity.modules;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.adapter.CSRInitAdapter;
import heartbeat.social.tcs.socialhb.adapter.UtilityCatAdapter;
import heartbeat.social.tcs.socialhb.bean.CSRInit;
import heartbeat.social.tcs.socialhb.bean.UtilityCategory;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.network.CheckInternetConnection;
import heartbeat.social.tcs.socialhb.utility.CSRInitModuleSelector;

public class UtilityModule extends AppCompatActivity {


    ProgressBar prgBar1;
    RecyclerView recyclerView1;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    ArrayList<UtilityCategory> utilityEnabledCategories;
    StaggeredGridLayoutManager mStaggeredLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_module);


        mDrawerLayout             = (DrawerLayout) findViewById(R.id.drawer_layout);
        prgBar1                   = (ProgressBar) findViewById(R.id.prgBar1);
        recyclerView1             = (RecyclerView) findViewById(R.id.recycleView1);
        toolbar                   = (Toolbar) findViewById(R.id.toolbar);
        utilityEnabledCategories  = new ArrayList<UtilityCategory>();

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
        getSupportActionBar().setTitle("Utility");


        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        checkInternetConnection.showNetworkIdentifier(getApplicationContext(), UtilityModule.this);

        if(checkInternetConnection.checkingInternetConnection(getApplicationContext())){
            //Getting Modules by hitting REST Web Service
            getUtilityEnabledCategories();
        }
        else{
            prgBar1.setVisibility(View.GONE);
        }

    }



    public void getUtilityEnabledCategories()
    {
        prgBar1.setVisibility(View.VISIBLE);

        String url = Web_API_Config.utility_enabled_module;
        Log.e("URL", url);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        prgBar1.setVisibility(View.GONE);
                        recyclerView1.setVisibility(View.VISIBLE);

                        try {


                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject s_utility_module  = (JSONObject) response.get(i);
                                int utility_cat_id        = s_utility_module.getInt("id");
                                String utility_cat_name   = s_utility_module.getString("utility_cat_name");
                                int utility_cat_status    = s_utility_module.getInt("status");

                              /*  CSRInitModuleSelector csrInitModuleSelector = new CSRInitModuleSelector();
                                String imageName = csrInitModuleSelector.getModuleNameByModuleId(cat_id);
                                String uri = "@drawable/"+imageName.toLowerCase();
                                int imageResource = getApplicationContext().getResources().getIdentifier(uri, null, getApplicationContext().getPackageName());
*/



                                UtilityCategory utilityCategory = new UtilityCategory();
                                utilityCategory.setId(utility_cat_id);
                                utilityCategory.setUtility_cat_name(utility_cat_name);
                                utilityCategory.setStatus(utility_cat_status);

                                if(utilityCategory.getStatus() == 1){
                                    utilityEnabledCategories.add(utilityCategory);
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
        mStaggeredLayoutManager.setSpanCount(1);
        recyclerView1.setLayoutManager(mStaggeredLayoutManager);

        UtilityCatAdapter utilityCatAdapter = new UtilityCatAdapter(utilityEnabledCategories, getApplicationContext());
        recyclerView1.setAdapter(utilityCatAdapter);
    }
}
