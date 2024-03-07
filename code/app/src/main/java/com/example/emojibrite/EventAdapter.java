package com.example.emojibrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Adapter class for displaying Event objects in a ListView.
 * This class extends ArrayAdapter and is customized to handle Event objects.
 */

public class EventAdapter extends ArrayAdapter<Event> {

    /**
     * Constructor for the EventAdapter.
     * @param context The current context.
     * @param events An ArrayList of Event objects to be displayed.
     */

    public EventAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
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
        Event event = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_event_adapter, parent, false);
        }

        ImageView eventImage = convertView.findViewById(R.id.image_event);
        TextView eventTitle = convertView.findViewById(R.id.event_title);
        TextView eventDescription = convertView.findViewById(R.id.event_description);
        TextView eventDate = convertView.findViewById(R.id.event_date);
        TextView eventTime = convertView.findViewById(R.id.event_time);
        // TextView eventCapacity = convertView.findViewById(R.id.event_capacity);



        //setting the retrieved data

        eventTitle.setText(event.getEventTitle());
        eventDescription.setText(event.getDescription());


        if (event.getDate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM | dd", Locale.getDefault());
            String formattedDate = simpleDateFormat.format(event.getDate());
            eventDate.setText(formattedDate);
        } else {
            eventDate.setText("");
        }

        eventTime.setText(event.getTime());

        if (event.getImagePath() != null) {
            Glide.with(getContext()).load(event.getImagePath()).into(eventImage);
        } else {
            eventImage.setImageResource(R.drawable.placeholder); 
        }



        return convertView;
    }
}
