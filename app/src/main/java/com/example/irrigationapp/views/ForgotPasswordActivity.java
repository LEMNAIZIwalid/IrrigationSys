package com.example.irrigationapp.views;

import android.content.Intent; // 🔧 Ajout de l'import manquant
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText, phoneEditText;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Liaison des vues
        emailEditText = findViewById(R.id.editTextEmail);
        phoneEditText = findViewById(R.id.editTextPhone);
        verifyButton = findViewById(R.id.buttonVerify);

        // Vérification simple (à remplacer par une vérification réelle en base de données)
        verifyButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (email.equals("admin@gmail.com") && phone.equals("0600112233")) {
                Toast.makeText(this, "Utilisateur vérifié. Redirection...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                finish(); // Facultatif : empêche retour à cette page
            } else {
                Toast.makeText(this, "Utilisateur introuvable.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
