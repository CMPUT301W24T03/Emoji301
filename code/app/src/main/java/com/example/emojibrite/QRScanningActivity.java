package com.example.emojibrite;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRScanningActivity extends AppCompatActivity {

    private Button qrScanButton;

    private TextView qr_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrscanning);

        qrScanButton = findViewById(R.id.scan_qr_button);
        qr_result = findViewById(R.id.qr_scan_result);

        qrScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QR_scan();
            }
        });



    }

    /**
     * Function to scan a QR code.
     */
    private void QR_scan(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR code");
        options.setOrientationLocked(true);
        options.setDesiredBarcodeFormats("QR_CODE");

        // launching scanner
        scanLauncher.launch(options);
    }

    /**
     * Launches QR scanner and sets the result to the textview.
     */
    private ActivityResultLauncher<ScanOptions> scanLauncher = registerForActivityResult(new ScanContract(), result ->{

        // checking to see if we actually scanned something
        if (result.getContents() != null){
            qr_result.setText(result.getContents());
        }
    });
}