package com.example.emojibrite;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminEventActivity extends AppCompatActivity {

    ListView eventList;
    Users user;
    ArrayList<Event> dataList;
    Event event;
    Database database = new Database();
    EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_home);


        ImageView notifbell = findViewById(R.id.notif_bell);
        ImageView profileButton = findViewById(R.id.profile_pic);


        notifbell.setVisibility(View.GONE);
        profileButton.setVisibility(View.GONE);

        Intent intent1 = getIntent();
        user = intent1.getParcelableExtra("userObject");

        eventList = findViewById(R.id.admin_event_list);
        dataList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, dataList);
        eventList.setAdapter(eventAdapter);
        FloatingActionButton backBtn = findViewById(R.id.backButton);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminEventActivity.this, AdminActivity.class);
            intent.putExtra("userObject", user);
            startActivity(intent);
        });


        eventList.setOnItemClickListener((parent, view, position, id) -> {
            Event event = dataList.get(position);
            Intent intent = new Intent(AdminEventActivity.this, EventDetailsActivity.class);
            intent.putExtra("eventId", event.getId());
            intent.putExtra("userlol", user.getProfileUid());
            intent.putExtra("priviledge", "2");
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchEvents();
    }
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
