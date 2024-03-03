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

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_event_adapter, parent, false);
        }

        ImageView eventImage = convertView.findViewById(R.id.image_event);
        TextView eventTitle = convertView.findViewById(R.id.event_title);
        TextView eventDescription = convertView.findViewById(R.id.event_description);
        TextView eventDate = convertView.findViewById(R.id.event_date);
        TextView eventTime = convertView.findViewById(R.id.event_time);
        // TextView eventCapacity = convertView.findViewById(R.id.event_capacity); // Uncomment and correct ID if needed

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



        return convertView;
    }
}
