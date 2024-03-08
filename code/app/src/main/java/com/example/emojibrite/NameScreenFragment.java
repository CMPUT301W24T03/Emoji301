package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import android.util.Log;
import android.widget.Toast;


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
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nameScreenLayout = inflater.inflate(R.layout.fragment_name_screen, container, false);
        inputName = nameScreenLayout.findViewById(R.id.inputNameEditText);
        generateNameText = nameScreenLayout.findViewById(R.id.generateNameTextView);
        backButton = nameScreenLayout.findViewById(R.id.nameScreenBackButton);
        nextButtonText = nameScreenLayout.findViewById(R.id.nameScreenBackNext);

        Bundle bundle = getArguments();

        user = bundle.getParcelable("userObject");
        //user =NameScreenFragmentArgs.fromBundle(getArguments()).getUserObject();
        Log.d(TAG, "onCreateView for name screen fragment: " + user.getProfileUid());

        return nameScreenLayout;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // when the next button is clicked, go to the next fragment
        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Enter button clicked");             // for debugging
                // if name is empty, auto generate a name automatically and randomly
                String name = inputName.getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(getActivity(), " Type in a name", Toast.LENGTH_SHORT).show();
                    /*
                    inputName.setText("GENERATED NAME");
                    name = inputName.getText().toString();

                   NavDirections action = NameScreenFragmentDirections.actionNameScreenToUploadImageScreen(name);
                   NavHostFragment.findNavController(NameScreenFragment.this).navigate(action);
                     */
                } else {
                    // recieving a bundle from the previous fragment/activity and setting the object name to be that.

                    user.setName(name);

                    NavDirections action = NameScreenFragmentDirections.actionNameScreenToUploadImageScreen(user);
                    NavHostFragment.findNavController(NameScreenFragment.this).navigate(action);

                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });
    }
}