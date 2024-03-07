package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.contrib.PickerActions;


import static java.util.regex.Pattern.matches;

import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.DrawableRes;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.emojibrite.EventHome;
import com.example.emojibrite.R;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
// This is incorrect for Espresso UI testing and can cause the error you're seeing.
import static java.util.regex.Pattern.matches;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEventTest {

    @Rule
    public ActivityScenarioRule<EventHome> activityRule = new ActivityScenarioRule<>(EventHome.class);

    @Test
    public void testAddSingleEvent() {
        // Click on the add event button
        onView(withId(R.id.event_add_btn)).perform(click());

        // Enter event title, description, location, and capacity
        onView(withId(R.id.edittext_event_title)).perform(typeText("Event Title 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_description)).perform(typeText("Description 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_location)).perform(typeText("Location 1"), closeSoftKeyboard());
        onView(withId(R.id.edittext_event_capacity)).perform(typeText("12"), closeSoftKeyboard());

        // Pick an event date
        onView(withId(R.id.edittext_event_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // Pick an event time
        onView(withId(R.id.edittext_event_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 0));
        onView(withId(android.R.id.button1)).perform(click());

        // Select an event poster image
        // Note: This assumes that you have an ActivityResultLauncher in the activity or fragment
        // that will handle the image selection. Espresso does not directly support
        // launching activities for results, so this would need to be mocked or
        // handled in a different manner in your production code.
        // For the purposes of this test, we'll simulate selecting an image by directly
        // setting an image on the ImageView.
        onView(withId(R.id.image_event_poster)).perform(setDrawableResource(R.drawable.ic_launcher_background));

        // Click the Next button to add the event
        onView(withId(R.id.button_next)).perform(click());

        // Verify that the event is added
        onView(withText("Event Title 1")).check(matches(isDisplayed()));
    }

    // Helper method to set drawable resource on an ImageView
    public static ViewAction setDrawableResource(@DrawableRes int drawableResId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(ImageView.class);
            }

            @Override
            public String getDescription() {
                return "Set drawable resource";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ImageView imageView = (ImageView) view;
                imageView.setImageResource(drawableResId);
            }
        };
    }



}
