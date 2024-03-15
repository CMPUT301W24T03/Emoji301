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

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class DatabaseTesting {

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference profileRef = db.collection("Users");
    private  Users user = new Users();

    private Database database = new Database(db);





    @Before
    public void setUp() {

        user.setProfileUid("testUid");
        user.setName("testName");

    }

    @Test
    public void testSetUserObject() {
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