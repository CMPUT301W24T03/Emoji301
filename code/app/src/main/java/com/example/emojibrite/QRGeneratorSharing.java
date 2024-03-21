package com.example.emojibrite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Class responsible for generating QR code images and sharing them.
 */
public class QRGeneratorSharing {

    private final Context context;
    private boolean eventSection;

    /**
     * Constructor for QRGeneratorSharing.
     *
     * @param context      The context from which the sharing will be initiated.
     * @param eventSection A boolean flag indicating the section of the app: true for event sharing, false for check-in.
     */

    public QRGeneratorSharing(Context context, Boolean eventSection) {
        this.context = context;
        this.eventSection = eventSection; //BOOLEAN IS FOR TRUE = IF ITS FOR EVENTSHARING AND FALSE IF ITS CHECK IN
    }

    /**
     * Shares the QR code image using an intent chooser to select the sharing application.
     *
     * @param bitmap The QR code image to be shared.
     */
    public void shareQRImage(Bitmap bitmap) {
        Uri uri = getImageUriToShare(bitmap);
        if (uri != null) {
            Intent shareIntent = createShareIntent(uri);
            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        } else {
            Toast.makeText(context, "Error in sharing QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Generates a file URI for the given bitmap.
     *
     * @param bitmap The bitmap to be shared.
     * @return The file URI.
     */
    private Uri getImageUriToShare(Bitmap bitmap) {
        File folder = new File(context.getCacheDir(), "images");
        Uri uri = null;

        try {
            folder.mkdirs();
            File file = new File(folder, "shared_qr_image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(context, "com.example.emojibrite", file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * Creates an intent for sharing the QR code.
     *
     * @param imageUri The URI of the image to be shared.
     * @return The intent configured for sharing.
     */
    private Intent createShareIntent(Uri imageUri) {
        if (eventSection) { //this is a different message for events
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Thank you for using EmojiBrite. To view the event, scan the attached QRCode on our app");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Emoji Brite QRCode Event details");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            return intent;
        }
        else { //This is a different message for check in
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Thank you for using EmojiBrite. To check in, scan the attached QRCode on our app");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Emoji Brite QRCode Check In");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            return intent;

        }
    }
}