package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Objects;

public class PreviewScreenFragment extends Fragment {
    //attributes
    ImageView picture;
    TextView name;
    FloatingActionButton backButton;
    TextView nextButtonText;
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
        picture = previewScreenLayout.findViewById(R.id.uploadImageImage);
        name = previewScreenLayout.findViewById(R.id.usernameTextView);
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

        // when the next button is clicked, go to EventHome Activity.
        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Next button clicked");
                // go to the EventHome Activity and finish the current activity which is the AccountCreationActivity
                // on which the fragments are hosted
                Intent intent = new Intent(getActivity(), EventHome.class);
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
                navController.navigateUp();
            }
        });
    }
}
