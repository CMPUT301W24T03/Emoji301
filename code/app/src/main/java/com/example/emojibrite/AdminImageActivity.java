//
//package com.example.emojibrite;
//
//
//import android.content.DialogInterface;
//import android.media.Image;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class AdminImageActivity extends AppCompatActivity {
//
//    FloatingActionButton fab;
//    private RecyclerView recyclerView;
//    private ArrayList<Image> dataList;
//    private AdminImageAdapter adapter;
//    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
//
//
//}
///*
//import java.util.ArrayList;
//
//public class AdminImageActivity extends AppCompatActivity {
//
//
//    private GridView gridView;
//    private AdminImageAdapter adminImageAdapter;
//
//    ArrayList<Image> dataList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_view);
//
//        gridView = findViewById(R.id.gridView);
//        adminImageAdapter = new AdminImageAdapter(this, dataList); // Replace AdminImageAdapter with your custom adapter
//        gridView.setAdapter(adminImageAdapter);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                showDeleteConfirmationPopup(position);
//            }
//        });
//    }
//
//    private void showDeleteConfirmationPopup(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Confirm Deletion");
//        builder.setMessage("Are you sure you want to delete this image?");
//        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                adminImageAdapter.removeItem(position);
//                Toast.makeText(AdminImageActivity.this, "Image deleted.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.create().show();
//    }
//}
//*/
