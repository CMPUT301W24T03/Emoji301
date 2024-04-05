package com.example.emojibrite;

import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.google.firebase.firestore.util.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MapsTesting {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventRef = db.collection("Events");
    Event event;

    @Test
    public void testGetGeolocationsList() {
        // Define an arbitrary event ID for the test
        String testEventId = "ma5dRouZTb1v";

        try {
            // Fetch the document from Firestore based on the event ID
            Task<DocumentSnapshot> task = eventRef.document(testEventId).get();

            // Wait for the Firestore operation to complete
            DocumentSnapshot documentSnapshot = Tasks.await(task);

            // Check if the document exists
            if (documentSnapshot.exists()) {
                // Retrieve the geolocations list from the document
                ArrayList<String> geolocationsList = (ArrayList<String>) documentSnapshot.get("geolocationList");
                assertNotNull("Geolocations list should not be null", geolocationsList);
                Log.d("Geolocations", Objects.requireNonNull(geolocationsList).toString());
            } else {
                // If document does not exist, fail the test
                fail("Document with ID " + testEventId + " does not exist");
            }
        } catch (Exception e) {
            // If any exception occurs, fail the test
            fail("Error occurred: " + e.getMessage());
        }
    }


    @Test
    public void testShowMaps() {
        // Create a string representation of latitude and longitude
        String locationString = 37.7749 + "," + -122.4194;

        // Create an instance of Event and set its geolocationList
        Event event = new Event();
        ArrayList<String> geolocationList = new ArrayList<>();
        geolocationList.add(locationString);
        event.setGeolocationList(geolocationList);

        // Start activity with intent containing latitude and longitude
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("geolocationList", geolocationList);
        ActivityScenario<MapsActivity> scenario = ActivityScenario.launch(intent);

        // Use the ActivityScenario to get the GoogleMap object
        scenario.onActivity(new ActivityScenario.ActivityAction<MapsActivity>() {
            @Override
            public void perform(MapsActivity activity) {
                GoogleMap googleMap = activity.getMap();

                // Get the visible region of the map
                LatLngBounds visibleRegion = googleMap.getProjection().getVisibleRegion().latLngBounds;

                // Loop through the geolocationList
                for (String geolocation : geolocationList) {
                    // Split the geolocation by comma
                    String[] latLng = geolocation.split(",");
                    // Use the first part as latitude and the second part as longitude
                    double latitude = Double.parseDouble(latLng[0]);
                    double longitude = Double.parseDouble(latLng[1]);

                    // Create a LatLng object with the latitude and longitude
                    LatLng location = new LatLng(latitude, longitude);

                    // Check if the location is within the visible region of the map
                    assertTrue("Location should be within the visible region of the map", visibleRegion.contains(location));
                }
            }
        });
    }
}
