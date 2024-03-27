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
    private Database database = new Database();

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
                        //Log.d(TAG, "is the user signed in or not???????????????????" + database.isUserSignedIn());
                        Log.d(TAG, " user id: " + database.getUserUid());
                        database.getUserDocument(database.getUserUid(), new Database.OnUserDocumentRetrievedListener() {
                            @Override
                            public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Log.d(TAG,"inside the if statement" + documentSnapshot.getData());
                                    user = documentSnapshot.toObject(Users.class);
                                    Log.d(TAG, "Check username" + user.getName());

                                    if (user.getName() != null) {
                                        Log.d(TAG, "Enter button clicked" ); // for debugging
                                        Intent intent = new Intent(MainActivity.this, EventHome.class);
                                        intent.putExtra("userObject", user);
                                        startActivity(intent);
                                    }
                                }
                                //if the user exists meaning there is a snapshot, then u will check if the user has a name. if yes, then send to eventhome
                                //if the user doesn't exist meaning there is no snapshot, then u will send to account creation
                                //what if the user exists but they didn't put a name. what case would this be
                                else if (!documentSnapshot.exists()) {
                                    Intent intent = new Intent(MainActivity.this, AccountCreationActivity.class);
                                    Log.d(TAG, " user id: before intent is send " + database.getUserUid());
                                    intent.putExtra("Uid", database.getUserUid());
                                    startActivity(intent);
                                    Log.d(TAG, "IT worked!!!!"); // for debugging
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
