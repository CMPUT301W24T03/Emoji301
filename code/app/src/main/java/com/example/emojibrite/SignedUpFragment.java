package com.example.emojibrite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SignedUpFragment extends Fragment {
    // put your uninitialized attributes here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View signedUpLayout = inflater.inflate(R.layout.fragment_signed_up, container, false);

        // init your attributes here

        return signedUpLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        // button logics
    }
}
