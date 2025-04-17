package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BifurcationActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.bifurcation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void openMainActivity3(View v)
    {
        Toast.makeText(this,"Loading",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainActivity3.class);
        startActivity(intent);
    }

    public void openPriorityActivity(View v)
    {
        Toast.makeText(this,"Loading",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,PriorityActivity.class);
        startActivity(intent);
    }
}