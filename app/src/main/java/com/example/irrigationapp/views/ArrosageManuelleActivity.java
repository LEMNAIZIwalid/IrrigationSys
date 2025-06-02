package com.example.irrigationapp.views;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.irrigationapp.R;

public class ArrosageManuelleActivity extends AppCompatActivity {

    private boolean isOn = true;
    private ImageButton powerButton;
    private TextView statusText, statusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrosage_manuelle);

        powerButton = findViewById(R.id.powerButton);
        statusText = findViewById(R.id.statusText);
        statusMessage = findViewById(R.id.statusMessage);

        // Initialiser à l'état ON (vert)
        powerButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_green_dark));
        statusText.setText("ON");
        statusMessage.setText("Arrosage");

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOn) {
                    // Passer à l'état OFF (rouge)
                    powerButton.setBackgroundTintList(ContextCompat.getColorStateList(ArrosageManuelleActivity.this, android.R.color.holo_red_dark));
                    statusText.setText("OFF");
                    statusMessage.setText("Arrosage OFF");
                } else {
                    // Revenir à l'état ON (vert)
                    powerButton.setBackgroundTintList(ContextCompat.getColorStateList(ArrosageManuelleActivity.this, android.R.color.holo_green_dark));
                    statusText.setText("ON");
                    statusMessage.setText("Arrosage ON");
                }
                isOn = !isOn;
            }
        });
    }
}
