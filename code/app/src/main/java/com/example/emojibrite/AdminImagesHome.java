package com.example.emojibrite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
/**
 * Activity for the admin images view
 */
public class AdminImagesHome extends AppCompatActivity {
    /**
     * user object
     */
    public static Users user;

    ListView imageList;
    ImageAdapter imageAdapter;
    ArrayList<Image> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_list);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");

        imageList = findViewById(R.id.admin_image_list);
        dataList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, dataList);
        imageList.setAdapter(imageAdapter);

        ImageView notifbell = findViewById(R.id.notif_bell);
        ImageView profileButton = findViewById(R.id.profile_pic);
        ImageView backArrow = findViewById(R.id.back_arrow_listImg);

        notifbell.setVisibility(View.GONE);
        profileButton.setVisibility(View.GONE);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminImagesHome.this, AdminActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
                finish();
            }
        });


        fetchImages();

        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image image = dataList.get(position);
                deleteAlertBuilder(image);


            }
        });

    }
    /**
     * fetchImages method to handle the fetching of images
     */
    private void fetchImages() {
        Database database = new Database();
        database.fetchImages(null, new Database.OnImageRetrievedListener() {
            @Override
            public void onImageRetrieved(List<Image> images) {
                dataList.clear();
                dataList.addAll(images);
                imageAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * deleteAlertBuilder method to handle the alert dialog for deleting an image
     * @param image
     */
    private void deleteAlertBuilder(Image image){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the message and the title of the dialog
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this event?");
        // Set the positive (Yes) button and its click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database database = new Database();

                database.deleteImageFromStorage(image, new Database.OnImageDeletedListener() {
                    @Override
                    public void onImageDeleted() {
                        fetchImages();

                    }
                });
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
}





