package it.aguzzo.androidgeofirebase.infowindow;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

/**
 * Created by GZZNGL83P24A323Y on 27/03/2018.
 */

public class CentralInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Map<String, GoogleMap.InfoWindowAdapter> adapterMap;

    public CentralInfoWindowAdapter(
            Map<String, GoogleMap.InfoWindowAdapter> adapterMap) {
        this.adapterMap = adapterMap;
    }

    @Override
    public View getInfoContents(Marker marker) {
        GoogleMap.InfoWindowAdapter adapter = adapterMap.get(marker.getId());
        return adapter.getInfoContents(marker);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        GoogleMap.InfoWindowAdapter adapter = adapterMap.get(marker.getId());
        return adapter.getInfoWindow(marker);
    }
}
