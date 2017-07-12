package com.example.ale.budgettracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.ale.budgettracker.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper dbh;
    double lat = 0;
    double lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        dbh = new DBHelper(this);
        GPSTracker gps = new GPSTracker(getApplicationContext());

        if(gps.canGetLocation()) {
            gps.getLocation();
            lat = gps.getLatitude(); // returns latitude
            lon = gps.getLongitude(); // returns longitude
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

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
        Bundle extras = getIntent().getExtras();
        if (!extras.getBoolean("menu")){
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng position) {
                    mMap.addMarker(new MarkerOptions().position(position).title("Custom location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    Toast.makeText(getApplicationContext(),position.latitude+" : "+position.longitude,Toast.LENGTH_SHORT).show();
                    String pos = String.valueOf(position.latitude) + "|" + String.valueOf(position.longitude);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("position", pos);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
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
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

}
