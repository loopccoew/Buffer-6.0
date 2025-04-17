package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simplewomensafetyapp.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationTermsActivity extends AppCompatActivity {

    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_terms);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String userId = getIntent().getStringExtra("userId");
            String name = getIntent().getStringExtra("name");
            String email = getIntent().getStringExtra("email");
            String phone = getIntent().getStringExtra("phone");
            String address = getIntent().getStringExtra("address");
            String age = getIntent().getStringExtra("age");
            String password = getIntent().getStringExtra("password");

            // 🔽 Add this line to initialize Firebase reference
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            // 🔽 Prepare data to store
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("userId", userId);
            userMap.put("name", name);
            userMap.put("email", email);
            userMap.put("phone", phone);
            userMap.put("address", address);
            userMap.put("age", age);
            userMap.put("password", password);

            // 🔽 Save to Firebase

            usersRef.child(phone).setValue(userMap)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Registration", "User successfully registered.");
                        Toast.makeText(this, "Registration successful. Please log in.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("RegistrationError", "Error during registration: " + e.getMessage());
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });


        });
    }
}
