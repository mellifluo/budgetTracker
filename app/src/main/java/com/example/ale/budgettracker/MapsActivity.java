package com.example.ale.budgettracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.ale.budgettracker.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper dbh;
    double lat = 0;
    double lon = 0;
    private String pos;
    private FusedLocationProviderClient mFusedLocationClient;
    Marker marker;
    FloatingActionButton fab;
    boolean nogps;
    LatLng lastM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        dbh = new DBHelper(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", pos);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        fab.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermission();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                updateMap();
                            }
                        }
                    });
            Bundle extras = getIntent().getExtras();
            if (!extras.getBoolean("menu")){
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng position) {
                        if (marker != null) marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(position).title("Posizione").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        pos = String.valueOf(position.latitude) + "|" + String.valueOf(position.longitude);
                        fab.setVisibility(View.VISIBLE);

                    }
                });
            }

            Cursor cursor = dbh.getAllAddress();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String pAddress = cursor.getString(cursor.getColumnIndex(DBHelper.POSITION));
                String nameAddress = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXPENSE_NAME));
                if (!pAddress.equals("")) {
                    double nLat = Double.valueOf(pAddress.substring(0,pAddress.indexOf("|")));
                    double nLon = Double.valueOf(pAddress.substring(pAddress.indexOf("|")+1));
                    LatLng nlatlng = new LatLng(nLat,nLon);
                    mMap.addMarker(new MarkerOptions().position(nlatlng).title(nameAddress));
                    lastM = nlatlng;
                }
                cursor.moveToNext();
            }
            cursor.close();
            updateMap();
        }
    }

    private void updateMap() {
        GPSTracker gps = new GPSTracker(getApplicationContext());
        if(gps.canGetLocation()) {
            gps.getLocation();
            if (gps.getLatitude() != 0 && gps.getLongitude() != 0) {
                lat = gps.getLatitude(); // returns latitude
                lon = gps.getLongitude(); // returns longitude
            }
        }
        if (lat != 0 && lon != 0) {
            LatLng mylatlng = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatlng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lon))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else if (nogps) {
            if (lastM != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lastM));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastM, 13));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(lastM)      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Non hai attivo il tuo GPS, vuoi attivarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        nogps = true;
                        dialog.cancel();
                        updateMap();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
            finish();
        }
    }
}
