package com.example.arthur.citybike;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ArrayList<Station> stationList;
    JSONArray stationsJSON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getStations();
        //Log.d("mainDingens", stationList.toString());

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.setPadding(0, 50, 0, 0);
            mMap.setOnMyLocationButtonClickListener(this);
        }

        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    private void getStations() {

        stationsJSON = getStationsJSON();

        if(stationsJSON != null) {

            Log.d("arraylist", String.valueOf(stationsJSON.length()));
            //fill ArrayList
            for (int i = 0; i < stationsJSON.length(); i++) {
                try {
                    JSONObject jsonObject = (JSONObject) stationsJSON.get(i);
                    boolean active = false;
                    if (jsonObject.get("status").equals("aktiv"))
                        active = true;
                    //new stations with data from JSON
                    Station station = new Station(
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getString("free_bikes"),
                            jsonObject.getString("free_boxes"),
                            jsonObject.getString("latitude"),
                            jsonObject.getString("longitude"),
                            active
                    );
                    if(stationList.add(station)) {
                    }else {
                        Log.d("arraylist", "shit");
                    };

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private JSONArray getStationsJSON() {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    URL url = new URL("http://dynamisch.citybikewien.at/citybike_xml.php?json");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                    JSONArray tempStationsJSON = new JSONArray(total.toString());

                } catch (IOException | JSONException e) {
                    Log.e("getStations",Log.getStackTraceString(e));
                }
            }
        });

        thread.start();
        return new JSONArray();
    }

}
