package com.example.emojibrite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * This activity is used for displaying a QR code associated with an event and allowing the user
 * to download or share the QR code.
 */

public class DisplayEventQRCode extends AppCompatActivity {

    String eventId;
    Database database;

    /**
     * Called when the activity is starting. This method initializes the activity, sets the content view, and retrieves the event ID from the intent.
     * It also sets up the UI components and their respective listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_qr_code_one);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        Button generateButton = findViewById(R.id.generate_button_event1);
        ImageView qrcodeImage = findViewById(R.id.event_qr_pic);
        FloatingActionButton backButton = findViewById(R.id.floatingActionButton_back_checkin_image);
        Button shareButton = findViewById(R.id.share_button_event_in1);
        // Hide the generate button as it's not needed for displaying
        generateButton.setText("Download");

        // Assuming 'database' is initialized properly elsewhere
        database = new Database();

        database.getEventById(eventId, event -> {
            if (event != null && event.getEventQRCode() != null) {
                // Run the image loading on the main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    // Load the QR code image into the ImageView
                    Glide.with(DisplayEventQRCode.this)
                            .load(event.getEventQRCode().toString())
                            .into(qrcodeImage);
                });
            } else {
                // Handle the case where event is null or doesn't have a QR code
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        generateButton.setOnClickListener(v -> downloadImage(qrcodeImage));

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable)qrcodeImage.getDrawable(); // Extract the bitmap from the ImageView
                Bitmap bitmap = bitmapDrawable.getBitmap();
                // Use the QRGeneratorSharing class to share the QR code
                new QRGeneratorSharing(DisplayEventQRCode.this, true).shareQRImage(bitmap);
            }
        });

    }

    /**
     * Initiates the download of the image displayed in an ImageView to the device's gallery.
     *
     * @param imageView The ImageView containing the image to be downloaded.
     */

    private void downloadImage(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        OutputStream fos;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "QR_Code_" + eventId + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
            } else {
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File image = new File(imagesDir, "QR_Code_" + eventId + ".jpg");
                fos = new FileOutputStream(image);
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(DisplayEventQRCode.this, "Image Downloaded", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(DisplayEventQRCode.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}


