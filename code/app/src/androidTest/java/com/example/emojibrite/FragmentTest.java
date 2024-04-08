package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

// this test is implemented using the following resources as reference:
// https://developer.android.com/guide/fragments/test
// https://stackoverflow.com/questions/30908969/android-writing-test-cases-for-fragments
// https://developer.android.com/guide/navigation/testing
@RunWith(AndroidJUnit4.class)
public class FragmentTest {
    // attributes
    String Uid = "1234";
    Users user = new Users(Uid);
    Bundle bundle = new Bundle();
    // NOTE: The test works if you change the FloatingActionButton to the regular button.

    @Test
    public void navBehaviourTest() {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        // put the user to into the bundle to becasue the NameScreenFragment has userObject in its arguments
        bundle.putParcelable("userObject", user);
        // Create a graphical FragmentScenario for the firs fragment
        FragmentScenario<NameScreenFragment> nameScreenScenario = FragmentScenario.launchInContainer(NameScreenFragment.class, bundle);

        nameScreenScenario.onFragment(fragment -> {
            // set the graph on the TestNavController
            navController.setGraph(R.navigation.login_nav_graph);

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController);
        });
        // set a name before performing the click action because of the check isEmpty() and close keyboard so that the test can actually click Next
        onView(withId(R.id.inputNameEditText)).perform(ViewActions.typeText("TestUser"), ViewActions.closeSoftKeyboard());
        // Verify that performing a click changes the NavController's state
        onView(withId(R.id.nameScreenBackNext)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.uploadImageScreen);
    }
}
