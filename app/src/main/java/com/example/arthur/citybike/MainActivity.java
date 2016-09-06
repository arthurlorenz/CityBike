package com.example.arthur.citybike;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            OnMapReadyCallback,
            GoogleMap.OnMyLocationButtonClickListener,
            ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ArrayList<Station> stationList;
    private ArrayList<MarkerOptions> markerList = new ArrayList<>();
    private final static String URL_CITY_BIKE = "http://dynamisch.citybikewien.at/citybike_xml.php?json";
    private Intent copyrightIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Test", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            new DownloadStations().execute(new URL(URL_CITY_BIKE));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_copyright) {
            copyrightIntent = new Intent(MainActivity.this, CopyrightActivity.class);
            MainActivity.this.startActivity(copyrightIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //maps activity
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

        enableMyLocation();

        setUpMap(googleMap);


    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    private void setStations(JSONArray stations) {
        JSONArray stationsJSON = stations;
        stationList = new ArrayList<>();

        if (stationsJSON != null) {

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

                    //add station to ArrayList
                    stationList.add(station);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setMarkerList(ArrayList<Station> arrayList) {
        for(int i = 0; i < arrayList.size(); i++) {
            this.markerList.add(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(arrayList.get(i).getLatitude()), Double.parseDouble(arrayList.get(i).getLongitude())))
                    .title(arrayList.get(i).getStationName())
                    .snippet("Freie Räder: "+arrayList.get(i).getBikesAvailable()+" Freie Boxen: "+arrayList.get(i).getBoxesAvailable()));
        }
    }

    private void setMarker(ArrayList<MarkerOptions> markerList) {
        for(int i = 0; i < markerList.size(); i++) {
            this.mMap.addMarker(markerList.get(i));
        }
    }

    private void setUpMap(GoogleMap googleMap) {

       /* // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();
*/

        //mMap.setPadding(0, 50, 0, 0);
        mMap.setOnMyLocationButtonClickListener(this);

        double latitude = 48.209272;
        double longitude = 16.372801;
        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        //set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Show the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    private class DownloadStations extends AsyncTask<URL, Integer, Long> {

        JSONArray stations;

        @Override
        protected Long doInBackground(URL... params) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) params[0].openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
                stations = new JSONArray(total.toString());

            } catch (IOException | JSONException e) {
                Log.e("getStations",Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            setStations(getStationJSON());
            setMarkerList(stationList);
            setMarker(markerList);
        }

        private JSONArray getStationJSON() {
            return stations;
        }

    }
}
