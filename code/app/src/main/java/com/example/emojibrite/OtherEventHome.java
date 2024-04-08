package com.example.emojibrite;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for the other event home view
 */
public class OtherEventHome extends AppCompatActivity {

    ListView eventList;
    EventAdapter eventAdapter; // Custom adapter to bind event data to the ListView
    ArrayList<Event> dataList1;
    private Users user;
    private Database database = new Database();

    private Map<Event, Boolean> eventMap = new HashMap<>();

    Context context = this;

    FloatingActionButton QRCodeScanner;

    ImageView profileButton;

    ImageView notifButton;

    private static final String TAG = "ProfileActivityTAG";

    /**
     * Opens the EventDetailsActivity to show the details of the selected event.
     *
     * @param event The event to show details for.
     */

    private void showEventDetails(Event event, Users user) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        if (user!=null){
            Log.d("TAG","CHECKING CHECKING CHECKING  "+ user.getProfileUid());}
        intent.putExtra("userObject", user);
        intent.putExtra("privilege", "1");//You send the current user profile id into the details section
        startActivity(intent);
    }


    /**
     * Checks if the user's document exists in the database and updates the UI accordingly.
     *
     * @param userUid The unique identifier of the user.
     */
    private void checkUserDoc(String userUid){
        database.getUserDocument(userUid, documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d(TAG, "User document exists");
                user = documentSnapshot.toObject(Users.class);
                user.setEnableAdmin(false);
                buttonListeners();
                settingUpPfp();

            } else {
                Log.d(TAG, "User document does not exist");

                Toast.makeText(this, "User got deleted by admin", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtherEventHome.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * Sets up listeners for various buttons in the activity including the event list, QR code scanner,
     * and profile button.
     */
    private void buttonListeners(){
        eventList.setOnItemClickListener(((parent, view, position, id) -> {
            Event selectedEvent = dataList1.get(position);

            showEventDetails(selectedEvent,user);
        }));
        QRCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherEventHome.this, QRScanningActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to the ProfileActivity page
                Log.d(TAG, "Enter button clicked"); // for debugging

                Intent intent = new Intent(OtherEventHome.this, ProfileActivity.class);

                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });


    }

    /**
     * Starts the NotificationsActivity to display user notifications.
     *
     * @param user The current user object.
     */
    private void showNotifications(Users user){
        Intent intent = new Intent(this, NotificationsActivity.class);
        if (user!=null){
            Log.d("TAG","CHECKING CHECKING CHECKING  "+ user.getProfileUid());}
        intent.putExtra("userObject", user);
        startActivity(intent);
    }

    /**
     * Sets up the user's profile picture in the activity.
     * It uses Glide to load the image asynchronously.
     */
    private void settingUpPfp(){
        if (user.getUploadedImageUri() != null) {
            // User uploaded a picture, use that as the ImageView
            //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(OtherEventHome.this).load(user.getUploadedImageUri()).into(profileButton);
                }
            });
            fetchAllEvents();
        } else if (user.getUploadedImageUri() == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(OtherEventHome.this).load(user.getAutoGenImageUri()).into(profileButton);
                }
            });
            fetchAllEvents();

        }

    }

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_event_home);

        eventList = findViewById(R.id.event_organizer_list);
        dataList1 = new ArrayList<>();

        eventAdapter = new EventAdapter(this, dataList1, null);
        eventList.setAdapter(eventAdapter);

        Button myEventButton = findViewById(R.id.my_events_button);

        QRCodeScanner = findViewById(R.id.event_scan_btn);

        notifButton = findViewById(R.id.notif_bell);

        myEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");

        profileButton = findViewById(R.id.profile_pic);

        checkUserDoc(user.getProfileUid());

        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotifications(user);

            }
        });






    }

    /**
     * Fetches all events from the database and updates the ListView adapter with these events.
     */
    private void fetchAllEvents(){

        database.fetchAllEventsDatabase(events -> {
            // Clear the current list of events to prepare for updated data.
            dataList1.clear();
            dataList1.addAll(events); // Add all the retrieved events to the local list.
            eventAdapter.notifyDataSetChanged();



        });

    }

}