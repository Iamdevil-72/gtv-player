package com.example.gtvplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.Executor;

public class profile extends AppCompatActivity{
    TextView userName,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phone = findViewById(R.id.profilePhone);
        userName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        logout = findViewById(R.id.logout);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("user profile").document(userId);
        documentReference.addSnapshotListener((Executor) this, (documentSnapshot, e) -> {
            assert documentSnapshot != null;
            phone.setText(documentSnapshot.getString("phone"));
            userName.setText(documentSnapshot.getString("userName"));
            email.setText(documentSnapshot.getString("email"));
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();//logout
            startActivity(new Intent(getApplicationContext(),login.class));
        });
    }
}
