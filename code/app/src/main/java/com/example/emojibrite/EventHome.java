package com.example.emojibrite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class EventHome extends AppCompatActivity implements AddEventFragment.AddEventListener {

    ListView eventList;
    EventAdapter eventAdapter;
    ArrayList<Event> dataList;

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