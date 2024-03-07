package com.example.emojibrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class EventHome extends AppCompatActivity implements AddEventFragment.AddEventListener {

    ListView eventList;
    EventAdapter eventAdapter;
    ArrayList<Event> dataList;

    Button profileButton;


    private static final String TAG = "ProfileActivityTAG";

    @Override
    public void onEventAdded(Event event) {
        addEvent(event);
    }


    public void addEvent(Event event) {
        if (event != null) {
            // Check if the event already exists in the list
            int index = -1;
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getId().equals(event.getId())) { // Assuming Event class has getId() method
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                // Event exists, update it
                dataList.set(index, event);
            } else {
                // New event, add it
                dataList.add(event);
            }
            eventAdapter.notifyDataSetChanged();
        }
    }





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
            showEditEventDialog(selectedEvent);
        }));

        FloatingActionButton fab = findViewById(R.id.event_add_btn);
        fab.setOnClickListener(view -> showAddEventDialog());

        FloatingActionButton profileButton = findViewById(R.id.profileButton);
        // When profile is clicked, go to profile activity
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to the ProfileActivity page
                Log.d(TAG, "Enter button clicked"); // for debugging
                Intent intent = new Intent(EventHome.this, ProfileActivity.class);
                startActivity(intent);


            }
        });


    }




    public void showEditEventDialog(Event eventToEdit) {
        AddEventFragment dialog = AddEventFragment.newInstance(eventToEdit);
        dialog.show(getSupportFragmentManager(), "AddEventFragment");
    }

    public void showAddEventDialog() {
        AddEventFragment dialog = new AddEventFragment();
        dialog.show(getSupportFragmentManager(), "AddEventFragment");
    }
}