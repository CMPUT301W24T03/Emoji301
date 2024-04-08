package com.example.emojibrite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Adapter for the attendees list
 */
public class AttendeesArrayAdapter extends ArrayAdapter<Users> {
    String privilege;
    ArrayList<Integer> checkInCounts; // This will store the check-in counts

    public AttendeesArrayAdapter(Context context, ArrayList<Users> users, String privilege, ArrayList<Integer> checkInCounts) {
        super(context, 0, users);
        this.privilege = privilege;
        this.checkInCounts = checkInCounts;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Users users = getItem(position);
        Database database = new Database();

        if (convertView  == null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.profile_attendees,parent,false);
        }

        ImageView userProfilePic = convertView.findViewById(R.id.image_event);
        TextView userName = convertView.findViewById(R.id.attendee_name);
        TextView userNumberCheckedIn = convertView.findViewById(R.id.num_checked_in);
        Button deleteButton = convertView.findViewById(R.id.delete_btn);
        SwitchCompat adminAccess = convertView.findViewById(R.id.admin_access);

        if (privilege.equals("3"))
        {
            deleteButton.setVisibility(View.VISIBLE);
            adminAccess.setVisibility(View.VISIBLE);
            userNumberCheckedIn.setVisibility(View.GONE);
            userName.setText(users.getName());
        }
        else if (privilege.equals("2"))
        {
            deleteButton.setVisibility(View.VISIBLE);
            adminAccess.setVisibility(View.GONE);
            userNumberCheckedIn.setVisibility(View.GONE);
            userName.setText(users.getName());
        }
        else {
            deleteButton.setVisibility(View.GONE);
            adminAccess.setVisibility(View.GONE);

            if (checkInCounts != null && position < checkInCounts.size()) {
                String countText = checkInCounts.get(position) + " Checked In";
                userNumberCheckedIn.setText(countText);
            } else {
                userNumberCheckedIn.setText("Signed up");
            }
            userName.setText(users.getName());
        }

        //setting image to be autogen or uploaded
        if (users.getUploadedImageUri()!=null)
        {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getContext()).load(users.getUploadedImageUri()).into(userProfilePic);
                }
            });
        }
        else{
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getContext()).load(users.getAutoGenImageUri()).into(userProfilePic);
                }
            });
        }
        adminAccess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //update the user's role
                if (isChecked)
                {
                    users.setRole("2");
                    database.setUserObject(users);
                }
                else
                {
                    users.setRole("1");
                    database.setUserObject(users);
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the user
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Set the message and the title of the dialog
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this user?");

                // Set the positive (Yes) button and its click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the user
                        database.deleteUser(users.getProfileUid(), users.getUploadedImageUri());
                        remove(users);
                        notifyDataSetChanged();
                    }
                });
                // Set the negative (No) button and its click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Create and show the dialog
                builder.create().show();
            }

        });

        return convertView;
    }
}
