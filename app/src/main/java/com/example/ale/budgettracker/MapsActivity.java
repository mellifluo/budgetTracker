package com.example.ale.budgettracker;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dbh = new DBHelper(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GPSTracker gps = new GPSTracker(getApplicationContext());
        mMap.setMyLocationEnabled(true);

        double lat = 0;
        double lon = 0;
        if(gps.canGetLocation()) {
            gps.getLocation();
            lat = gps.getLatitude(); // returns latitude
            lon = gps.getLongitude(); // returns longitude
        }

        if (lat != 0 && lon != 0) {
            LatLng mylatlng = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(mylatlng).title("Tu sei qui"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatlng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lon))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
/*
        Cursor cursor = dbh.getAllAddress();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String pAddress = cursor.getString(cursor.getColumnIndex(DBHelper.POSITION));
            String nameAddress = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXPENSE_NAME));
            LatLng nlatlng =
            getLocationFromAddress(getApplicationContext(),pAddress);
            mMap.addMarker(new MarkerOptions().position(nlatlng).title(nameAddress));
            cursor.moveToNext();
        }
        cursor.close();
        */
    }
//TODO PARSER URL DI MAPS PER PRENDERE LATITUDINE E LONGITUDINE

}
