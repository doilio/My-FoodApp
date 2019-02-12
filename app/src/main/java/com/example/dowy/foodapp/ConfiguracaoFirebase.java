package com.example.dowy.foodapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static FirebaseFirestore mFireStore;
    private static FirebaseAuth authentication;
    private static StorageReference storageRef;

    public static FirebaseFirestore getFireStore() {
        if (mFireStore == null) {
            mFireStore = FirebaseFirestore.getInstance();
        }
        return mFireStore;
    }

    public static StorageReference getStorage() {
        if (storageRef == null) {
            storageRef = FirebaseStorage.getInstance().getReference();
        }
        return storageRef;
    }

    public static FirebaseAuth getAuthentication() {
        if (authentication == null) {
            authentication = FirebaseAuth.getInstance();
        }
        return authentication;
    }

}
