package com.example.irrigationapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.irrigationapp.R;

public class SettingsActivity extends AppCompatActivity {

    private ImageView iconEditProfile;
    private TextView textUsername;
    private ImageView imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        iconEditProfile = findViewById(R.id.iconEditProfile);
        textUsername = findViewById(R.id.textUsername);
        imageProfile = findViewById(R.id.imageProfile);

        // Affichage du nom utilisateur (à adapter selon ton backend)
        textUsername.setText("Utilisateur");

        // Rediriger vers la page de gestion du profil
        iconEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileManagementActivity.class);
            startActivity(intent);
        });
    }
}
