package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.os.SystemClock;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class AdminImageTest {


    @Rule
    public ActivityScenarioRule<AdminImagesHome> activityRule = new ActivityScenarioRule<>(AdminImagesHome.class);

    public static Users mockUser;


    @Before
    public void setup() {
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
        AdminImagesHome.user = mockUser;
    }


    @Test
    public void testBackandImagesButton() {
        Espresso.onView(withId(R.id.back_arrow_listImg)).perform(click());
    }


    @Test
    public void testImageListDisplayed() {
        SystemClock.sleep(3000);
        onView(withId(R.id.admin_image_list)).check(matches(isDisplayed()));

    }


    @Test
    public void testDeleteImage() {
        SystemClock.sleep(3000);

        // Click on the first item in the list
        onData(anything()).inAdapterView(withId(R.id.admin_image_list)).atPosition(0).perform(click());

        // Check if the dialog with the title "Confirm Delete" is displayed
        onView(withText("Yes")).inRoot(isDialog()).check(matches(isDisplayed()));

    }


}
