package com.example.irrigationapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.irrigationapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private TextView welcomeText, dateText, weatherText;
    private ImageView profileImage, weatherIcon;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prefs = getSharedPreferences("user_settings", MODE_PRIVATE);

        welcomeText = findViewById(R.id.welcomeText);
        profileImage = findViewById(R.id.profileImage);
        dateText = findViewById(R.id.dateText);
        weatherText = findViewById(R.id.weatherText);
        weatherIcon = findViewById(R.id.weatherIcon);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Affichage de la date actuelle
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        dateText.setText(date);

        // Simuler la météo (tu peux remplacer par un appel API météo réel)
//        int temp = 26; // simulateur
//        if (temp > 30) {
//            weatherText.setText("Soleil");
//            weatherIcon.setImageResource(R.drawable.sun);
//        } else if (temp > 15) {
//            weatherText.setText("Nuageux");
//            weatherIcon.setImageResource(R.drawable.cloudyday);
//        } else if (temp > 5) {
//            weatherText.setText("Nuages");
//            weatherIcon.setImageResource(R.drawable.clouds);
//        } else {
//            weatherText.setText("Neige");
//            weatherIcon.setImageResource(R.drawable.snowflake);
//        }

        // Cliquer sur la photo → Aller aux paramètres
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Navigation bas
        bottomNav.setOnItemSelectedListener(navListener);
    }

    // ✅ Recharger les données utilisateur quand on revient sur cette page
    @Override
    protected void onResume() {
        super.onResume();

        String username = prefs.getString("username", "Utilisateur");
        welcomeText.setText(username);

        String uriStr = prefs.getString("profileImageUri", null);
        if (uriStr != null) {
            Glide.with(this).load(Uri.parse(uriStr)).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.logo_user);
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_notification:
                            startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                            return true;
                        case R.id.nav_settings:
                            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                            return true;
                        case R.id.nav_control:
                            startActivity(new Intent(HomeActivity.this, ArrosageControlActivity.class));
                            return true;
                        case R.id.nav_stats:
                            startActivity(new Intent(HomeActivity.this, StatistiquesActivity.class));
                            return true;
                    }
                    return false;
                }
            };
}
