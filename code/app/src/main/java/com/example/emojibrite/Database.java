package com.example.emojibrite;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final CollectionReference signedUpRef = db.collection("SignedUp");

    private final CollectionReference notificationRef = db.collection("Notifications");



    private String firestoreDebugTag = "Firestore";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userUid ;

    private ArrayList<Event> eventsList;
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

    public void getAllUsers(OnUsersRetrievedListener listener) {
        // Get all the users from the database
        profileRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Users> users = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    Users user = document.toObject(Users.class);
                    users.add(user);
                }
                listener.onUsersRetrieved(users);
            } else {
                Log.d(firestoreDebugTag, "Error getting documents: ", task.getException());
            }
        });
    }

    public interface OnUsersRetrievedListener {
        void onUsersRetrieved(List<Users> users);
    }

    public void deleteUser(String userId, String imageUri){
        profileRef.document(userId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(firestoreDebugTag, "DocumentSnapshot successfully deleted!");
                        deleteUserFromSignedUp(userId);
                        deleteUserFromAttendeeList(userId);
                        deleteEventIfOrganizerDeleted(userId);
                        deleteUserUploadedImageFromStorage(imageUri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(firestoreDebugTag, "Error deleting document", e);
                    }
                });
    }

    public void deleteUserUploadedImageFromStorage(String imageUri){
        if (imageUri == null) {
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReferenceFromUrl(imageUri);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Image successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting image", e);
                    }
                });
    }

    public void deleteEventIfOrganizerDeleted(String userId){
        eventRef.whereEqualTo("organizer", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String eventId = document.getId();
                    deleteEvent(eventId, null);

                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

    public void deleteUserFromSignedUp(String userId){

        signedUpRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    List<String> signedAttendees = (List<String>) document.get("signedAttendees");
                    if (signedAttendees != null && signedAttendees.contains(userId)) {
                        signedAttendees.remove(userId);
                        document.getReference().update("signedAttendees", signedAttendees);
                    }
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }
    public void deleteUserFromAttendeeList(String userId){
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    List<String> attendeeList = (List<String>) document.get("attendeesList");
                    if (attendeeList != null && attendeeList.contains(userId)) {
                        attendeeList.remove(userId);
                        document.getReference().update("attendeesList", attendeeList);
                    }
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

    public void deleteEventNotification(String eventId){
        notificationRef.document(eventId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Notification successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting notification", e);
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
        //needs to be gone. not used or needed
        DocumentReference docRef = db.collection("Users").document(uid);

        // Create a map to hold the image URI
        Map<String, Object> imageUriMap = new HashMap<>();
        if (imageType.equals("uploadedImage")) {
            docRef.update("uploadedImageUri", imageUri);
        } else if (imageType.equals("autoGenImage")) {
            docRef.update("autoGenImageUri", imageUri);
        }
        // Store the image URI in the database

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
        eventMap.put("checkInID", event.getCheckInID());
        // For Uri objects, converted into strings?
        eventMap.put("imageUri", event.getImageUri() != null ? event.getImageUri().toString() : null);
        eventMap.put("checkInQRCode", event.getCheckInQRCode() != null ? event.getCheckInQRCode().toString() : null);
        eventMap.put("eventQRCode", event.getEventQRCode() != null ? event.getEventQRCode().toString() : null);

        //2D arrays for attendees and geolocations
        eventMap.put("attendeesList", event.getAttendeesList());
        eventMap.put("geolocationList", event.getGeolocationList());

        if (event.getImageUri()!=null){
            Log.d(TAG, event.getImageUri().toString()); //testing
        }

        if (event.getOrganizer() != null) {
            eventMap.put("organizer", event.getOrganizer());
        }

        // Storing the event map in Firestore under the event's ID.
        eventRef.document(event.getId()).set(eventMap)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Event successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing event", e))
                .addOnCompleteListener(onCompleteListener);
    }

    public void deleteEvent(String eventId, OnCompleteListener<Void> onCompleteListener){
        eventRef.document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String qrCheckUri = documentSnapshot.getString("checkInQRCode");
                String qrEventUri = documentSnapshot.getString("eventQRCode");
                String imageUri = documentSnapshot.getString("imageUri");

                Log.d(TAG, "Deleting QR Check-In URI: " + qrCheckUri);
                Log.d(TAG, "Deleting QR Event URI: " + qrEventUri);
                Log.d(TAG, "Deleting Image URI: " + imageUri);

                deleteQrEventPoster(qrCheckUri, qrEventUri, imageUri);
                deleteEventNotification(eventId);

                eventRef.document(eventId).delete()
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Event successfully deleted!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error deleting event", e))
                        .addOnCompleteListener(onCompleteListener);
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error fetching event", e));
    }

    public void deleteQrEventPoster(String checkInUri, String eventUri, String eventPoster){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        //delete the qr code
        if (checkInUri != null) {
            StorageReference imageRef = storage.getReferenceFromUrl(checkInUri);
            imageRef.delete();


        }
        if (eventUri != null) {
            StorageReference imageRef = storage.getReferenceFromUrl(eventUri);
            imageRef.delete();

        }
        if (eventPoster != null) {
            StorageReference imageRef = storage.getReferenceFromUrl(eventPoster);
            imageRef.delete();
        }

    }

    public void deleteImageFromStorage(Image image, OnImageDeletedListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReferenceFromUrl(image.getImageURL().toString());
        imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Image deleted successfully
                    if (image.getEventId() != null) {
                        eventRef.document(image.getEventId()).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String imageUri = documentSnapshot.getString("imageUri");
                                if (imageUri != null && imageUri.equals(image.getImageURL().toString())) {
                                    eventRef.document(image.getEventId()).update("imageUri", null);
                                }
                            }
                        });
                    } else if ( image.getUserId() != null) {
                        profileRef.document(image.getUserId()).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String imageUri = documentSnapshot.getString("uploadedImageUri");
                                if (imageUri != null && imageUri.equals(image.getImageURL().toString())) {
                                    profileRef.document(image.getUserId()).update("uploadedImageUri", null);
                                }
                            }
                        });
                    }
                    listener.onImageDeleted();
                }

            }
        });


    }
    public interface OnImageDeletedListener {
        void onImageDeleted();
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
        eventRef.whereEqualTo("organizer", organizerId)
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
                        Log.d(TAG,"Organizer: "+ event.getOrganizer());

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


    /**
     * An intrfacce for a callback to be invoked when a single event is fetched
     */
    public interface EventCallBack{

        /**
         * Called when an event is successfully fetched from the Firestore database.
         *
         * @param event The {@link Event} object representing the fetched event.
         */
        void onEventFetched(Event event);
    }

    /**
     * Fetches a single event from the Firestore database using the event ID.
     * If the event is found, the provided {@link EventCallBack} is invoked with the retrieved event.
     *
     * @param eventId The unique ID of the event to fetch.
     * @param callBack The callback that will handle the event once it is fetched.
     */

    public void getEventById(String eventId, EventCallBack callBack){
        eventRef.document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                Log.d(TAG, "Raw Firestore Data: " + documentSnapshot.getData());
                Event event = documentSnapshot.toObject(Event.class);
                Log.d(TAG,"EVENT ID:"+eventId);
//                if(event.getOrganizer() != null) {
//                    Log.d(TAG,"EVENT ORGANIZER NAME:"+event.getOrganizer().getName());
//                } else {
//                    Log.d(TAG, "Organizer is null");
//                }
                Log.d(TAG,"URI IMAGE:"+event.getImageUri());
                Log.d(TAG, "Organizer ID: "+event.getOrganizer());
                Log.d("DATABASE ARRAY","Attendees List: " + event.getAttendeesList().toString());
                callBack.onEventFetched(event);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching event", e);
        });
    }

    /**
     * This method retreives all the events available on the database
     * @param listener
     */


    public void fetchAllEventsDatabase(OnEventsRetrievedListener listener){
        eventRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List <Event> events = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                        Event event = snapshot.toObject(Event.class);
                        Log.d(TAG,"Event ID " + event.getId());
                        Log.d(TAG, "Event Title: " + event.getEventTitle());
                        Log.d(TAG, "Event Image URI: " + event.getImageUri());
                        Log.d(TAG,"Description: " + event.getDescription());
                        Log.d(TAG,"Organizer: "+ event.getOrganizer());
                        events.add(event);
                    }
                    // Calling the listener method with the list of retrieved events
                    listener.onEventsRetrieved(events);
                }).addOnFailureListener(e-> Log.e(TAG,"error fetching all the events", e));
    }

    public void fetchImages( @Nullable String pageToken,OnImageRetrievedListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("images/");
        Task<ListResult> listPageTask = pageToken != null
                ? imageRef.list(100, pageToken)
                : imageRef.list(100);

        listPageTask
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> prefixes = listResult.getPrefixes();
                        List<StorageReference> items = listResult.getItems();

                        // Process page of results
                        List<Image> images = new ArrayList<>();
                        for (StorageReference item : items) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                        @Override
                                        public void onSuccess(StorageMetadata storageMetadata) {
                                            Image image = new Image();
                                            image.setImageURL(uri);
                                            image.setEventId(storageMetadata.getCustomMetadata("event_id"));
                                            image.setUserId(storageMetadata.getCustomMetadata("user_id"));
                                            images.add(image);

                                            // If all images have been fetched, return them through the listener
                                            if (images.size() == items.size()) {
                                                listener.onImageRetrieved(images);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        // Recurse onto next page
                        if (listResult.getPageToken() != null) {
                            fetchImages(listResult.getPageToken(), listener);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred.
                    }
                });
    }



    public interface OnImageRetrievedListener {
        void onImageRetrieved(List<Image> images);
    }

    /**
     * Registers a new attendee for a specific event. If the event already has a list of attendees, the new attendee
     * is added to this list. If the attendee is already registered, no changes are made. If the event does not
     * have any attendees yet, a new list is created with the attendee in it.
     *
     * @param eventId       The unique identifier of the event for which to register the attendee.
     * @param newAttendeeId The unique identifier of the attendee to register.
     */

    public void addSignin(String eventId, String newAttendeeId) {
        // Reference to the document for the specific event in the 'SignedUp' collection
        DocumentReference eventDocRef = signedUpRef.document(eventId);
        // Attempt to retrieve the event document
        eventDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieves the current list of attendees for the event
                List<String> attendees = (List<String>) documentSnapshot.get("signedAttendees");
                if (attendees == null) {
                    // If there's no list, create a new one
                    attendees = new ArrayList<>();
                }
                // Add the attendee if they're not already registered
                if (!attendees.contains(newAttendeeId)) {
                    attendees.add(newAttendeeId);
                }
                // Update the document with the new list of attendees
                eventDocRef.update("signedAttendees", attendees)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Attendee successfully added"))
                        .addOnFailureListener(e -> Log.e(TAG, "Error adding attendee", e));
            } else {
                // No document exists for the event, create a new event with the attendee
                List<String> attendees = new ArrayList<>();
                attendees.add(newAttendeeId);
                Map<String, Object> newEventMap = new HashMap<>();
                newEventMap.put("id", eventId);
                newEventMap.put("signedAttendees", attendees);
                // Set the new event in the database
                eventDocRef.set(newEventMap)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "New event created with attendee"))
                        .addOnFailureListener(e -> Log.e(TAG, "Error creating new event", e));
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error fetching event document", e));
    }


    /**
     * This method is to retrieve the array list once we pass the event id
     * @param eventId
     * @param listener
     */

    public void getSignedAttendees(String eventId, OnSignedAttendeesRetrievedListener listener) {
        signedUpRef.document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> attendees = (List<String>) documentSnapshot.get("signedAttendees");
                listener.onSignedAttendeesRetrieved(attendees);
            } else {
                listener.onSignedAttendeesRetrieved(new ArrayList<>());
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error fetching signed attendees", e));
    }

    public interface OnSignedAttendeesRetrievedListener {
        void onSignedAttendeesRetrieved(List<String> attendees);
    }

    /**
     * Fetches a single event from the Firestore database using the check-in QR ID.
     * If the event is found, the provided {@link EventCallBack} is invoked with the retrieved event.
     *
     * @param QRCheckinID
     * The ID of the check-in QR code.
     * @param callBack
     * The callback that will handle the event once it is fetched.
     */

    public void getEventByCheckInID(String QRCheckinID, EventCallBack callBack){
        eventRef.whereEqualTo("checkInID",QRCheckinID).get().addOnSuccessListener(querySnapshot -> {
            // if our query is not empty and we only have a single document returned
            if(!querySnapshot.isEmpty() && querySnapshot.size() == 1){
                // get the event and make it an event class.
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                Event event = document.toObject(Event.class);
                callBack.onEventFetched(event);
            }
            // else we found multiple events with the same QR check in.
            else{
                callBack.onEventFetched(null);
            }

            // exception for if the task for some reason failed
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching event", e);
        });
    }

    public void getSignedUpEvents(String userId, OnEventsRetrievedListener listener){
        fetchAllEventsDatabase(events -> {
            List<Event> signedUpEvents = new ArrayList<>();
            AtomicInteger callbackCount = new AtomicInteger(events.size());

            for (Event event : events) {
                checkUserInEvent(userId, event.getId(), isUserSignedUp -> {
                    if (isUserSignedUp) {
                        signedUpEvents.add(event);
                    }
                    if (callbackCount.decrementAndGet() == 0) {
                        // All callbacks have returned, we can now call the listener
                        listener.onEventsRetrieved(signedUpEvents);
                    }
                });
            }
        });
    }


    public void checkUserInEvent(String userUid, String eventId, CheckUserInEventCallback callback) {

        getSignedAttendees(eventId, attendees -> {
            boolean isUserSignedUp = attendees.contains(userUid);
            callback.onResult(isUserSignedUp);

    });
    }

    public interface CheckUserInEventCallback {
            void onResult(boolean isSignedUp);
        }




    /**
     * Method to update an event's attendee's list.
     * @param eventID
     * The event that's meant to be updated.
     * @param attendees
     * A 2D ArrayList containing the attendees and the number of times they have checked in.
     */
    public void updateEventAttendees(String eventID, ArrayList<String> attendees) {
        eventRef.document(eventID).update("attendeesList", attendees)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Event attendee list successfully updated!"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating event attendee list", e));
    }

    /**
     * Method to update an event's list of where attendees are checking in from.
     * @param eventID
     * The event that's meant to be updated.
     * @param checkInLocations
     * A 2D ArrayList containing the locations that attendees checked in from.
     */
    public void updateEventCheckInLocations(String eventID, ArrayList<String> checkInLocations){
        eventRef.document(eventID).update("geolocationList", checkInLocations).addOnSuccessListener( onSuccessListener -> {
            Log.d(TAG, "Event geolocation check-ins successfully updated!");

            // exception for if the task for some reason failed
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error updating event geolocation check-ins", e);
        });
    }

}