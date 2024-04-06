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
import androidx.core.content.ContextCompat;
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

    private Users user;

    /**
     * Sets up UI elements and functionality for QR scanning activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    private String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrscanning);

        qrScanButton = findViewById(R.id.scan_qr_button);

        // get the user object from the intent
        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");
        activity = intent.getParcelableExtra("activity");

        checkUserDoc(user.getProfileUid());

        // checking to see if we have location permissions
        if (user.getEnableGeolocation()){
            // run-time permission check
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }
            // if location permissions were denied
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this,"Geolocation tagging requires location permission.", Toast.LENGTH_LONG).show();
            }
        }

        // checking to see if camera permissions were given
        // if not we ask
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }
        // if camera permissions were denied
        // we exit QR scanning
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "QR scanning requires camera permissions.", Toast.LENGTH_LONG).show();
            finish();
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
                if (activity.equals("main")) {


                    Toast.makeText(this, "User got deleted by admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QRScanningActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (activity.equals("event")) {
                    Toast.makeText(this, "User got deleted by admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QRScanningActivity.this, OtherEventHome.class);
                    intent.putExtra("userObject", user);
                    startActivity(intent);
                }
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
                        showEventDetails(event, user);
                        found = true;
                        finish();
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

                        attendees.add(user.getProfileUid());
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
                        if (user.getEnableGeolocation()) {
                            getLocation(event);
                        }

                        // going to other event home after checking in
                        if (found){
                            Intent intent = new Intent(QRScanningActivity.this, OtherEventHome.class);
                            intent.putExtra("userObject", user);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }

        // if we did not find anything at all
        else if (!found){
            Toast.makeText(this,"The scanned QR is not associated with any events.", Toast.LENGTH_LONG);
            Intent intent = new Intent(QRScanningActivity.this, OtherEventHome.class);
            intent.putExtra("userObject", user);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    });





    /**
     * Opens the EventDetailsActivity to show the details of the selected event.
     *
     * @param event
     * The event to show details for.
     *
     * @param user
     * Current user.
     */
    private void showEventDetails(Event event, Users user) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        intent.putExtra("userObject", user);
        intent.putExtra("privilege", "0");
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