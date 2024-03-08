package com.example.emojibrite;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.auth.User;

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

    private Database database = new Database();




    Button profileButton;



    private static final String TAG = "ProfileActivityTAG";

    /**
     * Opens the EventDetailsActivity when an event is selected.
     * @param event The event to show details for.
     */

    private void showEventDetails(Event event) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        startActivity(intent);
    }

    /**
     * Opens the AddEventFragment as a dialog to add a new event.
     */

    public void showAddEventDialog() {
        AddEventFragment dialog = new AddEventFragment();
        dialog.show(getSupportFragmentManager(), "AddEventFragment");
    }

    /**
     * This method is called when a new event is added from the AddEventFragment.
     * @param event The new or updated event to add to the list.
     */

    @Override
    public void onEventAdded(Event event) {
        addEvent(event);
    }

    /**
     * Adds an event to the dataList and updates the ListView.
     * If the event already exists, it is updated. Otherwise, it's added to the list.
     * @param event The event to add or update.
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
     * onCreate is called when the activity is starting.
     * It initializes the activity, the ListView, and the FloatingActionButton.
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

        database.getUploadedProfileImageFromDatabase(new Database.ProfileImageCallBack() {
            @Override
            public void onProfileImageComplete(Bitmap profileImage) {
                // Use the profileImage bitmap here
                // For example, you can set it to an ImageView
                user.setUploadedImage(profileImage);
                if (user.getUploadedImage() == null){
                    String autoGenImage = user.getAutoGenImage();
                    byte[] decodedString = Base64.decode(autoGenImage,Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                    profileButton.setImageBitmap(decodedByte);}
                else {
                    String uploadedImage = user.getUploadedImage();
                    byte[] decodedString = Base64.decode(uploadedImage,Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                    profileButton.setImageBitmap(decodedByte);

                }

//                profileButton.setImageBitmap(profileImage);
            }
        });





        // When profile is clicked, go to profile activity
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to the ProfileActivity page
                Log.d(TAG, "Enter button clicked"); // for debugging

                Intent intent = new Intent(EventHome.this, ProfileActivity.class);

                intent.putExtra("userObject",user);
                startActivity(intent);


            }
        });


    }






//    public void showEditEventDialog(Event eventToEdit) {
//        AddEventFragment dialog = AddEventFragment.newInstance(eventToEdit);
//        dialog.show(getSupportFragmentManager(), "AddEventFragment");
//    }

}