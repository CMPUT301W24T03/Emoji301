package com.example.emojibrite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AdminAccountActivity extends AppCompatActivity {
    ListView profileList;
    ArrayList<Users> dataList;
    Users user;
    Database database = new Database();
    AttendeesArrayAdapter profileAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_view);

        Intent intent1 = getIntent();
        user = intent1.getParcelableExtra("userObject");

        profileList = findViewById(R.id.profile_list);
        dataList = new ArrayList<>();
        profileAdapter = new AttendeesArrayAdapter(this, dataList, user.getRole());
        profileList.setAdapter(profileAdapter);
        FloatingActionButton backBtn = findViewById(R.id.backButton);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminAccountActivity.this, AdminActivity.class);
            intent.putExtra("userObject", user);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUsers();
    }

    private void fetchUsers(){
        List<Users> users = new ArrayList<>();
        database.getAllUsers(new Database.OnUsersRetrievedListener() {
            @Override
            public void onUsersRetrieved(List<Users> users) {
                dataList.clear();
                dataList.addAll(users);
                removeAdminFromDataList();


                profileAdapter.notifyDataSetChanged();
            }
        });

    }

    private void removeAdminFromDataList(){
        Iterator<Users> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            Users user = iterator.next();
            if (user.getRole().equals("3")) {
                // Remove the user from the database
                iterator.remove();

                // Remove the user from the iterator and dataList

            }
            else if (user.getProfileUid().equals(this.user.getProfileUid())){
                iterator.remove();
            }
        }
    }

}
