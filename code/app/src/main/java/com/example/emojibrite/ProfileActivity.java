package com.example.emojibrite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
/**
 * The main activity for displaying and editing user profiles.
 * This activity allows users to view their profile information and initiate the editing process
 * through the {@link ProfileEditFragment}. It also handles the update callbacks from the fragment
 * to reflect changes in the user's profile.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileEditFragment.OnInputSelected {

    Users user;
    ImageView profilePictureImageView;
    SwitchCompat adminToggle;
    SwitchCompat geoToggle;

    SwitchCompat notifToggle;

    TextView adminText;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in
     *                           {@link #onSaveInstanceState(Bundle)}.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");

        TextView emailTextView = findViewById(R.id.userEmail);
        TextView phoneNumberTextView = findViewById(R.id.userPhoneNumber);
        TextView nameTextView = findViewById(R.id.userName);
        TextView homePageTextView = findViewById(R.id.userHomePage);
        adminText = findViewById(R.id.adminModeLabel);

        profilePictureImageView = findViewById(R.id.profilePicture);
        adminToggle = findViewById(R.id.adminModeSwitch);
        geoToggle = findViewById(R.id.geolocationSwitch);
        notifToggle = findViewById(R.id.notificationSwitch);

        FloatingActionButton back = findViewById(R.id.backButton);
        FloatingActionButton editButton = findViewById(R.id.editButton);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setEnableAdmin(adminToggle.isChecked());
                user.setEnableGeolocation(geoToggle.isChecked());
                user.setEnableNotification(notifToggle.isChecked());

                if (user.getEnableAdmin()) {
                    Log.d("ProfileActivity", "User is an admin");
                    // User is an admin, go to the OtherEventHome activity
                    Intent intent = new Intent(ProfileActivity.this, AdminActivity.class);
                    intent.putExtra("userObject", user);
                    startActivity(intent);
                } else {
                    // User is not an admin, go to the EventHome activity
                    Intent intent = new Intent(ProfileActivity.this, EventHome.class);
                    intent.putExtra("userObject", user);
                    startActivity(intent);
                }
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            // Create an instance of the ProfileEditFragment
            @Override
            public void onClick(View view) {
                // Initialize ProfileEditFragment and set the current profile
                ProfileEditFragment profileEditFragment = new ProfileEditFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("userObject", user);
                profileEditFragment.setArguments(bundle);
                profileEditFragment.show(getSupportFragmentManager(), "ProfileEditFragment");
                // Show the ProfileEditFragment
            }
        });


        emailTextView.setText(user.getEmail());
        phoneNumberTextView.setText(user.getNumber());
        nameTextView.setText(user.getName());
        homePageTextView.setText(user.getHomePage());
        adminToggle.setChecked(user.getEnableAdmin());
        notifToggle.setChecked(user.getEnableNotification());
        geoToggle.setChecked(user.getEnableGeolocation());

        checkRole();

        // Retrieve information from SharedPreferences and set it to UI elements

        settingPfp();
    }
    /**
     * Called when the user clicks the "Edit" button to edit their profile.
     */
    @Override
    public void sendInput(Users input) {
        user = input;
        // Update the UI elements with the new profile data
        TextView emailTextView = findViewById(R.id.userEmail);
        TextView phoneNumberTextView = findViewById(R.id.userPhoneNumber);
        TextView nameTextView = findViewById(R.id.userName);
        TextView homePageTextView = findViewById(R.id.userHomePage);
        profilePictureImageView = findViewById(R.id.profilePicture);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                emailTextView.setText(user.getEmail());
                phoneNumberTextView.setText(user.getNumber());
                nameTextView.setText(user.getName());
                homePageTextView.setText(user.getHomePage());
            }
        });
        settingPfp();
    }

    private void checkRole(){

        if (user.getRole().equals("3") || user.getRole().equals("2")){
            //meaning they are the MAIN admin or moderator
            adminToggle.setVisibility(View.VISIBLE);
            adminText.setVisibility(View.VISIBLE);
            Log.d("ProfileActivity", "User is an admin");

        }
        else {
            Log.d("ProfileActivity", "User is not an admin321");
            adminToggle.setVisibility(View.GONE);
            adminText.setVisibility(View.GONE);
        }
    }


    /**
     * Called when the user clicks the "Edit" button to edit their profile.
     */
    public void settingPfp(){
        if (user.getUploadedImageUri() != null) {
            // User uploaded a picture, use that as the ImageView
            //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(ProfileActivity.this).load(user.getUploadedImageUri()).into(profilePictureImageView);
                }
            });
        } else if (user.getUploadedImageUri() ==null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(ProfileActivity.this).load(user.getAutoGenImageUri()).into(profilePictureImageView);
                }
            });
        }
    }
}