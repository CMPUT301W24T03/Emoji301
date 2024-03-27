package com.example.emojibrite;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ImageUploader {

    private final FirebaseStorage storage;
    private final String storagePath;

    /**
     * Constructor to get the Image class started
     * @param storagePath could be anything. I went with "images" you can do anything
     */
    public ImageUploader(String storagePath){
        this.storage = FirebaseStorage.getInstance();
        this.storagePath = storagePath;
    }

    /**
     * Interface for success and no success
     */
    public interface UploadCallback{
        void onUploadSuccess(Uri downloadUri); //interface for when the stuff works
        void onUploadFailure(Exception exception); // error handler
    }

    /**
     * Uploading the image
     * @param imageUri the image which is passed to upload into firebase
     * @param callback
     */
    public void uploadImage(Uri imageUri, UploadCallback callback){
        StorageReference storageRef = storage.getReference(); //refers the storage
        StorageReference imageRef = storageRef.child(storagePath+"/"+ UUID.randomUUID().toString()+".jpg"); //uses the storage reference to store the images

        imageRef.putFile(imageUri).continueWithTask(task -> {
            if (!task.isSuccessful()){
                throw task.getException();
            }
            return imageRef.getDownloadUrl(); //gets the downloaded url which is uploaded
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Uri downloadedUri = task.getResult(); // gets the result of the new uri
                callback.onUploadSuccess(downloadedUri);  //gives the result as a variable called downloadedUri
            }
            else {
                callback.onUploadFailure(task.getException());
            }
        });

    }
}
