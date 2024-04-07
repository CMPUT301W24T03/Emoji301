package com.example.emojibrite;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// This class is implemented using the following website(s) as reference(s):
// https://firebase.google.com/docs/cloud-messaging/android/first-message#java
// https://firebase.google.com/docs/cloud-messaging/android/topic-messaging?utm_source=studio#java_4

/**
 * This class is used to send notifications to the list of attendees of an event
 * A {@link FirebaseMessagingService} subclass
 */
public class PushNotificationService extends FirebaseMessagingService {
    FirebaseUser user;
    Database database = new Database();
    private static final String TAG = "Notify";

    /**
     * This interface is used to ensure that the token is received before it is used
     * This is because the @link{FirebaseMessaging.getInstance().getToken()} method is asynchronous
     * and the token is not guaranteed to be received before it is used
     */
    public interface TokenCallback {
        /**
         * This method is called when the token is received
         * @param token The token used for sending messages to this application instance
         */
        void onTokenReceived(String token);
    }

    /**
     * This interface hosts the callback method for the subscription to an event
     */
    public interface SubscribeCallback {
        /**
         * This method is called when the subscription is successful or not
         * @param msg The message to be displayed when the subscription is successful or not
         */
        void onSubscriptionResult(String msg);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        /* -Aivan
        send the token to the server. this is not necessary since auto-init is disabled in the manifest.
        now we get to choose when to send the token to the server (when the user enables notifications)
        instead of sending it every time the token is refreshed. Can cause issue since it might do it
        before the user document is created cuz u cant send a token to a document that doesn't exist.
         */
//        sendRegistrationToServer(token);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived - app in foreground just got a notification");

