package com.example.irrigationapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.irrigationapp.R;

public class SettingsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView iconEditProfile, imageProfile;
    private TextView textUsername;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        iconEditProfile = findViewById(R.id.iconEditProfile);
        textUsername = findViewById(R.id.textUsername);
        imageProfile = findViewById(R.id.imageProfile);

        // ✅ Remplacement de l'ancienne méthode dépréciée
        prefs = getSharedPreferences("user_settings", MODE_PRIVATE);

        // Charger les données enregistrées
        loadProfileData();

        imageProfile.setOnClickListener(v -> openGallery());

        iconEditProfile.setOnClickListener(v -> {
            EditText input = new EditText(this);
            input.setText(textUsername.getText());

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Modifier le nom")
                    .setView(input)
                    .setPositiveButton("Valider", (dialog, which) -> {
                        String newUsername = input.getText().toString().trim();
                        if (!newUsername.isEmpty()) {
                            textUsername.setText(newUsername);
                            prefs.edit().putString("username", newUsername).apply();
                            Toast.makeText(this, "Nom modifié", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            prefs.edit().putString("profileImageUri", imageUri.toString()).apply();
            Glide.with(this).load(imageUri).into(imageProfile);
        }
    }

    private void loadProfileData() {
        String name = prefs.getString("username", "Utilisateur");
        textUsername.setText(name);

        String uriStr = prefs.getString("profileImageUri", null);
        if (uriStr != null) {
            Glide.with(this).load(Uri.parse(uriStr)).into(imageProfile);
        }
    }
}
