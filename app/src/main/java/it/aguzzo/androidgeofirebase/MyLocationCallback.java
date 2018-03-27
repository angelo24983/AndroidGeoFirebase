package it.aguzzo.androidgeofirebase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

import it.aguzzo.androidgeofirebase.info.CustomInfoWindowAdapter;
import it.aguzzo.androidgeofirebase.info.DefaultInfoWindowAdapter;

/**
 * Created by GZZNGL83P24A323Y on 23/03/2018.
 */

public class MyLocationCallback implements LocationCallback {

    private GeoFire geoFireMyLocation;

    private Favourite favourite;

    private MapsActivity mapsActivity;

    private GoogleMap map;

    private Map<String, GoogleMap.InfoWindowAdapter> adapterMap;

    public MyLocationCallback(GeoFire geoFireMyLocation, Favourite favourite, MapsActivity mapsActivity, GoogleMap map, Map<String, GoogleMap.InfoWindowAdapter> adapterMap) {
        this.geoFireMyLocation = geoFireMyLocation;
        this.favourite = favourite;
        this.mapsActivity = mapsActivity;
        this.map = map;
        this.adapterMap = adapterMap;
    }

    @Override
    public void onLocationResult(String key, final GeoLocation location) {

        Marker marker = map.addMarker(new MarkerOptions()
            .position(new LatLng(location.latitude, location.longitude))
            .icon(BitmapDescriptorFactory.defaultMarker(favourite.getMarkerColor()))
            .title(favourite.getType())
            .snippet(favourite.getDescription()));
        marker.setTag(favourite);

        adapterMap.put(marker.getId(), new CustomInfoWindowAdapter(mapsActivity));

        marker.showInfoWindow();

        //Add GeoQueryLavoro here
        //0.5f => 500m
        GeoQuery geoQuery = geoFireMyLocation.queryAtLocation(location, 0.5f);
        geoQuery.addGeoQueryEventListener(new MyFavouriteEventListener(favourite.getType(), mapsActivity));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}