package heartbeat.social.tcs.socialhb.activity.modules.sub_modules;

import android.app.Activity;
import android.os.Bundle;
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
import heartbeat.social.tcs.socialhb.adapter.DonatedItemStatusAdapter;
import heartbeat.social.tcs.socialhb.bean.DonateCategory;
import heartbeat.social.tcs.socialhb.bean.DonateItemStatus;
import heartbeat.social.tcs.socialhb.bean.SingleDonateItem;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;

public class ViewStatus extends AppCompatActivity {


    private ArrayList<SingleDonateItem> donateItemArrayList;
    private String TAG = "ViewStatus1";
    private Toolbar toolbar;
    private RecyclerView donate_item_status_recycler_view;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status);

        toolbar                             = (Toolbar) findViewById(R.id.toolbar);
        donate_item_status_recycler_view    = (RecyclerView) findViewById(R.id.recycleView1);
        progressBar                         = (ProgressBar) findViewById(R.id.prgBar1);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Status Report");


        donateItemArrayList = new ArrayList<SingleDonateItem>();

        getFullStatusData();

    }


    public void getFullStatusData(){

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String user_id = dbHelper.getUserID();

        String uri = Web_API_Config.donated_item_status + user_id;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(uri,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                       progressBar.setVisibility(View.INVISIBLE);
                       Log.e(TAG, ":::Status Report for this user :::::::");
                       Log.e(TAG,response.toString() );

                       if(response.length() > 0){

                           for(int i=0; i<response.length(); i++){

                               try {
                                   JSONObject donateItemObj           =  (JSONObject) response.get(i);
                                   JSONObject donateCategoryJSONObj   =  donateItemObj.getJSONObject("donateCategory");
                                   JSONObject donateItemStatusJSONObj =  donateItemObj.getJSONObject("donateItemStatus");

                                   String     donateItemDesc          =  donateItemObj.getString("description");
                                   int        id                      =  donateItemObj.getInt("id");
                                   int        noOfItems               =  donateItemObj.getInt("noOfItems");


                                   DonateCategory donateCategory  =new DonateCategory();
                                   donateCategory.setId(donateCategoryJSONObj.getInt("id"));
                                   donateCategory.setCategory(donateCategoryJSONObj.getString("category"));

                                   DonateItemStatus donateItemStatus = new DonateItemStatus();
                                   donateItemStatus.setId(donateItemStatusJSONObj.getInt("id"));
                                   donateItemStatus.setStatus(donateItemStatusJSONObj.getString("status"));


                                   SingleDonateItem singleDonateItem = new SingleDonateItem();
                                   singleDonateItem.setId(id);
                                   singleDonateItem.setNoOfItems(noOfItems);
                                   singleDonateItem.setDescription(donateItemDesc);
                                   singleDonateItem.setDonateItemStatus(donateItemStatus);
                                   singleDonateItem.setDonateCategory(donateCategory);

                                   donateItemArrayList.add(singleDonateItem);

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }

                           }
                       }

                        setItemToRecycleView();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


        Volley.newRequestQueue(this).add(jsonArrayRequest);


    }

    public void setItemToRecycleView()
    {
        donate_item_status_recycler_view.setVisibility(View.VISIBLE);
        donate_item_status_recycler_view.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        donate_item_status_recycler_view.setLayoutManager(mStaggeredLayoutManager);

        DonatedItemStatusAdapter donatedItemStatusAdapter = new DonatedItemStatusAdapter(donateItemArrayList, getApplicationContext());

        donate_item_status_recycler_view.setAdapter(donatedItemStatusAdapter);
    }

}
