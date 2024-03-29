package com.example.emojibrite;

import static android.app.PendingIntent.getActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

/**
 * Activity for displaying and sharing a QR code associated with event Display.
 */

public class QRCodeEventActivity extends AppCompatActivity {



    FloatingActionButton backEventQRCode;

    Button generate_event, upload_event;

    Uri selectedImageUri;
//upload_button_event_in1
    ImageView qrCode;

    private static final int CHECK_ACTIVITY_REQUEST = 100;






    /**
     * Sets up the activity's UI and button click listeners.
     * @param savedInstanceState If the activity is being re-initialized after being
     *                           previously shut down, this Bundle contains the data it most
     *                           recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_qr_code_one);
        backEventQRCode = findViewById(R.id.floatingActionButton_back_checkin_image);


        qrCode = findViewById(R.id.event_qr_pic);
        Button qrCodeShare = findViewById(R.id.share_button_event_in1);

        generate_event = findViewById(R.id.generate_button_event1);

        upload_event = findViewById(R.id.upload_button_event_in1);

        // Set click listener for the share button to enable QR code sharing
        qrCodeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable)qrCode.getDrawable(); // Extract the bitmap from the ImageView
                Bitmap bitmap = bitmapDrawable.getBitmap();
                // Use the QRGeneratorSharing class to share the QR code
                new QRGeneratorSharing(QRCodeEventActivity.this, true).shareQRImage(bitmap);
            }
        });

        generate_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQR(1222222222);
            }
        });

        upload_event.setOnClickListener(v -> openGallery());
        // Listener for the back navigation button
        backEventQRCode.setOnClickListener(v -> finish());

    }

    /**
     * Generates an Event QR code from the event ID.
     * @param eventID
     * A 12 digit ID passed from the event class.
     */
    public void generateQR(long eventID){

        // generating the qr code now
        MultiFormatWriter writer = new MultiFormatWriter();
        // need a try catch in case
        try {
            BitMatrix bitMatrix = writer.encode(Long.toString(eventID), BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        //Log.d("QRID", Long.toString(QRid));

    }


    /**
     * This is responsible for dealing with launching and retreiving a picture
     */
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri; // Save the selected image Uri.
                    try {
                        // Use MediaStore to fetch the selected image as a Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(QRCodeEventActivity.this.getContentResolver(), uri);
                        // Set the bitmap to the ImageView for display
                        qrCode.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(QRCodeEventActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    /**
     * Called to launch the gallery instance
     */
//    @AfterPermissionGranted(PICK_FROM_GALLERY)
    private void openGallery() {

        mGetContent.launch("image/*"); // "image/*" indicates that only image types are selectable
    }



}



