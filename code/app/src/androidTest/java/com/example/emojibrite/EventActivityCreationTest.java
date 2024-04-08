package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;

import org.junit.Test;

public class EventActivityCreationTest {
    @Test
    public void testAddButtonWorks() {
        // Mock a user object
        Users user = new Users();
        user.setProfileUid("mockUid");
        user.setName("mockName");

        // Launch EventHome activity with the mock user
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventHome.class);
        intent.putExtra("userObject", user);
        ActivityScenario<EventHome> scenario = ActivityScenario.launch(intent);

        // Use Espresso to click the add button
        onView(withId(R.id.event_scan_btn)).perform(click()); //test if this button works on clicking

        // Optionally, you can add a delay to see the logs before the test finishes
        try {
            Thread.sleep(2000); // 2 seconds delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
