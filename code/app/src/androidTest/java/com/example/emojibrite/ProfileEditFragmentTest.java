package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
/*
@RunWith(AndroidJUnit4.class)
public class ProfileEditFragmentTest {

    // Use ActivityScenario to launch the activity
    private ActivityScenario<TestActivity> activityScenario;

    @Before
    public void setUp() {
        // Initialize a test user object with desired values
        Users testUser = new Users("123", "Test User", "test@example.com", "https://example.com", null, "123456789");

        // Launch the activity and add the fragment
        activityScenario = ActivityScenario.launch(TestActivity.class);
        activityScenario.onActivity(activity -> activity.setFragment(testUser));

    }

    @Test
    public void testEditProfile() {
        // Replace the text in EditText fields
        onView(withId(R.id.editEmail)).perform(replaceText("newemail@example.com"));
        onView(withId(R.id.editPhoneNumber)).perform(replaceText("987654321"));
        onView(withId(R.id.editName)).perform(replaceText("New Name"));
        onView(withId(R.id.editHomePage)).perform(replaceText("https://newhomepage.com"));

        // Click the save button
        onView(withId(R.id.saveButton)).perform(click());

        // Check if the user object was updated
        // You may need to modify this part based on the actual behavior of your app
        // For example, you may want to check if the user object was updated in a ViewModel or database
        onView(withId(R.id.userEmail)).check(matches(withText("newemail@example.com")));
        onView(withId(R.id.userPhoneNumber)).check(matches(withText("987654321")));
        onView(withId(R.id.userName)).check(matches(withText("New Name")));
        onView(withId(R.id.userHomePage)).check(matches(withText("https://newhomepage.com")));
    }

    @Test
    public void testRemoveImage() {
        // Click the remove image button
        onView(withId(R.id.removeImageButton)).perform(click());

        // Check if the user object was updated
        // You may need to modify this part based on the actual behavior of your app
        // For example, you may want to check if the user object was updated in a ViewModel or database
        onView(withId(R.id.profilePicture)).check(matches(ViewMatchers.withTagValue(is(nullValue()))));
    }

    public static class TestActivity extends FragmentActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.test_profile_activity);
        }

        public void setFragment(Users user) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            ProfileEditFragment fragment = new ProfileEditFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("userObject", user);
            fragment.setArguments(bundle);
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        }
    }
}
*/
