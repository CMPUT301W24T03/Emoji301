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


@RunWith(AndroidJUnit4.class)
public class AdminEventTest {


    @Rule
    public ActivityScenarioRule<AdminEventActivity> activityRule = new ActivityScenarioRule<>(AdminEventActivity.class);

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
        AdminEventActivity.user = mockUser;
    }

    @Test
    public void testBackandEventButton() {
        Espresso.onView(withId(R.id.backButton)).perform(click());
    }


    @Test
    public void testEventListDisplayed() {
        onView(withId(R.id.admin_event_list)).check(matches(isDisplayed()));
    }


    @Test
    public void testClickEventToEventDetails() {
        SystemClock.sleep(2000);
        onView(withId(R.id.admin_event_list)).perform(testClickEventItemAtPosition());
        onView(withId(R.id.delete_event)).check(matches(isDisplayed()));

    }

    @Test
    public void testDeleteEvent() {
        SystemClock.sleep(2000);
        onView(withId(R.id.admin_event_list)).perform(testClickEventItemAtPosition());
        onView(withId(R.id.delete_event)).perform(click());
        onView(withText("Yes")).check(matches(isDisplayed()));
        //onView(withText("Yes")).perform(click());
    }


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
