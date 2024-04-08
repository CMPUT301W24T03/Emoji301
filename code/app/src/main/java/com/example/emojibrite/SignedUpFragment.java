package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment for the signed up participants of an event.
 */
public class SignedUpFragment extends Fragment {
    // put your uninitialized attributes here
    ListView attendeesListView;
    AttendeesArrayAdapter attendeesArrayAdapter;

    ArrayList<Users> attendeesList;

    private String eventId;




    // TODO: Create a custom adapter for the list view and use it here. ASK Snehal
    // custom adapter along with contentxml. You can reuse the same adapter for
    // both signedUp fragment and CheckedInFragment.
    // Copy Snehal's book implementation for the adapter and contentxml


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View signedUpLayout = inflater.inflate(R.layout.fragment_signed_up, container, false);
        Log.d("Aivan", "SignedUpFragmnetonCreateView: called");


        // init your attributes here
        attendeesListView = signedUpLayout.findViewById(R.id.attendee_view_list);

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString("eventId");
            Log.d("SignedUpFragment", "Received Event ID: " + eventId);
        } else {
            Log.d("SignedUpFragment", "No bundle passed");
        }





        attendeesList = new ArrayList<>();
        //i am sending 1 here cuz it doesn't matter either way BECAUSE whoever is on this, shouldn't be able to see delete button and stuff
        attendeesArrayAdapter = new AttendeesArrayAdapter(getContext(), attendeesList,"1", null);
        attendeesListView.setAdapter(attendeesArrayAdapter);
        loadAttendees(eventId);






        return signedUpLayout;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view. This gives
     * subclasses a chance to initialize themselves once they know their view hierarchy has been completely created.
     * The fragment's view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        // button logics
    }

    /**
     * It deals with retreiving all the users and setting it up by also passing it through the array adapted
     * @param eventId It retreives all the signed up participants from the events.
     *
     */

    private void loadAttendees(String eventId) {
        Database db = new Database();
        db.getSignedAttendees(eventId, new Database.OnSignedAttendeesRetrievedListener() {
            @Override
            public void onSignedAttendeesRetrieved(List<String> attendeeIds) {
                attendeesList.clear(); // Clear existing items
                for (String userId : attendeeIds) {
                    db.getUserDocument(userId, new Database.OnUserDocumentRetrievedListener() {
                        @Override
                        public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Users user = documentSnapshot.toObject(Users.class);
                                if (user != null) {
                                    attendeesList.add(user);
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(() -> attendeesArrayAdapter.notifyDataSetChanged());
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

}