package com.example.emojibrite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
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

    PushNotificationService pushNotificationService = new PushNotificationService(); // to subscribe the newly checked in attendee

    private String[] array;

    private boolean found = false;

    private String uid;

    private Users user;
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

        checkUserDoc(uid);

        // checking to see if we have location permissions
        if (geolocationBool){
            // run-time permission check
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }
        }




    }

    private void checkUserDoc(String userUid){
        database.getUserDocument(userUid, documentSnapshot -> {
            if (documentSnapshot.exists()) {

                user = documentSnapshot.toObject(Users.class);
                user.setEnableAdmin(false);

                qrScanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QR_scan();
                    }
                });


            } else {


                Toast.makeText(this, "User got deleted by admin", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QRScanningActivity.this, MainActivity.class);
                startActivity(intent);
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
                        ArrayList<String> attendees = event.getAttendeesList();
                        Log.d("QRScanningActivity", "Attendees List: " + attendees.toString());

                        // Check if the current user is already in the attendees list

                        attendees.add(uid);
                        Log.d("QRScanningActivity", "Attendees List: " + attendees.toString());
                        database.updateEventAttendees(event.getId(), attendees);

                        found = true;

                        Toast.makeText(getBaseContext(), "Successfully checked into " + event.getEventTitle() + "!", Toast.LENGTH_LONG).show();

                        // Notification: Subscribe current checked-in user to the event
                        pushNotificationService.subscribeToEvent(event.getId(), new PushNotificationService.SubscribeCallback() {
                            @Override
                            public void onSubscriptionResult(String msg) {
                                Toast.makeText(QRScanningActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                        // if the user has geolocation enabled
                        if (geolocationBool) {
                            getLocation(event);
                        }
                    }
                }
            });
        }

        // if we did not find anything at all
        else if (!found){
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
        // have to put this here or intelliJ is going to throw a fit
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }

        // gonna try to get a location
        try {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER,0,0,locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            // adding things to the list
            String geolocation;
            geolocation = latitude + "," + longitude;

            ArrayList<String> geolocationList = event.getGeolocationList();

            Log.d("GEOLOCATION LIST", geolocation);

            geolocationList.add(geolocation);

            Log.d("GEOLOCATION LIST UPDATED", geolocation);
            Log.d("GEOLOCATION", geolocation);

            database.updateEventCheckInLocations(event.getId(), geolocationList);
        }
        // something bad happened
        catch (Exception e){
            // most likely occurred because permissions were denied
            Log.d("GEOLOCATION", "Location services have not been granted: " + e);
        }
    }
}