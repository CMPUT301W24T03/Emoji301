package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class ProfileEditFragmentTest {

    public static Users mockUser;


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
        mockUser.setProfileUid("SroGuirZdhfbwcectKhBJkpJpdl2");
        mockUser.setEnableNotification(true);
        mockUser.setEnableGeolocation(true);
        mockUser.setAutoGenImageUri("https://ui-avatars.com/api/?name=John+Doe/");
        mockUser.setUploadedImageUri(null);
        mockUser.setHomePage("homepage");
        mockUser.setFcmToken("cXEdjvR2RRGFnwzJRgW0nj:APA91bGr5XYv7AhDGX6MqFKBwUxnKTwQUbIEg0u9IE1pitS2N9oi_jFPpPmz1VkYLpAF_HbS4W2YYlsOADlGc32fRVvCRgoMgiBxvQxCl4EoYVlqoI5yUn3qnDk_ZTLaOCG2mKaV_GqF");


        // Mock the intent used to start the activity

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ProfileActivity.class);
        intent.putExtra("userObject", mockUser);
        ActivityScenario.launch(intent);
    }

    @Test
    public void testEditTextFields() {
        onView(withId(R.id.editButton)).perform(click());

        onView(withId(R.id.editEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.editPhoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.editName)).check(matches(isDisplayed()));
        onView(withId(R.id.editHomePage)).check(matches(isDisplayed()));

        onView(withId(R.id.editEmail)).check(matches(withText(mockUser.getEmail())));
        onView(withId(R.id.editPhoneNumber)).check(matches(withText(mockUser.getNumber())));
        onView(withId(R.id.editName)).check(matches(withText(mockUser.getName())));
        onView(withId(R.id.editHomePage)).check(matches(withText(mockUser.getHomePage())));
    }


    @Test
    public void testSaveButton() {
        onView(withId(R.id.editButton)).perform(click());

        onView(withId(R.id.saveButton)).check(matches(isDisplayed()));
        onView(withId(R.id.saveButton)).check(matches(isClickable()));
    }


    @Test
    public void testRemoveImageButton() {
        onView(withId(R.id.editButton)).perform(click());

        onView(withId(R.id.removeImageButton)).check(matches(isDisplayed()));
        onView(withId(R.id.removeImageButton)).check(matches(isClickable()));
    }

  
    @Test
    public void testUploadImageButton() {
        onView(withId(R.id.editButton)).perform(click());

        onView(withId(R.id.uploadImageButton)).check(matches(isDisplayed()));
        onView(withId(R.id.uploadImageButton)).check(matches(isClickable()));
    }
}
