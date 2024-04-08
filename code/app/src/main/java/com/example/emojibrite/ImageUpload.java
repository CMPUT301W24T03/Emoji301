package com.example.emojibrite;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
/**
 * ImageUpload class is responsible for handling the image upload functionality
 */
public class ImageUpload {
    //most likely useless
    private Bitmap selectedImageBitmap = null;

    public Bitmap getSelectedImageBitmap() {

        return selectedImageBitmap;
    }
    public String bitmapToEncodedString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public Bitmap encodedStringToBitmap(String encodedString) {
        byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}



/*
private final Context context;
    private Bitmap selectedImageBitmap = null;
    private final ActivityResultLauncher<String> mGetContent;

    public ImageUpload(Context context, ActivityResultRegistryOwner registryOwner) {
        this.context = context;
        this.mGetContent = registryOwner.getActivityResultRegistry()
                .register("key", new ActivityResultContracts.GetContent(), this::handleImageSelectionResult);

    }
    public void openGallery() {

        Log.d(TAG,"openGallery");
        mGetContent.launch("image/*");
    }

    private void handleImageSelectionResult(Uri uri) {
        if (uri != null) {
            Log.d(TAG,"url is not null");
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            } catch (IOException e) {
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.d(TAG,"url is null");
        }
    }
 */