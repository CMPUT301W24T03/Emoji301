package com.example.emojibrite;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
// for going to a new activity
import android.content.Intent;
// for basic UI components
import android.view.View;
import android.widget.Button;
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
        * TODO: implement a way to check whether to go to eventHomeActivity or the CreateAccountActivity
        */
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // if the user exists in database, go to the eventHomeActivity page
//                Log.d(TAG, "Enter button clicked"); // for debugging
//                Intent intent = new Intent(MainActivity.this, EventHome.class);
//                startActivity(intent);
                // else go to nameScreenFragment through the AccountCreationActivity
                Intent intent = new Intent(MainActivity.this, AccountCreationActivity.class);
                startActivity(intent);
    
            }
        });

        // TODO: implement the following:
        // When the QR code text is clicked, go to the QR code scanner
        // When the admin access text is clicked, go to the admin access page
    }
}