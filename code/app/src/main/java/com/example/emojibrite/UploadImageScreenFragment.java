package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadImageScreenFragment extends Fragment {
    // attributes
    Button uploadImageButton;
    FloatingActionButton BackButton;
    TextView nextButtonText;
    private static final String TAG = "UploadImageScreenFragment";

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
        return uploadImageScreenLayout;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstancesState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstancesState) {
        super.onViewCreated(view, savedInstancesState);

        // when the next button is clicked, go to the next fragment - PreviewScreenFragment
        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Next button clicked");
                // if image is empty, auto generate?
                // when the next button is clicked, go to the next fragment.
                // put stuff you want to pass into the brackets. Make sure to add name and image
                NavDirections action = UploadImageScreenFragmentDirections.actionUploadImageScreenToPreviewScreen();    // put name and image here
                NavHostFragment.findNavController(UploadImageScreenFragment.this).navigate(action);
            }
        });

        // when the back button is clicked, go back to the previous fragment - NameScreenFragment
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Back button clicked");
                NavController navController = Navigation.findNavController(view);
                navController.navigateUp();
            }
        });
    }
}
