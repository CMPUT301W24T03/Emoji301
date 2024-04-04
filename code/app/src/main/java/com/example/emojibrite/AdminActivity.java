package com.example.emojibrite;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class AdminActivity extends AppCompatActivity {
    private Users user;

    private Button eventBtn;
    private Button accountBtn;

    private Button imageBtn;

    private ImageView profileButton;

    private Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.admin_home_page);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");

        eventBtn = findViewById(R.id.eventAdminButton);
        accountBtn = findViewById(R.id.accountAdminButton);
        imageBtn = findViewById(R.id.imagesAdminButton);
        profileButton = findViewById(R.id.profile_pic);

        checkUserDoc(user.getProfileUid());



    }

    private void buttonListeners(){
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to the ProfileActivity page
                Intent intent = new Intent(AdminActivity.this, ProfileActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });
        //the intent for the buttons below are just place holders for the updated ui'
        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the EventHome page
                Intent intent = new Intent(AdminActivity.this, AdminEventActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the AccountActivity page
                Intent intent = new Intent(AdminActivity.this, AdminAccountActivity.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the ImageActivity page
                Intent intent = new Intent(AdminActivity.this, AdminImagesHome.class);
                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

    }

    private void checkUserDoc(String userUid){
        database.getUserDocument(userUid, documentSnapshot -> {
            if (documentSnapshot.exists()) {

                user = documentSnapshot.toObject(Users.class);
                if(user.getRole().equals("1")){
                    Toast.makeText(this, "User lost moderator role", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                    startActivity(intent);
                }


                displayProfileIcon();
                buttonListeners();
            } else {


                Toast.makeText(this, "User got deleted by admin", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void displayProfileIcon() {
        if (user.getUploadedImageUri() != null) {
            // User uploaded a picture, use that as the ImageView
            //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(AdminActivity.this).load(user.getUploadedImageUri()).into(profileButton);
                }
            });
        } else if (user.getUploadedImageUri() == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(AdminActivity.this).load(user.getAutoGenImageUri()).into(profileButton);
                }
            });
        } else {
            Log.e("AdminActivity ", "User object is null");
        }
    }
}