package com.codegym.udemy.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Autowired
    private FirebaseProperties firebaseProperties;

    @PostConstruct
    public void init() {
        try {
            // Load the Firebase service account key JSON file
            ClassPathResource serviceAccountResource = new ClassPathResource("firebase-private-key.json");
            InputStream serviceAccountStream = serviceAccountResource.getInputStream();

            // Initialize Firebase Admin SDK
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setStorageBucket(firebaseProperties.getBucketName())
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            // Handle IOException (e.g., file not found or invalid)
            e.printStackTrace();
            // You might want to throw a runtime exception or log the error
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace();
        }
    }
}
