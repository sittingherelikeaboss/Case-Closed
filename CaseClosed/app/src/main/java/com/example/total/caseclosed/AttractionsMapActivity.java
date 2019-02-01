package com.example.total.caseclosed;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttractionsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String cityNameFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        double cityLat = 0, cityLng = 0; // Initialize latitude and longitude

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(); // Instance of Firebase Database node
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cityNameFirebase = dataSnapshot.child("cityName").getValue().toString(); // Get found address Object
                Toast.makeText(AttractionsMapActivity.this,cityNameFirebase,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed lol
            }
        });

        // We are using Geocoder to translate an address or city name, etc. into a Latitude and Longitude that will be
        // used for the LatLng function call at the bottom
//        try {
//            Geocoder gc = new Geocoder(this);
//            if(gc != null) {
//                List<Address> cityAddress = gc.getFromLocationName(cityNameFirebase, 1);
//                cityLat = cityAddress.get(0).getLatitude();
//                cityLng = cityAddress.get(0).getLongitude();
//            }
//        }
//        catch (IOException e) {
//            Log.e("LocateMe", "Could not get Geocoder data", e);
//        }

        mMap = googleMap;

        LatLng userCityChosen = new LatLng(cityLat, cityLng); // Get the first result of Geocoder List of LatLng
        //LatLng userCityChosen = new LatLng(43.65, -79.38); // Get the first result of Geocoder List of LatLng
        mMap.addMarker(new MarkerOptions().position(userCityChosen).title(cityNameFirebase));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userCityChosen));
    }
}
