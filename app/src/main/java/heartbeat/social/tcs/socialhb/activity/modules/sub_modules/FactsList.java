package heartbeat.social.tcs.socialhb.activity.modules.sub_modules;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.adapter.DonatedItemStatusAdapter;
import heartbeat.social.tcs.socialhb.adapter.FactAdapter;
import heartbeat.social.tcs.socialhb.bean.City;
import heartbeat.social.tcs.socialhb.bean.DonateCategory;
import heartbeat.social.tcs.socialhb.bean.DonateItemStatus;
import heartbeat.social.tcs.socialhb.bean.Fact;
import heartbeat.social.tcs.socialhb.bean.HostedImage;
import heartbeat.social.tcs.socialhb.bean.HostedUrl;
import heartbeat.social.tcs.socialhb.bean.SingleDonateItem;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;

public class FactsList extends AppCompatActivity implements SearchView.OnQueryTextListener{


    private ArrayList<Fact> facts_list;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String TAG;
    private RecyclerView factRecycleView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private FactAdapter factAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts_list);

        toolbar         = (Toolbar) findViewById(R.id.toolbar);
        progressBar     = (ProgressBar) findViewById(R.id.prgBar1);
        factRecycleView = (RecyclerView) findViewById(R.id.recycleView1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setTitle("Facts");

        facts_list = new ArrayList<Fact>();
        TAG = "FactsList";

        getAllFacts();
    }



    public void getAllFacts(){


        String uri = Web_API_Config.allFactsAPI;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(uri,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                        if(response.length() > 0){

                            for(int i=0; i<response.length(); i++){

                                try {
                                    JSONObject factJsonObject  =  (JSONObject) response.get(i);

                                    JSONObject cityJSONObject = factJsonObject.getJSONObject("city");
                                    JSONObject hostedUrlJSONObject = factJsonObject.getJSONObject("hostedUrl");
                                    JSONObject hostedImageJSONObject = factJsonObject.getJSONObject("hostedImage");

                                    int fact_id = factJsonObject.getInt("id");
                                    String fact = factJsonObject.getString("fact");
                                    String short_desc = factJsonObject.getString("short_desc");
                                    String detailed_desc = factJsonObject.getString("long_desc");


                                    Fact      factObj       = new Fact();
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

                                    facts_list.add(factObj);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        setRecycleView();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


        Volley.newRequestQueue(this).add(jsonArrayRequest);


    }


    public void setRecycleView()
    {
        progressBar.setVisibility(View.GONE);
        factRecycleView.setVisibility(View.VISIBLE);


        factRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        factRecycleView.setLayoutManager(mStaggeredLayoutManager);

        factAdapter = new FactAdapter(facts_list, getApplicationContext());

        factRecycleView.setAdapter(factAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.facts_menu, menu);

        final MenuItem item = menu.findItem(R.id.fact_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        factAdapter.setFilter(facts_list);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        final List<Fact> fact_list = filter(facts_list, query);
        factAdapter.setFilter(fact_list);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Fact> fact_list = filter(facts_list, newText);
        factAdapter.setFilter(fact_list);
        return true;
    }


    private List<Fact> filter(List<Fact> facts_p, String query) {
        query = query.toLowerCase();

        final List<Fact> facts_list = new ArrayList<>();
        for (Fact fact : facts_p) {
            final String city_text = fact.getCity().getName().toLowerCase();
            final String fact_text = fact.getFact().toLowerCase();
            if (city_text.contains(query) || fact_text.contains(query)) {
                facts_list.add(fact);
            }
        }
        return facts_list;
    }


}
