package com.example.emojibrite;

import static androidx.core.content.ContextCompat.startActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.DrawableRes;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import com.example.emojibrite.Users;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
// This is incorrect for Espresso UI testing and can cause the error you're seeing.
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventActivityTest {
    private Users user;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference profileRef = db.collection("Users");

    @Rule
    public ActivityScenarioRule<EventHome> activityRule = new ActivityScenarioRule<>(EventHome.class);
    @Before
    public void setUp() throws InterruptedException {
        user = new Users();
        user.setProfileUid("testUid");
        user.setName("testName");
    }
    @Test
    public void testUserPassed1() {
        // Create a new user
        Users newUser = new Users();
        newUser.setProfileUid("testUid");
        newUser.setName("testName");

        // Create an instance of the Database class
        Database database = new Database(FirebaseFirestore.getInstance());

        // Store the user in the database
        database.setUserObject(newUser);

        // Retrieve the user document to verify it was stored correctly
        database.getUserDocument(newUser.getProfileUid(), documentSnapshot -> {
            // Check that the document is not null
            assertNotNull(documentSnapshot);
            // Check that the user was stored correctly
            assertEquals(newUser.getName(), documentSnapshot.getString("name"));
            assertEquals(newUser.getProfileUid(), documentSnapshot.getString("profileUid"));

            // Create an Intent to start the EventHome activity
            Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventHome.class);
            // Add the newUser object to the Intent
            intent.putExtra("userObject", newUser);
            // Start the EventHome activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationProvider.getApplicationContext().startActivity(intent);
        });
    }

    @Test
    public void testUserPassed() {
        Database database = new Database(FirebaseFirestore.getInstance());
        String profileId = "testUser";

        database.getUserDocument(profileId, documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d("DatabaseCheck", "ProfileId exists in the database");
            } else {
                Log.d("DatabaseCheck", "ProfileId does not exist in the database");
            }
        });        // Create an intent to start the EventHome activity
//        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventHome.class);
//        // Pass the Users object to the intent
//        intent.putExtra("userObject", user);
//
//        // Launch the EventHome activity with the intent
//        ActivityScenario<EventHome> scenario = ActivityScenario.launch(intent);
//        // Check if the user object is not null
//        assertNotNull(user);
//        // Check if the user object has the correct name
//        assertEquals("testName", user.getName());
//        // Check if the user object has the correct profileUid
//        assertEquals("testUid", user.getProfileUid());

    }

    @Test
    public void testAddSingleEvent() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventHome.class);
        // Pass the Users object to the intent
        intent.putExtra("userObject", user);

        // Create a bundle and put the user object into it
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        ActivityScenario<EventHome> scenario = ActivityScenario.launch(intent);
        assertNotNull(user);
        // Check if the user object has the correct name
        assertEquals("testName", user.getName());
        // Check if the user object has the correct profileUid
        assertEquals("testUid", user.getProfileUid());
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 1"), closeSoftKeyboard());
    }


//        // Check if the title edit text view is displayed and interactable
//        onView(withId(R.id.edittext_event_title)).check(matches(isDisplayed())).perform(typeText("Event Title 1"), closeSoftKeyboard());
//
//        // Enter event description, location, milestone, and capacity
//        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 1"), closeSoftKeyboard());
//        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 1"), closeSoftKeyboard());
//        onView(withId(R.id.edittext_event_milestone)).perform(typeText("50"), closeSoftKeyboard());
//        onView(withId(R.id.edittext_event_capacity)).perform(typeText("100"), closeSoftKeyboard());
//
//        // Pick an event date
//        onView(withId(R.id.edittext_event_date)).perform(click());
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 10, 10));
//        onView(withId(android.R.id.button1)).perform(click());
//
//        // Pick an event time
//        onView(withId(R.id.edittext_event_time)).perform(click());
//        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
//        onView(withId(android.R.id.button1)).perform(click());
//
//        // Set the event poster image
//        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
//
//        // Click the Next button to add the event
//        onView(withId(R.id.button_next)).perform(scrollTo(), click());
//
//        // Verify that the event is added and all of its details are displayed
//        onView(withText("Event Title 1")).check(matches(isDisplayed()));
//        onView(withText("10/10/2024")).check(matches(isDisplayed()));
//        onView(withText("12:00")).check(matches(isDisplayed()));
//        onView(withText("Description 1")).check(matches(isDisplayed()));

