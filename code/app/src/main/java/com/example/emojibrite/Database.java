package com.example.emojibrite;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

/**
 * A class to represent the database
 */
public class Database {
    // attributes
    private final FirebaseFirestore db= FirebaseFirestore.getInstance();
    private final CollectionReference userRef = db.collection("Users");
    private String firestoreDebugTag = "Firestore";

    /**
     * A method to get the database
     * @return the database
     */
    public FirebaseFirestore getDb() {
        return db;
    }

    /**
     * A method to get the user collection
     * @return the user collection
     */
    public CollectionReference getUserRef() {
        return userRef;
    }

//    /**
//     * A method to add a user to the database
//     * @param user the user to add
//     */
//    public void addUser(Profile user) {
//        // refer to lab 5 for examples something like
//        HashMap<String, String> fieldValuePair = new HashMap<>();
//        fieldValuePair.put("name", user.getName());
//        fieldValuePair.put("email", user.getEmail());
//        fieldValuePair.put("role", user.getRole());
//        userRef
//                .document(user.getId())                 // uniquely generated id
//                .set(fieldValuePair)                    // set the fields and values in firestore
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(firestoreDebugTag, "DocumentSnapshot successfully written! ");
//                    }
//                })
//                .addOnFailureListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(firestoreDebugTag, "DocumentSnapshot successfully written! ");
//                    }
//                });
//    }
//
//    public void removeUser(User user) {
//
//    }
//
}
