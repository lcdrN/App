package com.example.no.toilette;

import android.location.Location;

import java.util.Comparator;

/**
 * Created by E146294Q on 25/03/16.
 */
public class Toilette {

    private float distance;
    private String name;
    private double latitude;
    private double longitude;
    private String commune;
    private String adresse;

    public Toilette(String name, double latitude, double longitude, String commune, String adresse) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.commune = commune;
        this.adresse = adresse;
    }

    public void setDistance(double lat2, double long2){
        Location L1 = new Location("");
        Location L2 = new Location("");
        L1.setLatitude(this.latitude);
        L1.setLongitude(this.longitude);
        L2.setLatitude(lat2);
        L2.setLongitude(long2);
        this.distance = L1.distanceTo(L2);
    }

    public float getDistance(){
        return distance;
    }

    public String distanceToString(){
        return Float.toString(this.distance/1000) + " Km";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }


}
