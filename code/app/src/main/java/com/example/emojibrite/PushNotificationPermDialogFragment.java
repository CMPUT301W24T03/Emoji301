package com.example.emojibrite;



import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class PushNotificationPermDialogFragment extends DialogFragment {
    PushNotificationDialogListener listener;
    private final static String TAG = "PushNotificationPermDialogFragment";

    /**
     * An interface to communicate with the activity
     */
    public interface PushNotificationDialogListener {
        void onNotifPermDialogPositiveClick(String token);
        void onNotifPermDialogNegativeClick();
    }

    /**
     * This interface is used to ensure that the token is received before it is used
     * This is because the @link{FirebaseMessaging.getInstance().getToken()} method is asynchronous
     * and the token is not guaranteed to be received before it is used
     */
    public interface TokenCallback {
        void onTokenReceived(String token);
    }

    /**
     * onAttach method to attach the context to the listener
     * @param context the activity context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PushNotificationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement NotificationDialogListener");
        }
    }

    /**
     * onCreateDialog method to create the dialog
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     * @return the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enable Notifications");
        builder.setMessage("Would you like to enable notifications?");

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // fetch the token from FCM
                getToken(new TokenCallback() {
                    @Override
                    public void onTokenReceived(String token) {
                        listener.onNotifPermDialogPositiveClick(token);
                    }
                });

            }
        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNotifPermDialogNegativeClick();
            }
        });
        return builder.create();
    }

    /**
     * This method retrieves the FCM token for the user
     */
    private void getToken(TokenCallback callback) {
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
}

