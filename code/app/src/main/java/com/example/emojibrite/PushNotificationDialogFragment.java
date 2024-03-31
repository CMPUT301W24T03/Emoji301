package com.example.emojibrite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * PushNotificationDialogFragment is a DialogFragment used to create
 * a dialog box for the message of the notification
 */
public class PushNotificationDialogFragment extends DialogFragment {
    /**
     * NotificationDialogListener interface to communicate with the activity
     */
    public interface NotificationDialogListener {
        /**
         * method to handle the positive click of the dialog
         * @param message The message to be sent to the list of attendees
         */
        void onDialogPositiveClick(String message);
    }

    // instance of the interface
    NotificationDialogListener listener;

    /**
     * onAttach method to attach the context to the listener
     * @param context the activity context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
             listener = (NotificationDialogListener) context;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_notification_dialog, null);

        // Associate the layout with the dialog
        EditText message = view.findViewById(R.id.notification_text);

        return builder.setView(view)
                .setTitle("Send Notification")
                .setPositiveButton("Send", (dialog, which) -> {
                    listener.onDialogPositiveClick(message.getText().toString());
                    // logic
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    PushNotificationDialogFragment.this.getDialog().cancel();})
                .create();
    }
}
