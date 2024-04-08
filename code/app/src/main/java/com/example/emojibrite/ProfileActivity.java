package com.example.emojibrite;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The main activity for displaying and editing user profiles.
 * This activity allows users to view their profile information and initiate the editing process
 * through the {@link ProfileEditFragment}. It also handles the update callbacks from the fragment
 * to reflect changes in the user's profile.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileEditFragment.OnInputSelected {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    static Users user;
    ImageView profilePictureImageView;
    SwitchCompat adminToggle;
    SwitchCompat geoToggle;

    SwitchCompat notifToggle;

    Boolean userUpdated = false;

    TextView adminText;
    PushNotificationService pushNotificationService = new PushNotificationService();
    Database database = new Database();

    Boolean userCheck = false;
    private boolean permissionNotificationDenied = false;    // flag

    String TAG = "ProfileActivity";

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

        database.getUserDocument(user.getProfileUid(), new Database.OnUserDocumentRetrievedListener() {
                @Override
                public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        userCheck = true;
                        user = documentSnapshot.toObject(Users.class);

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

                        Log.d(TAG, "User document retrieved successfully");
                    } else {
                        userCheck = false;
                        Log.d(TAG, "User document does not exist");
                        Toast.makeText(ProfileActivity.this, "user got deleted by admin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);


                    }
                }
                });





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

        geoToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = geoToggle.isChecked();
                if (isChecked) {
                    // If the toggle is turned on, check if the location permission is already granted
                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // If the permission is not granted, request the location permission
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    } else {
                        // If the permission is already granted, update the user object and save it to the database
                        user.setEnableGeolocation(true);
                        database.setUserObject(user);
                    }
                } else {
                    // If the toggle is turned off, guide the user to the settings page
                    new AlertDialog.Builder(ProfileActivity.this)
                            .setMessage("Please disable the location permission for this app in your device settings.")
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });




        notifToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notifToggle.isChecked()) {
                    requestNotificationPermission();
                } else {
                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                        showPermissionRationaleDialog();
                    } else {
                        updateNotificationPermission(false);
                    }
                }
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume", "User has resumed");
        if (userCheck) {
            checkNotificationOnResume();
            onResumeLocation();
        }


    }

    /**
     * Called when the user clicks the "Edit" button to edit their profile.
     * @param input The updated user profile data
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
    /**
     * Check if the user is an admin or moderator.
     */

    private void checkRole() {

        if (user.getRole().equals("3") || user.getRole().equals("2")) {
            //meaning they are the MAIN admin or moderator
            adminToggle.setVisibility(View.VISIBLE);
            adminText.setVisibility(View.VISIBLE);
            Log.d("ProfileActivity", "User is an admin");

        } else {
            Log.d("ProfileActivity", "User is not an admin321");
            adminToggle.setVisibility(View.GONE);
            adminText.setVisibility(View.GONE);
        }
    }

    /**
     * Called when the user clicks the "Edit" button to edit their profile.
     */
    public void settingPfp() {
        if (user.getUploadedImageUri() != null) {
            // User uploaded a picture, use that as the ImageView
            //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(ProfileActivity.this).load(user.getUploadedImageUri()).into(profilePictureImageView);
                }
            });
        } else if (user.getUploadedImageUri() == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(ProfileActivity.this).load(user.getAutoGenImageUri()).into(profilePictureImageView);
                }
            });
        }
    }

    // Notification area //
    // implemented using https://firebase.google.com/docs/cloud-messaging/android/client?_gl=1*ttt67n*_up*MQ..*_ga*NTU3NDA1OTAxLjE3MTE5MTY5NTc.*_ga_CW55HF8NVT*MTcxMTkxNjk1Ny4xLjAuMTcxMTkxNjk1Ny4wLjAuMA..

    /**
     * Request runtime notification permission
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Log.d("notif", "User accepted notification");
                    user.setEnableNotification(true);
                    pushNotificationService.getToken(new PushNotificationService.TokenCallback() {
                        @Override
                        public void onTokenReceived(String token) {
                            user.setFcmToken(token);
                            // Update the user in the database
                            database.setUserObject(user);
                            // Update the toggle button
                            notifToggle.setChecked(user.getEnableNotification());
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Notification Permission")
                            .setMessage("You have denied the notification permission. You can enable it in the settings.")
                            .setPositiveButton("OK", null)
                            .show();

                    permissionNotificationDenied = true;
                    notifToggle.setChecked(false);
                    pushNotificationService.deleteToken();
                    user.setFcmToken(null);
                    database.setUserObject(user);
                }
            });

    /**
     * Check if the user has the notification permission when the activity is resumed.
     */
    private void checkNotificationOnResume() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            if (!user.getEnableNotification()) {
                updateNotificationPermission(true);
            }
        } else {
            if (user.getEnableNotification()) {
                updateNotificationPermission(false);
            } else if (!permissionNotificationDenied) {
                requestNotificationPermission();
            }
        }
    }
    /**
     * Check if the user has the location permission when the activity is resumed.
     */
    private void onResumeLocation() {
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // If the permission is granted, update the user object and save it to the database
            user.setEnableGeolocation(true);
            database.setUserObject(user);
            // Update the toggle button
            geoToggle.setChecked(user.getEnableGeolocation());
        } else {
            // If the permission is not granted, update the user object and save it to the database
            user.setEnableGeolocation(false);
            database.setUserObject(user);
            // Update the toggle button
            geoToggle.setChecked(user.getEnableGeolocation());
        }
    }

    /**
     * Request notification permission from the user.
     */
    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            updateNotificationPermission(true);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            showPermissionRationaleDialog();
        } else {
            if (permissionNotificationDenied) {
                showPermissionNeededDialog();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    /**
     * Show a dialog to the user that they need to enable the notification permission.
     */
    private void showPermissionNeededDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("Please enable the notification permission from the system settings.")
                .setPositiveButton("OK", null)
                .show();
        notifToggle.setChecked(user.getEnableNotification());
    }

    /**
     * Show the permission rationale dialog to the user. This dialog explains why the app needs the
     * notification permission.
     */
    private void showPermissionRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("System Notification")
                .setMessage("You must visit system settings to change notifications.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                    }
                })
                .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateNotificationPermission(user.getEnableNotification());
                    }
                })
                .show();
    }

    /**
     * Update the notification permission for the user.
     * @param isGranted : true if the permission is granted, false otherwise
     */
    private void updateNotificationPermission(boolean isGranted) {
        user.setEnableNotification(isGranted);
        notifToggle.setChecked(isGranted);
        if (isGranted) {
            pushNotificationService.getToken(new PushNotificationService.TokenCallback() {
                @Override
                public void onTokenReceived(String token) {
                    user.setFcmToken(token);
                    database.setUserObject(user);
                }
            });
        } else {
            pushNotificationService.deleteToken();
            user.setFcmToken(null);
            database.setUserObject(user);
        }
    }

    // End of Notification area //

    // Geolocation Tracking area //
    /**
     * Request location permission from the user.
     * @param requestCode : the request code for the permission request
     * @param permissions  permissions : the permissions to request
     *  @param grantResults grantResults : the results of the permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                Log.d(TAG, "Location permission granted");
                // Update the enableGeolocation field in the Firestore database
                updateUserGeolocationPermission(true);
            } else {
                // Location permission denied
                Log.d(TAG, "Location permission denied");
                // Display permission needed message
                displayLocationPermissionMessage();
            }
        }
    }
    /**
     * Display a message to the user that they need to enable the location permission.
     */
    private void displayLocationPermissionMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission needed")
                .setMessage("Please enable the location permission from the system settings.")
                .setPositiveButton("OK", null)
                .show();
        geoToggle.setChecked(false);
    }
    /**
     * Update the user's geolocation permission in the Firestore database.
     * @param permissionGranted : true if the permission is granted, false otherwise
     */
    private void updateUserGeolocationPermission(boolean permissionGranted) {
        // Update the enableGeolocation field in the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(user.getProfileUid());
        userRef
                .update("enableGeolocation", permissionGranted)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User geolocation permission updated successfully");
                        // Update UI or perform other actions upon successful update
                        geoToggle.setChecked(permissionGranted);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating user geolocation permission", e);
                        // Handle failure if needed
                    }
                });
    }






}