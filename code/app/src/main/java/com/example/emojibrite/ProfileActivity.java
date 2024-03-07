package com.example.emojibrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    }


    public void onProfileUpdate(String newEmail, String newPhoneNumber) {
        // Update the userEmail and userPhone TextViews
        updateProfileData(newEmail, newPhoneNumber);
    }

    private void updateProfileData(String newEmail, String newPhoneNumber) {
        // Update the UI elements with the new data
        // For example, assuming you have TextViews to display email and phone number
        TextView emailTextView = findViewById(R.id.editEmail);
        TextView phoneNumberTextView = findViewById(R.id.editPhoneNumber);

        emailTextView.setText(newEmail);
        phoneNumberTextView.setText(newPhoneNumber);
    }

    private void openEditProfileDialog(Profile profile) {
        ProfileEditFragment editProfileDialogFragment = new ProfileEditFragment(profile);
        editProfileDialogFragment.show(getSupportFragmentManager(), "edit_profile_dialog");
    }

}
