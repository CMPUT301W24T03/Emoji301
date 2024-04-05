package com.example.emojibrite;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private void buttonListeners(){
        eventList.setOnItemClickListener(((parent, view, position, id) -> {
            Event selectedEvent = dataList1.get(position);

            showEventDetails(selectedEvent,user);
        }));
        QRCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the user is logged in (as in there is an account associated with the device)

                Bundle bundle = new Bundle();
                // we put in the user ID and geolocation enabled bool
                bundle.putStringArray("USER", new String[]{user.getProfileUid(), Boolean.toString(user.getEnableGeolocation())});
                Intent intent = new Intent(OtherEventHome.this, QRScanningActivity.class);
                intent.putExtras(bundle);
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

        eventAdapter = new EventAdapter(this, dataList1);
        eventList.setAdapter(eventAdapter);

        Button myEventButton = findViewById(R.id.my_events_button);

        QRCodeScanner = findViewById(R.id.event_scan_btn);

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






    }

    private void fetchAllEvents(){

        database.fetchAllEventsDatabase(events -> {
            // Clear the current list of events to prepare for updated data.
            dataList1.clear();
            dataList1.addAll(events); // Add all the retrieved events to the local list.
            eventAdapter.notifyDataSetChanged();



        });

    }

}