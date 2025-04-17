package com.example.simplewomensafetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HelpCenterDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center_detail);

        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phone");
        double lat = getIntent().getDoubleExtra("latitude", 0);
        double lng = getIntent().getDoubleExtra("longitude", 0);
        double userLat = getIntent().getDoubleExtra("userLat", 0);
        double userLng = getIntent().getDoubleExtra("userLng", 0);

        // Calculate distance
        float[] result = new float[1];
        Location.distanceBetween(userLat, userLng, lat, lng, result);
        float distanceInMeters = result[0];
        float distanceInKm = distanceInMeters / 1000;

        // Now set text on TextViews (you should create them in XML)
        TextView nameView = findViewById(R.id.tvHelpCenterName);
        TextView addrView = findViewById(R.id.tvHelpCenterAddress);
        TextView distView = findViewById(R.id.tvHelpCenterDistance);
        TextView phoneView = findViewById(R.id.tvHelpCenterContact);
        Button callBtn = findViewById(R.id.btnCall);
        Button smsBtn = findViewById(R.id.btnSendSMS);

        nameView.setText(name);
        addrView.setText(address);
        distView.setText(String.format("Distance: %.2f km", distanceInKm));
        phoneView.setText(phone);

        callBtn.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            }
        });

        smsBtn.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:" + phone));
            smsIntent.putExtra("sms_body", "Hi, I need help. Please assist.");
            startActivity(smsIntent);
        });
    }
}
