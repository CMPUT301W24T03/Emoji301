package com.example.emojibrite;

import android.os.Bundle;
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


public class NameScreenFragment extends Fragment {
    // attributes
    EditText inputName;
    TextView generateNameText;
    FloatingActionButton backButton;
    TextView nextButtonText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View nameScreenLayout = inflater.inflate(R.layout.fragment_name_screen, container, false);
        inputName = nameScreenLayout.findViewById(R.id.inputNameEditText);
        generateNameText = nameScreenLayout.findViewById(R.id.generateNameTextView);
        backButton = nameScreenLayout.findViewById(R.id.nameScreenBackButton);
        nextButtonText = nameScreenLayout.findViewById(R.id.nameScreenBackNext);
        return nameScreenLayout;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if name is empty, auto generate a name automatically randomly
                String name = inputName.getText().toString();
                if(name.isEmpty()) {
                    inputName.setText("GENERATED NAME");    // Todo: auto generate a name
                    name = inputName.getText().toString();
                    // navigate to the next fragment
                    NavDirections action = NameScreenFragmentDirections.actionNameScreenToUploadImageScreen(name);
                    NavHostFragment.findNavController(NameScreenFragment.this).navigate(action);
                } else {
                    // navigate to the next fragment
                    NavDirections action = NameScreenFragmentDirections.actionNameScreenToUploadImageScreen(name);
                    NavHostFragment.findNavController(NameScreenFragment.this).navigate(action);
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NameScreenFragment.this).navigateUp();
            }
        });
    }
}
