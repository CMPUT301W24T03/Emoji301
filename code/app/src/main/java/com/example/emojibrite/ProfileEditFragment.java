package com.example.emojibrite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * ProfileEditFragment is a DialogFragment that allows users to edit their profile information.
 * It allows users to change their name, email, phone number, and home page. It also allows users
 * to upload a profile picture or remove their current profile picture.
 */
public class ProfileEditFragment extends DialogFragment {

    private EditText EditEmail;
    private EditText EditPhoneNumber;
    private EditText EditName;
    private EditText EditHomePage;
    ImageView profilePicture;
    Button removeImageButton;
    Button saveButton;
    Database database = new Database();
    private Users user;

    private String nameAtStart;

    Uri imageUri;

    public OnInputSelected mOnInputSelected;



    private String currentImagePath;


    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;





    /**
     * Called to have the fragment instantiate its user interface view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userObject");
        nameAtStart = user.getName();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_profile, null);

        EditEmail = view.findViewById(R.id.editEmail);
        EditPhoneNumber = view.findViewById(R.id.editPhoneNumber);
        EditName = view.findViewById(R.id.editName);
        profilePicture = view.findViewById(R.id.profilePicture);
        removeImageButton = view.findViewById(R.id.removeImageButton);
        saveButton = view.findViewById(R.id.saveButton);
        EditHomePage = view.findViewById(R.id.editHomePage);
        saveButton = view.findViewById(R.id.saveButton);
        profilePicture = view.findViewById(R.id.profilePicture);
        removeImageButton = view.findViewById(R.id.removeImageButton);


        // Set initial values for the EditText fields
        if (user.getEmail() == null){
            EditEmail.setHint("Current Email: ");
        }
        else{
            EditEmail.setText(user.getEmail());
        }
        if ( user.getNumber() == null){
            EditPhoneNumber.setHint("Current Phone Number: ");
        }
        else{
            EditPhoneNumber.setText(user.getNumber());
        }

        if(user.getName() == null){
            EditName.setHint("Current Name: ");
        }
        else{
            EditName.setText(user.getName());
        }
        if(user.getHomePage() == null){
            EditHomePage.setHint("Current Home Page: ");
        }
        else{
            EditHomePage.setText(user.getHomePage());
        }
        /*
        // Set the current email and phone number as initial text
        EditEmail.setText(user.getEmail());
        EditPhoneNumber.setText(user.getNumber());
        EditName.setText(user.getName());
        EditHomePage.setText(user.getHomePage());
         */
        // Initialize profilePicture
        settingPfp();
        // Set a click listener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setHomePage(EditHomePage.getText().toString());
                user.setEmail(EditEmail.getText().toString());
                user.setNumber(EditPhoneNumber.getText().toString());
                user.setName(EditName.getText().toString());
                if (nameAtStart != user.getName()){
                    ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator(user.getProfileUid(), user.getName());
                    profileImageGenerator.getProfileImage(new ProfileImageGenerator.OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(Uri result) {
                            user.setAutoGenImageUri(result.toString());
                            database.setUserObject(user);
                            mOnInputSelected.sendInput(user);
                            dismiss();
                        }

                    });
                }
                else{
                    database.setUserObject(user);
                    mOnInputSelected.sendInput(user);
                    dismiss();
                }


            }
        });

        // Set a click listener for the updateImageButton
        removeImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// Remove the image from the user object
                user.setUploadedImageUri(null);

                settingPfp();
            }
        });

        FloatingActionButton uploadImageButton = view.findViewById(R.id.uploadImageButton);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
        builder.setView(view)
                .setTitle("Account Settings");

        return builder.create();
    }

    /**
     * Set the profile picture of the user.
     */
    public void settingPfp(){
        if (user.getUploadedImageUri() != null) {
            // User uploaded a picture, use that as the ImageView
            //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getContext()).load(user.getUploadedImageUri()).into(profilePicture);
                }
            });
        }
        else if (user.getUploadedImageUri() == null && user.getAutoGenImageUri() != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getContext()).load(user.getAutoGenImageUri()).into(profilePicture);
                }
            });
        }
        else if (user.getUploadedImageUri() == null && user.getAutoGenImageUri() == null){

            ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator(user.getProfileUid(), user.getName());
            profileImageGenerator.getProfileImage(new ProfileImageGenerator.OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Uri result) {
                    // Set the generated image as the ImageView
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            profilePicture.setImageResource(R.drawable.profile_pic);
                        }
                    });
                    user.setAutoGenImageUri(result.toString());
                }
            });
        }
    }

    /**
     * Called when a fragment is first attached to its context.
     * @param context The context to which the fragment is attached.
     */
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getActivity();
        } catch (ClassCastException e) {
            Log.e("ProfileEditFragment", "onAttach: ClassCastException : " + e.getMessage());
        }


        // Initialize the ActivityResultLauncher in onAttach()
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                imageUri = uri;
                // Store the URI of the selected image in the Users object and in the database
                user.setUploadedImageUri(uri.toString());

                //database.storeImageUri(user.getProfileUid(), uri.toString(), "uploadedImage");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getContext()).load(user.getUploadedImageUri()).into(profilePicture);
                    }
                });

            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
    }

    /**
     * Interface for sending the updated user object back to the ProfileActivity.
     */
    public interface OnInputSelected {
        void sendInput(Users users);
    }
}