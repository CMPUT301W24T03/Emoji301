package com.example.emojibrite;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

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

    Uri selectedImageUri;

    private String nameAtStart;

    Uri imageUri;

    /**
     * Interface for sending the updated user object back to the ProfileActivity.
     */
    public OnInputSelected mOnInputSelected;
    ImageUploader imageUploader = new ImageUploader("images");



    private String currentImagePath;


    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    /**
     * Called when the fragment is no longer in use.
     */
    private void deletingStorageUpImage(){
        if (user.getUploadedImageUri() != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Log.d(TAG, "Deleting image at: " + user.getUploadedImageUri());
            StorageReference imageRef = storage.getReferenceFromUrl(user.getUploadedImageUri());
            imageRef.delete();
        }
    }
    /**
     * Called when the fragment is no longer in use.
     */
    private void addingMetaData(){
        if (selectedImageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageRef = storage.getReferenceFromUrl(selectedImageUri.toString());

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("event_id", null)
                    .setCustomMetadata("user_id", user.getProfileUid())
                    .build();

            imageRef.updateMetadata(metadata);
        }

    }




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
        if (user.getEmail() == null || user.getEmail().isEmpty()){
            EditEmail.setHint("Current Email: ");
        }
        else{
            EditEmail.setText(user.getEmail());
        }
        if ( user.getNumber() == null|| user.getNumber().isEmpty()){
            EditPhoneNumber.setHint("Current Phone Number: ");
        }
        else{
            EditPhoneNumber.setText(user.getNumber());
        }

        if(user.getName() == null|| user.getName().isEmpty()){
            EditName.setHint("Current Name: ");
        }
        else{
            EditName.setText(user.getName());
        }
        if(user.getHomePage() == null|| user.getHomePage().isEmpty()){
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
                            addingMetaData();
                            dismiss();
                        }

                    });
                }
                else{
                    database.setUserObject(user);
                    mOnInputSelected.sendInput(user);
                    addingMetaData();
                    dismiss();
                }
            }
        });

        // Set a click listener for the updateImageButton
        removeImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// Remove the image from the user object
                deletingStorageUpImage();
                user.setUploadedImageUri(null);

                settingPfp();

            }
        });

        FloatingActionButton uploadImageButton = view.findViewById(R.id.uploadImageButton);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setEnabled(false);
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
                //user.setUploadedImageUri(uri.toString());

                imageUploader.uploadImage(uri, new ImageUploader.UploadCallback() {
                    @Override
                    public void onUploadSuccess(Uri downloadUri) {
                        user.setUploadedImageUri(downloadUri.toString());

                        selectedImageUri = downloadUri; // CHANGING THE SELECTED IMAGE URI TO THE DOWNLOADED URI RETREIVED AND THIS IS YOUR ANSWER
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(getContext()).load(user.getUploadedImageUri()).into(profilePicture);
                                saveButton.setEnabled(true);
                            }
                        });
                        Log.d(TAG,"DOWNLOADED URI STRING" + selectedImageUri.toString());
                    }
                    @Override
                    public void onUploadFailure(Exception exception) {
                        Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
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

        /**
         * Method to send the updated user object back to the ProfileActivity.
         * @param users The updated user object.
         */
        void sendInput(Users users);
    }
}