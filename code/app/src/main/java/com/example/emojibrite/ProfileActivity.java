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

import org.w3c.dom.Text;


/**
 * The main activity for displaying and editing user profiles.
 * This activity allows users to view their profile information and initiate the editing process
 * through the {@link ProfileEditFragment}. It also handles the update callbacks from the fragment
 * to reflect changes in the user's profile.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileEditFragment.OnProfileUpdateListener{

    Users currentProfile = new Users("123456", "John Doe", "john@example.com", "https://example.com", "path_to_image", "123456789");

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
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
        String storedName = preferences.getString("Name", "");
        String storedHomePage = preferences.getString("HomePage", "");

        updateProfileData(storedEmail, storedPhoneNumber, storedImagePath, storedName, storedHomePage );

    }


    /**
     * Callback method called when the user's profile is updated.
     * @param newEmail      The new email address.
     * @param newPhoneNumber The new phone number.
     * @param newImagePath  The new image path for the profile picture.
     * @param newName       The new name.
     * @param newHomePage   The new home page.
     */
    public void onProfileUpdate(String newEmail, String newPhoneNumber, String newImagePath, String newName, String newHomePage) {
        // Update the userEmail, userPhone, name, and profile image in the activity
        updateProfileData(newEmail, newPhoneNumber, newImagePath, newName, newHomePage);

        // Update the currentProfile with the new information
        currentProfile.setEmail(newEmail);
        currentProfile.setNumber(newPhoneNumber);
        currentProfile.setName(newName);
        currentProfile.setHomePage(newHomePage);
        currentProfile.setImagePath(newImagePath);
    }

    /**
     * Updates the UI with the provided profile data.
     * @param newEmail      The new email address.
     * @param newPhoneNumber The new phone number.
     * @param newImagePath  The new image path for the profile picture.
     * @param newName       The new name.
     * @param newHomePage   The new home page.
     */
    private void updateProfileData(String newEmail, String newPhoneNumber, String newImagePath, String newName, String newHomePage) {
        // Update the UI elements with the new data
        TextView emailTextView = findViewById(R.id.userEmail);
        TextView phoneNumberTextView = findViewById(R.id.userPhoneNumber);
        TextView nameTextView = findViewById(R.id.userName);
        TextView homePageTextView = findViewById(R.id.userHomePage);
        ImageView profilePictureImageView = findViewById(R.id.profilePicture);

        // Save the updated information in SharedPreferences
        saveProfileData(newEmail, newPhoneNumber, newImagePath, newName, newHomePage);

        emailTextView.setText(newEmail);
        phoneNumberTextView.setText(newPhoneNumber);
        nameTextView.setText(newName);
        homePageTextView.setText(newHomePage);

        if (newImagePath != null && !newImagePath.isEmpty()) {
            profilePictureImageView.setImageResource(R.drawable.ic_launcher_foreground); // Load image from new path
        } else {
            profilePictureImageView.setImageResource(R.drawable.profile_pic); // Set default image
        }
    }

    /**
     * Saves the updated profile data to SharedPreferences.
     * @param newEmail      The new email address.
     * @param newPhoneNumber The new phone number.
     * @param newImagePath  The new image path for the profile picture.
     * @param newName       The new name.
     * @param newHomePage   The new home page.
     */
    private void saveProfileData(String newEmail, String newPhoneNumber, String newImagePath, String newName, String newHomePage) {
        SharedPreferences preferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("email", newEmail);
        editor.putString("phoneNumber", newPhoneNumber);
        editor.putString("imagePath", newImagePath);
        editor.putString("name", newName);
        editor.putString("homePage", newHomePage);

        editor.apply();
    }

    /**
     * Updates the profile image in the UI.
     * @param newImagePath The new image path for the profile picture.
     */
    private void updateProfileImage(String newImagePath) {
        // Assuming you have an ImageView in your activity_profile.xml with id profileImage
        ImageView profileImage = findViewById(R.id.profilePicture);

        // For demonstration, assuming you're using a resource id
    }

    /**
     * Opens the edit profile dialog for the provided user profile.
     * @param profile The user profile to be edited.
     */
    private void openEditProfileDialog(Users profile) {
        ProfileEditFragment editProfileDialogFragment = new ProfileEditFragment(profile);
        editProfileDialogFragment.show(getSupportFragmentManager(), "edit_profile_dialog");
    }
}
