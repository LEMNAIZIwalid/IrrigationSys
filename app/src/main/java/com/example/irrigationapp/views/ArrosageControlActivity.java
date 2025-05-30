package com.example.irrigationapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;

public class ArrosageControlActivity extends AppCompatActivity {

    private Button buttonManual, buttonAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrosage_control);

        buttonManual = findViewById(R.id.buttonManual);
        buttonAuto = findViewById(R.id.buttonAuto);

        buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aller vers la page d'arrosage manuelle
                startActivity(new Intent(ArrosageControlActivity.this, ArrosageManuelleActivity.class));
            }
        });

        buttonAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aller vers la page d'arrosage automatique
                startActivity(new Intent(ArrosageControlActivity.this, ArrosageAutomatiqueActivity.class));
            }
        });
    }
}
