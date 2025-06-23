//package com.example.irrigationapp.views;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.irrigationapp.R;
//
//public class ArrosageManuelleActivity extends AppCompatActivity {
//
//    private boolean isOn = false; // État initial = OFF
//    private ImageButton powerButton;
//    private TextView statusText, statusMessage;
//
//    // Remplace cette IP par celle affichée dans le moniteur série de l'ESP32
//    private final String esp32IP = "http://192.168.125.31"; // Exemple : http://192.168.1.42
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_arrosage_manuelle);
//
//        powerButton = findViewById(R.id.powerButton);
//        statusText = findViewById(R.id.statusText);
//        statusMessage = findViewById(R.id.statusMessage);
//
//        updateUI(isOn); // mettre à jour l'état initial (OFF)
//
//
//        powerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isOn = !isOn; // inverser l’état
//                updateUI(isOn); // mettre à jour l'interface
//                sendCommandToESP32(isOn); // envoyer la commande à l'ESP32
//            }
//        });
//    }
//
//    private void updateUI(boolean on) {
//        if (on) {
//            powerButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_green_dark));
//            statusText.setText("ON");
//            statusMessage.setText("Pompe activée");
//        } else {
//            powerButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_dark));
//            statusText.setText("OFF");
//            statusMessage.setText("Pompe désactivée");
//        }
//    }
//
//    private void sendCommandToESP32(boolean turnOn) {
//        String endpoint = turnOn ? "/on" : "/off";
//        String url = esp32IP + endpoint;
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                response -> Toast.makeText(this, "Réponse ESP32 : " + response, Toast.LENGTH_SHORT).show(),
//                error -> {
//                    Toast.makeText(this, "Erreur de connexion à l’ESP32", Toast.LENGTH_SHORT).show();
//                    error.printStackTrace();
//                });
//
//        queue.add(request);
//    }
//}


package com.example.irrigationapp.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.irrigationapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArrosageManuelleActivity extends AppCompatActivity {

    private boolean isOn = false;
    private ImageButton powerButton;
    private TextView statusText, statusMessage;

    private final String esp32IP = "http://192.168.125.31";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrosage_manuelle);

        powerButton = findViewById(R.id.powerButton);
        statusText = findViewById(R.id.statusText);
        statusMessage = findViewById(R.id.statusMessage);

        updateUI(isOn);

        powerButton.setOnClickListener(view -> {
            isOn = !isOn;
            updateUI(isOn);
            sendCommandToESP32(isOn);

            // ➕ Ajouter une notification dans SharedPreferences
            String action = isOn ? "Arrosage manuelle ON" : "Arrosage manuelle OFF";
            addNotification(action);
        });
    }

    private void updateUI(boolean on) {
        if (on) {
            powerButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_green_dark));
            statusText.setText("ON");
            statusMessage.setText("Pompe activée");
        } else {
            powerButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_dark));
            statusText.setText("OFF");
            statusMessage.setText("Pompe désactivée");
        }
    }

    private void sendCommandToESP32(boolean turnOn) {
        String endpoint = turnOn ? "/on" : "/off";
        String url = esp32IP + endpoint;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> Toast.makeText(this, "Réponse ESP32 : " + response, Toast.LENGTH_SHORT).show(),
                error -> {
                    Toast.makeText(this, "Erreur de connexion à l’ESP32", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        queue.add(request);
    }

    private void addNotification(String message) {
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        String newEntry = "" + message + "" + "             " + time + "\n" +
                date + "\n\n";

        SharedPreferences prefs = getSharedPreferences("notifications", MODE_PRIVATE);
        String oldLogs = prefs.getString("logs", "");
        String updatedLogs = newEntry + oldLogs;

        prefs.edit().putString("logs", updatedLogs).apply();
    }
}
