package com.example.emojibrite;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * AddEventFragment is a DialogFragment used to create a new event or edit an existing one.
 * It provides fields for entering event details like title, description, date, time, and more.
 */
public class AddEventFragment extends DialogFragment{

    /**
     * Interface for listener to handle event addition.
     */
    interface AddEventListener {
        void onEventAdded(Event event);
    }

    private AddEventListener listener;

    EventHome qrcodeEvent, qrcodeCheckIn;

    private EditText editTitle, editDescription, editMilestone, editLocation, editCapacity;
//    private Switch switchCheckInQR, switchEventPageQR;


    private ImageView imageEventPoster;
    private Button buttonSelectPic, switchCheckInQR, switchEventPageQR;
    private Uri selectedImageUri; // Image Uri for the event poster
    private Users user;

    private Uri qrCodeCheckinURI, qrCodeEventURI;


    private String eventId, checkInID;

    ImageUploader imageUploader = new ImageUploader("eventPoster");
    ImageUploader imageUploaderQR = new ImageUploader("QRCode");

    private boolean isCheckInQRGenerated = false;
    private boolean isEventQRGenerated = false;



    private static final int PICK_FROM_GALLERY = 1; // Constant for gallery pick request






    /**
     * Factory method to create a new instance of this fragment.
     * @param eventToEdit The event to edit, if applicable.
     * @return A new instance of fragment AddEventFragment.
     */
    public static AddEventFragment newInstance(Event eventToEdit) {
        return new AddEventFragment();
    }

    /**
     * Called when a fragment is first attached to its context.
     * @param context The context attaching the fragment.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddEventListener) {
            listener = (AddEventListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddEventListener");
        }

    }

    // ActivityResultLauncher for handling image selection result
    /**
     * This is responsible for dealing with launching and retreiving a picture
     */

