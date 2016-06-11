package com.example.arthur.citybike;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by arthur on 11.06.16.
 */
public class StationList {

    private ArrayList<Station> stationArrayList;

    private void getStations(){
        try {
            URL url = new URL("http://dynamisch.citybikewien.at/citybike_xml.php?json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONObject jsonObject = new JSONObject(inputStream.toString());
            Log.d("getStations", jsonObject.toString());

        } catch (IOException | JSONException e) {
            Log.e("getStations",Log.getStackTraceString(e));
        }
    }

    public ArrayList<Station> getStationArrayList() {
        getStations();
        return stationArrayList;
    }
}
