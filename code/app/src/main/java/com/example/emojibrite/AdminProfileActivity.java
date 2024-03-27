package com.example.emojibrite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class AdminProfileActivity extends AppCompatActivity {

    ListView userList;
    ProfileAdapter profileAdapter;

    ArrayList<Users> dataList;
    EventAdapter eventAdapter; // Custom adapter to bind event data to the ListView
    Users user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_view);

        // Initialize userList (you may get it from somewhere else)
        userList = findViewById(R.id.profile_list);
        dataList = new ArrayList<>();

        // Populate userList with dummy data (for demonstration)
        populateUserList();

        // Initialize the ArrayAdapter
        profileAdapter = new ProfileAdapter(this, dataList);

        userList.setAdapter(profileAdapter);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("userObject");

        FloatingActionButton back = findViewById(R.id.backButton);


        // Set item click listener for ListView
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected user
                Users selectedUser = dataList.get(position);
                // Show popup menu to confirm deletion
                showDeleteConfirmationPopup(selectedUser);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminProfileActivity.this, AdminActivity.class);
                intent.putExtra("AdminHomePage", user);
                startActivity(intent);

            }
        });


    }

    private void populateUserList() {
        // Add dummy users for demonstration
        dataList.add(new Users("123456789"));
        dataList.add(new Users("987654321"));
        dataList.add(new Users("456123789"));
    }

    private void showDeleteConfirmationPopup(final Users user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete " + user.getName() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove the user from the list
                dataList.remove(user);
                // Notify the adapter that the data set has changed
                profileAdapter.notifyDataSetChanged();
                // Show a toast to indicate deletion
                Toast.makeText(AdminProfileActivity.this, user.getName() + " deleted.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }



}
