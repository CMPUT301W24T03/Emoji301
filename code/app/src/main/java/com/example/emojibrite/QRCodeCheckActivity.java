
package com.example.emojibrite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
 * Activity for displaying and sharing a QR code associated with event check-in.
 */

public class QRCodeCheckActivity extends AppCompatActivity {

    // Coordinates for touch events to handle swipe gestures

    float x1,x2, y1,y2;

    private static final int CHECK_ACTIVITY_REQUEST = 100;

    FloatingActionButton backCheckInQRCode;

    Uri selectedImageUri;

    ImageView qrCode;

    String checkInID;

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

        checkInID=generateRandomId();



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
                generateQR(checkInID);
            }
        });

        // Listener for the back navigation button
        backCheckInQRCode.setOnClickListener(v -> {returnResult();});

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



    private String generateRandomId(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i<13;i++){
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Function that generates a check in QR code from a 12 digit ID.
     * @param checkInID The 12 digit ID used to generate the QR code.
     */
    public void generateQR(String checkInID){

        // generating the qr code now
        MultiFormatWriter writer = new MultiFormatWriter();
        // need a try catch in case
        try {
            BitMatrix bitMatrix = writer.encode(checkInID, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);

            // disabling these buttons as they are no longer needed
            generateQRButton.setVisibility(View.INVISIBLE);
            uploadButton.setVisibility(View.INVISIBLE);

            selectedImageUri = saveImage(bitmap, "qr_code_" + checkInID + ".png");

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
                        // Load the bitmap from the gallery
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                        // Optionally, decode the QR code if you need to retrieve information from it
                        String decodedCheckInId = decodeQRCode(bitmap);
                        if(decodedCheckInId != null) {
                            checkInID = decodedCheckInId;
                            qrCode.setImageBitmap(bitmap);
                            Log.d("Upload",decodedCheckInId);
                            // If decoded successfully, handle the event ID
                        }
                        else {
                            Toast.makeText(this, "Invalid QR code. Add another one", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private String decodeQRCode(Bitmap bitmap) {
        try {
            int[] intArray = new int[bitmap.getWidth()*bitmap.getHeight()];
            // Copy pixel data from the Bitmap into the 'intArray' array
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


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
        resultIntent.putExtra("Check_In_ID",checkInID);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }





}
