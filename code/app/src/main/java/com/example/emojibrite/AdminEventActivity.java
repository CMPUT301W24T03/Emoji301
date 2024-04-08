package com.example.emojibrite;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
/**
 * Activity for the admin event view
 */
public class AdminEventActivity extends AppCompatActivity {

    ListView eventList;
    static Users user;
    ArrayList<Event> dataList;
    Event event;

    EventAdapter eventAdapter;
    FloatingActionButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_home);


        ImageView notifbell = findViewById(R.id.notif_bell);
        ImageView profileButton = findViewById(R.id.profile_pic);


        notifbell.setVisibility(View.GONE);
        profileButton.setVisibility(View.GONE);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");

        eventList = findViewById(R.id.admin_event_list);
        dataList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, dataList, null);
        eventList.setAdapter(eventAdapter);
        backBtn = findViewById(R.id.backButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEventActivity.this, AdminActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = dataList.get(position);
                Intent intent = new Intent(AdminEventActivity.this, EventDetailsActivity.class);
                intent.putExtra("eventId", event.getId());
                intent.putExtra("userObject", user);
                intent.putExtra("privilege", "2");
                startActivity(intent);
            }
        });

        fetchEvents();
    }

    /**
     * onResume method to handle the resuming of the activity
     */
    private void fetchEvents(){
        Database database = new Database();
        database.fetchAllEventsDatabase(new Database.OnEventsRetrievedListener() {
            @Override
            public void onEventsRetrieved(List<Event> events) {
                dataList.clear();
                dataList.addAll(events);
                eventAdapter.notifyDataSetChanged();
            }
        });
    }
}
