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

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class ImageGenTest {

    @Test
    public void testAddingPlusToName() {
        ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator("testUid", "testName");
        profileImageGenerator.setProfileImageName("test+Name");
        assertEquals("test+Name", profileImageGenerator.name);
    }

    @Test
    public void testGetProfileImage(){
        //requires changes to match the actual implementation
        ProfileImageGenerator profileImageGenerator = new ProfileImageGenerator("testUid", "testName");
        profileImageGenerator.getProfileImage(new ProfileImageGenerator.OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Uri result) {
                assertNotNull(result);
            }
        });
    }






}
