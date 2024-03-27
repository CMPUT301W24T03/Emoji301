package com.example.emojibrite;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * This class is used to send notifications to the list of attendees of an event
 * A {@link FirebaseMessagingService} subclass
 */
public class PushNotificationService extends FirebaseMessagingService {
    private static final String TAG = "Notify";

    /**
     * This function is called when the new token is made.
     * @param token The token used for sending messages to this application instance. This token is
     *     the same as the one retrieved by {@link FirebaseMessaging#getToken()}.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
