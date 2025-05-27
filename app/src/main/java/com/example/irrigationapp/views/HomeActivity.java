package com.example.irrigationapp.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
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
