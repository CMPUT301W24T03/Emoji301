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
import android.view.View;
import android.widget.AdapterView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test class for the AdminEventActivity
 */
@RunWith(AndroidJUnit4.class)
public class AdminImageTest {

    /**
     * Launch the AdminImagesHome using Rule. Bypassing MainActivity
     */
    @Rule
    public ActivityScenarioRule<AdminImagesHome> activityRule = new ActivityScenarioRule<>(AdminImagesHome.class);

    public static Users mockUser;

    /**
     * Setup the mock user data
     */
    @Before
    public void setup() {
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


        // Mock the intent used to start the activity
        AdminImagesHome.user = mockUser;
    }

    /**
     * Test the image and Back buttons
     */
    @Test
    public void testBackandImagesButton() {
        Espresso.onView(withId(R.id.back_arrow_listImg)).perform(click());
    }


    /**
     * Test the list of images displayed
     */
    @Test
    public void testImageListDisplayed() {
        SystemClock.sleep(3000);
        onView(withId(R.id.admin_image_list)).check(matches(isDisplayed()));

    }


    /**
     * Test the delete images
     */
    @Test
    public void testDeleteImage() {
        SystemClock.sleep(3000);

        // Click on the first item in the list
        onData(anything()).inAdapterView(withId(R.id.admin_image_list)).atPosition(0).perform(click());

        // Check if the dialog with the title "Confirm Delete" is displayed
        onView(withText("Yes")).inRoot(isDialog()).check(matches(isDisplayed()));

    }


    /**
     * Custom ViewAction to click on an item at a specific position
     * @return ViewAction
     */
    private ViewAction testClickEventItemAtPosition() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Click on an item at position " + 0;
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((AdapterView<?>) view).performItemClick(((AdapterView<?>) view).getChildAt(0), 0, ((AdapterView<?>) view).getItemIdAtPosition(0));
            }
        };
    }
}
