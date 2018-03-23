package it.aguzzo.androidgeofirebase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;

/**
 * Created by GZZNGL83P24A323Y on 23/03/2018.
 */

public class MyLocationCallback implements LocationCallback {

    private GeoFire geoFireMyLocation;

    private String myFavouriteType;

    private MapsActivity mapsActivity;

    public MyLocationCallback(GeoFire geoFireMyLocation, String myFavouriteType, MapsActivity mapsActivity) {
        this.geoFireMyLocation = geoFireMyLocation;
        this.myFavouriteType = myFavouriteType;
        this.mapsActivity = mapsActivity;
    }

    @Override
    public void onLocationResult(String key, GeoLocation location) {
        //Add GeoQueryLavoro here
        //0.5f => 500m
        GeoQuery geoQuery = geoFireMyLocation.queryAtLocation(location, 0.5f);
        geoQuery.addGeoQueryEventListener(new MyFavouriteEventListener(myFavouriteType, mapsActivity));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
