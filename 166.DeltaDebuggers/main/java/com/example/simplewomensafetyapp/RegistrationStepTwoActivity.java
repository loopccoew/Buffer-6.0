package com.example.simplewomensafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegistrationStepTwoActivity extends AppCompatActivity {

    private LinearLayout contactContainer;
    private Button btnAddContact, btnNextTerms;
    private ArrayList<EditText[]> contactInputs = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step_two);

        contactContainer = findViewById(R.id.contactContainer);
        btnAddContact = findViewById(R.id.btnAddContact);
        btnNextTerms = findViewById(R.id.btnNextTerms);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String age = intent.getStringExtra("age");
        String password = intent.getStringExtra("password");

        addContactField();

        btnAddContact.setOnClickListener(v -> addContactField());

        btnNextTerms.setOnClickListener(v -> {
            ArrayList<String> contactList = new ArrayList<>();

            for (EditText[] inputs : contactInputs) {
                String cname = inputs[0].getText().toString().trim();
                String crelation = inputs[1].getText().toString().trim();
                String cphone = inputs[2].getText().toString().trim();

                if (!cname.isEmpty() && !crelation.isEmpty() && !cphone.isEmpty()) {
                    dbHelper.addContact(cname, crelation, cphone);
                    contactList.add(cname + "," + crelation + "," + cphone);
                }
            }

            if (contactList.size() == 0) {
                Toast.makeText(this, "Please enter at least one emergency contact", Toast.LENGTH_SHORT).show();
                return;
            }


            Intent nextIntent = new Intent(RegistrationStepTwoActivity.this, RegistrationTermsActivity.class);
            nextIntent.putExtra("userId", userId);
            nextIntent.putExtra("name", name);
            nextIntent.putExtra("email", email);
            nextIntent.putExtra("phone", phone);
            nextIntent.putExtra("address", address);
            nextIntent.putExtra("age", age);
            nextIntent.putExtra("password", password);
            nextIntent.putStringArrayListExtra("contacts", contactList);
            startActivity(nextIntent);
        });
    }

    private void addContactField() {
        EditText etName = new EditText(this);
        etName.setHint("Contact Name");
        contactContainer.addView(etName);

        EditText etRelation = new EditText(this);
        etRelation.setHint("Relation");
        contactContainer.addView(etRelation);

        EditText etPhone = new EditText(this);
        etPhone.setHint("Contact Phone");
        etPhone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        contactContainer.addView(etPhone);

        contactInputs.add(new EditText[]{etName, etRelation, etPhone});
    }
}





