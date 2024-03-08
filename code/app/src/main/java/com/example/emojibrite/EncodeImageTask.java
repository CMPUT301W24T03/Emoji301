package com.example.emojibrite;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EncodeImageTask {
    private final Executor executor = Executors.newSingleThreadExecutor(); // single background thread
    private final Handler handler = new Handler(Looper.getMainLooper()); // main thread

    public void encodeImage(Bitmap bitmap, OnEncodedListener listener) {
        executor.execute(() -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String result = Base64.encodeToString(byteArray, Base64.DEFAULT);

            handler.post(() -> {
                listener.onEncoded(result);
            });
        });
    }

    public interface OnEncodedListener {
        void onEncoded(String result);
    }
}