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

// This class is implemented using the following website(s) as reference(s):
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

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
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
}
