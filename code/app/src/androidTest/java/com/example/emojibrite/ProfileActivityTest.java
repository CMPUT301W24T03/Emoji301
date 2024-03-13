package com.example.emojibrite;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {
    /*

    private ProfileActivity profileActivity;

    @Before
    public void setUp() {
        // Launch the ProfileActivity using ActivityScenario
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(ProfileActivity.class);
        scenario.onActivity(activity -> {
            profileActivity = activity;
        });
    }

    @After
    public void tearDown() {
        // Finish the activity after each test
        ActivityScenario.launch(ProfileActivity.class).onActivity(activity -> {
            activity.finish();
        });
    }

    @Test
    public void activityNotNull() {
        // Verify that the activity is not null
        assertNotNull(profileActivity);
    }

    @Test
    public void backButtonClick() {
        // Simulate a click on the back button
        profileActivity.findViewById(R.id.backButton).performClick();

        // Verify that the activity is finished
        assertTrue(profileActivity.isFinishing());
    }

    @Test
    public void editButtonClick() {
        // Simulate a click on the edit button
        profileActivity.findViewById(R.id.editButton).performClick();

        // Verify that the ProfileEditFragment is shown
        assertTrue(profileActivity.getSupportFragmentManager().findFragmentByTag("profile_edit_fragment") != null);
    }

    @Test
    public void profileDataUpdate() {
        // Simulate the update of profile data
        String newEmail = "newemail@example.com";
        String newPhoneNumber = "1234567890";
        String newImagePath = "new_image_path";
        String newName = "New User";
        String newHomePage = "https://newhomepage.com";

        profileActivity.onProfileUpdate(newEmail, newPhoneNumber, newImagePath, newName, newHomePage);

        // Verify that the UI elements are updated
        assertEquals(newEmail, ((TextView) profileActivity.findViewById(R.id.userEmail)).getText().toString());
        assertEquals(newPhoneNumber, ((TextView) profileActivity.findViewById(R.id.userPhoneNumber)).getText().toString());
        assertEquals(newName, ((TextView) profileActivity.findViewById(R.id.userName)).getText().toString());
        assertEquals(newHomePage, ((TextView) profileActivity.findViewById(R.id.userHomePage)).getText().toString());

        // Verify that the currentProfile is updated
        assertEquals(newEmail, profileActivity.currentProfile.getEmail());
        assertEquals(newPhoneNumber, profileActivity.currentProfile.getNumber());
        assertEquals(newName, profileActivity.currentProfile.getName());
        assertEquals(newHomePage, profileActivity.currentProfile.getHomePage());
        assertEquals(newImagePath, profileActivity.currentProfile.getImagePath());

        // Verify that the data is saved in SharedPreferences
        SharedPreferences preferences = profileActivity.getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        assertEquals(newEmail, preferences.getString("email", ""));
        assertEquals(newPhoneNumber, preferences.getString("phoneNumber", ""));
        assertEquals(newImagePath, preferences.getString("imagePath", ""));
        assertEquals(newName, preferences.getString("name", ""));
        assertEquals(newHomePage, preferences.getString("homePage", ""));
    }

     */
}


