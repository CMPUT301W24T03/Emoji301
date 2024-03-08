package com.example.emojibrite;
import android.content.Context;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.emojibrite.ProfileEditFragment;
import com.example.emojibrite.Users;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class ProfileEditFragmentTest {

    private ProfileEditFragment profileEditFragment;
    private boolean listenerCalled = false;

    @Before
    public void setUp() {
        // Create a mock user profile for testing
        Users mockProfile = new Users("123456", "John Doe", "john@example.com", "https://example.com", "path_to_image", "123456789");

        // Initialize the fragment with the mock profile
        profileEditFragment = new ProfileEditFragment(mockProfile);

        // Start the fragment to test its behavior
        startFragment(profileEditFragment);
    }

    @Test
    public void testProfileEditFragmentInitialState() {
        // Check if the initial values are correctly set in the EditText fields
        onView(withId(R.id.editEmail)).check(matches(isEditTextValueEqualTo("john@example.com")));
        onView(withId(R.id.editPhoneNumber)).check(matches(isEditTextValueEqualTo("123456789")));
        onView(withId(R.id.editName)).check(matches(isEditTextValueEqualTo("John Doe")));
        onView(withId(R.id.editHomePage)).check(matches(isEditTextValueEqualTo("https://example.com")));
    }

    @Test
    public void testProfileEditFragmentUpdateProfile() {
        // Perform actions to update the profile
        onView(withId(R.id.editEmail)).perform(typeText("newemail@example.com"));
        onView(withId(R.id.editPhoneNumber)).perform(typeText("987654321"));
        onView(withId(R.id.editName)).perform(typeText("New Name"));
        onView(withId(R.id.editHomePage)).perform(typeText("https://newexample.com"));

        // Save the changes by clicking the save button
        onView(withId(R.id.saveButton)).perform(click());

        // Verify if the listener is notified with the correct updated values
        assert (listenerCalled);
    }


    // Helper method to start a fragment for testing
    private void startFragment(ProfileEditFragment fragment) {
        FragmentManager fragmentManager = fragment.requireFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, null);
        transaction.commit();
    }

    // Helper method to check if the text in an EditText matches a given value
    private static Matcher<Object> isEditTextValueEqualTo(String value) {
        return new BoundedMatcher<Object, EditText>(EditText.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Value should be equal to: " + value);
            }

            @Override
            protected boolean matchesSafely(EditText item) {
                return item.getText().toString().equals(value);
            }
        };
    }
}
