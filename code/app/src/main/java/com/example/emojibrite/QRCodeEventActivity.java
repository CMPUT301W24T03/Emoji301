package com.example.emojibrite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Activity for displaying and sharing a QR code associated with event Display.
 */

public class QRCodeEventActivity extends AppCompatActivity {

    float x1,x2, y1,y2; // Coordinates for touch events to handle swipe gestures

    FloatingActionButton backEventQRCode;

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
        setContentView(R.layout.event_qr_code_one);
        backEventQRCode = findViewById(R.id.floatingActionButton_back_checkin_image);


        ImageView qrCode = findViewById(R.id.event_qr_pic);
        Button qrCodeShare = findViewById(R.id.share_button_event_in1);

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
        // Listener for the back navigation button
        backEventQRCode.setOnClickListener(v -> finish());

    }

    /**
     * Detects touch events to enable swipe gestures for navigation.
     * @param touchEvent The motion event triggering the touch.
     * @return Boolean indicating whether the touch was handled.
     */

    public boolean onTouchEvent(MotionEvent touchEvent)
    {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

            if (x1 > x2) {
                Intent i = new Intent(QRCodeEventActivity.this, QRCodeCheckActivity.class);  //swipe right to left
                startActivity(i);
            }
            break;
        }
        return super.onTouchEvent(touchEvent);
    }
}



