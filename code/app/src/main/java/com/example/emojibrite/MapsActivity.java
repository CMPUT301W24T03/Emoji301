
package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Activity for displaying the locations of attendees who have checked in to an event.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
//    private ActivityMapsBinding binding;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_maps);

        // Retrieve the Event object from the Intent
        event = (Event) getIntent().getSerializableExtra("geolocationsList");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        ImageView backButton = findViewById(R.id.floatingActionButton_back_checkin_image);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Retrieve the geolocationList from the Intent
        ArrayList<String> geolocationList = getIntent().getStringArrayListExtra("geolocationList");

        // Check if the geolocationList is null or empty
        if (geolocationList == null || geolocationList.isEmpty()) {
            // Show a message to the user and return from the method
            Toast.makeText(this, "No attendees have checked-in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Loop through the geolocationList
        for (String geolocation : geolocationList) {
            // Split the geolocation by comma
            String[] latLng = geolocation.split(",");
            // Use the first part as latitude and the second part as longitude
            double latitude = Double.parseDouble(latLng[0]);
            Log.d("LATITUDE", String.valueOf(latitude));
            double longitude = Double.parseDouble(latLng[1]);
            Log.d("LONGITUDE", String.valueOf(longitude));

            // Create a LatLng object with the latitude and longitude
            LatLng location = new LatLng(latitude, longitude);

            // Add a marker to the map at the location
            mMap.addMarker(new MarkerOptions().position(location));
        }

        // If the geolocationList is not empty, move the camera to the first location
        if (!geolocationList.isEmpty()) {
            String[] latLng = geolocationList.get(0).split(",");
            double latitude = Double.parseDouble(latLng[0]);
            double longitude = Double.parseDouble(latLng[1]);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        }
    }

    /**
     * Returns the GoogleMap object.
     * @return The GoogleMap object
     */
    public GoogleMap getMap() {
        return mMap;
    }
}