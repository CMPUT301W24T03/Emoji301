package com.example.emojibrite;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * EventHome is an AppCompatActivity that serves as the main page for displaying a list of events.
 * It allows users to view event details and add new events.
 */

public class EventHome extends AppCompatActivity implements AddEventFragment.AddEventListener {

    ListView eventList;
    EventAdapter eventAdapter; // Custom adapter to bind event data to the ListView
    ArrayList<Event> dataList;
    private Users user;
    private Database database = new Database(this);

//    Button otherEvent = findViewById(R.id.other_events_button);

    ImageView profileButton;

    private static final String TAG = "ProfileActivityTAG";

    /**
     * Opens the EventDetailsActivity to show the details of the selected event.
     *
     * @param event The event to show details for.
     */

    private void showEventDetails(Event event) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        startActivity(intent);
    }

    /**
     * Shows the AddEventFragment to add a new event.
     */

    public void showAddEventDialog() {
        AddEventFragment dialog = AddEventFragment.newInstance(user);
        dialog.show(getSupportFragmentManager(), "AddEventFragment");
    }


    /**
     * Callback method triggered when an event is added.
     * It attempts to add the event to the Firestore database.
     *
     * @param event The event object to be added. This should not be null.
     */
    @Override
    public void onEventAdded(Event event){
        if (event!=null){
            //We call the addEvent method in database class
            database.addEvent(event, task -> {
                if (task.isSuccessful()){
                    Log.d(TAG, "Event added successfully into database");
                    updateLocalEventList(event);
                }
                else {
                    Log.e(TAG, "ERROR IN ADDING TO THE DATABASE", task.getException());
                }
            });
        }
    }

    /**
     * Updates the local list of events. This method is called after an event is successfully added
     * to the Firestore database to reflect the change in the local user interface.
     *
     * @param event The newly added or updated event.
     */
    private void updateLocalEventList(Event event) {
        int index = -1;
        for (int i = 0; i < dataList.size(); i++) {

            String existingEventId = dataList.get(i).getId();
            String newEventId = event.getId();
            // Check if both IDs are non-null and equal
            if (existingEventId != null && newEventId != null && existingEventId.equals(newEventId)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            // If the event is found, update it in the list.
            dataList.set(index, event);
        } else {
            // If the event is not found, add it to the list.
            dataList.add(event);
        }
        // Notify the adapter that the data has changed to update the UI.
        eventAdapter.notifyDataSetChanged();
    }



    /**
     * Adds an event to the list of events and updates the ListView.
     *
     * @param event The event to add.
     */
    public void addEvent(Event event) {
        if (event != null) {
            EventRepository.getInstance().addEvent(event);
            int index = -1;
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getId().equals(event.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                dataList.set(index, event);
            } else {
                dataList.add(event);
            }
            eventAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.event_home_page);

        eventList = findViewById(R.id.event_organizer_list);
        dataList = new ArrayList<>();

        eventAdapter = new EventAdapter(this, dataList);
        eventList.setAdapter(eventAdapter);

        eventList.setOnItemClickListener(((parent, view, position, id) -> {
            Event selectedEvent = dataList.get(position);

            showEventDetails(selectedEvent);

        }));

        Button otherEvent = findViewById(R.id.other_events_button);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");
        if(user!=null) {
            Log.d(TAG, "user name for EventHome: " + user.getName() + user.getProfileUid() + user.getUploadedImageUri() + user.getAutoGenImageUri() + user.getHomePage());
            Log.d(TAG, "user id for EventHome: " + user.getProfileUid());
            database.setUserObject(user);
            FloatingActionButton fab = findViewById(R.id.event_add_btn);
            fab.setOnClickListener(view -> showAddEventDialog());
        }

        ImageView profileButton = findViewById(R.id.profile_pic);

        if (user.getUploadedImageUri() != null) {
            // User uploaded a picture, use that as the ImageView
            //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EventHome.this).load(user.getUploadedImageUri()).into(profileButton);
                }
            });
            fetchEventsForCurrentUser();
        } else if (user.getUploadedImageUri() == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EventHome.this).load(user.getAutoGenImageUri()).into(profileButton);
                }
            });

            fetchEventsForCurrentUser();
        }



        // When profile is clicked, go to profile activity
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to the ProfileActivity page
                Log.d(TAG, "Enter button clicked"); // for debugging

                Intent intent = new Intent(EventHome.this, ProfileActivity.class);

                intent.putExtra("userObject", user);
                startActivity(intent);


            }
        });

        otherEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Other events button clicked");
                Intent intent2 = new Intent(EventHome.this, OtherEventHome.class);

                intent2.putExtra("userObject", user);
                startActivity(intent2);  // Use intent2 to start the activity
            }
        });




    }

    /**
     * Fetches events from the Firestore database that are organized by the current user.
     * This method queries the database for events where the current user is the organizer and updates
     * the local list to reflect these events. This is typically used to populate the UI with relevant data.
     */
    private void fetchEventsForCurrentUser() {

        if (user != null) {
            // Retrieve the unique ID of the current user.
            String currentUserId = user.getProfileUid();
            // Call the method in the Database class to get events organized by this user.
            database.getEventsByOrganizer(currentUserId, events -> {
                // Clear the current list of events to prepare for updated data.
                dataList.clear();
                dataList.addAll(events); // Add all the retrieved events to the local list.
                eventAdapter.notifyDataSetChanged();
            });
        }
    }
}

