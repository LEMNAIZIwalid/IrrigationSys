package com.example.irrigationapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;
import com.example.irrigationapp.controller.LoginController;
import com.example.irrigationapp.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginBtn;
    private TextView forgotPasswordText;

    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        loginController = new LoginController();

        loginBtn.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(username, password);

            loginController.login(user, new LoginController.LoginCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
                }
            });
        });

        forgotPasswordText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }
}
