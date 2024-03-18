package com.example.emojibrite;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database class to handle the database
 */
public class Database {
    private  FirebaseApp firebaseApp;
    private Context context;
    // attributes
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private final CollectionReference profileRef = db.collection("Users");

    private final CollectionReference eventRef = db.collection("Events");

    private String firestoreDebugTag = "Firestore";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userUid ;
    /**
     * An interface that serves as a callback for user name retrieval
     *
     */
    public interface UserNameDBCallBack{
        /**
         * A method that is called when a user's name has been successfully retrieved
         * @param name is a string of the retrieved user
         */
        void onUserRetrieveNameComplete(String name);
    }
    /**
     * An interface that serves as a callback for sign in
     *
     */
    public interface SignInCallBack{
        /**
         * Part of the SignInCallBack interface which is called when the sign-in operation is
         * completed
         */
        void onSignInComplete();
    }

    /**
     * A constructor that is used to create a new instance of the database class
     * @param context is the current state of the application or activity
     */


    public Database(FirebaseFirestore database) {
        this.db = database;
    }

    public Database(){
    }
    /**
     * A method to get the user id
     * @return the user id
     */

    public boolean isUserSignedIn() {
        //no need to test since not used in the app
        Boolean signedIn = mAuth.getCurrentUser() != null;

        return signedIn;
    }

/*
check if user signed in:
if yes, get user id and u can use it to get user data
if no, sign in anonymously where if there isn't a user id in the database, it will create one
once created, u can call getuseruid to get the user id and use it to get user data
 */
    /**
     * A method to get the user id
     * @return the user id
     */
    public String getUserUid() {
        return userUid;
        //no need for test
    }
    /**
     * A method that sets the userUid field to the UID of the currently signed-in user.
     * Use this method after the user has successfully signed in.
     */
    public void setUserUid(){

        userUid = mAuth.getCurrentUser().getUid();
        //no need for test
    }
    /**
     * A method that signs in a user anonymously using Firebase Auth/
     * If the sign-in is successful, a new user is added to the databse with the user's unique ID.
     * If it fails, log it
     * @param callBack is an instance of SignInCallBack which used to handle the result of the sign-in operation.
     */
    public void anonymousSignIn(SignInCallBack callBack) {
        //no need for test
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "User ID: " + user.getUid());
                            userUid = user.getUid();
                            /*
                            when you call mAuth.signInAnonymously(), it returns a Task object. When the sign-in operation is complete, the Task is marked as successful or failed. If it's successful, you can get the FirebaseUser instance by calling mAuth.getCurrentUser(). This FirebaseUser instance represents the user that just signed in.
                             */
                            Log.d(TAG, "User ID1: " + user.getUid());
                            Log.d(TAG, "User created1");
                            callBack.onSignInComplete();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }
                    }
                });
    }
    /**
     * A method to get the user id
     * @return the user id
     */
    public CollectionReference getUserRef() {
        //no need for test
        return profileRef;
    }




    /**
     * A method to set the user object in the database
     * @param user to set
     */
    public void setUserObject(Users user){
        //need testing
        DocumentReference docRef = profileRef.document(user.getProfileUid());
        docRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(firestoreDebugTag, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(firestoreDebugTag, "Error writing document", e);
                    }
                });
    }




    /**
     * Retrieves a user document from the "user" collection in Firestore using the document uid.
     * If the document exists, it is converted into a Users object and the `onUserDocumentRetrieved` method of the provided listener is called with the Users object.
     * If the document doesn't exist, log it
     * If an error occurs during the document retrieval,log it.
     * @param uid is the user id of the document ot retrieve
     * @param listener is an instance of OnUserDocumentRetrievedListener which is used to handle the result of the document retrieval.
     */

    public void getUserDocument(String uid, OnUserDocumentRetrievedListener listener) {
        // Get the document with the specified UID
        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // Convert the document into a Users object
                    //Users retrievedUser = document.toObject(Users.class);
                    listener.onUserDocumentRetrieved(document);



                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                }
            }
        });
    }
    /**
     * An interface that serves as a callback for user document retrieval operations
     */
    public interface OnUserDocumentRetrievedListener {
        void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot);
    }
    /**
     * Store an image URI in a firestore document based on user id and image type
     * If the image type is "uploadedImage", the image URI is stored under the "uploadedImage" field.
     * If the image type is "autoGenImage", the image URI is stored under the "autoGenImage" field.
     * else nothing
     * @param uid is the  user ID of the document to store the image URI in.
     * @param imageUri is the URI of the image to store.
     * @param imageType is the type of the image, either 'uploadedImage' or 'autoGenImage'
     */
    //uri here
    public void storeImageUri(String uid, String imageUri, String imageType) {
        //need testing
        // Get a reference to the user document
        DocumentReference docRef = db.collection("Users").document(uid);

        // Create a map to hold the image URI
        Map<String, Object> imageUriMap = new HashMap<>();
        if (imageType.equals("uploadedImage")) {
            imageUriMap.put("uploadedImageUri", imageUri);
        } else if (imageType.equals("autoGenImage")) {
            imageUriMap.put("autoGenImageUri", imageUri);
        }
        // Store the image URI in the database
        docRef.set(imageUriMap, SetOptions.merge());
    }
    /**
     * An interface that serves as a callback for image processing operations
     */
    public interface ImageBitmapCallBack {
        /**
         * A method that is called when the image has been successfully loaded or processed
         * @param bitmap The image has been successfully loaded or processed
         */
        void onImageBitmapComplete(Bitmap bitmap);
    }

    //EVENT SECTION:

    /**
     * Adds an event to the Firebase Firestore database.
     * This method creates a map of event details and stores it under a document identified by the event's ID.
     *
     * @param event The event object containing the details of the event.
     * @param onCompleteListener A listener that is called upon the completion of the event addition process.
     */
    public void addEvent(Event event, OnCompleteListener<Void> onCompleteListener) {
        // Creating a map to hold event details.
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("id",event.getId());
        eventMap.put("eventTitle", event.getEventTitle());
        eventMap.put("description", event.getDescription());
        eventMap.put("date", event.getDate());
        eventMap.put("time", event.getTime());
        eventMap.put("milestone", event.getMilestone());
        eventMap.put("location", event.getLocation());
        eventMap.put("capacity", event.getCapacity());
        // For Uri objects, converted into strings?
        eventMap.put("imageUri", event.getImageUri() != null ? event.getImageUri().toString() : null);
        eventMap.put("checkInQRCode", event.getCheckInQRCode() != null ? event.getCheckInQRCode().toString() : null);
        eventMap.put("eventQRCode", event.getEventQRCode() != null ? event.getEventQRCode().toString() : null);

        if (event.getImageUri()!=null){
            Log.d(TAG, event.getImageUri().toString()); //testing
        }

        // Adding organizer to details
        if (event.getOrganizer() != null) {
            Map<String, Object> organizerMap = new HashMap<>();
            Users organizer = event.getOrganizer();
            organizerMap.put("organizerId", organizer.getProfileUid());
            organizerMap.put("organizerName", organizer.getName());
            organizerMap.put("organizerGenerated", organizer.getAutoGenImageUri());
            organizerMap.put("organizerUploaded", organizer.getProfileUid());
            // Add other relevant fields from the Users class
            eventMap.put("organizer", organizerMap);
        }

        // Storing the event map in Firestore under the event's ID.
        eventRef.document(event.getId()).set(eventMap)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Event successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing event", e))
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * Retrieves a list of events organized by a specific user from the Firestore database.
     * This method queries the Firestore database for events where the 'organizerId' matches the provided ID.
     *
     * @param organizerId The unique ID of the organizer whose events are to be retrieved.
     * @param listener A listener that is called when events are successfully retrieved.
     */

    public void getEventsByOrganizer(String organizerId, OnEventsRetrievedListener listener) {
        // Querying Firestore for events organized by the specified user.
        eventRef.whereEqualTo("organizer.organizerId", organizerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // List to hold the retrieved events.
                    List<Event> events = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Event event = snapshot.toObject(Event.class);
                        Log.d(TAG,"Event ID " + event.getId());
                        Log.d(TAG, "Event Title: " + event.getEventTitle());
                        Log.d(TAG, "Event Image URI: " + event.getImageUri());
                        Log.d(TAG,"Description: " + event.getDescription());

                        // Adding the event to the list.
                        events.add(event);
                    }

                    // Calling the listener method with the list of retrieved events
                    listener.onEventsRetrieved(events);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching events", e));
    }


    /**
     * An interface for listeners that handle the retrieval of a list of events.
     * Implement this interface to receive a callback when a list of events is successfully retrieved.
     */
    public interface OnEventsRetrievedListener {
        /**
         * Method called when a list of events is successfully retrieved.
         *
         * @param events A list of Event objects.
         */
        void onEventsRetrieved(List<Event> events);
    }














}
