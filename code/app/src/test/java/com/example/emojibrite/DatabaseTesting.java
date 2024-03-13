package com.example.emojibrite;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DatabaseTesting {
    @Test
    public void testUserSignedIn(){
            // Create a mock FirebaseAuth instance
            FirebaseAuth mockAuth = mock(FirebaseAuth.class);
            // Create a mock FirebaseUser instance
            FirebaseUser mockUser = mock(FirebaseUser.class);
            // When getCurrentUser is called on the mockAuth instance, return the mockUser
            when(mockAuth.getCurrentUser()).thenReturn(mockUser);
            // Create an instance of Database with the mocked FirebaseAuth
            Database database = new Database(mockAuth);
            // Assert that isUserSignedIn returns true
            assertTrue(database.isUserSignedIn());
    }

}
