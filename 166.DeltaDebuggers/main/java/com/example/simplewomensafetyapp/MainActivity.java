package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If already logged in, go to dashboard
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (prefs.contains("userId")) {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLoginPage);
        btnRegisterPage = findViewById(R.id.btnRegisterPage);

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        btnRegisterPage.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegistrationStepOneActivity.class)));
    }
}
