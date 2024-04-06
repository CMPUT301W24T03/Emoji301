package com.example.emojibrite;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Event> {

    public NotificationAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }
}
