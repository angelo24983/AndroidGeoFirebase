package it.aguzzo.androidgeofirebase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;

/**
 * Created by GZZNGL83P24A323Y on 23/03/2018.
 */

public class MyLocationCallback implements LocationCallback {

    private GeoFire geoFireMyLocation;

    private Favourite favourite;

    private float markerColor;

    private MapsActivity mapsActivity;

    private GoogleMap map;

    public MyLocationCallback(GeoFire geoFireMyLocation, Favourite favourite, float markerColor, MapsActivity mapsActivity, GoogleMap map) {
        this.geoFireMyLocation = geoFireMyLocation;
        this.favourite = favourite;
        this.markerColor = markerColor;
        this.mapsActivity = mapsActivity;
        this.map = map;
    }

    @Override
    public void onLocationResult(String key, final GeoLocation location) {

        map.addMarker(new MarkerOptions()
            .position(new LatLng(location.latitude, location.longitude))
            .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
            .title(favourite.getType())
            .snippet(favourite.getDescription()));

        //Add GeoQueryLavoro here
        //0.5f => 500m
        GeoQuery geoQuery = geoFireMyLocation.queryAtLocation(location, 0.5f);
        geoQuery.addGeoQueryEventListener(new MyFavouriteEventListener(favourite.getType(), mapsActivity));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}