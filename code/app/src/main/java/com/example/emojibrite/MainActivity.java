
package com.example.emojibrite;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.os.Build;
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
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * MainActivity class to handle the main activity of the app
 */
public class MainActivity extends AppCompatActivity {
    Button enterButton;
    TextView scanQRCode;
    private static final String TAG = "MainActivityTAG";
    private Users user;
    private Database database = new Database();

    private boolean loggedIn;

    private boolean userDocExist;

    //u can include String Fid to pass the firebase installation id
    @Override
    /**
     * onCreate method to handle the creation of the main activity
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // create notification channel
        createNotificationChannel();

        // Initialize the button and text view
        enterButton = findViewById(R.id.enterButton);
        scanQRCode = findViewById(R.id.qrCodeText);
        loggedIn = false;
        userDocExist = false;
        user = null;
        automaticSignIn();

        // making the textview clickable
        // we go to QR scanning activity
        scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the user is logged in (as in there is an account associated with the device)
                if (userDocExist) {
                    Intent intent = new Intent(MainActivity.this, QRScanningActivity.class);
                    intent.putExtra("userObject", user);
                    intent.putExtra("activity", "main");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"Please hit \"Enter\" and create an account first before scanning.",Toast.LENGTH_LONG).show();
                }
            }
        });

        /* When Enter Button is clicked, go to the next activity.
         * But which one??
         *
         */
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loggedIn) {
                    Toast.makeText(MainActivity.this, "wait while you are logged in", Toast.LENGTH_SHORT).show();

                }
                else {

                    if (userDocExist) {
                        if (user.getName() != null) {
                            Log.d(TAG, "Enter button clicked"); // for debugging
                            Intent intent = new Intent(MainActivity.this, EventHome.class);
                            intent.putExtra("userObject", user);
                            startActivity(intent);
                        }
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

    /**
     * This method creates a notification channel for the app which can be used to send notifications
     * to the user. You can customize the channel name and description as needed.
     * you can find this in settings -> apps -> your app -> notifications
     * <a href="https://developer.android.com/develop/ui/views/notifications/channels">...</a>
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.default_notification_channel_name);
            String description = "Default notification channel for EmojiBrite";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    /**
     * This method automatically signs in the user if they have already signed in before.
     * If the user has not signed in before, they will be prompted to sign in.
     */

    private void automaticSignIn(){
        database.anonymousSignIn(new Database.SignInCallBack() {
            @Override
            public void onSignInComplete() {
                //Log.d(TAG, "is the user signed in or not???????????????????" + database.isUserSignedIn());
                Log.d(TAG, " user id: " + database.getUserUid());

                database.getUserDocument(database.getUserUid(), new Database.OnUserDocumentRetrievedListener() {
                    @Override
                    public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            userDocExist = true;
                            loggedIn = true;
                            Log.d(TAG,"inside the if statement" + documentSnapshot.getData());
                            user = documentSnapshot.toObject(Users.class);
                            Toast.makeText(MainActivity.this, "You are LOGGED in", Toast.LENGTH_SHORT).show();

                        }
                        //if the user exists meaning there is a snapshot, then u will check if the user has a name. if yes, then send to eventhome
                        //if the user doesn't exist meaning there is no snapshot, then u will send to account creation
                        //what if the user exists but they didn't put a name. what case would this be
                        else if (!documentSnapshot.exists()) {
                            userDocExist = false;
                            loggedIn = true;
                            Toast.makeText(MainActivity.this, "You are LOGGED in", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
    }
}