/**

    @Test
    public void testActivitySwitch() {
        // Add an event
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_milestone)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_capacity)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_event_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
        onView(withId(R.id.button_next)).perform(scrollTo(), click());

        Espresso.onData(anything())
                .inAdapterView(withId(R.id.event_organizer_list))
                .atPosition(0)
                .perform(ViewActions.click());


        Espresso.onView(withId(R.id.Event_Title))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetails() {
        // Add an event
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_milestone)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_capacity)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_event_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
        onView(withId(R.id.button_next)).perform(scrollTo(), click());

        Espresso.onData(anything())
                .inAdapterView(withId(R.id.event_organizer_list))
                .atPosition(0)
                .perform(ViewActions.click());

        onView(withText("Event Title 1")).check(matches(isDisplayed()));
        onView(withText("10/10/2024 12:00")).check(matches(isDisplayed()));
        onView(withText("Description 1")).check(matches(isDisplayed()));
        onView(withText("50")).check(matches(isDisplayed()));
        onView(withText("100")).check(matches(isDisplayed()));
    }

    @Test
    public void testCheckInQR() {
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.button_generate_checkin_qr)).perform(click());
        Espresso.onView(withId(R.id.event_qr_pic_check_in))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventInQR() {
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.button_generate_eventpage_qr)).perform(click());
        Espresso.onView(withId(R.id.event_qr_code_text))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventCount() {
        // Add an event1
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_milestone)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_capacity)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_event_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
        onView(withId(R.id.button_next)).perform(scrollTo(), click());

        // Add an event2
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_milestone)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_capacity)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_event_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
        onView(withId(R.id.button_next)).perform(scrollTo(), click());

        onView(withId(R.id.event_organizer_list)).check(matches(hasChildCount(2)));
    }
    @Test
    public void testEventDetailsTwo() {
        // Add an event1
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_milestone)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_capacity)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_event_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
        onView(withId(R.id.button_next)).perform(scrollTo(), click());

        // Add an event2
        onView(withId(R.id.event_scan_btn)).perform(click());
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 2"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 2"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 2"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_milestone)).perform(typeText("70"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_capacity)).perform(typeText("150"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_event_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));
        onView(withId(R.id.button_next)).perform(scrollTo(), click());

        Espresso.onData(anything())
                .inAdapterView(withId(R.id.event_organizer_list))
                .atPosition(0)
                .perform(ViewActions.click());

        onView(withText("Event Title 1")).check(matches(isDisplayed()));
        onView(withText("10/10/2024 12:00")).check(matches(isDisplayed()));
        onView(withText("Description 1")).check(matches(isDisplayed()));
        onView(withText("50")).check(matches(isDisplayed()));
        onView(withText("100")).check(matches(isDisplayed()));

        onView(withId(R.id.back_arrow_eventdetails)).perform(click());

        Espresso.onData(anything())
                .inAdapterView(withId(R.id.event_organizer_list))
                .atPosition(1)
                .perform(ViewActions.click());

        onView(withText("Event Title 2")).check(matches(isDisplayed()));
        onView(withText("10/10/2023 11:00")).check(matches(isDisplayed()));
        onView(withText("Description 2")).check(matches(isDisplayed()));
        onView(withText("70")).check(matches(isDisplayed()));
        onView(withText("150")).check(matches(isDisplayed()));




    }
    // Helper method to set drawable resource on an ImageView
    public static ViewAction setDrawableResource(@DrawableRes int drawableResId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(ImageView.class);
            }

            @Override
            public String getDescription() {
                return "Set drawable resource";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ImageView imageView = (ImageView) view;
                imageView.setImageResource(drawableResId);
            }
        };
    }
    **/
}

