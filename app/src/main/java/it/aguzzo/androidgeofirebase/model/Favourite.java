package it.aguzzo.androidgeofirebase.model;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GZZNGL83P24A323Y on 23/03/2018.
 */

public class Favourite {

    private String type;
    private LatLng coordinates;
    private int strokeColor;
    private int fillColor;

    public Favourite(String type, LatLng coordinates, int strokeColor, int fillColor) {
        this.type = type;
        this.coordinates = coordinates;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }
}