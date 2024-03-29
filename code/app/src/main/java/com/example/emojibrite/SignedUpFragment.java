package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View for the fragment's UI, or null.
     */
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
        attendeesArrayAdapter = new AttendeesArrayAdapter(getContext(), attendeesList);
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