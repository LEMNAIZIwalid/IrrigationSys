package com.example.irrigationapp.views;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.irrigationapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StatistiquesActivity extends AppCompatActivity {

    private LinearLayout chartContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        chartContainer = findViewById(R.id.chartContainer);
        loadSensorDataForToday();
    }

    private void loadSensorDataForToday() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String apiUrl = "http://10.0.2.2:8080/api/users/sensorData/day/" + today;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        ArrayList<Entry> tempEntries = new ArrayList<>();
                        ArrayList<Entry> humAirEntries = new ArrayList<>();
                        ArrayList<Entry> humSolEntries = new ArrayList<>();
                        ArrayList<Entry> lightEntries = new ArrayList<>();
                        ArrayList<Entry> waterEntries = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String timestamp = obj.getString("timestamp");
                            int hour = Integer.parseInt(timestamp.substring(11, 13));

                            tempEntries.add(new Entry(hour, (float) obj.getDouble("temperature")));
                            humAirEntries.add(new Entry(hour, (float) obj.getDouble("humidite")));
                            humSolEntries.add(new Entry(hour, (float) obj.getDouble("solHumidite")));
                            lightEntries.add(new Entry(hour, obj.getInt("light")));
                            waterEntries.add(new Entry(hour, obj.getInt("waterLevel")));
                        }

                        createChart("🌡 Température (°C)", tempEntries, Color.parseColor("#E53935"));
                        createChart("💧 Humidité Air (%)", humAirEntries, Color.parseColor("#1E88E5"));
                        createChart("🌱 Humidité Sol (%)", humSolEntries, Color.parseColor("#43A047"));
                        createChart("☀️ Lumière (lx)", lightEntries, Color.parseColor("#FFB300"));
                        createChart("🪣 Niveau Eau (%)", waterEntries, Color.parseColor("#00ACC1"));

                    } catch (Exception e) {
                        Log.e("ChartError", "Parsing error", e);
                    }
                },
                error -> Log.e("API", "Failed to fetch data", error)
        );

        queue.add(request);
    }

    private void createChart(String label, ArrayList<Entry> entries, int color) {
        LineChart chart = new LineChart(this);
        chart.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                600));
        chart.setPadding(0, 40, 0, 40);

        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleColor(color);
        dataSet.setDrawValues(false);

        // Configuration du graphique
        chart.setData(new LineData(dataSet));
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.animateX(1200);

        // Axe X (heure)
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(23f);
        xAxis.setLabelCount(6, true);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.DKGRAY);

        // Axe Y gauche
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(Color.DKGRAY);
        yAxis.setTextSize(12f);
        yAxis.setDrawGridLines(true);

        // Désactiver l'axe Y droit
        chart.getAxisRight().setEnabled(false);

        // Légende
        Legend legend = chart.getLegend();
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(14f);
        legend.setForm(Legend.LegendForm.LINE);

        // Rafraîchir
        chart.invalidate();

        chartContainer.addView(chart);
    }
}
