package com.example.emojibrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class EventDatabaseTesting {

    private FirebaseFirestore db;
    private Database database;
    private Event testEvent;
    private Users user;

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
        database = new Database(db);

        user = new Users();

        user = new Users();
        user.setProfileUid("testUid");
        user.setName("testName");

        testEvent= new Event();

        testEvent = new Event();
        testEvent.setId("TESTINGID123");
        testEvent.setEventTitle("EVENT TEST 1");
        testEvent.setCapacity(12);
        testEvent.setLocation("Location 1");
        testEvent.setOrganizer(user.getProfileUid());
    }


    /**
     * Tests that the addEvent method successfully adds an event to the database.
     * Verifies that the event document is created with the correct title, ID, capacity, and organizer.
     *
     * Preconditions:
     * - The Firestore database is accessible and writable.
     * - No existing event with the same ID as testEvent.
     *
     * Postconditions:
     * - An event document corresponding to testEvent is created in the database.
     * - The fields of the created event document match those of testEvent.
     */
    @Test
    public void TestAddEvent() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        database.addEvent(testEvent, task -> {
            if (task.isSuccessful()) {
                db.collection("Events").document(testEvent.getId()).get()
                        .addOnCompleteListener(fetchTask -> {
                            if (fetchTask.isSuccessful()) {
                                DocumentSnapshot document = fetchTask.getResult();
                                assertTrue(document.exists());assertTrue(document.exists());
                                assertEquals(testEvent.getEventTitle(), document.getString("eventTitle"));
                                assertEquals(testEvent.getId(),document.getString("id"));
                                assertEquals(testEvent.getLocation(),document.getString("location"));
                                assertEquals(testEvent.getOrganizer(),document.getString("organizer"));


                            } else {
                                task.getException().printStackTrace();
                                fail("Add event failed with exception: " + task.getException().getMessage());
                                latch.countDown();
                            }
                            latch.countDown();
                        });
            } else {
                fail("Add event failed");
                latch.countDown();
            }
        });

        // Wait for the Firestore operation to complete or timeout
        latch.await(10, TimeUnit.SECONDS);
    }

    /**
     * Deletes the event after the testing of it
     * @throws InterruptedException
     */
    @After
    public void tearDown() throws InterruptedException {
        CountDownLatch deleteLatch = new CountDownLatch(1);

        db.collection("Events").document(testEvent.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Document successfully deleted!");
                    deleteLatch.countDown();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    fail("Deletion failed: " + e.getMessage());
                    deleteLatch.countDown();
                });

        deleteLatch.await(10, TimeUnit.SECONDS); // Wait for the delete operation to complete
    }

}
