package com.example.emojibrite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

    TextView numberOfTotalAttendees;

    String eventId;

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
        eventId = intent.getStringExtra("eventId");
        // then do Data somedata = intent.getfunction("somekey");

        Log.d("TAG","AttendeesListActivity "+eventId);

        Bundle bundle = new Bundle();
        bundle.putString("eventID",eventId);

        openSignedUpFragment(); // BY DEFAULT, OPENS UP THE SIGNED UP FRAGMENT PART





        checkInBtn.setOnClickListener(view -> {openCheckedUpFragment();});
        signedUpBtn.setOnClickListener(view -> {openSignedUpFragment();});
        numberOfTotalAttendees = findViewById(R.id.text_of_attendees);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to the event details activity
                finish();
            }
        });

        Database database = new Database();

        database.getEventById(eventId, event -> {
            checkInBtn.setText("CheckIn:"+event.getcurrentAttendance()+"users");
            numberOfTotalAttendees.setText("Total In-Person Attendees:"+event.getcurrentAttendance());
        });




    }
    /**
     * This opens up the fragment for checked in and it deals with passing the information in a bundle
     */
    private void openCheckedUpFragment(){
        CheckedInFragment checkedInFragment = new CheckedInFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle data = new Bundle();
        data.putString("eventId",eventId);
        checkedInFragment.setArguments(data);
        fragmentTransaction.replace(R.id.attendee_view_list_fragment_container, checkedInFragment).commit();
        updateButtonStyle(checkInBtn);


    }

    /**
     * This opens up the fragment for signed up and it deals with passing the information in a bundle
     */
    private void openSignedUpFragment(){
        SignedUpFragment signedUpFragment = new SignedUpFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle data = new Bundle();
        Log.d("TAG","TEST FOR COLLECTED EVENTID "+eventId);
        data.putString("eventId",eventId); //adds the evnt Id
        signedUpFragment.setArguments(data);
        fragmentTransaction.replace(R.id.attendee_view_list_fragment_container, signedUpFragment).commit();
        updateButtonStyle(signedUpBtn); //styles the button

    }
    /**
     * This replaces the fragment with the new fragment
     * @param fragment the fragment to be replaced
     * @param bundle the bundle to be passed
     */
    private void replaceFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.attendee_view_list_fragment_container, fragment)
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