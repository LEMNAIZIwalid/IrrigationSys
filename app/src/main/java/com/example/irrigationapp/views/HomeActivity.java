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

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        dateText.setText(currentDate);

        bottomNav.setOnItemSelectedListener(navListener);

        profileImage.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        });
    }

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

        getWeatherData();
        loadSensorDataForCurrentTime();
    }

    private void getWeatherData() {
        String apiKey = "dc574f17c624770a10434935e6af58c3";
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

                        double temperature = response.getDouble("temperature");
                        double humidite = response.getDouble("humidite");
                        double solHumidite = response.getDouble("solHumidite");
                        int waterLevel = response.getInt("waterLevel");
                        int light = response.getInt("light");

                        addSensorCard(R.drawable.thermometer, "Température", temperature + " °C");
                        addSensorCard(R.drawable.humidity, "Humidité de l'air", humidite + " %");
                        addSensorCard(R.drawable.soilhealth, "Humidité du sol", solHumidite + " %");
                        addSensorCard(R.drawable.sealevel, "Niveau d'eau", waterLevel + " %");
                        addSensorCard(R.drawable.editing, "Lumière", light + " lx");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());

        queue.add(request);
    }

    private void addSensorCard(int iconResId, String label, String value) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setPadding(24, 20, 24, 20);
        card.setBackgroundResource(R.drawable.sensor_card_bg);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        card.setElevation(6);

        ImageView icon = new ImageView(this);
        icon.setImageResource(iconResId);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(80, 80);
        iconParams.setMarginEnd(24);
        icon.setLayoutParams(iconParams);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(this);
        title.setText(label);
        title.setTextSize(16);
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView valueText = new TextView(this);
        valueText.setText(value);
        valueText.setTextSize(18);
        valueText.setTextColor(getResources().getColor(R.color.black));

        textLayout.addView(title);
        textLayout.addView(valueText);

        card.addView(icon);
        card.addView(textLayout);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 16, 0, 0);
        sensorDataLayout.addView(card, cardParams);
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