    private final ActivityResultLauncher<String> mediaGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {

                if (uri!=null){
                    imageUploader.uploadImage(uri, new ImageUploader.UploadCallback() {
                        @Override
                        public void onUploadSuccess(Uri downloadUri) {
                            imageEventPoster.setImageURI(null);
                            imageEventPoster.setImageURI(uri); // TO DISPLAY THE IMAGE INTO THE IMAGEVIEW
                            selectedImageUri = downloadUri; // CHANGING THE SELECTED IMAGE URI TO THE DOWNLOADED URI RETREIVED AND THIS IS YOUR ANSWER
                            Log.d(TAG,"DOWNLOADED URI STRING" + selectedImageUri.toString());
                        }

                        @Override
                        public void onUploadFailure(Exception exception) {
                            Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
    );

    private final ActivityResultLauncher<Intent> startForResultCheckIn = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String qrCodeUriString = data.getStringExtra("QR_CODE_URI");
                        qrCodeCheckinURI = Uri.parse(qrCodeUriString);


                        //DOING THE CONVERSIONS:
                        imageUploaderQR.uploadImage(qrCodeCheckinURI, new ImageUploader.UploadCallback() {
                            @Override
                            public void onUploadSuccess(Uri downloadUri) {
                                qrCodeCheckinURI = downloadUri;
                            }

                            @Override
                            public void onUploadFailure(Exception exception) {
                                Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        checkInID = data.getStringExtra("Check_In_ID");
                        isCheckInQRGenerated=true;

                    }
                }
            }
    );


    private final ActivityResultLauncher<Intent> startForResultEventDetails = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String qrCodeUriString = data.getStringExtra("QR_CODE_URI");
                        qrCodeEventURI = Uri.parse(qrCodeUriString);
                        eventId = data.getStringExtra("EventId");

                        imageUploaderQR.uploadImage(qrCodeEventURI, new ImageUploader.UploadCallback() {
                            @Override
                            public void onUploadSuccess(Uri downloadUri) {
                                qrCodeEventURI = downloadUri;
                            }

                            @Override
                            public void onUploadFailure(Exception exception) {
                                Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        isEventQRGenerated=true;

                    }
                }
            }
    );


    /**
     * Static factory method to create a new instance of AddEventFragment.
     * This method encapsulates the creation of the fragment and the setting of its arguments,
     * particularly the {@link Users} object to be passed to the fragment.
     *
     * @param user The {@link Users} object to be passed to the fragment. It should contain user-specific data
     *             needed for event creation.
     * @return A new instance of AddEventFragment with the given {@link Users} object included in its arguments.
     */
    public static AddEventFragment newInstance(Users user) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Check if the fragment has any arguments passed to it
        if (getArguments() != null) {
            // Retrieve the Users object from the fragment's arguments.
            // It's used for operations within the fragment, like populating user-specific information.
            user = getArguments().getParcelable("user");
        }
    }




    /**
     * Called to have the fragment instantiate its user interface view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_event_creation, null);

        // Initialize fields
        editTitle = view.findViewById(R.id.edittext_event_title);
        editDescription = view.findViewById(R.id.edittext_event_description);
        editMilestone = view.findViewById(R.id.edittext_event_milestone);
        editLocation = view.findViewById(R.id.edittext_event_location);
        editCapacity = view.findViewById(R.id.edittext_event_capacity); // Correctly initialize editCapacity
        switchCheckInQR = view.findViewById(R.id.button_generate_checkin_qr);
        switchEventPageQR = view.findViewById(R.id.button_generate_eventpage_qr);

        imageEventPoster = view.findViewById(R.id.image_event_poster);
        buttonSelectPic = view.findViewById(R.id.button_select_picture);

//        buttonSelectPic.setOnClickListener(v -> openGallery());

        buttonSelectPic = view.findViewById(R.id.button_select_picture);

        EditText editEventDate = view.findViewById(R.id.edittext_event_date);
        editEventDate.setOnClickListener(v -> showDatePicker());

        EditText editEventTime = view.findViewById(R.id.edittext_event_time);
        editEventTime.setOnClickListener(v -> showTimePicker());


        qrcodeEvent = (EventHome)getActivity();
        switchEventPageQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QRCodeEventActivity.class);
                startForResultEventDetails.launch(intent);
            }
        });

        qrcodeCheckIn = (EventHome)getActivity();
        switchCheckInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QRCodeCheckActivity.class);
                startForResultCheckIn.launch(intent);
            }
        });





        Button buttonNext = view.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(v -> {

            if (!isCheckInQRGenerated || !isEventQRGenerated) {
                // Prompt user to generate QR codes first
                Toast.makeText(getContext(), "Please generate both QR codes before proceeding.", Toast.LENGTH_LONG).show();
            }
            else {
                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();
                Integer milestone = editMilestone.getText().toString().isEmpty() ? null : Integer.parseInt(editMilestone.getText().toString());
                String location = editLocation.getText().toString();
                Integer capacity = editCapacity.getText().toString().isEmpty() ? null : Integer.parseInt(editCapacity.getText().toString());
//            Boolean checkInQR = switchCheckInQR.isChecked();
//            Boolean eventPageQR = switchEventPageQR.isChecked();

                String dateString = editEventDate.getText().toString();
                String timeString = editEventTime.getText().toString();
                Date eventDate = null;
                try {
                    eventDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();

                }

                String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : null;
                String checkInUriString = qrCodeCheckinURI != null ? qrCodeCheckinURI.toString() : null;
                String eventUriString = qrCodeEventURI != null ? qrCodeEventURI.toString() : null;

                Log.d(TAG, "EVENT DERIVED QR CODE ID: " + eventId);
                Log.d(TAG, "CHECK IN QR CODE ID: " + checkInID);

//            Event newEvent = new Event(selectedImageUri, title, eventDate, timeString, description, milestone, location, capacity, user); //ADDING USER WHICH WE GET AS AN ARGUMENT
                Event newEvent = new Event(eventId, imageUriString, title, eventDate, timeString, description, milestone, location, checkInUriString, eventUriString, capacity, user.getProfileUid(), checkInID); //ADDING USER WHICH WE GET AS AN ARGUMENT
                listener.onEventAdded(newEvent);

            /*
            creating meta data aka adding information to storage of the image so that I can use it for admin stuff later
            essentially adding new fields for me to trace back to
             */
                if (selectedImageUri != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference imageRef = storage.getReferenceFromUrl(imageUriString);

                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setCustomMetadata("event_id", newEvent.getId())
                            .setCustomMetadata("user_id", null)
                            .build();

                    imageRef.updateMetadata(metadata);
                }
            }

            dismiss();
        });

        buttonSelectPic.setOnClickListener(v -> openGallery());


        return createDialog(view);
    }


    /**
     * This is in charge of creating the dialog fragment to add our stuff
     * @param view
     * @return it creates the build produced
     */
    private Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Add Event")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        return builder.create();
    }


    /**
     * Called to launch the gallery instance
     */
//    @AfterPermissionGranted(PICK_FROM_GALLERY)
    private void openGallery() {


    mediaGetContent.launch("image/*"); // "image/*" indicates that only image types are selectable
}



    /**
     * Called when the date is picked and needs to be formatted in a custom way while also showing a fragment
     * of the date to pick a date as a selection
     */
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year1;
                    EditText editEventDate = getDialog().findViewById(R.id.edittext_event_date);
                    editEventDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Called to pick a time by bringing up a clock fragment to select the users' preferred time
     */
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                    EditText editEventTime = getDialog().findViewById(R.id.edittext_event_time);
                    editEventTime.setText(selectedTime);
                }, hour, minute, true);

        timePickerDialog.show();
    }

}