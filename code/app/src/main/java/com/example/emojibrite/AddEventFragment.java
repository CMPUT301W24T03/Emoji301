package com.example.emojibrite;
import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEventFragment extends DialogFragment implements EasyPermissions.PermissionCallbacks {
    interface AddEventListener {
        void onEventAdded(Event event);
    }

    private AddEventListener listener;
    private EditText editTitle, editDescription, editMilestone, editLocation, editCapacity;
    private Switch switchCheckInQR, switchEventPageQR;

    private ImageView imageEventPoster;
    private Button buttonSelectPic;

    private static final int PICK_FROM_GALLERY = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;

    public static AddEventFragment newInstance(Event eventToEdit) {
        return new AddEventFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddEventListener) {
            listener = (AddEventListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddEventListener");
        }
    }

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
        switchCheckInQR = view.findViewById(R.id.switch_checkin_qr);
        switchEventPageQR = view.findViewById(R.id.switch_eventpage_qr);

        imageEventPoster = view.findViewById(R.id.image_event_poster);
        buttonSelectPic = view.findViewById(R.id.button_select_picture);

        buttonSelectPic.setOnClickListener(v -> openGallery());

        buttonSelectPic = view.findViewById(R.id.button_select_picture);

        EditText editEventDate = view.findViewById(R.id.edittext_event_date);
        editEventDate.setOnClickListener(v -> showDatePicker());

        EditText editEventTime = view.findViewById(R.id.edittext_event_time);
        editEventTime.setOnClickListener(v -> showTimePicker());

        Button buttonNext = view.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(v -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            Integer milestone = editMilestone.getText().toString().isEmpty() ? null : Integer.parseInt(editMilestone.getText().toString());
            String location = editLocation.getText().toString();
            Integer capacity = editCapacity.getText().toString().isEmpty() ? null : Integer.parseInt(editCapacity.getText().toString());
            Boolean checkInQR = switchCheckInQR.isChecked();
            Boolean eventPageQR = switchEventPageQR.isChecked();

            String dateString = editEventDate.getText().toString();
            String timeString = editEventTime.getText().toString();
            Date eventDate = null;
            try {
                eventDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();

            }

            Event newEvent = new Event("", title, eventDate, timeString, description, milestone, location, checkInQR, eventPageQR, capacity);
            listener.onEventAdded(newEvent);

            dismiss();
        });

        // Set the OnClickListener for buttonSelectPic
//        buttonSelectPic.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//            } else {
//                openGallery();
//            }
//        });
        buttonSelectPic.setOnClickListener(v -> openGallery());


        return createDialog(view);
    }


    private void showPermissionExplanationDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Permission Needed")
                .setMessage("This app needs the read storage permission to select an image from your gallery.")
                .setPositiveButton("OK", (dialog, which) ->
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                .create()
                .show();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Permission Denied")
                .setMessage("Permission to access storage was denied. Please go to settings to allow permission for this app.")
                .setPositiveButton("Settings", (dialog, which) -> {
                    // Take the user to the app's settings page
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private Dialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Add Event")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        return builder.create();
    }




    @AfterPermissionGranted(PICK_FROM_GALLERY)
    private void openGallery() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.gallery_permission_rationale),
                    PICK_FROM_GALLERY, perms);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                imageEventPoster.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.image_pick_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == PICK_FROM_GALLERY) {
            openGallery();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == PICK_FROM_GALLERY) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this).build().show();
            } else {
                Toast.makeText(getActivity(), R.string.gallery_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

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
