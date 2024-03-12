package com.example.emojibrite;

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

    private Bitmap  imageBitMap;
    private Database database = new Database(getActivity());


    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View uploadImageScreenLayout = inflater.inflate(R.layout.fragment_uplaod_image_screen, container, false);
        uploadImageButton = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenButton);
        BackButton = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenBackButton);
        nextButtonText = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenNext);
        database.setUserUid();
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userObject");
        Log.d(TAG, "User UID: " + user.getProfileUid());

        Log.d(TAG, "onCreateView for upload image screen fragment: " + user.getProfileUid());

        return uploadImageScreenLayout;
    }
    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param context If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        // Initialize the ActivityResultLauncher in onAttach()
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);

                // Store the URI of the selected image in the Users object and in the database
                user.setUploadedImageUri(uri.toString());
                database.storeImageUri(user.getProfileUid(), uri.toString(), "uploadedImage");

                // Retrieve the image from the URI

            } else {
                Log.d("PhotoPicker", "No media selected");
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
                Log.d(TAG, "Next button clicked");
                // if image is empty, auto generate?
                // when the next button is clicked, go to the next fragment.
                // put stuff you want to pass into the brackets. Make sure to add name and image
                NavDirections action = UploadImageScreenFragmentDirections.actionUploadImageScreenToPreviewScreen(user);    // put name and image here
                NavHostFragment.findNavController(UploadImageScreenFragment.this).navigate(action);
            }
        });

        // when the back button is clicked, go back to the previous fragment - NameScreenFragment
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Back button clicked");
                NavController navController = Navigation.findNavController(view);
                user.setAutoGenImageUri(null);
                user.setUploadedImageUri(null);
                user.setName(null);
                UploadImageScreenFragmentDirections.ActionUploadImageScreenToNameScreen action =
                        UploadImageScreenFragmentDirections.actionUploadImageScreenToNameScreen(user);
                navController.navigate(action);



            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
    }
}
