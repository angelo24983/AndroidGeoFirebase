package it.aguzzo.androidgeofirebase.infowindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import it.aguzzo.androidgeofirebase.Favourite;
import it.aguzzo.androidgeofirebase.GlideApp;
import it.aguzzo.androidgeofirebase.R;

/**
 * Created by GZZNGL83P24A323Y on 27/03/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    private StorageReference storageReference;

    public CustomInfoWindowAdapter(Context ctx, StorageReference storageReference){
        context = ctx;
        this.storageReference = storageReference;
    }

    private final Map<Marker, Bitmap> images = new HashMap<>();
    private final Map<Marker, Target<Bitmap>> targets = new HashMap<>();

    @Override // GoogleMap.InfoWindowAdapter
    public View getInfoContents(Marker marker) {

        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info_window, null);

        Favourite favourite = (Favourite) marker.getTag();
        Bitmap image = images.get(marker);

        ImageView image_v = view.findViewById(R.id.image);

        if (image == null) {
            Glide.with(context).asBitmap().load(storageReference.child(favourite.getImage()+".jpg")).into(getTarget(marker));
            return null; // or something indicating loading
        } else {

            TextView type_tv = view.findViewById(R.id.type);
            TextView description_tv = view.findViewById(R.id.description);

            TextView address_tv = view.findViewById(R.id.address);

            type_tv.setText(marker.getTitle());
            description_tv.setText(marker.getSnippet());

            description_tv.setTextColor(favourite.getTextColor());

            address_tv.setText(favourite.getAddress());
            image_v.setImageBitmap(image);
            return view;
        }
    }
    @Override // GoogleMap.InfoWindowAdapter
    public View getInfoWindow(Marker marker) {
        return null;
    }
    private Target<Bitmap> getTarget(Marker marker) {
        Target<Bitmap> target = targets.get(marker);
        if (target == null) {
            target = new InfoTarget(marker);
        }
        return target;
    }
    private class InfoTarget extends SimpleTarget<Bitmap> {
        Marker marker;
        InfoTarget(Marker marker) {
            super(100, 100); // otherwise Glide will load original sized bitmap which is huge
            this.marker = marker;
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            // this prevents recursion, because Glide load only starts if image == null in getInfoContents
            images.put(marker, resource);
            // tell the maps API it can try to call getInfoContents again, this time finding the loaded image
            marker.showInfoWindow();
        }

        @Override // Target
        public void onLoadCleared(Drawable placeholder) {
            images.remove(marker); // clean up previous image, it became invalid
            // don't call marker.showInfoWindow() to update because this is most likely called from Glide.into()
        }
    }
}