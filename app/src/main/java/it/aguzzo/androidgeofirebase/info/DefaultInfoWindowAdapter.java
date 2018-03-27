package it.aguzzo.androidgeofirebase.info;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import it.aguzzo.androidgeofirebase.Favourite;
import it.aguzzo.androidgeofirebase.R;

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
