package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class AccountCreationActivity extends AppCompatActivity {
    private static final String TAG = "AccountCreationActivity";
    Users user;
    NavController navController;
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
        Intent intent = getIntent();
        String Uid = intent.getStringExtra("Uid");
        user = new Users(Uid);
        Log.d(TAG, "onCreate: " + "AccountCreationActivity" + user.getProfileUid());
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_account_creation);
        Bundle bundle = new Bundle();
        Log.d(TAG, "onStart1: " + user.getProfileUid());
        bundle.putParcelable("userObject", user);
        Log.d(TAG, "onStart2: " + user.getProfileUid());
        navController.setGraph(R.navigation.login_nav_graph, bundle);
        Log.d(TAG, "onStart3: " + user.getProfileUid());
        // for debugging


        // for debugging
    }
    /*
    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_account_creation);
        Bundle bundle = new Bundle();
        Log.d(TAG, "onStart1: " + user.getProfileUid());
        bundle.putParcelable("userObject", user);
        Log.d(TAG, "onStart2: " + user.getProfileUid());
        navController.setGraph(R.navigation.login_nav_graph, bundle);
        Log.d(TAG, "onStart3: " + user.getProfileUid());
    }
    *
    */

}
