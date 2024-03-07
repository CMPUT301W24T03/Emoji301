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
    Button updateImageButton;
    private Button saveButton;
    private Profile edit;
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
        profilePicture = view.findViewById(R.id.profilePicture); // Initialize profilePicture
        updateImageButton = view.findViewById(R.id.updateImageButton); // Initialize updateImageButton


        // Set initial values for the EditText fields
        EditEmail.setHint("Current Email: " + edit.getEmail());
        EditPhoneNumber.setHint("Current Phone Number: " + edit.getNumber());
        profilePicture.setImageResource(R.drawable.ic_launcher_foreground);

        // Set the current email and phone number as initial text
        EditEmail.setText(edit.getEmail());
        EditPhoneNumber.setText(edit.getNumber());

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
                        profileUpdateListener.onProfileUpdate(edit.getEmail(), edit.getNumber());
                    }
                    // Close the dialog or fragment
                    // Close the dialog or fragment
                    dismiss();  // If it's a DialogFragment
                } else {
                    // Handle the case where edit is null
                    Log.e("ProfileEditFragment", "edit object is null");
                }
            }
        });

        // Set a click listener for the updateImageButton
        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle updating or removing the profile picture here
                // You may open a new dialog or perform any other actions
            }
        });

        builder.setView(view)
                .setTitle("Account Settings")
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }

    public interface OnProfileUpdateListener {
        void onProfileUpdate(String newEmail, String newPhoneNumber);
    }

}
