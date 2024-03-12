package com.example.emojibrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AccountCreationActivityTest {
    // Launch the AccountCreationActivity using Rule. Bypassing MainActivity
    @Rule
    public ActivityScenarioRule<AccountCreationActivity> activityRule =
            new ActivityScenarioRule<>(AccountCreationActivity.class);

    @Test
    public void NavControllerSetupTest() {
        activityRule.getScenario().onActivity(activity -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_account_creation);
            // Verify that the NavController is not null
            assertNotNull(navController);
            // Verify that the NavController is on the nameScreen
            assertEquals(navController.getCurrentDestination().getId(), R.id.nameScreen);
        });
    }

    @Test
    public void ActivityToFragmentDataTest() {
        // user is parcelable and is passed to the fragment. user = new Users(Uid);
        String Uid = "123";
        Users user = new Users(Uid);
        // user is then passed to the fragment using a bundle
        Bundle bundle = new Bundle();
        bundle.putParcelable("userObject", user);
        NameScreenFragment nameScreenFragment = new NameScreenFragment();
        nameScreenFragment.setArguments(bundle);
        // retrieve the user object from the fragment
        Users retrievedUser = nameScreenFragment.getArguments().getParcelable("userObject");
        // verify that retrievedUser is not null
        assertNotNull(retrievedUser);
        assertEquals(retrievedUser.getProfileUid(), user.getProfileUid());
    }
}
