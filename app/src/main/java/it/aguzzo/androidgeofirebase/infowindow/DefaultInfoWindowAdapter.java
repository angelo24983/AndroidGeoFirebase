package it.aguzzo.androidgeofirebase.infowindow;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by GZZNGL83P24A323Y on 27/03/2018.
 */

public class DefaultInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    public DefaultInfoWindowAdapter(){}

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) { return null; }
}
