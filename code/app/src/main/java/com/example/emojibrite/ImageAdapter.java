package com.example.emojibrite;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
/**
 * Adapter for the images
 */
public class ImageAdapter extends ArrayAdapter<Image> {
    /**
     * Constructor for the image adapter
     * @param context The context
     * @param images The list of images
     */
    public ImageAdapter(Context context, ArrayList<Image> images) {
        super(context, 0, images);
    }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Image image = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.admin_image_adapter, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getContext()).load(image.getImageURL()).into(imageView);
            }
        });


        return convertView;
    }
}
