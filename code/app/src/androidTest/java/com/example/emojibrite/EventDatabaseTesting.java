package com.example.emojibrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import java.util.ArrayList;
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
        ArrayList<String> attendee = new ArrayList<>();
        attendee.add(user1.getProfileUid());
        testEvent.setAttendeesList(attendee);
        testEvent.setCheckInID("CHECK IN ID 1");

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

    /**
     * Testing for getting event by organizers if we pass in the first organizer from the mock up objects
     * @throws InterruptedException
     */
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

    /**
     * Testing for getting event by organizers if we pass in the first organizer from the mock up objects
     * @throws InterruptedException
     */
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

    /**
     * Test for retreiving event object by ID
     */
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

    /**
     * Test for retreiving all events from the database
     * @throws InterruptedException
     */
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
        CountDownLatch deleteLatch = new CountDownLatch(3);

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

        db.collection("SignedUp").document(testEvent1.getId()).delete()
                .addOnSuccessListener(Void -> {
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

    @Test
    public void testAddSignin() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Call addSignin method to register user1 for testEvent1
        database.addSignin(testEvent1.getId(), user1.getProfileUid());

        // Retrieve data from the SignedUp collection
        database.getSignedAttendees(testEvent1.getId(), attendees -> {
            assertNotNull("Attendees list should not be null", attendees);

            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetSignedAttendees() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Register user1 for testEvent1
        database.addSignin(testEvent1.getId(), user1.getProfileUid());

        // Allow time for the registration to process
        Thread.sleep(2000); // Adjust time if needed

        // Now test getSignedAttendees
        database.getSignedAttendees(testEvent1.getId(), attendees -> {
            assertNotNull("Attendees list should not be null", attendees);
            assertTrue("Attendees list should contain user1 ID", attendees.contains(user1.getProfileUid()));
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetSignedUpEvents() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Add user1 as an attendee to testEvent1
        database.addSignin(testEvent1.getId(), user1.getProfileUid());

        // Allow time for addSignin to complete
        Thread.sleep(2000); // Adjust time as needed

        // Now test getSignedUpEvents for user1
        database.getSignedUpEvents(user1.getProfileUid(), events -> {
            assertNotNull("List of events should not be null", events);
            assertFalse("List of events should not be empty", events.isEmpty());

            // Check if testEvent1 is in the list
            boolean foundTestEvent1 = false;
            for (Event event : events) {
                if (event.getId().equals(testEvent1.getId())) {
                    foundTestEvent1 = true;
                    break;
                }
            }

            assertTrue("testEvent1 should be in the list of signed-up events for user1", foundTestEvent1);
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetEventByCheckInID() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Assuming testEvent has a unique check-in ID set in the setUp method
        String checkInID = testEvent.getCheckInID();

        database.getEventByCheckInID(checkInID, event -> {
            assertNotNull("Event should not be null", event);
            assertEquals("Retrieved event should have the correct ID", testEvent.getId(), event.getId());
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetCheckedInEventsForUser1() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Setup: Ensure user1 is in the attendees list for testEvent in the setUp method
        // ...

        // Test execution: Request checked-in events for user1
        database.getCheckedInEvents(user1.getProfileUid(), events -> {
            assertNotNull("List of checked-in events should not be null", events);

            // Assert that the list contains the specific event user1 is checked in for
            boolean containsTestEvent = false;
            for (Event event : events) {
                if (event.getId().equals(testEvent.getId())) {
                    containsTestEvent = true;
                    break;
                }
            }

            assertTrue("List should contain event user1 is checked into", containsTestEvent);
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);  // Adjust the timeout as necessary
    }


    @Test
    public void testCheckUserInEvent_NotExists() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Use a user UID that is not added to any event's attendees list
        String nonExistingUserUid = "nonExistingUid";

        database.checkUserInEvent(nonExistingUserUid, testEvent.getId(), isUserSignedUp -> {
            assertFalse("User should not be signed up for the event", isUserSignedUp);
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS); // Adjust timeout as necessary
    }




}
