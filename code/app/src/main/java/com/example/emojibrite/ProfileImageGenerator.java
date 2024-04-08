package com.example.emojibrite;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class is used to generate a profile image for a user
 * using the ui-avatars.com API. The image is then stored in
 * the Firestore database.
 */
public class ProfileImageGenerator {
    OkHttpClient client = new OkHttpClient();
    String name;

    String Uid;

    /**
     * This interface is used to create a callback for when the
     * profile image is generated and stored in the database.
     * @param <T> The type of the result
     */
    public interface OnCompleteListener <T> {
        /**
         * This method is called when the profile image is generated
         * and stored in the database.
         * @param result The result of the operation
         */
        void onComplete(T result);
    }
    /**
     * This constructor is used to create a ProfileImageGenerator
     * object with the user's name and Uid.
     * @param Uid The user's unique ID
     * @param name The user's name
     */
    public ProfileImageGenerator(String Uid, String name) {
        this.name = name;

        this.Uid = Uid;
    }

    /**
     * This method is used to set the user's name.
     * @param name The user's name
     */
    public void  setProfileImageName(String name) {
        this.name = name;
    }
    /**
     * This method is used to get the user's name.
     * @return The user's name
     */
    private void addingPlusToName() {
        String[] nameArray = name.split(" ");
        String newName = "";
        for (int i = 0; i < nameArray.length; i++) {
            newName += nameArray[i] + "+";
        }
        name = newName.substring(0, newName.length() - 1);
    }


    /*
    i call addPLustoName because the url needs the username to be in the format of "name+restOfNameAfterSpace "
    i use a request and build it using the url.
    aftewards, i use the Okhttp client to call the request and enqueue it. ofc, since it is a callback it takes time hence why there is onresponse
    after u get a response, if it works, we use an inputstream to recieve the information, then decode it into bitmap,
    since we have to sent it to the database, we have to turn it into a byte array, then encode it into a string using base64
    then we just add it into the collection ProfileImages with the document ID being the Uid.
    we put a listener to check if data insertion to the database is successful
    NOT TOO SURE ABOUT THE ONCOMPLETE PART but it is for callback purposes
     */
    /**
     * This method is used to get the user's profile image from the
     * ui-avatars.com API and store it in the Firestore database.
     * @param onCompleteListener The callback for when the profile
     *                           image is generated and stored in the
     *                           database
     */
    public void getProfileImage( final OnCompleteListener<Uri> onCompleteListener) {
        addingPlusToName();
        String url = "https://ui-avatars.com/api/?name=" + name;

        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("ProfileImageGenerator", "Request is built");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("ProfileImageGenerator", "Failed to get profile image");
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("ProfileImageGenerator", "Response is successful");

                    // Convert the response to a URI
                    Uri imageUri = Uri.parse(response.request().url().toString());
                    Log.d("ProfileImageGenerator", "URI is parsed" + imageUri.toString());

                    // Convert the URI to a string and adding / cuz it is somehow needed. idk why
                    String change = imageUri.toString() + "/";

                    // Store the URI in the database
                    //we aren't goign to store uri in database in this part.
                    /*
                    Database database = new Database();
                    database.storeImageUri(Uid, imageUri.toString(), "autoGenImage");

                     */

                    // Notify the onCompleteListener after the database operation is done
                    onCompleteListener.onComplete(Uri.parse(change));

                    Log.d("ProfileImageGenerator", "URI is sent to database");
                }
            }
        });
    }
}