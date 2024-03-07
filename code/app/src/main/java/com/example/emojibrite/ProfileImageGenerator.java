package com.example.emojibrite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileImageGenerator {
    OkHttpClient client = new OkHttpClient();
    String name;
    ImageView imageView;
    String Uid;
    public interface OnCompleteListener <T> {
        void onComplete(T result);
    }

    public ProfileImageGenerator(String name, ImageView imageView, String Uid) {
        this.name = name;
        this.imageView = imageView;
        this.Uid = Uid;
    }

    private void addingPlusToName() {
        String[] nameArray = name.split(" ");
        String newName = "";
        for (int i = 0; i < nameArray.length; i++) {
            newName += nameArray[i] + "+";
        }
        name = newName.substring(0, newName.length() - 1);
    }

    public void getProfileImage( final OnCompleteListener<Void> onCompleteListener) {
        addingPlusToName();
        String url = "https://ui-avatars.com/api/?name=" + name;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("image", encodedImage);
                    db.collection("ProfileImages").document(Uid).set(data)
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
                }
            }
        });
    }
}


