package com.example.emojibrite;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Random;

public class CheckInQRGenerate_Activity extends AppCompatActivity {

    private Button generateQRButton;
    private ImageView QRcode_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_qrgenerate);

        // setting stuff up for the QR
        generateQRButton = findViewById(R.id.generate_qr_checkin_button);
        QRcode_iv = findViewById(R.id.checkin_qr_iv);

        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQR();
            }
        });
    }

    public void generateQR(){
        // generating a 12 digit code between 100000000000 and 999999999999
        long a = 100000000000L;
        long b = 999999999999L;
        long QRid = (long) Math.floor(Math.random() * b) + a;

        // generating the qr code now
        MultiFormatWriter writer = new MultiFormatWriter();
        // need a try catch in case
        try {
            BitMatrix bitMatrix = writer.encode(Long.toString(QRid), BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            QRcode_iv.setImageBitmap(bitmap);

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        //Log.d("QRID", Long.toString(QRid));

    }
}