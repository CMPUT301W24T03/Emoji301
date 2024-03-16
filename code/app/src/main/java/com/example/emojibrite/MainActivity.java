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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * MainActivity class to handle the main activity of the app
 */
public class MainActivity extends AppCompatActivity {
    Button enterButton;
    TextView scanQRCode;
    TextView adminAccess;
    private static final String TAG = "MainActivityTAG";
    private Users user;
    private Database database = new Database(this);

    //u can include String Fid to pass the firebase installation id
    @Override
    /**
     * onCreate method to handle the creation of the main activity
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize the button and text view
        enterButton = findViewById(R.id.enterButton);
        scanQRCode = findViewById(R.id.qrCodeText);
        adminAccess = findViewById(R.id.adminAccessText);


        /* When Enter Button is clicked, go to the next activity.
         * But which one??
         *
         */
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database.anonymousSignIn(new Database.SignInCallBack() {
                    @Override
                    public void onSignInComplete() {
                        Log.d(TAG, "is the user signed in or not???????????????????" + database.isUserSignedIn());
                        Log.d(TAG, " user id: " + database.getUserUid());
                        database.getUserName(new Database.UserNameDBCallBack() {

                            @Override
                            public void onUserRetrieveNameComplete(String name) {
                                if (name != null) {
                                    Log.d(TAG, "Enter button clicked"); // for debugging
                                    Intent intent = new Intent(MainActivity.this, EventHome.class);
                                    //TODO send in information to event page
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(MainActivity.this, AccountCreationActivity.class);
                                    Log.d(TAG, " user id: before intent is send " + database.getUserUid());
                                    intent.putExtra("Uid", database.getUserUid());
                                    startActivity(intent);
                                    Log.d(TAG, "IT worked!!!!"); // for debugging
                                }
                            }
                        });
                        //ImageView imageView = findViewById(R.id.profile_image);
                        database.getUserDocument(database.getUserUid(), new Database.OnUserDocumentRetrievedListener() {
                            @Override
                            public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Log.d(TAG,"inside the if statement" + documentSnapshot.getData());


                                    user = documentSnapshot.toObject(Users.class);

                                    Log.d(TAG, "user homepage for mainActivity1234321: " + documentSnapshot.getData());
                                    Log.d(TAG, "user id for mainactivity right after user is signed in" + user.getProfileUid());
                                    Log.d(TAG, "user name for mainactivity right after user is signed in" + user.getHomePage());
                                    if (user.getName() != null) {
                                        Log.d(TAG, "Enter button clicked" ); // for debugging
                                        Intent intent = new Intent(MainActivity.this, EventHome.class);
                                        intent.putExtra("userObject", user);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, AccountCreationActivity.class);
                                        Log.d(TAG, " user id: before intent is send " + database.getUserUid());
                                        intent.putExtra("Uid", database.getUserUid());
                                        startActivity(intent);
                                        Log.d(TAG, "IT worked!!!!"); // for debugging
                                    }
                                }
                            }
                        });
                    }
                });


//                // if the user exists in database, go to the event page
//                Log.d(TAG, "Enter button clicked"); // for debugging
//                Intent intent = new Intent(MainActivity.this, EventHome.class);
//                startActivity(intent);
                // else go to nameScreenFragment through the AccountCreationActivity


                // else go to nameScreenFragment

                //Log.d(TAG, "is the user signed in or not???" + database.isUserSignedIn()); // for debugging
            }
        });
    }
}