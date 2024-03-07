package com.example.emojibrite;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        String eventId = getIntent().getStringExtra("eventId");
        Event event = EventRepository.getInstance().getEventById(eventId);
        if (event != null) {
            setupViews(event);
        }
    }

    private void setupViews(Event event) {
        ImageView eventPosterImage = findViewById(R.id.image_event_poster);
        if (event.getImagePath() != null) {
            Glide.with(this).load(event.getImagePath()).into(eventPosterImage);
        } else {
            eventPosterImage.setImageResource(R.drawable.placeholder);
        }

        String dateTime = "";
        if (event.getDate() != null) {
            dateTime = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(event.getDate());
        }
        dateTime += (event.getTime() != null) ? " " + event.getTime() : "";
        setTextOrHide(findViewById(R.id.Event_DateandTime), dateTime);

        setTextOrHide(findViewById(R.id.Event_Title), event.getEventTitle());
        setTextOrHide(findViewById(R.id.location), event.getLocation());
        setTextOrHide(findViewById(R.id.milestone), (event.getMilestone() != null) ? String.valueOf(event.getMilestone()) : null);
        setTextOrHide(findViewById(R.id.capacity), (event.getCapacity() != null) ? String.valueOf(event.getCapacity()) : null);
        setTextOrHide(findViewById(R.id.more_details_Event), event.getDescription());
        setTextOrHide(findViewById(R.id.location_details), event.getLocation());
    }

    private void setTextOrHide(TextView textView, String text) {
        if (text != null && !text.isEmpty()) {
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}