package com.example.arthur.citybike;

import android.location.Location;

/**
 * Created by arthur on 11.06.16.
 */
public class Station {

    private final static String LOCATION_PROVIDER = "Arthur Lorenz";
    private String stationName;
    private String stationDescription;
    private String bikesAvailable;
    private String boxesAvailable;
    private String latitude;
    private String longitude;
    private Location location;
    private boolean active;

    public Station(String stationName, String stationDescription, String bikesAvailable, String boxesAvailable, String latitude, String longitude, boolean active) {
        this.stationName = stationName;
        this.stationDescription = stationDescription;
        this.bikesAvailable = bikesAvailable;
        this.boxesAvailable = boxesAvailable;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
        this.updateLocation();
    }

    public String getBikesAvailable() {
        return bikesAvailable;
    }

    public void setBikesAvailable(String bikesAvailable) {
        this.bikesAvailable = bikesAvailable;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationDescription() {
        return stationDescription;
    }

    public void setStationDescription(String stationDescription) {
        this.stationDescription = stationDescription;
    }

    public String getBoxesAvailable() {
        return boxesAvailable;
    }

    public void setBoxesAvailable(String boxesAvailable) {
        this.boxesAvailable = boxesAvailable;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Station copy() {
        Station station = new Station(
                stationName,
                stationDescription,
                bikesAvailable,
                boxesAvailable,
                latitude,
                longitude,
                active);
        station.updateLocation();
        return station;
    }

    public void clear() {
        this.setStationName(null);
        this.setStationDescription(null);
        this.setBikesAvailable(null);
        this.setBoxesAvailable(null);
        this.setLocation(null);
        this.setActive(false);
        this.setLatitude(null);
        this.setLongitude(null);
    }

    public void updateLocation() {
        Location location = new Location(LOCATION_PROVIDER);

        try {
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
        } catch (NumberFormatException e) {
        }

        this.location = location;
    }

}
