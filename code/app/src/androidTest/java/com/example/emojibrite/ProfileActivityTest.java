package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
public class ProfileActivityTest {

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
    public void testProfileActivityInView() {
        onView(withId(R.id.userEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.userPhoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.userName)).check(matches(isDisplayed()));
        onView(withId(R.id.userHomePage)).check(matches(isDisplayed()));
        onView(withId(R.id.profilePicture)).check(matches(isDisplayed()));
    }


    @Test
    public void testBackButton() {
        onView(withId(R.id.backButton)).perform(click());
    }


    @Test
    public void testEditButton() {
        onView(withId(R.id.editButton)).perform(click());
        onView(withText("Account Settings")).check(matches(isDisplayed()));
    }


    @Test
    public void testGeoToggle() {
        onView(withId(R.id.geolocationSwitch)).check(matches(isDisplayed()));
    }


    @Test
    public void testNotifToggle() {
        onView(withId(R.id.notificationSwitch)).check(matches(isDisplayed()));
    }
}
