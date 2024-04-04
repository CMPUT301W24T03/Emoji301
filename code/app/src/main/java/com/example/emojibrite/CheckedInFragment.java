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
import java.util.Objects;

public class CheckedInFragment extends Fragment {
    // put your uninitialized attributes here
    ListView attendeesListView;
    AttendeesArrayAdapter attendeesArrayAdapter;

    ArrayList<Integer> countOfAttendees;

    ArrayList<Users> attendeesList;

    private String eventId;
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
        View checkedInLayout = inflater.inflate(R.layout.fragment_checked_in, container, false);
        Log.d("Aivan", "CheckInFragmentonCreateView: called");

        // init your attributes here
        attendeesListView = checkedInLayout.findViewById(R.id.attendee_view_list);
        // if the data is passed via a bundle, do the following
        // Bundle bundle = getArguments();

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString("eventId");
            Log.d("SignedUpFragment", "Received Event ID: " + eventId);
        } else {
            Log.d("SignedUpFragment", "No bundle passed");
        }

            attendeesList = new ArrayList<>();
            countOfAttendees = new ArrayList<>(); // Initialize countOfAttendees
            attendeesArrayAdapter = new AttendeesArrayAdapter(getContext(), attendeesList, "1", countOfAttendees);
            attendeesListView.setAdapter(attendeesArrayAdapter);
            loadAttendees(eventId);

            return checkedInLayout;
        }

    /**
     *
     * @param eventId
     */

    private void loadAttendees(String eventId){
            Database db = new Database();
            attendeesList.clear();
            countOfAttendees.clear();
            db.getEventById(eventId, new Database.EventCallBack() {
                @Override
                public void onEventFetched(Event event) {
                    ArrayList<String> listAttendees = event.getAttendeesList();

                    // Reset countOfAttendees before populating
                    countOfAttendees.clear();

                    for (String userId : listAttendees) {
                        db.getUserDocument(userId, new Database.OnUserDocumentRetrievedListener() {
                            @Override
                            public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Users user = documentSnapshot.toObject(Users.class);
                                    if (user != null) {
                                        if (!attendeesList.contains(user)) {
                                            attendeesList.add(user);
                                            Log.d("ADDING USERS","checking how many times it adds");
                                            Integer countOfUsers = countOfElements(listAttendees, userId);
                                            countOfAttendees.add(countOfUsers); // Populate count list
                                        }
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


    /**
     *
     * @param listAttendees
     * @param userId
     * @return
     */
    public Integer countOfElements(ArrayList<String> listAttendees, String userId) {
        Integer counter = 0;
        for (String user : listAttendees) {
            if (Objects.equals(user, userId)) {
                counter++;
            }
        }
        return counter;
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
        super.onViewCreated(view, savedInstanceState);

        // button logics
    }
}