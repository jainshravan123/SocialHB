package heartbeat.social.tcs.socialhb.activity.modules.sub_modules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.Dashboard;
import heartbeat.social.tcs.socialhb.bean.DonateCategory;
import heartbeat.social.tcs.socialhb.bean.DonateItemStatus;
import heartbeat.social.tcs.socialhb.bean.SingleDonateItem;
import heartbeat.social.tcs.socialhb.bean.UserProfile;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.session.SessionManager;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.ProfileDBHelper;

public class DonatedItemStatusDescription extends AppCompatActivity {

    private String TAG = "DonatedItemStatusDescription";
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    private TextView donation_status, donation_cateogry, donation_amount,donation_details, donation_submitted_date, donation_view_by_admin_date, donation_last_processed_date;
    private ImageView statusImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_item_status_description);


        progressBar                  = (ProgressBar) findViewById(R.id.prgBar);
        toolbar                      = (Toolbar) findViewById(R.id.toolbar);
        linearLayout1                = (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2                = (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3                = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4                = (LinearLayout) findViewById(R.id.linearLayoutShortDetails);
        donation_status              = (TextView) findViewById(R.id.txt_circular_status);
        donation_cateogry            = (TextView) findViewById(R.id.txt_category_at);
        donation_amount              = (TextView) findViewById(R.id.txt_no_of_items);
        donation_details             = (TextView) findViewById(R.id.txt_donation_details);
        donation_submitted_date      = (TextView) findViewById(R.id.txt_created_at);
        donation_view_by_admin_date  = (TextView) findViewById(R.id.txt_start_date);
        donation_last_processed_date = (TextView) findViewById(R.id.txt_end_date);
        statusImageView              = (ImageView) findViewById(R.id.status_image_view);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Item Status");


        Intent intent1 = getIntent();
        SingleDonateItem singleDonateItem = new SingleDonateItem();
        singleDonateItem.setId(Integer.parseInt(intent1.getStringExtra("donated_item_id")));

        getSingleDonatedItemDetails(singleDonateItem);
    }

    public void getSingleDonatedItemDetails(SingleDonateItem singleDonateItem)
    {

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String user_id = dbHelper.getUserID();

        String uri = Web_API_Config.donated_item_status + user_id+"/"+singleDonateItem.getId();

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                        progressBar.setVisibility(View.GONE);
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.VISIBLE);
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.VISIBLE);
                        Log.e(TAG, "::::::: Single Donated Item Status Description ::::::");
                        Log.e(TAG, response.toString());

                        SingleDonateItem singleDonateItem = new SingleDonateItem();

                        try {
                            JSONObject donateItemObj           =  response;
                            JSONObject donateCategoryJSONObj   =  donateItemObj.getJSONObject("donateCategory");
                            JSONObject donateItemStatusJSONObj =  donateItemObj.getJSONObject("donateItemStatus");

                            String     donateItemDesc          =  donateItemObj.getString("description");
                            int        id                      =  donateItemObj.getInt("id");
                            int        noOfItems               =  donateItemObj.getInt("noOfItems");
                            String submissionDate              =  donateItemObj.getString("submission_date");
                            String viewByAdminDate             =  donateItemObj.getString("view_by_admin");
                            String lastProcessedDate           =  donateItemObj.getString("last_processed");


                            DonateCategory donateCategory  =new DonateCategory();
                            donateCategory.setId(donateCategoryJSONObj.getInt("id"));
                            donateCategory.setCategory(donateCategoryJSONObj.getString("category"));

                            DonateItemStatus donateItemStatus = new DonateItemStatus();
                            donateItemStatus.setId(donateItemStatusJSONObj.getInt("id"));
                            donateItemStatus.setStatus(donateItemStatusJSONObj.getString("status"));


                            singleDonateItem = new SingleDonateItem();
                            singleDonateItem.setId(id);
                            singleDonateItem.setNoOfItems(noOfItems);
                            singleDonateItem.setDescription(donateItemDesc);
                            singleDonateItem.setDonateItemStatus(donateItemStatus);
                            singleDonateItem.setDonateCategory(donateCategory);
                            singleDonateItem.setSubmission_date(submissionDate);
                            singleDonateItem.setView_by_admin(viewByAdminDate);
                            singleDonateItem.setLast_processed(lastProcessedDate);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        setDataWithElements(singleDonateItem);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);


    }

    private void setDataWithElements(SingleDonateItem singleDonateItem){
        donation_status.setText(singleDonateItem.getDonateItemStatus().getStatus());
        donation_cateogry.setText(singleDonateItem.getDonateCategory().getCategory());
        donation_amount.setText(String.valueOf(singleDonateItem.getNoOfItems()));
        donation_details.setText(singleDonateItem.getDescription());
        donation_submitted_date.setText(singleDonateItem.getSubmission_date());
        donation_view_by_admin_date.setText(singleDonateItem.getView_by_admin());
        donation_last_processed_date.setText(singleDonateItem.getLast_processed());


        if(singleDonateItem.getDonateItemStatus().getId() == 1){
            statusImageView.setImageResource(R.drawable.pending);
        }else if(singleDonateItem.getDonateItemStatus().getId() == 2){
            statusImageView.setImageResource(R.drawable.pending);
        }else if(singleDonateItem.getDonateItemStatus().getId() == 3){
            statusImageView.setImageResource(R.drawable.approved);
        }else if(singleDonateItem.getDonateItemStatus().getId() == 4){
            statusImageView.setImageResource(R.drawable.pending);
        }else if(singleDonateItem.getDonateItemStatus().getId() == 5){
            statusImageView.setImageResource(R.drawable.rejected);
        }

    }
}
