package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class AccountCreationActivity extends AppCompatActivity {

    private static final String TAG = "AccountCreationActivity";
    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        Log.d(TAG, "onCreate: " + "AccountCreationActivity");               // for debugging
    }
}
