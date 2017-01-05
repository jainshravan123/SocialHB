package heartbeat.social.tcs.socialhb.activity.modules.sub_modules;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import heartbeat.social.tcs.socialhb.adapter.CSRInitCatDataAdapter;
import heartbeat.social.tcs.socialhb.adapter.ModuleAdapter;
import heartbeat.social.tcs.socialhb.bean.CSRInitCatData;
import heartbeat.social.tcs.socialhb.bean.Module;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.network.CheckInternetConnection;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;

public class CSRInitCategoryDescription extends AppCompatActivity {

    String module_cat_name = "";
    int    module_cat_id   = 0;

    ProgressBar                prgBar1;
    RecyclerView               recyclerView1;
    Toolbar                    toolbar;
    DrawerLayout               mDrawerLayout;
    ArrayList<CSRInitCatData>  cat_data_list;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    TextView moreInfoTxtView;
    ImageView moreInfoImgView;
    LinearLayout linearLayout;
    ImageView csr_image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csrinit_category_description);

        Intent desc_intent = getIntent();
        module_cat_id   = Integer.parseInt(desc_intent.getStringExtra("module_id"));
        module_cat_name = desc_intent.getStringExtra("module_name");

        mDrawerLayout     = (DrawerLayout) findViewById(R.id.drawer_layout);
        prgBar1           = (ProgressBar) findViewById(R.id.prgBar1);
        recyclerView1     = (RecyclerView) findViewById(R.id.recycleView1);
        moreInfoTxtView   = (TextView) findViewById(R.id.moreInfoTxtView);
        moreInfoImgView   = (ImageView) findViewById(R.id.moreInfoImgView);
        linearLayout      = (LinearLayout) findViewById(R.id.linearLayout);
        csr_image_view    = (ImageView) findViewById(R.id.csr_image_view);
        cat_data_list     =  new ArrayList<CSRInitCatData>();

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
        getSupportActionBar().setTitle(module_cat_name);


        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        checkInternetConnection.showNetworkIdentifier(getApplicationContext(), CSRInitCategoryDescription.this);

        if(checkInternetConnection.checkingInternetConnection(getApplicationContext())){
            //Getting Modules by hitting REST Web Service
            getCategoryData();
        }
        else{
            prgBar1.setVisibility(View.GONE);
        }


        moreInfoTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Web_API_Config.csr_init_more_info));
                startActivity(browserIntent);
            }
        });

        moreInfoImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Web_API_Config.csr_init_more_info));
                startActivity(browserIntent);
            }
        });

        //Getting Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        ViewGroup.LayoutParams params = csr_image_view.getLayoutParams();
        params.height = (deviceHeight * 28) / 100;
        csr_image_view.setLayoutParams(params);


        switch(module_cat_id){
            case 1:
                        csr_image_view.setImageResource(R.drawable.environment);
                        break;
            case 2:
                        csr_image_view.setImageResource(R.drawable.education);
                        break;
            case 3:
                        csr_image_view.setImageResource(R.drawable.health);
                        break;
            case 4:
                        csr_image_view.setImageResource(R.drawable.affirmative_actions);
                        break;
            default:
                        csr_image_view.setImageResource(R.drawable.environment);
                        break;
        }

    }


    public void getCategoryData(){
        prgBar1.setVisibility(View.VISIBLE);

        String url = Web_API_Config.csr_init_category_data + String.valueOf(module_cat_id);
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
                                JSONObject csrInitCategory_jsonObject = s_module.getJSONObject("csrInitCategory");

                                int cat_data_id              = s_module.getInt("id");
                                String cat_data              = s_module.getString("cat_data");
                                String cat_data_heading      = s_module.getString("cat_data_heading");
                                int cat_id                   = csrInitCategory_jsonObject.getInt("id");


                                CSRInitCatData csrInitCatData = new CSRInitCatData();
                                csrInitCatData.setId(cat_data_id);
                                csrInitCatData.setCat_id(cat_id);
                                csrInitCatData.setCat_data_heading(cat_data_heading);
                                csrInitCatData.setCat_data(cat_data);



                                cat_data_list.add(csrInitCatData);


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


        linearLayout.setVisibility(View.VISIBLE);
        recyclerView1.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        recyclerView1.setLayoutManager(mStaggeredLayoutManager);


        CSRInitCatDataAdapter csrInitCatDataAdapter = new CSRInitCatDataAdapter(cat_data_list, getApplicationContext());
        recyclerView1.setAdapter(csrInitCatDataAdapter);


    }

}
