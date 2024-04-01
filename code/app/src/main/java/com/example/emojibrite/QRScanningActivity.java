package com.example.emojibrite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class QRScanningActivity extends AppCompatActivity {

    private Button qrScanButton;

    private Database database = new Database();

    private String[] array;

    private boolean found = false;

    private String uid;
    private boolean geolocationBool;

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
        uid = array[0];
        geolocationBool = Boolean.parseBoolean(array[1]);

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
    private void QR_scan() {
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
    private ActivityResultLauncher<ScanOptions> scanLauncher = registerForActivityResult(new ScanContract(), result -> {

        // checking to see if we've scanned an event QR
        if (result.getContents().length() == 12) {
            database.getEventById(result.getContents(), new Database.EventCallBack() {
                @Override
                public void onEventFetched(Event event) {
                    // if the event exists, we pass in the event and userid to go to the event details page
                    if (event != null) {
                        showEventDetails(event, uid);
                        found = true;
                    }
                }
            });
        }

        // checking to see if we actually scanned something as the check in QR can be any QR
        // and if we did not find a corresponding event for an event QR code
        if (result.getContents() != null && !found) {
            database.getEventByCheckInID(result.getContents(), new Database.EventCallBack() {
                @Override
                public void onEventFetched(Event event) {
                    // if the event exists
                    if (event != null) {
                        ArrayList<ArrayList<String>> attendees = event.getAttendeesList();
                        boolean match = false;
                        found = true;

                        // we now iterate through the list
                        // array[i][0] is the uid
                        // array[i][1] is the number of times that uid has checked in
                        for (int i = 0; i < attendees.size(); i++) {
                            if (attendees.get(i).get(0).equals(uid)) {
                                int a = Integer.parseInt(attendees.get(i).get(1));
                                attendees.get(i).set(1, Integer.toString(a + 1));
                                database.updateEventAttendees(event.getId(), attendees);
                                break;
                            }
                        }

                        // if the uid did not exist in the attendees list
                        if (!match) {
                            ArrayList<String> attendee = new ArrayList<String>();
                            attendee.add(uid);
                            attendee.add("0");
                            attendees.add(attendee);
                            database.updateEventAttendees(event.getId(), attendees);
                        }

                        // if the user has geolocation enabled
                        if (geolocationBool) {
                            getLocation(event);
                        }
                    }
                }
            });
        }
        else {
            Toast.makeText(this,"The scanned QR is not associated with any events.", Toast.LENGTH_LONG);
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

    /**
     * Method that gets the location (in longitude and latitude) of the user.
     * @param event
     * Associated event of the attendee's check-in.
     */
    private void getLocation(Event event){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // run-time permission check
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return;
        }

        // gonna try to get a location
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            // adding things to the list
            ArrayList<String> geolocation = new ArrayList<String>();
            geolocation.add(Double.toString(longitude));
            geolocation.add(Double.toString(latitude));

            ArrayList<ArrayList<String>> geolocationList = event.getGeolocationList();
            geolocationList.add(geolocation);

            database.updateEventCheckInLocations(event.getId(), geolocationList);
        }
        // something bad happened
        catch (Exception e){
            Log.d("GEOLOCATION", "SOMETHING DID NOT OCCUR CORRECTLY");
        }
    }
}