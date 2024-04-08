//package com.example.emojibrite;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.emojibrite.ProfileActivity;
//import com.example.emojibrite.ProfileEditFragment;
//import com.example.emojibrite.R;
//import com.example.emojibrite.Users;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.notNullValue;
//
///*
//@RunWith(AndroidJUnit4.class)
//public class ProfileActivityTest {
//<<<<<<< HEAD
//
//=======
//
//>>>>>>> 2bfd2daa89ea635408754bfd1df253972484a3da
//    private ProfileActivity profileActivity;
//
//    private Users mockUser;
//
//    @Before
//    public void setup() {
//        // Mock user data
//        mockUser = new Users("123456", "John Doe", "john@example.com", "https://example.com", "path_to_image", "123456789");
//
//        // Mock the intent used to start the activity
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("userObject", mockUser);
//        intent.putExtras(bundle);
//
//        // Use ActivityScenario to launch the activity
//
//        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent);
//        scenario.onActivity(activity -> {
//            profileActivity = activity;
//        });
//    }
//
//    @Test
//    public void testProfileActivityInitialization() {
//        // Verify that the activity is not null
//        assertThat(profileActivity, notNullValue());
//
//        // Verify that the UI elements are initialized correctly
//        TextView emailTextView = profileActivity.findViewById(R.id.userEmail);
//        TextView phoneNumberTextView = profileActivity.findViewById(R.id.userPhoneNumber);
//        TextView nameTextView = profileActivity.findViewById(R.id.userName);
//        TextView homePageTextView = profileActivity.findViewById(R.id.userHomePage);
//
//        assertThat(emailTextView.getText().toString(), equalTo(mockUser.getEmail()));
//        assertThat(phoneNumberTextView.getText().toString(), equalTo(mockUser.getNumber()));
//        assertThat(nameTextView.getText().toString(), equalTo(mockUser.getName()));
//        assertThat(homePageTextView.getText().toString(), equalTo(mockUser.getHomePage()));
//
//        // You can add more assertions based on your UI elements and data
//    }
//
//    @Test
//    public void testEditButtonClick() {
//        // Trigger the edit button click
//        profileActivity.findViewById(R.id.editButton).performClick();
//
//        // Get the launched fragment
//        ProfileEditFragment profileEditFragment = (ProfileEditFragment) profileActivity.getSupportFragmentManager().findFragmentByTag("ProfileEditFragment");
//
//        // Verify that the ProfileEditFragment is not null
//        assertThat(profileEditFragment, notNullValue());
//
//        // Verify that the fragment has the correct arguments
//        assert profileEditFragment != null;
//        Bundle arguments = profileEditFragment.getArguments();
//        assertThat(arguments, notNullValue());
//        assert arguments != null;
//        assertThat(arguments.getParcelable("userObject"), equalTo(mockUser));
//
//        // You can add more assertions based on the expected behavior of your edit button
//    }
//}
//*/