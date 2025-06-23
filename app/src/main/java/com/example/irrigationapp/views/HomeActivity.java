package com.example.irrigationapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.irrigationapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

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

        // Initialisation des vues
        welcomeText = findViewById(R.id.welcomeText);
        profileImage = findViewById(R.id.profileImage);
        dateText = findViewById(R.id.dateText);
        weatherText = findViewById(R.id.weatherText);
        weatherIcon = findViewById(R.id.weatherIcon);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Afficher la date
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        dateText.setText(currentDate);

        // Navigation barre du bas
        bottomNav.setOnItemSelectedListener(navListener);

        // Aller aux paramètres en cliquant sur la photo
        profileImage.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Mettre à jour le nom
        String username = prefs.getString("username", "Utilisateur");
        welcomeText.setText(username);

        // Mettre à jour l'image
        String uriStr = prefs.getString("profileImageUri", null);
        if (uriStr != null) {
            Glide.with(this).load(Uri.parse(uriStr)).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.logo_user);
        }

        // Charger météo depuis OpenWeatherMap
        getWeatherData();
    }

    private void getWeatherData() {
        String apiKey = "dc574f17c624770a10434935e6af58c3"; // 🔁 remplace par ta vraie clé API
        String city = "Agadir";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        double temp = main.getDouble("temp");

                        // Afficher la température
                        weatherText.setText(String.format(Locale.getDefault(), "%.1f°C", temp));

                        // Choisir une icône selon la température
                        if (temp > 30) {
                            weatherIcon.setImageResource(R.drawable.sun);
                        } else if (temp > 15) {
                            weatherIcon.setImageResource(R.drawable.cloudyday);
                        } else if (temp > 5) {
                            weatherIcon.setImageResource(R.drawable.clouds);
                        } else {
                            weatherIcon.setImageResource(R.drawable.snowflake);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
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
