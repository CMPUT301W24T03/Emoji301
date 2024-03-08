package com.example.emojibrite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class ProfileEditFragment extends DialogFragment {

    EditText EditEmail;
    EditText EditPhoneNumber;
    ImageView profilePicture;
    Button removeImageButton;
    private Button saveButton;
    private Profile edit;

    private String currentImagePath;
    private OnProfileUpdateListener profileUpdateListener;


    public void setProfileUpdateListener(OnProfileUpdateListener listener) {
        this.profileUpdateListener = listener;
    }
    public ProfileEditFragment(Profile profile){
        this.edit = profile;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_profile, null);

        EditEmail = view.findViewById(R.id.editEmail);
        EditPhoneNumber = view.findViewById(R.id.editPhoneNumber);
        saveButton = view.findViewById(R.id.saveButton);
        profilePicture = view.findViewById(R.id.profilePicture);
        removeImageButton = view.findViewById(R.id.removeImageButton);


        // Set initial values for the EditText fields
        EditEmail.setHint("Current Email: " + edit.getEmail());
        EditPhoneNumber.setHint("Current Phone Number: " + edit.getNumber());


        // Set the current email and phone number as initial text
        EditEmail.setText(edit.getEmail());
        EditPhoneNumber.setText(edit.getNumber());

        // Initialize profilePicture
        profilePicture = view.findViewById(R.id.profilePicture);
        // Set the image resource if available or load it from a URL
        if (edit.getImagePath() != null && !edit.getImagePath().isEmpty()) {
            // Load image from a file or URL
            // You can use a library like Picasso or Glide for efficient image loading
            // Here, assuming you are using a resource id, you can use setImageResource
            profilePicture.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            // If no image path is available, you can set a default image
            profilePicture.setImageResource(R.drawable.profile_pic);
        }

        // Set a click listener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Assuming "edit" is an instance of the Profile class
                if (edit != null) {
                    // Update the email and phone number in the Profile object
                    // Save the edited data back to the City object
                    edit.setEmail(EditEmail.getText().toString());
                    edit.setNumber(EditPhoneNumber.getText().toString());

                    // Notify the ProfileActivity about the changes
                    if (profileUpdateListener != null) {
                        profileUpdateListener.onProfileUpdate(edit.getEmail(), edit.getNumber(), edit.getImagePath());
                    }

                    // Close the dialog or fragment
                    dismiss();
                } else {
                    // Handle the case where edit is null
                    Log.e("ProfileEditFragment", "edit object is null");
                }
            }
        });

        // Set a click listener for the updateImageButton
        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the image from the ImageView
                profilePicture.setImageResource(0);
                profilePicture.setImageResource(R.drawable.profile_pic);

            }
        });

        builder.setView(view)
                .setTitle("Account Settings")
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }

    public interface OnProfileUpdateListener {
        void onProfileUpdate(String newEmail, String newPhoneNumber, String newImagePath);
    }

}
