package edu.uw.locationdemo;

import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LOCATION";
    //client that lets you talk to the google api
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mGoogleApiClient == null) {
            //lets us connect to the api
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        //when it becomes visible; oncreate is when you create it
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    //show the given location on the screen
    public void displayLocation(Location location){
        ((TextView) findViewById(R.id.txt_lat)).setText("" + location.getLatitude());
        ((TextView) findViewById(R.id.txt_lng)).setText("" + location.getLongitude());
    }

    //when google api is connected
    @Override
    public void onConnected(Bundle bundle) {
        //get location/where am i?
        LocationRequest request = new LocationRequest();
        //whether you want to prioritize battery or accuracy of location
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //how often do i want to request location update (min)
        request.setInterval(10000);
        //(max) time between requests
        request.setFastestInterval(5000);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //request periodic updates to my location
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
        } else {
            //if they haven't requested permission yet
            //params: context, string array of stuff you want to access
            ActivityCompat.requestPermissions(this, {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        // FusedLocationProviderApi
    }

    /*
    @Override
    public void onRequestPermissionResult(int requestCode) {

    }*?

    //when google connection is stopped
    @Override
    public void onConnectionSuspended(int i) {

    }

    //when you fail to connect to the api
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //do something when the location has changed
    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            displayLocation(location);
        } else {
            Log.v(TAG, "Got null location");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
