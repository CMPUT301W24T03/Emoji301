
package com.example.emojibrite;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class EventQRGenerate_Activity extends AppCompatActivity {

    private Button generateQRButton;
    private ImageView QRcode_iv;

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_qrgenerate);
//
//        generateQRButton = findViewById(R.id.generate_qr_event_button);
//        QRcode_iv = findViewById(R.id.event_qr_iv);
//
//        // NOTE YOU WILL NEED TO SET UP THE INTENT TO PASS THE EVENT ID
//        generateQRButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generateQR(eventID);
//            }
//        });
//
//
//    }
//
//    public void generateQR(long eventID){
//
//        // generating the qr code now
//        MultiFormatWriter writer = new MultiFormatWriter();
//        // need a try catch in case
//        try {
//            BitMatrix bitMatrix = writer.encode(Long.toString(eventID), BarcodeFormat.QR_CODE, 400,400);
//            BarcodeEncoder encoder = new BarcodeEncoder();
//            Bitmap bitmap = encoder.createBitmap(bitMatrix);
//            QRcode_iv.setImageBitmap(bitmap);
//
//        } catch (WriterException e) {
//            throw new RuntimeException(e);
//        }
//
//        //Log.d("QRID", Long.toString(QRid));
//
//    }

}

