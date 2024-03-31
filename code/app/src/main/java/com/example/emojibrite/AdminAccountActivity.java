package com.example.emojibrite;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class AdminAccountActivity extends AppCompatActivity {
    ListView profileList;
    ArrayList<Users> dataList;

    AttendeesArrayAdapter profileAdapter;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_view);

        profileList = findViewById(R.id.profile_list);
        dataList = new ArrayList<>();

        profileAdapter = new AttendeesArrayAdapter(this, dataList);
        profileList.setAdapter(profileAdapter);







    }
}
