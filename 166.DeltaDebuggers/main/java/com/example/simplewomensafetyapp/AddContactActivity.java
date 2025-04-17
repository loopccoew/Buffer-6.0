package com.example.simplewomensafetyapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    private EditText edtName, edtRelation, edtPhone;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Bind views
        edtName = findViewById(R.id.edtName);
        edtRelation = findViewById(R.id.edtRelation);
        edtPhone = findViewById(R.id.edtPhone);
        btnSave = findViewById(R.id.btnSave);

        // Save button click listener
        btnSave.setOnClickListener(view -> {
            String name = edtName.getText().toString().trim();
            String relation = edtRelation.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            // Validation
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(relation) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Phone number validation (optional, adjust as needed)
            if (!phone.matches("[0-9]{10}")) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to database
            DatabaseHelper db = new DatabaseHelper(this);
            db.addContact(name, relation, phone);
            Toast.makeText(this, "Contact saved successfully!", Toast.LENGTH_SHORT).show();

            finish(); // Close activity
        });
    }
}
