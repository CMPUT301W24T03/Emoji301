package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminImagesHome extends AppCompatActivity {
    private Users user;
    Button profilesBtn;
    Button postersBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_home_admin);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");

        profilesBtn = findViewById(R.id.imageProfilesButton);
        postersBtn = findViewById(R.id.imagePostersButton);

        profilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminImagesHome.this, AdminProfileActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });
        postersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminImagesHome.this, AdminProfileActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }

        });

        ImageView backArrow = findViewById(R.id.back_arrow_images); //back button
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminImagesHome.this, AdminActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
                finish();
            }
        });
    }
}
