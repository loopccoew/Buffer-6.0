package com.example.simplewomensafetyapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class BulkMessageActivity extends AppCompatActivity {
    Button sendAlertButton;
    FusedLocationProviderClient fusedLocationClient;
    final int REQUEST_CODE = 1;

    // Initialize Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_bulk_message); // Ensure this layout exists with correct ID

        sendAlertButton = findViewById(R.id.btnSendBulk);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_CODE);

        sendAlertButton.setOnClickListener(v -> sendEmergencySMS());
    }

    private void sendEmergencySMS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            String alertMessage = "\uD83D\uDEA8 I am in danger. Please help me!";
            String locationMessage = "Location not available";

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                locationMessage = "\uD83C\uDF0D My location: https://maps.google.com/?q=" + latitude + "," + longitude;
            }

            try {
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> emergencyContacts = DatabaseHelper.getStoredContacts(BulkMessageActivity.this);
                for (String contact : emergencyContacts) {
                    smsManager.sendTextMessage(contact, null, alertMessage, null, null);
                    smsManager.sendTextMessage(contact, null, locationMessage, null, null);
                }
                Toast.makeText(BulkMessageActivity.this, "Alert and location sent!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(BulkMessageActivity.this, "SMS failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
