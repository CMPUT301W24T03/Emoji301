/*
package com.example.emojibrite;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdminImageAdapter extends ArrayAdapter<Users> {


    private Context context;
    private List<Users> userList;



/**
     * Constructor for the EventAdapter.
     * @param context The current context.
     * @param userList An ArrayList of Event objects to be displayed.
     */
/*
    public AdminImageAdapter(@NonNull Context context, @NonNull ArrayList<Image> userList) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
    }


/**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
/*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView  = LayoutInflater.from(context).inflate(R.layout.activity_profile_adapter, parent, false);
        }

        Users currentUser = userList.get(position);

        ImageView userImage = convertView.findViewById(R.id.user_image);
        TextView nameTextView = convertView.findViewById(R.id.user_name);
        TextView emailTextView = convertView.findViewById(R.id.user_email);
        TextView numberTextView = convertView.findViewById(R.id.user_phone_number);
        TextView homepageTextView = convertView.findViewById(R.id.user_home_page);


        Glide.with(getContext()).load(currentUser.getImagePath()).into(userImage);
        nameTextView.setText(currentUser.getName());
        emailTextView.setText(currentUser.getEmail());
        numberTextView.setText(currentUser.getNumber());
        homepageTextView.setText(currentUser.getHomePage());


        return convertView;
    }
}
*/

