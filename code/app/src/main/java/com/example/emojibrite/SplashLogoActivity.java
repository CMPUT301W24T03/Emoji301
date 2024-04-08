package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
/**
 * SplashLogoActivity is an AppCompatActivity that serves as the splash screen for the app.
 * It displays the app logo for a few seconds before transitioning to the main activity.
 */
public class SplashLogoActivity extends AppCompatActivity {
    //private Database database = new Database();
    @Override
    /**
     * onCreate method to handle the creation of the splash screen activity
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_splash);


        //USING HANDLER CLASS that allows us to create a thread that will run after a few seconds -akila
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashLogoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}