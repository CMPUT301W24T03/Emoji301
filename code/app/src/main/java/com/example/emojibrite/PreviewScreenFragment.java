package com.example.emojibrite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PreviewScreenFragment extends Fragment {
    //attributes
    ImageView picture;
    TextView name;
    FloatingActionButton backButton;
    TextView nextButtonText;
    TextView nameText;

    Database database = new Database(getActivity());
    Bitmap autoGenprofileImage;

    Users user;
    ImageUpload imageUpload = new ImageUpload();
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
        Bundle userBundle = getArguments();
        user = userBundle.getParcelable("userObject");
        Log.d(TAG, "onCreateView for preview screen fragment: " + user.getProfileUid());
        picture = previewScreenLayout.findViewById(R.id.uploadImageImage);

        Log.d(TAG, "User UID: " + user.getProfileUid());
        Log.d(TAG, "User name: " + user.getName());
        if (user.getUploadedImageUri() == null) {
            Log.d(TAG, "User didn't upload a picture");
            // User didn't upload a picture, generate one based on the username
            ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator(getContext(), user.getProfileUid(), user.getName());
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
        } else {
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
        // this is were you want to get the thigns that are passed

        // when the next button is clicked, go to EventHome Activity.
        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Next button clicked");
                // go to the EventHome Activity and finish the current activity which is the AccountCreationActivity
                // on which the fragments are hosted

                Intent intent = new Intent(getActivity(), EventHome.class);
                intent.putExtra("userObject", user); // pass the user object to the EventHomeActivity

                // if you want to pass data from the fragments to EventHomeActivity,
                // use the putExtra method. Then, in the EventHomeActivity, use the
                // getIntent method to get the data
                // intent.putExtra("name", name.getText().toString()); key-value pair
                startActivity(intent);
                requireActivity().finish();

            }
        });
        // when the back button is clicked, go back to the previous fragment - UploadImageScreenFragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Back button clicked");
                NavController navController = Navigation.findNavController(view);
                user.setUploadedImageUri(null);
                user.setAutoGenImageUri(null);


                PreviewScreenFragmentDirections.ActionPreviewScreenToUploadImageScreen action =
                        PreviewScreenFragmentDirections.actionPreviewScreenToUploadImageScreen(user);
                navController.navigate(action);
            }
        });
    }

}


/*
profileImageGenerator.getProfileImage(new ProfileImageGenerator.OnCompleteListener<Void>() {
                public void onComplete(Void aVoid) {
                    // After getProfileImage() is complete, call getProfileImageFromDatabase()
                    Log.d(TAG, "inside oncomplete");
                    database.getAutoGenProfileImageFromDatabase(new Database.ProfileImageCallBack(){
                        @Override
                        public void onProfileImageComplete(Bitmap profileImageFromDatabase) {
                            Log.d(TAG, "inside onProfileImageComplete");
                            Log.d(TAG, "ProfileImageFromDatabase: " + profileImageFromDatabase);
                            // Use the profileImageFromDatabase bitmap here
                            autoGenprofileImage = profileImageFromDatabase;
                            user.setAutoGenImage(autoGenprofileImage);
                            Log.d(TAG, "half check");
                            byte[] decodedString = Base64.decode(user.getAutoGenImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            Log.d(TAG, "full check");
                            picture.setImageBitmap(autoGenprofileImage);
                        }
                    });
                }
            });
 */