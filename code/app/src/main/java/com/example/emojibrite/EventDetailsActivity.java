package com.example.emojibrite;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType.Companion.*;

/**
 * EventDetailsActivity is responsible for displaying the detailed information
 * of a selected event. It retrieves event data based on the passed event ID
 * and sets up the views to display this data.
 */
public class EventDetailsActivity extends AppCompatActivity implements PushNotificationDialogFragment.NotificationDialogListener{

    String currentUser;

    ArrayList<String> signedAttendees;

    Button signingup, attendeesButton, notificationButton,  deleteBtn, qrBtn, qrCodeEventDetails;
    TextView showMap;

    Database database;

    String privilege;

    String eventId;
    Users user;


    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, using findViewById(int)
     * to programmatically interact with widgets in the UI, etc.
     *
     * @param savedInstanceState If the activity is being re-initialized after being shut down,
     *                           then this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        ImageView backButton = findViewById(R.id.imageView); //back button
        attendeesButton = findViewById(R.id.attendees_button);
        signingup=findViewById(R.id.sign_up_button);
        notificationButton = findViewById(R.id.Notification_button);
        deleteBtn = findViewById(R.id.delete_event);
        showMap = findViewById(R.id.show_map);
        qrBtn = findViewById(R.id.qr_code);

        qrCodeEventDetails = findViewById(R.id.qr_code);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");
        currentUser = user.getProfileUid(); // get the user

        // Retrieving the event ID passed from the previous activity.
        eventId = getIntent().getStringExtra("eventId");

        privilege = intent.getStringExtra("privilege");
        if (privilege.equals("2")){
            ImageView notifbell = findViewById(R.id.notif_bell);
            ImageView profileButton = findViewById(R.id.profile_pic);

            notifbell.setVisibility(View.GONE);
            profileButton.setVisibility(View.GONE);
            attendeesButton.setVisibility(View.GONE);
            notificationButton.setVisibility(View.GONE);

            signingup.setVisibility(View.GONE);
            showMap.setVisibility(View.GONE);
            qrBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);

        }
        else{
            deleteBtn.setVisibility(View.GONE);
            attendeesButton.setVisibility(View.VISIBLE);
            signingup.setVisibility(View.VISIBLE);
            showMap.setVisibility(View.VISIBLE);

        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlertBuilder();

            }
        });



        if (currentUser!=null){
            Log.d(TAG,"YEPPIEEEE "+currentUser);
        }
        else{
            Log.d(TAG, "SAAAAAAD IT IS NULL");
        }


        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }


        });

        qrCodeEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var = "true";
                Intent intent = new Intent(EventDetailsActivity.this, DisplayEventQRCode.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
            }
        });


        attendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailsActivity.this, AttendeesListActivity.class);
                // todo: give attendees list activity something like the event id or list of attendees, etc.
                intent.putExtra("eventId",eventId);
                // intent.putExtra("attendees", signedAttendees); for example
                Log.d("Aivan" , "Attendees button clicked");
                startActivity(intent);
            }
        });

        ImageView backArrow = findViewById(R.id.back_arrow_eventdetails); //back button
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



        signedAttendees=new ArrayList<>();  //TODO: CREATE A NEW ARRAYLIST CALLED NOTIFICATION?