        // Handle FCM messages here
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job*/ true) {
                // for long-running task (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        }

        // Implemented using https://github.com/firebase/quickstart-android/blob/6c602cceec27ef137539390d1691846cfdb9ac21/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/java/MyFirebaseMessagingService.java#L165
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String notificationBody = remoteMessage.getNotification().getBody();
            if (remoteMessage.getNotification().getBody() != null) {
                Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
                if (remoteMessage.getNotification().getTitle() != null) {
                    foregroundNotification(notificationBody, remoteMessage.getNotification().getTitle());
                } else {
                    foregroundNotification(notificationBody, "EmojiBrite Event");
                }
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. see sendNotification method below
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * This method retrieves the FCM token for the user
     */
    public void getToken(TokenCallback callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, "FCM token before callback: " + token);

                        // call the callback method
                        callback.onTokenReceived(token);
                    }
                });
    }

    /**
     * This method deletes the FCM token for the user/
     */
    public void deleteToken() {
        FirebaseMessaging.getInstance().deleteToken()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Deleting FCM registration token failed", task.getException());
                        } else {
                            Log.d(TAG, "FCM token deleted successfully");
                        }
                    }
                });
    }

    /**
     * This method sends only the registration token to the server, thus updating it.
     * @param token The token used for sending messages to this application instance
     */
    public void sendRegistrationToServer(String token) {
        // get current instance of the user from Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference userRef = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(user.getUid());
            // update the fcmToken field in the user's document
            userRef.update("fcmToken", token)
                    .addOnSuccessListener(aVoid -> {
                        // Log the success of the update
                        Log.d(TAG, "Token updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        // Log the failure of the update
                        Log.e(TAG, "Failed to update token", e);
                    });
        }
    }

    private void scheduleJob() {

    }
    private void handleNow() {

    }

    /**
     * This method creates a notification that is displayed when the app is in the foreground
     * @param notificationBody The body of the notification to be displayed
     */
    private void foregroundNotification(String notificationBody, String eventTitle) {
        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.emoji_brite_logo)
                        .setContentTitle(eventTitle)  // change to event name in the future.
                        .setContentText(notificationBody)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Since android Oreo, notification channel is needed.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Events", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "Notification channel created: " + channel.toString());
        }
        notificationManager.notify(0/*ID of notification*/, notificationBuilder.build());
    }
    /**
     * This function is used to send a notification to the attendees of the event via topic
     * @param notifBody The message to be sent to the attendee.
     * @param eventId The id of the event to send the notification to.
     * references:
     * <a href="https://firebase.google.com/docs/cloud-messaging/android/send-multiple#build_send_requests">build_send_requests</a>
     * <a href="https://www.youtube.com/watch?v=G9TO6J_i3LU&list=WL&index=6">#13 Android Notification Tutorial - Sending Notification using HTTP V1</a>
     * <a href="https://www.youtube.com/watch?v=6_t87WW6_Gc&list=WL&index=7">Send Notification from One Device to Another Device</a>
     * <a href="https://www.youtube.com/watch?v=oNoRw69ro2k&list=WL&index=8">Firebase Cloud Messaging API (V1) Tutorial</a>
     */
    public void sendNotificationToTopic(String notifBody, String eventId) {
        Log.d("Notify", "SendNotificationToTopic is called " + eventId );

        database.getEventById(eventId, new Database.EventCallBack() {
            @Override
            public void onEventFetched(Event event) {
                String title = event.getEventTitle();
                createAndSendNotification(title, notifBody, "/topics/" + eventId);
            }
        });
    }

    /**
     * This function is used to send a notification to a specific device
     * @param notifBody The message to be sent to the attendee.
     * @param deviceToken The token of the device to send the notification to.
     */
    public void sendNotificationToDevice(String notifBody, String deviceToken) {
        Log.d("Notify", "SendNotificationToDevice is called for device: " + deviceToken);

        database.getEventById(deviceToken, new Database.EventCallBack() {
            @Override
            public void onEventFetched(Event event) {
                String title = event.getEventTitle() + " Milestone";
                createAndSendNotification(title, notifBody, deviceToken);
            }
        });
    }



    /**
     * This function is used to subscribe the user to the event via the event ID.
     * Uses the Firebase Cloud Messaging (FCM) token to subscribe the user to the event.
     * @param eventId The event ID to subscribe to
     */
    public void subscribeToEvent(String eventId, SubscribeCallback callback) {
        FirebaseMessaging.getInstance().subscribeToTopic(eventId)
                .addOnCompleteListener(task -> {
                    String msg = "Subscribe Successful";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe Failed";
                    }
                    Log.d(TAG, msg);
                    callback.onSubscriptionResult(msg);
                });
    }

    /**
     * This function is used to unsubscribe the user from the event via the event ID.
     * Uses the Firebase Cloud Messaging (FCM) token to unsubscribe the user from the event.
     * @param eventId The event ID to unsubscribe from
     */
    public void unsubscribeFromEvent(String eventId, SubscribeCallback callback) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(eventId)
                .addOnCompleteListener(task -> {
                    String msg = "Unsubscribe Successful";
                    if (!task.isSuccessful()) {
                        msg = "Unsubscribe Failed";
                    }
                    Log.d(TAG, msg);
                    callback.onSubscriptionResult(msg);
                });
    }

    /**
     * Creates the json object for the notification and sends it to the target
     * @param title The title of the notification
     * @param body The body of the notification
     * @param target The target of the notification
     */
    private void createAndSendNotification(String title, String body, String target) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // Message creation
        JSONObject message = new JSONObject();
        JSONObject notification = new JSONObject();

        try {
            message.put("to", target);
            notification.put("title", title);
            notification.put("body", body);
            message.put("notification", notification);
            Log.d("Notify", message.toString());

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
                    String responseBody = response.body().string();
                    if(response.isSuccessful()) {
                        Log.d("Notify", "Successful Response: " + responseBody);
                    } else {
                        Log.d("Notify", "Unsuccessful Response: " + responseBody);
                    }
                }
            });

        } catch (JSONException e){
            Log.d("Notify", "JSONException error: " + e);
        }
    }
}
