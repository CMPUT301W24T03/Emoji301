package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Test class for the AdminAccountActivity
 */
@RunWith(AndroidJUnit4.class)
public class AdminAccountTest {
    public static Users mockUser;

    
    
    
    /**
     * Setup the mock user data
     */
    @Before
    public void setup() throws IOException {

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.executeShellCommand("settings put global window_animation_scale 0");
        device.executeShellCommand("settings put global transition_animation_scale 0");
        device.executeShellCommand("settings put global animator_duration_scale 0");

        // Mock user data
        mockUser = new Users();

        mockUser.setName("John Doe");
        mockUser.setEmail("johndoe@example.com");
        mockUser.setNumber("1234567890");
        mockUser.setRole("3");
        mockUser.setProfileUid("uniqueProfileUid");
        mockUser.setEnableNotification(true);
        mockUser.setEnableGeolocation(true);
        mockUser.setEnableAdmin(true);
        mockUser.setAutoGenImageUri("autoGenImageUri");
        mockUser.setUploadedImageUri("uploadedImageUri");
        mockUser.setHomePage("homePage");
        mockUser.setFcmToken("fcmToken");

        AdminAccountActivity.user = mockUser;


        // Create an ArrayList of Users
        ArrayList<Users> users = new ArrayList<>();
        users.add(mockUser);

        // Create the AttendeesArrayAdapter with the ArrayList of Users
        AttendeesArrayAdapter adapter = new AttendeesArrayAdapter(ApplicationProvider.getApplicationContext(), users, "3", null);

        // Create an intent that contains the Users object
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminAccountActivity.class);
        intent.putExtra("userObject", mockUser);

        // Manually launch the AdminAccountActivity with the intent
        ActivityScenario.launch(intent);
    }


    /**
     * Test the back and account buttons
     */
    @Test
    public void testDeleteButton() {

        onView(withId(R.id.backButton)).check(matches(isDisplayed()));

    }

    /**
     * Test the list of accounts displayed
     */
    @Test
    public void testAccountListDisplayed() {

        onView(withId(R.id.profile_list)).check(matches(isDisplayed()));
    }

    /**
     * Test the admin toggle
     */
    @Test
    public void testAdminToggle() {

        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.accountAdminButton)).check(matches(isDisplayed()));
    }


    // Helper method to create a ViewAction that clicks on a child view with a given id
    private static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}

