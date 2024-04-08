package com.example.emojibrite;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

public class TextViewTextLogger implements ViewAssertion {
    private static final String TAG = "TextViewTextLogger";

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            Log.d(TAG, "TextView Text: " + textView.getText());
        }
    }
}
