package com.example.emojibrite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity for displaying and sharing a QR code associated with event check-in.
 */

public class QRCodeCheckActivity extends AppCompatActivity {

    // Coordinates for touch events to handle swipe gestures

    float x1,x2, y1,y2;

    private static final int CHECK_ACTIVITY_REQUEST = 100;

    FloatingActionButton backCheckInQRCode;

    /**
     * Sets up the activity's UI and button click listeners.
     * @param savedInstanceState If the activity is being re-initialized after being
     *                           previously shut down, this Bundle contains the data it most
     *                           recently supplied. Otherwise, it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in_qr_one);
        backCheckInQRCode = findViewById(R.id.floatingActionButton_back_checkin_image);

        // Set up ImageView and Button for sharing the QR code
        ImageView qrCode = findViewById(R.id.event_qr_pic_check_in);
        Button qrCodeShare = findViewById(R.id.share_button_check_in1);

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

        // Listener for the back navigation button
        backCheckInQRCode.setOnClickListener(v -> finish());

    }

    /**
     * Detects touch events to enable swipe gestures for navigation.
     * @param touchEvent The motion event triggering the touch.
     * @return Boolean indicating whether the touch was handled.
     */
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent)
    {
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                // User started touching the screen
                x1=touchEvent.getX();
                y1=touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                // User finished touching the screen
                x2=touchEvent.getX();
                y2=touchEvent.getY();

                // Detect if the swipe was from left to right
            if (x1<x2){
                // Start QRCodeEventActivity if it was a left-to-right swipe
                Intent i = new Intent(QRCodeCheckActivity.this,QRCodeEventActivity.class );
                startActivity(i);

            }
            break;

        }
        return super.onTouchEvent(touchEvent);
    }
}
