package com.example.emojibrite;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.IOException;

public class ImageUpload {
    private final Context context;
    private Bitmap selectedImageBitmap = null;
    private final ActivityResultLauncher<String> mGetContent;

    public ImageUpload(Context context, ActivityResultRegistryOwner registryOwner) {
        this.context = context;
        this.mGetContent = registryOwner.getActivityResultRegistry()
                .register("key", new ActivityResultContracts.GetContent(), this::handleImageSelectionResult);

    }
    public void openGallery() {
        mGetContent.launch("image/*");
    }

    private void handleImageSelectionResult(Uri uri) {
        if (uri != null) {
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap getSelectedImageBitmap() {

        return selectedImageBitmap;
    }
}