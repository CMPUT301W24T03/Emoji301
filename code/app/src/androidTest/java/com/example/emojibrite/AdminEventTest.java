package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class AdminEventTest {

    /**
     * Launch the AdminEventActivity using Rule. Bypassing MainActivity
     */
    @Rule
    public ActivityScenarioRule<AdminEventActivity> activityRule = new ActivityScenarioRule<>(AdminEventActivity.class);

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
        AdminEventActivity.user = mockUser;
    }

    /**
     * Test the event and Back buttons
     */
    @Test
    public void testBackandEventButton() {
        Espresso.onView(withId(R.id.backButton)).perform(click());
    }


    /**
     * Test the list of events displayed
     */
    @Test
    public void testEventListDisplayed() {
        onView(withId(R.id.admin_event_list)).check(matches(isDisplayed()));
    }

    /**
     * Test the event details
     */
    @Test
    public void testClickEventToEventDetails() {
        SystemClock.sleep(2000);
        onView(withId(R.id.admin_event_list)).perform(testClickEventItemAtPosition());
        onView(withId(R.id.delete_event)).check(matches(isDisplayed()));

    }

    /**
     * Test the delete event button
     */
    @Test
    public void testDeleteEvent() {
        SystemClock.sleep(2000);
        onView(withId(R.id.admin_event_list)).perform(testClickEventItemAtPosition());
        onView(withId(R.id.delete_event)).perform(click());
        onView(withText("Yes")).check(matches(isDisplayed()));
        //onView(withText("Yes")).perform(click());
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
