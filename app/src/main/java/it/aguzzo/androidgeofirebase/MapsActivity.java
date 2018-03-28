package it.aguzzo.androidgeofirebase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.aguzzo.androidgeofirebase.infowindow.CentralInfoWindowAdapter;
import it.aguzzo.androidgeofirebase.infowindow.DefaultInfoWindowAdapter;
import it.aguzzo.androidgeofirebase.model.Favourite;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;

    //Play Services Location
    private static final  int MY_PERMISSION_REQUEST_CODE= 1234;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 4321;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    DatabaseReference refMyLocation;
    DatabaseReference refMyFavourites;
    DatabaseReference refMyFavouritesData;
    StorageReference myStorageRef;
    GeoFire geoFireMyLocation;
    GeoFire geoFireMyFavourites;
    Marker mCurrent;
    List<Marker> mListMarkerCasa;
    List<Marker> mListMarkerLavoro;
    VerticalSeekBar mSeekbar;
    Menu mOptionsMenu;

    Map<String, GoogleMap.InfoWindowAdapter> adapterMap;

    boolean showCasa;
    boolean showLavoro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        checkLocationService();

        mapFragment.getMapAsync(this);

        mSeekbar = findViewById(R.id.verticalSeekBar);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(i), 2000, null);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        refMyLocation = FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFireMyLocation = new GeoFire(refMyLocation);

        refMyFavourites = FirebaseDatabase.getInstance().getReference("MyFavourites");
        geoFireMyFavourites = new GeoFire(refMyFavourites);

        refMyFavouritesData = FirebaseDatabase.getInstance().getReference("MyFavouritesData");

         myStorageRef = FirebaseStorage.getInstance().getReference();

        adapterMap = new HashMap<>();
        mListMarkerCasa = new ArrayList<>();
        mListMarkerLavoro = new ArrayList<>();

        //initializeMyFavourites();
        setUpLocation();
    }

    private void initializeMyFavourites() {

        refMyFavouritesData.push().setValue(new Favourite("casa", "questa è la mia casa", "via Tembien, 3", "sconfitti.jpg", BitmapDescriptorFactory.HUE_GREEN, Color.GREEN));
        refMyFavouritesData.push().setValue(new Favourite("lavoro", "questo è il mio lavoro", "via Barberini, 38", "fronte_principale.jpg", BitmapDescriptorFactory.HUE_BLUE, Color.BLUE));

        refMyFavouritesData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot myFavouriteSnapshot : dataSnapshot.getChildren()){
                    Favourite favourite = myFavouriteSnapshot.getValue(Favourite.class);
                    if("casa".equalsIgnoreCase(favourite.getType())){
                        geoFireMyFavourites.setLocation(myFavouriteSnapshot.getKey(), new GeoLocation(41.928936, 12.524968),
                                new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {
                                    }
                                });
                    }
                    else{
                        geoFireMyFavourites.setLocation(myFavouriteSnapshot.getKey(), new GeoLocation(41.904039, 12.491666),
                                new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("EDMTDEV", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void setUpLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //Request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        else{
            if(checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkPlayServices()){
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();

                    }
                }
                break;
        }
    }

    private void displayLocation() {
        if(!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null){
                final double latitude = mLastLocation.getLatitude();
                final double longitude = mLastLocation.getLongitude();

                //Update to Firebase
                geoFireMyLocation.setLocation("You", new GeoLocation(latitude, longitude),
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            //Add Marker
                            if(mCurrent != null){
                                mCurrent.remove();
                            }
                            mCurrent = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .title("You"));

                            adapterMap.put(mCurrent.getId(), new DefaultInfoWindowAdapter());

                            //Move Camera to this position
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
                        }
                    }
                );

                Log.d("EDMTDEV", String.format("Your location was changed : %f / %f", latitude, longitude));
            }
            else{
                Log.d("EDMTDEV", "Can not get your location");
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(googleAPI.isUserResolvableError(resultCode)){
                googleAPI.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else{

                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void checkLocationService() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //Request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        else{
            LocationManager locationManager =  (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            if(locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final MapsActivity thisMapsActivity = this;

        CentralInfoWindowAdapter centralInfoWindowAdapter= new CentralInfoWindowAdapter(adapterMap);
        mMap.setInfoWindowAdapter(centralInfoWindowAdapter);

        refMyFavouritesData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot myFavouriteSnapshot : dataSnapshot.getChildren()){
                    geoFireMyFavourites. getLocation(myFavouriteSnapshot.getKey(), new MyLocationCallback(geoFireMyLocation, myStorageRef, myFavouriteSnapshot.getValue(Favourite.class),
                            thisMapsActivity, mMap, adapterMap, mListMarkerCasa, mListMarkerLavoro));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("EDMTDEV", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        if(!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);

        showCasa = true;
        showLavoro = true;

        menu.findItem(R.id.itemMapMenuCasa).setChecked(showCasa);
        menu.findItem(R.id.itemMapMenuLavoro).setChecked(showLavoro);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemMapMenuCasa:
                showCasa = !showCasa;
                mOptionsMenu.findItem(R.id.itemMapMenuCasa).setChecked(showCasa);
                for (Marker marker : mListMarkerCasa) {
                    marker.setVisible(showCasa);
                }
                return true;
            case R.id.itemMapMenuLavoro:
                showLavoro = !showLavoro;
                mOptionsMenu.findItem(R.id.itemMapMenuLavoro).setChecked(showLavoro);
                for (Marker marker : mListMarkerLavoro) {
                    marker.setVisible(showLavoro);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}