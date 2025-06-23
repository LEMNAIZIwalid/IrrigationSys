package com.example.irrigationapp.model;

public class SensorData {
    private double temperature;
    private double humidity;
    private double soilHumidity;
    private int waterLevel;
    private int light;
    private String timestamp;

    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getSoilHumidity() { return soilHumidity; }
    public int getWaterLevel() { return waterLevel; }
    public int getLight() { return light; }
    public String getTimestamp() { return timestamp; }
}
