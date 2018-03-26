package it.aguzzo.androidgeofirebase;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by GZZNGL83P24A323Y on 23/03/2018.
 */

public class MyLocationCallback implements LocationCallback {

    private GeoFire geoFireMyLocation;

    private DatabaseReference refMyFavouritesData;

    private String myFavouriteType;

    private float markerColor;

    private MapsActivity mapsActivity;

    private GoogleMap map;

    public MyLocationCallback(GeoFire geoFireMyLocation, DatabaseReference refMyFavouritesData, String myFavouriteType, float markerColor, MapsActivity mapsActivity, GoogleMap map) {
        this.geoFireMyLocation = geoFireMyLocation;
        this.refMyFavouritesData = refMyFavouritesData;
        this.myFavouriteType = myFavouriteType;
        this.markerColor = markerColor;
        this.mapsActivity = mapsActivity;
        this.map = map;
    }

    @Override
    public void onLocationResult(String key, final GeoLocation location) {

        DatabaseReference myRef = refMyFavouritesData.child(myFavouriteType);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Favourite favourite = dataSnapshot.getValue(Favourite.class);
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.latitude, location.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                        .title(myFavouriteType)
                        .snippet(favourite.getDescription()));

                //Add GeoQueryLavoro here
                //0.5f => 500m
                GeoQuery geoQuery = geoFireMyLocation.queryAtLocation(location, 0.5f);
                geoQuery.addGeoQueryEventListener(new MyFavouriteEventListener(myFavouriteType, mapsActivity));
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("EDMTDEV", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
