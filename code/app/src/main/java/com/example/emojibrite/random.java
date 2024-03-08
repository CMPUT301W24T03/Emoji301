//package com.example.emojibrite;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.media.Image;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.PickVisualMediaRequest;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.fragment.app.DialogFragment;
//
//import com.bumptech.glide.Glide;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.io.IOException;
//
//import javax.annotation.Nonnull;
//
///**
// * A {@link DialogFragment} for editing user profile information.
// * This fragment allows users to edit their email, phone number, name, home page, and profile picture.
// * It communicates changes to the calling activity through the {@link OnProfileUpdateListener} interface.
// */
//public class ProfileEditFragment extends DialogFragment {
//
//    private EditText EditEmail;
//    private EditText EditPhoneNumber;
//    private EditText EditName;
//    private EditText EditHomePage;
//
//    private ImageView buttonType;
//    ImageView profilePicture;
//    Button removeImageButton;
//
//    private Users edit;
//
//    private String currentImagePath;
//    private OnProfileUpdateListener profileUpdateListener;
//
//    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
//
//    /**
//     * Sets the listener to be notified of profile updates.
//     * @param listener The listener implementing {@link OnProfileUpdateListener}.
//     */
//    public void setProfileUpdateListener(OnProfileUpdateListener listener) {
//        this.profileUpdateListener = listener;
//    }
//
//    /**
//     * Constructs a new instance of {@link ProfileEditFragment}.
//     * @param profile The user profile to be edited.
//     */
//    public ProfileEditFragment(Users profile){
//        this.edit = profile;
//    }
//
//    /**
//     * Called to create the dialog.
//     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
//     * @return A new instance of {@link Dialog}.
//     */
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_edit_profile, null);
//
//        EditEmail = view.findViewById(R.id.editEmail);
//        EditPhoneNumber = view.findViewById(R.id.editPhoneNumber);
//        EditName = view.findViewById(R.id.editName);
//        profilePicture = view.findViewById(R.id.profilePicture);
//        removeImageButton = view.findViewById(R.id.removeImageButton);
//        Button saveButton = view.findViewById(R.id.saveButton);
//        EditHomePage = view.findViewById(R.id.editHomePage);
//
//
//
//
//        // Set initial values for the EditText fields
//        EditEmail.setHint("Current Email: " + edit.getEmail());
//        EditPhoneNumber.setHint("Current Phone Number: " + edit.getNumber());
//        EditName.setHint("Current Name: " + edit.getName());
//        EditHomePage.setHint("Current Home Page: " + edit.getHomePage());
//
//
//        // Set the current email and phone number as initial text
//        EditEmail.setText(edit.getEmail());
//        EditPhoneNumber.setText(edit.getNumber());
//        EditName.setText(edit.getName());
//        EditHomePage.setText(edit.getHomePage());
//
//        // Initialize profilePicture
//        profilePicture = view.findViewById(R.id.profilePicture);
//        // Set the image resource if available or load it from a URL
//        if (edit.getImagePath() != null && !edit.getImagePath().isEmpty()) {
//            // Load image from a file or URL
//            profilePicture.setImageResource(R.drawable.ic_launcher_foreground);
////            Glide.with(getContext()).load(edit.getImagePath()).into(profilePicture);
//        } else {
//            // If no image path is available, you can set a default image
//            profilePicture.setImageResource(R.drawable.profile_pic);
//        }
//
//        // Set a click listener for the Save button
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Assuming "edit" is an instance of the Profile class
//                if (edit != null) {
//                    // Update the email and phone number in the Profile object
//                    // Save the edited data back to the City object
//                    edit.setEmail(EditEmail.getText().toString());
//                    edit.setNumber(EditPhoneNumber.getText().toString());
//                    edit.setName(EditName.getText().toString());
//                    edit.setImagePath(currentImagePath);
//
//                    // Notify the ProfileActivity about the changes
//                    if (profileUpdateListener != null) {
//                        profileUpdateListener.onProfileUpdate(edit.getEmail(), edit.getNumber(), edit.getImagePath(), edit.getName(), edit.getHomePage());
//                    }
//
//                    // Close the dialog or fragment
//                    dismiss();
//                } else {
//                    // Handle the case where edit is null
//                    Log.e("ProfileEditFragment", "edit object is null");
//                }
//            }
//        });
//
//        // Set a click listener for the updateImageButton
//        removeImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Clear the image from the ImageView
//                profilePicture.setImageResource(R.drawable.profile_pic);
//                currentImagePath = ""; // Reset currentImagePath
//                edit.setImagePath(currentImagePath); // Update edit object
//
//            }
//        });
//
//        FloatingActionButton uploadImageButton = view.findViewById(R.id.uploadImageButton);
//        uploadImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                pickMedia.launch(new PickVisualMediaRequest.Builder()
//                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
//            }
//        });
//
//        builder.setView(view)
//                .setTitle("Account Settings")
//                .setNegativeButton("Cancel", (dialog, which) -> dismiss());
//
//        return builder.create();
//    }
//
//    /**
//     * Called when the fragment is attached to the calling context.
//     * @param context The context to which the fragment is attached.
//     */
//    @Override
//    public void onAttach(@Nonnull Context context){
//        super.onAttach(getContext());
//
//        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
//            if (uri != null) {
//                Log.d("PhotoPicker", "Selected URI: " + uri);
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                    Log.d("PhotoPicker", "Selected Bitmap: " + bitmap);
//                    profilePicture.setImageBitmap(bitmap);
//                    currentImagePath = uri.toString();
//                    edit.setImagePath(currentImagePath);
//                } catch (IOException e) {
//                    Log.e("PhotoPicker", "Error getting bitmap from URI: " + e.getMessage());
//
//                    Toast.makeText(context, "Error loading image.", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Log.d("PhotoPicker", "No media selected");
//
//                Toast.makeText(context, "No image selected.", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//
//
//
//    /**
//     * Interface definition for a callback to be invoked when a user's profile is updated.
//     */
//    public interface OnProfileUpdateListener {
//        /**
//         * Called when a user's profile information is updated.
//         * @param newEmail      The new email address.
//         * @param newPhoneNumber The new phone number.
//         * @param newImagePath  The new image path for the profile picture.
//         * @param newName       The new name.
//         * @param newHomePage   The new home page.
//         */
//        void onProfileUpdate(String newEmail, String newPhoneNumber, String newImagePath, String newName, String newHomePage);
//    }
//
//}
