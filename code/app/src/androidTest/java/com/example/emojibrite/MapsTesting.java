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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.google.firebase.firestore.util.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MapsTesting {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventRef = db.collection("Events");
    private Database database;
    private Event testEvent;
    private Users user;

    @Before
    public void setUp() throws InterruptedException {
        db = FirebaseFirestore.getInstance();
        database = new Database(db);

        user = new Users();
        user.setProfileUid("testUid");
        user.setName("testName");

        testEvent = new Event();
        ArrayList<String> geolocationList = new ArrayList<>();
        geolocationList.add("37.421998333333335,-122.084");
        testEvent.setGeolocationList(geolocationList);
        testEvent.setId("MapTest1");
        testEvent.setEventTitle("EVENT TEST 1");
        testEvent.setCapacity(12);
        testEvent.setLocation("Location 1");
        testEvent.setOrganizer(user.getProfileUid());

        // Latch for waiting the event to be added
        CountDownLatch addLatch = new CountDownLatch(2);

        // Add event
        database.addEvent(testEvent, task -> {
            if (task.isSuccessful()) {
                addLatch.countDown(); // Decrement count
            } else {
                Assert.fail("Setup failed: Unable to add testEvent");
            }
        });

        addLatch.await(10, TimeUnit.SECONDS);
    }


    /**
     * Verifies that the event document is created with the correct title, ID, capacity, organizer, and geolocation list.
     *
     * Preconditions:
     * - The Firestore database is accessible and writable.
     *
     * Postconditions:
     * - An event document corresponding to testEvent is created in the database.
     * - The fields of the created event document match those of testEvent.
     */
    @Test
    public void testGetGeolocationsList() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        database.addEvent(testEvent, task -> {
            if (task.isSuccessful()) {
                db.collection("Events").document(testEvent.getId()).get()
                        .addOnCompleteListener(fetchTask -> {
                            if (fetchTask.isSuccessful()) {
                                DocumentSnapshot document = fetchTask.getResult();
                                assertTrue(document.exists());
                                assertEquals(testEvent.getEventTitle(), document.getString("eventTitle"));
                                assertEquals(testEvent.getId(),document.getString("id"));
                                assertEquals(testEvent.getLocation(),document.getString("location"));
                                assertEquals(testEvent.getOrganizer(),document.getString("organizer"));
                                assertEquals(testEvent.getGeolocationList(),document.get("geolocationList"));
                            } else {
                                task.getException().printStackTrace();
                                Assert.fail("Add event failed with exception: " + task.getException().getMessage());
                                latch.countDown();
                            }
                            latch.countDown();
                        });
            } else {
                Assert.fail("Add event failed");
                latch.countDown();
            }
        });

        // Wait for the Firestore operation to complete or timeout
        latch.await(10, TimeUnit.SECONDS);
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

    /**
     * Deletes the event after the testing of it
     * @throws InterruptedException
     */
    @After
    public void tearDown() throws InterruptedException {
        // Set CountDownLatch for two delete operations
        CountDownLatch deleteLatch = new CountDownLatch(2);

        // Delete event
        db.collection("Events").document(testEvent.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Document successfully deleted!");
                    deleteLatch.countDown(); // Decrement count
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Assert.fail("Deletion failed: " + e.getMessage());
                    deleteLatch.countDown(); // Decrement count
                });

        // Wait for both delete operations to complete
        deleteLatch.await(10, TimeUnit.SECONDS);
    }



}
