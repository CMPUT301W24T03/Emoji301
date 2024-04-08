package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

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

/**
 * Activity for displaying notifications related to events the user is involved in.
 * This includes notifications for events the user has signed up for or checked into.
 */

public class NotificationsActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    Users user;
    String currentUser;

    // Using a Set to avoid adding duplicate events
    Set<Event> eventSet = new HashSet<>();

    private ArrayList<EventNotifications> allEventNotifications = new ArrayList<>();
    private ListView notificationListView; // Assuming you are using a ListView
    private NotificationAdapter eventNotificationAdapter; // Assuming you have an adapter named NotificationAdapter

    private Database database = new Database();

    // Counter for tracking the number of events pending processing
    private int eventsToProcess;


    /**
     * Called when the activity is created. Sets up the UI components, retrieves the current user data,
     * and initiates the fetching of event notifications.
     *
     * @param savedInstanceState Contains data supplied if the activity is re-initialized after being shut down.
     */
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

            fetchMilestoneCompletionEvents();

//            fetchEventNotifications();
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

    /**
     * Fetches and processes events that the user has checked in for.
     */
    private void fetchCheckedInEvents() {
        database.getCheckedInEvents(currentUser, this::processEvents);
    }



    /**
     * Processes a list of events by fetching notifications for each event and updating the UI.
     * Once all events have been processed and their notifications fetched, the UI is updated.
     *
     * @param events List of events to be processed.
     */
    private void processEvents(List<Event> events) {
        eventsToProcess = events.size(); // Set the number of events that need to be processed

        if (eventsToProcess > 0) {
            for (Event event : events) {
                database.getNotificationsForEvent(event.getId(), notifications -> {
                    eventSet.add(event); // Make sure to add the event to the set
                    for (String message : notifications) {
                        allEventNotifications.add(new EventNotifications(event.getEventTitle(), message, event.getImageUri()));
                    }
                    eventsToProcess--; // Decrement the counter
                    if (eventsToProcess <= 0) {
                        // All events have been processed, update the UI
                        updateUI();
                    }
                });
            }
        } else {
            // No events to process, update the UI
            updateUI();
        }
    }

    // Call this method inside processEvents after fetching all events the user is signed up for or checked into

    /**
     * Fetches and processes notifications for each event in the set of events the user is involved in.
     * It updates the UI once all notifications have been fetched.
     */
    private void fetchEventNotifications() {
        for (Event event : eventSet) {
            database.getNotificationsForEvent(event.getId(), notifications -> {
                // For each notification message, create an EventNotification and add it to the list
                for (String message : notifications) {
                    allEventNotifications.add(new EventNotifications(event.getEventTitle(), message, event.getImageUri()));
                }
                // Update the UI after processing all events
                updateUI();
            });
        }
    }

    /**
     * this fetches events where users' current Milestones have finished
     */
    private void fetchMilestoneCompletionEvents() { //ADDING THIS COMMENT CUZ ITS MARKING THE 150TH PULL REQUEST
        if (user != null) {
            database.getEventsForOrganizerMilestoneCompletion(user.getProfileUid(), this::processMilestoneEvents);
        }
    }

    /**
     * This processes into the set and then passes into the adapter but it also hardcodes the congratulations message
     * @param events the event whose milestone is up
     */
    private void processMilestoneEvents(List<Event> events) {
        for (Event event : events) {
            String milestoneMessage = "Congratulations! Your event '" + event.getEventTitle() + "' reached its milestone.";
            EventNotifications newNotification = new EventNotifications(event.getEventTitle(), milestoneMessage, event.getImageUri());
            if (!allEventNotifications.contains(newNotification)) {
                allEventNotifications.add(newNotification);
            }
        }
        updateUI();
    }


    // Update the UI with the list of event notifications
    /**
     * Updates the user interface with the latest set of event notifications.
     * This method initializes the NotificationAdapter with the list of event notifications if it's not already created.
     * If the adapter already exists, it updates the adapter's data and refreshes the list view to reflect the latest information.
     * This method is called after event notifications have been fully processed and are ready to be displayed.
     */
    private void updateUI() {
        if (eventNotificationAdapter == null) {
            // Initialize your adapter with the list of event notifications
            eventNotificationAdapter = new NotificationAdapter(this, allEventNotifications);
            notificationListView = findViewById(R.id.attendee_view_list); // Replace with your actual ListView ID
            notificationListView.setAdapter(eventNotificationAdapter);
        } else {
            // Update the adapter's data and refresh the list
            eventNotificationAdapter.updateData(allEventNotifications);
            eventNotificationAdapter.notifyDataSetChanged();
        }
    }




}
