package com.example.emojibrite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * AttendeesListActivity is responsible for displaying the list of attendees
 * who have signed up for an event as well as those who checked in.
 */
public class AttendeesListActivity extends AppCompatActivity {
    // 2 buttons
    Button checkInBtn;
    Button signedUpBtn;
    FloatingActionButton backBtn;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_list);
        // init buttons by linking them to the view
        checkInBtn = findViewById(R.id.check_in_button);
        signedUpBtn = findViewById(R.id.signedup_button);
        backBtn = findViewById(R.id.back_event_details_button);

        // todo: get the data passed from EventDetailsActivity
        Intent intent = getIntent();
        // then do Data somedata = intent.getfunction("somekey");

        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragment to display the list of attendees who have checked in
                // to pass data, we cna just add a parameter to the replaceFragment function
                replaceFragment(new CheckedInFragment());

                // update the button style
                updateButtonStyle(checkInBtn);
            }
        });
        signedUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragment to display the list of attendees who have signed up
                replaceFragment(new SignedUpFragment());
                // update the button style
                updateButtonStyle(signedUpBtn);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to the event details activity
                finish();
            }
        });
    }

    private void replaceFragment(Fragment activeFragment) {
        // replace the current fragment with the activeFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.attendee_view_list_fragment_container, activeFragment)
                .commit();
    }

    /**
     * changes the look of the active button
     * @param activeButton the view of the currently active button
     */
    private void updateButtonStyle(Button activeButton) {
        if (activeButton == signedUpBtn) {
            // set the background of signedUpBtn to @drawable/event_button_solid
            signedUpBtn.setBackground(getDrawable(R.drawable.event_button_solid));
            // set text color to #FFFFFF
            signedUpBtn.setTextColor(Color.parseColor("#FFFFFF"));
            // set the background of checkInBtn to @drawable/event_button_outlined
            checkInBtn.setBackground(getDrawable(R.drawable.event_button_outlined));
            // set text color to #000000
            checkInBtn.setTextColor(Color.parseColor("#000000"));
        } else {
            // set the background of checkInBtn to @drawable/event_button_solid
            checkInBtn.setBackground(getDrawable(R.drawable.event_button_solid));
            // set text color to #FFFFFF
            checkInBtn.setTextColor(Color.parseColor("#FFFFFF"));
            // set the background of signedUpBtn to @drawable/event_button_outlined
            signedUpBtn.setBackground(getDrawable(R.drawable.event_button_outlined));
            // set text color to #000000
            signedUpBtn.setTextColor(Color.parseColor("#000000"));
        }
    }
}
