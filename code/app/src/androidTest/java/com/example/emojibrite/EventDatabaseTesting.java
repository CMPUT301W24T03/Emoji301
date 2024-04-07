package com.example.emojibrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
    private Event testEvent, testEvent1;
    private Users user, user1;

    @Before
    public void setUp() throws InterruptedException {
        db = FirebaseFirestore.getInstance();
        database = new Database(db);

        user = new Users();
        user.setProfileUid("testUid");
        user.setName("testName");



        user1 = new Users();
        user1.setProfileUid("testUid1");
        user1.setName("testName2");

        testEvent= new Event();

        testEvent = new Event();
        testEvent.setId("TESTINGID123");
        testEvent.setEventTitle("EVENT TEST 1");
        testEvent.setCapacity(12);
        testEvent.setLocation("Location 1");
        testEvent.setOrganizer(user.getProfileUid());

        //2ND EVENT

        testEvent1=new Event();
        testEvent1.setId("TESTINGID456");
        testEvent1.setEventTitle("EVENT TEST 2");
        testEvent1.setCapacity(12);
        testEvent1.setLocation("Location 2");
        testEvent1.setOrganizer(user1.getProfileUid());

        // Latch for waiting for both events to be added
        CountDownLatch addLatch = new CountDownLatch(2);

        // Add first event
        database.addEvent(testEvent, task -> {
            if (task.isSuccessful()) {
                addLatch.countDown(); // Decrement count
            } else {
                fail("Setup failed: Unable to add testEvent");
            }
        });

        // Add second event
        database.addEvent(testEvent1, task -> {
            if (task.isSuccessful()) {
                addLatch.countDown(); // Decrement count
            } else {
                fail("Setup failed: Unable to add testEvent1");
            }
        });

        // Wait for both add operations to complete
        addLatch.await(10, TimeUnit.SECONDS);



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
        final CountDownLatch latch = new CountDownLatch(1); //

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

    @Test
    public void testGetEventsByFirstOrganizer() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        database.getEventsByOrganizer(user.getProfileUid(), events -> {
            assertEquals(1, events.size());
            assertEquals("TESTINGID123", events.get(0).getId());
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetEventsBySecondOrganizer() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        database.getEventsByOrganizer(user1.getProfileUid(), events -> {
            assertEquals(1, events.size());
            assertEquals("TESTINGID456", events.get(0).getId());
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetEventById(){
        final CountDownLatch latch = new CountDownLatch(1);

        database.getEventById(testEvent.getId(), event -> {
            assertNotNull(event);
            assertEquals(event.getId(), "TESTINGID123");
            assertNotEquals(event.getId(),"TESTINGID456");
            assertEquals(event.getEventTitle(),"EVENT TEST 1");
            assertEquals(event.getOrganizer(),user.getProfileUid());
        });
    }

    @Test
    public void testFetchAllEventsDatabase() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        database.fetchAllEventsDatabase(events -> {
            // Assert that the returned list is not null
            assertNotNull(events);

            // Check for the presence of specific test events by ID
            boolean foundTestEvent = false;
            boolean foundTestEvent1 = false;
            for (Event event : events) {
                if (event.getId().equals(testEvent.getId())) foundTestEvent = true;
                if (event.getId().equals(testEvent1.getId())) foundTestEvent1 = true;
            }

            assertTrue("testEvent found", foundTestEvent);
            assertTrue("testEvent1 found", foundTestEvent1);

            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
    }







    /**
     * Deletes the event after the testing of it
     * @throws InterruptedException
     */
    @After
    public void tearDown() throws InterruptedException {
        // Set CountDownLatch for two delete operations
        CountDownLatch deleteLatch = new CountDownLatch(2);

        // Delete first event
        db.collection("Events").document(testEvent.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Document successfully deleted!");
                    deleteLatch.countDown(); // Decrement count
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    fail("Deletion failed: " + e.getMessage());
                    deleteLatch.countDown(); // Decrement count
                });

        // Delete second event
        db.collection("Events").document(testEvent1.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Document successfully deleted!");
                    deleteLatch.countDown(); // Decrement count
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    fail("Deletion failed: " + e.getMessage());
                    deleteLatch.countDown(); // Decrement count
                });

        // Wait for both delete operations to complete
        deleteLatch.await(10, TimeUnit.SECONDS);
    }


}
