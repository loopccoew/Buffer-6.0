package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RegistrationStepOneActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etAddress, etAge, etPassword;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step_one);

        // Initializing EditTexts and Button
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etAge = findViewById(R.id.etAge);
        etPassword = findViewById(R.id.etPassword);
        btnNext = findViewById(R.id.btnNextStep);

        // Button click listener
        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validation check for empty fields
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || age.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate a random 4-digit user ID
            String userId = String.format("%04d", new Random().nextInt(10000));

            // Pass data to the next step
            Intent intent = new Intent(RegistrationStepOneActivity.this, RegistrationStepTwoActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            intent.putExtra("address", address);
            intent.putExtra("age", age);
            intent.putExtra("password", password);
            startActivity(intent);
        });
    }
}
