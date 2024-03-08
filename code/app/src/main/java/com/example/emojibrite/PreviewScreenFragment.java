package com.example.emojibrite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

public class PreviewScreenFragment extends Fragment {
    //attributes
    ImageView picture;
    TextView name;
    FloatingActionButton backButton;
    TextView nextButtonText;

    Database database = new Database();
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
        database.setUserUid();

        if ( user.getUploadedImage() == null) {
            Log.d(TAG, "The user's uploaded image is null");
            ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator(user.getProfileUid(), user.getName());
            profileImageGenerator.getProfileImage(new ProfileImageGenerator.OnCompleteListener<Void>() {
                public void onComplete(Void aVoid) {
                    // After getProfileImage() is complete, call getProfileImageFromDatabase()
                    database.getAutoGenProfileImageFromDatabase(new Database.ProfileImageCallBack(){
                        @Override
                        public void onProfileImageComplete(Bitmap profileImageFromDatabase) {
                            // Use the profileImageFromDatabase bitmap here
                            autoGenprofileImage = profileImageFromDatabase;
                            user.setAutoGenImage(autoGenprofileImage);

                            byte[] decodedString = Base64.decode(user.getAutoGenImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            picture.setImageBitmap(decodedByte);
                        }
                    });
                }
            });
        }
        else {
            byte[] decodedString = Base64.decode(user.getUploadedImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            picture.setImageBitmap(decodedByte);
        }

        name = previewScreenLayout.findViewById(R.id.usernameTextView);
        name.setText(user.getName());

        backButton = previewScreenLayout.findViewById(R.id.uploadImageBackButton);
        nextButtonText = previewScreenLayout.findViewById(R.id.uploadImageScreenNext);

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
                user.setUploadedImage(null);
                user.setAutoGenImage(null);


                PreviewScreenFragmentDirections.ActionPreviewScreenToUploadImageScreen action =
                        PreviewScreenFragmentDirections.actionPreviewScreenToUploadImageScreen(user);
                navController.navigate(action);
            }
        });
    }

}
