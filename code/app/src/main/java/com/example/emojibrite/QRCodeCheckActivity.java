package com.example.emojibrite;

import android.app.Activity;
import android.content.Intent;
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
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Activity for displaying and sharing a QR code associated with event check-in.
 */

public class QRCodeCheckActivity extends AppCompatActivity {

    // Coordinates for touch events to handle swipe gestures

    float x1,x2, y1,y2;

    private static final int CHECK_ACTIVITY_REQUEST = 100;

    FloatingActionButton backCheckInQRCode;

    Uri selectedImageUri;

    ImageView qrCode;

    private Button generateQRButton, uploadButton;

    /**
     * Sets up the activity's UI and button click listeners.
     * @param savedInstanceState If the activity is being re-initialized after being
     *                           previously shut down, this Bundle contains the data it most
     *                           recently supplied. Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_qr_one);
        backCheckInQRCode = findViewById(R.id.floatingActionButton_back_checkin_image);

        // Set up ImageView and Button for sharing the QR code
        qrCode = findViewById(R.id.event_qr_pic_check_in);
        Button qrCodeShare = findViewById(R.id.share_button_check_in1);

        uploadButton = findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(v -> openGallery());



        generateQRButton = findViewById(R.id.generate_button);

        // Set click listener for the share button to enable QR code sharing
        qrCodeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract the bitmap from the ImageView
                BitmapDrawable bitmapDrawable = (BitmapDrawable)qrCode.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                // Use the QRGeneratorSharing class to share the QR code
                new QRGeneratorSharing(QRCodeCheckActivity.this, false).shareQRImage(bitmap);
            }
        });

        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQR();
            }
        });

        // Listener for the back navigation button
        backCheckInQRCode.setOnClickListener(v -> {returnResult();});

    }

    private Uri saveImage(Bitmap bitmap, String fileName) throws IOException {
        // Get the cache directory
        File cachePath = new File(getCacheDir(), "images");
        cachePath.mkdirs();

        // Create the file in the cache directory
        File imageFile = new File(cachePath, fileName);
        FileOutputStream stream = new FileOutputStream(imageFile); // Overwrites this image every time
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.close();

        // Get the URI of the file
        return FileProvider.getUriForFile(this, "com.example.emojibrite", imageFile);
    }

    /**
     * Function that generates a check in QR code from a 12 digit ID.
     */
    public void generateQR(){
        // generating a 12 digit code between 100000000000 and 999999999999
        long a = 100000000000L;
        long b = 999999999999L;
        long QRid = (long) Math.floor(Math.random() * b) + a;

        // generating the qr code now
        MultiFormatWriter writer = new MultiFormatWriter();
        // need a try catch in case
        try {
            BitMatrix bitMatrix = writer.encode(Long.toString(QRid), BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);

            selectedImageUri = saveImage(bitmap, "qr_code_" + QRid + ".png");

        } catch (WriterException | IOException e) {
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(QRCodeCheckActivity.this.getContentResolver(), uri);
                        // Set the bitmap to the ImageView for display
                        qrCode.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(QRCodeCheckActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
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

    private void returnResult() {
        Intent resultIntent = new Intent();
        // Assume 'selectedImageUri' is the URI of your generated or selected QR code
        resultIntent.putExtra("QR_CODE_URI", selectedImageUri.toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }





}
