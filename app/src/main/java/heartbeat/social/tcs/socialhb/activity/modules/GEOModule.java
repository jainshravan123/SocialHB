package heartbeat.social.tcs.socialhb.activity.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.FactsList;
import heartbeat.social.tcs.socialhb.bean.CSRInit;
import heartbeat.social.tcs.socialhb.bean.City;
import heartbeat.social.tcs.socialhb.bean.Country;
import heartbeat.social.tcs.socialhb.bean.OfficeAddress;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;

public class GEOModule extends AppCompatActivity {

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String TAG;

    private ArrayList<OfficeAddress> officeAddressesList;
    private CSRInit areaOfInterest;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private AppCompatButton searchBtn;
    private TextView txt_area_of_interest;
    private MaterialBetterSpinner spinner_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geomodule);

        Toolbar toolbar         = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.prgBar1);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayoutAreaOfInterest);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayoutCityAndSearch);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayoutGoogleMap);
        txt_area_of_interest = (TextView) findViewById(R.id.txt_area_of_interest);
        spinner_city   = (MaterialBetterSpinner) findViewById(R.id.spinner_city);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("GEO");

        TAG = "GEOModule";
        officeAddressesList = new ArrayList<OfficeAddress>();
        areaOfInterest      = new CSRInit();

        searchBtn = (AppCompatButton) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getApplicationContext(), FactsList.class);
                startActivity(intent1);
            }
        });

        getOfficeAddresses();
    }

    public void getOfficeAddresses(){


        String uri = Web_API_Config.officeAddressAPI;

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(uri,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        getAreaOfInterest();

                        //Log.e(TAG +": Offc Addr : ", response.toString());



                        try {
                                  for(int i=0; i<response.length(); i++){

                                          OfficeAddress officeAddress = new OfficeAddress();
                                          City city                   = new City();
                                          Country country             = new Country();

                                          JSONObject jsonObject = (JSONObject) response.get(i);
                                          String address_name  = jsonObject.getString("address_name");
                                          String building_name = jsonObject.getString("building_name");
                                          String comp_name     = jsonObject.getString("comp_name");
                                          int id               = jsonObject.getInt("id");
                                          String locality_name = jsonObject.getString("locality_name");
                                          String postal_code   = jsonObject.getString("postal_code");

                                          JSONObject cityJsonObject    = jsonObject.getJSONObject("city");
                                          JSONObject countryJsonObject = cityJsonObject.getJSONObject("country");

                                          int country_id      = countryJsonObject.getInt("id");
                                          String country_name = countryJsonObject.getString("name");

                                          int city_id    = cityJsonObject.getInt("id");
                                          String city_name = cityJsonObject.getString("name");

                                          country.setId(country_id);
                                          country.setName(country_name);

                                          city.setId(city_id);
                                          city.setName(city_name);
                                          city.setCountry(country);

                                          officeAddress.setId(id);
                                          officeAddress.setAddress_name(address_name);
                                          officeAddress.setBuilding_name(building_name);
                                          officeAddress.setComp_name(comp_name);
                                          officeAddress.setLocality_name(locality_name);
                                          officeAddress.setPostal_code(postal_code);
                                          officeAddress.setCity(city);

                                          officeAddressesList.add(officeAddress);

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


        Volley.newRequestQueue(this).add(jsonArrayRequest);



    }

    public void getAreaOfInterest(){

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String user_id       = dbHelper.getUserID();
        String emp_id        = dbHelper.getEmpID();

        String uri = Web_API_Config.areaOfInterestAPI;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", user_id);
            jsonObject.put("emp_id", emp_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, uri, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        //sAlertDialog.dismiss();

                        try {
                            areaOfInterest.setCsr_module_id(response.getInt("id"));
                            areaOfInterest.setCsr_module_name(response.getString("cat"));
                            areaOfInterest.setCsr_module_status(response.getInt("status"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                       // Log.e(TAG +": AOI : ", response.toString());

                        showData();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);


    }

    public void showData(){


        Map<Integer, OfficeAddress> map = new HashMap<Integer, OfficeAddress>();
        for(OfficeAddress officeAddress : officeAddressesList){

            if(!map.containsKey(officeAddress.getCity().getId())){
                map.put(officeAddress.getCity().getId(), officeAddress);
            }
        }

        Collection<OfficeAddress> officeAddressCollection = map.values();


        Log.e(TAG, ":::::: CITIES :::::");
        for(OfficeAddress officeAddress : officeAddressCollection){
            Log.e(TAG + "City ID : ", String.valueOf(officeAddress.getCity().getId()));
            Log.e(TAG +": City Name : ", officeAddress.getCity().getName());
        }

        Log.e(TAG, ":::::: AREA OF INTEREST::::::");
        Log.e(TAG +"::: AOI :::", areaOfInterest.getCsr_module_name());
        progressBar.setVisibility(View.INVISIBLE);
        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        linearLayout3.setVisibility(View.VISIBLE);
        searchBtn.setVisibility(View.VISIBLE);

        txt_area_of_interest.setText(areaOfInterest.getCsr_module_name());
        setDataToDropDown(officeAddressCollection);
    }

    private void setDataToDropDown(Collection<OfficeAddress> officeAddressCollection){



        ArrayList<String> cities_list = new ArrayList<String>();


        for(OfficeAddress officeAddress : officeAddressCollection)
        {
            cities_list.add(officeAddress.getCity().getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.customize_drop_down_item, cities_list);
        spinner_city.setAdapter(arrayAdapter);

    }
}
