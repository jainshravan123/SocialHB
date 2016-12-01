package heartbeat.social.tcs.socialhb.activity.modules.sub_modules;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.bean.City;
import heartbeat.social.tcs.socialhb.bean.DonateCategory;
import heartbeat.social.tcs.socialhb.bean.DonateItemStatus;
import heartbeat.social.tcs.socialhb.bean.Fact;
import heartbeat.social.tcs.socialhb.bean.HostedImage;
import heartbeat.social.tcs.socialhb.bean.HostedUrl;
import heartbeat.social.tcs.socialhb.bean.SingleDonateItem;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;

public class FactDescription extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private String   TAG = "FactDescription";
    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private TextView txtFact, txtFactCity, txtFactDescription;
    private ImageView imgFact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact_description);

        toolbar            = (Toolbar)      findViewById(R.id.toolbar);
        progressBar        = (ProgressBar)  findViewById(R.id.prgBar);
        linearLayout1      = (LinearLayout) findViewById(R.id.linearLayoutFactImg);
        linearLayout2      = (LinearLayout) findViewById(R.id.linearLayoutFactDetails);
        linearLayout3      = (LinearLayout) findViewById(R.id.linearLayoutFactDescriptionDtls);
        txtFact            = (TextView)     findViewById(R.id.txt_fact);
        txtFactCity        = (TextView)     findViewById(R.id.txt_fact_city);
        txtFactDescription = (TextView)     findViewById(R.id.txt_fact_desc);
        imgFact            = (ImageView)    findViewById(R.id.img_fact);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle("Fact Description");

        Intent intent1 = getIntent();
        Fact fact = new Fact();
        fact.setId(Integer.parseInt(intent1.getStringExtra("fact_id")));

        //Getting Screen Size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        imgFact.getLayoutParams().height  = (deviceHeight * 36)/100;



        getSingleFactDetails(fact);
    }



    public void getSingleFactDetails(final Fact fact_p){

        String uri = Web_API_Config.allFactsAPI +"/"+fact_p.getId();

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                        Fact   factObj       = new Fact();

                        try {
                            JSONObject factJsonObject  =  (JSONObject) response;

                            JSONObject cityJSONObject = factJsonObject.getJSONObject("city");
                            JSONObject hostedUrlJSONObject = factJsonObject.getJSONObject("hostedUrl");
                            JSONObject hostedImageJSONObject = factJsonObject.getJSONObject("hostedImage");

                            int fact_id = factJsonObject.getInt("id");
                            String fact = factJsonObject.getString("fact");
                            String short_desc = factJsonObject.getString("short_desc");
                            String detailed_desc = factJsonObject.getString("long_desc");



                            HostedUrl hostedUrl     = new HostedUrl();
                            HostedImage hostedImage = new HostedImage();
                            City city               = new City();

                            hostedUrl.setId(hostedImageJSONObject.getInt("id"));
                            hostedUrl.setUrl(hostedUrlJSONObject.getString("url"));

                            hostedImage.setId(hostedImageJSONObject.getInt("id"));
                            hostedImage.setImage_url(hostedImageJSONObject.getString("image_url"));

                            city.setId(cityJSONObject.getInt("id"));
                            city.setName(cityJSONObject.getString("name"));

                            factObj.setId(fact_id);
                            factObj.setFact(fact);
                            factObj.setShort_desc(short_desc);
                            factObj.setLong_desc(detailed_desc);
                            factObj.setHostedUrl(hostedUrl);
                            factObj.setHostedImage(hostedImage);
                            factObj.setCity(city);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        setDataWithElements(factObj);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }


    public void setDataWithElements(Fact fact_p){
        progressBar.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        linearLayout3.setVisibility(View.VISIBLE);
        txtFact.setText(fact_p.getFact());
        txtFactCity.setText(fact_p.getCity().getName());
        txtFactDescription.setText(fact_p.getLong_desc());
        Picasso.with(getApplicationContext()).load(fact_p.getHostedImage().getImage_url()).into(imgFact);
        Log.e(TAG,fact_p.getHostedUrl().getUrl());

    }
}
