package com.example.emojibrite;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.os.Bundle;
// for going to a new activity
import android.content.Intent;
// for basic UI components
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
// for logcat debugging
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    Button enterButton;
    TextView scanQRCode;
    TextView adminAccess;
    private static final String TAG = "MainActivityTAG";
    private Database database = new Database();

    //u can include String Fid to pass the firebase installation id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the button and text view
        enterButton = findViewById(R.id.enterButton);
        scanQRCode = findViewById(R.id.qrCodeText);
        adminAccess = findViewById(R.id.adminAccessText);

        /* When Enter Button is clicked, go to the next activity.
         * But which one??
         * TODO: implement a way to check whether to go to event page or the create account page?
         */
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the user exists in database, go to the event page
                Log.d(TAG, "Enter button clicked"); // for debugging
                //Intent intent = new Intent(MainActivity.this, EventHome.class);
                //startActivity(intent);
                // else go to nameScreenFragment

                Intent intent = new Intent(MainActivity.this, AccountCreationActivity.class);
                startActivity(intent);
//                // if the user exists in database, go to the event page
//                Log.d(TAG, "Enter button clicked"); // for debugging
//                Intent intent = new Intent(MainActivity.this, EventHome.class);
//                startActivity(intent);

                //Log.d(TAG, "is the user signed in or not???" + database.isUserSignedIn()); // for debugging

                database.anonymousSignIn(new Database.SignInCallBack() {
                    @Override
                    public void onSignInComplete() {
                        Log.d(TAG, "is the user signed in or not???????????????????" + database.isUserSignedIn());

                        Log.d(TAG, " user id: " + database.getUserUid());
                        //ImageView imageView = findViewById(R.id.profile_image);


                    }
                });
            }
        });


//    private void retrieveUserNameCheck() {
//        database.getUserName(new Database.UserNameDBCallBack() {
//            @Override
//            public void onUserRetrieveNameComplete(String name) {
//                if (name != null) {
//                    Log.d(TAG, "Enter button clicked"); // for debugging
//                    Intent intent = new Intent(MainActivity.this, EventHome.class);
//                    startActivity(intent);
//                } else {
//                    NameScreenFragment nameScreenFragment = new NameScreenFragment();
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, nameScreenFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
//
//
//                    Log.d(TAG, "IT worked!!!!"); // for debugging
//
//                }
//            }
//        });
//    }
    }
}