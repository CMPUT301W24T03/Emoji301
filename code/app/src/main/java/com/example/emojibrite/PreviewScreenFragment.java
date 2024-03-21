package com.example.emojibrite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
/**
 * PreviewScreenFragment is the fragment that displays the user's profile picture and name
 * before the user is taken to the EventHome Activity.
 */
public class PreviewScreenFragment extends Fragment {
    //attributes
    ImageView picture;
    FloatingActionButton backButton;
    TextView nextButtonText;
    TextView nameText;

    Database database = new Database();
    Bitmap autoGenprofileImage;
    Users user;

    private static final String TAG = "PreviewScreenFragment";

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
        View previewScreenLayout = inflater.inflate(R.layout.fragment_preview_screen, container, false);

        initializeUser(previewScreenLayout);
        initializeImageView(previewScreenLayout);

        backButton = previewScreenLayout.findViewById(R.id.uploadImageBackButton);
        nextButtonText = previewScreenLayout.findViewById(R.id.uploadImageScreenNext);
        nameText = previewScreenLayout.findViewById(R.id.usernameTextView);
        nameText.setText(user.getName());

        return previewScreenLayout;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // This is where you want to setup your buttons or other view's listeners

        // when the next button is clicked, call navigateToEventHomeNoReturn() function
        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                navigateToEventHomeNoReturn(view);
            }
        });
        // when the back button is clicked, go back to the previous fragment - UploadImageScreenFragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateBackToUploadImage(view);
            }
        });
    }

    /**
     * Initializes the user object from the arguments bundle
     * @param view The view that will be used to find view elements
     */
    private void initializeUser(View view) {
        Bundle userBundle = getArguments();
        assert userBundle != null;
        user = userBundle.getParcelable("userObject");
        Log.d(TAG, "onCreateView for preview screen fragment: " + user.getProfileUid());
        Log.d(TAG, "User UID: " + user.getProfileUid());
        Log.d(TAG, "User name: " + user.getName());
    }

    /**
     * Initializes the ImageView for the user's profile picture
     * If the user has not uploaded a picture, call the generateProfileImage function
     * If the user has uploaded a picture, call loadUploadedProfileImage function
     * @param view The view that will be used to find view elements
     */
    private void initializeImageView(View view) {
        picture = view.findViewById(R.id.uploadImageImage);
        if (user.getUploadedImageUri() == null) {
            generateProfileImage();
        } else {
            loadUploadedProfileImage();
        }
    }

    /**
     * Generates a profile image for the user based on their username.
     * Sets the generated image as the ImageView and updates the user's auto-generated image URI.
     */
    private void generateProfileImage() {
        Log.d(TAG, "User didn't upload a picture");
        // User didn't upload a picture, generate one based on the username
        ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator( user.getProfileUid(), user.getName());
        profileImageGenerator.getProfileImage(new ProfileImageGenerator.OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Uri result) {
                // Set the generated image as the ImageView
                Log.d(TAG, "Generated image URI: " + result);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getContext()).load(result).into(picture);
                    }
                });
                user.setAutoGenImageUri(result.toString());
            }
        });
    }

    /**
     * Loads the user's uploaded profile image into the ImageView.
     */
    private void loadUploadedProfileImage() {
        // User uploaded a picture, use that as the ImageView
        Log.d(TAG, "User uploaded a picture");
        //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getContext()).load(user.getUploadedImageUri()).into(picture);
            }
        });
    }

    /**
     * Called when the Next Button is clicked
     * Transitions the user to the EventHome Activity along with the data (user Object).
     * After starting the EventHome activity, it finishes the current activity therefore
     * you won't be able to go back
     * @param view The view that was clicked to trigger navigation
     */
    protected void navigateToEventHomeNoReturn(View view) {
        user.setRole("1");
        database.setUserObject(user);

        Log.d(TAG, "Next button clicked");
        // go to the EventHome Activity and finish the current activity which is the AccountCreationActivity
        // on which the fragments are hosted

        Intent intent = new Intent(getActivity(), EventHome.class);
        Log.d(TAG, "User name for EventHome: " + user.getName() + user.getProfileUid() + user.getUploadedImageUri()+user.getAutoGenImageUri());
        intent.putExtra("userObject", user); // pass the user object to the EventHomeActivity

        // if you want to pass data from the fragments to EventHomeActivity,
        // use the putExtra method. Then, in the EventHomeActivity, use the
        // getIntent method to get the data
        // intent.putExtra("name", name.getText().toString()); key-value pair
        startActivity(intent);
        requireActivity().finish();
    }

    /**
     * Called when the Back Button is clicked
     * navigates the user back to the UploadImage fragment using
     * the NavController associated with the clicked view
     * It resets the user's uploaded and auto-generated URIs,
     * @param view The view that was clicked to trigger navigation
     */
    protected void navigateBackToUploadImage(View view) {
        Log.d(TAG, "Back button clicked");
        NavController navController = Navigation.findNavController(view);
        user.setUploadedImageUri(null);
        user.setAutoGenImageUri(null);

        PreviewScreenFragmentDirections.ActionPreviewScreenToUploadImageScreen action =
                PreviewScreenFragmentDirections.actionPreviewScreenToUploadImageScreen(user);
        navController.navigate(action);
    }
}