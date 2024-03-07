package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
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

    /**
     * onCreate is called when the activity is starting.
     * It initializes the activity, the ListView, and the FloatingActionButton.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_home_page);

        // Initialize the ListView and ArrayList

        eventList = findViewById(R.id.event_organizer_list);
        dataList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, dataList);
        eventList.setAdapter(eventAdapter);

        // Set an item click listener on the ListView to handle event selection

        eventList.setOnItemClickListener(((parent, view, position, id) -> {
            Event selectedEvent = dataList.get(position);
            showEventDetails(selectedEvent);
        }));

        // Initialize the FloatingActionButton and set its click listener

        FloatingActionButton fab = findViewById(R.id.event_add_btn);
        fab.setOnClickListener(view -> showAddEventDialog());
    }

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


}
