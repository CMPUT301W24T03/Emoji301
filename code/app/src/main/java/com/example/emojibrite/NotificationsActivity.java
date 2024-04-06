package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationsActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    Users user;
    String currentUser;

    // Using a Set to avoid adding duplicate events
    Set<Event> eventSet = new HashSet<>();

    private Database database = new Database();
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        backButton = findViewById(R.id.backButtonFAB);

        // Set up the back button to finish the activity
        backButton.setOnClickListener(view -> finish());

        // Retrieve user object passed from previous activity
        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");
        if (user != null) {
            currentUser = user.getProfileUid(); // get the user ID

            //fetch events signed up to
            fetchSignedUpEvents(); // it clears up the list before it starts

            //fetch events checked into the same array as seen above in fetchSignedUpEvents
            fetchCheckedInEvents();
        }

        // Setup the adapter for the RecyclerView or ListView you are using (code for this setup is not provided here)
        // You will need to convert your Set to a List to use with an adapter
    }

    /**
     * Fetching Signed Up Events
     */
    private void fetchSignedUpEvents() {
        database.getSignedUpEvents(currentUser, this::processEvents);
    }

    private void fetchCheckedInEvents() {
        database.getCheckedInEvents(currentUser, this::processEvents);
    }

    /**
     * Process events and update UI accordingly
     * @param events List of events to be processed
     */
    private void processEvents(List<Event> events) {
        eventSet.addAll(events);
//        updateUI(new ArrayList<>(eventSet)); // WHAT I WANT IS TO TRAVERSE THROUGH ALL THE EVENTS IN THE LIST AND GET ME THE
    }


}
