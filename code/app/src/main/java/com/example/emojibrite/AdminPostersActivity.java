package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.emojibrite.R;

public class AdminPostersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_images_list);

        ImageView backArrow = findViewById(R.id.back_arrow_listImg); //back button
        Log.d("AdminPostersActivity", "Check this");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPostersActivity.this, AdminImagesHome.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
