package com.example.irrigationapp.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordEditText, confirmPasswordEditText;
    private Button validateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordEditText = findViewById(R.id.editTextNewPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        validateButton = findViewById(R.id.buttonValidate);

        String email = getIntent().getStringExtra("email");

        validateButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            } else {
                // Appel à l'API pour changer le mot de passe
                new Thread(() -> {
                    try {
                        URL url = new URL("http://10.0.2.2:8080/api/users/update-password");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        String postData = "email=" + email + "&newPassword=" + newPassword;

                        OutputStream os = conn.getOutputStream();
                        os.write(postData.getBytes());
                        os.flush();
                        os.close();

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String response = in.readLine();
                        in.close();

                        boolean success = response != null && response.trim().equalsIgnoreCase("true");

                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Mot de passe changé avec succès", Toast.LENGTH_LONG).show();
                                finish(); // ou rediriger vers login
                            } else {
                                Toast.makeText(this, "Échec de la mise à jour du mot de passe", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {
                        runOnUiThread(() ->
                                Toast.makeText(this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show());
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
}
