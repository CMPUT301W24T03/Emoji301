package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class EventHome extends AppCompatActivity implements AddEventFragment.AddEventListener {

    ListView eventList;
    EventAdapter eventAdapter;
    ArrayList<Event> dataList;

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

        FloatingActionButton fab = findViewById(R.id.event_add_btn);
        fab.setOnClickListener(view -> showAddEventDialog());
    }

    private void showEventDetails(Event event) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        startActivity(intent);
    }

    public void showAddEventDialog() {
        AddEventFragment dialog = new AddEventFragment();
        dialog.show(getSupportFragmentManager(), "AddEventFragment");
    }

    @Override
    public void onEventAdded(Event event) {
        addEvent(event);
    }

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

    // Other methods...
}
