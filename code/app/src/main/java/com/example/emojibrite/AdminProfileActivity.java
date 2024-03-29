package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminProfileActivity extends AppCompatActivity {
    private Users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_list);

        ImageView backArrow = findViewById(R.id.back_arrow_listImg); //back button
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileActivity.this, AdminImagesHome.class);
                finish();
            }
        });
    }
}
