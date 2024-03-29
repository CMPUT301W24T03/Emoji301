package com.example.emojibrite;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AttendeesArrayAdapter extends ArrayAdapter<Users> {
    public AttendeesArrayAdapter(Context context, ArrayList<Users> users) {super(context,0,users);}


    /**
     * Sets up the view
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Users users = getItem(position);

        if (convertView  == null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.profile_attendees,parent,false);

        }

        ImageView userProfilePic = convertView.findViewById(R.id.image_event);
        TextView userName = convertView.findViewById(R.id.attendee_name);
        TextView userNumberCheckedIn = convertView.findViewById(R.id.num_checked_in);

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

        userName.setText(users.getName());

        userNumberCheckedIn.setText("Heloooooo");

        return convertView;


    }
}
