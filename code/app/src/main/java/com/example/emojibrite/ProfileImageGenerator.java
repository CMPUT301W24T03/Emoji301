package com.example.emojibrite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileImageGenerator {
    OkHttpClient client = new OkHttpClient();
    String name;

    String Uid;
    private Context context;
    public interface OnCompleteListener <T> {
        void onComplete(T result);
    }

    public ProfileImageGenerator(Context context, String Uid, String name) {
        this.name = name;
        this.context = context;
        this.Uid = Uid;
    }
    public void  setProfileImageName(String name) {
        this.name = name;
    }

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

                    // Store the URI in the database
                    Database database = new Database(context);
                    database.storeImageUri(Uid, imageUri.toString(), "autoGenImage");

                    // Notify the onCompleteListener after the database operation is done
                    onCompleteListener.onComplete(imageUri);

                    Log.d("ProfileImageGenerator", "URI is sent to database");

                    /*
                    Map<String, Object> data = new HashMap<>();
                    data.put("autoGenImage", encodedImage);
                    db.collection("Users").document(Uid).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Firestore", "DocumentSnapshot successfully written!");
                                    onCompleteListener.onComplete(null);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Firestore", "Error writing document", e);
                                    onCompleteListener.onComplete(null);
                                }
                            });

                     */
                }
            }
        });
    }
}




