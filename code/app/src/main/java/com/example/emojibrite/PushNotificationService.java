package com.example.emojibrite;

import android.annotation.SuppressLint;
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

// This class is implemted using the following website(s) as reference(s):
// https://firebase.google.com/docs/cloud-messaging/android/first-message#java
// https://firebase.google.com/docs/cloud-messaging/android/topic-messaging?utm_source=studio#java_4

/**
 * This class is used to send notifications to the list of attendees of an event
 * A {@link FirebaseMessagingService} subclass
 */
public class PushNotificationService extends FirebaseMessagingService {
    FirebaseUser user;
    private static final String TAG = "Notify";

    /**
     * This interface is used to ensure that the token is received before it is used
     * This is because the @link{FirebaseMessaging.getInstance().getToken()} method is asynchronous
     * and the token is not guaranteed to be received before it is used
     */
    public interface TokenCallback {
        void onTokenReceived(String token);
    }

    /**
     * This callback is called when the new token is made.
     * @param token The token used for sending messages to this application instance. This token is
     *     the same as the one retrieved by {@link FirebaseMessaging#getToken()}.
     */
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

    @SuppressLint("MissingPermission")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("FirebaseMessaging", "onMessageReceived - app in foreground just got a notification");
        // Get the notification title and body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "your_channel_id")
                .setSmallIcon(R.drawable.emoji_brite_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL); // This will make a sound and vibrate
        Log.d("FirebaseMessaging", "title: " + title);
        Log.d("FirebaseMessaging", "text: " + body);
        Log.d("FirebaseMessaging", "id" + remoteMessage);
        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}
