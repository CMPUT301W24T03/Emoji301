package com.example.emojibrite;


import static org.hamcrest.Matchers.not;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class EventDetailsTesting {


    @Rule
    public ActivityTestRule<EventDetailsActivity> activityRule =
            new ActivityTestRule<>(EventDetailsActivity.class, true, false);

    @Before
    public void setUp() throws InterruptedException {

    }

    /**
     * Test to see if all the fields are visible
     */
    @Test
    public void eventDetailsDisplayTest() {
        // Create mock event and user objects
        Users mockUser = new Users();
        mockUser.setProfileUid("testUid");
        mockUser.setName("testName");

        Event mockEvent = new Event();
        mockEvent.setId("TESTINGID123");
        mockEvent.setEventTitle("Event Title");
        mockEvent.setLocation("Location");
        mockEvent.setOrganizer(mockUser.getProfileUid());

        // Launch the Activity with the required intents
        Intent intent = new Intent();
        intent.putExtra("eventId", mockEvent.getId());
        intent.putExtra("userObject", mockUser);
        intent.putExtra("privilege", "1");
        activityRule.launchActivity(intent);

        Espresso.onView(ViewMatchers.withId(R.id.Event_Title))
                .check(new TextViewTextLogger());

        // Your existing assertion
        Espresso.onView(ViewMatchers.withId(R.id.Event_Title))
                .check(ViewAssertions.matches(ViewMatchers.withText("Event Title")));

        // Check if the location is displayed
        Espresso.onView(ViewMatchers.withId(R.id.location)) // Replace with actual ID
                .check(ViewAssertions.matches(ViewMatchers.withText("Location")));


    }

    /**
     * Test to see if buttons are visible for organizers which is not sign up
     */
    @Test
    public void ButtonViewTestOrganizer() {
        // Create mock event and user objects
        Users mockUser = new Users();
        mockUser.setProfileUid("testUid");
        mockUser.setName("testName");

        Event mockEvent = new Event();
        mockEvent.setId("TESTINGID123");
        mockEvent.setEventTitle("Event Title");
        mockEvent.setLocation("Location");
        mockEvent.setOrganizer(mockUser.getProfileUid());

        // Launch the Activity with the required intents
        Intent intent = new Intent();
        intent.putExtra("eventId", mockEvent.getId());
        intent.putExtra("userObject", mockUser);
        intent.putExtra("privilege", "1");
        activityRule.launchActivity(intent);

        // Check if attendees and notification buttons are visible
        Espresso.onView(ViewMatchers.withId(R.id.attendees_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.Notification_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

//        Espresso.onView(ViewMatchers.withId(R.id.sign_up_button))
//                .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed()))); //cannot view it

        Espresso.onView(ViewMatchers.withId(R.id.check_in_qr))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));


        Espresso.onView(ViewMatchers.withId(R.id.event_qr))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }










}
