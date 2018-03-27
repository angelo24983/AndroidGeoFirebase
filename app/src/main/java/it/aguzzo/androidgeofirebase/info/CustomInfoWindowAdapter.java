package it.aguzzo.androidgeofirebase.info;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import it.aguzzo.androidgeofirebase.Favourite;
import it.aguzzo.androidgeofirebase.R;

/**
 * Created by GZZNGL83P24A323Y on 27/03/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info_window, null);

        TextView type_tv = view.findViewById(R.id.type);
        TextView description_tv = view.findViewById(R.id.description);
        ImageView image_v = view.findViewById(R.id.image);
        TextView address_tv = view.findViewById(R.id.address);

        type_tv.setText(marker.getTitle());
        description_tv.setText(marker.getSnippet());

        Favourite favourite = (Favourite) marker.getTag();

        description_tv.setTextColor(favourite.getTextColor());

        int imageId = context.getResources().getIdentifier(favourite.getImage().toLowerCase(), "drawable", context.getPackageName());
        image_v.setImageResource(imageId);

        address_tv.setText(favourite.getAddress());

        return view;
    }
}
