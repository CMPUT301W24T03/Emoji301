package com.example.emojibrite;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import javax.annotation.PostConstruct;
//
//@Service
//public class NotificationService {
//
//    private final String SERVICE_ACCOUNT_FILE_PATH = "\"C:\\Users\\aivan\\Downloads\\emojibrite-0b50cc51aa0d.json\""; // Replace with path to your service account key file
//
//    @PostConstruct
//    public void initializeFirebase() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_FILE_PATH);
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        FirebaseApp.initializeApp(options);
//    }
//
//    public String sendNotification(String registrationToken, String title, String body) throws Exception {
//        Message message = Message.builder()
//                .putData("title", title)
//                .putData("body", body)
//                .setToken(registrationToken)
//                .build();
//
//        return FirebaseMessaging.getInstance().send(message);
//    }
//}