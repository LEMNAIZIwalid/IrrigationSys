package com.example.irrigationapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout sensorDataLayout;

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
        sensorDataLayout = findViewById(R.id.sensorDataLayout);

        // Afficher la date actuelle
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        dateText.setText(currentDate);

        // Navigation bas
        bottomNav.setOnItemSelectedListener(navListener);

        // Redirection paramètres
        profileImage.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Affichage du nom d'utilisateur
        String username = prefs.getString("username", "Utilisateur");
        welcomeText.setText(username);

        // Affichage de la photo
        String uriStr = prefs.getString("profileImageUri", null);
        if (uriStr != null) {
            Glide.with(this).load(Uri.parse(uriStr)).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.logo_user);
        }

        getWeatherData();
        loadSensorDataForCurrentTime();
    }

    private void getWeatherData() {
        String apiKey = "dc574f17c624770a10434935e6af58c3"; // ← Remplace ta vraie clé si nécessaire
        String city = "Agadir";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        double temp = main.getDouble("temp");

                        weatherText.setText(String.format(Locale.getDefault(), "%.1f°C", temp));

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

    private void loadSensorDataForCurrentTime() {
        String apiUrl = "http://10.0.2.2:8080/api/users/sensorData/current";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        sensorDataLayout.removeAllViews();
                        android.util.Log.d("SensorDataAPI", response.toString());

                        double temperature = response.getDouble("temperature");
                        double humidite = response.getDouble("humidite");
                        double solHumidite = response.getDouble("solHumidite");
                        int waterLevel = response.getInt("waterLevel");
                        int light = response.getInt("light");

                        addLine("🌡 Température : " + temperature + " °C");
                        addLine("💧 Humidité air : " + humidite + " %");
                        addLine("🌱 Humidité sol : " + solHumidite + " %");
                        addLine("🪣 Niveau eau : " + waterLevel + " %");
                        addLine("☀️ Lumière : " + light + " lx");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
    }

    private void addLine(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setPadding(12, 10, 12, 10);
        tv.setGravity(Gravity.START);
        tv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        sensorDataLayout.addView(tv);
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
