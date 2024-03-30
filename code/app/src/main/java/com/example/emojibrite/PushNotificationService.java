package com.example.emojibrite;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * This class is used to send notifications to the list of attendees of an event
 * A {@link FirebaseMessagingService} subclass
 */
public class PushNotificationService extends FirebaseMessagingService {
    FirebaseUser user;
    private static final String TAG = "Notify";

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
}
