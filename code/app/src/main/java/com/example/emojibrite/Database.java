package com.example.emojibrite;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

/**
 * A class to represent the database
 */
public class Database {
    // attributes
    private final FirebaseFirestore db= FirebaseFirestore.getInstance();
    private final CollectionReference profileRef = db.collection("Users");

    private String firestoreDebugTag = "Firestore";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userUid = null;

    public interface UserNameDBCallBack{
        void onUserRetrieveNameComplete(String name);
    }
    public interface SignInCallBack{
        void onSignInComplete();
    }
    public interface ProfileImageCallBack{
        void onProfileImageComplete(Bitmap profileImage);
    }


    /**
     * A method to get the database
     * @return the database
     */
    public FirebaseFirestore getDb() {

        return db;
    }

    public boolean isUserSignedIn() {
        Boolean signedIn = mAuth.getCurrentUser() != null;

        return signedIn;
    }
    public void signOutUser(){

        mAuth.signOut();
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

        if (mAuth.getCurrentUser() != null) {
            userUid = mAuth.getCurrentUser().getUid();
        }
        return userUid;
    }
    public void setUserUid(){
        userUid = mAuth.getCurrentUser().getUid();
    }

    public void getUserName(UserNameDBCallBack callBack){
        DocumentReference docRef = profileRef.document(userUid);
        Log.d(TAG, "inside get username: " );
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    callBack.onUserRetrieveNameComplete(documentSnapshot.getString("name"));
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "get failed with ", e);
            }
        });
    }
    public void anonymousSignIn(SignInCallBack callBack) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "next step");
                            Log.d(TAG, "User ID: " + user.getUid());

                            userUid = user.getUid();
                            /*
                            when you call mAuth.signInAnonymously(), it returns a Task object. When the sign-in operation is complete, the Task is marked as successful or failed. If it's successful, you can get the FirebaseUser instance by calling mAuth.getCurrentUser(). This FirebaseUser instance represents the user that just signed in.
                             */
                            addUser(new Users(userUid));
                            Log.d(TAG, "User ID: " + user.getUid());
                            Log.d(TAG, "User created");
                            callBack.onSignInComplete();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }
                    }
                });
    }
    public CollectionReference getUserRef() {

        return profileRef;
    }



    /*
    basically, adduser recieves a user object.
    there are a few conditions this checks.

    it first does a database search in collections for the Uid meaning it tries to recieve the document
    NOW, there can be no document that is fine.
    if there is no document, (we check it using !document.exists((), we are going to add the user object(this object just has the Uid, every other field is null) to the database
    then there is a bunch of success listener which u don't have to worry about.

    NOW, what if there is a document meaning the user already exists in the database?
    it goes to the else where it checks, if the name is null meaning it is just a newly added user who hasn't initialized their name and other information yet(aka in the newaccountcreation session),
    then you can call this function still in which the database will be updated with the new user information
     */
    private void addUser(Users user){
        if (user == null) {
            Log.d(TAG, "User is null");
        }
        else{
            Log.d(TAG, "User is not null---" +  user.getProfileUid());
        }
        DocumentReference docRef = profileRef.document(user.getProfileUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        Log.d(TAG, "Document does not exist!");
                        profileRef.document(user.getProfileUid()).set(user)
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
                    else{
                        Log.d(TAG, "Document exists!");
                        if (document.getString("name") == null) {
                            profileRef.document(user.getProfileUid()).set(user)
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
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Log.d(TAG, "User id test test test: " + getUserUid());
                }
            }
        });
    }

    /**
     * A method to get the profile image
     * @param callBack
     */
    /*
    database.getProfileImage(new Database.ProfileImageCallBack() {
    @Override
    public void onProfileImageComplete(Bitmap profileImage) {
        // Use the profileImage bitmap here
        ImageView imageView = findViewById(R.id.profile_image);
        imageView.setImageBitmap(profileImage);
    }
    });

     */
    public void getAutoGenProfileImageFromDatabase(ProfileImageCallBack callBack){
        DocumentReference docRef = profileRef.document(userUid);
        Log.d(TAG, "inside getAutoGenProfileImageFromDatabase: " );
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.exists());

                        String encodedImage = document.getString("autoGenImage");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        callBack.onProfileImageComplete(decodedByte);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public void getUploadedProfileImageFromDatabase(ProfileImageCallBack callBack){
        DocumentReference docRef = profileRef.document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String encodedImage = document.getString("uploadedImage");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        callBack.onProfileImageComplete(decodedByte);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public void sendUploadedProfileImageToDatabase(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        profileRef.document(userUid).update("uploadedImage", encodedImage);
    }
public void sendAutoGenProfileImageToDatabase(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d(TAG, "encoded image: " + encodedImage);
        profileRef.document(userUid).update("autoGenImage", encodedImage);
        Log.d(TAG, "image sent to database");
    }

    /*
    ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator("aivan Edi", imageView, database.getUserUid());
                        profileImageGenerator.getProfileImage(new ProfileImageGenerator.OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Void aVoid) {
                                // After getProfileImage() is complete, call getProfileImageFromDatabase()
                                database.getProfileImageFromDatabase(new Database.ProfileImageCallBack() {
                                    @Override
                                    public void onProfileImageComplete(Bitmap profileImageFromDatabase) {
                                        // Use the profileImageFromDatabase bitmap here
                                        ImageView imageView = findViewById(R.id.profile_image);
                                        imageView.setImageBitmap(profileImageFromDatabase);
                                    }
                                });
                            }
                        });
     */
    /**
     * A method to get the profile collection
     * @return the profile collection
     * how to use?
     *  RetrieveFid (new UserDatabaseCallBack() {
     *   @Override
     *   public void onUserRetrieveIdComplete(String userExist) {
     *    print(userExist);
     *    }
     */
    /*
    public String RetrieveFid(userFidCallBack callBack){
        String Fid = "";
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String FID = task.getResult();
                            Log.d("Installations", "Installation ID: " + task.getResult());
                            callBack.onUserRetrieveIdComplete(FID);
                        } else {
                            Log.e("Installations", "Unable to get Installation ID");
                        }
                    }
                });
        return Fid;
    }

     */
    /**
     * A method to check if a user exists
     * @param callback the callback to use
     *                 to return the result
     *                 of the check
     * how to use?
     *    checkUserExist(new UserExistCallback() {
     *     @Override
     *     public void onUserExistCheckComplete(boolean userExists) {
     *         if (userExists) {
     *             System.out.println("User exists!");
     *         } else {
     *             System.out.println("User does not exist!");
     *         }
     *     }
     * });
     * database database = new Database();
     *
     * database.checkUserExist(new Database.userExistCallBack() {
     *     @Override
     *     public void onUserExistComplete(boolean userExist) {
     *         if (userExist) {
     *             // Handle the case where the user exists
     *         } else {
     *             // Handle the case where the user does not exist
     *         }
     *     }
     * });
     */
    /*
    public void checkUserExist(userExistCallBack callback){
        RetrieveFid(new userFidCallBack() {
            @Override
            public void onUserRetrieveIdComplete(String userExist) {
                Query query = profileRef.whereEqualTo("FID", userExist);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().size() > 0){
                                callback.onUserExistComplete(true);
                            }else{
                                callback.onUserExistComplete(false);
                            }
                        }else{

                            callback.onUserExistComplete(false);
                            //if it is some kind of error, it just returns false meaning user doesn't exist so it will create an account.
                        }
                    }
                });
    }
*/

    /**
     * A method to get the user collection
     * @return the user collection
     */


//    /**
//     * A method to add a user to the database
//     * @param user the user to add
//     */
//    public void addUser(Users user) {
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





}
