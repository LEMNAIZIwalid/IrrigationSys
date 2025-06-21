package com.example.irrigationapp.views;

import android.content.Intent;
import android.os.Bundle;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText, phoneEditText;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.editTextEmail);
        phoneEditText = findViewById(R.id.editTextPhone);
        verifyButton = findViewById(R.id.buttonVerify);

        verifyButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    URL url = new URL("http://10.0.2.2:8080/api/users/verify");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    String postData = "email=" + email + "&phoneNumber=" + phone;
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
                            Toast.makeText(this, "Utilisateur vérifié. Redirection...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("email", email); // 👈 Passe l'email à l'activité suivante
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Email ou numéro incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
