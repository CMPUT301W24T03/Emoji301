package com.example.emojibrite;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NotificationAdapter extends ArrayAdapter<EventNotifications> {

    private ArrayList<EventNotifications> notifications;

    public NotificationAdapter(Context context, ArrayList<EventNotifications> notifications){
        super(context, 0, notifications);
        this.notifications = new ArrayList<>(notifications); // Initialize the list
    }

    /**
     * It updates the the array list
     * @param newNotifications This is the array of event notifications to be updated.
     */
    public void updateData(ArrayList<EventNotifications> newNotifications) {
        this.notifications.clear();
        this.notifications.addAll(newNotifications);
        notifyDataSetChanged(); // Notify the adapter to refresh the list
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EventNotifications eventNotifications = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notifications_list, parent, false);
        }

        ImageView eventPoster = convertView.findViewById(R.id.image_event);
        TextView eventTitle = convertView.findViewById(R.id.event_title);
        TextView notificationMessage = convertView.findViewById(R.id.event_notifications);

        eventTitle.setText(eventNotifications.getEventTitle());
        notificationMessage.setText(eventNotifications.getNotifMessage());

        if (eventNotifications.getImageUri() != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getContext()).load(eventNotifications.getImageUri()).into(eventPoster);
                }
            });
//            Glide.with(getContext()).load(event.getImageUri()).into(eventImage);
            Log.d("NotificationsAdapter", "PICTURE LOOOOAAADDING");
        } else {
            eventPoster.setImageResource(R.drawable.placeholder);
            Log.d("notificationsAdapter", "PICTURE NOOOOOOOT LOOOOAAADDING");
        }



        return convertView;
    }
}
