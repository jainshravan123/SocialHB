package heartbeat.social.tcs.socialhb.activity.modules;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.Trial;
import heartbeat.social.tcs.socialhb.activity.modules.quiz_activity.QuizQusAnsActivity;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.FactsList;
import heartbeat.social.tcs.socialhb.bean.CSRInit;
import heartbeat.social.tcs.socialhb.bean.City;
import heartbeat.social.tcs.socialhb.bean.Country;
import heartbeat.social.tcs.socialhb.bean.Fact;
import heartbeat.social.tcs.socialhb.bean.OfficeAddress;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.fragment.MapFragment;
import heartbeat.social.tcs.socialhb.network.LocationProvider;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;

public class GEOModule extends AppCompatActivity{

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String TAG;

    private ArrayList<OfficeAddress> officeAddressesList;
    private ArrayList<CSRInit> csrInitCategories;
    private CSRInit areaOfInterest;

    private LinearLayout linearLayout1, linearLayout3;
    private Spinner spinner_city, spinner_area_of_interest;
    int flag = 0;
    private String latitude, longitude;
    private LocationProvider mLocationProvider;
    private Fragment map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geomodule);

        /*Intent intent1 = new Intent(getApplicationContext(), Trial.class);
        startActivity(intent1);*/
        //getIntent().setAction("Already created");

        Toolbar toolbar         = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.prgBar1);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayoutAreaOfInterest);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayoutGoogleMap);
        spinner_city   = (Spinner) findViewById(R.id.spinner_city);
        spinner_area_of_interest  = (Spinner) findViewById(R.id.spinner_area_of_interest);
        map = (Fragment)getFragmentManager().findFragmentById(R.id.map);
        //mLocationProvider = new LocationProvider(this, this);
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



        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled){
            progressBar.setVisibility(View.GONE);

            new SweetAlertDialog(GEOModule.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Location Off")
                    .setContentText("Please turn on the location").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(viewIntent);
                }
            }).show();

        }else{
            getOfficeAddresses();

        }

    }

   /* @Override
    protected void onResume() {

        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Log.v("Example", "Force restart");
            Intent intent = new Intent(this, GEOModule.class);
            startActivity(intent);
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);

        super.onResume();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.geo_menu, menu);
        return true;

    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.geo_search :
                                      Intent intent1 = new Intent(getApplicationContext(), FactsList.class);
                                      startActivity(intent1);
                                      return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }*/

    public void getOfficeAddresses(){


        String uri = Web_API_Config.officeAddressAPI;

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(uri,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        getAreaOfInterest();


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
                        getAllCSRInitCategories();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);


    }




    public void getAllCSRInitCategories(){

        csrInitCategories = new ArrayList<CSRInit>();

        String uri = Web_API_Config.csr_init_enabled_modules;

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(uri,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                            for(int i=0; i<response.length(); i++){

                                try {

                                    JSONObject csrJsonObject= (JSONObject) response.get(i);
                                    CSRInit csrInit = new CSRInit();
                                    csrInit.setCsr_module_id(csrJsonObject.getInt("id"));
                                    csrInit.setCsr_module_name(csrJsonObject.getString("cat"));
                                    csrInit.setCsr_module_icon(csrJsonObject.getString("cat_icon"));
                                    csrInit.setCsr_module_status(csrJsonObject.getInt("status"));

                                    csrInitCategories.add(csrInit);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                showData();
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

    public void showData(){

        for(CSRInit csrInit : csrInitCategories){
            Log.e(TAG, "Module Name : "+csrInit.getCsr_module_name());
        }

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
        Log.e(TAG + "::: AOI :::", areaOfInterest.getCsr_module_name());
        progressBar.setVisibility(View.INVISIBLE);
        linearLayout1.setVisibility(View.VISIBLE);
        linearLayout3.setVisibility(View.VISIBLE);


        //txt_area_of_interest.setText(areaOfInterest.getCsr_module_name());
        setDataToDropDown(officeAddressCollection);
    }

    private void setDataToDropDown(Collection<OfficeAddress> officeAddressCollection){



        ArrayList<String> cities_list = new ArrayList<String>();
        ArrayList<City>  cities_data  = new ArrayList<City>();


        for(OfficeAddress officeAddress : officeAddressCollection)
        {
            cities_list.add(officeAddress.getCity().getName());
            cities_data.add(officeAddress.getCity());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.customize_drop_down_item, cities_list);
        spinner_city.setAdapter(arrayAdapter);


        ArrayList<String> csr_init_categories = new ArrayList<>();
        for(CSRInit csrInit : csrInitCategories){
            csr_init_categories.add(csrInit.getCsr_module_name());
        }

        //csrInitCategories
        ArrayAdapter<String> aoiAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.customize_drop_down_item, csr_init_categories);

        //aoiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinner_area_of_interest.setAdapter(aoiAdapter);
        spinner_area_of_interest.setAdapter(aoiAdapter);

        int defaultAOIPos = 0;
        for(CSRInit csrInit : csrInitCategories){
            if(csrInit.getCsr_module_id() == areaOfInterest.getCsr_module_id()){
               break;
            }
            defaultAOIPos++;
        }

        spinner_area_of_interest.setSelection(defaultAOIPos);


        selectdData(cities_data);
    }

    public void selectdData(final ArrayList<City> cities_data){


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e(TAG, "SELECTED CITY DATA 1 : " + spinner_city.getSelectedItem().toString());
                Log.e(TAG, "SELECTED CITY DATA 2 : " + cities_data.get(position).getName());
                Log.e(TAG, "SELCTED CITY ID 3 : " + cities_data.get(position).getId());
                if (flag > 0) {
                    Intent intent1 = new Intent(getApplicationContext(), FactsList.class);
                    intent1.putExtra("selected_city", String.valueOf(cities_data.get(position).getId()));
                    //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }

                flag++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


   /* @Override
    public void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        //Log.e("My Latitude : ", String.valueOf(currentLatitude));


    }*/
}
