
package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.Toast;

/**
 * NameScreenFragment is the fragment for the name screen.
 * It allows the user to enter their name and go to the next screen.
 */
public class NameScreenFragment extends Fragment {
    // attributes
    EditText inputName;
    TextView generateNameText;
    FloatingActionButton backButton;
    TextView nextButtonText;
    Users user;
    private static final String TAG = "NameScreenFragment";

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
        View nameScreenLayout = inflater.inflate(R.layout.fragment_name_screen, container, false);
        inputName = nameScreenLayout.findViewById(R.id.inputNameEditText);
        generateNameText = nameScreenLayout.findViewById(R.id.generateNameTextView);
        backButton = nameScreenLayout.findViewById(R.id.nameScreenBackButton);
        nextButtonText = nameScreenLayout.findViewById(R.id.nameScreenBackNext);

        initializeUser(nameScreenLayout);

        return nameScreenLayout;
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view.
     * It is safe to perform operations on views in this method.
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // when the next button is clicked, call the onNextButtonClick() function
        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onViewCreated");
                processNameAndNavigate(view);
            }
        });
        // when the back button is clicked, call the onBackButtonClick() function
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitToMainActivity(view);
            }
        });

        // space for more View listeners //
    }

    /**
     * Initializes the user object from the arguments bundle
     * @param view The view that will be used to find view elements
     */
    private void initializeUser(View view) {
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userObject");

        //user =NameScreenFragmentArgs.fromBundle(getArguments()).getUserObject();
        Log.d(TAG, "onCreateView for name screen fragment: " + user.getProfileUid());
    }

    /**
     * Called when the Next button is clicked.
     * If the name input field is empty, a toast message is displayed prompting the user to type a name
     * If a name has been entered, it is set as the user's name and the app navigates to the UploadImageScreen fragment
     * passing the userObject (which is parcelable) to the next screen
     * @param view The view that was clicked to trigger navigation
     */
    protected void processNameAndNavigate(View view) {
        Log.d(TAG, "Enter button id nameScreenBackNext is clicked");             // for debugging
        String name = inputName.getText().toString();
        if(name.isEmpty()) {
            Toast.makeText(getActivity(), " Type in a name", Toast.LENGTH_SHORT).show();
        } else {
            // receiving a bundle from the previous fragment/activity and setting the object name to be that.
            user.setName(name);
            Log.d(TAG, "User name: " + user.getName());

            // represents the navigation action from this fragment to the next fragment and passing
            // the user object which is parcelable.
            NavDirections action = NameScreenFragmentDirections.actionNameScreenToUploadImageScreen(user);
            Log.d(TAG, "Next button clicked");             // for debugging
            // performs the navigation to the next fragment
            NavHostFragment.findNavController(NameScreenFragment.this).navigate(action);

        }
    }

    /**
     * Called when the Back button is clicked.
     * If the current activity is not null, it finishes the activity, effectively navigating back to the main activity.
     * @param view The view that was clicked to trigger navigation
     */
    protected void exitToMainActivity(View view) {
        if (getActivity() != null) {
            Log.d(TAG, "back button clicked");          // for debugging
            // go back to the main activity from
            // AccountCreation activity which
            // has the name screen fragment
            if (getActivity() != null) {
                getActivity().finish();

            }
        }
    }
}
