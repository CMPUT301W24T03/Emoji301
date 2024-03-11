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
     * Called when an event is added or updated.
     *
     * @param event The event that was added or updated.
     */

    @Override
    public void onEventAdded(Event event) {
        addEvent(event);
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

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");


        FloatingActionButton fab = findViewById(R.id.event_add_btn);
        fab.setOnClickListener(view -> showAddEventDialog());

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
        } else if (user.getUploadedImageUri() == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EventHome.this).load(user.getAutoGenImageUri()).into(profileButton);
                }
            });
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


    }
}

