package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * CLASS TO TEST IF GENERATING EVENT QR CODE WORKS OR NOT
 */

@RunWith(AndroidJUnit4.class)
public class QRCodeEventActivityTest {

    @Rule
    public ActivityScenarioRule<QRCodeEventActivity> activityRule = new ActivityScenarioRule<>(QRCodeEventActivity.class);

    @Test
    public void testGenerateEventQRButton() {
        // Start the activity
        ActivityScenario<QRCodeEventActivity> scenario = activityRule.getScenario();

        // Perform a click on the generate event QR code button
        onView(withId(R.id.generate_button_event1)).perform(click());

        // Check if the ImageView that displays the QR code is visible
        onView(withId(R.id.event_qr_pic)).check(matches(isDisplayed()));

        // Additionally, you can check if the ImageView has changed from its initial state
        onView(withId(R.id.event_qr_pic)).check(matches(not(hasNoDrawable())));
    }

    // Define a custom matcher to check if an ImageView has a drawable
    public static Matcher<View> hasNoDrawable() {
        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof ImageView)) {
                    return false;
                }
                ImageView imageView = (ImageView) item;
                return imageView.getDrawable() == null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("ImageView should have a drawable");
            }
        };
    }
}
