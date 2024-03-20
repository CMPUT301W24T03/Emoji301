package com.example.emojibrite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class DatabaseTesting {

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference profileRef = db.collection("Users");
    private  Users user = new Users();







    @Before
    public void setUp() {

        user.setProfileUid("testUid");
        user.setName("testName");

    }
    @Test
    public void testGetUserDocument()  {
        Database database = new Database(FirebaseFirestore.getInstance());
        String testUid = "testUid"; // replace with a valid uid

        // Call the method
        database.getUserDocument(testUid, new Database.OnUserDocumentRetrievedListener() {
            @Override
            public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                // Check that the document is not null
                assertNotNull(documentSnapshot);
                // Perform other assertions here if needed
            }
        });
    }
    @Test
    public void testStoreImageUri() {
        Database database = new Database(FirebaseFirestore.getInstance());
        String testUid = "testUid"; // replace with a valid uid
        String testUploadedImageUri = "testUploadedImageUri";
        String testAutoGenImageUri = "testAutoGenImageUri";

        // Call the method for uploadedImage
        database.storeImageUri(testUid, testUploadedImageUri, "uploadedImage");

        // Retrieve the document and check if the uploadedImage URI was stored correctly
        database.getUserDocument(testUid, new Database.OnUserDocumentRetrievedListener() {
            @Override
            public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                // Check that the document is not null
                assertNotNull(documentSnapshot);
                // Check that the uploadedImage URI was stored correctly
                assertEquals(testUploadedImageUri, documentSnapshot.getString("uploadedImageUri"));
            }
        });

        // Call the method for autoGenImage
        database.storeImageUri(testUid, testAutoGenImageUri, "autoGenImage");

        // Retrieve the document and check if the autoGenImage URI was stored correctly
        database.getUserDocument(testUid, new Database.OnUserDocumentRetrievedListener() {
            @Override
            public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                // Check that the document is not null
                assertNotNull(documentSnapshot);
                // Check that the autoGenImage URI was stored correctly
                assertEquals(testAutoGenImageUri, documentSnapshot.getString("autoGenImageUri"));
            }
        });
    }

    @Test
    public void testSetUserObject() {
        Database database = new Database(FirebaseFirestore.getInstance());
        database.setUserObject(user);

        profileRef.document(user.getProfileUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assertEquals(document.getString("name"), "testName");
                    assertEquals(document.getString("profileUid"), "testUid");

                }
            }
        });
    }
}