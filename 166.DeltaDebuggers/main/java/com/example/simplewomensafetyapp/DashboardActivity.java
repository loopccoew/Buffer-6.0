package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnMap, btnBulkMessage, btnLogout , fakeCallBtn , btnSigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnMap = findViewById(R.id.btnDashboardMap);
        btnBulkMessage = findViewById(R.id.btnDashboardBulkMessage);
        btnLogout = findViewById(R.id.btnDashboardLogout);


        fakeCallBtn = findViewById(R.id.fakeCallButton);

        fakeCallBtn.setOnClickListener(v -> {
            // Show message or toast here if you want
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(DashboardActivity.this, FakeCallActivity.class);
                startActivity(intent);
            }, 5000); // 5-second delay
        });

        btnSigns = findViewById(R.id.btnDashboardSigns);
        btnSigns.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, Sign.class);
            startActivity(intent);
        });


        btnBulkMessage = findViewById(R.id.btnDashboardBulkMessage);
        btnBulkMessage.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, BulkMessageActivity.class)));


        btnMap.setOnClickListener(v -> startActivity(new Intent(this, MapsActivity.class)));


        btnLogout.setOnClickListener(v -> {
            // Show logout message
            Toast.makeText(DashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

            // Redirect to MainActivity
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finish DashboardActivity so user can't come back with back button
        });

    }
}
