package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRScanningActivity extends AppCompatActivity {

    private Button qrScanButton;

    private Database database = new Database();

    private String[] array;

    private boolean found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrscanning);

        qrScanButton = findViewById(R.id.scan_qr_button);

        // need to pass in UID and geolocation bool
        Bundle bundle = this.getIntent().getExtras();
        // index 0 is uid, index 1 is geolocation bool
        array = bundle.getStringArray("USER");

        qrScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QR_scan();
            }
        });



    }

    /**
     * Function to scan a QR code.
     */
    private void QR_scan(){
        // setting up ability to scan
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setDesiredBarcodeFormats("QR_CODE");

        // launching scanner
        scanLauncher.launch(options);
    }

    /**
     * Launches QR scanner.
     */
    private ActivityResultLauncher<ScanOptions> scanLauncher = registerForActivityResult(new ScanContract(), result ->{

        // checking to see if we've scanned an event QR
        if (result.getContents().length() == 12) {
            database.getEventById(result.getContents(), new Database.EventCallBack() {
                @Override
                public void onEventFetched(Event event) {
                    // if the event exists, we pass in the event and userid to go to the event details page
                    if (event != null) {
                        showEventDetails(event, array[0]);
                        found = true;
                    }
                }
            });
        }

        // checking to see if we actually scanned something as the check in QR can be any QR
        // and if we did not find a corresponding event
        if (result.getContents() != null && !found){

        }
    });

    /**
     * Opens the EventDetailsActivity to show the details of the selected event.
     *
     * @param event
     * The event to show details for.
     *
     * @param userID
     * Current user.
     */
    private void showEventDetails(Event event, String userID) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        intent.putExtra("userlol",userID); //You send the current user profile id into the details section
        startActivity(intent);
    }
}