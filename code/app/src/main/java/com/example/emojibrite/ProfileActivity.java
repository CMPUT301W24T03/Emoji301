package com.example.emojibrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ProfileActivity extends AppCompatActivity implements ProfileEditFragment.OnProfileUpdateListener{

    Profile currentProfile = new Profile("John Doe", "john@example.com", "https://example.com", "path_to_image", "123456789");
    private Button backButton;
    private Button editButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        FloatingActionButton back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click to go back to the main activity
                finish();
            }
        });

        FloatingActionButton editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            // Create an instance of the ProfileEditFragment
            @Override
            public void onClick(View view) {
                // Initialize ProfileEditFragment and set the current profile
                ProfileEditFragment profileEditFragment = new ProfileEditFragment(currentProfile);
                profileEditFragment.setProfileUpdateListener(ProfileActivity.this);

                // Show the ProfileEditFragment
                profileEditFragment.show(getSupportFragmentManager(), "profile_edit_fragment");
            }
        });


        // Retrieve information from SharedPreferences and set it to UI elements
        SharedPreferences preferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        String storedEmail = preferences.getString("email", "");
        String storedPhoneNumber = preferences.getString("phoneNumber", "");
        String storedImagePath = preferences.getString("imagePath", "");

        updateProfileData(storedEmail, storedPhoneNumber, storedImagePath);

    }


    public void onProfileUpdate(String newEmail, String newPhoneNumber, String newImagePath) {
        // Update the userEmail, userPhone, and profile image in the activity
        updateProfileData(newEmail, newPhoneNumber, newImagePath);
        updateProfileImage(newImagePath);

        // Update the currentProfile with the new information
        currentProfile.setEmail(newEmail);
        currentProfile.setNumber(newPhoneNumber);
        currentProfile.setImagePath(newImagePath);
    }

    private void updateProfileData(String newEmail, String newPhoneNumber, String newImagePath) {
        // Update the UI elements with the new data
        // For example, assuming you have TextViews to display email and phone number
        TextView emailTextView = findViewById(R.id.editEmail);
        TextView phoneNumberTextView = findViewById(R.id.editPhoneNumber);

        // Save the updated information in SharedPreferences
        saveProfileData(newEmail, newPhoneNumber, newImagePath);

        emailTextView.setText(newEmail);
        phoneNumberTextView.setText(newPhoneNumber);
    }

    private void saveProfileData(String newEmail, String newPhoneNumber, String newImagePath) {
        SharedPreferences preferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("email", newEmail);
        editor.putString("phoneNumber", newPhoneNumber);
        editor.putString("imagePath", newImagePath);

        editor.apply();
    }

    private void updateProfileImage(String newImagePath) {
        // Assuming you have an ImageView in your activity_profile.xml with id profileImage
        ImageView profileImage = findViewById(R.id.profilePicture);

        // For demonstration, assuming you're using a resource id
        if (newImagePath != null && !newImagePath.isEmpty()) {
            profileImage.setImageResource(R.drawable.profile_pic); // Load image from new path
        } else {
            profileImage.setImageResource(R.drawable.profile_pic); // Set default image
        }
    }

    private void openEditProfileDialog(Profile profile) {
        ProfileEditFragment editProfileDialogFragment = new ProfileEditFragment(profile);
        editProfileDialogFragment.show(getSupportFragmentManager(), "edit_profile_dialog");
    }
}