//        signingup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signingup.setText("You have signed up"); // Set the text to indicate the user has signed up
//                signingup.setBackgroundColor(Color.GREEN); // Change the background color to green
//                signingup.setEnabled(false);
//            }
//        });


        database = new Database();
        database.getEventById(eventId, new Database.EventCallBack() {
            @Override
            public void onEventFetched(Event event) {
                if (event != null) {
                    setupViews(event);
                    database.getSignedAttendees(eventId, attendees -> {
                        signedAttendees = new ArrayList<>(attendees);
                        updateSignUpStatus(eventId, event.getCapacity());
                    });
                } else {
                    // Handle the case where event is null
                }
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PushNotificationDialogFragment().show(getSupportFragmentManager(), "PushNotificationDialogFragment");
            }
        });
    }

    /**
     * This function is to handle the view as well the signing up part of events
     * @param eventId this uses the event ID where users will be setting it up
     * @param capacity this is the capacity of the event
     */

    private void updateSignUpStatus(String eventId, Integer capacity) {

        boolean isCapacityFull = capacity != null && signedAttendees.size() >= capacity;
        boolean isUserSignedUp = signedAttendees.contains(currentUser);

        if (isCapacityFull){
            signingup.setText("Event has reached capacity"); // Set the text to indicate the user has signed up
            signingup.setBackgroundColor(Color.RED); // Change the background color to green
            signingup.setEnabled(false);
        }
        else if (isUserSignedUp){
            signingup.setText("You have signed up"); // Set the text to indicate the user has signed up
            signingup.setBackgroundColor(Color.GREEN); // Change the background color to green
            signingup.setEnabled(false);
        }
        else{
            signingup.setOnClickListener(v -> signUpForEvent(eventId));
        }
    }

    /**
     *This just signs the user into event Id
     * @param eventId
     */

    private void signUpForEvent(String eventId) {
        signedAttendees.add(currentUser);
        subscribeToEvent(eventId);
        database.addSignin(eventId, currentUser);
        signingup.setText("You have signed up"); // Set the text to indicate the user has signed up
        signingup.setBackgroundColor(Color.GREEN); // Change the background color to green
        signingup.setEnabled(false);
    }



    /**
     * Sets up the views in the layout with the details of the event.
     *
     * @param event The event object containing the details to be displayed.
     */
    private void setupViews(Event event) {

        // Handling event poster image loading.
        ImageView eventPosterImage = findViewById(R.id.image_event_poster);
        if (event.getImageUri() != null) {
            // Using Glide library to load the image from the URL/path.
            Glide.with(EventDetailsActivity.this).load(event.getImageUri()).into(eventPosterImage);
        } else {
            // In case the event does not have an image, a placeholder image is used.
            eventPosterImage.setImageResource(R.drawable.placeholder);
        }

        if (event.getOrganizer()!=null){
            Database database = new Database();
            database.getUserDocument(event.getOrganizer(), new Database.OnUserDocumentRetrievedListener(){
                @Override
                public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot){
                    if (documentSnapshot.exists()){
                        Users organizer = documentSnapshot.toObject(Users.class);
                        if (organizer!=null){
                            setOrganizerViews(organizer, event.getId());
                        }
                        else{
                            Log.d(TAG,"no organizer document. CHECK FIREBASE");
                        }
                    }
                }
            });
        }



        if (event.getOrganizer().equals(currentUser)){ // you get the current user and the organizer
            //if both their ids match, that means they are the organizer itslf
            //so they do not have to sign up aka, we can remove it
            signingup.setVisibility(View.GONE);
            if (!privilege.equals("2")) {
                notificationButton.setVisibility(View.VISIBLE);
            }
            Log.d(TAG,"SIGNINGUP BUTTON IS GOOONE");

        }
        else{
            //if they are not the same, that means that the current user is an attendee
            //therefore they dont have to see the attendees list
            Log.d(TAG,"OOPS, FAILED");
            Log.d(TAG,("ORGANIZER:")+event.getOrganizer());
            Log.d(TAG,"Current User"+currentUser);
            attendeesButton.setVisibility(View.GONE);
            notificationButton.setVisibility(View.GONE);

        }
        // Formatting and displaying the event date and time.
        String dateTime = "";
        if (event.getDate() != null) {
            dateTime = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(event.getDate());
        }
        dateTime += (event.getTime() != null) ? " " + event.getTime() : "";
        setTextOrHide(findViewById(R.id.Event_DateandTime), dateTime);

        setTextOrHide(findViewById(R.id.Event_Title), event.getEventTitle());
        setTextOrHide(findViewById(R.id.location), event.getLocation());
        setTextOrHide(findViewById(R.id.milestone), (event.getMilestone() != null) ? String.valueOf(event.getMilestone()) : null);
        setTextOrHide(findViewById(R.id.capacity), (event.getCapacity() != null) ? String.valueOf(event.getCapacity()) : null);
        setTextOrHide(findViewById(R.id.more_details_Event), event.getDescription());
        setTextOrHide(findViewById(R.id.location_details), event.getLocation());
    }

    /**
     * The function is to generate real time organizer's data such as their username and profile picture
     * @param organizer The organizer as the user object contains all the organizer's information
     */
    private void setOrganizerViews(Users organizer, String eventId) {
        ImageView organizerProfilePic = findViewById(R.id.EmojiBriteLogo);
        setTextOrHide(findViewById(R.id.organizer_name), organizer.getName());


        //CHECK IF ORGANIZER HAS
        if(organizer.getUploadedImageUri()!=null){
            new Handler(Looper. getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EventDetailsActivity.this).load(organizer.getUploadedImageUri()).into(organizerProfilePic);
                }
            });
        } else if (organizer.getUploadedImageUri() == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EventDetailsActivity.this).load(organizer.getAutoGenImageUri()).into(organizerProfilePic);
                }
            });
        }

    }
    /**
     * Sets text to a TextView or hides it if the text is null or empty.
     *
     * @param textView The TextView to be manipulated.
     * @param text     The text to be set to the TextView.
     */
    private void setTextOrHide(TextView textView, String text) {
        if (text != null && !text.isEmpty()) {
            textView.setText(text);
        } else {
            // If text is null or empty, make the TextView disappear from the layout.
            textView.setVisibility(View.GONE);
        }
    }

    // Start of Notification stuff

    /**
     * method to handle the positive click of the dialog
     * @param message The message to be sent to the list of attendees
     */
    @Override
    public void onDialogPositiveClick(String message) {
        sendNotification(message);
    }

    /**
     * This function is used to send a notification to the attendees of the event
     * @param notifBody The message to be sent to the attendee
     * reference: <a href="https://firebase.google.com/docs/cloud-messaging/android/send-multiple#build_send_requests">Here</a>
     *                  <a href="https://www.youtube.com/watch?v=G9TO6J_i3LU&list=WL&index=6">here2</a>
     *                  <a href="https://www.youtube.com/watch?v=6_t87WW6_Gc&list=WL&index=7">here3</a>
     *                  <a href="https://www.youtube.com/watch?v=oNoRw69ro2k&list=WL&index=8">here4</a>
     */
    private void sendNotification(String notifBody) {
        Log.d("Notify", "SendNotification is called");
        // todo: might want ot put this in PushNotificationService. Also send() is deprecated
        // send the notification to the attendees signed up for the event (eventId)
        // The topic name can be optionally prefixed with "/topics/"
        String topic = eventId;

//        // See documentation on defining a message payload
//        Message messageNotif = Message.builder()
//                .putData("title", "EmojiBrite")
//                .putData("body", message.toString())
//                .setTopic(topic)
//                .build();
//
//        // Send a message to the devices subscribed to the provided topic:
//        String response = FirebaseMessaging.getInstance().send(messageNotif);
//        // Response is a message ID string.
//        Log.d(TAG, "Successfully sent message: " + response);

//        String host = "https://fcm.googleapis.com/v1/projects/emojibrite/messages:send";
//        String token = "0b50cc51aa0d6c280549701f8f5149ae003f5580"; // todo: replace with your server key

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        // Message creation
        JSONObject message = new JSONObject();
        JSONObject notification = new JSONObject();

        try{
            notification.put("title", "EmojiBrite");
            notification.put("body", notifBody);
            JSONObject msg = new JSONObject();
            msg.put("topic", topic);
            msg.put("notification", notification);
            message.put("message", msg);
            Log.d("Notify", message.toString());
        } catch (JSONException e){
            Log.d("Notify", "JSONException error: " + e);
        }

        // send message to firebase API
        RequestBody rBody = RequestBody.create(message.toString(), mediaType);
        Log.d("Notify", "rBody: "+ rBody);
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(rBody)
                .addHeader("Authorization", "key=AAAAiYm6-Io:APA91bE8KQIhhQ7C3QeqISXSEfWcyr_p9-QWvquKJoHrTqHknOfjLdLGopi88PqhDdLkU2Il1vbG9NLLK7TkfqAZytcnxm48Ux2hdlPOhwnh4GHWip2KEqE346t2y2wOcNexz9djZrb7")
                .addHeader("Content-Type", "application/json")
                .build();
        Log.d("Notify", "Request " +  request);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // This is where you would handle a request failure
                Log.d("Notify", "IOException error: " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // This is where you would handle the response
                Log.d("Notify", "Response: " + response);
                response.close();
            }
        });

    }

    /**
     * This function is used to subscribe the user to the event via the event ID.
     * Uses the Firebase Cloud Messaging (FCM) token to subscribe the user to the event.
     * @param eventId The event ID to subscribe to
     */
    private void subscribeToEvent(String eventId) {
        FirebaseMessaging.getInstance().subscribeToTopic(eventId)
                .addOnCompleteListener(task -> {
                    String msg = "User "+ currentUser +" is now subscribed to event: " + eventId;
                    if (!task.isSuccessful()) {
                        msg = "Failed to subscribe user " + currentUser + "to event: " + eventId;
                    }
                    Log.d(TAG, msg);
                    Toast.makeText(EventDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * This function is used to unsubscribe the user from the event via the event ID.
     * Uses the Firebase Cloud Messaging (FCM) token to unsubscribe the user from the event.
     * @param eventId The event ID to unsubscribe from
     */
    private void unsubscribeFromEvent(String eventId) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(eventId)
                .addOnCompleteListener(task -> {
                    String msg = "User "+ currentUser +" is now unsubscribed from event: " + eventId;
                    if (!task.isSuccessful()) {
                        msg = "Failed to unsubscribe user " + currentUser + "from event: " + eventId;
                    }
                    Log.d(TAG, msg);
                    Toast.makeText(EventDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }
    // End of Notification stuff

    private void deleteAlertBuilder(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the message and the title of the dialog
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this event?");
        // Set the positive (Yes) button and its click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteEvent(eventId, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Event deleted successfully");
                            Intent intent = new Intent(EventDetailsActivity.this, AdminEventActivity.class);
                            intent.putExtra("userObject", user);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.d(TAG, "Failed to delete event");
                        }
                    }
                });
            }
        });
        // Set the negative (No) button and its click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        // Create and show the dialog
        builder.create().show();
    }

}