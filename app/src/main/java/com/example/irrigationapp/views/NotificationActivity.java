package com.example.irrigationapp.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;

public class NotificationActivity extends AppCompatActivity {

    private TextView notificationLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationLog = findViewById(R.id.notificationLog);

        SharedPreferences prefs = getSharedPreferences("notifications", MODE_PRIVATE);
        String logs = prefs.getString("logs", "Aucune notification");
        notificationLog.setText(logs);
    }
}
