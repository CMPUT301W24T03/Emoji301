package com.example.emojibrite;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the new method to
 * create an instance of this fragment.
 */
public class UploadImageScreenFragment extends Fragment {
    // attributes
    Button uploadImageButton;
    FloatingActionButton BackButton;
    TextView nextButtonText;

    Bitmap selectedImageBitmap;

    Users user;
    private static final String TAG = "UploadImageScreenFragment";
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri imageUri;

    Uri selectedImageUri;
    ImageUploader imageUploader = new ImageUploader("images");

    private Bitmap  imageBitMap;
    private Database database = new Database();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View uploadImageScreenLayout = inflater.inflate(R.layout.fragment_uplaod_image_screen, container, false);
        uploadImageButton = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenButton);
        BackButton = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenBackButton);
        nextButtonText = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenNext);
        database.setUserUid();

        initializeUser(uploadImageScreenLayout);

        return uploadImageScreenLayout;
    }
    /**
     * Initializes the user object from the arguments bundle
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
    /**
     * Initializes the user object from the arguments bundle
     */
    private void deletingStorageUpImage(){
        if (user.getUploadedImageUri() != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Log.d(TAG, "Deleting image at: " + user.getUploadedImageUri());
            StorageReference imageRef = storage.getReferenceFromUrl(user.getUploadedImageUri());
            imageRef.delete();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Initialize the ActivityResultLauncher in onAttach()
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                imageUri = uri;
                Log.d("PhotoPicker", "Selected URI: " + uri);
                imageUploader.uploadImage(uri, new ImageUploader.UploadCallback() {
                    @Override
                    public void onUploadSuccess(Uri downloadUri) {
                        Log.d("PhotoPicker", "Upload success: " + downloadUri);
                        selectedImageUri = downloadUri;
                        user.setUploadedImageUri(downloadUri.toString());
                        addingMetaData();
                        nextButtonText.setClickable(true);
                        //database.storeImageUri(user.getProfileUid(), uri.toString(), "uploadedImage");

                    }

                    @Override
                    public void onUploadFailure(Exception exception) {
                        nextButtonText.setClickable(true);

                    }
                });

                //database.storeImageUri(user.getProfileUid(), uri.toString(), "uploadedImage");

                // Retrieve the image from the URI

            } else {
                Log.d("PhotoPicker", "No media selected");
                nextButtonText.setClickable(true);
            }
        });
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstancesState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstancesState) {
        super.onViewCreated(view, savedInstancesState);
        //String name = UploadImageScreenFragmentArgs.fromBundle(getArguments()).getUserObject();
        // when the next button is clicked, go to the next fragment - PreviewScreenFragment
        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToImagePreview(v);
            }
        });
        // when the back button is clicked, go back to the previous fragment - NameScreenFragment
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletingStorageUpImage();
                navigateBackToNameScreen(v);
            }
        });
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonText.setClickable(false);
                launchMediaPicker(v);
            }
        });
    }

    /**
     * Called when the next button is clicked
     * Navigates to the ImagePreviewScreen
     * @param view The view that was clicked to trigger navigation
     */
    protected void navigateToImagePreview(View view) {
        Log.d(TAG, "Next button clicked");
        // if image is empty, auto generate?
        // when the next button is clicked, go to the next fragment.
        // put stuff you want to pass into the brackets. Make sure to add name and image
        NavDirections action = UploadImageScreenFragmentDirections.actionUploadImageScreenToPreviewScreen(user);    // put name and image here
        NavHostFragment.findNavController(UploadImageScreenFragment.this).navigate(action);
    }

    /**
     * Called when the back button is clicked
     * Navigates back to the name screen
     * It also clears the user's auto-generated image URI, uploaded image URI, and name.
     * @param view The view that was clicked to trigger navigation
     */
    protected void navigateBackToNameScreen(View view) {
        Log.d(TAG, "Back button clicked");
        NavController navController = Navigation.findNavController(view);
        user.setAutoGenImageUri(null);
        user.setUploadedImageUri(null);
        user.setName(null);
        UploadImageScreenFragmentDirections.ActionUploadImageScreenToNameScreen action =
                UploadImageScreenFragmentDirections.actionUploadImageScreenToNameScreen(user);
        navController.navigate(action);
    }

    /**
     * Called when the UploadImage button is clicked
     * Launches the media picker to allow the user to select an image.
     * @param view The view that was clicked to trigger navigation
     */
    protected void launchMediaPicker(View view) {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    /**
     * Initializes the user object from the arguments bundle
     * @param view The view that will be used to find view elements
     */
    private void initializeUser(View view) {
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userObject");
        Log.d(TAG, "User UID: " + user.getProfileUid());

        Log.d(TAG, "onCreateView for upload image screen fragment: " + user.getProfileUid());
    }
}