package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText etPhone, etPassword;
    Button btnLogin;

    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        btnLogin.setOnClickListener(v -> {
            String userId = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            Log.d("LOGIN", "Phone: " + userId + ", Password: " + password);

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter phone and password", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            usersRef.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot snapshot = task.getResult();
                    String savedPassword = snapshot.child("password").getValue(String.class);

                    if (savedPassword != null && savedPassword.equals(password)) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.saveUserId(this, userId);
                        startActivity(new Intent(this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "User not found. Redirecting to registration...", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, RegistrationStepOneActivity.class));
                }
            });
        });
    }
}
