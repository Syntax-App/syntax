package edu.brown.cs.student.main.server.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class configurates the Firebase/Firestore communications and
 * functionality of our backend.
 */
public class FirebaseConfig {

    private Firestore db;

    /**
     * This accessor method gets the Firestore database of our project
     * @return Firestore database containing user/project data
     */
    public Firestore getDb() {
        return this.db;
    }

    /**
     * This method initializes the Firebase authentication of our backend
     */
    public void initializeFirebase() {
        try {
            // use credentials to initialize Firebase
            FileInputStream refreshToken = new FileInputStream("src/main/java/edu/brown/cs/student/main/server/config/firebase_credentials.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .build();
            FirebaseApp.initializeApp(options);
            this.db = FirestoreClient.getFirestore();
        } catch (FileNotFoundException e) {
            System.err.println("Refresh token file not found!");
        } catch (IOException e) {
            System.err.println("Failed to read refresh token");
        }
    }
}
