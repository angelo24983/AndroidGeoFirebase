package it.aguzzo.androidgeofirebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.Random;

/**
 * Created by GZZNGL83P24A323Y on 23/03/2018.
 */

public class MyFavouriteEventListener implements GeoQueryEventListener {

    private String myFavouriteType;

    private MapsActivity mapsActivity;

    public MyFavouriteEventListener(String myFavouriteType, MapsActivity mapsActivity) {
        this.myFavouriteType = myFavouriteType;
        this.mapsActivity = mapsActivity;
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("EDMTDEV", String.format("%s entrato nella zona %s", key, myFavouriteType));
    }

    @Override
    public void onKeyExited(String key) {
        sendNotification("EDMTDEV", String.format("%s uscito dalla zona %s", key, myFavouriteType));
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        Log.d("MOVE", String.format("%s sei ancora nella zona %s [%f/%f]", key, myFavouriteType, location.latitude, location.longitude));
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Log.e("ERROR", error.toString());
    }

    private void sendNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(mapsActivity)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content);
        NotificationManager manager = (NotificationManager)mapsActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(mapsActivity, MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mapsActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        manager.notify(new Random().nextInt(), notification);
    }
}
