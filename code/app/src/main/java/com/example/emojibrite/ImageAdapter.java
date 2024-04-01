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

public class ImageAdapter extends ArrayAdapter<Image> {

    public ImageAdapter(Context context, ArrayList<Image> images) {
        super(context, 0, images);
    }


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
