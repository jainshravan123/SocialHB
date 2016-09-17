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
import heartbeat.social.tcs.socialhb.activity.Dashboard;
import heartbeat.social.tcs.socialhb.adapter.CSRInitAdapter;
import heartbeat.social.tcs.socialhb.adapter.ModuleAdapter;
import heartbeat.social.tcs.socialhb.bean.CSRInit;
import heartbeat.social.tcs.socialhb.bean.Module;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.network.CheckInternetConnection;
import heartbeat.social.tcs.socialhb.utility.CSRInitModuleSelector;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;

public class CSRInitModule extends AppCompatActivity {

    ProgressBar prgBar1;
    RecyclerView recyclerView1;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    ArrayList<CSRInit> csr_init_categories;
    StaggeredGridLayoutManager mStaggeredLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csrmodule);

        mDrawerLayout        = (DrawerLayout) findViewById(R.id.drawer_layout);
        prgBar1              = (ProgressBar) findViewById(R.id.prgBar1);
        recyclerView1        = (RecyclerView) findViewById(R.id.recycleView1);
        toolbar              = (Toolbar) findViewById(R.id.toolbar);
        csr_init_categories  = new ArrayList<CSRInit>();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("CSR");


        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        checkInternetConnection.showNetworkIdentifier(getApplicationContext(), CSRInitModule.this);

        if(checkInternetConnection.checkingInternetConnection(getApplicationContext())){
            //Getting Modules by hitting REST Web Service
            getCsrCategories();
        }
        else{
            prgBar1.setVisibility(View.GONE);
        }
    }





    public void getCsrCategories(){

        prgBar1.setVisibility(View.VISIBLE);

        String url = Web_API_Config.csr_init_enabled_modules;
        Log.e("URL", url);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        prgBar1.setVisibility(View.GONE);
                        recyclerView1.setVisibility(View.VISIBLE);

                        try {


                            for (int i = 0; i < response.length(); i++) {
                                JSONObject s_module  = (JSONObject) response.get(i);
                                int cat_id        = s_module.getInt("id");
                                String cat_name   = s_module.getString("cat");
                                String cat_icon   = s_module.getString("cat_icon");
                                int cat_status    = s_module.getInt("status");

                                CSRInitModuleSelector csrInitModuleSelector = new CSRInitModuleSelector();
                                String imageName = csrInitModuleSelector.getModuleNameByModuleId(cat_id);
                                String uri = "@drawable/"+imageName.toLowerCase();
                                int imageResource = getApplicationContext().getResources().getIdentifier(uri, null, getApplicationContext().getPackageName());



                                CSRInit csrInit = new CSRInit();
                                csrInit.setCsr_module_id(cat_id);
                                csrInit.setCsr_module_icon(cat_icon);
                                csrInit.setCsr_module_name(cat_name);
                                csrInit.setCsr_module_status(cat_status);
                                csrInit.setCsr_module_icon_id(imageResource);


                                if(csrInit.getCsr_module_status() == 1){
                                    csr_init_categories.add(csrInit);
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

        CSRInitAdapter csrInitAdapter = new CSRInitAdapter(csr_init_categories, getApplicationContext());

        recyclerView1.setAdapter(csrInitAdapter);

    }

}
