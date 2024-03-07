package com.example.emojibrite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadImageScreenFragment extends Fragment {
    // attributes
    Button uploadImageButton;
    FloatingActionButton uploadImageBackButton;
    TextView nextButtonText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View uploadImageScreenLayout = inflater.inflate(R.layout.fragment_uplaod_image_screen, container, false);
        uploadImageButton = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenButton);
        uploadImageBackButton = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenBackButton);
        nextButtonText = uploadImageScreenLayout.findViewById(R.id.uploadImageScreenNext);
        return uploadImageScreenLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstancesState) {
        super.onViewCreated(view, savedInstancesState);

        nextButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if image is empty, auto generate?
                // when the next button is clicked, go to the next fragment.
                // put stuff you want to pass into the brackets. Make sure to add name and image
                NavDirections action = UploadImageScreenFragmentDirections.actionUploadImageScreenToPreviewScreen();    // put name and image here
                NavHostFragment.findNavController(UploadImageScreenFragment.this).navigate(action);
            }
        });

        uploadImageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to the previous fragment - NameScreenFragment
                // use navigateup not finish.
            }
        });
    }
}
