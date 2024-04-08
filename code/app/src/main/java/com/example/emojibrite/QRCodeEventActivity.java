
package com.example.emojibrite;

import static android.app.PendingIntent.getActivity;

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
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Activity for displaying and sharing a QR code associated with event Display.
 */

public class QRCodeEventActivity extends AppCompatActivity {



    FloatingActionButton backEventQRCode;

    Button generate_event;

    Uri selectedImageUri;
    //upload_button_event_in1
    ImageView qrCode;

    String eventId;

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

        eventId = generateRandomId();

        generate_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQR(eventId);
            }
        });
        // Listener for the back navigation button
        backEventQRCode.setOnClickListener(v -> {returnResult();});

    }

    /**
     * It generates a random 12 digit string for the event id
     * @return generated event id
     */
    private String generateRandomId(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i<12;i++){
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Generates an Event QR code from the event ID.
     * @param eventID
     * A 12 digit ID passed from the event class.
     */
    public void generateQR(String eventID){

        // generating the qr code now
        MultiFormatWriter writer = new MultiFormatWriter();
        // need a try catch in case
        try {

            BitMatrix bitMatrix = writer.encode(eventID, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);

            // disabling this button as it's no longer needed
            generate_event.setVisibility(View.INVISIBLE);

            selectedImageUri = saveImage(bitmap, "qr_code_" + eventID + ".png");

        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }

        //Log.d("QRID", Long.toString(QRid));

    }

    /**
     * Takes care of returning the result when ending the activity as addEventFragment retreives the eventId and the qr code
     */

    private void returnResult() {
        Intent resultIntent = new Intent();
        // Assume 'selectedImageUri' is the URI of your generated or selected QR code
        resultIntent.putExtra("QR_CODE_URI", selectedImageUri.toString());
        resultIntent.putExtra("EventId", eventId);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }



    /**
     * Saves a bitmap image to the internal cache directory and returns its URI.
     * The method creates a file in the cache directory with the specified file name, writes the bitmap to this file,
     * and then returns the URI for the file using a FileProvider.
     *
     * @param bitmap The bitmap image to be saved. This is typically the QR code bitmap generated in the activity.
     * @param fileName The name of the file in which the bitmap will be saved. Should be unique to avoid overwriting existing files.
     * @return The Uri of the saved bitmap image. This URI can be used to share or process the image further.
     * @throws IOException if an error occurs during file writing.
     *
     * Note: The method assumes that a FileProvider is defined in the AndroidManifest.xml with an authority of
     * "com.example.emojibrite" and a path pointing to the cache directory.
     */
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
}
