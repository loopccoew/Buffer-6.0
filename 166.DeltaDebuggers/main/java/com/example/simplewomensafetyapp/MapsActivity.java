package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnSuccessListener;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng userLocation;
    private List<HelpCenter> helpCenters = new ArrayList<>();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadHelpCenters(); // Load from assets
    }

    private void loadHelpCenters() {
        try {
            InputStream is = getAssets().open("help_center.json");
            Reader reader = new InputStreamReader(is);
            StringBuilder stringBuilder = new StringBuilder();
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                stringBuilder.append(current);
                data = reader.read();
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("help_centers");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject helpCenterJson = jsonArray.getJSONObject(i);
                HelpCenter helpCenter = new HelpCenter(
                        helpCenterJson.getString("name"),
                        helpCenterJson.getString("address"),
                        helpCenterJson.getString("phone"),
                        helpCenterJson.getDouble("latitude"),
                        helpCenterJson.getDouble("longitude")
                );
                helpCenters.add(helpCenter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading help centers", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // UPDATED METHOD: Replaces original distanceBetween() method using haversine formula
    private double distanceBetween(LatLng l1, LatLng l2) {
        double R = 6371000; // Radius of Earth in meters
        double lat1Rad = Math.toRadians(l1.latitude);
        double lat2Rad = Math.toRadians(l2.latitude);
        double deltaLat = Math.toRadians(l2.latitude - l1.latitude);
        double deltaLng = Math.toRadians(l2.longitude - l1.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // returns distance in meters
    }

    private void addNearestHelpCenterMarkers() {
        if (userLocation == null) return;

        if (userLocation != null && helpCenters != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                helpCenters.sort((h1, h2) -> {
                    double d1 = distanceBetween(userLocation, new LatLng(h1.getLatitude(), h1.getLongitude()));
                    double d2 = distanceBetween(userLocation, new LatLng(h2.getLatitude(), h2.getLongitude()));
                    return Double.compare(d1, d2);
                });
            }
        }

        for (int i = 0; i < Math.min(5, helpCenters.size()); i++) {
            HelpCenter hc = helpCenters.get(i);
            LatLng loc = new LatLng(hc.getLatitude(), hc.getLongitude());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title(hc.getName())
                    .snippet("Tap for contact")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            marker.setTag(hc);
        }

        mMap.setOnInfoWindowClickListener(marker -> {
            HelpCenter selected = (HelpCenter) marker.getTag();
            if (selected != null) {
                Intent intent = new Intent(MapsActivity.this, HelpCenterDetailActivity.class);
                intent.putExtra("name", selected.getName());
                intent.putExtra("address", selected.getAddress());
                intent.putExtra("phone", selected.getPhone());
                intent.putExtra("latitude", selected.getLatitude());
                intent.putExtra("longitude", selected.getLongitude());
                intent.putExtra("userLat", userLocation.latitude);
                intent.putExtra("userLng", userLocation.longitude);
                startActivity(intent);
            }
        });

    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true); // default blue dot

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                // Move and zoom camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));

                // Add custom blue marker
                mMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));  // Blue marker

                addNearestHelpCenterMarkers();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Toast.makeText(this, "Location permission is required to show your location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
