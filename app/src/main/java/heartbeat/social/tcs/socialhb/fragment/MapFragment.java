package heartbeat.social.tcs.socialhb.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import heartbeat.social.tcs.socialhb.bean.OfficeAddress;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.network.LocationProvider;

/**
 * Created by admin on 12/12/16.
 */
public class MapFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        LocationProvider.LocationCallback{

    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 0;

    private GoogleApiClient mGoogleApiClient;
    private LocationProvider mLocationProvider;
    private double currentLatitude;
    private double currentLongitude;

    private String TAG = "MapFragment";


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLocationProvider = new LocationProvider(getActivity(), this);
        mLocationProvider.connect();

        /*getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

            Log.e(TAG, "LAT  : "+currentLatitude);
            Log.e(TAG, "Long : "+currentLongitude);

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(currentLatitude, currentLongitude))
                        .title("Hello world"))
                        .setVisible(true);

            }
        });*/

    }

    @Override
    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        super.getMapAsync(onMapReadyCallback);
    }

    @Override
    public void handleNewLocation(final Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Log.e("My Latitude Map : ", String.valueOf(currentLatitude));

        //showDataOnMap(location);
       /* LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch(Exception ex) {}

        if(gps_enabled){
            Log.e(TAG, "Location ON");

        }else{
            getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {



                }
            });

        }*/

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        if(gps_enabled){
            nearestTcsOffice(location);
        }


    }

    public void nearestTcsOffice(final Location location){

        String uri  = Web_API_Config.nearestTcsOffcLocAPI+String.valueOf(location.getLatitude())+"/"+String .valueOf(location.getLongitude());

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                        Log.e(TAG, "______  (Start)Nearest Location Response _______");
                        Log.e(TAG, String.valueOf(response.toString()));
                        Log.e(TAG, "______  (END)Nearest Location Response _______");



                        OfficeAddress officeAddress = new OfficeAddress();

                        try {


                            officeAddress.setLatitude(response.getString("latitude"));
                            officeAddress.setLongitude(response.getString("longitude"));

                            showDataOnMap(location, officeAddress);

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

        Volley.newRequestQueue(getActivity()).add(jsonRequest);

    }

    public void showDataOnMap(final Location location, final OfficeAddress officeAddress)
    {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                double theta = currentLongitude - Double.parseDouble(officeAddress.getLongitude());
                double dist = Math.sin(deg2rad(currentLatitude)) * Math.sin(deg2rad(Double.parseDouble(officeAddress.getLongitude()))) + Math.cos(deg2rad(currentLatitude)) * Math.cos(deg2rad(Double.parseDouble(officeAddress.getLongitude()))) * Math.cos(deg2rad(theta));


                Log.e(TAG, "LAT  : " + currentLatitude);
                Log.e(TAG, "Long : " + currentLongitude);
                Log.e(TAG, "Distance " + dist);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(currentLatitude, currentLongitude))
                        .title("Your Location");

                Marker marker = mMap.addMarker(markerOptions);
                marker.setVisible(true);
                marker.showInfoWindow();


                Location location1 = location;
                // Showing the current location in Google Map
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(new LatLng(currentLatitude, currentLongitude))
                        .zoom(14)
                        .bearing(location1.getBearing())
                        .tilt(0)
                        .build();
                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                mMap.animateCamera(camUpd3);

                double officeLat = Double.parseDouble(officeAddress.getLatitude());
                double officeLng = Double.parseDouble(officeAddress.getLongitude());

                MarkerOptions officeMarkerOption = new MarkerOptions()
                        .position(new LatLng(officeLat, officeLng))
                        .title("TCS");

                Marker marker1 = mMap.addMarker(officeMarkerOption);
                marker1.setIcon(BitmapDescriptorFactory.defaultMarker(217));
                marker1.setVisible(true);
                marker1.showInfoWindow();

            }
        });
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
