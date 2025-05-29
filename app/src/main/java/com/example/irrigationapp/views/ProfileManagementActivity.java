package com.example.irrigationapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;

public class ProfileManagementActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPhone, editTextCurrentPassword;
    private Button buttonUpdateProfile, buttonChangePassword, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonUpdateProfile.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String currentPassword = editTextCurrentPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(phone) || TextUtils.isEmpty(currentPassword)) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // TODO : Mettre à jour les données via RMI ou API
                Toast.makeText(this, "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        buttonChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileManagementActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            // TODO : Nettoyer la session / token
            Toast.makeText(this, "Déconnecté", Toast.LENGTH_SHORT).show();
            finish(); // ou redirection vers LoginActivity
        });
    }
}
