package it.aguzzo.androidgeofirebase;

import android.graphics.Color;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;

/**
 * Created by GZZNGL83P24A323Y on 23/03/2018.
 */

public class MyLocationCallback implements LocationCallback {

    private GeoFire geoFireMyLocation;

    private String myFavouriteType;

    private int strokeColor;

    private int fillColor;

    private MapsActivity mapsActivity;

    private GoogleMap map;

    public MyLocationCallback(GeoFire geoFireMyLocation, String myFavouriteType, int strokeColor, int fillColor, MapsActivity mapsActivity, GoogleMap map) {
        this.geoFireMyLocation = geoFireMyLocation;
        this.myFavouriteType = myFavouriteType;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.mapsActivity = mapsActivity;
        this.map = map;
    }

    @Override
    public void onLocationResult(String key, GeoLocation location) {

        map.addCircle(new CircleOptions()
                .center(new LatLng(location.latitude, location.longitude))
                .radius(500)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
                .strokeWidth(5.0f)
        );

        //Add GeoQueryLavoro here
        //0.5f => 500m
        GeoQuery geoQuery = geoFireMyLocation.queryAtLocation(location, 0.5f);
        geoQuery.addGeoQueryEventListener(new MyFavouriteEventListener(myFavouriteType, mapsActivity));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
