package com.codegym.udemy.service.impl;

import com.codegym.udemy.configuration.FirebaseProperties;
import com.codegym.udemy.service.FirebaseStorageService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    private final String bucketName = "cg-udemy-746a7.appspot.com";
    @Autowired
    FirebaseProperties firebaseProperties;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("firebase-private-key.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(firebaseProperties.getBucketName())
                    .build();
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            } else {
                FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        String filename = generateUniqueFileName(file.getOriginalFilename());
        Blob blob = bucket.create(filename, file.getBytes(), file.getContentType());

        String url = firebaseProperties.getImageUrl() + "o/" + blob.getName() + "?alt=media";
        return url;
    }

    private String generateUniqueFileName(String originalFileName) {
        // Implement your logic to generate a unique file name
        return UUID.randomUUID().toString() + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    private String getDownloadUrl(String fileName) {
        // Construct the download URL of the file in Firebase Storage
        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }
}
