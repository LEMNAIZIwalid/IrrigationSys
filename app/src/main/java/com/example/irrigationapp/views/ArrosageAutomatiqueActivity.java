package com.example.irrigationapp.views;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.irrigationapp.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ArrosageAutomatiqueActivity extends AppCompatActivity {

    private LinearLayout scheduleList;
    private TextView emptyMessage;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "SchedulePrefs";
    private static final String KEY_SCHEDULES = "schedules";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrosage_automatique);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        scheduleList = findViewById(R.id.scheduleList);
        emptyMessage = findViewById(R.id.emptyMessage);
        ImageButton addButton = findViewById(R.id.addScheduleButton);

        addButton.setOnClickListener(v -> openAddScheduleDialog(null));

        loadSchedules();
    }

    private void openAddScheduleDialog(View existingCard) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_schedule, null);
        TimePicker startPicker = dialogView.findViewById(R.id.startTimePicker);
        TimePicker endPicker = dialogView.findViewById(R.id.endTimePicker);
        startPicker.setIs24HourView(true);
        endPicker.setIs24HourView(true);

        String oldSchedule = null;

        if (existingCard != null) {
            TextView timeRange = existingCard.findViewById(R.id.timeRangeText);
            String[] parts = timeRange.getText().toString().replace("De ", "").replace(" à ", "-").split("-");
            String[] start = parts[0].split(":");
            String[] end = parts[1].split(":");

            startPicker.setHour(Integer.parseInt(start[0]));
            startPicker.setMinute(Integer.parseInt(start[1]));
            endPicker.setHour(Integer.parseInt(end[0]));
            endPicker.setMinute(Integer.parseInt(end[1]));

            oldSchedule = formatSchedule(
                    Integer.parseInt(start[0]),
                    Integer.parseInt(start[1]),
                    Integer.parseInt(end[0]),
                    Integer.parseInt(end[1])
            );
        }

        String finalOldSchedule = oldSchedule;

        new AlertDialog.Builder(this)
                .setTitle(existingCard == null ? "Ajouter un arrosage" : "Modifier l'arrosage")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    int startH = startPicker.getHour();
                    int startM = startPicker.getMinute();
                    int endH = endPicker.getHour();
                    int endM = endPicker.getMinute();

                    String newSchedule = formatSchedule(startH, startM, endH, endM);

                    if (isDuplicateSchedule(newSchedule, finalOldSchedule)) {
                        new AlertDialog.Builder(this)
                                .setTitle("Erreur")
                                .setMessage("Cet arrosage existe déjà.")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        if (existingCard != null) {
                            scheduleList.removeView(existingCard);
                            removeSchedule(finalOldSchedule);
                        }
                        addScheduleCard(startH, startM, endH, endM);
                        saveSchedule(newSchedule);
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private boolean isDuplicateSchedule(String newSchedule, String except) {
        String json = sharedPreferences.getString(KEY_SCHEDULES, "[]");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                String schedule = array.getString(i);
                if (schedule.equals(newSchedule) && (except == null || !schedule.equals(except))) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveSchedule(String schedule) {
        String json = sharedPreferences.getString(KEY_SCHEDULES, "[]");
        try {
            JSONArray array = new JSONArray(json);
            array.put(schedule);
            sharedPreferences.edit().putString(KEY_SCHEDULES, array.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void removeSchedule(String schedule) {
        String json = sharedPreferences.getString(KEY_SCHEDULES, "[]");
        try {
            JSONArray array = new JSONArray(json);
            JSONArray updatedArray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                if (!array.getString(i).equals(schedule)) {
                    updatedArray.put(array.getString(i));
                }
            }
            sharedPreferences.edit().putString(KEY_SCHEDULES, updatedArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addScheduleCard(int startH, int startM, int endH, int endM) {
        emptyMessage.setVisibility(View.GONE);

        String scheduleString = formatSchedule(startH, startM, endH, endM);

        View card = LayoutInflater.from(this).inflate(R.layout.item_schedule_card, scheduleList, false);
        TextView timeRange = card.findViewById(R.id.timeRangeText);
        ImageButton deleteBtn = card.findViewById(R.id.deleteButton);
        ImageButton editBtn = card.findViewById(R.id.editButton);

        timeRange.setText(scheduleString);

        deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmer la suppression")
                    .setMessage("Voulez-vous vraiment supprimer cet arrosage ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        scheduleList.removeView(card);
                        removeSchedule(scheduleString);
                        if (scheduleList.getChildCount() == 0)
                            emptyMessage.setVisibility(View.VISIBLE);
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });

        editBtn.setOnClickListener(v -> openAddScheduleDialog(card));

        scheduleList.addView(card);
    }

    private void loadSchedules() {
        String json = sharedPreferences.getString(KEY_SCHEDULES, "[]");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                String[] parts = array.getString(i).replace("De ", "").replace(" à ", "-").split("-");
                String[] start = parts[0].split(":");
                String[] end = parts[1].split(":");
                addScheduleCard(
                        Integer.parseInt(start[0]), Integer.parseInt(start[1]),
                        Integer.parseInt(end[0]), Integer.parseInt(end[1])
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String formatSchedule(int startH, int startM, int endH, int endM) {
        return String.format("De %02d:%02d à %02d:%02d", startH, startM, endH, endM);
    }
}
