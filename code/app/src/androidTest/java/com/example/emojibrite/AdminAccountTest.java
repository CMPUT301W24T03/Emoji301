package com.example.emojibrite;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

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
 * Test class for the AdminAccountActivity
 */
@RunWith(AndroidJUnit4.class)
public class AdminAccountTest {

    /**
     * Launch the AdminAccountActivity using Rule. Bypassing MainActivity
     */
    @Rule
    public ActivityScenarioRule<AdminAccountActivity> activityRule = new ActivityScenarioRule<>(AdminAccountActivity.class);

    public static Users mockUser;

    /**
     * Setup the mock user data
     */
    @Before
    public void setup() {
        // Mock user data
        mockUser = new Users();
        mockUser.setUploadedImageUri("mockUri");

        // Mock the intent used to start the activity
        AdminAccountActivity.user = mockUser;
    }
    /**
     * Test the Account button
     */
    @Test
    public void testAccountButton() {

        onView(withId(R.id.backButton)).perform(click());
        Espresso.onView(withId(R.id.accountAdminButton)).check(matches(isDisplayed()));
    }

    /**
     * Test the back button
     */
    @Test
    public void testBackButton() {
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.accountAdminButton)).perform(click());
        Espresso.onView(withId(R.id.backButton)).check(matches(isDisplayed()));
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
        onView(withId(R.id.profile_list)).perform(testClickEventItemAtPosition());
        onView(withId(R.id.delete_btn)).check(matches(isDisplayed()));
    }

    /**
     * Test the delete event button
     */
    @Test
    public void testdeleteEvent() {
        onView(withId(R.id.profile_list)).perform(testClickEventItemAtPosition());
        onView(withId(R.id.delete_event)).perform(click());
        onView(withId(R.id.profile_list)).check(matches(isDisplayed()));
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
