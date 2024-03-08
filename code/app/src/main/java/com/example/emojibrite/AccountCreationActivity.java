package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class AccountCreationActivity extends AppCompatActivity {
    private static final String TAG = "AccountCreationActivity";
    Users user;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        Intent intent = getIntent();
        // retrieve the uid from the intent
        String Uid = intent.getStringExtra("Uid");
        // create a new user object based on the Uid
        user = new Users(Uid);
        Bundle bundle = new Bundle();
        bundle.putParcelable("userObject", user);
        Log.d(TAG, "ProfileUID: " + user.getProfileUid());
        Log.d(TAG, "Object: " + bundle.getParcelable("userObject"));
        // pass the bundle to the nameScreen which is the first fragment in the nav graph
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_account_creation);
        navController = navHostFragment.getNavController();
        navController.setGraph(R.navigation.login_nav_graph, bundle);
    }
}
